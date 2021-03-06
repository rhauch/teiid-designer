/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.transformation.aspects.validation;

import org.eclipse.emf.ecore.EObject;
import com.metamatrix.core.util.CoreArgCheck;
import com.metamatrix.metamodels.transformation.MappingClassColumn;
import com.metamatrix.metamodels.transformation.TransformationPackage;
import com.metamatrix.modeler.core.metamodel.aspect.MetamodelEntity;
import com.metamatrix.modeler.core.validation.ValidationContext;
import com.metamatrix.modeler.core.validation.ValidationRule;
import com.metamatrix.modeler.core.validation.ValidationRuleSet;
import com.metamatrix.modeler.core.validation.rules.StringLengthRule;
import com.metamatrix.modeler.core.validation.rules.StringNameRule;

/**
 * @
 */
public class MappingClassColumnAspect extends TransformationAspect {

	public static final ValidationRule NAME_RULE = new StringNameRule(TransformationPackage.MAPPING_CLASS_COLUMN__NAME);
	public static final ValidationRule LENGTH_RULE = new StringLengthRule(TransformationPackage.MAPPING_CLASS_COLUMN__NAME);

	public MappingClassColumnAspect(final MetamodelEntity entity) {
		super(entity);
	}

	/**
	 * Get validation rules for MappingClassColumnAspect
	 */
	@Override
    public ValidationRuleSet getValidationRules() {
		addRule(NAME_RULE);
		addRule(LENGTH_RULE);
		return super.getValidationRules();
	}
	
	
    /** 
     * @see com.metamatrix.modeler.core.metamodel.aspect.ValidationAspect#shouldValidate(org.eclipse.emf.ecore.EObject)
     * @since 4.2
     */
    @Override
    public boolean shouldValidate(final EObject eObject, final ValidationContext context) {
        CoreArgCheck.isInstanceOf(MappingClassColumn.class, eObject);
        // Defect 23839 - this method previously called it's parent MappingClass's validation aspect shouldValidate() method.
        // This method is not quick. So we needed a way to NOT call this method for ALL Mapping Class Columns.
        // Since the Mapping Class is guaranteed to be check for shouldValidate() first it will end up in the context's shouldIgnore()
        // list first AND all it's columns will be recursively added to this list too.
        // SO.... all we need to do is check if the columns themselves are in this ignore list instead.  Much faster
     
        return (!context.shouldIgnore(eObject));
    }
}
