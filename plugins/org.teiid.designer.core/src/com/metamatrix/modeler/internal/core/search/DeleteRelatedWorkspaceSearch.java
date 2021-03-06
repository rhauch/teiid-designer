/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.core.search;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import com.metamatrix.modeler.core.ModelerCore;
import com.metamatrix.modeler.core.index.IndexSelector;
import com.metamatrix.modeler.core.workspace.ModelResource;
import com.metamatrix.modeler.internal.core.index.ModelWorkspaceSearchIndexSelector;
import com.metamatrix.modeler.internal.core.workspace.ModelWorkspaceManager;


/** 
 * Class used to fine-tune the delete-related workspace search
 * In particular, it insures that if certain model types (i.e. xsd files) are out of scope for the search, they don't get 
 * included. This class can be expanded in the future to handle other cases to help reduce "time-to-delete"
 * see Defect 22774
 * @since 5.0.2
 */
public class DeleteRelatedWorkspaceSearch extends ModelWorkspaceSearch {

    boolean ignoreXsdResources = false;
    /** 
     * 
     * @since 5.0.2
     */
    public DeleteRelatedWorkspaceSearch() {
        super();
    }

    public DeleteRelatedWorkspaceSearch(boolean ignoreXsdResources) {
        this();
        this.ignoreXsdResources = ignoreXsdResources;
    }
    
    @Override
    protected IndexSelector createIndexSelector( IProgressMonitor monitor ) {
        return new ModelWorkspaceSearchIndexSelector(getApplicableModelResources(), monitor);
    }
    
    protected Collection getApplicableModelResources() {
        Collection modelResources = new ArrayList();
        ModelResource[] resources = null;
        try {
            resources = ModelWorkspaceManager.getModelWorkspaceManager().getModelWorkspace().getModelResources();
        } catch (CoreException theException) {
            ModelerCore.Util.log(IStatus.ERROR,theException,theException.getMessage());
        }
        if( !ignoreXsdResources ) {
            modelResources = Arrays.asList(resources);
        } else {
            if( resources != null && resources.length > 0 ) {
                // Look for XSD files, if NOT xsd, add to collection
                for( int i=0; i<resources.length; i++ ) {
                    if( !resources[i].isXsd()) {
                        modelResources.add(resources[i]);
                    }
                }
            }
        }
        
        return modelResources;
    }
    
}
