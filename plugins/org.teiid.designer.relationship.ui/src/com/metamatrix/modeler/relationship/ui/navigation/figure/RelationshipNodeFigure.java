/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.relationship.ui.navigation.figure;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.Ellipse;
import org.eclipse.draw2d.Figure;
import org.eclipse.draw2d.ImageFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.swt.graphics.Image;
import com.metamatrix.modeler.relationship.ui.navigation.model.RelationshipModelNode;


/**
 * @author BLaFond
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class RelationshipNodeFigure extends Figure {
	private RelationshipModelNode modelNode = null;
	private ImageFigure objectIcon;
	private boolean paintBackground = true;
    
	protected Ellipse circle;
	/**
	 * 
	 */
	public RelationshipNodeFigure(Image image) {
		super();
		init(image);
	}
	
	public RelationshipNodeFigure(RelationshipModelNode modelNode) {
		super();
		this.modelNode = modelNode;
		paintBackground = false;
		init(null);
	}
	
	private void init(Image icon) {
		int initialSize = 30;
		circle = new Ellipse();
		if( icon != null )
			objectIcon = new ImageFigure(icon);

		circle.setSize(initialSize, initialSize);
		circle.setLineWidth(3);
		circle.setForegroundColor(ColorConstants.darkBlue);
		if( paintBackground )
			circle.setBackgroundColor(ColorConstants.orange);
		circle.setFillXOR(false); // Couldn't tell if this did anything
		circle.setOutlineXOR(false); // Couldn't tell if this did anything
		this.add(circle);
		
		if( objectIcon != null) {
			this.add(objectIcon);
			int newX = 15 - objectIcon.getBounds().width/2;
			int newY = 15 - objectIcon.getBounds().height/2;

			objectIcon.setLocation(new Point(newX, newY));
		}
		modelNode.setSize(circle.getSize());
		this.setSize(circle.getSize());
	}
	
	@Override
    protected boolean useLocalCoordinates(){
		return true;
	}
	
	public void layoutFigure() {
		super.layout();
        
	}
    
	public void activate() {
		// Default implementation does nothing;
	}
    
	public void deactivate() {
		// Default implementation does nothing;
	}
    
    
	public void updateForSize(Dimension newSize ) {
		circle.setSize(newSize);
		centerIcon();
//		this.setSize(newSize);
	}
    
    
	public void updateForLocation(Point newLocation ) {
		this.setLocation(newLocation);
	}
	
	private void centerIcon() {
		if( objectIcon != null) {
			int newX = circle.getSize().width/2 - objectIcon.getBounds().width/2;
			int newY = circle.getSize().height/2 - objectIcon.getBounds().height/2;

			objectIcon.setLocation(new Point(newX, newY));
		}
	}
}
