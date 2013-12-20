/*
 * Copyright (c) 2013, Michael Kühweg
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
package de.kuehweg.sqltool.common.sqlediting;

/**
 * Codevorlagen mit Syntaxbeschreibung.
 * 
 * @author Michael Kühweg
 */
public enum CodeHelp {

	SELECT("SELECT", "SELECT * \nFROM \nWHERE \n-- GROUP BY\n-- ORDER BY",
			EBNFProductions.SUBQUERY + "\n" + EBNFProductions.SELECT_LIST
					+ "\n" + EBNFProductions.JOIN_SOURCE + "\n"
					+ EBNFProductions.JOIN_CLAUSE + "\n"
					+ EBNFProductions.INNER_CROSS_JOIN_CLAUSE + "\n"
					+ EBNFProductions.OUTER_JOIN_CLAUSE + "\n"
					+ EBNFProductions.JOIN_CONSTRAINT + "\n"
					+ EBNFProductions.COMPOUND_OPERATOR + "\n"
					+ EBNFProductions.TABLE_REFERENCE + "\n"
					+ EBNFProductions.QUERY_TABLE_EXPRESSION + "\n"
					+ EBNFProductions.WHERE_CLAUSE + "\n"
					+ EBNFProductions.GROUP_BY_CLAUSE + "\n"
					+ EBNFProductions.ORDER_BY_CLAUSE + "\n"), INSERT("INSERT",
			"INSERT INTO     (   )\nVALUES (   )", EBNFProductions.INSERT
					+ "\n" + EBNFProductions.INSERT_INTO_CLAUSE + "\n"
					+ EBNFProductions.VALUES_CLAUSE + "\n"), UPDATE("UPDATE",
			"UPDATE \nSET \nWHERE ", EBNFProductions.UPDATE + "\n"
					+ EBNFProductions.TABLE_REFERENCE + "\n"
					+ EBNFProductions.UPDATE_SET_CLAUSE + "\n"
					+ EBNFProductions.WHERE_CLAUSE + "\n"), DELETE("DELETE",
			"DELETE FROM \nWHERE ", EBNFProductions.DELETE + "\n"
					+ EBNFProductions.TABLE_REFERENCE + "\n"
					+ EBNFProductions.WHERE_CLAUSE + "\n"), CREATE_TABLE(
			"CREATE TABLE", "CREATE TABLE ", EBNFProductions.CREATE_TABLE
					+ "\n" + EBNFProductions.RELATIONAL_PROPERTIES + "\n"
					+ EBNFProductions.TABLE_PROPERTIES + "\n"), DROP_TABLE(
			"DROP TABLE", "DROP TABLE ", EBNFProductions.DROP_TABLE + "\n"), CHECKPOINT(
			"CHECKPOINT", "CHECKPOINT", "(HSQLDB SQL only)"), SCRIPT("SCRIPT",
			"SCRIPT", "SCRIPT [ 'file' ]\n\n(HSQLDB SQL only)"), SHUTDOWN(
			"SHUTDOWN", "SHUTDOWN",
			"SHUTDOWN [ COMPACT | IMMEDIATELY | SCRIPT ]\n\n(HSQLDB SQL only)"), SET(
			"SET ", "SET ", "SET AUTOCOMMIT { TRUE | FALSE }\n"
					+ "SET DATABASE COLLATION \"<collationname>\"\n"
					+ "SET FILES CHECKPOINT DEFRAG <size>\n"
					+ "SET DATABASE INITIAL SCHEMA <schemaname>\n"
					+ "SET FILES LOG SIZE <size>\n" + "SET MAXROWS maxrows\n"
					+ "SET PASSWORD <password>\n"
					+ "SET FILES READ { ONLY | WRITE }\n"
					+ "SET SCHEMA <schemaname>\n"
					+ "SET TABLE <tablename> READ { ONLY | WRITE }\n"
					+ "SET TABLE <tablename> SOURCE { ON | OFF }\n"
					+ "SET TABLE <tablename> SOURCE \"<file>\" [DESC]\n"
					+ "\n\n(HSQLDB SQL only)");
	private final String readable;
	private final String template;
	private final String syntaxDescription;

	private CodeHelp(final String readable, final String template,
			final String syntaxDescription) {
		this.readable = readable;
		this.template = template;
		this.syntaxDescription = syntaxDescription;
	}

	@Override
	public String toString() {
		return readable;
	}

	public String getCodeTemplate() {
		return template;
	}

	public String getSyntaxDescription() {
		return syntaxDescription;
	}
}
