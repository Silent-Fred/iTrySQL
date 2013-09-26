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
import de.kuehweg.sqltool.common.sqlediting.SQLHistory;
import de.kuehweg.sqltool.common.sqlediting.SQLHistoryKeeper;
import de.kuehweg.sqltool.common.sqlediting.StatementExtractor;
import de.kuehweg.sqltool.database.ConnectionHolder;
import de.kuehweg.sqltool.database.JDBCType;
import de.kuehweg.sqltool.dialog.AlertBox;
import de.kuehweg.sqltool.dialog.ConnectionDialog;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import de.kuehweg.sqltool.dialog.ExecutionInputEnvironment;
import de.kuehweg.sqltool.dialog.ExecutionProgressEnvironment;
import de.kuehweg.sqltool.dialog.ExecutionResultEnvironment;
import de.kuehweg.sqltool.dialog.License;
import de.kuehweg.sqltool.dialog.action.ExecuteAction;
import de.kuehweg.sqltool.dialog.action.FontAction;
import de.kuehweg.sqltool.dialog.action.SchemaTreeBuilderTask;
import de.kuehweg.sqltool.dialog.action.ScriptAction;
import de.kuehweg.sqltool.dialog.action.TutorialAction;
import de.kuehweg.sqltool.dialog.component.CodeTemplateComponent;
import de.kuehweg.sqltool.dialog.component.ConnectionComponentController;
import de.kuehweg.sqltool.dialog.component.ServerComponentController;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
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
import javafx.stage.FileChooser;

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
    private CodeTemplateComponent codeTemplateComponent;
    private ConnectionComponentController connectionComponentController;
    private ServerComponentController serverComponentController;

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

    // Handler for MenuItem[fx:id="menuItemCopy"] onAction
    public void clipboardCopy(final ActionEvent event) {
        // currently no action
    }

    // Handler for MenuItem[fx:id="menuItemCut"] onAction
    public void clipboardCut(final ActionEvent event) {
        // currently no action
    }

    // Handler for MenuItem[fx:id="menuItemPaste"] onAction
    public void clipboardPaste(final ActionEvent event) {
        // currently no action
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
        createExecuteAction().handleExecuteAction(sql);
    }

    // Handler for MenuItem[fx:id="menuItemExecuteScript"] onAction
    public void executeScript(final ActionEvent event) {
        createExecuteAction().handleExecuteAction(statementInput.getText());
    }

    // Handler for Button[fx:id="toolbarCommit"] onAction
    // Handler for MenuItem[fx:id="menuItemCommit"] onAction
    public void commit(final ActionEvent event) {
        createExecuteAction().handleExecuteAction("COMMIT");
    }

    // Handler for Button[fx:id="toolbarCheckpoint"] onAction
    // Handler for MenuItem[fx:id="menuItemCheckpoint"] onAction
    public void checkpoint(final ActionEvent event) {
        createExecuteAction().handleExecuteAction("CHECKPOINT");
    }

    // Handler for Button[fx:id="toolbarRollback"] onAction
    // Handler for MenuItem[fx:id="menuItemRollback"] onAction
    public void rollback(final ActionEvent event) {
        createExecuteAction().handleExecuteAction("ROLLBACK");
    }

    // Handler for Button[fx:id="toolbarZoomIn"] onAction
    // Handler for Button[fx:id="toolbarZoomOut"] onAction
    public void fontAction(final ActionEvent event) {
        FontAction.handleFontAction(event);
    }

    // Handler for Button[fx:id="toolbarTutorialData"] onAction
    public void tutorialAction(final ActionEvent event) {
        new TutorialAction().createTutorial(createExecuteAction());
    }

    // Handler for MenuItem[fx:id="menuItemFileOpenScript"] onAction
    public void fileOpenScriptAction(final ActionEvent event) {
        new ScriptAction(statementInput).loadScript();
    }

    // Handler for MenuItem[fx:id="menuItemFileSaveScript"] onAction
    public void fileSaveScriptAction(final ActionEvent event) {
        new ScriptAction(statementInput).saveScript();
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

    // Handler for Button[fx:id="refreshTree"] onAction
    public void refreshTree(final ActionEvent event) {
        final Task refreshTask = new SchemaTreeBuilderTask(
                getConnectionHolder().getConnection(), schemaTreeView);
        final Thread th = new Thread(refreshTask);
        th.setDaemon(true);
        th.start();
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void cancelConnectionSettings(final ActionEvent event) {
        connectionComponentController.cancelEdit();
    }

    // Handler for ComboBox[fx:id="connectionSelection"] onAction
    public void changeConnection(final ActionEvent event) {
        connectionComponentController.changeConnection();
    }

    // Handler for ComboBox[fx:id="connectionType"] onAction
    public void changeConnectionType(final ActionEvent event) {
        connectionComponentController.changeConnectionType();
    }

    // Handler for Button[fx:id="connectionDirectoryChoice"] onAction
    public void chooseDbDirectory(final ActionEvent event) {
        connectionComponentController.chooseDbDirectory();
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void createConnection(final ActionEvent event) {
        connectionComponentController.createConnection();
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void editConnection(final ActionEvent event) {
        connectionComponentController.editConnection();
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void removeConnection(final ActionEvent event) {
        connectionComponentController.removeConnection();
        serverComponentController.refreshServerConnectionSettings();
    }

    // Handler for Button[Button[id=null, styleClass=button]] onAction
    public void saveConnectionSettings(final ActionEvent event) {
        connectionComponentController.saveConnectionSettings();
        serverComponentController.refreshServerConnectionSettings();
    }

    // Handler for ComboBox[fx:id="serverConnectionSelection"] onAction
    public void changeServerConnection(final ActionEvent event) {
        serverComponentController.changeServerConnection();
    }

    // Handler for Button[fx:id="startServer"] onAction
    public void startServer(final ActionEvent event) {
        serverComponentController.startServer();
    }

    // Handler for Button[fx:id="shutdownServer"] onAction
    public void shutdownServer(final ActionEvent event) {
        serverComponentController.shutdownServer();
    }

    // ---
    private void initializeContinued() {
        initializeConnectionComponentController();
        initializeServerComponentController();
        codeTemplateComponent = new CodeTemplateComponent(
                accordionCodeTemplateList, statementInput, dbOutput);
        codeTemplateComponent.fillCodeTemplates();
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
        permanentMessage.visibleProperty().set(false);
        refreshTree(null);
    }

    private void initializeConnectionComponentController() {
        ConnectionComponentController.Builder builder =
                new ConnectionComponentController.Builder(
                connectionSettings);
        builder.connectionName(connectionName);
        builder.connectionSelection(connectionSelection);
        builder.connectionType(connectionType);
        builder.connectionUrl(connectionUrl);
        builder.connectionUser(connectionUser);
        builder.dbName(connectionDbName);
        builder.browseButton(connectionDirectoryChoice);
        builder.editButton(editConnection);
        builder.removeButton(removeConnection);
        connectionComponentController = builder.build();
    }

    private void initializeServerComponentController() {
        serverComponentController = new ServerComponentController.Builder().
                serverConnectionSelection(serverConnectionSelection).
                startButton(startServer).shutdownButton(shutdownServer).
                serverAlias(serverAlias).build();
        serverComponentController.refreshServerConnectionSettings();
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

    private ExecuteAction createExecuteAction() {
        final ExecutionInputEnvironment input =
                new ExecutionInputEnvironment.Builder(getConnectionHolder()).
                limitMaxRows(
                UserPreferencesManager.getSharedInstance().isLimitMaxRows()).
                historyKeeper(this).build();
        final ExecutionProgressEnvironment progress =
                new ExecutionProgressEnvironment.Builder(
                executionProgressIndicator).dbOutput(dbOutput).executionTime(
                executionTime).build();
        final ExecutionResultEnvironment result =
                new ExecutionResultEnvironment.Builder().dbOutput(dbOutput).
                resultTableContainer(resultTableContainer).build();
        return new ExecuteAction(input, progress, result);
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
