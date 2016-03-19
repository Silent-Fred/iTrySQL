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
package de.kuehweg.sqltool.itrysql;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;
import java.util.prefs.BackingStoreException;

import javax.xml.bind.JAXBException;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.common.FileUtil;
import de.kuehweg.sqltool.common.ProvidedAudioClip;
import de.kuehweg.sqltool.common.RomanNumber;
import de.kuehweg.sqltool.common.UserPreferencesManager;
import de.kuehweg.sqltool.common.achievement.AchievementManager;
import de.kuehweg.sqltool.common.achievement.NamedAchievementEvent;
import de.kuehweg.sqltool.common.exception.DatabaseConnectionException;
import de.kuehweg.sqltool.common.sqlediting.StatementExtractor;
import de.kuehweg.sqltool.database.ConnectionHolder;
import de.kuehweg.sqltool.database.ConnectionSetting;
import de.kuehweg.sqltool.database.JDBCType;
import de.kuehweg.sqltool.database.ManagedConnectionSettings;
import de.kuehweg.sqltool.dialog.AlertBox;
import de.kuehweg.sqltool.dialog.ConfirmDialog;
import de.kuehweg.sqltool.dialog.ConnectionDialog;
import de.kuehweg.sqltool.dialog.ErrorMessage;
import de.kuehweg.sqltool.dialog.License;
import de.kuehweg.sqltool.dialog.action.ExecuteAction;
import de.kuehweg.sqltool.dialog.action.FindAction;
import de.kuehweg.sqltool.dialog.action.ScriptAction;
import de.kuehweg.sqltool.dialog.action.TableViewFindAction;
import de.kuehweg.sqltool.dialog.action.TextAreaFindAction;
import de.kuehweg.sqltool.dialog.action.TextTreeViewFindAction;
import de.kuehweg.sqltool.dialog.action.TutorialAction;
import de.kuehweg.sqltool.dialog.base.FontSizeZoomable;
import de.kuehweg.sqltool.dialog.base.TextAreaZoomable;
import de.kuehweg.sqltool.dialog.component.AudioFeedback;
import de.kuehweg.sqltool.dialog.component.ConnectionComponentController;
import de.kuehweg.sqltool.dialog.component.ErrorOnExecutionMessage;
import de.kuehweg.sqltool.dialog.component.ExecutionBasedAchievementTracker;
import de.kuehweg.sqltool.dialog.component.ExecutionProgressComponent;
import de.kuehweg.sqltool.dialog.component.QueryResultTableView;
import de.kuehweg.sqltool.dialog.component.QueryResultTextView;
import de.kuehweg.sqltool.dialog.component.SourceFileDropTargetUtil;
import de.kuehweg.sqltool.dialog.component.StatementEditorHolder;
import de.kuehweg.sqltool.dialog.component.achievement.AchievementHtmlFormatter;
import de.kuehweg.sqltool.dialog.component.achievement.AchievementView;
import de.kuehweg.sqltool.dialog.component.editor.AceBasedEditor;
import de.kuehweg.sqltool.dialog.component.editor.CodeMirrorBasedEditor;
import de.kuehweg.sqltool.dialog.component.editor.StatementEditor;
import de.kuehweg.sqltool.dialog.component.editor.TextAreaBasedEditor;
import de.kuehweg.sqltool.dialog.component.schematree.SchemaTreeBuilderTask;
import de.kuehweg.sqltool.dialog.component.schematree.SchemaTreeModificationDetector;
import de.kuehweg.sqltool.dialog.component.sqlhistory.SQLHistoryButtonCell;
import de.kuehweg.sqltool.dialog.component.sqlhistory.SQLHistoryComponent;
import de.kuehweg.sqltool.dialog.component.sqlhistory.SqlHistoryEntry;
import de.kuehweg.sqltool.dialog.util.WebViewBundledResourceErrorDetection;
import de.kuehweg.sqltool.dialog.util.WebViewWithHSQLDBBugfix;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.concurrent.Worker.State;
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
import javafx.scene.control.Slider;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.stage.WindowEvent;

/**
 * Hauptcontroller für den SQL Schulungsclient iTry SQL.
 *
 * @author Michael Kühweg
 */
public class iTrySQLController implements Initializable, EventHandler<WindowEvent>, StatementEditorHolder {

	private static int countWindows = 1;

	@FXML
	private TitledPane accordionPreferences;
	@FXML
	private TitledPane accordionSchemaTreeView;
	@FXML
	private CheckBox autoCommit;
	@FXML
	private ComboBox<ProvidedAudioClip> beepSelection;
	@FXML
	private Slider beepVolume;
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
	private MenuItem menuItemTutorial;
	@FXML
	private MenuItem menuItemNewSession;
	@FXML
	private Button refreshTree;
	@FXML
	private HBox resultTableContainer;
	@FXML
	private TableView<ObservableList<String>> resultTableView;
	@FXML
	private TreeView<String> schemaTreeView;
	@FXML
	private TableView<SqlHistoryEntry> sqlHistory;
	@FXML
	private TableColumn<SqlHistoryEntry, String> sqlHistoryColumnAction;
	@FXML
	private TableColumn<SqlHistoryEntry, String> sqlHistoryColumnStatement;
	@FXML
	private TableColumn<SqlHistoryEntry, String> sqlHistoryColumnTimestamp;
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
	private Tab tabSyntax;
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
	private Button toolbarTabDbOutputZoomIn;
	@FXML
	private Button toolbarTabDbOutputZoomOut;
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
	private Button removeConnection;
	@FXML
	private Button exportConnections;
	@FXML
	private Button importConnections;
	@FXML
	private Label permanentMessage;
	@FXML
	private WebView syntaxView;
	@FXML
	private WebView achievementsView;
	@FXML
	private TextField findInput;
	@FXML
	private Button toggleSyntaxColoring;

