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
import de.kuehweg.sqltool.common.RomanNumber;
import de.kuehweg.sqltool.common.UserPreferencesManager;
import de.kuehweg.sqltool.common.sqlediting.ConnectionSetting;
import de.kuehweg.sqltool.common.sqlediting.SQLHistory;
import de.kuehweg.sqltool.common.sqlediting.SQLHistoryKeeper;
import de.kuehweg.sqltool.common.sqlediting.StatementExtractor;
import de.kuehweg.sqltool.database.ConnectionHolder;
import de.kuehweg.sqltool.database.JDBCType;
import de.kuehweg.sqltool.dialog.AlertBox;
import de.kuehweg.sqltool.dialog.ConnectionDialog;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import de.kuehweg.sqltool.dialog.License;
import de.kuehweg.sqltool.dialog.action.ExecuteAction;
import de.kuehweg.sqltool.dialog.action.ExecutionGUIUpdater;
import de.kuehweg.sqltool.dialog.action.FontAction;
import de.kuehweg.sqltool.dialog.action.SchemaTreeBuilderTask;
import de.kuehweg.sqltool.dialog.action.ScriptAction;
import de.kuehweg.sqltool.dialog.action.TutorialAction;
import de.kuehweg.sqltool.dialog.component.ConnectionComponentController;
import de.kuehweg.sqltool.dialog.component.QueryResultTableView;
import de.kuehweg.sqltool.dialog.component.ServerComponentController;
import de.kuehweg.sqltool.dialog.component.SourceFileDropTargetUtil;
import de.kuehweg.sqltool.dialog.environment.ExecutionInputEnvironment;
import de.kuehweg.sqltool.dialog.environment.ExecutionProgressEnvironment;
import de.kuehweg.sqltool.dialog.environment.ExecutionResultEnvironment;
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
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

