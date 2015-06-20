/*
 * Copyright (c) 2013-2015, Michael Kühweg
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
package de.kuehweg.sqltool.dialog.component;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.ConnectionSetting;
import de.kuehweg.sqltool.database.DefaultConnectionSettingsProvider;
import de.kuehweg.sqltool.database.JDBCType;
import de.kuehweg.sqltool.database.ManagedConnectionSettings;
import de.kuehweg.sqltool.dialog.ConfirmDialog;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import java.io.File;
import java.util.prefs.BackingStoreException;
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;

/**
 * Baustein für die Verbindungsverwaltung
 *
 * @author Michael Kühweg
 */
public class ConnectionComponentController {

    /**
     * Builder-Pattern
     */
    public static class Builder {

        private final VBox container;
        private TextField connectionName;
        private ComboBox<ConnectionSetting> connectionSelection;
        private ComboBox<JDBCType> connectionType;
        private TextField connectionUrl;
        private TextField connectionUser;
        private Button browseButton;
        private TextField dbName;
        private Button editButton;
        private Button removeButton;

        public Builder(final VBox container) {
            this.container = container;
        }

        public Builder editButton(final Button editButton) {
            this.editButton = editButton;
            return this;
        }

        public Builder removeButton(final Button removeButton) {
            this.removeButton = removeButton;
            return this;
        }

        public Builder connectionName(final TextField connectionName) {
            this.connectionName = connectionName;
            return this;
        }

        public Builder connectionSelection(
                final ComboBox<ConnectionSetting> connectionSelection) {
            this.connectionSelection = connectionSelection;
            return this;
        }

        public Builder connectionType(final ComboBox<JDBCType> connectionType) {
            this.connectionType = connectionType;
            return this;
        }

        public Builder connectionUrl(final TextField connectionUrl) {
            this.connectionUrl = connectionUrl;
            return this;
        }

        public Builder connectionUser(final TextField connectionUser) {
            this.connectionUser = connectionUser;
            return this;
        }

        public Builder browseButton(final Button browseButton) {
            this.browseButton = browseButton;
            return this;
        }

        public Builder dbName(final TextField dbName) {
            this.dbName = dbName;
            return this;
        }

        public ConnectionComponentController build() {
            return new ConnectionComponentController(this);
        }
    }
    private final VBox container;
    private final TextField connectionName;
    private final ComboBox<ConnectionSetting> connectionSelection;
    private final ComboBox<JDBCType> connectionType;
    private final TextField connectionUrl;
    private final TextField connectionUser;
    private final Button browseButton;
    private final TextField dbName;
    private final Button editButton;
    private final Button removeButton;
    private final BooleanProperty connectionSettingsEditable;
    private final BooleanProperty connectionSettingFileBased;

    private ConnectionComponentController(final Builder builder) {
        container = builder.container;
        connectionName = builder.connectionName;
        connectionSelection = builder.connectionSelection;
        connectionType = builder.connectionType;
        connectionUrl = builder.connectionUrl;
        connectionUser = builder.connectionUser;
        browseButton = builder.browseButton;
        dbName = builder.dbName;
        editButton = builder.editButton;
        removeButton = builder.removeButton;
        connectionSettingsEditable = new SimpleBooleanProperty(false);
        connectionSettingFileBased = new SimpleBooleanProperty(false);
        connectionName.disableProperty().bind(Bindings.not(connectionSettingsEditable));
        connectionType.disableProperty().bind(Bindings.not(connectionSettingsEditable));
        connectionUrl.disableProperty().bind(Bindings.not(connectionSettingsEditable));
        dbName.disableProperty().bind(Bindings.not(connectionSettingsEditable));
        connectionUser.disableProperty().bind(Bindings.not(connectionSettingsEditable));
        browseButton.disableProperty().bind(Bindings.not(connectionSettingsEditable).or(
                Bindings.not(connectionSettingFileBased)));
        prepareConnectionSettings();
    }

    public void changeConnection() {
        putSelectedConnectionSettingInDialog();
        controlConnectionSettingsVisibility();
    }

    public void createConnection() {
        final ConnectionSetting connectionSetting = DefaultConnectionSettingsProvider.
                getDefaultTemplateStandaloneUserHomeConnection();
        connectionSetting.setName(
                createValidNewConnectionName(connectionSetting.getName()));
        putConnectionSettingInDialog(connectionSetting);
        saveConnectionSettings();
        if (connectionSelection.getItems().indexOf(connectionSetting) >= 0) {
            connectionSelection.setValue(connectionSetting);
            editConnection();
        }
        container.visibleProperty().set(true);
        editButton.disableProperty().set(true);
        removeButton.disableProperty().set(true);
        connectionSettingsEditable.set(true);
        connectionSettingFileBased.set(isFileBasedType(connectionSetting));
    }

    public void editConnection() {
        putSelectedConnectionSettingInDialog();
        controlConnectionSettingsVisibility();
        connectionSettingsEditable.set(true);
    }

    public void changeConnectionType() {
        final JDBCType type = connectionType.getValue();
        if (type != null) {
            connectionUrl.setText(null);
            dbName.setText(null);
            connectionSettingFileBased.set(isFileBasedType(type));
        }
    }

