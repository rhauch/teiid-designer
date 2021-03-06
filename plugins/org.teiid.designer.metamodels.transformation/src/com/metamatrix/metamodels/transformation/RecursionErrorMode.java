/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.transformation;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.eclipse.emf.common.util.AbstractEnumerator;

/**
 * <!-- begin-user-doc --> A representation of the literals of the enumeration '<em><b>Recursion Error Mode</b></em>', and utility
 * methods for working with them. <!-- end-user-doc -->
 * 
 * @see com.metamatrix.metamodels.transformation.TransformationPackage#getRecursionErrorMode()
 * @model
 * @generated
 */
public final class RecursionErrorMode extends AbstractEnumerator {

    /**
     * The '<em><b>THROW</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #THROW_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int THROW = 0;

    /**
     * The '<em><b>RECORD</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #RECORD_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int RECORD = 1;

    /**
     * The '<em><b>DISCARD</b></em>' literal value. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @see #DISCARD_LITERAL
     * @model
     * @generated
     * @ordered
     */
    public static final int DISCARD = 2;

    /**
     * The '<em><b>THROW</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>THROW</b></em>' literal object isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #THROW
     * @generated
     * @ordered
     */
    public static final RecursionErrorMode THROW_LITERAL = new RecursionErrorMode(THROW, "THROW"); //$NON-NLS-1$

    /**
     * The '<em><b>RECORD</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>RECORD</b></em>' literal object isn't clear, there really should be more of a description here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #RECORD
     * @generated
     * @ordered
     */
    public static final RecursionErrorMode RECORD_LITERAL = new RecursionErrorMode(RECORD, "RECORD"); //$NON-NLS-1$

    /**
     * The '<em><b>DISCARD</b></em>' literal object. <!-- begin-user-doc -->
     * <p>
     * If the meaning of '<em><b>DISCARD</b></em>' literal object isn't clear, there really should be more of a description
     * here...
     * </p>
     * <!-- end-user-doc -->
     * 
     * @see #DISCARD
     * @generated
     * @ordered
     */
    public static final RecursionErrorMode DISCARD_LITERAL = new RecursionErrorMode(DISCARD, "DISCARD"); //$NON-NLS-1$

    /**
     * An array of all the '<em><b>Recursion Error Mode</b></em>' enumerators. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private static final RecursionErrorMode[] VALUES_ARRAY = new RecursionErrorMode[] {THROW_LITERAL, RECORD_LITERAL,
        DISCARD_LITERAL,};

    /**
     * A public read-only list of all the '<em><b>Recursion Error Mode</b></em>' enumerators. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static final List VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

    /**
     * Returns the '<em><b>Recursion Error Mode</b></em>' literal with the specified name. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static RecursionErrorMode get( String name ) {
        for (int i = 0; i < VALUES_ARRAY.length; ++i) {
            RecursionErrorMode result = VALUES_ARRAY[i];
            if (result.toString().equals(name)) {
                return result;
            }
        }
        return null;
    }

    /**
     * Returns the '<em><b>Recursion Error Mode</b></em>' literal with the specified value. <!-- begin-user-doc --> <!--
     * end-user-doc -->
     * 
     * @generated
     */
    public static RecursionErrorMode get( int value ) { // NO_UCD
        switch (value) {
            case THROW:
                return THROW_LITERAL;
            case RECORD:
                return RECORD_LITERAL;
            case DISCARD:
                return DISCARD_LITERAL;
        }
        return null;
    }

    /**
     * Only this class can construct instances. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    private RecursionErrorMode( int value,
                                String name ) {
        super(value, name);
    }

} // RecursionErrorMode
