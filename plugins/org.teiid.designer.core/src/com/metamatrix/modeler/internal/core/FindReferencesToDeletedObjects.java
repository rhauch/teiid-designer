/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.core;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import com.metamatrix.core.util.CoreArgCheck;
import com.metamatrix.modeler.core.util.ModelVisitor;

/**
 * This class finds (bi-directional) references to the deleted object and it's contents.
 * 
 * @see com.metamatrix.modeler.internal.core.FindRelatedObjectsToDeleted
 * @see com.metamatrix.modeler.core.ModelEditor#delete(EObject)
 */
public class FindReferencesToDeletedObjects implements ModelVisitor {

    private final Collection allDeletedObjects;
    private final Collection referencesToDeleted;

    /**
     * Construct an instance of FindReferencesToDeletedObjects.
     */
    public FindReferencesToDeletedObjects( final Collection allDeletedObjects ) {
        CoreArgCheck.isNotNull(allDeletedObjects);
        this.allDeletedObjects = new HashSet(allDeletedObjects);
        this.referencesToDeleted = new HashSet();
    }

    /**
     * Visit a deleted object and record references from non-deleted objects to it.
     * 
     * @param object one of the objects being deleted
     * @see com.metamatrix.modeler.core.util.ModelVisitor#visit(org.eclipse.emf.ecore.EObject)
     */
    public boolean visit( final EObject object ) {
        // Find all references ...
        final EClass eclass = object.eClass();
        final Collection allRefs = eclass.getEAllReferences();
        for (final Iterator iter = allRefs.iterator(); iter.hasNext();) {
            final EReference reference = (EReference)iter.next();
            final EReference opposite = reference.getEOpposite();
            // Process only non-containment references ...
            if (!reference.isContainment() && opposite != null && !opposite.isContainment()) {
                if (reference.isMany()) {
                    final Collection values = (Collection)object.eGet(reference);
                    for (final Iterator valueIter = values.iterator(); valueIter.hasNext();) {
                        final Object value = valueIter.next();
                        // If this value is in the list of deleted objects
                        if (this.allDeletedObjects.contains(value) && !this.referencesToDeleted.contains(value)) {
                            this.referencesToDeleted.add(value);
                        }
                    }
                } else {
                    final Object value = object.eGet(reference);
                    // If this value is in the list of deleted objects
                    if (this.allDeletedObjects.contains(value) && !this.referencesToDeleted.contains(value)) {
                        this.referencesToDeleted.add(value);
                    }
                }
            }

        }
        return true;
    }

    /**
     * @see com.metamatrix.modeler.core.util.ModelVisitor#visit(org.eclipse.emf.ecore.resource.Resource)
     */
    public boolean visit( final Resource resource ) {
        return resource != null;
    }

    /**
     * @return
     */
    public Collection getReferencesToDeletedObjects() {
        return this.referencesToDeleted;
    }

}
