/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.diagram.ui.preferences;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import com.metamatrix.modeler.diagram.ui.DiagramUiConstants;
import com.metamatrix.modeler.diagram.ui.DiagramUiPlugin;
import com.metamatrix.modeler.diagram.ui.PluginConstants;
import com.metamatrix.ui.internal.util.WidgetFactory;

/**
 * This class is the preference page for setting the Modeler Diagram Printing Preferences.
 */
public class DiagramPrintPreferencePage extends PreferencePage
    implements DiagramUiConstants, PluginConstants, IWorkbenchPreferencePage {

    // Property File Keys
    private static final String KEY_PAGE_DESCRIPTION = Util.getString("DiagramPrintPreferencePage.description"); //$NON-NLS-1$

    private static final String ORIENTATION_TITLE = Util.getString("DiagramPrintPreferencePage.orientation.title"); //$NON-NLS-1$
    private static final String PORTRAIT = Util.getString("DiagramPrintPreferencePage.portrait.text"); //$NON-NLS-1$
    private static final String LANDSCAPE = Util.getString("DiagramPrintPreferencePage.landscape.text"); //$NON-NLS-1$

    private Button radioPortrait;
    private Button radioLandscape;

    private static final String SCALING_TITLE = Util.getString("DiagramPrintPreferencePage.scaling.title"); //$NON-NLS-1$
    private static final String ADJUST_TO = Util.getString("DiagramPrintPreferencePage.adjustTo.text"); //$NON-NLS-1$
    private static final String PERCENT_NORMAL_SIZE = Util.getString("DiagramPrintPreferencePage.percentNormalSize.text"); //$NON-NLS-1$

    private static final String FIT_TO_ONE_PAGE = Util.getString("DiagramPrintPreferencePage.fitToOnePage.text"); //$NON-NLS-1$
    private static final String FIT_TO_ONE_PAGE_HIGH = Util.getString("DiagramPrintPreferencePage.fitToOnePageHigh.text"); //$NON-NLS-1$
    private static final String FIT_TO_ONE_PAGE_WIDE = Util.getString("DiagramPrintPreferencePage.fitToOnePageWide.text"); //$NON-NLS-1$

    private Button radioFitToOnePage;
    private Button radioFitToOnePageWide;
    private Button radioFitToOnePageHigh;
    private Button radioAdjustToPercentage;
    private Spinner spinScalingPercentage;

    private int SCALING_PERCENTAGE_MIN = 50;
    private int SCALING_PERCENTAGE_MAX = 300;
    private int SCALING_PERCENTAGE_INCREMENT = 5;

    private static final String MARGINS_TITLE = Util.getString("DiagramPrintPreferencePage.margins.title"); //$NON-NLS-1$
    private static final String TOP = Util.getString("DiagramPrintPreferencePage.top.text"); //$NON-NLS-1$
    private static final String RIGHT = Util.getString("DiagramPrintPreferencePage.right.text"); //$NON-NLS-1$
    private static final String BOTTOM = Util.getString("DiagramPrintPreferencePage.bottom.text"); //$NON-NLS-1$
    private static final String LEFT = Util.getString("DiagramPrintPreferencePage.left.text"); //$NON-NLS-1$

    private Spinner spinTopMargin;
    private Spinner spinRightMargin;
    private Spinner spinBottomMargin;
    private Spinner spinLeftMargin;

    private int MARGIN_MIN = 0;
    private int MARGIN_MAX = 30;
    private int MARGIN_INCREMENT = 1;

    // shifting decimal to the left 1; so divide by 10
    private double MARGIN_DIVISOR = 10;

    private static final String PAGE_ORDER_TITLE = Util.getString("DiagramPrintPreferencePage.pageOrder.title"); //$NON-NLS-1$

    private static final String OVER_THEN_DOWN = Util.getString("DiagramPrintPreferencePage.overThenDown.text"); //$NON-NLS-1$
    private static final String DOWN_THEN_OVER = Util.getString("DiagramPrintPreferencePage.downThenOver.text"); //$NON-NLS-1$    	

    private Button radioOverThenDown;
    private Button radioDownThenOver;

    public DiagramPrintPreferencePage() {
        super();
        setPreferenceStore(DiagramUiPlugin.getDefault().getPreferenceStore());
        setDescription(KEY_PAGE_DESCRIPTION);

    }

    public void init( IWorkbench workbench ) {
    }

    @Override
    public Control createContents( Composite parent ) {
        Composite comp = new Composite(parent, SWT.NONE);

        GridLayout layout = new GridLayout();
        comp.setLayout(layout);

        // ================================================
        // 1. Orientation
        // ================================================
        Group grpOrientation = WidgetFactory.createGroup(comp, ORIENTATION_TITLE, GridData.FILL_HORIZONTAL, 1, 2);

        radioPortrait = WidgetFactory.createRadioButton(grpOrientation, PORTRAIT);
        radioLandscape = WidgetFactory.createRadioButton(grpOrientation, LANDSCAPE);

        // ================================================
        // 2. Scaling
        // ================================================
        Group grpScaling = WidgetFactory.createGroup(comp, SCALING_TITLE, GridData.FILL_HORIZONTAL);

        radioFitToOnePage = WidgetFactory.createRadioButton(grpScaling, FIT_TO_ONE_PAGE);
        radioFitToOnePageWide = WidgetFactory.createRadioButton(grpScaling, FIT_TO_ONE_PAGE_HIGH);
        radioFitToOnePageHigh = WidgetFactory.createRadioButton(grpScaling, FIT_TO_ONE_PAGE_WIDE);
        radioAdjustToPercentage = WidgetFactory.createRadioButton(grpScaling, ADJUST_TO);

        // Indent this last row
        Composite pnlPercentSpinner = new Composite(grpScaling, SWT.NONE);
        GridLayout layPercentSpinner = new GridLayout();
        layPercentSpinner.numColumns = 3;
        pnlPercentSpinner.setLayout(layPercentSpinner);

        GridData gdPercentSpinner = new GridData(GridData.FILL_HORIZONTAL);
        pnlPercentSpinner.setLayoutData(gdPercentSpinner);

        createSpacer(pnlPercentSpinner);

        spinScalingPercentage = new Spinner(pnlPercentSpinner, SWT.READ_ONLY);
        spinScalingPercentage.setMinimum(SCALING_PERCENTAGE_MIN);
        spinScalingPercentage.setMaximum(SCALING_PERCENTAGE_MAX);
        spinScalingPercentage.setIncrement(SCALING_PERCENTAGE_INCREMENT);
        spinScalingPercentage.setToolTipText(Util.getString("DiagramPrintPreferencePage.spinner.toolTip", //$NON-NLS-1$
                                                            spinScalingPercentage.getMinimum(),
                                                            spinScalingPercentage.getMaximum()));

        WidgetFactory.createLabel(pnlPercentSpinner, PERCENT_NORMAL_SIZE);

        radioAdjustToPercentage.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected( SelectionEvent theEvent ) {
                syncPercentageSpinnerToAdjustRadio();
            }
        });

        // ================================================
        // 3. Margins
        // ================================================
        Group grpMargins = WidgetFactory.createGroup(comp, MARGINS_TITLE, GridData.FILL_HORIZONTAL, 1, 3);

        createSpacer(grpMargins);
        spinTopMargin = createMarginPanel(grpMargins, TOP);
        createSpacer(grpMargins);

        spinLeftMargin = createMarginPanel(grpMargins, LEFT);
        createSpacer(grpMargins);
        spinRightMargin = createMarginPanel(grpMargins, RIGHT);

        createSpacer(grpMargins);
        spinBottomMargin = createMarginPanel(grpMargins, BOTTOM);
        createSpacer(grpMargins);

        // ================================================
        // 4. Page Order
        // ================================================
        Group grpPageOrder = WidgetFactory.createGroup(comp, PAGE_ORDER_TITLE, GridData.FILL_HORIZONTAL);

        radioOverThenDown = WidgetFactory.createRadioButton(grpPageOrder, OVER_THEN_DOWN);
        radioDownThenOver = WidgetFactory.createRadioButton(grpPageOrder, DOWN_THEN_OVER);

        // ================================================
        // 5. Load pref data from pref store to gui
        // ================================================
        setUIFromPreferenceStore();
        syncPercentageSpinnerToAdjustRadio();

        return comp;
    }

    void syncPercentageSpinnerToAdjustRadio() {
        spinScalingPercentage.setEnabled(radioAdjustToPercentage.getSelection());
    }

    private Spinner createMarginPanel( Composite parent,
                                       String sLabel ) {
        /*
         * Goal:    Top: [ 1.0 ]#
         */

        // Indent this last row
        Composite pnlOuter = new Composite(parent, SWT.NONE);
        GridLayout layOuter = new GridLayout();
        layOuter.numColumns = 2;
        pnlOuter.setLayout(layOuter);

        GridData gdOuter = new GridData(GridData.FILL_HORIZONTAL);
        gdOuter.horizontalAlignment = SWT.RIGHT;

        pnlOuter.setLayoutData(gdOuter);

        WidgetFactory.createLabel(pnlOuter, sLabel);

        Spinner spinMargin = new Spinner(pnlOuter, SWT.READ_ONLY);
        spinMargin.setMinimum(MARGIN_MIN);
        spinMargin.setMaximum(MARGIN_MAX);
        spinMargin.setIncrement(MARGIN_INCREMENT);
        spinMargin.setDigits(1);
        spinMargin.setToolTipText(Util.getString("DiagramPrintPreferencePage.spinner.toolTip", //$NON-NLS-1$
                                                 Double.toString(spinMargin.getMinimum() / MARGIN_DIVISOR),
                                                 Double.toString(spinMargin.getMaximum() / MARGIN_DIVISOR)));

        return spinMargin;
    }

    private void createSpacer( Composite parent ) {
        Spinner spinTemp = new Spinner(parent, SWT.NONE);
        spinTemp.setVisible(false);
    }

    private void setPreferenceStoreFromUI() {
        IPreferenceStore store = getPreferenceStore();

        // ================================================
        // 1. Orientation
        // ================================================
        store.setValue(PluginConstants.Prefs.Print.PORTRAIT, radioPortrait.getSelection());
        store.setValue(PluginConstants.Prefs.Print.LANDSCAPE, radioLandscape.getSelection());

        // ================================================
        // 2. Scaling
        // ================================================

        store.setValue(PluginConstants.Prefs.Print.FIT_TO_ONE_PAGE, radioFitToOnePage.getSelection());
        store.setValue(PluginConstants.Prefs.Print.FIT_TO_ONE_PAGE_WIDE, radioFitToOnePageWide.getSelection());
        store.setValue(PluginConstants.Prefs.Print.FIT_TO_ONE_PAGE_HIGH, radioFitToOnePageHigh.getSelection());
        store.setValue(PluginConstants.Prefs.Print.ADJUST_TO_PERCENT, radioAdjustToPercentage.getSelection());

        int iPct = spinScalingPercentage.getSelection();
        store.setValue(PluginConstants.Prefs.Print.SCALING_PERCENTAGE, iPct);

        // ================================================
        // 3. Margins
        // ================================================

        int value = spinTopMargin.getSelection();
        store.setValue(PluginConstants.Prefs.Print.TOP_MARGIN, (value / MARGIN_DIVISOR));

        value = spinRightMargin.getSelection();
        store.setValue(PluginConstants.Prefs.Print.RIGHT_MARGIN, (value / MARGIN_DIVISOR));

        value = spinBottomMargin.getSelection();
        store.setValue(PluginConstants.Prefs.Print.BOTTOM_MARGIN, (value / MARGIN_DIVISOR));

        value = spinLeftMargin.getSelection();
        store.setValue(PluginConstants.Prefs.Print.LEFT_MARGIN, (value / MARGIN_DIVISOR));

        // ================================================
        // 4. Page Order
        // ================================================
        store.setValue(PluginConstants.Prefs.Print.OVER_THEN_DOWN, radioOverThenDown.getSelection());
        store.setValue(PluginConstants.Prefs.Print.DOWN_THEN_OVER, radioDownThenOver.getSelection());

        DiagramUiPlugin.getDefault().savePreferences();
    }

    private void setUIFromPreferenceStore() {

        IPreferenceStore store = getPreferenceStore();

        // ================================================
        // 1. Orientation
        // ================================================
        boolean bool = store.getBoolean(PluginConstants.Prefs.Print.PORTRAIT);
        radioPortrait.setSelection(bool);

        bool = store.getBoolean(PluginConstants.Prefs.Print.LANDSCAPE);
        radioLandscape.setSelection(bool);

        // ================================================
        // 2. Scaling
        // ================================================
        bool = store.getBoolean(PluginConstants.Prefs.Print.FIT_TO_ONE_PAGE);
        radioFitToOnePage.setSelection(bool);

        bool = store.getBoolean(PluginConstants.Prefs.Print.FIT_TO_ONE_PAGE_WIDE);
        radioFitToOnePageWide.setSelection(bool);

        bool = store.getBoolean(PluginConstants.Prefs.Print.FIT_TO_ONE_PAGE_HIGH);
        radioFitToOnePageHigh.setSelection(bool);

        bool = store.getBoolean(PluginConstants.Prefs.Print.ADJUST_TO_PERCENT);
        radioAdjustToPercentage.setSelection(bool);

        int iPct = store.getInt(PluginConstants.Prefs.Print.SCALING_PERCENTAGE);
        spinScalingPercentage.setSelection(iPct);

        // ================================================
        // 3. Margins
        // ================================================
        double dMargin = store.getDouble(PluginConstants.Prefs.Print.TOP_MARGIN);
        spinTopMargin.setSelection((int)(dMargin * MARGIN_DIVISOR));

        dMargin = store.getDouble(PluginConstants.Prefs.Print.RIGHT_MARGIN);
        spinRightMargin.setSelection((int)(dMargin * MARGIN_DIVISOR));

        dMargin = store.getDouble(PluginConstants.Prefs.Print.BOTTOM_MARGIN);
        spinBottomMargin.setSelection((int)(dMargin * MARGIN_DIVISOR));

        dMargin = store.getDouble(PluginConstants.Prefs.Print.LEFT_MARGIN);
        spinLeftMargin.setSelection((int)(dMargin * MARGIN_DIVISOR));

        // ================================================
        // 4. Page Order
        // ================================================
        bool = store.getBoolean(PluginConstants.Prefs.Print.OVER_THEN_DOWN);
        radioOverThenDown.setSelection(bool);

        bool = store.getBoolean(PluginConstants.Prefs.Print.DOWN_THEN_OVER);
        radioDownThenOver.setSelection(bool);

    }

    private void setUIFromPreferenceDefaults() {

        IPreferenceStore store = getPreferenceStore();

        // ================================================
        // 1. Orientation
        // ================================================
        boolean bool = store.getDefaultBoolean(PluginConstants.Prefs.Print.PORTRAIT);
        radioPortrait.setSelection(bool);

        bool = store.getDefaultBoolean(PluginConstants.Prefs.Print.LANDSCAPE);
        radioLandscape.setSelection(bool);

        // ================================================
        // 2. Scaling
        // ================================================
        bool = store.getDefaultBoolean(PluginConstants.Prefs.Print.FIT_TO_ONE_PAGE);
        radioFitToOnePage.setSelection(bool);

        bool = store.getDefaultBoolean(PluginConstants.Prefs.Print.FIT_TO_ONE_PAGE_WIDE);
        radioFitToOnePageWide.setSelection(bool);

        bool = store.getDefaultBoolean(PluginConstants.Prefs.Print.FIT_TO_ONE_PAGE_HIGH);
        radioFitToOnePageHigh.setSelection(bool);

        bool = store.getDefaultBoolean(PluginConstants.Prefs.Print.ADJUST_TO_PERCENT);
        radioAdjustToPercentage.setSelection(bool);

        int iPct = store.getDefaultInt(PluginConstants.Prefs.Print.SCALING_PERCENTAGE);
        spinScalingPercentage.setSelection(iPct);
        spinScalingPercentage.setEnabled(radioAdjustToPercentage.getSelection());

        // ================================================
        // 3. Margins
        // ================================================
        double dMargin = store.getDefaultDouble(PluginConstants.Prefs.Print.TOP_MARGIN);
        spinTopMargin.setSelection((int)(dMargin * MARGIN_DIVISOR));

        dMargin = store.getDefaultDouble(PluginConstants.Prefs.Print.RIGHT_MARGIN);
        spinRightMargin.setSelection((int)(dMargin * MARGIN_DIVISOR));

        dMargin = store.getDefaultDouble(PluginConstants.Prefs.Print.BOTTOM_MARGIN);
        spinBottomMargin.setSelection((int)(dMargin * MARGIN_DIVISOR));

        dMargin = store.getDefaultDouble(PluginConstants.Prefs.Print.LEFT_MARGIN);
        spinLeftMargin.setSelection((int)(dMargin * MARGIN_DIVISOR));

        // ================================================
        // 4. Page Order
        // ================================================
        bool = store.getDefaultBoolean(PluginConstants.Prefs.Print.OVER_THEN_DOWN);
        radioOverThenDown.setSelection(bool);

        bool = store.getDefaultBoolean(PluginConstants.Prefs.Print.DOWN_THEN_OVER);
        radioDownThenOver.setSelection(bool);

    }

    @Override
    public boolean performOk() {
        setPreferenceStoreFromUI();
        DiagramUiPlugin.updateEditorForPreferences();

        return true;
    }

    @Override
    public void performDefaults() {
        setUIFromPreferenceDefaults();
    }

    @Override
    public void dispose() {
        // Need to check the case where the user "imported" preferences??
        super.dispose();
    }

    @Override
    public boolean performCancel() {
        return super.performCancel();
    }
}
