/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.relationship.util;

import org.eclipse.emf.ecore.EObject;
import com.metamatrix.metamodels.relationship.RelationshipType;

/**
 * RelationshipTypeFinder.java
 */
public class RelationshipTypeFinder extends RelationshipEntityFinder {

    /* (non-Javadoc)
     * @see com.metamatrix.modeler.core.util.ModelVisitor#visit(org.eclipse.emf.ecore.EObject)
     */
    public boolean visit( EObject object ) {
        if (object instanceof RelationshipType) {
            // collect the Relationship
            found((RelationshipType)object);
        }
        return false;
    }

}
