/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.mapping.ui.part;

import java.util.List;
import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Graphics;
import org.eclipse.draw2d.IFigure;
import com.metamatrix.modeler.diagram.ui.connection.NodeConnectionEditPart;
import com.metamatrix.modeler.diagram.ui.connection.NodeConnectionModel;
import com.metamatrix.modeler.diagram.ui.figure.DiagramPolylineConnection;

/**
 * MappingLinkEditPart
 */
public class MappingLinkEditPart extends NodeConnectionEditPart {
    /**
     * @see org.eclipse.gef.editparts.AbstractGraphicalEditPart#createFigure()
     **/
    @Override
    protected IFigure createFigure() {
        DiagramPolylineConnection connectionFigure = new DiagramPolylineConnection();

        List toolTips = ((NodeConnectionModel)getModel()).getToolTipStrings();
        if (toolTips != null && !toolTips.isEmpty()) connectionFigure.setToolTip(connectionFigure.createToolTip(toolTips));

        connectionFigure.setLineStyle(Graphics.LINE_SOLID);
        int iStandardWidth = connectionFigure.getLineWidth();
        iStandardWidth = 1;
        connectionFigure.setLineWidth(iStandardWidth);
        connectionFigure.setForegroundColor(ColorConstants.darkBlue);

        return connectionFigure;
    }

}
