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

package de.kuehweg.sqltool.common.text;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Michael Kühweg
 */
public class HtmlEntities {

	private static final String[][] ENTITIES_MAPPING = {
			// Kommentare nur wegen Formatierung in IDE
			{ "&x22;", "&quot;" }, //
			{ "&x26;", "&amp;" }, //
			{ "&x3C;", "&lt;" }, //
			{ "&x3E;", "&gt;" }, //
			{ "&xA0;", "&nbsp;" }, //
			{ "&xA1;", "&iexcl;" }, //
			{ "&xA2;", "&cent;" }, //
			{ "&xA3;", "&pound;" }, //
			{ "&xA4;", "&curren;" }, //
			{ "&xA5;", "&yen;" }, //
			{ "&xA6;", "&brvbar;" }, //
			{ "&xA7;", "&sect;" }, //
			{ "&xA8;", "&uml;" }, //
			{ "&xA9;", "&copy;" }, //
			{ "&xAA;", "&ordf;" }, //
			{ "&xAB;", "&laquo;" }, //
			{ "&xAC;", "&not;" }, //
			{ "&xAD;", "&shy;" }, //
			{ "&xAE;", "&reg;" }, //
			{ "&xAF;", "&macr;" }, //
			{ "&xB0;", "&deg;" }, //
			{ "&xB1;", "&plusmn;" }, //
			{ "&xB2;", "&sup2;" }, //
			{ "&xB3;", "&sup3;" }, //
			{ "&xB4;", "&acute;" }, //
			{ "&xB5;", "&micro;" }, //
			{ "&xB6;", "&para;" }, //
			{ "&xB7;", "&middot;" }, //
			{ "&xB8;", "&cedil;" }, //
			{ "&xB9;", "&sup1;" }, //
			{ "&xBA;", "&ordm;" }, //
			{ "&xBB;", "&raquo;" }, //
			{ "&xBC;", "&frac14;" }, //
			{ "&xBD;", "&frac12;" }, //
			{ "&xBE;", "&frac34;" }, //
			{ "&xBF;", "&iquest;" }, //
			{ "&xC0;", "&Agrave;" }, //
			{ "&xC1;", "&Aacute;" }, //
			{ "&xC2;", "&Acirc;" }, //
			{ "&xC3;", "&Atilde;" }, //
			{ "&xC4;", "&Auml;" }, //
			{ "&xC5;", "&Aring;" }, //
			{ "&xC6;", "&AElig;" }, //
			{ "&xC7;", "&Ccedil;" }, //
			{ "&xC8;", "&Egrave;" }, //
			{ "&xC9;", "&Eacute;" }, //
			{ "&xCA;", "&Ecirc;" }, //
			{ "&xCB;", "&Euml;" }, //
			{ "&xCC;", "&Igrave;" }, //
			{ "&xCD;", "&Iacute;" }, //
			{ "&xCE;", "&Icirc;" }, //
			{ "&xCF;", "&Iuml;" }, //
			{ "&xD0;", "&ETH;" }, //
			{ "&xD1;", "&Ntilde;" }, //
			{ "&xD2;", "&Ograve;" }, //
			{ "&xD3;", "&Oacute;" }, //
			{ "&xD4;", "&Ocirc;" }, //
			{ "&xD5;", "&Otilde;" }, //
			{ "&xD6;", "&Ouml;" }, //
			{ "&xD7;", "&times;" }, //
			{ "&xD8;", "&Oslash;" }, //
			{ "&xD9;", "&Ugrave;" }, //
			{ "&xDA;", "&Uacute;" }, //
			{ "&xDB;", "&Ucirc;" }, //
			{ "&xDC;", "&Uuml;" }, //
			{ "&xDD;", "&Yacute;" }, //
			{ "&xDE;", "&THORN;" }, //
			{ "&xDF;", "&szlig;" }, //
			{ "&xE0;", "&agrave;" }, //
			{ "&xE1;", "&aacute;" }, //
			{ "&xE2;", "&acirc;" }, //
			{ "&xE3;", "&atilde;" }, //
			{ "&xE4;", "&auml;" }, //
			{ "&xE5;", "&aring;" }, //
			{ "&xE6;", "&aelig;" }, //
			{ "&xE7;", "&ccedil;" }, //
			{ "&xE8;", "&egrave;" }, //
			{ "&xE9;", "&eacute;" }, //
			{ "&xEA;", "&ecirc;" }, //
			{ "&xEB;", "&euml;" }, //
			{ "&xEC;", "&igrave;" }, //
			{ "&xED;", "&iacute;" }, //
			{ "&xEE;", "&icirc;" }, //
			{ "&xEF;", "&iuml;" }, //
			{ "&xF0;", "&eth;" }, //
			{ "&xF1;", "&ntilde;" }, //
			{ "&xF2;", "&ograve;" }, //
			{ "&xF3;", "&oacute;" }, //
			{ "&xF4;", "&ocirc;" }, //
			{ "&xF5;", "&otilde;" }, //
			{ "&xF6;", "&ouml;" }, //
			{ "&xF7;", "&divide;" }, //
			{ "&xF8;", "&oslash;" }, //
			{ "&xF9;", "&ugrave;" }, //
			{ "&xFA;", "&uacute;" }, //
			{ "&xFB;", "&ucirc;" }, //
			{ "&xFC;", "&uuml;" }, //
			{ "&xFD;", "&yacute;" }, //
			{ "&xFE;", "&thorn;" }, //
			{ "&xFF;", "&yuml;" }, //
			{ "&x152;", "&OElig;" }, //
			{ "&x153;", "&oelig;" }, //
			{ "&x160;", "&Scaron;" }, //
			{ "&x161;", "&scaron;" }, //
			{ "&x178;", "&Yuml;" }, //
			{ "&x192;", "&fnof;" }, //
			{ "&x2C6;", "&circ;" }, //
			{ "&x2DC;", "&tilde;" }, //
			{ "&x391;", "&Alpha;" }, //
			{ "&x392;", "&Beta;" }, //
			{ "&x393;", "&Gamma;" }, //
			{ "&x394;", "&Delta;" }, //
			{ "&x395;", "&Epsilon;" }, //
			{ "&x396;", "&Zeta;" }, //
			{ "&x397;", "&Eta;" }, //
			{ "&x398;", "&Theta;" }, //
			{ "&x399;", "&Iota;" }, //
			{ "&x39A;", "&Kappa;" }, //
			{ "&x39B;", "&Lambda;" }, //
			{ "&x39C;", "&Mu;" }, //
			{ "&x39D;", "&Nu;" }, //
			{ "&x39E;", "&Xi;" }, //
			{ "&x39F;", "&Omicron;" }, //
			{ "&x3A0;", "&Pi;" }, //
			{ "&x3A1;", "&Rho;" }, //
			{ "&x3A3;", "&Sigma;" }, //
			{ "&x3A4;", "&Tau;" }, //
			{ "&x3A5;", "&Upsilon;" }, //
			{ "&x3A6;", "&Phi;" }, //
			{ "&x3A7;", "&Chi;" }, //
			{ "&x3A8;", "&Psi;" }, //
			{ "&x3A9;", "&Omega;" }, //
			{ "&x3B1;", "&alpha;" }, //
			{ "&x3B2;", "&beta;" }, //
			{ "&x3B3;", "&gamma;" }, //
			{ "&x3B4;", "&delta;" }, //
			{ "&x3B5;", "&epsilon;" }, //
			{ "&x3B6;", "&zeta;" }, //
			{ "&x3B7;", "&eta;" }, //
			{ "&x3B8;", "&theta;" }, //
			{ "&x3B9;", "&iota;" }, //
			{ "&x3BA;", "&kappa;" }, //
			{ "&x3BB;", "&lambda;" }, //
			{ "&x3BC;", "&mu;" }, //
			{ "&x3BD;", "&nu;" }, //
			{ "&x3BE;", "&xi;" }, //
			{ "&x3BF;", "&omicron;" }, //
			{ "&x3C0;", "&pi;" }, //
			{ "&x3C1;", "&rho;" }, //
			{ "&x3C2;", "&sigmaf;" }, //
			{ "&x3C3;", "&sigma;" }, //
			{ "&x3C4;", "&tau;" }, //
			{ "&x3C5;", "&upsilon;" }, //
			{ "&x3C6;", "&phi;" }, //
			{ "&x3C7;", "&chi;" }, //
			{ "&x3C8;", "&psi;" }, //
			{ "&x3C9;", "&omega;" }, //
			{ "&x3D1;", "&thetasym;" }, //
			{ "&x3D2;", "&upsih;" }, //
			{ "&x3D6;", "&piv;" }, //
			{ "&x2002;", "&ensp;" }, //
			{ "&x2003;", "&emsp;" }, //
			{ "&x2009;", "&thinsp;" }, //
			{ "&x200C;", "&zwnj;" }, //
			{ "&x200D;", "&zwj;" }, //
			{ "&x200E;", "&lrm;" }, //
			{ "&x200F;", "&rlm;" }, //
			{ "&x2013;", "&ndash;" }, //
			{ "&x2014;", "&mdash;" }, //
			{ "&x2018;", "&lsquo;" }, //
			{ "&x2019;", "&rsquo;" }, //
			{ "&x201A;", "&sbquo;" }, //
			{ "&x201C;", "&ldquo;" }, //
			{ "&x201D;", "&rdquo;" }, //
			{ "&x201E;", "&bdquo;" }, //
			{ "&x2020;", "&dagger;" }, //
			{ "&x2021;", "&Dagger;" }, //
			{ "&x2022;", "&bull;" }, //
			{ "&x2026;", "&hellip;" }, //
			{ "&x2030;", "&permil;" }, //
			{ "&x2032;", "&prime;" }, //
			{ "&x2033;", "&Prime;" }, //
			{ "&x2039;", "&lsaquo;" }, //
			{ "&x203A;", "&rsaquo;" }, //
			{ "&x203E;", "&oline;" }, //
			{ "&x2044;", "&frasl;" }, //
			{ "&x20AC;", "&euro;" }, //
			{ "&x2118;", "&weierp;" }, //
			{ "&x2111;", "&image;" }, //
			{ "&x211C;", "&real;" }, //
			{ "&x2122;", "&trade;" }, //
			{ "&x2135;", "&alefsym;" }, //
			{ "&x2190;", "&larr;" }, //
			{ "&x2191;", "&uarr;" }, //
			{ "&x2192;", "&rarr;" }, //
			{ "&x2193;", "&darr;" }, //
			{ "&x2194;", "&harr;" }, //
			{ "&x21B5;", "&crarr;" }, //
			{ "&x21D0;", "&lArr;" }, //
			{ "&x21D1;", "&uArr;" }, //
			{ "&x21D2;", "&rArr;" }, //
			{ "&x21D3;", "&dArr;" }, //
			{ "&x21D4;", "&hArr;" }, //
			{ "&x2200;", "&forall;" }, //
			{ "&x2202;", "&part;" }, //
			{ "&x2203;", "&exist;" }, //
			{ "&x2205;", "&empty;" }, //
			{ "&x2207;", "&nabla;" }, //
			{ "&x2208;", "&isin;" }, //
			{ "&x2209;", "&notin;" }, //
			{ "&x220B;", "&ni;" }, //
			{ "&x220F;", "&prod;" }, //
			{ "&x2211;", "&sum;" }, //
			{ "&x2212;", "&minus;" }, //
			{ "&x2217;", "&lowast;" }, //
			{ "&x221A;", "&radic;" }, //
			{ "&x221D;", "&prop;" }, //
			{ "&x221E;", "&infin;" }, //
			{ "&x2220;", "&ang;" }, //
			{ "&x2227;", "&and;" }, //
			{ "&x2228;", "&or;" }, //
			{ "&x2229;", "&cap;" }, //
			{ "&x222A;", "&cup;" }, //
			{ "&x222B;", "&int;" }, //
			{ "&x2234;", "&there4;" }, //
			{ "&x223C;", "&sim;" }, //
			{ "&x2245;", "&cong;" }, //
			{ "&x2248;", "&asymp;" }, //
			{ "&x2260;", "&ne;" }, //
			{ "&x2261;", "&equiv;" }, //
			{ "&x2264;", "&le;" }, //
			{ "&x2265;", "&ge;" }, //
			{ "&x2282;", "&sub;" }, //
			{ "&x2283;", "&sup;" }, //
			{ "&x2286;", "&sube;" }, //
			{ "&x2287;", "&supe;" }, //
			{ "&x2295;", "&oplus;" }, //
			{ "&x2297;", "&otimes;" }, //
			{ "&x22A5;", "&perp;" }, //
			{ "&x22C5;", "&sdot;" }, //
			{ "&x2308;", "&lceil;" }, //
			{ "&x2309;", "&rceil;" }, //
			{ "&x230A;", "&lfloor;" }, //
			{ "&x230B;", "&rfloor;" }, //
			{ "&x2329;", "&lang;" }, //
			{ "&x232A;", "&rang;" }, //
			{ "&x25CA;", "&loz;" }, //
			{ "&x2660;", "&spades;" }, //
			{ "&x2663;", "&clubs;" }, //
			{ "&x2665;", "&hearts;" }, //
			{ "&x2666;", "&diams;" } //
	};

	private final Map<String, String> entitiesMap;

	public HtmlEntities() {
		entitiesMap = buildEntitiesMap();
	}

	private Map<String, String> buildEntitiesMap() {
		final Map<String, String> map = new HashMap<>(ENTITIES_MAPPING.length);
		for (final String[] mapping : ENTITIES_MAPPING) {
			map.put(mapping[0], mapping[1]);
		}
		return map;
	}

	public String findEntityForCharacter(final Character character) {
		final String unicode = encodeAsUnicode(character);
		if (entitiesMap.containsKey(unicode)) {
			return entitiesMap.get(unicode);
		}
		return unicode;
	}

	private String encodeAsUnicode(final Character character) {
		return new StringBuilder("&x").append(Integer.toHexString(character).toUpperCase()).append(";").toString();
	}
}
