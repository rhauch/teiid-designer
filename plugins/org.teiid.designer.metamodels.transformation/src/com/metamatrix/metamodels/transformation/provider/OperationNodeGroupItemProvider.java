/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.transformation.provider;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.edit.provider.ViewerNotification;
import com.metamatrix.metamodels.transformation.OperationNodeGroup;
import com.metamatrix.metamodels.transformation.TransformationFactory;
import com.metamatrix.metamodels.transformation.TransformationPackage;

/**
 * This is the item provider adapter for a {@link com.metamatrix.metamodels.transformation.OperationNodeGroup} object. <!--
 * begin-user-doc --> <!-- end-user-doc -->
 * 
 * @generated
 */
public class OperationNodeGroupItemProvider extends AbstractOperationNodeItemProvider {

    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public OperationNodeGroupItemProvider( AdapterFactory adapterFactory ) {
        super(adapterFactory);
    }

    /**
     * This returns the property descriptors for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public List getPropertyDescriptors( Object object ) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

        }
        return itemPropertyDescriptors;
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Collection getChildrenFeatures( Object object ) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents());
        }
        return childrenFeatures;
    }

    /**
     * This returns OperationNodeGroup.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object getImage( Object object ) {
        return getResourceLocator().getImage("full/obj16/OperationNodeGroup"); //$NON-NLS-1$
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getText( Object object ) {
        String label = ((OperationNodeGroup)object).getName();
        return label == null || label.length() == 0 ? getString("_UI_OperationNodeGroup_type") : //$NON-NLS-1$
        getString("_UI_OperationNodeGroup_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached children and by creating a viewer
     * notification, which it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void notifyChanged( Notification notification ) {
        updateChildren(notification);

        switch (notification.getFeatureID(OperationNodeGroup.class)) {
            case TransformationPackage.OPERATION_NODE_GROUP__CONTENTS:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

    /**
     * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s describing all of the children that
     * can be created under this object. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected void collectNewChildDescriptors( Collection newChildDescriptors,
                                               Object object ) {
        super.collectNewChildDescriptors(newChildDescriptors, object);

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createOperationNodeGroup()));

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createOperationNode()));

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createJoinNode()));

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createUnionNode()));

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createProjectionNode()));

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createFilterNode()));

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createGroupingNode()));

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createDupRemovalNode()));

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createSortNode()));

        newChildDescriptors.add(createChildParameter(TransformationPackage.eINSTANCE.getOperationNodeGroup_Contents(),
                                                     TransformationFactory.eINSTANCE.createSqlNode()));
    }

    /**
     * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        return TransformationEditPlugin.INSTANCE;
    }

}
