/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.core.index;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import org.eclipse.emf.ecore.EObject;

/**
 * IndexingContext.
 */
public class IndexingContext {

	// map between a target and transformation to which it is a target
    private Map targetTransformMap;
    
	// map between a table marked for materialization to a collection of its materialized tables    
    private Map targetMaterializedTablesMap;
    
    // collection of either {@link org.eclipse.emf.ecore.resource.Resource}
    // or {@link org.eclipse.core.resources.IResource} instances defining
    // the context of the validation execution
    private Collection resourcesInContext;

	public Collection getMaterializedTables(final EObject target) {
		if(targetMaterializedTablesMap != null) {
			return (Collection) targetMaterializedTablesMap.get(target);		    
		}
		return null;
	}
	
	public void addMaterializedTables(final EObject target, final Collection tables) {
		if(targetMaterializedTablesMap == null) {
		    targetMaterializedTablesMap = new HashMap();
		}
		targetMaterializedTablesMap.put(target, tables);
	}
	
	public void addTargetTransform(final EObject target, final EObject transform) {
		if(targetTransformMap == null) {
			targetTransformMap = new HashMap();
		}
		if(target != null) {
		    targetTransformMap.put(target, transform);
		}
	}
	
	public boolean hasTransformation(final EObject target) {
	    if(targetTransformMap != null) {
	        return this.targetTransformMap.containsKey(target);
	    }
	    return false;
	}
	
    /** 
     * Returns the collection of either {@link org.eclipse.emf.ecore.resource.Resource} 
     * or {@link org.eclipse.core.resources.IResource} instances defining the context of 
     * the validation execution
     * <p>
     * If the {@link #getResourceContainer() resource container} is used, all of the
     * {@link #getResourcesInContext() resources in the context} should exist in the same container.
     * </p>
     * @return Returns the resourcesInContext.
     * @since 4.2
     */
    public Collection getResourcesInContext() {
        if (this.resourcesInContext == null) {
            this.resourcesInContext = new HashSet();
        }
        return this.resourcesInContext;
    }

    /** 
     * Sets the collection of either {@link org.eclipse.emf.ecore.resource.Resource} 
     * or {@link org.eclipse.core.resources.IResource} instances defining the context 
     * of the validation execution
     * <p>
     * If the {@link #getResourceContainer() resource container} is used, all of the
     * {@link #getResourcesInContext() resources in the context} should exist in the same container.
     * </p>
     * @param resourcesInContext The resourcesInContext to set.
     * @since 4.2
     */
    public void setResourcesInContext(final Collection eResourcesInContext) {
        this.resourcesInContext = eResourcesInContext;
    }
    
	/**
	 * Clear all the results on this context
	 */
	public void clearState() {
		if(this.targetTransformMap != null) {
			this.targetTransformMap.clear();
		}
		if(this.resourcesInContext != null) {
		    this.resourcesInContext .clear();
		}
		if(this.targetMaterializedTablesMap != null) {
		    this.targetMaterializedTablesMap.clear();
		}
	}	

}
