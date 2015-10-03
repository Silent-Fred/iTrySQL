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

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.zip.ZipException;

import org.junit.Before;
import org.junit.Test;

/**
 * @author Michael Kühweg
 */
public class ZippoTest {

	private byte[] data;

	@Before
	public void setUp() throws Exception {
		data = ("L'Enfant\n" + "\n" + "Les turcs ont passé là. Tout est ruine et deuil.\n"
				+ "Chio, l'île des vins, n'est plus qu'un sombre écueil, \n" + "Chio, qu'ombrageaient les charmilles,\n"
				+ "Chio, qui dans les flots reflétait ses grands bois,\n"
				+ "Ses coteaux, ses palais, et le soir quelquefois \n" + "Un chœur dansant de jeunes filles.\n" + "\n"
				+ "Tout est désert. Mais non ; seul près des murs noircis,\n"
				+ "Un enfant aux yeux bleus, un enfant grec, assis, \n" + "Courbait sa tête humiliée ;\n"
				+ "Il avait pour asile, il avait pour appui\n" + "Une blanche aubépine, une fleur, comme lui \n"
				+ "Dans le grand ravage oubliée.\n" + "\n" + "Ah ! pauvre enfant, pieds nus sur les rocs anguleux !\n"
				+ "Hélas ! pour essuyer les pleurs de tes yeux bleus \n" + "Comme le ciel et comme l'onde,\n"
				+ "Pour que dans leur azur, de larmes orageux,\n" + "Passe le vif éclair de la joie et des jeux, \n"
				+ "Pour relever ta tête blonde,\n" + "\n" + "Que veux-tu ? Bel enfant, que te faut-il donner\n"
				+ "Pour rattacher gaîment et gaîment ramener \n" + "En boucles sur ta blanche épaule\n"
				+ "Ces cheveux, qui du fer n'ont pas subi l'affront,\n"
				+ "Et qui pleurent épars autour de ton beau front, \n" + "Comme les feuilles sur le saule ?\n" + "\n"
				+ "Qui pourrait dissiper tes chagrins nébuleux ?\n"
				+ "Est-ce d'avoir ce lys, bleu comme tes yeux bleus, \n" + "Qui d'Iran borde le puits sombre ?\n"
				+ "Ou le fruit du tuba, de cet arbre si grand,\n" + "Qu'un cheval au galop met, toujours en courant, \n"
				+ "Cent ans à sortir de son ombre ?\n" + "\n" + "Veux-tu, pour me sourire, un bel oiseau des bois,\n"
				+ "Qui chante avec un chant plus doux que le hautbois, \n" + "Plus éclatant que les cymbales ?\n"
				+ "Que veux-tu ? fleur, beau fruit, ou l'oiseau merveilleux ?\n"
				+ "- Ami, dit l'enfant grec, dit l'enfant aux yeux bleus, \n" + "Je veux de la poudre et des balles.\n"
				+ "\n" + "8-10 juillet 1828\n" + "\n" + "     Victor Hugo - Les Orientales")
						.getBytes(Charset.forName("UTF-8"));
	}

	@Test
	public void zipAndUnzip() throws IOException {
		assertFalse(Arrays.equals(data, Zippo.zip(data)));
		assertTrue(Arrays.equals(data, Zippo.unzip(Zippo.zip(data))));
	}

	@Test(expected = ZipException.class)
	public void unzipOfUntouchedData() throws IOException {
		Zippo.unzip(data);
	}
}