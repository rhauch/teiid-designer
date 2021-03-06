/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.widgets.Event;
import org.eclipse.ui.IActionDelegate2;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.views.navigator.ResourceNavigator;

import com.metamatrix.modeler.internal.ui.PluginConstants;
import com.metamatrix.modeler.ui.UiPlugin;
import com.metamatrix.modeler.ui.viewsupport.NonModelViewerFilter;


/**
 * The <code>ShowNonModelsAction</code> controls whether the Model Explorer shows/hides non-model files.
 * It uses and updates the associated preference value.
 * @since 5.0.1
 */
public class ShowNonModelsAction extends Action
                                 implements IActionDelegate2,
                                            IViewActionDelegate {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTANTS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * The key used to store/obtain the value in/from the preference store 
     * @since 5.0.1
     */
    public static final String PREF_ID = PluginConstants.Prefs.General.SHOW_NON_MODELS_IN_MODEL_EXPLORER;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // FIELDS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    private IAction action;
    
    private ViewerFilter filter = new NonModelViewerFilter();
    
    private TreeViewer viewer;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Constructs the action.
     */
    public ShowNonModelsAction() {
        // don't need to externalize this string as the text is actually taken from the plugin.properties file.
        super("Show Non-models", AS_CHECK_BOX); //$NON-NLS-1$
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /** 
     * @see org.eclipse.ui.IActionDelegate2#dispose()
     * @since 5.0.1
     */
    public void dispose() {
        // nothing to do
    }
    
    /**
     * Obtain the preference store.
     * @return the store
     */
    private IPreferenceStore getPreferenceStore() {
        return UiPlugin.getDefault().getPreferenceStore();
    }
    
    /**
     * Obtains the current value of the preference. 
     * @return the value
     * @since 5.0.1
     */
    protected boolean getPreferenceValue() {
        IPreferenceStore store = getPreferenceStore();
        boolean result = true;

        if (store.contains(PREF_ID)) {
            result = store.getBoolean(PREF_ID);
        } else {
            result = store.getDefaultBoolean(PREF_ID);
        }

        return result;       
    }
    /** 
     * @see org.eclipse.ui.IActionDelegate2#init(org.eclipse.jface.action.IAction)
     * @since 5.0.1
     */
    public void init(IAction theAction) {
        this.action = theAction;
    }

    /** 
     * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
     * @since 5.0.1
     */
    public void init(IViewPart theView) {
        this.viewer = ((ResourceNavigator)theView).getTreeViewer();

        // set initial state based on preference
        boolean checked = getPreferenceValue();
        this.action.setChecked(checked);
        run(this.action);
    }
    
    /** 
     * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
     * @since 5.0.1
     */
    public void run(IAction theAction) {
        if (!theAction.isChecked()) {
            this.viewer.addFilter(this.filter);
        } else {
            this.viewer.removeFilter(this.filter);
        }

        // update preference value
        IPreferenceStore store = getPreferenceStore();
        store.setValue(PREF_ID, theAction.isChecked());
    }
    
    /** 
     * @see org.eclipse.ui.IActionDelegate2#runWithEvent(org.eclipse.jface.action.IAction, org.eclipse.swt.widgets.Event)
     * @since 5.0.1
     */
    public void runWithEvent(IAction theAction,
                             Event theEvent) {
        run(theAction);
    }

    /** 
     * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction, org.eclipse.jface.viewers.ISelection)
     * @since 5.0.1
     */
    public void selectionChanged(IAction theAction,
                                 ISelection theSelection) {
        // do nothing
    }

}
