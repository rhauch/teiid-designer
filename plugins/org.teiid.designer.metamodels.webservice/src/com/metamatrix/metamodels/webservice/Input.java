/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.webservice;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Input</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link com.metamatrix.metamodels.webservice.Input#getOperation <em>Operation</em>}</li>
 * </ul>
 * </p>
 * 
 * @see com.metamatrix.metamodels.webservice.WebServicePackage#getInput()
 * @model
 * @generated
 */
public interface Input extends Message {

    /**
     * Returns the value of the '<em><b>Operation</b></em>' container reference. It is bidirectional and its opposite is '
     * {@link com.metamatrix.metamodels.webservice.Operation#getInput <em>Input</em>}'. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Operation</em>' container reference isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Operation</em>' container reference.
     * @see #setOperation(Operation)
     * @see com.metamatrix.metamodels.webservice.WebServicePackage#getInput_Operation()
     * @see com.metamatrix.metamodels.webservice.Operation#getInput
     * @model opposite="input" required="true"
     * @generated
     */
    Operation getOperation();

    /**
     * Sets the value of the '{@link com.metamatrix.metamodels.webservice.Input#getOperation <em>Operation</em>}' container
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Operation</em>' container reference.
     * @see #getOperation()
     * @generated
     */
    void setOperation( Operation value );

} // Input
