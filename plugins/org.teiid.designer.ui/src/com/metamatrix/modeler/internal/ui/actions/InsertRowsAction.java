/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.ui.actions;

import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import com.metamatrix.modeler.internal.ui.editors.ModelEditor;
import com.metamatrix.modeler.internal.ui.table.InsertRowsDialog;
import com.metamatrix.modeler.internal.ui.table.ModelTableEditor;
import com.metamatrix.modeler.ui.UiPlugin;
import com.metamatrix.modeler.ui.actions.AbstractModelerAction;

/**
 * InsertRowsAction
 */
public class InsertRowsAction extends AbstractModelerAction {

    int iNewRows = 1;


    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////////////////////////

    public InsertRowsAction() {
        super(UiPlugin.getDefault());
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /* (non-Javadoc)
     * @see com.metamatrix.ui.actions.AbstractAction#doRun()
     */
    @Override
    protected void doRun() {
        Shell shell = UiPlugin.getDefault().getCurrentWorkbenchWindow().getShell();
                                        
        InsertRowsDialog dialog = new InsertRowsDialog( shell, getImageDescriptor().createImage() );
        
        dialog.open();

        if ( dialog.getReturnCode() == Window.OK) {
            iNewRows = dialog.getCount();

            getTableEditor().insertRows( iNewRows );
        }        
    }

    private ModelTableEditor getTableEditor() {
        ModelTableEditor result = null;
        IEditorPart editor = getActiveEditor();

        if (editor instanceof ModelEditor) {
            ModelEditor modelEditor = (ModelEditor)editor;
            IEditorPart subEditor = modelEditor.getActiveEditor();
        
            if (subEditor instanceof ModelTableEditor) {
                result = (ModelTableEditor)subEditor;
            }
        }
        
        return result;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.ui.actions.AbstractAction#selectionChanged(org.eclipse.ui.IWorkbenchPart, org.eclipse.jface.viewers.ISelection)
     */
    @Override
    public void selectionChanged(IWorkbenchPart thePart, ISelection theSelection) {
        super.selectionChanged(thePart, theSelection);
        setEnabledState(); 
    }
    
    /* (non-Javadoc)
     * @see com.metamatrix.ui.actions.AbstractAction#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    @Override
    public void selectionChanged(SelectionChangedEvent theEvent) {
        super.selectionChanged(theEvent);
        setEnabledState();
    }
    
    /**
     * Sets the enabled state of the action.
     */
    private void setEnabledState() {
        boolean enable = false;
        
        if (getTableEditor() != null) {
            enable = getTableEditor().canInsertRows();
        }

        setEnabled(enable);
    }

}
