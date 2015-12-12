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
package de.kuehweg.sqltool.common;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Michael Kühweg
 */
public class RomanNumberTest {

	public RomanNumberTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	@Test
	public void smallNumbers() {
		assertEquals("I", new RomanNumber(1L).toString());
		assertEquals("II", new RomanNumber(2L).toString());
		assertEquals("III", new RomanNumber(3L).toString());
		assertEquals("IV", new RomanNumber(4L).toString());
		assertEquals("V", new RomanNumber(5L).toString());
		assertEquals("VI", new RomanNumber(6L).toString());
		assertEquals("VII", new RomanNumber(7L).toString());
		assertEquals("VIII", new RomanNumber(8L).toString());
		assertEquals("IX", new RomanNumber(9L).toString());
	}

	@Test
	public void usualSuspects() {
		assertEquals("XL", new RomanNumber(40L).toString());
		assertEquals("XLIX", new RomanNumber(49L).toString());
		assertEquals("L", new RomanNumber(50L).toString());
		assertEquals("XC", new RomanNumber(90L).toString());
		assertEquals("XCIX", new RomanNumber(99L).toString());
		assertEquals("C", new RomanNumber(100L).toString());
		assertEquals("CXLIX", new RomanNumber(149L).toString());
		assertEquals("CXCIX", new RomanNumber(199L).toString());
		assertEquals("CC", new RomanNumber(200L).toString());
		assertEquals("CD", new RomanNumber(400L).toString());
		assertEquals("CM", new RomanNumber(900L).toString());
		assertEquals("MM", new RomanNumber(2000L).toString());
	}

	@Test
	public void elvis() {
		assertEquals("MCMXXXV", new RomanNumber(1935).toString());
	}

	@Test
	public void goodOldRomansWerePositive() {
		assertEquals("mendum", new RomanNumber(0).toString());
		assertEquals("mendum", new RomanNumber(-1).toString());
	}
}
