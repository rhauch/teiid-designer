/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.core.metamodel.core.aspects.uml;

import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import com.metamatrix.modeler.core.ModelerCore;
import com.metamatrix.modeler.core.metamodel.aspect.AbstractMetamodelAspect;
import com.metamatrix.modeler.core.metamodel.aspect.uml.UmlDiagramAspect;

/**
 * RelationalEntityAspect
 */
public abstract class AbstractExtensionUmlAspect extends AbstractMetamodelAspect implements UmlDiagramAspect {
    public static final String ASPECT_ID = ModelerCore.EXTENSION_POINT.UML_DIAGRAM_ASPECT.ID;
    
    protected AbstractExtensionUmlAspect(){
        setID(ASPECT_ID);
    }
    
    public int getVisibility(Object eObject) {
        return VISIBILITY_PUBLIC;
    }

    /** 
     * @see com.metamatrix.modeler.core.metamodel.aspect.uml.UmlDiagramAspect#getImage(java.lang.Object)
     * @since 4.2
     */
    public Object getImage(Object eObject) {
        // get the adapter factory
        final AdapterFactory adapterFactory = ModelerCore.getMetamodelRegistry().getAdapterFactory();
		// lookup item provider for the eobjet
		final IItemLabelProvider provider = (IItemLabelProvider)adapterFactory.adapt(eObject,IItemLabelProvider.class);
		// look up image
		return provider.getImage(eObject);
    }

}
