/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.relationship.ui.custom.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.viewers.ISelection;

import com.metamatrix.metamodels.diagram.Diagram;
import com.metamatrix.metamodels.relationship.RelationshipFolder;
import com.metamatrix.modeler.core.workspace.ModelResource;
import com.metamatrix.modeler.core.workspace.ModelWorkspaceException;
import com.metamatrix.modeler.internal.core.workspace.ModelUtil;
import com.metamatrix.modeler.internal.ui.viewsupport.ModelUtilities;
import com.metamatrix.modeler.relationship.ui.UiPlugin;
import com.metamatrix.modeler.relationship.ui.actions.RelationshipAction;
import com.metamatrix.modeler.relationship.ui.diagram.RelationshipDiagramUtil;
import com.metamatrix.modeler.relationship.ui.navigation.actions.OpenInNavigatorAction;
import com.metamatrix.modeler.ui.actions.IModelObjectActionContributor;
import com.metamatrix.ui.internal.eventsupport.SelectionUtilities;

/**
 * PackageDiagramPermanentActionContributor
 */
public class CustomDiagramPermanentActionContributor implements IModelObjectActionContributor {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // FIELDS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    private RelationshipAction createDiagramAction;
    private RelationshipAction createDiagramSiblingAction;
    
    private OpenInNavigatorAction openInNavigatorAction;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    public CustomDiagramPermanentActionContributor() {
        initActions();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /* (non-Javadoc)
     * @see com.metamatrix.modeler.ui.actions.IModelObjectActionContributor#contributeToContextMenu(org.eclipse.jface.action.IMenuManager, org.eclipse.jface.viewers.ISelection)
     */
    public void contributeToContextMenu(IMenuManager theMenuMgr, ISelection theSelection) {
        // Need to check the selection first.
        
        if( SelectionUtilities.isSingleSelection(theSelection) ) {
            Object selectedObject = SelectionUtilities.getSelectedObject(theSelection);
            if( selectedObject instanceof EObject ) {
                EObject eObject = (EObject)selectedObject;
				if( RelationshipDiagramUtil.isRelationshipModelResource(eObject) ){
	                if( isPackage(eObject) ) {
	                    addToChildMenu(theMenuMgr);
	                }
	                if( eObject instanceof Diagram || (eObject.eContainer()!= null && 
	                    isPackage(eObject.eContainer()) )) {
	                    addToSiblingMenu(theMenuMgr);
	                } else if( eObject.eContainer() == null ) {
	                    addToSiblingMenu(theMenuMgr);
	                }
				}
            }else if( (selectedObject instanceof IResource) && 
						ModelUtilities.isModelFile((IResource)selectedObject) ) {
				// make sure this is a relationship model
			            	
				ModelResource mr = null;
			            	
				try {
					mr = ModelUtil.getModelResource((IFile)selectedObject, false);
				} catch (ModelWorkspaceException e) {
					e.printStackTrace();
				}
			            	
				if( mr != null && RelationshipDiagramUtil.isRelationshipModelResource(mr))
					addToChildMenu(theMenuMgr);
			}
        }
    }
    
    /**
     *  
     * @see com.metamatrix.modeler.ui.actions.IModelObjectActionContributor#getAdditionalModelingActions(org.eclipse.jface.viewers.ISelection)
     * @since 5.0
     */
    public List<IAction> getAdditionalModelingActions(ISelection theSelection) {
        List addedActions = new ArrayList();
        
        // Need to check the selection first.
        if( openInNavigatorAction.isEnabled() ) {
            if( SelectionUtilities.isSingleSelection(theSelection) &&
            	SelectionUtilities.isAllEObjects(theSelection)  ) {
                addedActions.add(openInNavigatorAction);
            }
        }
        
        return addedActions;
    }
    
    /**
     * Construct and register actions.
     */
    private void initActions() {
        createDiagramAction = new NewCustomDiagramAction();
        UiPlugin.registerActionForSelection(createDiagramAction);
        createDiagramSiblingAction = new NewCustomDiagramSiblingAction();
        UiPlugin.registerActionForSelection(createDiagramSiblingAction);
        openInNavigatorAction = new OpenInNavigatorAction();
        UiPlugin.registerActionForSelection(openInNavigatorAction);
    }
    
    private void addToChildMenu(IMenuManager theMenuMgr) {
//        String menuPath = ModelerActionBarIdManager.getInsertChildMenuId();
//                
//        IMenuManager mm = theMenuMgr.findMenuUsingPath(menuPath);
//                
//        if( mm != null ) {
//            mm.add(createDiagramAction);
//        } else {
//            theMenuMgr.add(createDiagramAction);
//        }
    }
    
    private void addToSiblingMenu(IMenuManager theMenuMgr) {
//        String menuPath = ModelerActionBarIdManager.getInsertSiblingMenuId();
//                
//        IMenuManager mm = theMenuMgr.findMenuUsingPath(menuPath);
//                
//        if( mm != null ) {
//            mm.add(createDiagramSiblingAction);
//        } else {
//            theMenuMgr.add(createDiagramSiblingAction);
//        }
    }
    
    private boolean isPackage(EObject eObject) {
        if( eObject instanceof RelationshipFolder )
            return true;
            
        return false;
    }
}

