/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.uml2.compare;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.mapping.Mapping;
import org.eclipse.emf.mapping.MappingFactory;
import org.eclipse.uml2.uml.NamedElement;
import com.metamatrix.modeler.core.compare.AbstractEObjectMatcher;

/**
 * UmlNamedElementMatcher
 */
public class UmlNamedElementCaseInsensitiveMatcher extends AbstractEObjectMatcher {

    /**
     * Construct an instance of UmlObjectMatcher.
     * 
     */
    public UmlNamedElementCaseInsensitiveMatcher() {
        super();
    }

    /**
     * @see com.metamatrix.modeler.core.compare.EObjectMatcher#addMappingsForRoots(java.util.List, java.util.List, org.eclipse.emf.mapping.Mapping, org.eclipse.emf.mapping.MappingFactory)
     */
    public void addMappingsForRoots(final List inputs, final List outputs, 
                                    final Mapping mapping, final MappingFactory factory) {
        // Delegate ...
        addMappings(null,inputs,outputs,mapping,factory);
    }

    /**
     * @see com.metamatrix.modeler.core.compare.EObjectMatcher#addMappings(org.eclipse.emf.ecore.EReference, java.util.List, java.util.List, org.eclipse.emf.mapping.Mapping, org.eclipse.emf.mapping.MappingFactory)
     */
    public void addMappings(final EReference reference, final List inputs, final List outputs, 
                            final Mapping mapping, final MappingFactory factory) {
        //
        // Loop over the inputs and accumulate the UUIDs ...
        final Map inputByName = new HashMap();
        final Iterator iter = inputs.iterator();
        while (iter.hasNext()) {
            final EObject obj = (EObject)iter.next();
            if ( obj instanceof NamedElement ) {
                final NamedElement entity = (NamedElement)obj;
                final String key = entity.getName();
                if ( key != null && key.length() != 0 ) {
                    inputByName.put(key.toUpperCase(),obj);
                }
            }
        }
        
        if ( inputByName.isEmpty() ) {
            return;
        }
        
        // Loop over the outputs and compare the names ...
        final Iterator outputIter = outputs.iterator();
        while (outputIter.hasNext()) {
            final EObject output = (EObject)outputIter.next();
            if ( output instanceof NamedElement ) {
                final NamedElement outputEntity = (NamedElement)output;
                final String key = outputEntity.getName();
                if ( key != null ) {
                    final NamedElement inputEntity = (NamedElement) inputByName.get(key.toUpperCase());
                    if ( inputEntity != null ) {
                        final EClass inputMetaclass = inputEntity.eClass();
                        final EClass outputMetaclass = outputEntity.eClass();
                        if ( inputMetaclass.equals(outputMetaclass) ) {
                            inputs.remove(inputEntity);
                            outputIter.remove();
                            addMapping(inputEntity,outputEntity,mapping,factory);
                        }
                    }
                }
            }
        }

    }

}
