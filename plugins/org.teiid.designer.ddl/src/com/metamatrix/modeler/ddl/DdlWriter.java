/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.ddl;

import java.io.OutputStream;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.resource.Resource;
import org.teiid.logging.Logger;
import com.metamatrix.modeler.core.workspace.ModelResource;
import com.metamatrix.modeler.core.workspace.ModelWorkspaceSelections;

/**
 * DdlWriter
 */
public interface DdlWriter {

    /**
     * Get the logger that this writer is using.
     * 
     * @return the logger; never null, but may be a {@link com.metamatrix.core.log.NullLogger NullLogger} if there is no logging.
     */
    public Logger getLogger();

    /**
     * Set the logger that this writer should use.
     * 
     * @param logger the new logger; may be null or a {@link com.metamatrix.core.log.NullLogger NullLogger} if there is to be no
     *        logging.
     */
    public void setLogger( Logger logger );

    /**
     * Get the options that this writer is currently using
     * 
     * @return the options; never null
     */
    public DdlOptions getOptions();

    /**
     * Write out the supplied model as DDL to the supplied stream.
     * 
     * @param emfResource the {@link com.metamatrix.metamodels.relational.RelationalPackage relational} EMF resource that contains
     *        the model to be written out; may not be null
     * @param modelName the name of the model in the resource; may not be null
     * @param modelFilename the name of the model resource
     * @param stream the stream to which the DDL is to be written; may not be null
     * @param monitor the monitor the should be used; may be null
     * @return a status of the process with any {@link IStatus#WARNING warnings}, {@link IStatus#ERROR errors} or
     *         {@link IStatus#INFO information messages}, or which will be {@link IStatus#isOK() marked as OK} if there were no
     *         warnings, errors or other messages.
     */
    public IStatus write( Resource emfResource,
                          String modelName,
                          String modelFilename,
                          OutputStream stream,
                          IProgressMonitor monitor );

    /**
     * Write out the supplied model as DDL to the supplied stream.
     * 
     * @param model the {@link com.metamatrix.metamodels.relational.RelationalPackage relational} model that is to be written out;
     *        may not be null
     * @param stream the stream to which the DDL is to be written; may not be null
     * @param monitor the monitor the should be used; may be null
     * @return a status of the process with any {@link IStatus#WARNING warnings}, {@link IStatus#ERROR errors} or
     *         {@link IStatus#INFO information messages}, or which will be {@link IStatus#isOK() marked as OK} if there were no
     *         warnings, errors or other messages.
     */
    public IStatus write( ModelResource model,
                          OutputStream stream,
                          IProgressMonitor monitor );

    /**
     * Write out the selected objects in the {@link ModelWorkspace model workspace} as DDL to the supplied stream.
     * 
     * @param model the {@link com.metamatrix.metamodels.relational.RelationalPackage relational} model that is to be written out;
     *        may not be null
     * @param stream the stream to which the DDL is to be written; may not be null
     * @param monitor the monitor the should be used; may be null
     * @return a status of the process with any {@link IStatus#WARNING warnings}, {@link IStatus#ERROR errors} or
     *         {@link IStatus#INFO information messages}, or which will be {@link IStatus#isOK() marked as OK} if there were no
     *         warnings, errors or other messages.
     */
    public IStatus write( ModelWorkspaceSelections selections,
                          OutputStream stream,
                          IProgressMonitor monitor );
}
