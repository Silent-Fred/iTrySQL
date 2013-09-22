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
import javafx.beans.binding.Bindings;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.hsqldb.server.ServerAcl;

public class iTrySQLController implements Initializable, SQLHistoryKeeper {

    @FXML
    private ListView<CodeHelp> accordionCodeTemplateList;
    @FXML
    private TitledPane accordionCodeTemplates;
    @FXML
    private TitledPane accordionPreferences;
    @FXML
    private TitledPane accordionSchemaTreeView;
    @FXML
    private CheckBox autoCommit;
    @FXML
    private TextField connectionDbName;
    @FXML
    private Button connectionDirectoryChoice;
    @FXML
    private TextField connectionName;
    @FXML
    private ComboBox<ConnectionSetting> connectionSelection;
    @FXML
    private ComboBox<JDBCType> connectionType;
    @FXML
    private TextField connectionUrl;
    @FXML
    private TextField connectionUser;
    @FXML
    private TextArea dbOutput;
    @FXML
    private ProgressBar executionProgressIndicator;
    @FXML
    private Label executionTime;
    @FXML
    private CheckBox limitMaxRows;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem menuItemCheckpoint;
    @FXML
    private MenuItem menuItemClose;
    @FXML
    private MenuItem menuItemCommit;
    @FXML
    private MenuItem menuItemConnect;
    @FXML
    private MenuItem menuItemDisconnect;
    @FXML
    private MenuItem menuItemCopy;
    @FXML
    private MenuItem menuItemCut;
    @FXML
    private MenuItem menuItemExecute;
    @FXML
    private MenuItem menuItemExecuteScript;
    @FXML
    private MenuItem menuItemFileOpenScript;
    @FXML
    private MenuItem menuItemFileSaveScript;
    @FXML
    private MenuItem menuItemPaste;
    @FXML
    private MenuItem menuItemRollback;
    @FXML
    private Button refreshTree;
    @FXML
    private HBox resultTableContainer;
    @FXML
    private TreeView<?> schemaTreeView;
    @FXML
    private ComboBox<ConnectionSetting> serverConnectionSelection;
    @FXML
    private TableView<SQLHistory> sqlHistory;
    @FXML
    private TableColumn<SQLHistory, String> sqlHistoryColumnStatement;
    @FXML
    private TableColumn<SQLHistory, String> sqlHistoryColumnTimestamp;
    @FXML
    private Button startServer;
    @FXML
    private Button shutdownServer;
    @FXML
    private TextArea statementInput;
    @FXML
    private Tab tabDbOutput;
    @FXML
    private Tab tabHistory;
    @FXML
    private TabPane tabPaneProtocols;
    @FXML
    private Tab tabResult;
    @FXML
    private ToolBar toolBar;
    @FXML
    private Button toolbarCheckpoint;
    @FXML
    private Button toolbarCommit;
    @FXML
    private Button toolbarExecute;
    @FXML
    private Button toolbarRollback;
    @FXML
    private Button toolbarTabDbOutputClear;
    @FXML
    private Button toolbarTabDbOutputExport;
    @FXML
    private Button toolbarTutorialData;
    @FXML
    private Button toolbarZoomIn;
    @FXML
    private Button toolbarZoomOut;
    @FXML
    private VBox connectionSettings;
    @FXML
    private Button createConnection;
    @FXML
    private Button editConnection;
    @FXML
    private Button removeConnection;
    @FXML
    private Label permanentMessage;
    @FXML
    private TextField serverAlias;
    // my own special creation
    private ConnectionHolder connectionHolder;
    private ObservableList<SQLHistory> sqlHistoryItems;
    private BooleanProperty connectionSettingsEdit;
    private BooleanProperty connectionSettingFileBased;

    @Override
    // This method is called by the FXMLLoader when initialization is complete
    public void initialize(final URL fxmlFileLocation,
            final ResourceBundle resources) {
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
        assert menuItemDisconnect != null : "fx:id=\"menuItemDisconnect\" was not injected: check your FXML file 'iTrySQL.fxml'.";
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
        assert toolbarTabDbOutputExport != null : "fx:id=\"toolbarTabDbOutputExport\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarTutorialData != null : "fx:id=\"toolbarTutorialData\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarZoomIn != null : "fx:id=\"toolbarZoomIn\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarZoomOut != null : "fx:id=\"toolbarZoomOut\" was not injected: check your FXML file 'iTrySQL.fxml'.";

        // initialize your logic here: all @FXML variables will have been
        // injected
        initializeContinued();
    }

