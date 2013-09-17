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
package de.kuehweg.sqltool.itrysql;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.FileUtil;
import de.kuehweg.sqltool.common.UserPreferencesManager;
import de.kuehweg.sqltool.common.sqlediting.CodeHelp;
import de.kuehweg.sqltool.common.sqlediting.ConnectionSetting;
import de.kuehweg.sqltool.common.sqlediting.ConnectionSettings;
import de.kuehweg.sqltool.common.sqlediting.SQLHistory;
import de.kuehweg.sqltool.common.sqlediting.SQLHistoryKeeper;
import de.kuehweg.sqltool.common.sqlediting.StatementExtractor;
import de.kuehweg.sqltool.database.ConnectionHolder;
import de.kuehweg.sqltool.database.JDBCType;
import de.kuehweg.sqltool.database.ServerManager;
import de.kuehweg.sqltool.dialog.AlertBox;
import de.kuehweg.sqltool.dialog.ConfirmDialog;
import de.kuehweg.sqltool.dialog.ConnectionDialog;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import de.kuehweg.sqltool.dialog.License;
import de.kuehweg.sqltool.dialog.action.ExecuteAction;
import de.kuehweg.sqltool.dialog.action.FontAction;
import de.kuehweg.sqltool.dialog.action.SchemaTreeBuilderTask;
import de.kuehweg.sqltool.dialog.images.ImagePack;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.hsqldb.server.ServerAcl;

