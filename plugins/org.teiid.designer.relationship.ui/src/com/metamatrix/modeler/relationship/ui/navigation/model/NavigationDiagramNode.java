/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.relationship.ui.navigation.model;


/**
 * @author BLaFond
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class NavigationDiagramNode extends AbstractNavigationModelNode {
	
	public NavigationDiagramNode() {
		super(null, null, null);
	}
	    
	@Override
    public String toString() {
		return "NavigationDiagramNode(" + getName() + ")"; //$NON-NLS-1$ //$NON-NLS-2$
	}
}
