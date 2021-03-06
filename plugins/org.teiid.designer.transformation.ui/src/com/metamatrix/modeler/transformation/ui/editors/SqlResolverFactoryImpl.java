/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.transformation.ui.editors;

import org.eclipse.emf.ecore.EObject;
import com.metamatrix.modeler.transformation.metadata.TransformationMetadataFactory;
import com.metamatrix.modeler.transformation.ui.editors.sqleditor.SqlResolverFactory;

import org.teiid.query.metadata.QueryMetadataInterface;

/**
 * SqlResolverFactoryImpl
 */
public class SqlResolverFactoryImpl implements SqlResolverFactory {
    
    private EObject eObj;
    private QueryMetadataInterface metadata;
    
    public void setCurrentEObject(EObject eObject) {
        this.eObj = eObject;
        this.metadata = null;
    }
    
    public QueryMetadataInterface getQueryMetadata() {
    	if(metadata == null) {
    		metadata = TransformationMetadataFactory.getInstance().getModelerMetadata(this.eObj); 
    	}
        return metadata;
    }

}
