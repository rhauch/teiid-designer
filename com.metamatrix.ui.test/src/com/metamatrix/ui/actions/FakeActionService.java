/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.ui.actions;

import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import com.metamatrix.ui.AbstractUiPlugin;

/**
 * @since 4.0
 */
public class FakeActionService implements ActionService {

    private IWorkbenchWindow window;

    /**
     * @since 4.0
     */
    public FakeActionService( IWorkbenchWindow theWindow ) {
        super();
        setWorkbenchWindow(theWindow);
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#contributeToContextMenu(org.eclipse.jface.action.IMenuManager,
     *      com.metamatrix.ui.actions.GlobalActionsMap, org.eclipse.jface.viewers.ISelection)
     */
    public void contributeToContextMenu( IMenuManager theMenuMgr,
                                         GlobalActionsMap theActionsMap,
                                         ISelection theSelection ) {
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#contributePermanentActionsToContextMenu(org.eclipse.jface.action.IMenuManager,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void contributePermanentActionsToContextMenu( IMenuManager theMenuMgr,
                                                         ISelection theSelection ) {
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#contributePermanentActionsToEditMenu(org.eclipse.jface.action.IMenuManager,
     *      org.eclipse.jface.viewers.ISelection)
     */
    public void contributePermanentActionsToEditMenu( IMenuManager theMenuMgr,
                                                      ISelection theSelection ) {
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#getAction(java.lang.String)
     * @since 4.0
     */
    public IAction getAction( String theActionId ) {
        return null;
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#getDefaultAction(java.lang.String)
     */
    public IAction getDefaultAction( String theActionId ) {
        return null;
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#addEditorSelectionListener(org.eclipse.ui.IEditorPart,
     *      org.eclipse.jface.viewers.ISelectionChangedListener)
     * @since 4.0
     */
    public void addEditorSelectionListener( IEditorPart theEditor,
                                            ISelectionChangedListener theListener ) {

    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#addResourceChangeListener(IResourceChangeListener)
     */
    public void addResourceChangeListener( IResourceChangeListener theListener ) {
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#addViewSelectionListener(org.eclipse.ui.IViewPart,
     *      org.eclipse.jface.viewers.ISelectionChangedListener)
     * @since 4.0
     */
    public void addViewSelectionListener( IViewPart theView,
                                          ISelectionChangedListener theListener ) {

    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#addWorkbenchSelectionListener(org.eclipse.ui.ISelectionListener)
     * @since 4.0
     */
    public void addWorkbenchSelectionListener( ISelectionListener theListener ) {
        if (window == null) {
            throw new IllegalStateException("Workbench window has not been set."); //$NON-NLS-1$
        }
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#removeEditorSelectionListener(org.eclipse.ui.IEditorPart,
     *      org.eclipse.jface.viewers.ISelectionChangedListener)
     * @since 4.0
     */
    public void removeEditorSelectionListener( IEditorPart theEditor,
                                               ISelectionChangedListener theListener ) {

    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#removeResourceChangeListener(IResourceChangeListener)
     */
    public void removeResourceChangeListener( IResourceChangeListener theListener ) {
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#removeViewSelectionListener(org.eclipse.ui.IViewPart,
     *      org.eclipse.jface.viewers.ISelectionChangedListener)
     * @since 4.0
     */
    public void removeViewSelectionListener( IViewPart theView,
                                             ISelectionChangedListener theListener ) {

    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#removeWorkbenchSelectionListener(org.eclipse.ui.ISelectionListener)
     * @since 4.0
     */
    public void removeWorkbenchSelectionListener( ISelectionListener theListener ) {
        if (window == null) {
            throw new IllegalStateException("Workbench window has not been set."); //$NON-NLS-1$
        }
    }

    /**
     * Gets the plugin associated with this service.
     * 
     * @return the plugin
     */
    public AbstractUiPlugin getPlugin() {
        return null;
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#getWorkbenchWindow()
     */
    public IWorkbenchWindow getWorkbenchWindow() {
        if (window == null) {
            throw new IllegalStateException("Workbench window has not been set."); //$NON-NLS-1$
        }

        return window;
    }

    /**
     * Convenience version of registerAction. Key defaults to getId of the action.
     * 
     * @param theActionId the action identifier
     * @return true if successful
     */
    public boolean registerAction( IAction theAction ) {
        return false;
    }

    /**
     * Registers an action.
     * 
     * @param theActionId the action identifier
     * @param theAction the action
     * @return true if successful
     */
    public boolean registerAction( String sActionId,
                                   IAction theAction ) {
        return false;
    }

    /**
     * Registers an action.
     * 
     * @param theActionId the action identifier
     * @param theAction the action
     * @param bForce true if this action should replace an exising one with the same key
     * @return true if successful
     */
    public boolean registerAction( String sActionId,
                                   IAction theAction,
                                   boolean bForce ) {
        return false;
    }

    /**
     * Indicates if and action with the given identifier has been registered.
     * 
     * @param theActionId the action identifier
     * @return <code>true</code> if the action is registered; <code>false</code> otherwise.
     */
    public boolean isRegistered( String theActionId ) {
        return false;
    }

    /**
     * Indicates if and action with the given <code>Class</code> has been registered.
     * 
     * @param theActionClass the action class
     * @return <code>true</code> if the action is registered; <code>false</code> otherwise.
     */
    public boolean isRegistered( Class theActionClass ) {
        return false;
    }

    /**
     * Removes the action with the given identifier from the action service.
     * 
     * @param theActionId the identifier of the action being removed
     * @throws com.metamatrix.core.util.AssertionError if input is null
     */
    public void removeAction( String theActionId ) {
    }

    /**
     * Removes the action with the given from the action service.
     * 
     * @param theActionClass the class of the action being removed
     * @throws com.metamatrix.core.util.AssertionError if input is null
     */
    public void removeAction( Class theActionClass ) {
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#setWorkbenchWindow(IWorkbenchWindow)
     */
    public void setWorkbenchWindow( IWorkbenchWindow theWindow ) {
        window = theWindow;
    }

    /**
     * @see com.metamatrix.ui.actions.ActionService#shutdown()
     */
    public void shutdown() {
    }

}
