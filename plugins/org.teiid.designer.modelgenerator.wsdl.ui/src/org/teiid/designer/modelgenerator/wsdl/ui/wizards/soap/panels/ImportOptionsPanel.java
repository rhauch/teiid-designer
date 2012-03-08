/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.modelgenerator.wsdl.ui.wizards.soap.panels;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.teiid.designer.modelgenerator.wsdl.ui.Messages;
import org.teiid.designer.modelgenerator.wsdl.ui.wizards.soap.OperationsDetailsPage;

import com.metamatrix.modeler.core.ModelerCore;
import com.metamatrix.modeler.core.workspace.ModelResource;
import com.metamatrix.modeler.internal.core.workspace.ModelUtil;
import com.metamatrix.modeler.internal.ui.explorer.ModelExplorerContentProvider;
import com.metamatrix.modeler.internal.ui.explorer.ModelExplorerLabelProvider;
import com.metamatrix.modeler.internal.ui.viewsupport.ModelIdentifier;
import com.metamatrix.modeler.internal.ui.viewsupport.ModelProjectSelectionStatusValidator;
import com.metamatrix.modeler.internal.ui.viewsupport.ModelResourceSelectionValidator;
import com.metamatrix.modeler.internal.ui.viewsupport.ModelWorkspaceViewerFilter;
import com.metamatrix.modeler.modelgenerator.wsdl.ui.internal.util.ModelGeneratorWsdlUiUtil;
import com.metamatrix.modeler.modelgenerator.wsdl.ui.internal.wizards.WSDLImportWizardManager;
import com.metamatrix.modeler.ui.viewsupport.ModelingResourceFilter;
import com.metamatrix.ui.internal.util.WidgetFactory;
import com.metamatrix.ui.internal.util.WidgetUtil;

public class ImportOptionsPanel {
    private Text sourceModelFileText;
    private Text modelContainerText;
    private Text viewModelFileText;
    private IPath modelFileContainerPath;

    final OperationsDetailsPage detailsPage;

    public ImportOptionsPanel(Composite parent, OperationsDetailsPage detailsPage) {
	super();
	this.detailsPage = detailsPage;
	init(parent);
    }

