/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.internal.dqp.ui.workspace;

import java.util.ArrayList;
import java.util.Iterator;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.views.properties.IPropertySource;
import org.eclipse.ui.views.properties.IPropertySourceProvider;
import org.teiid.designer.runtime.TeiidTranslator;
import org.teiid.designer.runtime.TranslatorTemplate;
import org.teiid.designer.runtime.connection.SourceConnectionBinding;

import com.metamatrix.modeler.core.workspace.ModelResource;
import com.metamatrix.modeler.core.workspace.ModelWorkspaceException;
import com.metamatrix.modeler.dqp.ui.DqpUiConstants;
import com.metamatrix.modeler.internal.core.workspace.ModelWorkspaceManager;
import com.metamatrix.modeler.internal.ui.properties.ModelPropertySource;

/**
 * @since 4.2
 */
public class RuntimePropertySourceProvider implements IPropertySourceProvider {

    private boolean connectorsEditable = false;

    private ArrayList<IPropertyChangeListener> listenerList = new ArrayList<IPropertyChangeListener>();
    private boolean showExpertProps = false;

    public void addPropertyChangeListener( IPropertyChangeListener listener ) {
        if (!listenerList.contains(listener)) {
            listenerList.add(listener);
        }
    }

    /**
     * @see org.eclipse.ui.views.properties.IPropertySourceProvider#getPropertySource(java.lang.Object)
     * @since 4.2
     */
    public IPropertySource getPropertySource( Object object ) {
        if (object instanceof TranslatorTemplate) {
            ConnectionPropertySource source = new ConnectionPropertySource((TeiidTranslator)object);
            source.setEditable(this.connectorsEditable);
            source.setProvider(this);
            return source;
        } else if (object instanceof TeiidTranslator) {
            ConnectionPropertySource source = new ConnectionPropertySource(new TranslatorTemplate((TeiidTranslator)object));
            source.setEditable(this.connectorsEditable);
            source.setProvider(this);
            return source;
        } else if (object instanceof SourceConnectionBinding) {
            SourceConnectionBinding binding = (SourceConnectionBinding)object;
            // Create the project path
            IPath modelPath = new Path(binding.getModelLocation());
            // append the model name
            modelPath = modelPath.append(binding.getModelName());

            ModelResource mr = ModelWorkspaceManager.getModelWorkspaceManager().getModelWorkspace().findModelResource(modelPath);

            if (mr != null) {
                IFile theResource = null;

                try {
                    theResource = (IFile)mr.getUnderlyingResource();
                } catch (ModelWorkspaceException theException) {
                    DqpUiConstants.UTIL.log(theException);
                }

                if (theResource != null) {
                    return new ModelPropertySource(theResource);
                }
            }

        }
        return null;
    }

    /**
     * Indicates if the expert properties are being shown.
     * 
     * @return <code>true</code> if being shown; <code>false</code> otherwise.
     * @since 5.0.2
     */
    public boolean isShowingExpertProperties() {
        return this.showExpertProps;
    }

    void propertyChanged( PropertyChangeEvent event ) {
        for (Iterator<IPropertyChangeListener> iter = listenerList.iterator(); iter.hasNext();) {
            iter.next().propertyChange(event);
        }
    }

    public void removePropertyChangeListener( IPropertyChangeListener listener ) {
        listenerList.remove(listener);
    }

    /**
     * Sets the editable state for <strong>both</strong> the connector binding and component type property sources.
     * 
     * @param isEditable
     */
    public void setEditable( boolean isEditable ) {
        setEditable(isEditable, isEditable);
    }

    /**
     * @param connectorsEditable the new editable state for the connector binding property source
     * @param connectorTypesEditable the new editable state for the component type property source
     */
    public void setEditable( boolean connectorBindingsEditable,
                             boolean componentTypesEditable ) {
        this.connectorsEditable = connectorBindingsEditable;
    }

    /**
     * Sets if the expert properties should be shown or hidden.
     * 
     * @param theShowFlag a flag indicating if the expert properties should be shown
     * @since 5.0.2
     */
    public void setShowExpertProperties( boolean theShowFlag ) {
        this.showExpertProps = theShowFlag;
    }

}
