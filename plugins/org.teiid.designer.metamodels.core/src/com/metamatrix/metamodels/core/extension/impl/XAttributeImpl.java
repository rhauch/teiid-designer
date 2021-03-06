/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.core.extension.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EAttributeImpl;
import com.metamatrix.metamodels.core.extension.ExtensionPackage;
import com.metamatrix.metamodels.core.extension.XAttribute;

/**
 * <!-- begin-user-doc --> An implementation of the model object '<em><b>XAttribute</b></em>'. <!-- end-user-doc -->
 * <p>
 * </p>
 * 
 * @generated
 */
public class XAttributeImpl extends EAttributeImpl implements XAttribute {

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected XAttributeImpl() {
        super();
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EClass eStaticClass() {
        return ExtensionPackage.Literals.XATTRIBUTE;
    }

    /**
     * @see org.eclipse.emf.ecore.ETypedElement#setEType(org.eclipse.emf.ecore.EClassifier)
     * @since 4.2
     */
    @Override
    public void setEType( EClassifier theValue ) {
        // let the delegate be regenerated since the delegate caches the type
        this.settingDelegate = null;

        // let the factory get reassigned each time the type changes. if you don't the default value
        // doesn't get regenerated
        this.defaultValueFactory = null;

        // set the type
        super.setEType(theValue);
    }

    /**
     * @see org.eclipse.emf.ecore.EStructuralFeature#getDefaultValue()
     * @since 4.2
     * @generated NOT
     */
    @Override
    public Object getDefaultValue() {
        Object result = null;
        final EDataType type = (EDataType)getEType();

        // make sure the type has been set
        if (type != null) {
            final EPackage ePackage = type.getEPackage();
            final EFactory factory = ePackage.getEFactoryInstance();

            try {
                // this method will throw an exception if the value can't be converted to the proper type
                factory.createFromString(type, getDefaultValueLiteral());

                // if value literal is valid just call super
                result = super.getDefaultValue();
            } catch (RuntimeException theException) {
                // just return null if the default value literal can't be converted
            }
        }

        return result;
    }

} // XAttributeImpl
