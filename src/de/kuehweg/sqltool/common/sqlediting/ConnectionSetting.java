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

import de.kuehweg.sqltool.database.JDBCType;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Verbindungsdaten um sich mit einer Datenbank (unterschiedlicher Typen)
 * verbinden zu können.
 *
 * @author Michael Kühweg
 */
public class ConnectionSetting implements Serializable,
        Comparable<ConnectionSetting> {

    private static final long serialVersionUID = -3540291839181124105L;
    private String name;
    private JDBCType type;
    private String dbPath;
    private String dbName;
    private String user;
    private String password;

    protected ConnectionSetting() {
    }

    public ConnectionSetting(final String name, final JDBCType type,
            final String dbPath, final String dbName, final String user,
            final String password) {

        this.name = name;
        this.type = type;
        this.dbPath = dbPath;
        this.dbName = dbName;
        this.user = user;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public JDBCType getType() {
        return type;
    }

    public void setType(final JDBCType type) {
        this.type = type;
    }

    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(final String dbPath) {
        this.dbPath = dbPath;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(final String dbName) {
        this.dbName = dbName;
    }

    public String getUser() {
        return user;
    }

    public void setUser(final String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getUrl() {
        String url = type.getUrlPrefix() + type.getDbType() + dbPath;
        if (!url.endsWith("/")) {
            url += "/";
        }
        try {
            return url + URLEncoder.encode(dbName, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return url + "johndoe";
        }
    }

    public String getUrlWithoutTypePrefix() {
        String url = type.getDbType() + dbPath;
        if (!url.endsWith("/")) {
            url += "/";
        }
        try {
            return url + URLEncoder.encode(dbName, "UTF-8");
        } catch (UnsupportedEncodingException ex) {
            return url + "johndoe";
        }
    }

    @Override
    public boolean equals(final Object obj) {

        if (!(obj instanceof ConnectionSetting)) {
            return false;
        }

        final ConnectionSetting other = (ConnectionSetting) obj;

        if (getName() == null && other.getName() == null) {
            return true;
        }

        if (getName() == null) {
            return false;
        }

        return getName().trim().equals(other.getName().trim());
    }

    @Override
    public int hashCode() {
        return getName() == null ? 0 : getName().trim().hashCode();
    }

    @Override
    public int compareTo(final ConnectionSetting other) {
        if (getName() != null) {
            if (other.getName() != null) {
                return getName().compareTo(other.getName());
            } else {
                return 1;
            }
        } else {
            if (other.getName() != null) {
                return -1;
            } else {
                return 0;
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
