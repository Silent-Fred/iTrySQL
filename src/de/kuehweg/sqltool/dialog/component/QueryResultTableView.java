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
package de.kuehweg.sqltool.dialog.component;

import de.kuehweg.sqltool.common.DialogDictionary;
import de.kuehweg.sqltool.database.formatter.HtmlResultFormatter;
import de.kuehweg.sqltool.database.formatter.ResultFormatter;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.List;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * Baustein für Ergebnistabelle.
 *
 * @author Michael Kühweg
 */
public class QueryResultTableView extends TableView<ObservableList<String>> {

    private final ResultFormatter resultFormatter;

    public QueryResultTableView(final ResultFormatter resultFormatter) {
        super();
        this.resultFormatter = resultFormatter;
    }

    public void buildTableView() {
        int i = 0;
        for (final String head : resultFormatter.getHeader()) {
            final TableColumn<ObservableList<String>, String> col =
                    new TableColumn<>(
                    head);
            col.setMinWidth(100);
            final int accessIndex = i++;
            col.setCellValueFactory(
                    new Callback<CellDataFeatures<ObservableList<String>, String>, ObservableValue<String>>() {
                @Override
                public ObservableValue<String> call(
                        final CellDataFeatures<ObservableList<String>, String> param) {
                    return new SimpleStringProperty(param.getValue()
                            .get(accessIndex).toString());
                }
            });
            getColumns().add(col);
        }
        final ObservableList<ObservableList<String>> content = FXCollections
                .observableArrayList();
        if (resultFormatter.isHeadOnly()) {
            content.add(FXCollections
                    .observableArrayList(MessageFormat.format(
                    DialogDictionary.PATTERN_UPDATECOUNT.toString(),
                    resultFormatter.getUpdateCount())));
        } else {
            for (final List<String> rowFromFormatter : resultFormatter
                    .getRows()) {
                final ObservableList<String> row = FXCollections
                        .observableArrayList();
                for (final String column : rowFromFormatter) {
                    row.add(column);
                }
                content.add(row);
            }
        }
        setItems(content);
    }

    public String toHtml() {
        if (resultFormatter != null) {
            try {
                return new HtmlResultFormatter(resultFormatter).formatAsHtml();
            } catch (final IOException ex) {
                return ex.toString();
            }
        }
        return "";
    }
}
