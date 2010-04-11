/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.vdb;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import net.jcip.annotations.ThreadSafe;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.Resource.Diagnostic;
import org.teiid.designer.vdb.manifest.ModelElement;
import org.teiid.designer.vdb.manifest.PropertyElement;
import com.metamatrix.metamodels.core.ModelType;
import com.metamatrix.modeler.core.ModelerCore;
import com.metamatrix.modeler.core.container.ResourceFinder;
import com.metamatrix.modeler.internal.core.resource.EmfResource;

/**
 *
 */
@ThreadSafe
public class VdbModelEntry extends VdbEntry {

    private final Vdb vdb;
    private final Set<Diagnostic> errors = new HashSet<Diagnostic>();
    private final Set<Diagnostic> warnings = new HashSet<Diagnostic>();
    private final AtomicBoolean visible = new AtomicBoolean(true);
    private final CopyOnWriteArraySet<VdbModelEntry> dependsUpon = new CopyOnWriteArraySet<VdbModelEntry>();
    private final CopyOnWriteArraySet<VdbModelEntry> dependentOf = new CopyOnWriteArraySet<VdbModelEntry>();
    private final boolean builtIn;
    private final ModelType type;
    private final AtomicReference<String> connector = new AtomicReference();

    VdbModelEntry( final IPath name,
                   final Vdb vdb ) {
        super(name);
        final Resource model = findModel();
        builtIn = (model == null ? false : getFinder().isBuiltInResource(model));
        assert model instanceof EmfResource;
        type = ((EmfResource)model).getModelAnnotation().getModelType();
        this.vdb = vdb;
        synchronizeModelEntry();
    }

    VdbModelEntry( final ModelElement model,
                   final Vdb vdb ) {
        super(model.getName(), model.getPath());
        type = ModelType.get(model.getType());
        visible.set(model.isVisible());
        boolean builtIn = false;
        for (final PropertyElement property : model.getProperties())
            if (PropertyElement.BUILT_IN.equals(property.getName())) {
                builtIn = Boolean.parseBoolean(property.getValue());
                break;
            }
        this.builtIn = builtIn;
        this.vdb = vdb;
        synchronizeModelEntry();
    }

    private void clean() {
        // Clear problems
        errors.clear();
        warnings.clear();
        // Clear set of dependents and inverse relationships
        for (final VdbModelEntry entry : dependsUpon) {
            entry.dependentOf.remove(this);
            if (entry.isBuiltIn()) entry.dispose();
        }
        dependsUpon.clear();
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.teiid.designer.vdb.VdbEntry#dispose()
     */
    @Override
    final void dispose() {
        super.dispose();
        for (final VdbModelEntry entry : dependentOf)
            vdb.removeEntry(entry);
        clean();
    }

    private Resource findModel() {
        final Resource[] models = getFinder().findByName(getName().toString(), true, false);
        if (models.length == 0) return null;
        assert models.length == 1;
        return models[0];
    }

    /**
     * @return connector
     */
    public String getConnector() {
        return connector.get();
    }

    /**
     * @return the immutable set of model entries that depend upon this model entry
     */
    public final Set<VdbModelEntry> getDependentOf() {
        return Collections.unmodifiableSet(dependentOf);
    }

    /**
     * @return the immutable set of model entries upon which this model entry depends
     */
    public final Set<VdbModelEntry> getDependsUpon() {
        return Collections.unmodifiableSet(dependsUpon);
    }

    /**
     * @return the immutable set of errors associated with this model entry
     */
    public final Set<Diagnostic> getErrors() {
        return Collections.unmodifiableSet(errors);
    }

    private ResourceFinder getFinder() {
        try {
            return ModelerCore.getModelContainer().getResourceFinder();
        } catch (final Exception error) {
            throw new RuntimeException(error);
        }
    }

    /**
     * @return type
     */
    public final ModelType getType() {
        return type;
    }

    /**
     * @return the immutable set of warnings associated with this model entry
     */
    public final Set<Diagnostic> getWarnings() {
        return Collections.unmodifiableSet(warnings);
    }

    /**
     * @return <code>true</code> if the associated model is a hidden built-in model.
     */
    public final boolean isBuiltIn() {
        return builtIn;
    }

    /**
     * @return <code>true</code> if the associated model will be directly accessible to users.
     */
    public final boolean isVisible() {
        return visible.get();
    }

    /**
     * @param connector Sets connector to the specified value.
     */
    public void setConnector( final String connector ) {
        this.connector.set(connector);
    }

    /**
     * @param visible <code>true</code> if the associated model will be directly accessible to users.
     */
    public final void setVisible( final boolean visible ) {
        this.visible.set(visible);
    }

    /**
     * {@inheritDoc}
     * 
     * @see org.teiid.designer.vdb.VdbEntry#synchronize()
     */
    @Override
    public final boolean synchronize() {
        if (!super.synchronize()) return false;
        return synchronizeModelEntry();
    }

    private boolean synchronizeModelEntry() {
        final Resource model = findModel();
        // Return if resource to synchronize on doesn't exist
        clean();
        // Synchronize model problems
        errors.addAll(model.getErrors());
        warnings.addAll(model.getWarnings());
        // TODO: get ref to index
        // Also add dependent models
        for (final Resource dependentModel : getFinder().findReferencesFrom(model, true, false)) {
            final String name = dependentModel.getURI().toString();
            VdbModelEntry dependentEntry = null;
            for (final VdbModelEntry existingEntry : vdb.getModelEntries())
                if (name.equals(existingEntry.getName().toString())) {
                    dependentEntry = existingEntry;
                    break;
                }
            if (dependentEntry == null) dependentEntry = vdb.addModelEntry(Path.fromOSString(name));
            dependsUpon.add(dependentEntry);
            dependentEntry.dependentOf.add(this);
        }
        return true;
    }
}