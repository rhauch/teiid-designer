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
import com.metamatrix.modeler.core.types.DatatypeConstants;
import com.metamatrix.modeler.core.types.DatatypeManager;
import com.metamatrix.modeler.core.validation.ObjectValidationRule;
import com.metamatrix.modeler.core.validation.ValidationContext;
import com.metamatrix.modeler.core.validation.ValidationProblem;
import com.metamatrix.modeler.core.validation.ValidationResult;
import com.metamatrix.modeler.internal.core.validation.ValidationProblemImpl;
import com.metamatrix.modeler.internal.core.validation.ValidationResultImpl;

/**
 * ColumnIntegerDatatypeRule
 */
public class ColumnIntegerDatatypeRule implements ObjectValidationRule {
    
    /*
     * @see com.metamatrix.modeler.core.validation.ObjectValidationRule#validate(org.eclipse.emf.ecore.EObject, com.metamatrix.modeler.core.validation.ValidationContext)
     */
    public void validate(EObject eObject, ValidationContext context) {
        CoreArgCheck.isInstanceOf(Column.class, eObject);

        Column column = (Column) eObject;

        EObject dataType = column.getType();
        if(dataType == null) {
            return;    
        }
        final DatatypeManager dtMgr = context.getDatatypeManager();
        boolean isBuiltInType = dtMgr.isBuiltInDatatype(dataType);
        String typeName = dtMgr.getName(dataType);
        if(!isBuiltInType ||typeName == null || !typeName.equals(DatatypeConstants.BuiltInNames.INTEGER)) {
            return;
        }
        
        int severity = IStatus.WARNING;

        if (context.hasPreferences()) {
            severity = context.getPreferenceStatus(ValidationPreferences.RELATIONAL_COLUMN_INTEGER_TYPE, severity);

            if (severity == IStatus.OK) {
                return;
            }
        }

        // problem exists
        ValidationResult result = new ValidationResultImpl(eObject);
        // create validation problem and add it to the result
        final String msg = RelationalPlugin.Util.getString("ColumnIntegerDatatypeRule.Integer_datatype_would_result_in_a_bigInteger_runtimetype._1"); //$NON-NLS-1$
        ValidationProblem problem  = new ValidationProblemImpl(0, severity ,msg);
        result.addProblem(problem);
        context.addResult(result);
    }

}
