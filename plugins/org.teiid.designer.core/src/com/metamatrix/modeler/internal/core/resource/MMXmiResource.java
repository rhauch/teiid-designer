/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.core.resource;

import java.util.List;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.teiid.core.id.ObjectID;
import com.metamatrix.metamodels.core.Annotation;
import com.metamatrix.metamodels.core.AnnotationContainer;
import com.metamatrix.metamodels.core.ModelAnnotation;
import com.metamatrix.metamodels.core.ModelType;

/**
 * MTK implementation of a Resource
 */
public interface MMXmiResource extends Resource.Internal {

    /**
     * Lookup / resolve a proxied EObject within a resource
     */
    EObject getEObject(Object key);

    /**
     * Obtain the list of namespace prefixes to URIs.
     * @return
     */
    List getNamespacePrefixToUris();

    /**
     * Obtain the model annotation object.
     * @return
     */
    ModelAnnotation getModelAnnotation();


    Annotation getAnnotation(EObject eobj);


    /**
     * Return the {@link com.metamatrix.metamodels.core.ModelType} for this
     * resource.  The model type information is obtained by either reading
     * the model file header information or by examining the model annotation
     * node depending on whether the resource has been loaded. If the type
     * cannot be determined then null will be returned.
     * @return
     */
    ModelType getModelType();

    /**
     * Return the description string defined for this resource or null
     * if one does not exist. The description information is obtained by either
     * reading the model file header information or by examining the model
     * annotation node depending on whether the resource has been loaded. If
     * no description exists then null will be returned.
     * @return
     */
    String getDescription();

    /**
     * Return the UUID defined for this resource or null
     * if one does not exist. The UUID is obtained by either
     * reading the model file header information or by examining the model
     * annotation node depending on whether the resource has been loaded.
     * @return
     */
    ObjectID getUuid();

    /**
     * Return the URI for the primary metamodel in this model
     * @return the URI; may be null if the primary metamodel is not registered
     */
    URI getPrimaryMetamodelUri();

    /**
     * Return the whether the resource is marked as visible
     * @return true if the resource is marked as visible
     */
    boolean isVisible();

    AnnotationContainer getAnnotationContainer(boolean createIfNeeded);


}
