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
import java.text.MessageFormat;

/**
 * Vorlage für den Export eines Abfrageergebnisses.
 *
 * @author Michael Kühweg
 */
public class ResultTemplate implements Serializable {

	private static final long serialVersionUID = 7747205068436233755L;

	private static final String FALLBACK_TEMPLATE = "{0}\n{1}\n{2}\n{3}\n";

	private String template;

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

	public void setTemplate(final String template) {
		this.template = template;
	}

	public String getExecutionInformation() {
		return executionInformation;
	}

	public void setExecutionInformation(final String executionInformation) {
		this.executionInformation = executionInformation;
	}

	public String getResultTable() {
		return resultTable;
	}

	public void setResultTable(final String resultTable) {
		this.resultTable = resultTable;
	}

	public String getRowCount() {
		return rowCount;
	}

	public void setRowCount(final String rowCount) {
		this.rowCount = rowCount;
	}

	public String getLimitedRows() {
		return limitedRows;
	}

	public void setLimitedRows(final String limitedRows) {
		this.limitedRows = limitedRows;
	}

	protected final String buildWithFallbackTemplate() {
		return MessageFormat.format(FALLBACK_TEMPLATE,
				getExecutionInformation() != null ? getExecutionInformation() : "",
				getResultTable() != null ? getResultTable() : "", getRowCount() != null ? getRowCount() : "",
				getLimitedRows() != null ? getLimitedRows() : "");
	}

	public String buildWithTemplate() {
		if (getTemplate() == null) {
			return "";
		}
		try {
			return MessageFormat.format(getTemplate(),
					getExecutionInformation() != null ? getExecutionInformation() : "",
					getResultTable() != null ? getResultTable() : "", getRowCount() != null ? getRowCount() : "",
					getLimitedRows() != null ? getLimitedRows() : "");
		} catch (final IllegalArgumentException e) {
			// bei defektem Template wird eine Standardformatierung verwendet
			return buildWithFallbackTemplate();
		}
	}
}
