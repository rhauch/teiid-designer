/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.diagram.ui.notation.uml.part;

import java.beans.PropertyChangeEvent;
import java.util.List;
import java.util.Vector;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import org.eclipse.gef.DragTracker;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.Request;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.swt.graphics.Color;
import com.metamatrix.modeler.diagram.ui.DiagramUiConstants;
import com.metamatrix.modeler.diagram.ui.connection.BinaryAssociation;
import com.metamatrix.modeler.diagram.ui.connection.DiagramUmlAssociation;
import com.metamatrix.modeler.diagram.ui.connection.LinkBendpointEditPolicy;
import com.metamatrix.modeler.diagram.ui.connection.LinkEndpointEditPolicy;
import com.metamatrix.modeler.diagram.ui.connection.NodeConnectionEditPart;
import com.metamatrix.modeler.diagram.ui.connection.NodeConnectionModel;
import com.metamatrix.modeler.diagram.ui.connection.decorator.DecoratorFactory;
import com.metamatrix.modeler.diagram.ui.editor.DiagramEditorUtil;
import com.metamatrix.modeler.diagram.ui.figure.DiagramPolylineConnection;
import com.metamatrix.modeler.diagram.ui.util.ConnectionSelectionTracker;
import com.metamatrix.modeler.diagram.ui.util.DiagramNodeSelectionEditPolicy;

/**
 * ForeignKeyLinkEditPart
 */
public class UmlAssociationLinkEditPart extends NodeConnectionEditPart {
    private DragTracker dragTracker = null;
	/**
	 * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
	**/
	@Override
    protected IFigure createFigure() {
		DiagramPolylineConnection connectionFigure = new DiagramPolylineConnection();
		
		List toolTips = ((NodeConnectionModel)getModel()).getToolTipStrings();
		if( toolTips != null && !toolTips.isEmpty() )
			connectionFigure.setToolTip(connectionFigure.createToolTip(toolTips));
			
		int targetDecID = ((NodeConnectionModel)getModel()).getTargetDecoratorId();
		int sourceDecID = ((NodeConnectionModel)getModel()).getSourceDecoratorId();
		Color diagramBkgdColor = ((AbstractGraphicalEditPart)getDiagramViewer().getContents()).getFigure().getBackgroundColor();
		
		PolygonDecoration newTargetDecoration = DecoratorFactory.getDecorator(targetDecID);
		PolygonDecoration newSourceDecoration = DecoratorFactory.getDecorator(sourceDecID);
		int iStandardWidth = 1;
		
		if( newTargetDecoration != null ) {
			newTargetDecoration.setLineWidth(iStandardWidth);
			if( targetDecID == BinaryAssociation.DECORATOR_DIAMOND_OPEN ||
				targetDecID == BinaryAssociation.DECORATOR_ARROW_CLOSED)
				newTargetDecoration.setBackgroundColor(diagramBkgdColor);
			connectionFigure.setTargetDecoration(newTargetDecoration);
		}
		if( newSourceDecoration != null ) {
			newSourceDecoration.setLineWidth(iStandardWidth);
			if( sourceDecID == BinaryAssociation.DECORATOR_DIAMOND_OPEN ||
				sourceDecID == BinaryAssociation.DECORATOR_ARROW_CLOSED)
				newSourceDecoration.setBackgroundColor(diagramBkgdColor);
			connectionFigure.setSourceDecoration(newSourceDecoration);
		}
		
		connectionFigure.setLineStyle( ((NodeConnectionModel)getModel()).getLineStyle() );

		connectionFigure.setLineWidth( iStandardWidth );
		connectionFigure.setForegroundColor(ColorConstants.blue);
        
		return connectionFigure;
	}
	
	
	/* (non-Javadoc)
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
    protected void createEditPolicies() {
//        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new DiagramConnectionEndpointEditPolicy());
        refreshBendpointEditPolicy();
        installEditPolicy(EditPolicy.CONNECTION_ENDPOINTS_ROLE, new LinkEndpointEditPolicy());
        installEditPolicy(EditPolicy.SELECTION_FEEDBACK_ROLE, new DiagramNodeSelectionEditPolicy());
	}

	/* (non-Javadoc)
	 * @see org.eclipse.gef.EditPart#getDragTracker(org.eclipse.gef.Request)
	 */
	@Override
    public DragTracker getDragTracker(Request req) {
		if( dragTracker == null )
			dragTracker = new ConnectionSelectionTracker(this, getDiagramViewer().getSelectionHandler());
			
		return dragTracker;
	}

    
    private void refreshBendpointEditPolicy(){
        if (DiagramEditorUtil.getCurrentDiagramRouterStyle() != DiagramUiConstants.LinkRouter.MANUAL )
            installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, null);
        else
            installEditPolicy(EditPolicy.CONNECTION_BENDPOINTS_ROLE, new LinkBendpointEditPolicy());
    }
    
    /** 
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     * @since 4.2
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        String prop = evt.getPropertyName();
        super.propertyChange(evt);
        if (DiagramUiConstants.DiagramNodeProperties.ROUTER.equals(prop)){
            if(((DiagramUmlAssociation)getModel()).changedRouterFromOtoM((String)evt.getOldValue(), (String)evt.getNewValue()) ) {
                DiagramPolylineConnection dpc = (DiagramPolylineConnection)getFigure();
                Vector orthPts = new Vector(dpc.getInternalPoints());
                ((NodeConnectionModel)getModel()).clearBendpoints();
                ((NodeConnectionModel)getModel()).setBendpoints(orthPts);
            }
            
            
            refreshBendpointEditPolicy();
            refreshBendpoints();

        }
//        super.propertyChange(evt);
    }
}
