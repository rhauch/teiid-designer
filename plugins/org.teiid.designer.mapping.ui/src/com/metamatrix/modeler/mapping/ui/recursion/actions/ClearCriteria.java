/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.mapping.ui.recursion.actions;

import com.metamatrix.modeler.internal.ui.viewsupport.ModelObjectUtilities;
import com.metamatrix.modeler.mapping.ui.PluginConstants;
import com.metamatrix.modeler.mapping.ui.UiPlugin;
import com.metamatrix.modeler.mapping.ui.recursion.RecursionPanel;
import com.metamatrix.ui.actions.AbstractAction;

/**
 * The <code>ClearCriteria</code> class launches the Criteria Builder
 * @since 4.0
 */
public class ClearCriteria extends AbstractAction {

    // =================================================================
    // FIELDS
    // =================================================================
    private RecursionPanel pnlRecursionPanel;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    public ClearCriteria( RecursionPanel pnlRecursionPanel ) {
        super( UiPlugin.getDefault() );
   
        setImageDescriptor( UiPlugin.getDefault().getImageDescriptor( PluginConstants.Images.CLEAR_ICON ) );
                
        this.pnlRecursionPanel = pnlRecursionPanel;

        // default to disabled:
        setEnabled( false );
    }
    
    // =================================================================
    // METHODS
    // =================================================================
    
    @Override
    protected void doRun() {
        // Tell ChoicePanel to show the Criteria builder
//        System.out.println("[ClearCriteria.doRun]"); //$NON-NLS-1$
        pnlRecursionPanel.clearCriteria();
    }
    
    
    /**
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
     * @since 4.0
     */
    public void selectionChanged() {
//        System.out.println("[ClearCriteria.selectionChanged]"); //$NON-NLS-1$
        if ( ModelObjectUtilities.isReadOnly( pnlRecursionPanel.getRecursionObject().getMappingClass() ) ) {
            setEnabled( false );
            return;
        }

        if ( pnlRecursionPanel.canClearCriteria() ) {         
//            System.out.println("[ClearCriteria.selectionChanged] About to setEnabled( true )"); //$NON-NLS-1$
            setEnabled( true );            
        } else {
//            System.out.println("[ClearCriteria.selectionChanged] About to setEnabled( false )"); //$NON-NLS-1$
            setEnabled( false );
        }
    }    
    
}
