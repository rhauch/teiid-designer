/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.vdb.ui.properties;

import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.eclipse.ui.views.properties.PropertySheetPage;
import org.teiid.designer.vdb.VdbModelEntry;

/**
 * @since 4.2
 */
public class ModelEntryPropertySourceProvider implements IPropertySourceProvider {

    private PropertySheetPage propertySheetPage;

    /**
     * Obtain a PropertySheetPage for displaying Model Objects.
     * 
     * @since 4.2
     */
    public PropertySheetPage getPropertySheetPage() {
        if (propertySheetPage == null) {
            propertySheetPage = new PropertySheetPage();
            propertySheetPage.setPropertySourceProvider(this);
        }

        return propertySheetPage;
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySourceProvider#getPropertySource(java.lang.Object)
     * @since 4.2
     */
    public IPropertySource getPropertySource( final Object object ) {
        if (object instanceof VdbModelEntry) return new ModelEntryPropertySource((VdbModelEntry)object);
        return null;
    }

}