	// my own special creation
	private QueryResultTableView queryResultTableView;
	private AchievementView achievementViewComponent;

	private final ConnectionHolder connectionHolder = new ConnectionHolder();
	private ConnectionComponentController connectionComponentController;

	private final Collection<FindAction> findActionsForQuickSearch = new ArrayList<>();

	private StatementEditor statementEditor;

	private Window applicationWindow;

	@Override
	public final void initialize(final URL fxmlFileLocation, final ResourceBundle resources) {
		assert accordionPreferences != null : "fx:id=\"accordionPreferences\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert accordionSchemaTreeView != null : "fx:id=\"accordionSchemaTreeView\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert autoCommit != null : "fx:id=\"autoCommit\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert beepSelection != null : "fx:id=\"beepSelection\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert beepVolume != null : "fx:id=\"beepVolume\" was not injected: check your FXML file 'iTrySQL.fxml'.";
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
		assert menuItemTutorial != null : "fx:id=\"menuItemTutorial\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert menuItemNewSession != null : "fx:id=\"menuItemNewSession\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert permanentMessage != null : "fx:id=\"permanentMessage\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert refreshTree != null : "fx:id=\"refreshTree\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert removeConnection != null : "fx:id=\"removeConnection\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert resultTableContainer != null : "fx:id=\"resultTableContainer\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert resultTableView != null : "fx:id=\"resultTableView\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert schemaTreeView != null : "fx:id=\"schemaTreeView\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert sqlHistory != null : "fx:id=\"sqlHistory\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert sqlHistoryColumnAction != null : "fx:id=\"sqlHistoryColumnAction\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert sqlHistoryColumnStatement != null : "fx:id=\"sqlHistoryColumnStatement\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert sqlHistoryColumnTimestamp != null : "fx:id=\"sqlHistoryColumnTimestamp\" was not injected: check your FXML file 'iTrySQL.fxml'.";
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
		assert toolbarTabDbOutputZoomOut != null : "fx:id=\"toolbarTabDbOutputZoomOut\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert toolbarTabDbOutputZoomIn != null : "fx:id=\"toolbarTabDbOutputZoomIn\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert toolbarZoomOut != null : "fx:id=\"toolbarZoomOut\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert achievementsView != null : "fx:id=\"achievementsView\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert exportConnections != null : "fx:id=\"exportConnections\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert importConnections != null : "fx:id=\"importConnections\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert findInput != null : "fx:id=\"findInput\" was not injected: check your FXML file 'iTrySQL.fxml'.";
		assert toggleSyntaxColoring != null : "fx:id=\"toggleSyntaxColoring\" was not injected: check your FXML file 'iTrySQL.fxml'.";

		fixWebViewWithHSQLDBBug();

		initializeContinued();

		buildComponents();

		initializeQuickSearch();

	}

	/**
	 * @return the applicationWindow
	 */
	public Window getApplicationWindow() {
		return applicationWindow;
	}

	/**
	 * @param applicationWindow
	 *            the applicationWindow to set
	 */
	public void setApplicationWindow(final Window applicationWindow) {
		this.applicationWindow = applicationWindow;
	}

	/**
	 * Komponenten aufbauen, die mehr Daten enthalten, als in der
	 * Dialogkomponente enthalten ist.
	 */
	private void buildComponents() {
		// "Dumme" Komponenten werden nur jeweils zur Ausführung erzeugt.
		// Komponenten, die mehr Informationen enthalten, als sie anzeigen (z.B.
		// HTML-Export aus Tabellensicht)
		// werden dagegen einmal pro Fenster angelegt.
		queryResultTableView = new QueryResultTableView(resultTableView);
	}

	/**
	 * FIXME Falls noch Fälle offen sein sollten, in denen der Log-Level
	 * umgestellt wird und damit die WebView in den Debugmodus verfällt, sollte
	 * mit wenigen Handgriffen des Anwenders der Normalzustand wiederhergestellt
	 * werden können.
	 */
	private void fixWebViewWithHSQLDBBug() {
		syntaxView.setOnMouseEntered((final MouseEvent t) -> {
			WebViewWithHSQLDBBugfix.fix();
		});
		syntaxView.setOnMouseExited((final MouseEvent t) -> {
			WebViewWithHSQLDBBugfix.fix();
		});
		achievementsView.setOnMouseEntered((final MouseEvent t) -> {
			WebViewWithHSQLDBBugfix.fix();
		});
		achievementsView.setOnMouseExited((final MouseEvent t) -> {
			WebViewWithHSQLDBBugfix.fix();
		});
		// FIXME falls der Editor zur Eingabe der Anweisungen auch in einer
		// WebView läuft, dann muss diese auch hinzugefügt werden
	}

