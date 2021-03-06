/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.xsd.ui.editor;

import org.eclipse.xsd.XSDConstrainingFacet;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import com.metamatrix.modeler.internal.ui.forms.FormUtil;


public class FacetValue implements Cloneable {
    public boolean isFixedLocal;
    public Object  value;
    public Object  defaultValue;
    public String  description;
    public XSDSimpleTypeDefinition type;
    public XSDConstrainingFacet    facet;

    //
    // Constructors:
    //
    public FacetValue() {
    }

    public FacetValue(FacetValue fv) {
        copyValuesOf(fv);
    }

    //
    // Setup Methods:
    //
    public void copyValuesOf(FacetValue fv) {
        if (fv != null) {
            // copy contents:
            isFixedLocal = fv.isFixedLocal;
            type         = fv.type;
            value        = fv.value;
            defaultValue = fv.defaultValue;
            description  = fv.description;
            facet        = fv.facet;
        } else {
            clear();
        } // endif
    }

    public void clear() {
        isFixedLocal = false;
        type         = null;
        value        = null;
        defaultValue = null;
        description  = null;
        facet        = null;
    }
    
    public FacetValue cloneValue() {
        try {
            return (FacetValue) super.clone();
        } catch (CloneNotSupportedException e) {
            // should never happen, since we implement cloneable
            return new FacetValue(this);
        } // endtry
    }

    //
    // Data methods:
    //
    public boolean isDefault() {
        return FormUtil.safeEquals(description, null, true) // desc null
            && (facet == null                                  // no facet defined yet -- assumes someone is setting the facet before checking this
             || isFixedByParent()                              // parent has fixed this
             || FormUtil.safeEquals(value, defaultValue));  // value same as default
    }

    public void resetToDefault() {
        value       = defaultValue;
        description = null;
    }

    public boolean isInherited() {
        return facet != null
            && facet.getContainer() != type;
    }

    public boolean isFixedByParent() {
        return FacetHelper.isFixed(facet)
            && isInherited();
    }

    //
    // Overrides:
    //
    @Override
    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        } // endif

        if (obj instanceof FacetValue) {
            FacetValue fv = (FacetValue) obj;
            // TODO update
            return //isDefault == fv.isDefault &&
                   isFixedLocal  == fv.isFixedLocal
                && isInherited() == fv.isInherited()
//                && SimpleComponentSet.safeEquals(currentFacetHolder, fv.currentFacetHolder)
                && FormUtil.safeEquals(value, fv.value)
                && FormUtil.safeEquals(description, fv.description);
        } // endif
        
        return false;
    }

    @Override
    public String toString() {
        return "FacetValue: isDefault="+isDefault() //$NON-NLS-1$
              +"; isFixedLocal="+isFixedLocal //$NON-NLS-1$
              +"; isInherited="+isInherited() //$NON-NLS-1$
//              +"; currentHolder="+currentFacetHolder //$NON-NLS-1$
              +"; value="+value //$NON-NLS-1$
              +"; desc="+description; //$NON-NLS-1$
    }

}