public class iTrySQLController implements Initializable, SQLHistoryKeeper,
        EventHandler<WindowEvent> {

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
    private MenuItem menuItemNewSession;
    @FXML
    private Button refreshTree;
    @FXML
    private HBox resultTableContainer;
    @FXML
    private TreeView<String> schemaTreeView;
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
    private AnchorPane statementPane;
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
    private Button toolbarTabTableViewExport;
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
    @FXML
    private WebView syntaxDefinitionView;
    // my own special creation
    private ConnectionHolder connectionHolder;
    private ObservableList<SQLHistory> sqlHistoryItems;
    private ConnectionComponentController connectionComponentController;
    private ServerComponentController serverComponentController;
    private static int countWindows = 1;

    @Override
    public void initialize(final URL fxmlFileLocation,
            final ResourceBundle resources) {
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
        assert menuItemNewSession != null : "fx:id=\"menuItemNewSession\" was not injected: check your FXML file 'iTrySQL.fxml'.";
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
        assert statementPane != null : "fx:id=\"statementPane\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert tabDbOutput != null : "fx:id=\"tabDbOutput\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert tabHistory != null : "fx:id=\"tabHistory\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert tabPaneProtocols != null : "fx:id=\"tabPaneProtocols\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert tabResult != null : "fx:id=\"tabResult\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolBar != null : "fx:id=\"toolBar\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarCommit != null : "fx:id=\"toolbarCommit\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarExecute != null : "fx:id=\"toolbarExecute\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarRollback != null : "fx:id=\"toolbarRollback\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarTabDbOutputClear != null : "fx:id=\"toolbarTabDbOutputClear\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarTabDbOutputExport != null : "fx:id=\"toolbarTabDbOutputExport\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarTabTableViewExport != null : "fx:id=\"toolbarTabTableViewExport\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarTutorialData != null : "fx:id=\"toolbarTutorialData\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarZoomIn != null : "fx:id=\"toolbarZoomIn\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert toolbarZoomOut != null : "fx:id=\"toolbarZoomOut\" was not injected: check your FXML file 'iTrySQL.fxml'.";
        assert syntaxDefinitionView != null : "fx:id=\"syntaxDefinitionView\" was not injected: check your FXML file 'iTrySQL.fxml'.";

        initializeContinued();
    }

    private void webViewFixWithHSQLDB() {
        // HSQLDB setzt Log-Level auf ein Niveau, das in JavaFX WebViews
        // katastrophale Auswirkungen hat - deshalb nach Aufbau einer
        // Datenbankverbindung den Log-Level für WebViews auf OFF
        java.util.logging.Logger.getLogger(
                com.sun.webpane.sg.prism.WCGraphicsPrismContext.class.
                getName()).setLevel(java.util.logging.Level.OFF);
    }

    public void about(final ActionEvent event) {
        webViewFixWithHSQLDB();
        final License aboutBox = new License();
        aboutBox.showAndWait();
    }

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

    public void newSession(final ActionEvent event) {
        final String title = DialogDictionary.APPLICATION.toString() + "("
                + new RomanNumber(countWindows++).toString().toLowerCase()
                + ")";
        final ITrySQLStage iTrySQLStage = new ITrySQLStage((Stage) menuBar
                .getScene().getWindow(), title);
        iTrySQLStage.show();
    }

    public void clipboardCopy(final ActionEvent event) {
        // currently no action
    }

    public void clipboardCut(final ActionEvent event) {
        // currently no action
    }

    public void clipboardPaste(final ActionEvent event) {
        // currently no action
    }

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
            webViewFixWithHSQLDB();
        }
    }

    public void disconnect(final ActionEvent event) {
        getConnectionHolder().disconnect();
        refreshTree(null);
        permanentMessage.visibleProperty().set(false);
    }

    public void execute(final ActionEvent event) {
        // Markierter Text?
        String sql = statementInput.getSelectedText();
        // sonst die Anweisung, in der sich die Eingabemarkierung befindet
        if (sql.trim().length() == 0) {
            sql = StatementExtractor
                    .extractStatementAtCaretPosition(statementInput.getText(),
                    statementInput.getCaretPosition());
        }
        createExecuteAction().handleExecuteAction(sql);
    }

    public void executeScript(final ActionEvent event) {
        createExecuteAction().handleExecuteAction(statementInput.getText());
    }

    public void commit(final ActionEvent event) {
        createExecuteAction().handleExecuteAction("COMMIT");
    }

    public void rollback(final ActionEvent event) {
        createExecuteAction().handleExecuteAction("ROLLBACK");
    }

    public void fontAction(final ActionEvent event) {
        FontAction.handleFontAction(event);
    }

    public void tutorialAction(final ActionEvent event) {
        new TutorialAction().createTutorial(createExecuteAction());
    }

    public void fileOpenScriptAction(final ActionEvent event) {
        new ScriptAction(statementInput).loadScript();
    }

    public void fileSaveScriptAction(final ActionEvent event) {
        new ScriptAction(statementInput).saveScript();
    }

    public void limitMaxRows(final ActionEvent event) {
        UserPreferencesManager.getSharedInstance().setLimitMaxRows(
                limitMaxRows.isSelected());
    }

    public void quit(final ActionEvent event) {
        Platform.exit();
    }

    public void toolbarTabDbOutputClearAction(final ActionEvent event) {
        dbOutput.clear();
    }

    private void export(final DialogDictionary fileChooserTitle,
            final Window attachChooser, final String filenameExtension,
            final String exportContent) {
        final FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(fileChooserTitle.toString());
        final File file = fileChooser.showSaveDialog(attachChooser);
        if (file != null) {
            try {
                FileUtil.writeFile(FileUtil.enforceExtension(
                        file.toURI().toURL(), filenameExtension),
                        exportContent);
            } catch (final IOException ex) {
                final ErrorMessage msg = new ErrorMessage(
                        DialogDictionary.MESSAGEBOX_ERROR.toString(),
                        DialogDictionary.ERR_FILE_SAVE_FAILED.toString(),
                        DialogDictionary.COMMON_BUTTON_OK.toString());
                msg.askUserFeedback();

            }
        }
    }

    public void toolbarTabDbOutputExportAction(final ActionEvent event) {
        export(DialogDictionary.LABEL_SAVE_OUTPUT, ((Node) event.getSource())
                .getScene().getWindow(), "txt", dbOutput.getText());
    }

    public void toolbarTabTableViewExportAction(final ActionEvent event) {
        if (resultTableContainer.getChildren() != null) {
            for (final Node node : resultTableContainer.getChildren()) {
                if (ExecutionGUIUpdater.RESULT_TABLE_ID.equals(node.getId())) {
                    export(DialogDictionary.LABEL_SAVE_OUTPUT_HTML,
                            ((Node) event.getSource()).getScene().getWindow(),
                            "html", ((QueryResultTableView) node).toHtml());
                }
            }
        }
    }

    public void refreshTree(final ActionEvent event) {
        final Task<Void> refreshTask = new SchemaTreeBuilderTask(
                getConnectionHolder().getConnection(), schemaTreeView);
        final Thread th = new Thread(refreshTask);
        th.setDaemon(true);
        th.start();
    }

    public void cancelConnectionSettings(final ActionEvent event) {
        connectionComponentController.cancelEdit();
    }

    public void changeConnection(final ActionEvent event) {
        connectionComponentController.changeConnection();
    }

    public void changeConnectionType(final ActionEvent event) {
        connectionComponentController.changeConnectionType();
    }

    public void chooseDbDirectory(final ActionEvent event) {
        connectionComponentController.chooseDbDirectory();
    }

    public void createConnection(final ActionEvent event) {
        connectionComponentController.createConnection();
    }

    public void editConnection(final ActionEvent event) {
        connectionComponentController.editConnection();
    }

    public void removeConnection(final ActionEvent event) {
        connectionComponentController.removeConnection();
        serverComponentController.refreshServerConnectionSettings();
    }

    public void saveConnectionSettings(final ActionEvent event) {
        connectionComponentController.saveConnectionSettings();
        serverComponentController.refreshServerConnectionSettings();
    }

    public void changeServerConnection(final ActionEvent event) {
        serverComponentController.changeServerConnection();
    }

    public void startServer(final ActionEvent event) {
        serverComponentController.startServer();
    }

    public void shutdownServer(final ActionEvent event) {
        serverComponentController.shutdownServer();
    }

    // ---
    private void initializeContinued() {
        initializeConnectionComponentController();
        initializeServerComponentController();
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
        SourceFileDropTargetUtil.transformIntoSourceFileDropTarget(
                statementPane, statementInput);
        syntaxDefinitionView.getEngine().load(
                this.getClass().getResource("/resources/syntax/index.html")
                .toExternalForm());
    }

    private void initializeConnectionComponentController() {
        final ConnectionComponentController.Builder builder =
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
        serverComponentController = new ServerComponentController.Builder()
                .serverConnectionSelection(serverConnectionSelection)
                .startButton(startServer).shutdownButton(shutdownServer)
                .serverAlias(serverAlias).build();
        serverComponentController.refreshServerConnectionSettings();
    }

    private void setupToolbar() {
        Tooltip.install(toolbarExecute, new Tooltip(
                DialogDictionary.TOOLTIP_EXECUTE.toString()));
        Tooltip.install(toolbarCommit, new Tooltip(
                DialogDictionary.TOOLTIP_COMMIT.toString()));
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
        menuItemNewSession.setAccelerator(new KeyCodeCombination(KeyCode.N,
                KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));

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
            }
        });
    }

    private ExecuteAction createExecuteAction() {
        final ExecutionInputEnvironment input =
                new ExecutionInputEnvironment.Builder(
                getConnectionHolder()).limitMaxRows(
                UserPreferencesManager.getSharedInstance().isLimitMaxRows())
                .build();
        final ExecutionProgressEnvironment progress =
                new ExecutionProgressEnvironment.Builder(
                executionProgressIndicator).executionTime(executionTime)
                .build();
        final ExecutionResultEnvironment result =
                new ExecutionResultEnvironment.Builder()
                .dbOutput(dbOutput).historyKeeper(this)
                .resultTableContainer(resultTableContainer).build();
        return new ExecuteAction(input, progress, result);
    }

    // --- implementierte Interfaces und weitere Spezialitäten
    @Override
    public void handle(final WindowEvent event) {
        getConnectionHolder().disconnect();
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