    public void chooseDbDirectory() {
        final DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle(DialogDictionary.LABEL_DB_DIRECTORY_CHOOSER.toString());
        final File dir = dirChooser.showDialog(container.getScene().getWindow());
        if (dir != null) {
            final JDBCType type = connectionType.valueProperty().get();
            if (type != null) {
                final String dbSource = dir.getAbsolutePath();
                connectionUrl.setText(dbSource);
            }
        }
    }

    public void cancelEdit() {
        connectionSelection.valueProperty().set(null);
        controlConnectionSettingsVisibility();
        connectionSettingsEditable.set(false);
    }

    public void saveConnectionSettings() {
        try {
            final ManagedConnectionSettings settings = new ManagedConnectionSettings();
            final ConnectionSetting setting = getConnectionSettingFromDialog();
            settings.addConnectionSetting(setting);
            prepareConnectionSettings();
            connectionSelection.setValue(setting);
            putSelectedConnectionSettingInDialog();
        } catch (final BackingStoreException ex) {
            final ErrorMessage msg = new ErrorMessage(
                    DialogDictionary.MESSAGEBOX_ERROR.toString(),
                    DialogDictionary.ERR_CONNECTION_SETTING_SAVE_FAILED
                    .toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        }
    }

    public void removeConnection() {
        final ConfirmDialog confirm = new ConfirmDialog(
                DialogDictionary.MESSAGEBOX_CONFIRM.toString(),
                DialogDictionary.MSG_REALLY_REMOVE_CONNECTION.toString(),
                DialogDictionary.LABEL_REMOVE_CONNECTION.toString(),
                DialogDictionary.COMMON_BUTTON_CANCEL.toString());
        final String confirmation = confirm.askUserFeedback();
        if (DialogDictionary.LABEL_REMOVE_CONNECTION.toString().equals(
                confirmation)) {
            try {
                removeConnectionSettingFromPreferences(connectionSelection.getValue());
            } catch (final BackingStoreException ex) {
                final ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_CONNECTION_SETTING_SAVE_FAILED
                        .toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
            prepareConnectionSettings();
            // nach dem Löschen immer den Editiermodus beenden
            cancelEdit();
        }
    }

    private void removeConnectionSettingFromPreferences(
            final ConnectionSetting connectionSetting) throws BackingStoreException {
        if (connectionSetting != null) {
            final ManagedConnectionSettings settings = new ManagedConnectionSettings();
            settings.removeConnectionSetting(connectionSetting);
        }
    }

    private boolean isFileBasedType(final ConnectionSetting connectionSetting) {
        return connectionSetting != null && isFileBasedType(connectionSetting.getType());
    }

    private boolean isFileBasedType(JDBCType type) {
        return type != null && type == JDBCType.HSQL_STANDALONE;
    }

    private void controlConnectionSettingsVisibility() {
        final boolean empty = connectionSelection.valueProperty().get() == null;
        container.visibleProperty().set(!empty);
        removeButton.disableProperty().set(empty);
        editButton.disableProperty().set(empty);
    }

    private void putSelectedConnectionSettingInDialog() {
        putConnectionSettingInDialog(connectionSelection.getValue());
    }

    private void putConnectionSettingInDialog(final ConnectionSetting setting) {
        if (setting == null) {
            container.visibleProperty().set(false);
        } else {
            connectionName.setText(setting.getName());
            connectionType.valueProperty().set(setting.getType());
            connectionUrl.setText(setting.getDbPath());
            dbName.setText(setting.getDbName());
            connectionUser.setText(setting.getUser());
        }
    }

    private ConnectionSetting getConnectionSettingFromDialog() {
        final ConnectionSetting setting = new ConnectionSetting(
                connectionName.getText(), connectionType.getValue(),
                connectionUrl.getText(), dbName.getText(),
                connectionUser.getText(), null);
        return setting;
    }

    private void fillConnectionSettings() {
        final ManagedConnectionSettings settings = new ManagedConnectionSettings();
        final ObservableList<ConnectionSetting> localSettings = FXCollections
                .observableArrayList();
        for (final ConnectionSetting connectionSetting : settings
                .getConnectionSettings()) {
            localSettings.add(connectionSetting);
        }
        connectionSelection.setItems(localSettings);
    }

    private void fillJdbcTypes() {
        connectionType.getItems().clear();
        connectionType.getItems().addAll(JDBCType.values());
    }

    private String createValidNewConnectionName(final String baseName) {
        String name = baseName;
        if (connectionNameAlreadyUsed(name)) {
            int count = 1;
            do {
                name = DialogDictionary.PATTERN_NEW_CONNECTION_NAME.toString()
                        + " (" + count++ + ")";
            } while (connectionNameAlreadyUsed(name));
        }
        return name;
    }

    private boolean connectionNameAlreadyUsed(final String name) {
        if (name == null) {
            return true;
        }
        final ManagedConnectionSettings settings = new ManagedConnectionSettings();
        for (final ConnectionSetting setting : settings.getConnectionSettings()) {
            if (setting.getName() != null && setting.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    private void prepareConnectionSettings() {
        fillConnectionSettings();
        fillJdbcTypes();
        controlConnectionSettingsVisibility();
    }
}
