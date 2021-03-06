/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.wsdl.soap;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;
import com.metamatrix.metamodels.wsdl.BindingFault;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Fault</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link com.metamatrix.metamodels.wsdl.soap.SoapFault#getBindingFault <em>Binding Fault</em>}</li>
 * <li>{@link com.metamatrix.metamodels.wsdl.soap.SoapFault#getUse <em>Use</em>}</li>
 * <li>{@link com.metamatrix.metamodels.wsdl.soap.SoapFault#getNamespace <em>Namespace</em>}</li>
 * <li>{@link com.metamatrix.metamodels.wsdl.soap.SoapFault#getEncodingStyles <em>Encoding Styles</em>}</li>
 * </ul>
 * </p>
 * 
 * @see com.metamatrix.metamodels.wsdl.soap.SoapPackage#getSoapFault()
 * @model
 * @generated
 */
public interface SoapFault extends EObject {

    /**
     * Returns the value of the '<em><b>Use</b></em>' attribute. The literals are from the enumeration
     * {@link com.metamatrix.metamodels.wsdl.soap.SoapUseType}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Use</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Use</em>' attribute.
     * @see com.metamatrix.metamodels.wsdl.soap.SoapUseType
     * @see #setUse(SoapUseType)
     * @see com.metamatrix.metamodels.wsdl.soap.SoapPackage#getSoapFault_Use()
     * @model
     * @generated
     */
    SoapUseType getUse();

    /**
     * Sets the value of the '{@link com.metamatrix.metamodels.wsdl.soap.SoapFault#getUse <em>Use</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Use</em>' attribute.
     * @see com.metamatrix.metamodels.wsdl.soap.SoapUseType
     * @see #getUse()
     * @generated
     */
    void setUse( SoapUseType value );

    /**
     * Returns the value of the '<em><b>Namespace</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Namespace</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Namespace</em>' attribute.
     * @see #setNamespace(String)
     * @see com.metamatrix.metamodels.wsdl.soap.SoapPackage#getSoapFault_Namespace()
     * @model
     * @generated
     */
    String getNamespace();

    /**
     * Sets the value of the '{@link com.metamatrix.metamodels.wsdl.soap.SoapFault#getNamespace <em>Namespace</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Namespace</em>' attribute.
     * @see #getNamespace()
     * @generated
     */
    void setNamespace( String value );

    /**
     * Returns the value of the '<em><b>Encoding Styles</b></em>' attribute list. The list contents are of type
     * {@link java.lang.String}. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Encoding Styles</em>' attribute list isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Encoding Styles</em>' attribute list.
     * @see com.metamatrix.metamodels.wsdl.soap.SoapPackage#getSoapFault_EncodingStyles()
     * @model type="java.lang.String"
     * @generated
     */
    EList getEncodingStyles();

    /**
     * Returns the value of the '<em><b>Binding Fault</b></em>' container reference. It is bidirectional and its opposite is '
     * {@link com.metamatrix.metamodels.wsdl.BindingFault#getSoapFault <em>Soap Fault</em>}'. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Binding Fault</em>' container reference isn't clear, there really should be more of a
     * description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Binding Fault</em>' container reference.
     * @see #setBindingFault(BindingFault)
     * @see com.metamatrix.metamodels.wsdl.soap.SoapPackage#getSoapFault_BindingFault()
     * @see com.metamatrix.metamodels.wsdl.BindingFault#getSoapFault
     * @model opposite="soapFault"
     * @generated
     */
    BindingFault getBindingFault();

    /**
     * Sets the value of the '{@link com.metamatrix.metamodels.wsdl.soap.SoapFault#getBindingFault <em>Binding Fault</em>}'
     * container reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Binding Fault</em>' container reference.
     * @see #getBindingFault()
     * @generated
     */
    void setBindingFault( BindingFault value );

} // SoapFault
