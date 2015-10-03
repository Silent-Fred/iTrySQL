/*
 * Copyright (c) 2015, Michael Kühweg
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package de.kuehweg.gamification;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;

import de.kuehweg.gamification.xml.XmlAchievementCounter;
import de.kuehweg.gamification.xml.XmlAchievementCounters;

/**
 * @author Michael Kühweg
 */
public class AchievementXmlFilePersister implements AchievementPersister {

	private final String filename;

	/**
	 * @param filename
	 *            Dateiname, unter dem der Stand der Achievements abgelegt wird.
	 */
	public AchievementXmlFilePersister(final String filename) {
		this.filename = filename;
	}

	@Override
	public Collection<AchievementCounter> read() {
		JAXBContext context;
		try {
			final byte[] data = readFromFile(filename);
			final ByteArrayInputStream input = new ByteArrayInputStream(data);
			context = JAXBContext.newInstance(XmlAchievementCounters.class);
			final XmlAchievementCounters counters = (XmlAchievementCounters) context.createUnmarshaller()
					.unmarshal(input);
			return countersFromXml(counters);
		} catch (final Exception e) {
			// unschön, aber derzeit sind Achievements nicht "mission critical",
			// ein einfacher Eintrag im Log reicht
			Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
			return Collections.emptyList();
		}
	}

	@Override
	public void persist(final Collection<Achievement> achievements) {
		final XmlAchievementCounters counters = countersToXml(achievements);
		try {
			final JAXBContext context = JAXBContext.newInstance(XmlAchievementCounters.class);
			final ByteArrayOutputStream output = new ByteArrayOutputStream();
			context.createMarshaller().marshal(counters, output);
			writeToFile(filename, output.toByteArray());
		} catch (final Exception e) {
			// unschön, aber derzeit sind Achievements nicht "mission critical",
			// ein einfacher Eintrag im Log reicht deshalb aus
			Logger.getLogger(this.getClass().getSimpleName()).warning(e.getLocalizedMessage());
		}
	}

	private Collection<AchievementCounter> countersFromXml(final XmlAchievementCounters xmlCounters) {
		final Collection<AchievementCounter> counters = new LinkedList<>();
		for (final XmlAchievementCounter xmlCounter : xmlCounters.getCounters()) {
			counters.add(
					new AchievementCounter(new AchievementEvent(xmlCounter.getUniqueId()), xmlCounter.getCounter()));
		}
		return counters;
	}

	private XmlAchievementCounters countersToXml(final Collection<Achievement> achievements) {
		final XmlAchievementCounters counters = new XmlAchievementCounters();
		counters.setCounters(new LinkedList<>());
		for (final AchievementCounter max : getMaxCountPerEvent(achievements)) {
			final XmlAchievementCounter counter = new XmlAchievementCounter();
			counter.setUniqueId(max.getEvent().getUniqueId());
			counter.setCounter(max.getCounter());
			counters.getCounters().add(counter);
		}
		return counters;
	}

	/**
	 * Ermittelt die maximal erreichte Anzahl eines Events über alle
	 * Achievements. Wenn alle Achievements, die ein Event verarbeiten, erreicht
	 * sind, dann bleibt das Maximum für dieses Event konstant. Vorher können
	 * manche Achivements bereits erreicht sein, das Event wird aber noch von
	 * anderen Achievements verarbeitet, so dass unterschiedliche Zählerstände
	 * für das Event existieren.
	 *
	 * Insgesamt wird kein reiner Event-Zähler verwendet, um für Events mit
	 * hohen Zählwerten - z.B. Ergebniszeilen zählen o.ä. - sicher kein Überlauf
	 * entstehen kann. Egal wie unwahrscheinlich das bei einem Long-Wert auch
	 * sein mag ;-)
	 *
	 * @param achievements
	 *            Menge der Achievements, die ausgewertet werden
	 * @return AchievementCounter für jedes AchievementEvent mit dem jeweils
	 *         höchsten erreichten Stand. Jedes AchievementEvent ist in der
	 *         Ergebnismenge nur ein einziges Mal enthalten.
	 */
	private Collection<AchievementCounter> getMaxCountPerEvent(final Collection<Achievement> achievements) {
		final Map<AchievementEvent, AchievementCounter> maxCountPerEvent = new HashMap<>();
		for (final Achievement achievement : achievements) {
			for (final AchievementCounter counter : achievement.achieved()) {
				AchievementCounter max = maxCountPerEvent.get(counter.getEvent());
				if (max == null) {
					max = new AchievementCounter(counter.getEvent(), 0);
					maxCountPerEvent.put(max.getEvent(), max);
				}
				if (counter.getCounter() > max.getCounter()) {
					max.setCounter(counter.getCounter());
				}
			}
		}
		return new ArrayList<>(maxCountPerEvent.values());
	}

	private void writeToFile(final String filename, final byte[] data) throws Exception {
		Files.write(new File(filename).toPath(), prepareForPersist(data), StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
	}

	private byte[] readFromFile(final String filename) throws Exception {
		return prepareAfterRead(Files.readAllBytes(new File(filename).toPath()));
	}

	/**
	 * Andockstelle für abgeleitete Klassen, um die Daten z.B. verschlüsselt zu
	 * speichern. Wird nach der Aufbereitung der Daten direkt vor dem Speichern
	 * aufgerufen.
	 *
	 * @param data
	 *            Das vorbereitete XML als Byte-Array
	 * @return Die Daten, die als Byte-Array persistiert werden sollen.
	 * @throws Exception
	 */
	protected byte[] prepareForPersist(final byte[] data) throws Exception {
		return Arrays.copyOf(data, data.length);
	}

	/**
	 * Andockstelle für abgeleitete Klassen, um z.B. verschlüsselt abgelegte
	 * Daten nach dem Lesen für die Verarbeitung vorzubereiten. Wird direkt nach
	 * dem Lesen der Daten aus der Persistenz (Datei) aufgerufen.
	 *
	 * @param data
	 *            Die einelesenen Daten als Byte-Array
	 * @return Die Byte-Array-Repräsentation der XML Darstellung des Achievement
	 *         Fortschritts.
	 * @throws Exception
	 */
	protected byte[] prepareAfterRead(final byte[] data) throws Exception {
		return Arrays.copyOf(data, data.length);
	}
}
