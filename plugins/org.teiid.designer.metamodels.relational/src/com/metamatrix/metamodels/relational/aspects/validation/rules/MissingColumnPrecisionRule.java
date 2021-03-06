/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.relational.aspects.validation.rules;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EObject;
import com.metamatrix.core.util.CoreArgCheck;
import com.metamatrix.metamodels.relational.Column;
import com.metamatrix.metamodels.relational.RelationalPlugin;
import com.metamatrix.modeler.core.ValidationPreferences;
import com.metamatrix.modeler.core.types.DatatypeManager;
import com.metamatrix.modeler.core.validation.ObjectValidationRule;
import com.metamatrix.modeler.core.validation.ValidationContext;
import com.metamatrix.modeler.core.validation.ValidationProblem;
import com.metamatrix.modeler.core.validation.ValidationResult;
import com.metamatrix.modeler.internal.core.validation.ValidationProblemImpl;
import com.metamatrix.modeler.internal.core.validation.ValidationResultImpl;

/**
 * MissingColumnPrecisionRule
 */
public class MissingColumnPrecisionRule implements
                                       ObjectValidationRule {

    /*
     * @see com.metamatrix.modeler.core.validation.ObjectValidationRule#validate(org.eclipse.emf.ecore.EObject,
     *      com.metamatrix.modeler.core.validation.ValidationContext)
     */
    public void validate(EObject eObject,
                         ValidationContext context) {
        CoreArgCheck.isInstanceOf(Column.class, eObject);

        final Column column = (Column)eObject;

        if (column.getPrecision() > 0) {
            return;
        }

        final int status = getPreferenceStatus(context);

        if (status == IStatus.OK) {
            return;
        }

        final EObject type = column.getType();
        final DatatypeManager dtMgr = context.getDatatypeManager();
        if (type != null) {
            if (dtMgr.isNumeric(type)) { 
                final ValidationResult result = new ValidationResultImpl(eObject);
                // create validation problem and add it to the result
                final Object[] params = new Object[] {};
                final String msg = RelationalPlugin.Util.getString("MissingColumnPrecisionRule.failure_message", params); //$NON-NLS-1$
                final ValidationProblem problem = new ValidationProblemImpl(0, status, msg);
                problem.setHasPreference(context.hasPreferences());
                result.addProblem(problem);
                context.addResult(result);
            }
        }
    }

    protected int getPreferenceStatus(ValidationContext context) {
        return context.getPreferenceStatus(ValidationPreferences.RELATIONAL_MISSING_COLUMN_PRECISION, IStatus.WARNING);
    }

}
