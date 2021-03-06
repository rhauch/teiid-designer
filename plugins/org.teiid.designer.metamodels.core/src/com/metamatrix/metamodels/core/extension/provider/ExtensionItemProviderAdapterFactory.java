/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.core.extension.provider;

import java.util.ArrayList;
import java.util.Collection;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.edit.provider.ChangeNotifier;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ComposedAdapterFactory;
import org.eclipse.emf.edit.provider.IChangeNotifier;
import org.eclipse.emf.edit.provider.IDisposable;
import org.eclipse.emf.edit.provider.IEditingDomainItemProvider;
import org.eclipse.emf.edit.provider.IItemLabelProvider;
import org.eclipse.emf.edit.provider.IItemPropertySource;
import org.eclipse.emf.edit.provider.INotifyChangedListener;
import org.eclipse.emf.edit.provider.IStructuredItemContentProvider;
import org.eclipse.emf.edit.provider.ITreeItemContentProvider;
import com.metamatrix.metamodels.core.extension.util.ExtensionAdapterFactory;

/**
 * This is the factory that is used to provide the interfaces needed to support Viewers.
 * The adapters generated by this factory convert EMF adapter notifications into calls to {@link #fireNotifyChanged fireNotifyChanged}.
 * The adapters also support Eclipse property sheets.
 * Note that most of the adapters are shared among multiple instances.
 * <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * @generated
 */
public class ExtensionItemProviderAdapterFactory extends ExtensionAdapterFactory implements ComposeableAdapterFactory, IChangeNotifier, IDisposable {
    /**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final String copyright = "See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing."; //$NON-NLS-1$

	/**
	 * This keeps track of the root adapter factory that delegates to this adapter factory.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected ComposedAdapterFactory parentAdapterFactory;

    /**
	 * This is used to implement {@link org.eclipse.emf.edit.provider.IChangeNotifier}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected IChangeNotifier changeNotifier = new ChangeNotifier();

    /**
	 * This keeps track of all the supported types checked by {@link #isFactoryForType isFactoryForType}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected Collection supportedTypes = new ArrayList();

    /**
	 * This constructs an instance.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ExtensionItemProviderAdapterFactory() {
		supportedTypes.add(IStructuredItemContentProvider.class);
		supportedTypes.add(ITreeItemContentProvider.class);
		supportedTypes.add(IItemPropertySource.class);
		supportedTypes.add(IEditingDomainItemProvider.class);
		supportedTypes.add(IItemLabelProvider.class);
	}

    /**
	 * This keeps track of the one adapter used for all {@link com.metamatrix.metamodels.core.extension.XClass} instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected XClassItemProvider xClassItemProvider;

    /**
	 * This creates an adapter for a {@link com.metamatrix.metamodels.core.extension.XClass}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Adapter createXClassAdapter() {
		if (xClassItemProvider == null) {
			xClassItemProvider = new XClassItemProvider(this);
		}

		return xClassItemProvider;
	}

    /**
	 * This keeps track of the one adapter used for all {@link com.metamatrix.metamodels.core.extension.XPackage} instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected XPackageItemProvider xPackageItemProvider;

    /**
	 * This creates an adapter for a {@link com.metamatrix.metamodels.core.extension.XPackage}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Adapter createXPackageAdapter() {
		if (xPackageItemProvider == null) {
			xPackageItemProvider = new XPackageItemProvider(this);
		}

		return xPackageItemProvider;
	}

    /**
	 * This keeps track of the one adapter used for all {@link com.metamatrix.metamodels.core.extension.XAttribute} instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected XAttributeItemProvider xAttributeItemProvider;

    /**
	 * This creates an adapter for a {@link com.metamatrix.metamodels.core.extension.XAttribute}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Adapter createXAttributeAdapter() {
		if (xAttributeItemProvider == null) {
			xAttributeItemProvider = new XAttributeItemProvider(this);
		}

		return xAttributeItemProvider;
	}

    /**
	 * This keeps track of the one adapter used for all {@link com.metamatrix.metamodels.core.extension.XEnum} instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected XEnumItemProvider xEnumItemProvider;

    /**
	 * This creates an adapter for a {@link com.metamatrix.metamodels.core.extension.XEnum}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Adapter createXEnumAdapter() {
		if (xEnumItemProvider == null) {
			xEnumItemProvider = new XEnumItemProvider(this);
		}

		return xEnumItemProvider;
	}

    /**
	 * This keeps track of the one adapter used for all {@link com.metamatrix.metamodels.core.extension.XEnumLiteral} instances.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected XEnumLiteralItemProvider xEnumLiteralItemProvider;

    /**
	 * This creates an adapter for a {@link com.metamatrix.metamodels.core.extension.XEnumLiteral}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Adapter createXEnumLiteralAdapter() {
		if (xEnumLiteralItemProvider == null) {
			xEnumLiteralItemProvider = new XEnumLiteralItemProvider(this);
		}

		return xEnumLiteralItemProvider;
	}

    /**
	 * This returns the root adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public ComposeableAdapterFactory getRootAdapterFactory() {
		return parentAdapterFactory == null ? this : parentAdapterFactory.getRootAdapterFactory();
	}

    /**
	 * This sets the composed adapter factory that contains this factory.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void setParentAdapterFactory(ComposedAdapterFactory parentAdapterFactory) {
		this.parentAdapterFactory = parentAdapterFactory;
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public boolean isFactoryForType(Object type) {
		return supportedTypes.contains(type) || super.isFactoryForType(type);
	}

    /**
	 * This implementation substitutes the factory itself as the key for the adapter.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Adapter adapt(Notifier notifier, Object type) {
		return super.adapt(notifier, this);
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    public Object adapt(Object object, Object type) {
		if (isFactoryForType(type)) {
			Object adapter = super.adapt(object, type);
			if (!(type instanceof Class) || (((Class)type).isInstance(adapter))) {
				return adapter;
			}
		}

		return null;
	}

    /**
	 * This adds a listener.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void addListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.addListener(notifyChangedListener);
	}

    /**
	 * This removes a listener.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void removeListener(INotifyChangedListener notifyChangedListener) {
		changeNotifier.removeListener(notifyChangedListener);
	}

    /**
	 * This delegates to {@link #changeNotifier} and to {@link #parentAdapterFactory}.
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    public void fireNotifyChanged(Notification notification) {
		changeNotifier.fireNotifyChanged(notification);

		if (parentAdapterFactory != null) {
			parentAdapterFactory.fireNotifyChanged(notification);
		}
	}

				/**
	 * This disposes all of the item providers created by this factory. 
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void dispose() {
		if (xClassItemProvider != null) xClassItemProvider.dispose();
		if (xPackageItemProvider != null) xPackageItemProvider.dispose();
		if (xAttributeItemProvider != null) xAttributeItemProvider.dispose();
		if (xEnumItemProvider != null) xEnumItemProvider.dispose();
		if (xEnumLiteralItemProvider != null) xEnumLiteralItemProvider.dispose();
	}

}
