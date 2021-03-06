/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.transformation;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Input Set</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link com.metamatrix.metamodels.transformation.InputSet#getMappingClass <em>Mapping Class</em>}</li>
 * <li>{@link com.metamatrix.metamodels.transformation.InputSet#getInputParameters <em>Input Parameters</em>}</li>
 * </ul>
 * </p>
 * 
 * @see com.metamatrix.metamodels.transformation.TransformationPackage#getInputSet()
 * @model
 * @generated
 */
public interface InputSet extends EObject {

    /**
     * Returns the value of the '<em><b>Mapping Class</b></em>' container reference. It is bidirectional and its opposite is '
     * {@link com.metamatrix.metamodels.transformation.MappingClass#getInputSet <em>Input Set</em>}'. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Mapping Class</em>' container reference isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Mapping Class</em>' container reference.
     * @see #setMappingClass(MappingClass)
     * @see com.metamatrix.metamodels.transformation.TransformationPackage#getInputSet_MappingClass()
     * @see com.metamatrix.metamodels.transformation.MappingClass#getInputSet
     * @model opposite="inputSet" required="true"
     * @generated
     */
    MappingClass getMappingClass();

    /**
     * Sets the value of the '{@link com.metamatrix.metamodels.transformation.InputSet#getMappingClass <em>Mapping Class</em>}'
     * container reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Mapping Class</em>' container reference.
     * @see #getMappingClass()
     * @generated
     */
    void setMappingClass( MappingClass value );

    /**
     * Returns the value of the '<em><b>Input Parameters</b></em>' containment reference list. The list contents are of type
     * {@link com.metamatrix.metamodels.transformation.InputParameter}. It is bidirectional and its opposite is '
     * {@link com.metamatrix.metamodels.transformation.InputParameter#getInputSet <em>Input Set</em>}'. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Input Parameters</em>' containment reference list isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Input Parameters</em>' containment reference list.
     * @see com.metamatrix.metamodels.transformation.TransformationPackage#getInputSet_InputParameters()
     * @see com.metamatrix.metamodels.transformation.InputParameter#getInputSet
     * @model type="com.metamatrix.metamodels.transformation.InputParameter" opposite="inputSet" containment="true"
     * @generated
     */
    EList getInputParameters();

} // InputSet
