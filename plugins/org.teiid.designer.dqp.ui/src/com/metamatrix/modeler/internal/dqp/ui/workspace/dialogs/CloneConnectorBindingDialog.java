/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.dqp.ui.workspace.dialogs;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Properties;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.teiid.designer.runtime.ConnectorType;
import com.metamatrix.core.event.IChangeListener;
import com.metamatrix.core.event.IChangeNotifier;
import com.metamatrix.core.util.I18nUtil;
import com.metamatrix.modeler.dqp.ui.DqpUiConstants;
import com.metamatrix.modeler.dqp.ui.DqpUiPlugin;
import com.metamatrix.ui.internal.widget.ExtendedTitleAreaDialog;

/**
 * @since 5.0
 */
public class CloneConnectorBindingDialog extends ExtendedTitleAreaDialog implements IChangeListener {

    private static final String I18N_PREFIX = I18nUtil.getPropertyPrefix(CloneConnectorBindingDialog.class);
    private static final int WIDTH = 400;

    private Button btnOk;

    private CloneConnectorBindingPanel pnlBindings;

    private String name;
    private ConnectorType type;
    private Properties properties;

    private Collection<IChangeListener> changeListenerList = new ArrayList<IChangeListener>(2);

    public CloneConnectorBindingDialog( Shell theParentShell,
                                        String name,
                                        ConnectorType type,
                                        Properties properties ) {
        super(theParentShell, DqpUiPlugin.getDefault());

        this.name = name;
        this.type = type;
        this.properties = properties;
    }

    @Override
    protected Control createDialogArea( Composite parent ) {
        Composite mainComposite = (Composite)super.createDialogArea(parent);
        GridLayout gridLayout = new GridLayout();
        mainComposite.setLayout(gridLayout);
        gridLayout.numColumns = 1;

        GridData gd = new GridData(SWT.FILL, SWT.FILL, true, true);
        gd.widthHint = WIDTH;
        mainComposite.setLayoutData(gd);

        this.pnlBindings = new CloneConnectorBindingPanel(mainComposite, this.name, this.type, this.properties);
        this.pnlBindings.addChangeListener(this);
        this.pnlBindings.setFocus();

        getShell().setText(DqpUiConstants.UTIL.getString(I18N_PREFIX + "name")); //$NON-NLS-1$
        setTitle(DqpUiConstants.UTIL.getString(I18N_PREFIX + "title")); //$NON-NLS-1$

        Display.getDefault().asyncExec(new Runnable() {

            public void run() {
                updateState();
            }
        });

        return mainComposite;
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#createButton(org.eclipse.swt.widgets.Composite, int, java.lang.String, boolean)
     * @since 4.3
     */
    @Override
    protected Button createButton( Composite theParent,
                                   int theId,
                                   String theLabel,
                                   boolean theDefaultButton ) {
        Button btn = super.createButton(theParent, theId, theLabel, theDefaultButton);

        if (theId == IDialogConstants.OK_ID) {
            this.btnOk = btn;
            this.btnOk.setEnabled(false);
            this.btnOk.setToolTipText("OK"); // Util.getStringOrKey(I18N_PREFIX + "btnOk.tip")); //$NON-NLS-1$
        }

        return btn;
    }

    public void addIChangeListener( IChangeListener listener ) {
        if (!changeListenerList.contains(listener)) {
            changeListenerList.add(listener);
        }
    }

    public void removeIChangeListener( IChangeListener listener ) {
        changeListenerList.remove(listener);
    }

    /**
     * @see com.metamatrix.core.event.IChangeListener#stateChanged(com.metamatrix.core.event.IChangeNotifier)
     * @since 4.3
     */
    public void stateChanged( IChangeNotifier theSource ) {
        for (Iterator<IChangeListener> iter = changeListenerList.iterator(); iter.hasNext();) {
            iter.next().stateChanged(theSource);
        }
        updateState();
    }

    void updateState() {
        IStatus status = this.pnlBindings.getStatus();
        setMessage(status.getMessage(), status.getSeverity());

        this.btnOk.setEnabled(status.getSeverity() == IStatus.OK);
    }

    /**
     * @see org.eclipse.jface.dialogs.Dialog#okPressed()
     * @since 4.3
     */
    @Override
    protected void okPressed() {
        this.pnlBindings.save();
        super.okPressed();
    }

    public String getConnectorName() {
        return this.pnlBindings.getConnectorName();
    }

    public Properties getConnectorProperties() {
        return this.pnlBindings.getConnectorProperties();
    }
}