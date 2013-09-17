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

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.JDBCType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

/**
 * Verwaltung der vom Anwender angelegten Verbindungsdaten (ConnectionSetting)
 * über Preferences. Die Verbindungsdaten stehen beim nächsten Programmstart
 * wieder zur Verfügung.
 *
 * @author Michael Kühweg
 */
public class ConnectionSettings {

    private static final String TYPE_POSTFIX = ".jdbc";
    private static final String DBPATH_POSTFIX = ".dbpath";
    private static final String DBNAME_POSTFIX = ".dbname";
    private static final String USER_POSTFIX = ".user";
    private final Preferences preferences;
    private Set<ConnectionSetting> connectionSettings;

    public ConnectionSettings() {
        // mehrere Instanzen von ConnectionSettings können angelegt werden,
        // die eigentliche Ablage in den Preferences passiert aber zentral
        // pro Anwender
        preferences = Preferences.userNodeForPackage(getClass()).node(
                getClass().getSimpleName());
    }

    private void initConnectionSettingsFromPreferences() {
        try {
            Collection<String> keys = Arrays.asList(preferences.keys());
            final Map<String, ConnectionSetting> settings = new HashMap<>();
            for (final String key : keys) {
                final String name = extractConnectionNameFromKey(key);
                if (name != null) {
                    ConnectionSetting setting = settings.get(name);
                    if (setting == null) {
                        setting = new ConnectionSetting();
                        setting.setName(name);
                        settings.put(name, setting);
                    }
                    setSettingFromKey(setting, key);
                }
            }
            connectionSettings = new HashSet<>(
                    settings.values());
            connectionSettings.add(getDefaultConnection());
        } catch (final BackingStoreException e) {
            connectionSettings = new HashSet<>(Collections.singleton(
                    getDefaultConnection()));
        }
    }

    private static ConnectionSetting getDefaultConnection() {
        return new ConnectionSetting(
                DialogDictionary.LABEL_DEFAULT_CONNECTION.toString(),
                JDBCType.HSQL_IN_MEMORY, null, "rastelli",
                null, null);
    }

    public List<ConnectionSetting> getConnectionSettings() {
        if (connectionSettings == null) {
            initConnectionSettingsFromPreferences();
        }
        final List<ConnectionSetting> result = new ArrayList<>(
                connectionSettings);
        Collections.sort(result);
        return result;
    }

    public List<ConnectionSetting> getConnectionSettingsAfterAdditionOf(
            final ConnectionSetting connectionSetting)
            throws BackingStoreException {
        if (connectionSetting != null) {
            if (connectionSettings == null) {
                initConnectionSettingsFromPreferences();
            }
            connectionSettings.add(connectionSetting);
            if (connectionSetting.getType() != null) {
                preferences.put(connectionSetting.getName() + TYPE_POSTFIX,
                        connectionSetting.getType().name());
            }
            if (connectionSetting.getDbPath() != null) {
                preferences.put(connectionSetting.getName() + DBPATH_POSTFIX,
                        connectionSetting.getDbPath());
            }
            if (connectionSetting.getDbName() != null) {
                preferences.put(connectionSetting.getName() + DBNAME_POSTFIX,
                        connectionSetting.getDbName());
            }
            if (connectionSetting.getUser() != null) {
                preferences.put(connectionSetting.getName() + USER_POSTFIX,
                        connectionSetting.getUser());
            }
            preferences.flush();
        }
        return getConnectionSettings();
    }

    public List<ConnectionSetting> getConnectionSettingsAfterRemovalOf(
            final ConnectionSetting connectionSetting)
            throws BackingStoreException {
        if (connectionSetting != null) {
            if (connectionSettings == null) {
                initConnectionSettingsFromPreferences();
            }
            connectionSettings.remove(connectionSetting);
            preferences.remove(connectionSetting.getName() + TYPE_POSTFIX);
            preferences.remove(connectionSetting.getName() + DBPATH_POSTFIX);
            preferences.remove(connectionSetting.getName() + DBNAME_POSTFIX);
            preferences.remove(connectionSetting.getName() + USER_POSTFIX);
            preferences.flush();
        }
        return getConnectionSettings();
    }

    public List<ConnectionSetting> getConnectionSettingsRetainingOnlyNames(
            final Collection<String> namedConnections)
            throws BackingStoreException {
        if (connectionSettings == null) {
            initConnectionSettingsFromPreferences();
        }
        final Collection<ConnectionSetting> settingsToRemove = new HashSet<>();
        for (final ConnectionSetting connectionSetting : connectionSettings) {
            if (!namedConnections.contains(connectionSetting.getName())) {
                settingsToRemove.add(connectionSetting);
            }
        }
        for (final ConnectionSetting connectionSetting : settingsToRemove) {
            getConnectionSettingsAfterRemovalOf(connectionSetting);
        }
        return getConnectionSettings();
    }

    private void setSettingFromKey(final ConnectionSetting setting,
            final String key) {
        if (setting != null && key != null) {
            final String value = preferences.get(key, "unknown");
            if (key.endsWith(TYPE_POSTFIX)) {
                try {
                    setting.setType(JDBCType.valueOf(value));
                } catch (final IllegalArgumentException e) {
                    setting.setType(JDBCType.HSQL_IN_MEMORY);
                }
            } else if (key.endsWith(DBPATH_POSTFIX)) {
                setting.setDbPath(value);
            } else if (key.endsWith(DBNAME_POSTFIX)) {
                setting.setDbName(value);
            } else if (key.endsWith(USER_POSTFIX)) {
                setting.setUser(value);
            }
        }
    }

    private String extractConnectionNameFromKey(final String key) {
        String connectionName = extractConnectionNameFromKeyByPostfix(key,
                TYPE_POSTFIX);
        connectionName = connectionName != null ? connectionName
                : extractConnectionNameFromKeyByPostfix(key, DBPATH_POSTFIX);
        connectionName = connectionName != null ? connectionName
                : extractConnectionNameFromKeyByPostfix(key, DBNAME_POSTFIX);
        connectionName = connectionName != null ? connectionName
                : extractConnectionNameFromKeyByPostfix(key, USER_POSTFIX);
        return connectionName;
    }

    private String extractConnectionNameFromKeyByPostfix(final String key,
            final String postfix) {
        if (key != null && postfix != null && key.length() > postfix.length()) {
            if (key.endsWith(postfix)) {
                return key.substring(0, key.length() - postfix.length());
            }
        }
        return null;
    }
}
