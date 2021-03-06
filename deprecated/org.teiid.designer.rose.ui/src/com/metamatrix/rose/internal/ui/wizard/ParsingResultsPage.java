/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.rose.internal.ui.wizard;

import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import com.metamatrix.core.util.I18nUtil;
import com.metamatrix.rose.internal.RoseImporter;
import com.metamatrix.rose.internal.ui.IRoseUiConstants;
import com.metamatrix.rose.internal.ui.util.MessageTableViewForm;
import com.metamatrix.ui.internal.wizard.AbstractWizardPage;

/**
 * ParsingResultsPage
 */
public final class ParsingResultsPage extends AbstractWizardPage
                                      implements IRoseUiConstants,
                                                 IRoseUiConstants.Images {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTANTS
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /** Wizard page identifier. */
    public static final String PAGE_ID = ParsingResultsPage.class.getSimpleName();

    /** Properties key prefix. */
    private static final String PREFIX = I18nUtil.getPropertyPrefix(ParsingResultsPage.class);

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // FIELDS
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /** Business object for page. */
    private RoseImporter importer;

    private MessageTableViewForm msgTable;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Constructs a <code>ParsingResultsPage</code> wizard page using the specified business object.
     * @param theImporter the wizard business object
     */
    public ParsingResultsPage(RoseImporter theImporter) {
        super(PAGE_ID, UTIL.getString(PREFIX + "title")); //$NON-NLS-1$

        this.importer = theImporter;

        setPageComplete(true); // always complete
        setMessage(UTIL.getString(PREFIX + "msg.pageComplete")); //$NON-NLS-1$
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////////////////////////

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
     */
    public void createControl(Composite theParent) {
        this.msgTable = new MessageTableViewForm(theParent);
        this.msgTable.setLayoutData(new GridData(GridData.FILL_BOTH));
        setControl(this.msgTable);
    }

    private RoseImporter getImporter() {
        return this.importer;
    }

    /**
     * @see org.eclipse.jface.dialogs.IDialogPage#setVisible(boolean)
     */
    @Override
    public void setVisible(boolean theShowFlag) {
        if (theShowFlag) {
            // load message table
            this.msgTable.setMessages(getImporter().getParseProblems());
        }

        super.setVisible(theShowFlag);
    }

}
