/*
 * Copyright (c) 2014-2015, Michael Kühweg
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
package de.kuehweg.sqltool.database.metadata.description;

import java.util.Objects;

/**
 * @author Michael Kühweg
 */
public abstract class ForeignKeyColumnDescription extends DatabaseObjectDescription {

    private final String foreignKeyName;
    private final String insideColumnName;
    private final String outsideCatalog;
    private final String outsideSchema;
    private final String outsideTableName;
    private final String outsideColumnName;

    public ForeignKeyColumnDescription(String foreignKeyName, String insideColumnName,
            String outsideCatalog, String outsideSchema, String outsideTableName,
            String outsideColumnName) {
        super(foreignKeyName);
        this.foreignKeyName = foreignKeyName == null ? "" : foreignKeyName;
        this.insideColumnName = insideColumnName == null ? "" : insideColumnName;
        this.outsideCatalog = outsideCatalog == null ? "" : outsideCatalog;
        this.outsideSchema = outsideSchema == null ? "" : outsideSchema;
        this.outsideTableName = outsideTableName == null ? "" : outsideTableName;
        this.outsideColumnName = outsideColumnName == null ? "" : outsideColumnName;
    }

    public String getInsideColumnName() {
        return insideColumnName;
    }

    public String getOutsideCatalog() {
        return outsideCatalog;
    }

    public String getOutsideSchema() {
        return outsideSchema;
    }

    public String getOutsideTableName() {
        return outsideTableName;
    }

    public String getOutsideColumnName() {
        return outsideColumnName;
    }

    private boolean referencesOutsideSchema() {
        DatabaseObjectDescription schema = findHierarchyLevel(SchemaDescription.class);
        return schema == null || !schema.getName().equals(outsideSchema);
    }

    private boolean referencesOutsideCatalog() {
        DatabaseObjectDescription catalog = findHierarchyLevel(CatalogDescription.class);
        return catalog == null || !catalog.getName().equals(outsideCatalog);
    }

    public boolean referencesFarOutside() {
        return referencesOutsideSchema() || referencesOutsideCatalog();
    }

    public String getQualifiedOutsideColumn() {
        return getOutsideTableName() + "." + getOutsideColumnName();
    }

    public String getFullyQualifiedOutsideColumn() {
        return getOutsideCatalog() + "." + getOutsideSchema() + "." + getOutsideTableName() + "."
                + getOutsideColumnName();
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 89 * hash + Objects.hashCode(this.foreignKeyName);
        hash = 89 * hash + Objects.hashCode(this.insideColumnName);
        hash = 89 * hash + Objects.hashCode(this.outsideCatalog);
        hash = 89 * hash + Objects.hashCode(this.outsideSchema);
        hash = 89 * hash + Objects.hashCode(this.outsideTableName);
        hash = 89 * hash + Objects.hashCode(this.outsideColumnName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            final ForeignKeyColumnDescription other = (ForeignKeyColumnDescription) obj;
            if (!Objects.equals(this.insideColumnName, other.insideColumnName)) {
                return false;
            }
            if (!Objects.equals(this.outsideCatalog, other.outsideCatalog)) {
                return false;
            }
            if (!Objects.equals(this.outsideSchema, other.outsideSchema)) {
                return false;
            }
            if (!Objects.equals(this.outsideTableName, other.outsideTableName)) {
                return false;
            }
            return Objects.equals(this.outsideColumnName, other.outsideColumnName);
        }
        return false;
    }

}
