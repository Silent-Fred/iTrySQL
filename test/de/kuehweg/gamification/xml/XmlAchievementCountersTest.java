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
package de.kuehweg.gamification.xml;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import org.junit.Test;

/**
 * @author Michael Kühweg
 */
public class XmlAchievementCountersTest {

	@Test
	public void marshal() throws JAXBException {
		final XmlAchievementCounter counter1 = new XmlAchievementCounter();
		counter1.setUniqueId("A");
		counter1.setCounter(10);
		final XmlAchievementCounter counter2 = new XmlAchievementCounter();
		counter2.setUniqueId("B");
		counter2.setCounter(20);

		final List<XmlAchievementCounter> counters = Arrays.asList(new XmlAchievementCounter[] { counter1, counter2 });

		final XmlAchievementCounters achievement = new XmlAchievementCounters();
		achievement.setCounters(counters);

		final JAXBContext context = JAXBContext.newInstance(XmlAchievementCounters.class);
		final StringWriter writer = new StringWriter();
		context.createMarshaller().marshal(achievement, writer);
		assertEquals(
				"<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><achieved><counters><count event=\"A\">10</count><count event=\"B\">20</count></counters></achieved>",
				writer.toString());
	}
}