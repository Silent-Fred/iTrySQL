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

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Michael Kühweg
 */
public class AchievementEventTest {

    @Test
    public void equalsAndHashCode() {
        String a1 = "A";
        String a2 = "AB".substring(0, 1);
        String b = "B";

        // Setup des Tests nicht ungewollt verändern...
        assertEquals(a1, a2);
        assertFalse(a1 == a2);

        AchievementEvent event1 = new AchievementEvent(a1);
        AchievementEvent event2 = new AchievementEvent(a2);
        AchievementEvent event3 = new AchievementEvent(b);

        assertEquals(event1, event2);
        assertNotEquals(event1, event3);

        Set<AchievementEvent> set = new HashSet<>();
        set.add(event1);
        set.add(event2);
        set.add(event3);
        assertEquals(2, set.size());
    }
}
