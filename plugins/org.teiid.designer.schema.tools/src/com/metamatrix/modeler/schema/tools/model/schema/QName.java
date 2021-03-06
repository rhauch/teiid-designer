/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.schema.tools.model.schema;

public interface QName {

	public abstract boolean equals(Object o);

	public abstract int hashCode();

	public abstract String toString();

	public abstract String getNamespace();

	public abstract void setNamespace(String namespace);

	public abstract String getLName();

}
