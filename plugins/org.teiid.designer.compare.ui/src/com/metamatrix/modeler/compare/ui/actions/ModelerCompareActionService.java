/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.compare.ui.actions;

import org.eclipse.jface.action.IAction;
import org.eclipse.ui.IWorkbenchPage;
import com.metamatrix.modeler.compare.ui.UiConstants;
import com.metamatrix.modeler.compare.ui.UiPlugin;
import com.metamatrix.ui.actions.AbstractActionService;

/**
 * The <code>ModelerCompareActionService</code> class is the Modeler Plugin's action service. It is responsible for
 * managing all actions for this plugin.
 */
public final class ModelerCompareActionService extends AbstractActionService implements UiConstants {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructs a <code>ModelerXmlActionService</code> associated with the given <code>IWorkbenchWindow</code>.
     * @param theWindow the associated workbench window
     */
    public ModelerCompareActionService(IWorkbenchPage page) {
        super(UiPlugin.getDefault(), page);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /* (non-Javadoc)
     * @see com.metamatrix.ui.actions.AbstractActionService#getDefaultAction(java.lang.String)
     */
    public IAction getDefaultAction(String theActionId) {
        return null;
    }

    
}
