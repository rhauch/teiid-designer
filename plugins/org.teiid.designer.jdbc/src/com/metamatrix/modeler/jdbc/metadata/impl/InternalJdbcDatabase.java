/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.jdbc.metadata.impl;

/**
 * InternalJdbcDatabase
 */
public interface InternalJdbcDatabase {

    /**
     * Return the cache of JdbcNode instances.
     * @return
     */
    public JdbcNodeCache getJdbcNodeCache();

    /**
     * Return the node selections.
     * @return
     */
    public JdbcNodeSelections getJdbcNodeSelections();
}
