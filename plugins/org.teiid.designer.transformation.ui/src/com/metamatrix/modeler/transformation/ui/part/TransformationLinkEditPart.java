/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.transformation.ui.part;

import java.util.List;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.PolygonDecoration;
import com.metamatrix.modeler.diagram.ui.connection.NodeConnectionEditPart;
import com.metamatrix.modeler.diagram.ui.connection.NodeConnectionModel;
import com.metamatrix.modeler.diagram.ui.connection.decorator.OffsetArrowDecoration;
import com.metamatrix.modeler.diagram.ui.figure.DiagramPolylineConnection;

/**
 * TransformationLinkEditPart
 */
public class TransformationLinkEditPart extends NodeConnectionEditPart {
    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     **/
    @Override
    protected IFigure createFigure() {
        DiagramPolylineConnection connectionFigure = new DiagramPolylineConnection();

        List toolTips = ((NodeConnectionModel)getModel()).getToolTipStrings();
        if (toolTips != null && !toolTips.isEmpty()) connectionFigure.setToolTip(connectionFigure.createToolTip(toolTips));

        PolygonDecoration newDecoration = new OffsetArrowDecoration();
        newDecoration.setScale(8.0, 4.0);
        connectionFigure.setTargetDecoration(newDecoration);
        newDecoration.setForegroundColor(ColorConstants.black);

        connectionFigure.setLineStyle(Graphics.LINE_SOLID);
        int iStandardWidth = connectionFigure.getLineWidth();
        iStandardWidth = 1;
        connectionFigure.setLineWidth(iStandardWidth);
        connectionFigure.setForegroundColor(ColorConstants.darkBlue);

        return connectionFigure;
    }

}
