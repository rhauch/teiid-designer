/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.ui.actions;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PartInitException;
import com.metamatrix.modeler.internal.ui.PluginConstants;
import com.metamatrix.modeler.internal.ui.search.ModelObjectSelectionDialog;
import com.metamatrix.modeler.ui.UiConstants;
import com.metamatrix.modeler.ui.UiPlugin;
import com.metamatrix.modeler.ui.editors.ModelEditorManager;
import com.metamatrix.ui.actions.AbstractAction;
import com.metamatrix.ui.internal.product.ProductCustomizerMgr;
import com.metamatrix.ui.internal.util.UiUtil;
import com.metamatrix.ui.internal.util.WidgetUtil;


/** 
 * @since 4.2
 */
public class FindModelObjectAction extends AbstractAction
                                   implements IWorkbenchWindowActionDelegate,
                                              UiConstants{

    /** 
     * @param thePlugin
     * @since 4.2
     */
    public FindModelObjectAction() {
        super(UiPlugin.getDefault());
        setImageDescriptor(UiPlugin.getDefault().getImageDescriptor(PluginConstants.Images.FIND));
    }

    // ===================================================
    //  Methods
    // ===================================================
    
    /**
     * Initializes this action delegate with the workbench window it will work in.
     *
     * @param window the window that provides the context for this delegate
     */
    public void init(IWorkbenchWindow window) {
        
    }
    
    public void run( IAction action ) {
        doRun(); 
    }
    
    /** 
     * @see com.metamatrix.ui.actions.AbstractAction#doRun()
     * @since 4.2
     */
    @Override
    protected void doRun() {
        
        Shell shell = UiPlugin.getDefault().getCurrentWorkbenchWindow().getShell();
        
        ModelObjectSelectionDialog dialog = new ModelObjectSelectionDialog( shell );
        
        if ( dialog.userCancelledDuringLoad() ) {
            return;
        }
        
        dialog.open();

        if (dialog.getReturnCode() == Window.OK) {
            // now select the object in the private view for the product
            EObject eObj = dialog.getSelectedEObject();
            
            if (eObj != null) {
                // change perspective to product's default perspective
                switchToDefaultPerspective();
                
                // open navigation view
                String viewId = ProductCustomizerMgr.getInstance().getProductCharacteristics().getPrimaryNavigationViewId();
                
                if (viewId != null) {
                    IWorkbenchPage page = UiUtil.getWorkbenchPage();
                    IViewPart view = page.findView(viewId);
                    
                    // if the view is not found in current perspective then open it
                    if (view == null) {
                        try {
                            view = page.showView(viewId);
                        } catch (PartInitException theException) {
                            Util.log(theException);
                            WidgetUtil.showError(theException.getLocalizedMessage());
                        }
                    }
                    
                    if (view != null) {
                        // use the views selection provider (if one exists) to select object
                        ISelectionProvider selectionProvider = view.getViewSite().getSelectionProvider();
                        
                        if (selectionProvider != null) {
                            selectionProvider.setSelection(new StructuredSelection(eObj));
                        }
                    }
                }
    
                // now open the object in an editor
                ModelEditorManager.open(eObj, !ModelEditorManager.isOpen(eObj));
            }
        }        
    }
    
    /**
     * Switches to the current product's default perspective (if one exists). 
     * @since 5.0
     */
    private void switchToDefaultPerspective() {
        String id = ProductCustomizerMgr.getInstance().getProductCharacteristics().getDefaultPerspectiveId();
        
        // product characteristics might not identify a default perspective
        if (id != null) {
            IWorkbench workbench = UiUtil.getWorkbench();

            try {
                workbench.showPerspective(id, UiUtil.getWorkbenchWindowOnlyIfUiThread());
            } catch (Exception theException) {
                Util.log(theException);
            }
        }
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.ui.ISelectionListener#selectionChanged(IWorkbenchPart, ISelection)
     */
    public void selectionChanged(IAction action,    
                                 ISelection theSelection) {
    }

}