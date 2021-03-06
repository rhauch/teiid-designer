/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.modelgenerator.uml2.processor;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.uml2.uml.NamedElement;

import com.metamatrix.metamodels.relational.RelationalEntity;
import com.metamatrix.metamodels.relationship.Relationship;
import com.metamatrix.metamodels.relationship.RelationshipFactory;
import com.metamatrix.modeler.compare.selector.ModelSelector;
import com.metamatrix.modeler.core.validation.rules.StringNameValidator;
import com.metamatrix.modeler.modelgenerator.uml2.Uml2ModelGeneratorPlugin;

/**
 * Uml2RelationalRelationTrackerImpl
 */
public class Uml2RelationalRelationTrackerImpl extends RelationTrackerImpl {

    private StringNameValidator validator = new StringNameValidator();
    
    /**
     * Construct an instance of Uml2RelationalRelationTrackerImpl.
     * @param relationModelSelector
     * @param factory
     */
    public Uml2RelationalRelationTrackerImpl(
        ModelSelector relationModelSelector,
        RelationshipFactory factory) {
        super(relationModelSelector, factory);
    }
    
    /**
     * @see com.metamatrix.modeler.modelgenerator.uml2.processor.RelationTrackerImpl#doSetName(com.metamatrix.metamodels.relationship.Relationship, org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
     */
    @Override
    protected void doSetName(Relationship relationship, EObject input, EObject output) {
        final Object[] params = new Object[4];
        if ( input != null ) {
            params[2] = input.eClass().getName();
            if ( input instanceof NamedElement ) {
                params[3] = ((NamedElement)input).getName();
            } else {
                params[3] = "input";   // not expected ... //$NON-NLS-1$
            }
        } else {
            params[2] = ""; //$NON-NLS-1$
            params[3] = ""; //$NON-NLS-1$
        }
        if ( output != null ) {
            params[0] = output.eClass().getName();
            if ( output instanceof RelationalEntity ) {
                params[1] = ((RelationalEntity)output).getName();
            } else {
                params[1] = "output";   // not expected ... //$NON-NLS-1$
            }
        } else {
            params[0] = ""; //$NON-NLS-1$
            params[1] = ""; //$NON-NLS-1$
        }
        String name = Uml2ModelGeneratorPlugin.Util.getString("Uml2RelationalRelationTrackerImpl.RelationalMetaclass_EntityName_generated_from_UMLmetaclass_EntityName",params); //$NON-NLS-1$
        final String validName = validator.createUniqueName(name);
        name = (validName == null ? name : validName);
        relationship.setName(name);
    }

    /**
     * @see com.metamatrix.modeler.modelgenerator.uml2.processor.RelationTrackerImpl#doSetName(com.metamatrix.metamodels.relationship.Relationship, org.eclipse.emf.ecore.EObject, java.util.List)
     */
    @Override
    protected void doSetName(Relationship relationship, EObject input, List outputs) {
        final Object[] params = new Object[4];
        if ( input != null ) {
            params[0] = input.eClass().getName();
            if ( input instanceof NamedElement ) {
                params[1] = ((NamedElement)input).getName();
            } else {
                params[1] = "input";   // not expected ... //$NON-NLS-1$
            }
        } else {
            params[0] = ""; //$NON-NLS-1$
            params[1] = ""; //$NON-NLS-1$
        }
        final String name = Uml2ModelGeneratorPlugin.Util.getString("Uml2RelationalRelationTrackerImpl.Generated_from_UMLmetaclass_EntityName",params); //$NON-NLS-1$
        relationship.setName(name);
    }

    /**
     * @see com.metamatrix.modeler.modelgenerator.uml2.processor.RelationTrackerImpl#doSetName(com.metamatrix.metamodels.relationship.Relationship, java.util.List, java.util.List)
     */
    @Override
    protected void doSetName(Relationship relationship, List inputs, List outputs) {
        super.doSetName(relationship, inputs, outputs);
    }

}