public class iTrySQLController
        implements Initializable, SQLHistoryKeeper {

    @FXML //  fx:id="accordionCodeTemplateList"
    private ListView<CodeHelp> accordionCodeTemplateList; // Value injected by FXMLLoader
    @FXML //  fx:id="accordionCodeTemplates"
    private TitledPane accordionCodeTemplates; // Value injected by FXMLLoader
    @FXML //  fx:id="accordionPreferences"
    private TitledPane accordionPreferences; // Value injected by FXMLLoader
    @FXML //  fx:id="accordionSchemaTreeView"
    private TitledPane accordionSchemaTreeView; // Value injected by FXMLLoader
    @FXML //  fx:id="autoCommit"
    private CheckBox autoCommit; // Value injected by FXMLLoader
    @FXML //  fx:id="connectionDbName"
    private TextField connectionDbName; // Value injected by FXMLLoader
    @FXML //  fx:id="connectionDirectoryChoice"
    private Button connectionDirectoryChoice; // Value injected by FXMLLoader
    @FXML //  fx:id="connectionName"
    private TextField connectionName; // Value injected by FXMLLoader
    @FXML //  fx:id="connectionSelection"
    private ComboBox<ConnectionSetting> connectionSelection; // Value injected by FXMLLoader
    @FXML //  fx:id="connectionType"
    private ComboBox<JDBCType> connectionType; // Value injected by FXMLLoader
    @FXML //  fx:id="connectionUrl"
    private TextField connectionUrl; // Value injected by FXMLLoader
    @FXML //  fx:id="connectionUser"
    private TextField connectionUser; // Value injected by FXMLLoader
    @FXML //  fx:id="dbOutput"
    private TextArea dbOutput; // Value injected by FXMLLoader
    @FXML //  fx:id="executionProgressIndicator"
    private ProgressBar executionProgressIndicator; // Value injected by FXMLLoader
    @FXML //  fx:id="executionTime"
    private Label executionTime; // Value injected by FXMLLoader
    @FXML //  fx:id="limitMaxRows"
    private CheckBox limitMaxRows; // Value injected by FXMLLoader
    @FXML //  fx:id="menuBar"
    private MenuBar menuBar; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemCheckpoint"
    private MenuItem menuItemCheckpoint; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemClose"
    private MenuItem menuItemClose; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemCommit"
    private MenuItem menuItemCommit; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemConnect"
    private MenuItem menuItemConnect; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemCopy"
    private MenuItem menuItemCopy; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemCut"
    private MenuItem menuItemCut; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemExecute"
    private MenuItem menuItemExecute; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemExecuteScript"
    private MenuItem menuItemExecuteScript; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemFileOpenScript"
    private MenuItem menuItemFileOpenScript; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemFileSaveScript"
    private MenuItem menuItemFileSaveScript; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemPaste"
    private MenuItem menuItemPaste; // Value injected by FXMLLoader
    @FXML //  fx:id="menuItemRollback"
    private MenuItem menuItemRollback; // Value injected by FXMLLoader
    @FXML //  fx:id="refreshTree"
    private Button refreshTree; // Value injected by FXMLLoader
    @FXML //  fx:id="resultTableContainer"
    private HBox resultTableContainer; // Value injected by FXMLLoader
    @FXML //  fx:id="schemaTreeView"
    private TreeView<?> schemaTreeView; // Value injected by FXMLLoader
    @FXML //  fx:id="serverConnectionSelection"
    private ComboBox<ConnectionSetting> serverConnectionSelection; // Value injected by FXMLLoader
    @FXML //  fx:id="sqlHistory"
    private TableView<SQLHistory> sqlHistory; // Value injected by FXMLLoader
    @FXML //  fx:id="sqlHistoryColumnStatement"
    private TableColumn<SQLHistory, String> sqlHistoryColumnStatement; // Value injected by FXMLLoader
    @FXML //  fx:id="sqlHistoryColumnTimestamp"
    private TableColumn<SQLHistory, String> sqlHistoryColumnTimestamp; // Value injected by FXMLLoader
    @FXML //  fx:id="startServer"
    private Button startServer; // Value injected by FXMLLoader
    @FXML //  fx:id="shutdownServer"
    private Button shutdownServer; // Value injected by FXMLLoader
    @FXML //  fx:id="statementInput"
    private TextArea statementInput; // Value injected by FXMLLoader
    @FXML //  fx:id="tabDbOutput"
    private Tab tabDbOutput; // Value injected by FXMLLoader
    @FXML //  fx:id="tabHistory"
    private Tab tabHistory; // Value injected by FXMLLoader
    @FXML //  fx:id="tabPaneProtocols"
    private TabPane tabPaneProtocols; // Value injected by FXMLLoader
    @FXML //  fx:id="tabResult"
    private Tab tabResult; // Value injected by FXMLLoader
    @FXML //  fx:id="toolBar"
    private ToolBar toolBar; // Value injected by FXMLLoader
    @FXML //  fx:id="toolbarCheckpoint"
    private Button toolbarCheckpoint; // Value injected by FXMLLoader
    @FXML //  fx:id="toolbarCommit"
    private Button toolbarCommit; // Value injected by FXMLLoader
    @FXML //  fx:id="toolbarExecute"
    private Button toolbarExecute; // Value injected by FXMLLoader
    @FXML //  fx:id="toolbarRollback"
    private Button toolbarRollback; // Value injected by FXMLLoader
    @FXML //  fx:id="toolbarTabDbOutputClear"
    private Button toolbarTabDbOutputClear; // Value injected by FXMLLoader
    @FXML //  fx:id="toolbarTutorialData"
    private Button toolbarTutorialData; // Value injected by FXMLLoader
    @FXML //  fx:id="toolbarZoomIn"
    private Button toolbarZoomIn; // Value injected by FXMLLoader
    @FXML //  fx:id="toolbarZoomOut"
    private Button toolbarZoomOut; // Value injected by FXMLLoader
    @FXML //  fx:id="connectionSettings"
    private VBox connectionSettings; // Value injected by FXMLLoader
    @FXML //  fx:id="createConnection"
    private Button createConnection; // Value injected by FXMLLoader
    @FXML //  fx:id="editConnection"
    private Button editConnection; // Value injected by FXMLLoader
    @FXML //  fx:id="removeConnection"
    private Button removeConnection; // Value injected by FXMLLoader
    @FXML //  fx:id="permanentMessage"
    private Label permanentMessage; // Value injected by FXMLLoader
    @FXML //  fx:id="serverAlias"
    private TextField serverAlias; // Value injected by FXMLLoader
    // my own special creation
    private ConnectionHolder connectionHolder;
    private ObservableList<SQLHistory> sqlHistoryItems;

    // Handler for MenuItem[javafx.scene.control.MenuItem@e6702f4] onAction
    public void about(ActionEvent event) {
        License aboutBox = new License();
        aboutBox.showAndWait();
    }

    // Handler for CheckBox[fx:id="autoCommit"] onAction
    public void autoCommit(ActionEvent event) {
        // Auto-Commit ist keine echte Benutzereinstellung sondern wird pro Verbindung gesteuert, z.T. auch durch JDBC-Vorgaben
        if (getConnectionHolder().getConnection() == null) {
            AlertBox msg = new AlertBox(DialogDictionary.MESSAGEBOX_WARNING.
                    toString(),
                    DialogDictionary.MSG_NO_DB_CONNECTION.toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        } else {
            try {
                connectionHolder.getConnection().setAutoCommit(autoCommit.
                        isSelected());
            } catch (SQLException ex) {
                ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_AUTO_COMMIT_FAILURE.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
        }
    }

    // Handler for Button[fx:id="toolbarCheckpoint"] onAction
    // Handler for MenuItem[fx:id="menuItemCheckpoint"] onAction
    public void checkpoint(ActionEvent event) {
        ExecuteAction.
                handleExecuteAction(menuBar.getScene(), this, "CHECKPOINT");
    }

    // Handler for MenuItem[fx:id="menuItemCopy"] onAction
    public void clipboardCopy(ActionEvent event) {
        // Clipboard clipboard = Clipboard.getSystemClipboard();
        // final ClipboardContent content = new ClipboardContent();
        // content.putString(statementInput.getSelectedText());
        // clipboard.setContent(content);
    }

    // Handler for MenuItem[fx:id="menuItemCut"] onAction
    public void clipboardCut(ActionEvent event) {
        // Clipboard clipboard = Clipboard.getSystemClipboard();
        // final ClipboardContent content = new ClipboardContent();
        // content.putString(statementInput.getSelectedText());
        // clipboard.setContent(content);
        // statementInput.replaceSelection("");
    }

    // Handler for MenuItem[fx:id="menuItemPaste"] onAction
    public void clipboardPaste(ActionEvent event) {
        // Clipboard clipboard = Clipboard.getSystemClipboard();
        // String content = (String)clipboard.getContent(DataFormat.PLAIN_TEXT);
        // statementInput.replaceSelection(content);
    }

    // Handler for Button[fx:id="toolbarCommit"] onAction
    // Handler for MenuItem[fx:id="menuItemCommit"] onAction
    public void commit(ActionEvent event) {
        ExecuteAction.handleExecuteAction(menuBar.getScene(), this, "COMMIT");
    }

    // Handler for MenuItem[fx:id="menuItemConnect"] onAction
    public void connect(ActionEvent event) {
        ConnectionDialog connectionDialog = new ConnectionDialog();
        connectionDialog.showAndWait();
        ConnectionSetting connectionSetting = connectionDialog.
                getConnectionSetting();
        if (connectionSetting != null) {
            getConnectionHolder().connect(connectionSetting);
            controlAutoCommitCheckBoxState();
            refreshTree(event);
            if (connectionSetting.getType() == JDBCType.HSQL_IN_MEMORY) {
                permanentMessage.setText(MessageFormat.format(
                        DialogDictionary.PATTERN_MESSAGE_IN_MEMORY_DATABASE.
                        toString(), connectionSetting.getName()));
                permanentMessage.visibleProperty().set(true);
            } else {
                permanentMessage.visibleProperty().set(false);
            }
        }
    }

    // Handler for Button[fx:id="toolbarExecute"] onAction
    // Handler for MenuItem[fx:id="menuItemExecute"] onAction
    public void execute(ActionEvent event) {
        // highlighted text to be executed ?
        String sql = statementInput.getSelectedText();
        // current statement to be executed
        if (sql.trim().length() == 0) {
            sql = StatementExtractor.extractStatementAtCaretPosition(
                    statementInput.getText(),
                    statementInput.getCaretPosition());
        }
        ExecuteAction.handleExecuteAction(menuBar.getScene(), this, sql);
    }

    // Handler for MenuItem[fx:id="menuItemExecuteScript"] onAction
    public void executeScript(ActionEvent event) {
        ExecuteAction.handleExecuteAction(menuBar.getScene(), this,
                statementInput.getText());
    }

    // Handler for MenuItem[fx:id="menuItemFileOpenScript"] onAction
    public void fileOpenScriptAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(DialogDictionary.LABEL_OPEN_SCRIPT.toString());
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try {
                String script = FileUtil.readFile(file.getAbsolutePath());
                statementInput.setText(script);
            } catch (IOException ex) {
                ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_FILE_OPEN_FAILED.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
        }
    }

    // Handler for MenuItem[fx:id="menuItemFileSaveScript"] onAction
    public void fileSaveScriptAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(DialogDictionary.LABEL_OPEN_SCRIPT.toString());
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                FileUtil.writeFile(file.getAbsolutePath(), statementInput.
                        getText());
            } catch (IOException ex) {
                ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_FILE_SAVE_FAILED.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();

            }
        }
    }

    // Handler for Button[fx:id="toolbarZoomIn"] onAction
    // Handler for Button[fx:id="toolbarZoomOut"] onAction
    public void fontAction(ActionEvent event) {
        FontAction.handleFontAction(event);
    }

    // Handler for CheckBox[fx:id="limitMaxRows"] onAction
    public void limitMaxRows(ActionEvent event) {
        UserPreferencesManager.getSharedInstance().setLimitMaxRows(limitMaxRows.
                isSelected());
    }

    // Handler for MenuItem[fx:id="menuItemClose"] onAction
    public void quit(ActionEvent event) {
        Platform.exit();
    }

    // Handler for Button[fx:id="refreshTree"] onAction
    public void refreshTree(ActionEvent event) {
        Task refreshTask = new SchemaTreeBuilderTask(
                getConnectionHolder().getConnection(), schemaTreeView);
        Thread th = new Thread(refreshTask);
        th.setDaemon(true);
        th.start();
    }

    // Handler for Button[fx:id="toolbarRollback"] onAction
    // Handler for MenuItem[fx:id="menuItemRollback"] onAction
    public void rollback(ActionEvent event) {
        ExecuteAction.handleExecuteAction(menuBar.getScene(), this, "ROLLBACK");
    }

    // Handler for ImageView[fx:id="toolbarTabDbOutputClear"] onMouseClicked
    public void toolbarTabDbOutputClearAction(ActionEvent event) {
        dbOutput.clear();
    }

    // Handler for ImageView[fx:id="toolbarTutorialData"] onMouseClicked
    public void tutorialAction(ActionEvent event) {
        ConfirmDialog confirm = new ConfirmDialog(
                DialogDictionary.MESSAGEBOX_CONFIRM.toString(),
                DialogDictionary.MSG_REALLY_CREATE_TUTORIAL_DATA.toString(),
                DialogDictionary.LABEL_CREATE_TUTORIAL_DATA.toString(),
                DialogDictionary.COMMON_BUTTON_CANCEL.toString());
        if (DialogDictionary.LABEL_CREATE_TUTORIAL_DATA.toString().equals(
                confirm.askUserFeedback())) {
            try {
                InputStream tutorialStream = getClass().getResourceAsStream(
                        "tutorial.sql");
                final StringBuffer b;
                try (InputStreamReader reader =
                        new InputStreamReader(tutorialStream, "UTF-8"); BufferedReader bufferedReader =
                        new BufferedReader(reader)) {
                    b = new StringBuffer();
                    String s = null;
                    while ((s = bufferedReader.readLine()) != null) {
                        b.append(s);
                        b.append('\n');
                    }
                }
                String tutorialSql = b.toString();
                ExecuteAction.handleExecuteActionSilently(menuBar.getScene(),
                        this,
                        tutorialSql);
            } catch (IOException ex) {
                ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_TUTORIAL_CREATION_FAILED + " ("
                        + ex.
                        getLocalizedMessage() + ")",
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
        }
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void cancelConnectionSettings(ActionEvent event) {
        connectionSelection.valueProperty().set(null);
        controlConnectionSettingsVisibility();
    }

    // Handler for ComboBox[fx:id="connectionSelection"] onAction
    public void changeConnection(ActionEvent event) {
        controlConnectionSettingsVisibility();
        ConnectionSetting setting = connectionSelection.getValue();
        putConnectionSettingInDialog(setting);
    }

    // Handler for ComboBox[fx:id="connectionType"] onAction
    public void changeConnectionType(ActionEvent event) {
        ConnectionSetting setting = connectionSelection.getValue();
        JDBCType type = connectionType.getValue();
        if (type != null) {
            connectionUrl.setText(null);
            connectionDbName.setText(null);
            connectionDirectoryChoice.disableProperty().set(type
                    != JDBCType.HSQL_STANDALONE);
        }
    }

    // Handler for Button[fx:id="connectionDirectoryChoice"] onAction
    public void chooseDbDirectory(ActionEvent event) {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle(DialogDictionary.LABEL_DB_DIRECTORY_CHOOSER.
                toString());
        File dir = dirChooser.showDialog(null);
        if (dir != null) {
            JDBCType type = connectionType.valueProperty().get();
            if (type != null) {
                String dbSource = dir.getAbsolutePath();
                connectionUrl.setText(dbSource);
            }
        }
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void createConnection(ActionEvent event) {
        String name = createValidNewConnectionName(
                DialogDictionary.PATTERN_NEW_CONNECTION_NAME.toString());
        ConnectionSetting setting = new ConnectionSetting(name,
                JDBCType.HSQL_IN_MEMORY, "", "johndoe", null, null);
        putConnectionSettingInDialog(setting);
        connectionSettings.visibleProperty().set(true);
        editConnection.disableProperty().set(true);
        removeConnection.disableProperty().set(true);
        enableConnectionSettingsControls(true);
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void editConnection(ActionEvent event) {
        ConnectionSetting setting = connectionSelection.getValue();
        putConnectionSettingInDialog(setting);
        controlConnectionSettingsVisibility();
        enableConnectionSettingsControls(true);
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void removeConnection(ActionEvent event) {
        ConfirmDialog confirm = new ConfirmDialog(
                DialogDictionary.MESSAGEBOX_CONFIRM.toString(),
                DialogDictionary.MSG_REALLY_REMOVE_CONNECTION.toString(),
                DialogDictionary.LABEL_REMOVE_CONNECTION.toString(),
                DialogDictionary.COMMON_BUTTON_CANCEL.toString());
        String confirmation = confirm.askUserFeedback();
        if (DialogDictionary.LABEL_REMOVE_CONNECTION.toString().equals(
                confirmation)) {
            try {
                ConnectionSettings settings = new ConnectionSettings();
                settings.getConnectionSettingsAfterRemovalOf(
                        getConnectionSettingFromDialog());
            } catch (BackingStoreException ex) {
                ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_CONNECTION_SETTING_SAVE_FAILED.
                        toString(), DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
            prepareConnectionSettings();
        }
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void saveConnectionSettings(ActionEvent event) {
        ConnectionSetting setting = getConnectionSettingFromDialog();
        ConnectionSettings settings = new ConnectionSettings();
        try {
            settings.getConnectionSettingsAfterAdditionOf(setting);
        } catch (BackingStoreException ex) {
            ErrorMessage msg = new ErrorMessage(
                    DialogDictionary.MESSAGEBOX_ERROR.toString(),
                    DialogDictionary.ERR_CONNECTION_SETTING_SAVE_FAILED.
                    toString(), DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        }
        prepareConnectionSettings();
    }

    // Handler for ComboBox[fx:id="serverConnectionSelection"] onAction
    public void changeServerConnection(ActionEvent event) {
        ConnectionSetting connectionSetting = serverConnectionSelection.
                getValue();
        serverAlias.setText(connectionSetting != null ? connectionSetting.
                getDbName() : null);
    }

    // Handler for Button[fx:id="startServer"] onAction
    public void startServer(ActionEvent event) {
        try {
            ConnectionSetting connectionSetting = serverConnectionSelection.
                    getValue();
            ServerManager.getSharedInstance().startServer(connectionSetting,
                    serverAlias.getText());
        } catch (IOException | ServerAcl.AclFormatException | IllegalArgumentException ex) {
            ErrorMessage msg = new ErrorMessage(
                    DialogDictionary.MESSAGEBOX_ERROR.toString(),
                    DialogDictionary.ERR_SERVER_START_FAILED.toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        }
    }

    // Handler for Button[fx:id="shutdownServer"] onAction
    public void shutdownServer(ActionEvent event) {
        try {
            ServerManager.getSharedInstance().shutdownServer();
        } catch (Throwable ex) {
            ErrorMessage msg = new ErrorMessage(
                    DialogDictionary.MESSAGEBOX_ERROR.toString(),
                    DialogDictionary.ERR_SERVER_START_FAILED.toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        }
    }

    @Override // This method is called by the FXMLLoader when initialization is complete
    public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
        assert accordionCodeTemplateList != null : "fx:id=\"accordionCodeTemplateList\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert accordionCodeTemplates != null : "fx:id=\"accordionCodeTemplates\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert accordionPreferences != null : "fx:id=\"accordionPreferences\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert accordionSchemaTreeView != null : "fx:id=\"accordionSchemaTreeView\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert autoCommit != null : "fx:id=\"autoCommit\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert connectionDbName != null : "fx:id=\"connectionDbName\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert connectionDirectoryChoice != null : "fx:id=\"connectionDirectoryChoice\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert connectionName != null : "fx:id=\"connectionName\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert connectionSelection != null : "fx:id=\"connectionSelection\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert connectionSettings != null : "fx:id=\"connectionSettings\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert connectionType != null : "fx:id=\"connectionType\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert connectionUrl != null : "fx:id=\"connectionUrl\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert connectionUser != null : "fx:id=\"connectionUser\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert createConnection != null : "fx:id=\"createConnection\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert dbOutput != null : "fx:id=\"dbOutput\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert editConnection != null : "fx:id=\"editConnection\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert executionProgressIndicator != null : "fx:id=\"executionProgressIndicator\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert executionTime != null : "fx:id=\"executionTime\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert limitMaxRows != null : "fx:id=\"limitMaxRows\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuBar != null : "fx:id=\"menuBar\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemCheckpoint != null : "fx:id=\"menuItemCheckpoint\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemClose != null : "fx:id=\"menuItemClose\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemCommit != null : "fx:id=\"menuItemCommit\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemConnect != null : "fx:id=\"menuItemConnect\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemCopy != null : "fx:id=\"menuItemCopy\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemCut != null : "fx:id=\"menuItemCut\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemExecute != null : "fx:id=\"menuItemExecute\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemExecuteScript != null : "fx:id=\"menuItemExecuteScript\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemFileOpenScript != null : "fx:id=\"menuItemFileOpenScript\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemFileSaveScript != null : "fx:id=\"menuItemFileSaveScript\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemPaste != null : "fx:id=\"menuItemPaste\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert menuItemRollback != null : "fx:id=\"menuItemRollback\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert permanentMessage != null : "fx:id=\"permanentMessage\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert refreshTree != null : "fx:id=\"refreshTree\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert removeConnection != null : "fx:id=\"removeConnection\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert resultTableContainer != null : "fx:id=\"resultTableContainer\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert schemaTreeView != null : "fx:id=\"schemaTreeView\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert serverAlias != null : "fx:id=\"serverAlias\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert serverConnectionSelection != null : "fx:id=\"serverConnectionSelection\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert shutdownServer != null : "fx:id=\"shutdownServer\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert sqlHistory != null : "fx:id=\"sqlHistory\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert sqlHistoryColumnStatement != null : "fx:id=\"sqlHistoryColumnStatement\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert sqlHistoryColumnTimestamp != null : "fx:id=\"sqlHistoryColumnTimestamp\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert startServer != null : "fx:id=\"startServer\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert statementInput != null : "fx:id=\"statementInput\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert tabDbOutput != null : "fx:id=\"tabDbOutput\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert tabHistory != null : "fx:id=\"tabHistory\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert tabPaneProtocols != null : "fx:id=\"tabPaneProtocols\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert tabResult != null : "fx:id=\"tabResult\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolBar != null : "fx:id=\"toolBar\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarCheckpoint != null : "fx:id=\"toolbarCheckpoint\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarCommit != null : "fx:id=\"toolbarCommit\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarExecute != null : "fx:id=\"toolbarExecute\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarRollback != null : "fx:id=\"toolbarRollback\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarTabDbOutputClear != null : "fx:id=\"toolbarTabDbOutputClear\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarTutorialData != null : "fx:id=\"toolbarTutorialData\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarZoomIn != null : "fx:id=\"toolbarZoomIn\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarZoomOut != null : "fx:id=\"toolbarZoomOut\" was not injected: check your FXML file 'iTrySQL.fxml'.";

        // initialize your logic here: all @FXML variables will have been injected
        initializeContinued();
    }

    private void initializeContinued() {
        prepareConnectionSettings();
        fillCodeTemplates();
        prepareHistory();
        statementInput.setStyle("-fx-font-size: " + (UserPreferencesManager.
                getSharedInstance().getFontSize()) + ";");
        dbOutput.setStyle("-fx-font-size: " + (UserPreferencesManager.
                getSharedInstance().getFontSize()) + ";");

        limitMaxRows.setSelected(UserPreferencesManager.getSharedInstance().
                isLimitMaxRows());

        setupToolbarButtons();
        setupMenuAccelerators();

        controlAutoCommitCheckBoxState();
        controlServerStatusButtonStates();
        permanentMessage.visibleProperty().set(false);
        refreshTree(null);
    }

    private void setupToolbarButtons() {
        toolbarExecute.setGraphic(new ImageView(ImagePack.EXECUTE.getAsImage()));
        toolbarExecute.setText(null);
        Tooltip.install(toolbarExecute, new Tooltip(
                DialogDictionary.TOOLTIP_EXECUTE.toString()));

        toolbarCommit.setGraphic(new ImageView(ImagePack.COMMIT.getAsImage()));
        toolbarCommit.setText(null);
        Tooltip.install(toolbarCommit, new Tooltip(
                DialogDictionary.TOOLTIP_COMMIT.toString()));

        toolbarCheckpoint.setGraphic(new ImageView(ImagePack.CHECKPOINT.
                getAsImage()));
        toolbarCheckpoint.setText(null);
        Tooltip.install(toolbarCheckpoint, new Tooltip(
                DialogDictionary.TOOLTIP_CHECKPOINT.toString()));

        toolbarRollback.setGraphic(
                new ImageView(ImagePack.ROLLBACK.getAsImage()));
        toolbarRollback.setText(null);
        Tooltip.install(toolbarRollback, new Tooltip(
                DialogDictionary.TOOLTIP_ROLLBACK.toString()));

        toolbarZoomOut.setGraphic(new ImageView(ImagePack.ZOOMOUT.getAsImage()));
        toolbarZoomOut.setText(null);
        Tooltip.install(toolbarZoomOut, new Tooltip(
                DialogDictionary.TOOLTIP_DECREASE_FONTSIZE.toString()));

        toolbarZoomIn.setGraphic(new ImageView(ImagePack.ZOOMIN.getAsImage()));
        toolbarZoomIn.setText(null);
        Tooltip.install(toolbarZoomIn, new Tooltip(
                DialogDictionary.TOOLTIP_INCREASE_FONTSIZE.toString()));

        toolbarTutorialData.setGraphic(new ImageView(ImagePack.TUTORIAL_DATA.
                getAsImage()));
        toolbarTutorialData.setText(null);
        Tooltip.install(toolbarTutorialData, new Tooltip(
                DialogDictionary.TOOLTIP_TUTORIAL_DATA.toString()));

        toolbarTabDbOutputClear.setGraphic(new ImageView(ImagePack.CLEAR.
                getAsImage()));
        toolbarTabDbOutputClear.setText(null);
        Tooltip.install(toolbarTabDbOutputClear, new Tooltip(
                DialogDictionary.TOOLTIP_CLEAR_OUTPUT.toString()));
    }

    private void setupMenuAccelerators() {
        menuItemExecute.setAccelerator(
                new KeyCodeCombination(
                KeyCode.ENTER,
                KeyCombination.SHORTCUT_DOWN));
        menuItemExecuteScript.setAccelerator(
                new KeyCodeCombination(
                KeyCode.ENTER,
                KeyCombination.SHORTCUT_DOWN,
                KeyCombination.SHIFT_DOWN));
        menuItemFileOpenScript.setAccelerator(
                new KeyCodeCombination(
                KeyCode.O,
                KeyCombination.SHORTCUT_DOWN));
        menuItemFileSaveScript.setAccelerator(
                new KeyCodeCombination(
                KeyCode.S,
                KeyCombination.SHORTCUT_DOWN));
    }

    private void controlAutoCommitCheckBoxState() {
        if (getConnectionHolder().getConnection() != null) {
            try {
                autoCommit.setSelected(connectionHolder.getConnection().
                        getAutoCommit());
            } catch (SQLException ex) {
                autoCommit.setSelected(true);
            }
        }
    }

    private void controlServerStatusButtonStates() {
        startServer.disableProperty().set(false);
        shutdownServer.disableProperty().set(true);
        Task serverStatusTask;
        serverStatusTask =
                new Task() {
            @Override
            protected Object call() throws Exception {
                while (!isCancelled()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            boolean running = ServerManager.getSharedInstance().
                                    isRunning();
                            startServer.disableProperty().set(running);
                            shutdownServer.disableProperty().set(!running);
                        }
                    });
                    Thread.sleep(1000);
                }
                return null;
            }
        };
        Thread th = new Thread(serverStatusTask);
        th.setDaemon(true);
        th.start();
    }

    private void fillCodeTemplates() {
        ObservableList<CodeHelp> templateListViewItems = FXCollections.
                observableArrayList();
        for (final CodeHelp template : CodeHelp.values()) {
            templateListViewItems.add(template);
        }
        accordionCodeTemplateList.setItems(templateListViewItems);
        accordionCodeTemplateList.getSelectionModel().selectedItemProperty().
                addListener(
                new ChangeListener<CodeHelp>() {
            @Override
            public void changed(ObservableValue<? extends CodeHelp> ov,
                    CodeHelp oldValue, CodeHelp newValue) {
                StringBuilder statementTemplate = new StringBuilder();
                if (newValue.getSyntaxDescription() != null && newValue.
                        getSyntaxDescription().trim().length() > 0) {
                    statementTemplate
                            .append("-- ")
                            .append(
                            DialogDictionary.LABEL_SEE_SYNTAX_IN_DBOUTPUT.
                            toString())
                            .append("\n");
                    dbOutput.appendText("\n" + newValue.getSyntaxDescription()
                            + "\n");
                }
                statementTemplate.append(newValue.getCodeTemplate());
                statementInput.insertText(statementInput.getCaretPosition(),
                        statementTemplate.toString());
            }
        });
    }

    private void prepareHistory() {
        sqlHistoryItems = FXCollections.observableArrayList();
        sqlHistoryColumnTimestamp.setCellValueFactory(
                new PropertyValueFactory<SQLHistory, String>("timestamp"));
        sqlHistoryColumnStatement.setCellValueFactory(
                new PropertyValueFactory<SQLHistory, String>("sqlForDisplay"));
        sqlHistory.setItems(sqlHistoryItems);
        sqlHistory.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<SQLHistory>() {
            @Override
            public void changed(ObservableValue<? extends SQLHistory> ov,
                    SQLHistory oldValue, SQLHistory newValue) {
                if (newValue != null) {
                    statementInput.appendText(newValue.getOriginalSQL());
                }
                sqlHistory.getSelectionModel().clearSelection();
            }
        });
    }

    private void prepareConnectionSettings() {
        fillConnectionSettings();
        fillJdbcTypes();
        controlConnectionSettingsVisibility();
        enableConnectionSettingsControls(false);
    }

    private void controlConnectionSettingsVisibility() {
        final boolean empty = connectionSelection.valueProperty().get() == null;
        connectionSettings.visibleProperty().set(!empty);
        removeConnection.disableProperty().set(empty);
        editConnection.disableProperty().set(empty);
    }

    private void enableConnectionSettingsControls(final boolean enabled) {
        connectionName.disableProperty().set(!enabled);
        connectionType.disableProperty().set(!enabled);
        connectionUrl.disableProperty().set(!enabled);
        connectionDbName.disableProperty().set(!enabled);
        connectionUser.disableProperty().set(!enabled);
    }

    private void putConnectionSettingInDialog(final ConnectionSetting setting) {
        if (setting == null) {
            connectionSettings.visibleProperty().set(false);
        } else {
            connectionName.setText(setting.getName());
            connectionType.valueProperty().set(setting.getType());
            connectionUrl.setText(setting.getDbPath());
            connectionDbName.setText(setting.getDbName());
            connectionUser.setText(setting.getUser());
        }
    }

    private ConnectionSetting getConnectionSettingFromDialog() {
        ConnectionSetting setting = new ConnectionSetting(connectionName.
                getText(),
                connectionType.getValue(), connectionUrl.getText(),
                connectionDbName.getText(),
                connectionUser.getText(), null);
        return setting;
    }

    private void fillConnectionSettings() {
        ConnectionSettings settings = new ConnectionSettings();
        ObservableList<ConnectionSetting> localSettings = FXCollections.
                observableArrayList();
        ObservableList<ConnectionSetting> serverSettings = FXCollections.
                observableArrayList();
        for (final ConnectionSetting connectionSetting : settings.
                getConnectionSettings()) {
            localSettings.add(connectionSetting);
            if (connectionSetting.getType().isPossibleServer()) {
                serverSettings.add(connectionSetting);
            }
        }
        connectionSelection.setItems(localSettings);
        serverConnectionSelection.setItems(serverSettings);
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
                        + " (" + (count++) + ")";
            } while (connectionNameAlreadyUsed(name));
        }
        return name;
    }

    private boolean connectionNameAlreadyUsed(final String name) {
        if (name == null) {
            return true;
        }
        ConnectionSettings settings = new ConnectionSettings();
        for (final ConnectionSetting setting : settings.getConnectionSettings()) {
            if (setting.getName() != null && setting.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void addExecutedSQLToHistory(final String sql) {
        if (sql != null && sql.trim().length() > 0) {
            sqlHistoryItems.add(0, new SQLHistory(sql));
        }
    }

    public ConnectionHolder getConnectionHolder() {
        if (connectionHolder == null) {
            connectionHolder = new ConnectionHolder();
        }
        return connectionHolder;
    }
}