	/**
	 * Aufruf des Dialogs mit Informationen über die Anwendung.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void about(final ActionEvent event) {
		// FIXME
		WebViewWithHSQLDBBugfix.fix();
		AchievementManager.getInstance().fireEvent(NamedAchievementEvent.READ_ABOUT.asAchievementEvent(), 1);
		final License aboutBox = new License(getApplicationWindow());
		aboutBox.showAndWait();
	}

	/**
	 * Reagiert auf die Umstellung der Auto-Commit Einstellung durch den
	 * Anwender.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void autoCommit(final ActionEvent event) {
		// Auto-Commit ist keine echte Benutzereinstellung sondern wird pro
		// Verbindung gesteuert, z.T. auch durch JDBC-Vorgaben
		if (getConnection() == null) {
			final AlertBox msg = new AlertBox(DialogDictionary.MESSAGEBOX_WARNING.toString(),
					DialogDictionary.MSG_NO_DB_CONNECTION.toString(), DialogDictionary.COMMON_BUTTON_OK.toString());
			msg.askUserFeedback();
		} else {
			try {
				getConnection().setAutoCommit(autoCommit.isSelected());
			} catch (final SQLException ex) {
				final ErrorMessage msg = new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
						DialogDictionary.ERR_AUTO_COMMIT_FAILURE.toString(),
						DialogDictionary.COMMON_BUTTON_OK.toString());
				msg.askUserFeedback();
			}
		}
	}

	/**
	 * Öffnet ein neues Fenster in der Anwendung, um sich in einer neuen Sitzung
	 * anmelden zu können.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void newSession(final ActionEvent event) {
		final String title = DialogDictionary.APPLICATION.toString() + "("
				+ new RomanNumber(countWindows++).toString().toLowerCase() + ")";
		final ITrySQLStage iTrySQLStage = new ITrySQLStage((Stage) menuBar.getScene().getWindow(), title);
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

	/**
	 * Reagiert auf die Aktion des Anwenders, eine neue Verbindung zur Datenbank
	 * aufbauen zu wollen.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void connect(final ActionEvent event) {
		// final ConnectionDialog connectionDialog = new
		// ConnectionDialog(getApplicationWindow());
		final ConnectionDialog connectionDialog = new ConnectionDialog(statementPane.getScene().getWindow());

		connectionDialog.showAndWait();
		final ConnectionSetting connectionSetting = connectionDialog.getConnectionSetting();
		if (connectionSetting != null) {
			try {
				connectionHolder.connect(connectionSetting);
				controlAutoCommitVisuals();
				refreshTree(event);
				if (connectionSetting.getType() == JDBCType.HSQL_IN_MEMORY) {
					permanentMessage.setText(
							MessageFormat.format(DialogDictionary.PATTERN_MESSAGE_IN_MEMORY_DATABASE.toString(),
									connectionSetting.getName()));
					permanentMessage.visibleProperty().set(true);
				} else {
					permanentMessage.visibleProperty().set(false);
				}
				// FIXME
				WebViewWithHSQLDBBugfix.fix();
				AchievementManager.getInstance()
						.fireEvent(NamedAchievementEvent.CONNECTION_ESTABLISHED.asAchievementEvent(), 1);
			} catch (final DatabaseConnectionException ex) {
				final ErrorMessage msg = new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
						DialogDictionary.ERR_CONNECTION_FAILURE.toString(),
						DialogDictionary.COMMON_BUTTON_OK.toString());
				msg.askUserFeedback();
			}
		}
	}

	/**
	 * Reagiert auf die Aktion des Anwenders, die Verbindung zur Datenbank
	 * trennen zu wollen.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void disconnect(final ActionEvent event) {
		connectionHolder.disconnect();
		refreshTree(null, schemaTreeView);
		permanentMessage.visibleProperty().set(false);
	}

	/**
	 * Ausführung einer einzelnen Anweisung.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void execute(final ActionEvent event) {
		// Markierter Text?
		String sql = statementEditor.getSelectedText();
		// sonst die Anweisung, in der sich die Eingabemarkierung befindet
		if (sql.trim().length() == 0) {
			sql = new StatementExtractor().extractStatementAtCaretPosition(statementEditor.getText(),
					statementEditor.getCaretPositionAsIndex());
		}
		focusResult();
		createExecuteAction().handleExecuteAction(sql, getConnection());
	}

	/**
	 * Ausführung <em>aller</em> Anweisungen im Eingabebereich.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void executeScript(final ActionEvent event) {
		focusResult();
		createExecuteAction().handleExecuteAction(statementEditor.getText(), getConnection());
		AchievementManager.getInstance().fireEvent(NamedAchievementEvent.SCRIPT_EXECUTED.asAchievementEvent(), 1);
	}

	/**
	 * COMMIT Anweisung an die Datenbank, ausgelöst durch Benutzeraktion.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void commit(final ActionEvent event) {
		createExecuteAction().handleExecuteAction("COMMIT", getConnection());
	}

	/**
	 * ROLLBACK Anweisung an die Datenbank, ausgelöst durch Benutzeraktion.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void rollback(final ActionEvent event) {
		createExecuteAction().handleExecuteAction("ROLLBACK", getConnection());
	}

	/**
	 * Änderung an der Schriftgröße.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent - ermöglicht die Zuordnung, für
	 *            welchen Textbereich die Schriftgröße geändert werden soll
	 */
	@FXML
	public void fontAction(final ActionEvent event) {
		if (event != null && event.getSource() != null) {
			FontSizeZoomable zoom;
			switch (((Node) event.getSource()).getId()) {
			case "toolbarZoomIn":
				zoom = statementEditor;
				zoom.zoomIn();
				UserPreferencesManager.getSharedInstance().setFontSizeStatementInput(zoom.getFontSize());
				break;
			case "toolbarZoomOut":
				zoom = statementEditor;
				zoom.zoomOut();
				UserPreferencesManager.getSharedInstance().setFontSizeStatementInput(zoom.getFontSize());
				break;
			case "toolbarTabDbOutputZoomIn":
				zoom = new TextAreaZoomable(dbOutput);
				zoom.zoomIn();
				UserPreferencesManager.getSharedInstance().setFontSizeDbOutput(zoom.getFontSize());
				break;
			case "toolbarTabDbOutputZoomOut":
				zoom = new TextAreaZoomable(dbOutput);
				zoom.zoomOut();
				UserPreferencesManager.getSharedInstance().setFontSizeDbOutput(zoom.getFontSize());
				break;
			default:
				break;
			}
		}
		AchievementManager.getInstance().fireEvent(NamedAchievementEvent.FONT_SIZE_CHANGED.asAchievementEvent(), 1);
	}

