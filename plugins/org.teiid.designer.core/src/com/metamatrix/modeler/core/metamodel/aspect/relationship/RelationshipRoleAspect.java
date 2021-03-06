/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.core.metamodel.aspect.relationship;

import org.eclipse.emf.ecore.EObject;

/**
 * RelationshipRoleAspect
 */
public interface RelationshipRoleAspect extends RelationshipMetamodelAspect {

	/**
	 * Get the name of the metamodel entity.
	 * @param eObject The <code>EObject</code> for which name is obtained
	 * @return name of the metamodel entity.
	 */
	String getName(EObject eObject);

	/**
	 * Get stereotype on the role, this may be null.
	 * @param eObject The <code>EObject</code> for which stereotype is obtained 
	 * @return roles stereotype if any.
	 */
	String getStereoType(EObject eObject);

	/**
	 * Check if the relationshiprole is ordered
	 * @param eObject The <code>EObject</code> whose orderliness is checked. 
	 * @return boolean indicating orderliness.
	 */
	boolean isOrdered(EObject eObject);

	/**
	 * Check if the relationshiprole is unique
	 * @param eObject The <code>EObject</code> whose uniqueness is checked. 
	 * @return boolean indicating uniqueness.
	 */
	boolean isUnique(EObject eObject);

	/**
	 * Check if the relationshiprole is naviable
	 * @param eObject The <code>EObject</code> whose navigability is checked. 
	 * @return boolean indicating navigability.
	 */
	boolean isNavigable(EObject eObject);

	/**
	 * Get lowerbound for this role.
	 * @param eObject The <code>EObject</code> for which role lowerbound is obtained 
	 * @return lowerbound value of a relationshiprole.
	 */
	int getLowerBound(EObject eObject);

	/**
	 * Get upperbound for this role.
	 * @param eObject The <code>EObject</code> for which role upperbound is obtained 
	 * @return upperbound value of a relationshiprole.
	 */
	int getUpperBound(EObject eObject);

	/**
	 * Check if the relationshiprole is a source role
	 * @param eObject The <code>EObject</code> is checked if it is a source role. 
	 * @return boolean indicating if the role is a source role.
	 */
	boolean isSourceRole(EObject eObject);
	
	/**
	 * Check if the relationshiprole is a source role
	 * @param eObject The <code>EObject</code> is checked if it is a source role. 
	 * @return boolean indicating if the role is a source role.
	 */
	boolean isTargetRole(EObject eObject);

	/**
	 * Get opposite role for this role.
	 * @param eObject The <code>EObject</code> for which opposite role 
	 * @return opposite role for this relationshiprole.
	 */
	Object getOppositeRole(EObject eObject);

	/**
	 * Get overridden role for this role.
	 * @param eObject The <code>EObject</code> for which overriddenrole 
	 * @return overridden role for this relationshiprole.
	 */
	Object getOverriddenRole(EObject eObject);

}
