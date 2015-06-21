/*
 * Copyright (c) 2015, Michael Kühweg
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
package de.kuehweg.sqltool.database.formatter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Vorlage für den HTML-Export eines Abfrageergebnisses
 *
 * @author Michael Kühweg
 */
public class ResultTemplate implements Serializable {

    private static final long serialVersionUID = 7747205068436233754L;

    private String template;
    private String placeholderExecution;
    private String placeholderResultTable;
    private String placeholderRowCount;
    private String placeholderLimitRows;

    private String executionInformation;
    private String resultTable;
    private String rowCount;
    private String limitedRows;

    public ResultTemplate() {
    }

    public ResultTemplate(final String template) {
        this.template = template;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public String getPlaceholderExecution() {
        return placeholderExecution;
    }

    public void setPlaceholderExecution(String placeholderExecution) {
        this.placeholderExecution = placeholderExecution;
    }

    public String getPlaceholderResultTable() {
        return placeholderResultTable;
    }

    public void setPlaceholderResultTable(String placeholderResultTable) {
        this.placeholderResultTable = placeholderResultTable;
    }

    public String getPlaceholderRowCount() {
        return placeholderRowCount;
    }

    public void setPlaceholderRowCount(String placeholderRowCount) {
        this.placeholderRowCount = placeholderRowCount;
    }

    public String getPlaceholderLimitRows() {
        return placeholderLimitRows;
    }

    public void setPlaceholderLimitRows(String placeholderLimitRows) {
        this.placeholderLimitRows = placeholderLimitRows;
    }

    public String getExecutionInformation() {
        return executionInformation;
    }

    public void setExecutionInformation(String executionInformation) {
        this.executionInformation = executionInformation;
    }

    public String getResultTable() {
        return resultTable;
    }

    public void setResultTable(String resultTable) {
        this.resultTable = resultTable;
    }

    public String getRowCount() {
        return rowCount;
    }

    public void setRowCount(String rowCount) {
        this.rowCount = rowCount;
    }

    public String getLimitedRows() {
        return limitedRows;
    }

    public void setLimitedRows(String limitedRows) {
        this.limitedRows = limitedRows;
    }

    private List<String> splitTemplate(final String part, final String placeholder) {
        List<String> split = new LinkedList<>();
        if (part != null && placeholder != null) {
            int pos = part.indexOf(placeholder);
            if (pos < 0) {
                split.add(part);
            } else {
                if (pos > 0) {
                    split.add(part.substring(0, pos));
                }
                split.add(placeholder);
                if (pos + placeholder.length() < part.length()) {
                    split.addAll(splitTemplate(part.substring(pos + placeholder.length()),
                            placeholder));
                }
            }
        }
        return split;
    }

    private List<String> splitTemplate(final List<String> parts, final String placeholder) {
        List<String> split = new LinkedList<>();
        for (String part : parts) {
            split.addAll(splitTemplate(part, placeholder));
        }
        return split;
    }

    private List<String> fillInPlaceholder(final List<String> parts,
            final String placeholder, final String replacement) {
        List<String> withReplacements = new ArrayList<>(parts.size());
        for (String part : parts) {
            if (placeholder.equals(part)) {
                withReplacements.add(replacement != null ? replacement : "");
            } else {
                withReplacements.add(part);
            }
        }
        return withReplacements;
    }

    public String buildWithTemplate() {
        String buildWithMe = getTemplate() != null ? getTemplate() : "";
        List<String> parts = new LinkedList<>();
        parts.add(buildWithMe);
        parts = splitTemplate(parts, placeholderExecution);
        parts = splitTemplate(parts, placeholderResultTable);
        parts = splitTemplate(parts, placeholderRowCount);
        parts = splitTemplate(parts, placeholderLimitRows);

        parts = fillInPlaceholder(parts, placeholderExecution, getExecutionInformation());
        parts = fillInPlaceholder(parts, placeholderResultTable, getResultTable());
        parts = fillInPlaceholder(parts, placeholderRowCount, getRowCount());
        parts = fillInPlaceholder(parts, placeholderLimitRows, getLimitedRows());

        StringBuilder builder = new StringBuilder();
        for (String part : parts) {
            builder.append(part);
        }
        return builder.toString();
    }
}
