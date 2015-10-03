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

package de.kuehweg.sqltool.dialog.component.achievement;

import java.util.Comparator;

import de.kuehweg.gamification.Achievement;
import de.kuehweg.sqltool.common.achievement.NamedAchievement;

/**
 * Standardsortierung der Achievements für die Ausgabeaufbereitung. Bei
 * Achievements aus {@link NamedAchievement} wird die dort definierte
 * Reihenfolge verwendet. Wenn andere Achivements ins Spiel kommen, gilt die
 * Reihenfolge gemäß den Namen der Achievements.
 *
 * @author Michael Kühweg
 */
public class AchievementDefaultSortOrder implements Comparator<Achievement> {

	@Override
	public int compare(final Achievement o1, final Achievement o2) {
		try {
			final NamedAchievement namedAchievement1 = NamedAchievement.valueOf(o1.getName());
			final NamedAchievement namedAchievement2 = NamedAchievement.valueOf(o2.getName());
			return namedAchievement1.compareTo(namedAchievement2);
		} catch (final IllegalArgumentException e) {
			return o1.getName().compareTo(o2.getName());
		}
	}

}
