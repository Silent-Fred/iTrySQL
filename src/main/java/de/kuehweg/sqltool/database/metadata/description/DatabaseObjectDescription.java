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
package de.kuehweg.sqltool.database.metadata.description;

import java.util.Objects;

/**
 * Datenbankobjekte als Beschreibung für die Verwendung z.B. in
 * Strukturansichten
 *
 * @author Michael Kühweg
 */
public abstract class DatabaseObjectDescription implements Comparable<DatabaseObjectDescription> {

	private final String name;

	private DatabaseObjectDescription parent;

	public DatabaseObjectDescription(final String name) {
		this.name = name == null ? "" : name;
	}

	public String getName() {
		return name;
	}

	public DatabaseObjectDescription getParent() {
		return parent;
	}

	protected void setParent(final DatabaseObjectDescription parent) {
		this.parent = parent;
	}

	/**
	 * Navigiert in der Hierarchie nach oben, bis zur angegebenen Klasse.
	 *
	 * @param levelClass
	 *            Klasse der gesuchten Hierarchieebene.
	 * @return
	 */
	public DatabaseObjectDescription findHierarchyLevel(final Class<? extends DatabaseObjectDescription> levelClass) {
		DatabaseObjectDescription currentLevel = this;
		while (!currentLevel.getClass().isAssignableFrom(levelClass)) {
			currentLevel = currentLevel.getParent();
		}
		return currentLevel;
	}

	public String getQualifiedName() {
		return isOrphan() ? getName() : parent.getName() + "." + getName();
	}

	public String getFullyQualifiedName() {
		return isOrphan() ? getName() : parent.getFullyQualifiedName() + "." + getName();
	}

	public boolean isOrphan() {
		return getParent() == null;
	}

	public void adoptOrphan(final DatabaseObjectDescription child) {
		if (!child.isOrphan()) {
			throw new IllegalArgumentException("Attempt to adopt a child that is not orphaned (parent: "
					+ getFullyQualifiedName() + " child: " + child.getFullyQualifiedName() + ")");
		}
		child.setParent(this);
		appendChild(child);
	}

	/**
	 * Fügt ein Kindelement hinzu. Abgeleitete Klassen, die Kindelemente
	 * verwalten, müssen diese Methode überschreiben.
	 *
	 * @param child
	 */
	protected void appendChild(final DatabaseObjectDescription child) {
		throw new IllegalArgumentException("Attempt to add a child of class " + child.getClass().getSimpleName()
				+ " to " + this.getClass().getSimpleName() + ".");
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 53 * hash + Objects.hashCode(name);
		return hash;
	}

	@Override
	public boolean equals(final Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final DatabaseObjectDescription other = (DatabaseObjectDescription) obj;
		return Objects.equals(name, other.name);
	}

	@Override
	public int compareTo(final DatabaseObjectDescription other) {
		return getName().compareTo(other.getName());
	}
}
