/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.transformation.compare;

import org.eclipse.emf.ecore.EObject;
import com.metamatrix.metamodels.transformation.InputParameter;
import com.metamatrix.modeler.core.compare.AbstractEObjectNameMatcher;


/** 
 * @since 4.2
 */
public class InputParameterNameToNameIgnoreCaseMatcher extends AbstractEObjectNameMatcher {

    public InputParameterNameToNameIgnoreCaseMatcher() {
        super();
    }

    @Override
    protected String getInputKey( final EObject entity ) {
        if(entity instanceof InputParameter) {
            final String name = ((InputParameter)entity).getName();
            if(name != null) {
                return name.toUpperCase();
            }
        }
        return null;
    }

    @Override
    protected String getOutputKey( final EObject entity ) {
        if(entity instanceof InputParameter) {
            final String name = ((InputParameter)entity).getName();
            if(name != null) {
                return name.toUpperCase();
            }
        }
        return null;
    }

}