	/**
	 * Aktion zum Aufbau der Schulungsinhalte in der Datenbank, zu der aktuell
	 * eine Verbindung aufgebaut ist.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void tutorialAction(final ActionEvent event) {
		new TutorialAction().createTutorial(createSilentExecuteAction(), getConnection());
	}

	/**
	 * Laden einer Datei in den Eingabebereich.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void fileOpenScriptAction(final ActionEvent event) {
		new ScriptAction(this).attachFileChooserToWindow(applicationWindow).loadScript();
	}

	/**
	 * Speichern des Inhalts im Eingabebereich in einer Textdatei.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void fileSaveScriptAction(final ActionEvent event) {
		new ScriptAction(this).attachFileChooserToWindow(applicationWindow).saveScript();
		AchievementManager.getInstance().fireEvent(NamedAchievementEvent.SCRIPT_SAVED.asAchievementEvent(), 1);
	}

	/**
	 * Reagiert auf das Umschalten zwischen eingeschränkter und nicht
	 * eingeschränkter Ergebnismenge.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void limitMaxRows(final ActionEvent event) {
		UserPreferencesManager.getSharedInstance().setLimitMaxRows(limitMaxRows.isSelected());
	}

	/**
	 * Rückfrage, ob die Applikation beendet werden soll - kann und soll in
	 * einem onCloseRequest-EventHandler aufgerufen werden.
	 *
	 * @return true wenn beendet werden soll
	 */
	public boolean quit() {
		final ConfirmDialog confirm = new ConfirmDialog(DialogDictionary.MESSAGEBOX_CONFIRM.toString(),
				DialogDictionary.MSG_REALLY_QUIT.toString(), DialogDictionary.LABEL_REALLY_QUIT.toString(),
				DialogDictionary.LABEL_NOT_REALLY_QUIT.toString());
		return DialogDictionary.LABEL_REALLY_QUIT.toString().equals(confirm.askUserFeedback());
	}