    private void init(Composite parent) {
	Group group = WidgetFactory.createGroup(parent, Messages.ModelsDefinition, GridData.FILL_HORIZONTAL, 1);

	group.setLayout(new GridLayout(3, false));
	Label locationLabel = new Label(group, SWT.NULL);
	locationLabel.setText(Messages.Location);

	this.modelContainerText = new Text(group, SWT.BORDER | SWT.SINGLE);
	GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
	this.modelContainerText.setLayoutData(gridData);
	this.modelContainerText.setBackground(WidgetUtil.getReadOnlyBackgroundColor());
	this.modelContainerText.setForeground(WidgetUtil.getDarkBlueColor());
	this.modelContainerText.setEditable(false);

	Button browseButton = new Button(group, SWT.PUSH);
	gridData = new GridData();
	// buttonGridData.horizontalAlignment = GridData.HORIZONTAL_ALIGN_END;
	browseButton.setLayoutData(gridData);
	browseButton.setText(Messages.BrowseElipsis);
	browseButton.setToolTipText(NLS.bind(Messages.BrowseToSelect_0_Model, Messages.Source_lower));
	browseButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		handleModelLocationBrowse();
	    }
	});

	Label fileLabel = new Label(group, SWT.NULL);
	fileLabel.setText(Messages.Source);
	fileLabel.setToolTipText(Messages.SourceNameTooltip);

	this.sourceModelFileText = new Text(group, SWT.BORDER | SWT.SINGLE);
	gridData = new GridData(GridData.FILL_HORIZONTAL);
	gridData.widthHint = 200;
	this.sourceModelFileText.setLayoutData(gridData);
	this.sourceModelFileText.setToolTipText(Messages.SourceNameTooltip);
	this.sourceModelFileText.setBackground(WidgetUtil.getReadOnlyBackgroundColor());
	this.sourceModelFileText.setForeground(WidgetUtil.getDarkBlueColor());
	this.sourceModelFileText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent e) {
		// Check view file name for existing if "location" is already
		// set
		handleSourceModelTextChanged();
	    }
	});

	browseButton = new Button(group, SWT.PUSH);
	gridData = new GridData();
	browseButton.setLayoutData(gridData);
	browseButton.setText(Messages.BrowseElipsis);
	browseButton.setToolTipText(NLS.bind(Messages.BrowseToSelect_0_Model, Messages.Source_lower));
	browseButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		handleSourceModelBrowse();
	    }
	});

	fileLabel = new Label(group, SWT.NULL);
	fileLabel.setText(Messages.View);
	fileLabel.setToolTipText(Messages.ViewNameTooltip);

	this.viewModelFileText = new Text(group, SWT.BORDER | SWT.SINGLE);
	gridData = new GridData(GridData.FILL_HORIZONTAL);
	this.viewModelFileText.setLayoutData(gridData);
	this.viewModelFileText.setToolTipText(Messages.ViewNameTooltip);
	this.viewModelFileText.setBackground(WidgetUtil.getReadOnlyBackgroundColor());
	this.viewModelFileText.setForeground(WidgetUtil.getDarkBlueColor());
	this.viewModelFileText.addModifyListener(new ModifyListener() {
	    public void modifyText(ModifyEvent e) {
		// Check view file name for existing if "location" is already
		// set
		handleViewModelTextChanged();
	    }
	});

	browseButton = new Button(group, SWT.PUSH);
	gridData = new GridData();
	browseButton.setLayoutData(gridData);
	browseButton.setText(Messages.BrowseElipsis);
	browseButton.setToolTipText(NLS.bind(Messages.BrowseToSelect_0_Model, Messages.View_lower));
	browseButton.addSelectionListener(new SelectionAdapter() {
	    @Override
	    public void widgetSelected(SelectionEvent e) {
		handleViewModelBrowse();
	    }
	});

    }

    public void setVisible() {
	// Set field values from import manager
	this.modelContainerText.setText(getWizardManager().getTargetModelLocation().getFullPath().makeRelative()
	    .toString());
	this.sourceModelFileText.setText(getWizardManager().getSourceModelName());
	this.viewModelFileText.setText(getWizardManager().getTargetViewModelName());
    }

    /**
     * Uses the standard container selection dialog to choose the new value for
     * the container field.
     */
    void handleModelLocationBrowse() {
	final IContainer folder = WidgetUtil.showFolderSelectionDialog(ResourcesPlugin.getWorkspace().getRoot(),
	    new ModelingResourceFilter(), new ModelProjectSelectionStatusValidator());

	if (folder != null && modelContainerText != null) {
	    // viewModelContainerText.setText(folder.getFullPath().makeRelative().toString());
	    getWizardManager().setTargetModelLocation(folder);
	}

	validate();
    }

    void handleSourceModelBrowse() {
	final Object[] selections = WidgetUtil.showWorkspaceObjectSelectionDialog(Messages.SelectSourceModel_title,
	    Messages.SelectSourceModel_message, false, null, virtualModelFilter,
	    new ModelResourceSelectionValidator(false), new ModelExplorerLabelProvider(),
	    new ModelExplorerContentProvider());

	if (selections != null && selections.length == 1 && viewModelFileText != null) {
	    if (selections[0] instanceof IFile) {
		IFile modelFile = (IFile) selections[0];
		IContainer folder = modelFile.getParent();
		String modelName = modelFile.getFullPath().lastSegment();
		getWizardManager().setViewModelExists(true);
		getWizardManager().setTargetModelLocation(folder);
		getWizardManager().setTargetViewModelName(modelName);
	    }
	}

	validate();
    }

    void handleViewModelBrowse() {
	final Object[] selections = WidgetUtil.showWorkspaceObjectSelectionDialog(Messages.SelectViewModel_title,
	    Messages.SelectViewModel_message, false, null, virtualModelFilter,
	    new ModelResourceSelectionValidator(false), new ModelExplorerLabelProvider(),
	    new ModelExplorerContentProvider());

	if (selections != null && selections.length == 1 && viewModelFileText != null) {
	    if (selections[0] instanceof IFile) {
		IFile modelFile = (IFile) selections[0];
		IContainer folder = modelFile.getParent();
		String modelName = modelFile.getFullPath().lastSegment();
		getWizardManager().setViewModelExists(true);
		getWizardManager().setTargetModelLocation(folder);
		getWizardManager().setTargetViewModelName(modelName);
	    }
	}

	validate();
    }

    private WSDLImportWizardManager getWizardManager() {
	return this.detailsPage.getImportManager();
    }

    void handleViewModelTextChanged() {

	String newName = ""; //$NON-NLS-1$
	if (this.viewModelFileText.getText() != null) {
	    if (this.viewModelFileText.getText().length() == 0) {
		getWizardManager().setTargetViewModelName(newName);
		getWizardManager().setViewModelExists(false);
	    } else {
		newName = this.viewModelFileText.getText();
		getWizardManager().setTargetViewModelName(newName);
		getWizardManager().setViewModelExists(viewModelExists());
	    }

	}

	validate();
    }

    void handleSourceModelTextChanged() {

	String newName = ""; //$NON-NLS-1$
	if (this.viewModelFileText.getText() != null) {
	    if (this.viewModelFileText.getText().length() == 0) {
		getWizardManager().setSourceModelName(newName);
		getWizardManager().setSourceModelExists(false);
	    } else {
		newName = this.viewModelFileText.getText();
		getWizardManager().setSourceModelName(newName);
		getWizardManager().setSourceModelExists(sourceModelExists());
	    }

	}

	validate();
    }

    private boolean viewModelExists() {
	if (this.modelFileContainerPath == null) {
	    return false;
	}

	return ModelGeneratorWsdlUiUtil.modelExists(modelFileContainerPath.toOSString(),
	    this.viewModelFileText.getText());
    }

    private boolean sourceModelExists() {
	if (this.modelFileContainerPath == null) {
	    return false;
	}

	return ModelGeneratorWsdlUiUtil.modelExists(modelFileContainerPath.toOSString(),
	    this.sourceModelFileText.getText());
    }

    private void validate() {

    }

    final ViewerFilter virtualModelFilter = new ModelWorkspaceViewerFilter(true) {

	@Override
	public boolean select(final Viewer viewer, final Object parent, final Object element) {
	    boolean doSelect = false;
	    if (element instanceof IResource) {
		// If the project is closed, dont show
		boolean projectOpen = ((IResource) element).getProject().isOpen();
		if (projectOpen) {
		    // Show open projects
		    if (element instanceof IProject) {
			doSelect = true;
		    } else if (element instanceof IContainer) {
			doSelect = true;
			// Show webservice model files, and not .xsd files
		    } else if (element instanceof IFile && ModelUtil.isModelFile((IFile) element)) {
			ModelResource theModel = null;
			try {
			    theModel = ModelUtil.getModelResource((IFile) element, true);
			} catch (Exception ex) {
			    ModelerCore.Util.log(ex);
			}
			if (theModel != null && ModelIdentifier.isRelationalViewModel(theModel)) {
			    doSelect = true;
			}
		    }
		}
	    } else if (element instanceof IContainer) {
		doSelect = true;
	    }

	    return doSelect;
	}
    };

}