/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.ui.viewsupport;

import org.eclipse.emf.ecore.EObject;
import com.metamatrix.modeler.core.ModelerCore;

/**
 * ModelObjectPathLabelProvider is a specialization of ModelObjectPathLabelProvider
 * that adds the full path to the object's text label.
 */
public class ModelObjectPathLabelProvider extends ModelObjectLabelProvider {

    private static final String SEP_PFX = Util.getString("ModelObjectPathLabelProvider.prefixPath");  //$NON-NLS-1$
    private static final String SEP_SFX = Util.getString("ModelObjectPathLabelProvider.suffixPath");  //$NON-NLS-1$

    /**
     * Construct an instance of ModelObjectPathLabelProvider.
     */
    public ModelObjectPathLabelProvider() {
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ILabelProvider#getText(java.lang.Object)
     */
    @Override
    public String getText(Object element) {
        if ( element instanceof EObject ) {
            // Get label for object
            String name = super.getText(element);
            // Strip type info
            final int ndx = name.indexOf(' ');
            if (ndx >= 0) {
                name = name.substring(0, ndx);
            }
            // Return stripped label + path
            return name + SEP_PFX + ModelerCore.getModelEditor().getFullPathToParent((EObject)element) + SEP_SFX;
        }
        return super.getText(element);
    }

}
