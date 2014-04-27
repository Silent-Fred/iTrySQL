/*
 * Copyright (c) 2014, Michael Kühweg
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

import java.util.HashSet;
import java.util.Set;
import javafx.collections.ObservableList;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * Verwaltung der geöffneten Elemente der Schema-Baumansicht
 *
 * @author Michael Kühweg
 */
public class SchemaTreeExpandedStateSaver {

    private Set<String> expanded = new HashSet<>();

    public void readExpandedStateFrom(final TreeView<String> tree) {
        expanded.clear();
        if (tree != null && tree.getRoot() != null) {
            recurseRead(tree.getRoot(), tree.getRoot().getValue());
        }
    }

    public void expandFromSavedState(final TreeView<String> tree) {
        if (tree != null && tree.getRoot() != null) {
            recurseExpand(tree.getRoot(), tree.getRoot().getValue());
        }
    }

    private void recurseRead(final TreeItem<String> parent,
            final String parentKey) {
        if (parent != null) {
            if (parent.isExpanded()) {
                expanded.add(parentKey);
                ObservableList<TreeItem<String>> children = parent.getChildren();
                for (final TreeItem<String> child : children) {
                    recurseRead(child, parentKey + "." + child.getValue());
                }
            }
        }
    }

    private void recurseExpand(final TreeItem<String> parent,
            final String parentKey) {
        if (parent != null) {
            parent.setExpanded(expanded.contains(parentKey));
            ObservableList<TreeItem<String>> children = parent.getChildren();
            for (final TreeItem<String> child : children) {
                recurseExpand(child, parentKey + "." + child.getValue());
            }
        }
    }
}
