/*
 * Copyright (c) 2015-2016, Michael Kühweg
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

package de.kuehweg.sqltool.dialog.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 * @author Michael Kühweg
 */
public class TextTreeViewFindAction extends FindAction {

	private final TreeView<String> tree;

	private TreeItem<String> findFrom;

	public TextTreeViewFindAction(final TreeView<String> tree) {
		super();
		this.tree = tree;
	}

	@Override
	public void find(final String searchString) {
		deselectAllOccurrencesInComponent();
		findFrom = findInFlattenedTree(searchString, null, flattenedTreeForEasierSearch(tree));
		if (findFrom != null) {
			selectOccurrenceInTreeView(findFrom);
		}
	}

	@Override
	public void nextOccurrence(final String searchString) {
		if (searchString != null && !searchString.isEmpty()) {
			final List<TreeItem<String>> forward = flattenedTreeForEasierSearch(tree);
			nextOccurrenceInFlattenedTree(searchString, forward);
		}
	}

	@Override
	public void previousOccurrence(final String searchString) {
		if (searchString != null && !searchString.isEmpty()) {
			final List<TreeItem<String>> backwards = flattenedTreeForEasierSearch(tree);
			Collections.reverse(backwards);
			nextOccurrenceInFlattenedTree(searchString, backwards);
		}
	}

	private TreeItem<String> findInFlattenedTree(final String searchString, final TreeItem<String> findFrom,
			final List<TreeItem<String>> flattenedTree) {
		final String preparedSearchString = preparedSearchString(searchString);
		boolean skip = findFrom != null;
		for (final TreeItem<String> item : flattenedTree) {
			skip = skip && item != findFrom;
			if (!skip && item != findFrom && getValueSafeAndLowerCase(item).contains(preparedSearchString)) {
				return item;
			}
		}
		return null;
	}

	private List<TreeItem<String>> flattenedTreeForEasierSearch(final TreeView<String> tree) {
		return flattenedSubtreeForEasierSearch(tree.getRoot());
	}

	private List<TreeItem<String>> flattenedSubtreeForEasierSearch(final TreeItem<String> subtree) {
		final List<TreeItem<String>> flattenedSubtree = new ArrayList<>();
		flattenedSubtree.add(subtree);
		if (subtree.getChildren() != null) {
			for (final TreeItem<String> subSubtree : subtree.getChildren()) {
				flattenedSubtree.addAll(flattenedSubtreeForEasierSearch(subSubtree));
			}
		}
		return flattenedSubtree;
	}

	private void nextOccurrenceInFlattenedTree(final String searchString, final List<TreeItem<String>> flattenedTree) {
		final TreeItem<String> nextOccurrence = findInFlattenedTree(searchString, findFrom, flattenedTree);
		if (nextOccurrence != null) {
			findFrom = nextOccurrence;
			selectOccurrenceInTreeView(findFrom);
		}
	}

	private void deselectAllOccurrencesInComponent() {
		tree.getSelectionModel().clearSelection();
	}

	private void selectOccurrenceInTreeView(final TreeItem<String> item) {
		tree.getSelectionModel().select(item);
		TreeItem<String> parent = item.getParent();
		while (parent != null) {
			parent.setExpanded(true);
			parent = parent.getParent();
		}
		tree.scrollTo(tree.getSelectionModel().getSelectedIndex());
	}

	private String getValueSafeAndLowerCase(final TreeItem<String> treeItem) {
		return treeItem.getValue() == null ? "" : treeItem.getValue().toLowerCase();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.kuehweg.sqltool.dialog.action.FindAction#resetSearchPosition()
	 */
	@Override
	public void resetSearchPosition() {
		findFrom = null;
	}

}