    // Handler for MenuItem[javafx.scene.control.MenuItem@e6702f4] onAction
    public void about(final ActionEvent event) {
        final License aboutBox = new License();
        aboutBox.showAndWait();
    }

    // Handler for CheckBox[fx:id="autoCommit"] onAction
    public void autoCommit(final ActionEvent event) {
        // Auto-Commit ist keine echte Benutzereinstellung sondern wird pro
        // Verbindung gesteuert, z.T. auch durch JDBC-Vorgaben
        if (getConnectionHolder().getConnection() == null) {
            final AlertBox msg = new AlertBox(
                    DialogDictionary.MESSAGEBOX_WARNING.toString(),
                    DialogDictionary.MSG_NO_DB_CONNECTION.toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        } else {
            try {
                connectionHolder.getConnection().setAutoCommit(
                        autoCommit.isSelected());
            } catch (final SQLException ex) {
                final ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_AUTO_COMMIT_FAILURE.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
        }
    }

    // Handler for Button[fx:id="toolbarCheckpoint"] onAction
    // Handler for MenuItem[fx:id="menuItemCheckpoint"] onAction
    public void checkpoint(final ActionEvent event) {
        ExecuteAction.handleExecuteAction(menuBar.getScene(), this,
                "CHECKPOINT");
    }

    // Handler for MenuItem[fx:id="menuItemCopy"] onAction
    public void clipboardCopy(final ActionEvent event) {
        // Clipboard clipboard = Clipboard.getSystemClipboard();
        // final ClipboardContent content = new ClipboardContent();
        // content.putString(statementInput.getSelectedText());
        // clipboard.setContent(content);
    }

    // Handler for MenuItem[fx:id="menuItemCut"] onAction
    public void clipboardCut(final ActionEvent event) {
        // Clipboard clipboard = Clipboard.getSystemClipboard();
        // final ClipboardContent content = new ClipboardContent();
        // content.putString(statementInput.getSelectedText());
        // clipboard.setContent(content);
        // statementInput.replaceSelection("");
    }

    // Handler for MenuItem[fx:id="menuItemPaste"] onAction
    public void clipboardPaste(final ActionEvent event) {
        // Clipboard clipboard = Clipboard.getSystemClipboard();
        // String content = (String)clipboard.getContent(DataFormat.PLAIN_TEXT);
        // statementInput.replaceSelection(content);
    }

    // Handler for Button[fx:id="toolbarCommit"] onAction
    // Handler for MenuItem[fx:id="menuItemCommit"] onAction
    public void commit(final ActionEvent event) {
        ExecuteAction.handleExecuteAction(menuBar.getScene(), this, "COMMIT");
    }

    // Handler for MenuItem[fx:id="menuItemConnect"] onAction
    public void connect(final ActionEvent event) {
        final ConnectionDialog connectionDialog = new ConnectionDialog();
        connectionDialog.showAndWait();
        final ConnectionSetting connectionSetting = connectionDialog
                .getConnectionSetting();
        if (connectionSetting != null) {
            getConnectionHolder().connect(connectionSetting);
            controlAutoCommitCheckBoxState();
            refreshTree(event);
            if (connectionSetting.getType() == JDBCType.HSQL_IN_MEMORY) {
                permanentMessage.setText(MessageFormat.format(
                        DialogDictionary.PATTERN_MESSAGE_IN_MEMORY_DATABASE
                        .toString(), connectionSetting.getName()));
                permanentMessage.visibleProperty().set(true);
            } else {
                permanentMessage.visibleProperty().set(false);
            }
        }
    }

    // Handler for MenuItem[fx:id="menuItemDisconnect"] onAction
    public void disconnect(final ActionEvent event) {
        getConnectionHolder().disconnect();
        refreshTree(null);
        permanentMessage.visibleProperty().set(false);
    }

    // Handler for Button[fx:id="toolbarExecute"] onAction
    // Handler for MenuItem[fx:id="menuItemExecute"] onAction
    public void execute(final ActionEvent event) {
        // highlighted text to be executed ?
        String sql = statementInput.getSelectedText();
        // current statement to be executed
        if (sql.trim().length() == 0) {
            sql = StatementExtractor
                    .extractStatementAtCaretPosition(statementInput.getText(),
                    statementInput.getCaretPosition());
        }
        ExecuteAction.handleExecuteAction(menuBar.getScene(), this, sql);
    }

    // Handler for MenuItem[fx:id="menuItemExecuteScript"] onAction
    public void executeScript(final ActionEvent event) {
        ExecuteAction.handleExecuteAction(menuBar.getScene(), this,
                statementInput.getText());
    }

    // Handler for MenuItem[fx:id="menuItemFileOpenScript"] onAction
    public void fileOpenScriptAction(final ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(DialogDictionary.LABEL_OPEN_SCRIPT.toString());
        final File file = fileChooser.showOpenDialog(menuBar.getScene()
                .getWindow());
        if (file != null) {
            try {
                final String script = FileUtil.readFile(file.getAbsolutePath());
                statementInput.setText(script);
            } catch (final IOException ex) {
                final ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_FILE_OPEN_FAILED.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
        }
    }

    // Handler for MenuItem[fx:id="menuItemFileSaveScript"] onAction
    public void fileSaveScriptAction(final ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(DialogDictionary.LABEL_SAVE_SCRIPT.toString());
        final File file = fileChooser.showSaveDialog(menuBar.getScene()
                .getWindow());
        if (file != null) {
            try {
                FileUtil.writeFile(file.getAbsolutePath(),
                        statementInput.getText());
            } catch (final IOException ex) {
                final ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_FILE_SAVE_FAILED.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();

            }
        }
    }

    // Handler for Button[fx:id="toolbarZoomIn"] onAction
    // Handler for Button[fx:id="toolbarZoomOut"] onAction
    public void fontAction(final ActionEvent event) {
        FontAction.handleFontAction(event);
    }

    // Handler for CheckBox[fx:id="limitMaxRows"] onAction
    public void limitMaxRows(final ActionEvent event) {
        UserPreferencesManager.getSharedInstance().setLimitMaxRows(
                limitMaxRows.isSelected());
    }

    // Handler for MenuItem[fx:id="menuItemClose"] onAction
    public void quit(final ActionEvent event) {
        Platform.exit();
    }

    // Handler for Button[fx:id="refreshTree"] onAction
    public void refreshTree(final ActionEvent event) {
        final Task refreshTask = new SchemaTreeBuilderTask(
                getConnectionHolder().getConnection(), schemaTreeView);
        final Thread th = new Thread(refreshTask);
        th.setDaemon(true);
        th.start();
    }

    // Handler for Button[fx:id="toolbarRollback"] onAction
    // Handler for MenuItem[fx:id="menuItemRollback"] onAction
    public void rollback(final ActionEvent event) {
        ExecuteAction.handleExecuteAction(menuBar.getScene(), this, "ROLLBACK");
    }

    // Handler for Button[fx:id="toolbarTabDbOutputClear"] onAction
    public void toolbarTabDbOutputClearAction(final ActionEvent event) {
        dbOutput.clear();
    }

    // Handler for Button[fx:id="toolbarTabDbOutputExport"] onAction
    public void toolbarTabDbOutputExportAction(final ActionEvent event) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(DialogDictionary.LABEL_SAVE_OUTPUT.toString());
        final File file = fileChooser.showSaveDialog(((Node) event.getSource())
                .getScene().getWindow());
        if (file != null) {
            try {
                FileUtil.writeFile(file.getAbsolutePath(), dbOutput.getText());
            } catch (final IOException ex) {
                final ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_FILE_SAVE_FAILED.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();

            }
        }
    }

    // Handler for Button[fx:id="toolbarTutorialData"] onAction
    public void tutorialAction(final ActionEvent event) {
        final ConfirmDialog confirm = new ConfirmDialog(
                DialogDictionary.MESSAGEBOX_CONFIRM.toString(),
                DialogDictionary.MSG_REALLY_CREATE_TUTORIAL_DATA.toString(),
                DialogDictionary.LABEL_CREATE_TUTORIAL_DATA.toString(),
                DialogDictionary.COMMON_BUTTON_CANCEL.toString());
        if (DialogDictionary.LABEL_CREATE_TUTORIAL_DATA.toString().equals(
                confirm.askUserFeedback())) {
            try {
                final InputStream tutorialStream = getClass()
                        .getResourceAsStream("/resources/sql/tutorial.sql");
                final StringBuffer b;
                try (InputStreamReader reader = new InputStreamReader(
                        tutorialStream, "UTF-8");
                        BufferedReader bufferedReader = new BufferedReader(
                        reader)) {
                    b = new StringBuffer();
                    String s = null;
                    while ((s = bufferedReader.readLine()) != null) {
                        b.append(s);
                        b.append('\n');
                    }
                }
                final String tutorialSql = b.toString();
                ExecuteAction.handleExecuteActionSilently(menuBar.getScene(),
                        this, tutorialSql);
            } catch (final IOException ex) {
                final ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_TUTORIAL_CREATION_FAILED + " ("
                        + ex.getLocalizedMessage() + ")",
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
        }
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void cancelConnectionSettings(final ActionEvent event) {
        connectionSelection.valueProperty().set(null);
        controlConnectionSettingsVisibility();
        connectionSettingsEdit.set(false);
    }

    // Handler for ComboBox[fx:id="connectionSelection"] onAction
    public void changeConnection(final ActionEvent event) {
        controlConnectionSettingsVisibility();
        final ConnectionSetting setting = connectionSelection.getValue();
        putConnectionSettingInDialog(setting);
    }

    // Handler for ComboBox[fx:id="connectionType"] onAction
    public void changeConnectionType(final ActionEvent event) {
        final JDBCType type = connectionType.getValue();
        if (type != null) {
            connectionUrl.setText(null);
            connectionDbName.setText(null);
            connectionSettingFileBased.set(type == JDBCType.HSQL_STANDALONE);
        }
    }

    // Handler for Button[fx:id="connectionDirectoryChoice"] onAction
    public void chooseDbDirectory(final ActionEvent event) {
        final DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle(DialogDictionary.LABEL_DB_DIRECTORY_CHOOSER
                .toString());
        final File dir = dirChooser.showDialog(menuBar.getScene().getWindow());
        if (dir != null) {
            final JDBCType type = connectionType.valueProperty().get();
            if (type != null) {
                final String dbSource = dir.getAbsolutePath();
                connectionUrl.setText(dbSource);
            }
        }
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void createConnection(final ActionEvent event) {
        final String name = createValidNewConnectionName(
                DialogDictionary.PATTERN_NEW_CONNECTION_NAME
                .toString());
        final ConnectionSetting setting = new ConnectionSetting(name,
                JDBCType.HSQL_IN_MEMORY, "", "johndoe", null, null);
        putConnectionSettingInDialog(setting);
        connectionSettings.visibleProperty().set(true);
        editConnection.disableProperty().set(true);
        removeConnection.disableProperty().set(true);
        connectionSettingsEdit.set(true);
        connectionSettingFileBased.set(false);
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void editConnection(final ActionEvent event) {
        final ConnectionSetting setting = connectionSelection.getValue();
        putConnectionSettingInDialog(setting);
        controlConnectionSettingsVisibility();
        connectionSettingsEdit.set(true);
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void removeConnection(final ActionEvent event) {
        final ConfirmDialog confirm = new ConfirmDialog(
                DialogDictionary.MESSAGEBOX_CONFIRM.toString(),
                DialogDictionary.MSG_REALLY_REMOVE_CONNECTION.toString(),
                DialogDictionary.LABEL_REMOVE_CONNECTION.toString(),
                DialogDictionary.COMMON_BUTTON_CANCEL.toString());
        final String confirmation = confirm.askUserFeedback();
        if (DialogDictionary.LABEL_REMOVE_CONNECTION.toString().equals(
                confirmation)) {
            try {
                final ConnectionSettings settings = new ConnectionSettings();
                settings.getConnectionSettingsAfterRemovalOf(
                        getConnectionSettingFromDialog());
            } catch (final BackingStoreException ex) {
                final ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_CONNECTION_SETTING_SAVE_FAILED
                        .toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();
            }
            prepareConnectionSettings();
        }
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void saveConnectionSettings(final ActionEvent event) {
        final ConnectionSetting setting = getConnectionSettingFromDialog();
        final ConnectionSettings settings = new ConnectionSettings();
        try {
            settings.getConnectionSettingsAfterAdditionOf(setting);
        } catch (final BackingStoreException ex) {
            final ErrorMessage msg = new ErrorMessage(
                    DialogDictionary.MESSAGEBOX_ERROR.toString(),
                    DialogDictionary.ERR_CONNECTION_SETTING_SAVE_FAILED
                    .toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        }
        prepareConnectionSettings();
    }

    // Handler for ComboBox[fx:id="serverConnectionSelection"] onAction
    public void changeServerConnection(final ActionEvent event) {
        final ConnectionSetting connectionSetting = serverConnectionSelection
                .getValue();
        serverAlias.setText(connectionSetting != null ? connectionSetting
                .getDbName() : null);
    }

    // Handler for Button[fx:id="startServer"] onAction
    public void startServer(final ActionEvent event) {
        try {
            final ConnectionSetting connectionSetting =
                    serverConnectionSelection
                    .getValue();
            ServerManager.getSharedInstance().startServer(connectionSetting,
                    serverAlias.getText());
        } catch (IOException | ServerAcl.AclFormatException | IllegalArgumentException ex) {
            final ErrorMessage msg = new ErrorMessage(
                    DialogDictionary.MESSAGEBOX_ERROR.toString(),
                    DialogDictionary.ERR_SERVER_START_FAILED.toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        }
    }

    // Handler for Button[fx:id="shutdownServer"] onAction
    public void shutdownServer(final ActionEvent event) {
        try {
            ServerManager.getSharedInstance().shutdownServer();
        } catch (final Throwable ex) {
            final ErrorMessage msg = new ErrorMessage(
                    DialogDictionary.MESSAGEBOX_ERROR.toString(),
                    DialogDictionary.ERR_SERVER_START_FAILED.toString(),
                    DialogDictionary.COMMON_BUTTON_OK.toString());
            msg.askUserFeedback();
        }
    }

    // ---
    private void initializeContinued() {
        connectionSettingsEdit = new SimpleBooleanProperty(false);
        connectionSettingFileBased = new SimpleBooleanProperty(false);
        connectionName.disableProperty().bind(
                Bindings.not(connectionSettingsEdit));
        connectionType.disableProperty().bind(
                Bindings.not(connectionSettingsEdit));
        connectionUrl.disableProperty().bind(
                Bindings.not(connectionSettingsEdit));
        connectionDbName.disableProperty().bind(
                Bindings.not(connectionSettingsEdit));
        connectionUser.disableProperty().bind(
                Bindings.not(connectionSettingsEdit));
        connectionDirectoryChoice.disableProperty().bind(
                Bindings.not(connectionSettingsEdit).or(
                Bindings.not(connectionSettingFileBased)));
        prepareConnectionSettings();
        fillCodeTemplates();
        prepareHistory();
        statementInput.setStyle("-fx-font-size: "
                + UserPreferencesManager.getSharedInstance().getFontSize()
                + ";");
        dbOutput.setStyle("-fx-font-size: "
                + UserPreferencesManager.getSharedInstance().getFontSize()
                + ";");

        limitMaxRows.setSelected(UserPreferencesManager.getSharedInstance()
                .isLimitMaxRows());

        setupToolbar();
        setupMenu();

        controlAutoCommitCheckBoxState();
        controlServerStatusButtonStates();
        permanentMessage.visibleProperty().set(false);
        refreshTree(null);
    }

    private void setupToolbar() {
        Tooltip.install(toolbarExecute, new Tooltip(
                DialogDictionary.TOOLTIP_EXECUTE.toString()));
        Tooltip.install(toolbarCommit, new Tooltip(
                DialogDictionary.TOOLTIP_COMMIT.toString()));
        Tooltip.install(toolbarCheckpoint, new Tooltip(
                DialogDictionary.TOOLTIP_CHECKPOINT.toString()));
        Tooltip.install(toolbarRollback, new Tooltip(
                DialogDictionary.TOOLTIP_ROLLBACK.toString()));
        Tooltip.install(toolbarZoomOut, new Tooltip(
                DialogDictionary.TOOLTIP_DECREASE_FONTSIZE.toString()));
        Tooltip.install(toolbarZoomIn, new Tooltip(
                DialogDictionary.TOOLTIP_INCREASE_FONTSIZE.toString()));
        Tooltip.install(toolbarTutorialData, new Tooltip(
                DialogDictionary.TOOLTIP_TUTORIAL_DATA.toString()));
        Tooltip.install(toolbarTabDbOutputExport, new Tooltip(
                DialogDictionary.TOOLTIP_EXPORT_OUTPUT.toString()));
        Tooltip.install(toolbarTabDbOutputClear, new Tooltip(
                DialogDictionary.TOOLTIP_CLEAR_OUTPUT.toString()));
    }

    private void setupMenu() {
        menuItemExecute.setAccelerator(new KeyCodeCombination(KeyCode.ENTER,
                KeyCombination.SHORTCUT_DOWN));
        menuItemExecuteScript.setAccelerator(new KeyCodeCombination(
                KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN,
                KeyCombination.SHIFT_DOWN));
        menuItemFileOpenScript.setAccelerator(new KeyCodeCombination(KeyCode.O,
                KeyCombination.SHORTCUT_DOWN));
        menuItemFileSaveScript.setAccelerator(new KeyCodeCombination(KeyCode.S,
                KeyCombination.SHORTCUT_DOWN));
        menuItemDisconnect.disableProperty().bind(
                Bindings.not(getConnectionHolder().connectedProperty()));
    }

    private void controlAutoCommitCheckBoxState() {
        if (getConnectionHolder().getConnection() != null) {
            try {
                autoCommit.setSelected(connectionHolder.getConnection()
                        .getAutoCommit());
            } catch (final SQLException ex) {
                autoCommit.setSelected(true);
            }
        }
    }

    private void controlServerStatusButtonStates() {
        startServer.disableProperty().set(false);
        shutdownServer.disableProperty().set(true);
        Task serverStatusTask;
        serverStatusTask = new Task() {
            @Override
            protected Object call() throws Exception {
                while (!isCancelled()) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            final boolean running = ServerManager
                                    .getSharedInstance().isRunning();
                            startServer.disableProperty().set(running);
                            shutdownServer.disableProperty().set(!running);
                        }
                    });
                    Thread.sleep(1000);
                }
                return null;
            }
        };
        final Thread th = new Thread(serverStatusTask);
        th.setDaemon(true);
        th.start();
    }

    private void fillCodeTemplates() {
        final ObservableList<CodeHelp> templateListViewItems = FXCollections
                .observableArrayList();
        for (final CodeHelp template : CodeHelp.values()) {
            templateListViewItems.add(template);
        }
        accordionCodeTemplateList.setItems(templateListViewItems);
        accordionCodeTemplateList.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<CodeHelp>() {
            @Override
            public void changed(
                    final ObservableValue<? extends CodeHelp> ov,
                    final CodeHelp oldValue, final CodeHelp newValue) {
                final StringBuilder statementTemplate = new StringBuilder();
                if (newValue.getSyntaxDescription() != null
                        && newValue.getSyntaxDescription().trim()
                        .length() > 0) {
                    statementTemplate
                            .append("-- ")
                            .append(
                            DialogDictionary.LABEL_SEE_SYNTAX_IN_DBOUTPUT
                            .toString()).append("\n");
                    dbOutput.appendText("\n"
                            + newValue.getSyntaxDescription() + "\n");
                }
                statementTemplate.append(newValue.getCodeTemplate());
                statementInput.insertText(
                        statementInput.getCaretPosition(),
                        statementTemplate.toString());
            }
        });
    }

    private void prepareHistory() {
        sqlHistoryItems = FXCollections.observableArrayList();
        sqlHistoryColumnTimestamp
                .setCellValueFactory(
                new PropertyValueFactory<SQLHistory, String>(
                "timestamp"));
        sqlHistoryColumnStatement
                .setCellValueFactory(
                new PropertyValueFactory<SQLHistory, String>(
                "sqlForDisplay"));
        sqlHistory.setItems(sqlHistoryItems);
        sqlHistory.getSelectionModel().selectedItemProperty()
                .addListener(new ChangeListener<SQLHistory>() {
            @Override
            public void changed(
                    final ObservableValue<? extends SQLHistory> ov,
                    final SQLHistory oldValue, final SQLHistory newValue) {
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
    }

    private void controlConnectionSettingsVisibility() {
        final boolean empty = connectionSelection.valueProperty().get() == null;
        connectionSettings.visibleProperty().set(!empty);
        removeConnection.disableProperty().set(empty);
        editConnection.disableProperty().set(empty);

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
        final ConnectionSetting setting = new ConnectionSetting(
                connectionName.getText(), connectionType.getValue(),
                connectionUrl.getText(), connectionDbName.getText(),
                connectionUser.getText(), null);
        return setting;
    }

    private void fillConnectionSettings() {
        final ConnectionSettings settings = new ConnectionSettings();
        final ObservableList<ConnectionSetting> localSettings = FXCollections
                .observableArrayList();
        final ObservableList<ConnectionSetting> serverSettings = FXCollections
                .observableArrayList();
        for (final ConnectionSetting connectionSetting : settings
                .getConnectionSettings()) {
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
                        + " (" + count++ + ")";
            } while (connectionNameAlreadyUsed(name));
        }
        return name;
    }

    private boolean connectionNameAlreadyUsed(final String name) {
        if (name == null) {
            return true;
        }
        final ConnectionSettings settings = new ConnectionSettings();
        for (final ConnectionSetting setting : settings.getConnectionSettings()) {
            if (setting.getName() != null && setting.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    // --- Interfaces und specialties
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
