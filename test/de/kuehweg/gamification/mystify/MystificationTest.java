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
package de.kuehweg.gamification.mystify;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.nio.charset.Charset;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Kühweg
 */
public class MystificationTest {

	private byte[] data;

	private byte[] key;
	private byte[] wrongKey;

	@Before
	public void setUp() throws Exception {
		data = ("In the old age black was not counted fair,\n" + "Or if it were it bore not beauty's name:\n"
				+ "But now is black beauty's successive heir,\n" + "And beauty slandered with a bastard shame,\n"
				+ "For since each hand hath put on nature's power,\n"
				+ "Fairing the foul with art's false borrowed face,\n" + "Sweet beauty hath no name no holy bower,\n"
				+ "But is profaned, if not lives in disgrace.\n" + "Therefore my mistress' eyes are raven black,\n"
				+ "Her eyes so suited, and they mourners seem,\n" + "At such who not born fair no beauty lack,\n"
				+ "Slandering creation with a false esteem,\n" + "Yet so they mourn becoming of their woe,\n"
				+ "That every tongue says beauty should look so.").getBytes(Charset.forName("UTF-8"));

		key = "William Shakespeare".getBytes(Charset.forName("UTF-8"));
		wrongKey = "Victor Hugo".getBytes(Charset.forName("UTF-8"));
	}

	@Test
	public void mystifyAndDemystify() throws Exception {
		Mystification mystification = new Mystification();
		assertFalse(Arrays.equals(data, mystification.mystifyDataWithKey(data, key)));
		assertTrue(Arrays.equals(data,
				mystification.demystifyDataWithKey(mystification.mystifyDataWithKey(data, key), key)));
	}

	@Test(expected = Exception.class)
	public void demystifyWithWrongKey() throws Exception {
		Mystification mystification = new Mystification();
		mystification.demystifyDataWithKey(mystification.mystifyDataWithKey(data, key), wrongKey);
	}

	@Test(expected = Exception.class)
	public void demystifyOfUntouchedData() throws Exception {
		new Mystification().demystifyDataWithKey(data, key);
	}
}