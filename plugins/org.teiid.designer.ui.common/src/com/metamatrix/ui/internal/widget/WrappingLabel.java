/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.ui.internal.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

/**
 * @since 4.0
 */
public class WrappingLabel extends Composite {

    // ============================================================================================================================
    // Constants

    private static final int CLABEL_INDENT = 3;
    private static final double PHI = 0.618;

    // ============================================================================================================================
    // Variables

    private Label label;

    // ============================================================================================================================
    // Constructors

    /**
     * @since 4.0
     */
    public WrappingLabel(final Composite parent,
                         int gridStyle,
                         int span,
                         String text) {
        super(parent, SWT.NONE);
        GridData gridData = new GridData(gridStyle);
        gridData.horizontalSpan = span;
        setLayoutData(gridData);
        GridLayout layout = new GridLayout();
        layout.marginWidth = layout.marginHeight = CLABEL_INDENT;
        setLayout(layout);
        this.label = new Label(this, SWT.WRAP);
        this.label.setLayoutData(new GridData(GridData.FILL_BOTH));
        if (text != null) {
            setText(text);
        }
        parent.addControlListener(new ControlAdapter() {

            @Override
            public void controlResized(final ControlEvent event) {
                parent.layout();
            }
        });
    }

    // ============================================================================================================================
    // Methods
    
    /**
     * @see org.eclipse.swt.widgets.Composite#computeSize(int, int, boolean)
     * @since 4.0
     */
    @Override
    public Point computeSize(int wHint,
                             int hHint,
                             boolean changed) {
        Point size = this.label.computeSize(wHint, hHint, changed);
        size = this.label.computeSize((int)(size.x * PHI), hHint, changed);
        GridLayout layout = (GridLayout)getLayout();
        size.x += layout.marginWidth * 2;
        size.y += layout.marginHeight * 2;
        Rectangle bounds = computeTrim(0, 0, size.x, size.y);
        return new Point(bounds.width, bounds.height);
    }
    
    /**
     * @since 4.0
     */
    public String getText() {
        return this.label.getText();
    }

    /**
     * @since 4.0
     */
    public void setText(String text) {
        this.label.setText(text);
    }
}
