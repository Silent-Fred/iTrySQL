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
package de.kuehweg.sqltool.dialog.component.schematree.node;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Oberflächenunabhäbgige Respräsentation eines Knotens in der Strukturansicht
 *
 * @author Michael Kühweg
 */
public class SchemaTreeNode {

    private final SchemaTreeNodeType type;
    private final String title;
    private final List<SchemaTreeNode> children;

    public SchemaTreeNode(SchemaTreeNodeType type, String title) {
        this.type = type;
        this.title = title;
        children = new LinkedList<>();
    }

    public SchemaTreeNodeType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public List<SchemaTreeNode> getChildren() {
        return new ArrayList<>(children);
    }

    /**
     * Fügt einen weiteren Knoten am Ende an. Leere Knoten (null) werden dabei ignoriert
     * und nicht hinzugefügt.
     *
     * @param child
     */
    public void appendChild(final SchemaTreeNode child) {
        children.add(child);
    }
}
