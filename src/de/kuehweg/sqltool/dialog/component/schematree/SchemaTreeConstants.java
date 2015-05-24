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
package de.kuehweg.sqltool.dialog.component.schematree;

/**
 * Konstanten für die Strukturansicht
 *
 * @author Michael Kühweg
 */
public class SchemaTreeConstants {

    public static final String DATABASE = "d";
    public static final String USER = "u";
    public static final String TABLE = "t";
    public static final String COLUMN = "c";
    public static final String PRIMARY_KEY = "p";
    public static final String FOREIGN_KEY_CONSTRAINT = "f";
    public static final String REFERENCES = "r";
    public static final String INDEX = "i";

    public static final String STYLE_DATABASE = "schema-tree-database";
    public static final String STYLE_USER = "schema-tree-user";
    public static final String STYLE_TABLE = "schema-tree-table";
    public static final String STYLE_COLUMN = "schema-tree-column";
    public static final String STYLE_PRIMARY_KEY = "schema-tree-primary-key";
    public static final String STYLE_FOREIGN_KEY_CONSTRAINT = "schema-tree-foreign-key-constraint";
    public static final String STYLE_REFERENCES = "schema-tree-references";
    public static final String STYLE_INDEX = "schema-tree-index";
    
    private SchemaTreeConstants() {
    }
}
