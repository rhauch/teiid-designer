/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.core.metamodel.core.aspects.validation.rules;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import com.metamatrix.core.util.CoreArgCheck;
import com.metamatrix.metamodels.core.extension.XClass;
import com.metamatrix.metamodels.core.extension.XPackage;
import com.metamatrix.modeler.core.ModelerCore;
import com.metamatrix.modeler.core.validation.ObjectValidationRule;
import com.metamatrix.modeler.core.validation.ValidationContext;
import com.metamatrix.modeler.core.validation.ValidationProblem;
import com.metamatrix.modeler.core.validation.ValidationResult;
import com.metamatrix.modeler.internal.core.validation.ValidationProblemImpl;
import com.metamatrix.modeler.internal.core.validation.ValidationResultImpl;


/** 
 * @since 4.2
 */
public class XClassUniqueExtendedClassInXPackageRule implements ObjectValidationRule {

    /** 
     * @see com.metamatrix.modeler.core.validation.ObjectValidationRule#validate(org.eclipse.emf.ecore.EObject, com.metamatrix.modeler.core.validation.ValidationContext)
     * @since 4.2
     */
    public void validate(EObject eObject, ValidationContext context) {
        CoreArgCheck.isInstanceOf(XPackage.class, eObject);

        final XPackage xpackage = (XPackage) eObject;
        final Resource resource = xpackage.eResource();
        if (resource == null) {
            return;
        }
        
        // Get the XClasses and check for duplicate names ...
        final Map xclassesByMetaclasses = new HashMap();
        final List xclasses = xpackage.getEClassifiers();
        final Iterator iter = xclasses.iterator();
        while (iter.hasNext()) {
            final EClassifier classifier = (EClassifier)iter.next();
            if ( classifier instanceof XClass ) {
                final XClass xclass = (XClass)classifier;
                final EClass extendedMetaclass = xclass.getExtendedClass();
                if ( extendedMetaclass != null ) {
                    // Look for (or create & add) the list of XClasses for the metaclass
                    List xclassesForMetaclass = (List) xclassesByMetaclasses.get(extendedMetaclass);
                    if ( xclassesForMetaclass == null ) {
                        xclassesForMetaclass = new ArrayList(5);
                        xclassesByMetaclasses.put(extendedMetaclass,xclassesForMetaclass);
                    }
                    // Add this XClass to the list ...
                    xclassesForMetaclass.add(xclass);
                }
            }
        }
        
        // Check for duplicate metaclasses ...
        final Iterator extendedClassIter = xclassesByMetaclasses.entrySet().iterator();
        while (extendedClassIter.hasNext()) {
            final Map.Entry entry = (Map.Entry)extendedClassIter.next();
            final List xclassesWithSameMetaclass = (List)entry.getValue();
            final int numDuplicates = xclassesWithSameMetaclass.size();
            if ( numDuplicates > 1 ) {
                // Iterate and create error messages for each ...
                final Integer numOthers = new Integer(numDuplicates-1);
                final Iterator xclassIter = xclassesWithSameMetaclass.iterator();
                while (xclassIter.hasNext()) {
                    final XClass xclass = (XClass)xclassIter.next();
                    final ValidationResult result = new ValidationResultImpl(xclass);
                    final Object[] params = new Object[]{numOthers};
                    final String msg = ModelerCore.Util.getString("XClassUniqueExtendedClassInXPackageRule.ExtendedClassHasSameMetaclassAs_n_Others",params); //$NON-NLS-1$
                    final ValidationProblem problem  = new ValidationProblemImpl(0, IStatus.ERROR ,msg);
                    result.addProblem(problem);
                    context.addResult(result);
                }
            }
            
        }
    }

}
