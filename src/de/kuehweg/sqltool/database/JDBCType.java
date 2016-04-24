/*
 * Copyright (c) 2013-2016, Michael Kühweg
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
package de.kuehweg.sqltool.database;

/**
 * Unterstützte JDBC-Treiber.
 *
 * @author Michael Kühweg
 */
public enum JDBCType {

	HSQL_IN_MEMORY("HSQL Database Engine In-Memory", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:", "mem:") {
		@Override
		public boolean isPermanent() {
			return false;
		}
	},

	HSQL_STANDALONE("HSQL Database Engine Standalone", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:", "file:"),

	HSQL_SERVER("HSQL Database Engine Server", "org.hsqldb.jdbcDriver", "jdbc:hsqldb:", "hsql:");

	private final String name;
	private final String driverClass;
	private final String urlPrefix;
	private final String dbType;

	JDBCType(final String name, final String driverClass, final String urlPrefix, final String dbType) {
		this.name = name;
		this.driverClass = driverClass;
		this.urlPrefix = urlPrefix;
		this.dbType = dbType;
	}

	public String getName() {
		return name;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public String getUrlPrefix() {
		return urlPrefix;
	}

	public String getDbType() {
		return dbType;
	}

	public boolean isPermanent() {
		return true;
	}

	@Override
	public String toString() {
		return name;
	}
}
