/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.vdb.edit.manifest;

import org.eclipse.emf.ecore.EObject;

/**
 * <!-- begin-user-doc --> A representation of the model object '<em><b>Model Source Property</b></em>'. <!-- end-user-doc -->
 * <p>
 * The following features are supported:
 * <ul>
 * <li>{@link com.metamatrix.vdb.edit.manifest.ModelSourceProperty#getName <em>Name</em>}</li>
 * <li>{@link com.metamatrix.vdb.edit.manifest.ModelSourceProperty#getValue <em>Value</em>}</li>
 * <li>{@link com.metamatrix.vdb.edit.manifest.ModelSourceProperty#getSource <em>Source</em>}</li>
 * </ul>
 * </p>
 * 
 * @see com.metamatrix.vdb.edit.manifest.ManifestPackage#getModelSourceProperty()
 * @model
 * @generated
 */
public interface ModelSourceProperty extends EObject {

    /**
     * Returns the value of the '<em><b>Name</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Name</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Name</em>' attribute.
     * @see #setName(String)
     * @see com.metamatrix.vdb.edit.manifest.ManifestPackage#getModelSourceProperty_Name()
     * @model
     * @generated
     */
    String getName();

    /**
     * Sets the value of the '{@link com.metamatrix.vdb.edit.manifest.ModelSourceProperty#getName <em>Name</em>}' attribute. <!--
     * begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Name</em>' attribute.
     * @see #getName()
     * @generated
     */
    void setName( String value );

    /**
     * Returns the value of the '<em><b>Value</b></em>' attribute. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Value</em>' attribute isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Value</em>' attribute.
     * @see #setValue(String)
     * @see com.metamatrix.vdb.edit.manifest.ManifestPackage#getModelSourceProperty_Value()
     * @model
     * @generated
     */
    String getValue();

    /**
     * Sets the value of the '{@link com.metamatrix.vdb.edit.manifest.ModelSourceProperty#getValue <em>Value</em>}' attribute.
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Value</em>' attribute.
     * @see #getValue()
     * @generated
     */
    void setValue( String value );

    /**
     * Returns the value of the '<em><b>Source</b></em>' container reference. It is bidirectional and its opposite is '
     * {@link com.metamatrix.vdb.edit.manifest.ModelSource#getProperties <em>Properties</em>}'. <!-- begin-user-doc -->
     * <p>
     * If the meaning of the '<em>Source</em>' container reference isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @return the value of the '<em>Source</em>' container reference.
     * @see #setSource(ModelSource)
     * @see com.metamatrix.vdb.edit.manifest.ManifestPackage#getModelSourceProperty_Source()
     * @see com.metamatrix.vdb.edit.manifest.ModelSource#getProperties
     * @model opposite="properties" required="true"
     * @generated
     */
    ModelSource getSource();

    /**
     * Sets the value of the '{@link com.metamatrix.vdb.edit.manifest.ModelSourceProperty#getSource <em>Source</em>}' container
     * reference. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @param value the new value of the '<em>Source</em>' container reference.
     * @see #getSource()
     * @generated
     */
    void setSource( ModelSource value );

} // ModelSourceProperty