	/**
	 * Beenden der Anwendung.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	public void quit(final ActionEvent event) {
		if (quit()) {
			Platform.exit();
		}
	}

	/**
	 * Bisher mitprotokollierte Datenbankausgaben löschen.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void toolbarTabDbOutputClearAction(final ActionEvent event) {
		dbOutput.clear();
	}

	/**
	 * Export von Datenbankausgaben.
	 *
	 * @param fileChooserTitle
	 *            Titel für die Dateiauswahl
	 * @param attachChooser
	 *            Fenster, an das die Dateiauswahl angeheftet werden soll
	 * @param filenameExtension
	 *            Dateinamenserweiterung
	 * @param exportContent
	 *            Der zu exportierende vorab aufbereitete Inhalt
	 */
	private void export(final DialogDictionary fileChooserTitle, final Window attachChooser,
			final String filenameExtension, final String exportContent) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(fileChooserTitle.toString());
		final File file = fileChooser.showSaveDialog(attachChooser);
		if (file != null) {
			try {
				FileUtil.writeFile(FileUtil.enforceExtension(file.toURI().toURL(), filenameExtension), exportContent);
				AchievementManager.getInstance().fireEvent(NamedAchievementEvent.EXPORTED_RESULT.asAchievementEvent(),
						1);
			} catch (final IOException ex) {
				final ErrorMessage msg = new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
						DialogDictionary.ERR_FILE_SAVE_FAILED.toString(), DialogDictionary.COMMON_BUTTON_OK.toString());
				msg.askUserFeedback();
			}
		}
	}

	/**
	 * Export der Datenbankausgaben in Textform.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void toolbarTabDbOutputExportAction(final ActionEvent event) {
		export(DialogDictionary.LABEL_SAVE_OUTPUT, ((Node) event.getSource()).getScene().getWindow(), "txt",
				dbOutput.getText());
	}

	/**
	 * Ausgabe der Tabellenansicht (mit evtl. Abfrageergebnis) als HTML Datei.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void toolbarTabTableViewExportAction(final ActionEvent event) {
		export(DialogDictionary.LABEL_SAVE_OUTPUT_HTML, ((Node) event.getSource()).getScene().getWindow(), "html",
				queryResultTableView.toHtml());
	}

	/**
	 * Strukturansicht neu aufbauen - z.B. falls in einem anderen Fenster
	 * DDL-Anweisungen ausgeführt wurden.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void refreshTree(final ActionEvent event) {
		refreshTree(getConnection(), schemaTreeView);
	}

	/**
	 * Neuaufbau der Strukturansicht für eine Verbindung.
	 *
	 * @param connection
	 *            Datenbankverbindung, aus der die Struktur gelesen wird.
	 * @param treeToUpdate
	 *            TreeView, die mit den aufbereiteten Strukturdaten neu gefüllt
	 *            wird.
	 */
	private void refreshTree(final Connection connection, final TreeView<String> treeToUpdate) {
		final Task<Void> refreshTask = new SchemaTreeBuilderTask(connection, treeToUpdate);
		final Thread th = new Thread(refreshTask);
		th.setDaemon(true);
		th.start();
	}

	/**
	 * Änderungen an Verbindungseinstellungen verwerfen.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void cancelConnectionSettings(final ActionEvent event) {
		connectionComponentController.cancelEdit();
	}

	/**
	 * Andere Verbindungseinstellungen auswählen.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void changeConnection(final ActionEvent event) {
		connectionComponentController.changeConnection();
	}

	/**
	 * Verbindungstyp der momentan angezeigten Verbindungseinstellungen ändern.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void changeConnectionType(final ActionEvent event) {
		connectionComponentController.changeConnectionType();
	}

	/**
	 * Verzeichnis der momentan angezeigten Verbindungseinstellungen ändern (für
	 * Standalone Datenbanken).
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void chooseDbDirectory(final ActionEvent event) {
		connectionComponentController.chooseDbDirectory();
	}

	/**
	 * Neue Verbindungseinstellungen anlegen.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void createConnection(final ActionEvent event) {
		connectionComponentController.createConnection();
	}

	/**
	 * Momentan angezeigte Verbindungseinstellungen löschen.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void removeConnection(final ActionEvent event) {
		connectionComponentController.removeConnection();
	}

	/**
	 * Verbindungseinstellungen speichern.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void saveConnectionSettings(final ActionEvent event) {
		connectionComponentController.saveConnectionSettings();
	}

	/**
	 * Exportiert die Verbindungsdaten in eine vom Anwender wählbare Datei.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void exportConnections(final ActionEvent event) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(DialogDictionary.LABEL_EXPORT_CONNECTIONS.toString());
		final File file = fileChooser.showSaveDialog(((Node) event.getSource()).getScene().getWindow());
		if (file != null) {
			try {
				new ManagedConnectionSettings().exportToFile(file);
			} catch (final JAXBException ex) {
				final ErrorMessage msg = new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
						DialogDictionary.ERR_FILE_SAVE_FAILED.toString(), DialogDictionary.COMMON_BUTTON_OK.toString());
				msg.askUserFeedback();
			}
		}
	}

	/**
	 * Einlesen gespeicherter Verbindungsdaten aus einer vom Anwender wählbaren
	 * Datei.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void importConnections(final ActionEvent event) {
		final FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle(DialogDictionary.LABEL_IMPORT_CONNECTIONS.toString());
		final File file = fileChooser.showOpenDialog(((Node) event.getSource()).getScene().getWindow());
		if (file != null) {
			try {
				new ManagedConnectionSettings().importFromFile(file);
				connectionComponentController.saveConnectionSettings();
			} catch (JAXBException | BackingStoreException ex) {
				final ErrorMessage msg = new ErrorMessage(DialogDictionary.MESSAGEBOX_ERROR.toString(),
						DialogDictionary.ERR_FILE_OPEN_FAILED.toString(), DialogDictionary.COMMON_BUTTON_OK.toString());
				msg.askUserFeedback();
			}
		}
	}

	/**
	 * Nächsten Suchtreffer in den Komponenten suchen und markieren, die in der
	 * Schnellsuche angesprochen werden.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void quickSearchFindNext(final ActionEvent event) {
		final String searchString = findInput.textProperty().getValue();
		findActionsForQuickSearch.forEach(findAction -> findAction.nextOccurrence(searchString));
	}

	/**
	 * Vorhergehenden Suchtreffer in den Komponenten suchen und markieren, die
	 * in der Schnellsuche angesprochen werden.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void quickSearchFindPrevious(final ActionEvent event) {
		final String searchString = findInput.textProperty().getValue();
		findActionsForQuickSearch.forEach(findAction -> findAction.previousOccurrence(searchString));
	}

	/**
	 * Syntaxhervorhebung ein- und ausschalten.
	 *
	 * @param event
	 *            Ausgelöstes ActionEvent
	 */
	@FXML
	public void toggleSyntaxColoring(final ActionEvent event) {
		if (statementEditor instanceof TextAreaBasedEditor) {
			installEditorCodeMirror();
		} else if (statementEditor instanceof CodeMirrorBasedEditor) {
			installEditorAce();
		} else {
			installEditorTextArea();
		}
	}

	/**
	 * Fortführung der Initalisierung. TODO Kein sonderlich sinnvoller Name.
	 */
	private void initializeContinued() {
		initializeConnectionComponentController();
		prepareHistory();
		initialzeSyntaxHelp();
		initializeUserPreferences();

		setupTooltips();
		setupMenu();

		// Standardeinstellung beim Start ist die TextArea (sollte auf jeden
		// Fall funktionieren)
		installEditorTextArea();
		toggleSyntaxColoring.disableProperty()
				.set(WebViewBundledResourceErrorDetection.runningOnJavaVersionWithRenderingDeficiencies());

		controlAutoCommitVisuals();

		permanentMessage.visibleProperty().set(false);
		refreshTree(null, schemaTreeView);
		SourceFileDropTargetUtil.transformIntoSourceFileDropTarget(statementPane, this);

		achievementViewComponent = new AchievementView(achievementsView, new AchievementHtmlFormatter());
		achievementViewComponent.refresh();
	}

	/**
	 * @param resource
	 *            HTML mit Editor zur Einbettung in die WebView
	 * @return Die fertig aufgebaute und in die Anwendung eingebettete WebView,
	 *         in der der Javascript Editor läuft.
	 */
	private WebView installJavascriptEditor(final String resource) {
		final String content = statementEditor != null ? statementEditor.getText() : "";
		final WebView javascriptEditor = installWebViewForJavascriptEditor();
		installJavascriptEditorWithInitialContent(javascriptEditor, resource, content);
		webViewFixForJavascriptEditor(javascriptEditor);
		return javascriptEditor;
	}

	/**
	 * @return WebView zur Aufnahme des Editors. Die Fixierung am umgebenden
	 *         Anchor-Node wird vorbelegt.
	 */
	private WebView installWebViewForJavascriptEditor() {
		final WebView javascriptEditor = new WebView();
		statementPane.getChildren().clear();
		statementPane.getChildren().add(javascriptEditor);
		fixAnchor(javascriptEditor);
		return javascriptEditor;
	}

	/**
	 * @param javascriptEditor
	 *            WebView, in der der Javascript laufen soll.
	 * @param resource
	 *            HTML Ressource zur Einbettung des Editors.
	 * @param content
	 *            Initialer Inhalt des Editors.
	 */
	private void installJavascriptEditorWithInitialContent(final WebView javascriptEditor, final String resource,
			final String content) {
		final WebEngine engine = javascriptEditor.getEngine();
		engine.getLoadWorker().stateProperty().addListener(new ChangeListener<State>() {
			@Override
			public void changed(final ObservableValue<? extends State> observable, final State oldState,
					final State newState) {
				if (newState == State.SUCCEEDED) {
					engine.getLoadWorker().stateProperty().removeListener(this);
					statementEditor.setText(content);
					statementEditor.focus();
				}
			}
		});
		javascriptEditor.getEngine().load(this.getClass().getResource(resource).toExternalForm());
	}

	/**
	 * @param javascriptEditor
	 */
	private void webViewFixForJavascriptEditor(final WebView javascriptEditor) {
		// FIXME der übliche Workaround
		javascriptEditor.setOnMouseEntered((final MouseEvent t) -> {
			WebViewWithHSQLDBBugfix.fix();
		});
		javascriptEditor.setOnMouseExited((final MouseEvent t) -> {
			WebViewWithHSQLDBBugfix.fix();
		});
	}

	/**
	 * Installiert ACE als Editor für die Eingabe der SQL-Anweisungen.
	 */
	private void installEditorAce() {
		statementEditor = new AceBasedEditor(installJavascriptEditor(AceBasedEditor.RESOURCE));
	}

	/**
	 * Installiert CodeMirror als Editor für die Eingabe der SQL-Anweisungen.
	 */
	private void installEditorCodeMirror() {
		statementEditor = new CodeMirrorBasedEditor(installJavascriptEditor(CodeMirrorBasedEditor.RESOURCE));
	}

	/**
	 * Installiert eine einfache TextArea als Editor für die Eingabe der
	 * SQL-Anweisungen.
	 */
	private void installEditorTextArea() {
		final String content = statementEditor != null ? statementEditor.getText() : "";
		final TextArea textArea = new TextArea();
		textArea.setPromptText(DialogDictionary.PROMPT_ENTER_STATEMENT.toString());
		textArea.getStyleClass().add("itry-statement-edit");
		textArea.setStyle(
				"-fx-font-size: " + UserPreferencesManager.getSharedInstance().getFontSizeStatementInput() + ";");
		statementPane.getChildren().clear();
		statementPane.getChildren().add(textArea);
		fixAnchor(textArea);
		statementEditor = new TextAreaBasedEditor(textArea);
		statementEditor.setText(content);
		statementEditor.focus();
	}

	/**
	 * @param node
	 *            Fixiert alle Ecken des übergebenen Knotens an der
	 *            übergeordneten AnchorPane, d.h. der Node ändert seine Größe
	 *            jeweils mit der AnchorPane.
	 */
	private void fixAnchor(final Node node) {
		AnchorPane.setTopAnchor(node, 0.0);
		AnchorPane.setRightAnchor(node, 0.0);
		AnchorPane.setBottomAnchor(node, 0.0);
		AnchorPane.setLeftAnchor(node, 0.0);
	}

	/**
	 * Aufbau der Syntaxhilfe.
	 */
	private void initialzeSyntaxHelp() {
		final boolean webViewError = WebViewBundledResourceErrorDetection
				.runningOnJavaVersionWithRenderingDeficiencies();
		tabSyntax.disableProperty().set(webViewError);
		if (webViewError) {
			tabSyntax.setTooltip(new Tooltip(DialogDictionary.TOOLTIP_WEBVIEW_RENDERING_ERROR.toString()));
			// TODO auch eine Variante: Text einblenden mit Erläuterung zur
			// Fehlerkonstellation
			// syntaxView.getEngine()
			// .load(this.getClass().getResource("/resources/syntax/inconvenience.html").toExternalForm());
		} else {
			syntaxView.getEngine().load(this.getClass().getResource("/resources/syntax/index.html").toExternalForm());
		}
		// FIXME Stand Java 1.8.0_72 funktionieren zwar Ressourcen im Bundle
		// wieder, jedoch keine SVG Bilder. Wenn das behoben ist, kann home.png
		// wieder durch home.svg ersetzt werden.
	}

	/**
	 * Initialisiert die Benutzereinstellungen und wendet den Inhalt auf
	 * betroffene Komponenten an.
	 */
	private void initializeUserPreferences() {

		dbOutput.setStyle("-fx-font-size: " + UserPreferencesManager.getSharedInstance().getFontSizeDbOutput() + ";");

		limitMaxRows.setSelected(UserPreferencesManager.getSharedInstance().isLimitMaxRows());
		beepSelection.getItems().clear();
		beepSelection.getItems().addAll(ProvidedAudioClip.values());
		beepSelection.setValue(UserPreferencesManager.getSharedInstance().getBeepAudioClip());
		beepSelection.valueProperty().addListener((ChangeListener<ProvidedAudioClip>) (ov, oldValue, newValue) -> {
			if (newValue != null) {
				UserPreferencesManager.getSharedInstance().setBeepAudioClip(newValue);
				newValue.play(UserPreferencesManager.getSharedInstance().getBeepVolume());
			}
		});
		beepVolume.valueProperty().set(UserPreferencesManager.getSharedInstance().getBeepVolume());
		beepVolume.valueProperty().addListener((ChangeListener<Number>) (ov, oldValue, newValue) -> {
			if (newValue != null) {
				UserPreferencesManager.getSharedInstance().setBeepVolume(newValue.doubleValue());
			}
		});
	}

	/**
	 * Initialisiert die Komponente zur Verwaltung der Vebindungsdaten.
	 */
	private void initializeConnectionComponentController() {
		final ConnectionComponentController.Builder builder = new ConnectionComponentController.Builder(
				connectionSettings);
		builder.connectionName(connectionName);
		builder.connectionSelection(connectionSelection);
		builder.connectionType(connectionType);
		builder.connectionUrl(connectionUrl);
		builder.connectionUser(connectionUser);
		builder.dbName(connectionDbName);
		builder.browseButton(connectionDirectoryChoice);
		builder.removeButton(removeConnection);
		connectionComponentController = builder.build();
	}

	/**
	 * Aufbau der Tooltips.
	 */
	private void setupTooltips() {
		Tooltip.install(toolbarExecute, new Tooltip(DialogDictionary.TOOLTIP_EXECUTE.toString()));
		Tooltip.install(toolbarCommit, new Tooltip(DialogDictionary.TOOLTIP_COMMIT.toString()));
		Tooltip.install(toolbarRollback, new Tooltip(DialogDictionary.TOOLTIP_ROLLBACK.toString()));
		Tooltip.install(toolbarZoomOut, new Tooltip(DialogDictionary.TOOLTIP_DECREASE_FONTSIZE.toString()));
		Tooltip.install(toolbarZoomIn, new Tooltip(DialogDictionary.TOOLTIP_INCREASE_FONTSIZE.toString()));
		Tooltip.install(toolbarTutorialData, new Tooltip(DialogDictionary.TOOLTIP_TUTORIAL_DATA.toString()));

		Tooltip.install(toolbarTabTableViewExport, new Tooltip(DialogDictionary.TOOLTIP_EXPORT_RESULT.toString()));

		Tooltip.install(toolbarTabDbOutputExport, new Tooltip(DialogDictionary.TOOLTIP_EXPORT_OUTPUT.toString()));
		Tooltip.install(toolbarTabDbOutputClear, new Tooltip(DialogDictionary.TOOLTIP_CLEAR_OUTPUT.toString()));
		Tooltip.install(toolbarTabDbOutputZoomOut, new Tooltip(DialogDictionary.TOOLTIP_DECREASE_FONTSIZE.toString()));
		Tooltip.install(toolbarTabDbOutputZoomIn, new Tooltip(DialogDictionary.TOOLTIP_INCREASE_FONTSIZE.toString()));

		Tooltip.install(toggleSyntaxColoring, new Tooltip(DialogDictionary.TOOLTIP_SYNTAX_HIGHLIGHTING.toString()));

		Tooltip.install(permanentMessage, new Tooltip(DialogDictionary.TOOLTIP_IN_MEMORY_DATABASE.toString()));
	}

	private void setupMenu() {
		setupMenuGraphics();
		setupAccelerators();
	}

	private void setupAccelerators() {
		menuItemExecute.setAccelerator(new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN));
		menuItemExecuteScript.setAccelerator(
				new KeyCodeCombination(KeyCode.ENTER, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
		menuItemTutorial.setAccelerator(
				new KeyCodeCombination(KeyCode.T, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
		menuItemFileOpenScript.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.SHORTCUT_DOWN));
		menuItemFileSaveScript.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.SHORTCUT_DOWN));
		menuItemNewSession.setAccelerator(
				new KeyCodeCombination(KeyCode.N, KeyCombination.SHORTCUT_DOWN, KeyCombination.SHIFT_DOWN));
		menuItemConnect.setAccelerator(new KeyCodeCombination(KeyCode.L, KeyCombination.SHORTCUT_DOWN));
		menuItemDisconnect.disableProperty().bind(Bindings.not(connectionHolder.connectedProperty()));
	}

	private void setupMenuGraphics() {
		menuItemCommit.setGraphic(new Label("C"));
		menuItemExecute.setGraphic(new Label("E"));
		menuItemRollback.setGraphic(new Label("R"));
		menuItemTutorial.setGraphic(new Label("T"));
	}

	/**
	 * Steuert die Anzeige der Auto-Commit-Einstellung und der davon abhängigen
	 * Dialogelemente.
	 */
	private void controlAutoCommitVisuals() {
		if (getConnection() != null) {
			try {
				autoCommit.setSelected(getConnection().getAutoCommit());
			} catch (final SQLException ex) {
				autoCommit.setSelected(true);
			}
		}
		// Transaktionssteuerelemente in der Toolbar disabled, wenn Auto Commit
		// ausgewählt ist
		toolbarCommit.disableProperty().bind(autoCommit.selectedProperty());
		toolbarRollback.disableProperty().bind(autoCommit.selectedProperty());
		menuItemCommit.disableProperty().bind(autoCommit.selectedProperty());
		menuItemRollback.disableProperty().bind(autoCommit.selectedProperty());
	}

	/**
	 * Bereitet die Verlaufskomponente vor.
	 */
	private void prepareHistory() {
		sqlHistoryColumnTimestamp.setCellValueFactory(new PropertyValueFactory<>("timestampFormatted"));
		sqlHistoryColumnStatement.setCellValueFactory(new PropertyValueFactory<>("sqlForDisplay"));

		// FIXME das muss wahrscheinlich noch geändert werden, damit die
		// History-Einträge immer an den aktuellen Editor angehängt werden
		sqlHistoryColumnAction.setCellFactory(
				(final TableColumn<SqlHistoryEntry, String> p) -> new SQLHistoryButtonCell(statementEditor));
	}

	/**
	 * Initialisiert die FindActions als ChangeListener am Suchfeld für alle
	 * Komponenenten, die in der Schnellsuche angesprochen werden. Die
	 * FindActions werden für die gemeinsame Behandlung im Eventhandling in der
	 * dafür vorgesehenen Collection eingetragen.
	 */
	private void initializeQuickSearch() {
		// TODO FindAction für den StatementEditor

		final FindAction findActionOnDbOutput = new TextAreaFindAction(dbOutput);
		findInput.textProperty().addListener(findActionOnDbOutput);
		findActionsForQuickSearch.add(findActionOnDbOutput);

		final FindAction findActionOnSchemaTree = new TextTreeViewFindAction(schemaTreeView);
		findInput.textProperty().addListener(findActionOnSchemaTree);
		findActionsForQuickSearch.add(findActionOnSchemaTree);

		final FindAction findActionOnResultTableView = new TableViewFindAction(resultTableView);
		findInput.textProperty().addListener(findActionOnResultTableView);
		findActionsForQuickSearch.add(findActionOnResultTableView);
	}

	/**
	 * Bereitet die Ausführung von SQL Anweisungen vor.
	 *
	 * @return Die vorbereitete ExecuteAction
	 */
	private ExecuteAction createExecuteAction() {
		final ExecuteAction executeAction = new ExecuteAction();

		executeAction.attach(queryResultTableView, new SQLHistoryComponent(sqlHistory),
				new QueryResultTextView(dbOutput),
				new ExecutionProgressComponent(executionProgressIndicator, executionTime), new AudioFeedback(),
				new SchemaTreeModificationDetector(schemaTreeView, getConnection()),
				new ExecutionBasedAchievementTracker());

		executeAction.setLimitMaxRows(UserPreferencesManager.getSharedInstance().isLimitMaxRows());

		return executeAction;
	}

	/**
	 * Bereitet die Ausführung von SQL Anweisungen vor für den Fall, dass die
	 * Ausführung:
	 * <ul>
	 * <li>keine Ergebnisse anzeigen soll,</li>
	 * <li>keine Einträge im Verlauf anlegen soll und</li>
	 * <li>nicht als Fortschritt bei den Achievements gezählt werden soll.</li>
	 * </ul>
	 * Wird zur Anlage der Tutorialdaten verwendet.
	 *
	 * @return Die vorbereitete ExecuteAction
	 */
	private ExecuteAction createSilentExecuteAction() {
		final ExecuteAction executeAction = new ExecuteAction();

		executeAction.attach(new ExecutionProgressComponent(executionProgressIndicator, executionTime),
				new AudioFeedback(), new SchemaTreeModificationDetector(schemaTreeView, getConnection()),
				new ErrorOnExecutionMessage());
		// hier ohne Achievements für die Ausführung - wird nur aufgerufen um
		// die Tutorialdaten aufzubauen, und dafür gibt es ein spezielles
		// Achievement

		return executeAction;
	}

	/**
	 * Zugriff auf die zum Fenster gehörende Datenbankverbindung.
	 *
	 * @return Verbindung
	 */
	private Connection getConnection() {
		return connectionHolder.getConnection();
	}

	/**
	 * Nach dem Ausführen einer SQL Anweisung wird im Dialog das Ergebnis in den
	 * Vordergrund geholt (sofern es das nicht bereits ist).
	 */
	public void focusResult() {
		final Tab currentlySelectedTab = tabPaneProtocols.selectionModelProperty().getValue().getSelectedItem();
		if (currentlySelectedTab == null
				|| !currentlySelectedTab.equals(tabResult) && !currentlySelectedTab.equals(tabDbOutput)) {
			tabPaneProtocols.selectionModelProperty().getValue().select(tabResult);
		}
	}

	// --- implementierte Interfaces und weitere Spezialitäten
	@Override
	public void handle(final WindowEvent event) {
		if (WindowEvent.WINDOW_HIDING.equals(event.getEventType())) {
			// Wenn das Fenster geschlossen wird, dann auch die Verbindung zur
			// Datenbank schließen.
			connectionHolder.disconnect();
		}
	}

	@Override
	public StatementEditor getActiveStatementEditor() {
		return statementEditor;
	}

	/**
	 * Abschließende Aktionen, bevor die Anwendung beendet wird.
	 */
	public void stop() {
		connectionHolder.disconnect();
		AchievementManager.getInstance().flush();
	}

}
