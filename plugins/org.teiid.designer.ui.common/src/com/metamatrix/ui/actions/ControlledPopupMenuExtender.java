/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.ui.actions;


import org.eclipse.jface.action.IContributionItem;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.action.SubMenuManager;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.internal.ObjectActionContributorManager;
import org.eclipse.ui.internal.ViewerActionBuilder;

/**
 * This class extends a single popup menu and is derived by tweaking the PopupMenuExtender by not adding itself to
 * the listener of the menu manager.  This allows direct control of if and when to directly call menuAboutToShow() so
 * the external contributed object actions can be customized by specific menu adapters.
 * 
 * The initial case came from PackageDiagramActionAdapter, which needed to control the context menu when a link is selected.
 */
public class ControlledPopupMenuExtender implements IMenuListener {
    private String menuID;
    private MenuManager menu;
    private SubMenuManager menuWrapper;
    private ISelectionProvider selProvider;
    private IWorkbenchPart part;
    private ViewerActionBuilder staticActionBuilder;

    /**
     * Construct a new menu extender.
     */
    public ControlledPopupMenuExtender(String id, MenuManager menu, ISelectionProvider prov, IWorkbenchPart part) {
        super();
        this.menuID = id;
        this.menu = menu;
        this.selProvider = prov;
        this.part = part;
        //***************************************
        // BML 7/27/04
        // This next line is the key.  PopupMenuExtender always wired itself to the menu manager
        // We didn't want this in some cases.
//        menu.addMenuListener(this);
        //***************************************
        
        if (!menu.getRemoveAllWhenShown()) {
            menuWrapper = new SubMenuManager(menu);
            menuWrapper.setVisible(true);
        }
        readStaticActions();
    }
    /**
     * Contributes items registered for the object type(s) in
     * the current selection.
     */
    private void addObjectActions(IMenuManager mgr) {
        if (selProvider != null) {
            if (ObjectActionContributorManager.getManager().contributeObjectActions(part, mgr, selProvider)) {
                mgr.add(new Separator());
            }
        }
    }
    /**
     * Adds static items to the context menu.
     */
    private void addStaticActions(IMenuManager mgr) {
        if (staticActionBuilder != null)
            staticActionBuilder.contribute(mgr, null, true);
    }
    /**
     * Notifies the listener that the menu is about to be shown.
     */
    public void menuAboutToShow(IMenuManager mgr) {
        testForAdditions();
        if (menuWrapper != null) {
            mgr = menuWrapper;
            menuWrapper.removeAll();
        }
        addObjectActions(mgr);
        addStaticActions(mgr);
    }
    /**
     * Read static items for the context menu.
     */
    private void readStaticActions() {
        // If no menu id provided, then there is no contributions
        // to add. Fix for bug #33140.
        if (menuID != null && menuID.length() > 0) {
            staticActionBuilder = new ViewerActionBuilder();
            if (!staticActionBuilder.readViewerContributions(menuID, selProvider, part))
                staticActionBuilder = null;
        }
    }
    /**
     * Checks for the existance of an MB_ADDITIONS group.
     */
    private void testForAdditions() {
        IContributionItem item = menu.find(IWorkbenchActionConstants.MB_ADDITIONS);
        if (item == null) {
            menu.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
        }
    }
    /**
     * Dispose of the menu extender. Should only be called when the part
     * is disposed.
     */
    public void dispose() {
        if (staticActionBuilder != null) {
            staticActionBuilder.dispose();
        }
    }
}
