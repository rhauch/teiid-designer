/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.jdbc.metadata.impl;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IStatus;
import com.metamatrix.modeler.jdbc.JdbcException;
import com.metamatrix.modeler.jdbc.JdbcPlugin;
import com.metamatrix.modeler.jdbc.metadata.JdbcDatabase;
import com.metamatrix.modeler.jdbc.metadata.JdbcNode;
import com.metamatrix.modeler.jdbc.metadata.JdbcProcedureType;

/**
 * JdbcProcedureTypeImpl
 */
public class JdbcProcedureTypeImpl extends JdbcNodeImpl implements JdbcProcedureType {
    static final String SQL_SERVER_DB_METADATA = "SQLServerDatabaseMetaData"; //$NON-NLS-1$
    
    /**
     * Construct an instance of JdbcProcedureTypeImpl.
     * 
     */
    public JdbcProcedureTypeImpl( final JdbcNode parent, final String procTerm ) {
        super(PROCEDURE_TYPE,procTerm,parent);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.modeler.jdbc.metadata.impl.JdbcNodeImpl#computeChildren()
     */
    @Override
    protected JdbcNode[] computeChildren() throws JdbcException {
        // Create the children ...
        final DatabaseMetaData metadata = getJdbcDatabase().getDatabaseMetaData();
        final List children = new ArrayList();
        
        final String schemaName = getSchemaName(this);
        final String catalogName = getCatalogName(this);
        
        boolean isSqlServer = metadata.getClass().getName().endsWith(SQL_SERVER_DB_METADATA);
        
        // Get the procedures ...
        ResultSet resultSet = null;
        try {
            resultSet = metadata.getProcedures(catalogName,schemaName,WILDCARD_PATTERN);
            while( resultSet.next() ) {
                final String procName = resultSet.getString(3);
                final String remarks = resultSet.getString(7);
                final short procType = resultSet.getShort(8);
                final JdbcProcedureImpl proc = new JdbcProcedureImpl(this,procName);
                proc.setRemarks(remarks);
                proc.setProcedureType(procType);
                children.add(proc);
            }
        } catch (Throwable t) {
            if (!isSqlServer) {
                final Object[] params = new Object[]{metadata.getClass().getName(),getJdbcDatabase()};
                final String msg = JdbcPlugin.Util.getString("JdbcProcedureTypeImpl.Unexpected_exception_while_calling_getProcedures()_and_processing_results",params); //$NON-NLS-1$
                JdbcPlugin.Util.log(IStatus.WARNING,t,msg);
            }
        } finally {
            if ( resultSet != null ) {
                try {
                    resultSet.close();
                } catch (SQLException e) {
                    JdbcPlugin.Util.log(e);
                }
            }
        }
      
        // Convert the list to an array and return ...
        return (JdbcNode[])children.toArray(new JdbcNode[children.size()]);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.modeler.jdbc.metadata.impl.JdbcNodeImpl#getTypeName()
     */
    public String getTypeName() {
        return JdbcPlugin.Util.getString("JdbcProcedureTypeImpl.ProcedureTypeName"); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see com.metamatrix.modeler.jdbc.metadata.JdbcNode#getFullyQualifiedName()
     */
    public String getFullyQualifiedName() {
        return getParent().getFullyQualifiedName();
    }

    /* (non-Javadoc)
     * @see com.metamatrix.modeler.jdbc.metadata.JdbcNode#getPathInSource()
     */
    @Override
    public IPath getPathInSource() {
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.metamatrix.modeler.jdbc.metadata.JdbcNode#getPathInSource(boolean, boolean)
     */
    public IPath getPathInSource( final boolean includeCatalog, final boolean includeSchema) {
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.metamatrix.modeler.jdbc.metadata.JdbcNode#getParentDatabaseObject(boolean, boolean)
     */
    @Override
    public JdbcNode getParentDatabaseObject(final boolean includeCatalog, final boolean includeSchema) {
        return null;
    }
    
    /* (non-Javadoc)
     * @see com.metamatrix.modeler.jdbc.metadata.JdbcNode#isDatabaseObject()
     */
    @Override
    public boolean isDatabaseObject() {
        return false;
    }


    /* (non-Javadoc)
     * @see com.metamatrix.modeler.jdbc.metadata.JdbcNode#getJdbcDatabase()
     */
    public JdbcDatabase getJdbcDatabase() {
        return getParent().getJdbcDatabase();
    }
}
