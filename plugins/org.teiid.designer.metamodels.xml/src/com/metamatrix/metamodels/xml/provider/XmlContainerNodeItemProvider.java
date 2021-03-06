/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.metamodels.xml.provider;

import java.util.Collection;
import java.util.List;
import org.eclipse.emf.common.notify.AdapterFactory;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.util.ResourceLocator;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.edit.provider.ComposeableAdapterFactory;
import org.eclipse.emf.edit.provider.ItemPropertyDescriptor;
import org.eclipse.emf.edit.provider.ViewerNotification;
import com.metamatrix.metamodels.xml.XmlContainerNode;
import com.metamatrix.metamodels.xml.XmlDocumentFactory;
import com.metamatrix.metamodels.xml.XmlDocumentPackage;

/**
 * This is the item provider adapter for a {@link com.metamatrix.metamodels.xml.XmlContainerNode} object. <!-- begin-user-doc -->
 * <!-- end-user-doc -->
 * 
 * @generated
 */
public class XmlContainerNodeItemProvider extends XmlDocumentEntityItemProvider {
    /**
     * This constructs an instance from a factory and a notifier. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public XmlContainerNodeItemProvider( final AdapterFactory adapterFactory ) {
        super(adapterFactory);
    }

    /**
     * This adds a property descriptor for the Build State feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addBuildStatePropertyDescriptor( final Object object ) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_XmlBuildable_buildState_feature"), //$NON-NLS-1$
                                                                 getString("_UI_PropertyDescriptor_description", "_UI_XmlBuildable_buildState_feature", "_UI_XmlBuildable_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                 XmlDocumentPackage.eINSTANCE.getXmlBuildable_BuildState(),
                                                                 true,
                                                                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Choice Criteria feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected void addChoiceCriteriaPropertyDescriptor( final Object object ) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_ChoiceOption_choiceCriteria_feature"), //$NON-NLS-1$
                                                                 // Start customized code
                                                                 getString("XmlDocumentNodeItemProvider._UI_PropertyDescriptor_description_ChoiceCriteria"), //$NON-NLS-1$ 
                                                                 XmlDocumentPackage.eINSTANCE.getChoiceOption_ChoiceCriteria(),
                                                                 false,
                                                                 // End customized code
                                                                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Choice Criteria feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addChoiceCriteriaPropertyDescriptorGen( final Object object ) { // NO_UCD
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_ChoiceOption_choiceCriteria_feature"), //$NON-NLS-1$
                                                                 getString("_UI_PropertyDescriptor_description", "_UI_ChoiceOption_choiceCriteria_feature", "_UI_ChoiceOption_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                 XmlDocumentPackage.eINSTANCE.getChoiceOption_ChoiceCriteria(),
                                                                 true,
                                                                 ItemPropertyDescriptor.GENERIC_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Choice Order feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected void addChoiceOrderPropertyDescriptor( final Object object ) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_ChoiceOption_choiceOrder_feature"), //$NON-NLS-1$
                                                                 // Start customized code
                                                                 getString("XmlDocumentNodeItemProvider._UI_PropertyDescriptor_description_ChoiceOrder"), //$NON-NLS-1$ 
                                                                 // End customized code
                                                                 XmlDocumentPackage.eINSTANCE.getChoiceOption_ChoiceOrder(),
                                                                 true,
                                                                 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Choice Order feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addChoiceOrderPropertyDescriptorGen( final Object object ) { // NO_UCD
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_ChoiceOption_choiceOrder_feature"), //$NON-NLS-1$
                                                                 getString("_UI_PropertyDescriptor_description", "_UI_ChoiceOption_choiceOrder_feature", "_UI_ChoiceOption_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                 XmlDocumentPackage.eINSTANCE.getChoiceOption_ChoiceOrder(),
                                                                 true,
                                                                 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Default For feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected void addDefaultForPropertyDescriptor( final Object object ) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_ChoiceOption_defaultFor_feature"), //$NON-NLS-1$
                                                                 // Start customized code
                                                                 getString("XmlDocumentNodeItemProvider._UI_PropertyDescriptor_description_DefaultFor"), //$NON-NLS-1$ 
                                                                 // End customized code
                                                                 XmlDocumentPackage.eINSTANCE.getChoiceOption_DefaultFor(),
                                                                 true,
                                                                 null,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Default For feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addDefaultForPropertyDescriptorGen( final Object object ) { // NO_UCD
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_ChoiceOption_defaultFor_feature"), //$NON-NLS-1$
                                                                 getString("_UI_PropertyDescriptor_description", "_UI_ChoiceOption_defaultFor_feature", "_UI_ChoiceOption_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                 XmlDocumentPackage.eINSTANCE.getChoiceOption_DefaultFor(),
                                                                 true,
                                                                 null,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Exclude From Document feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected void addExcludeFromDocumentPropertyDescriptor( final Object object ) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_XmlContainerNode_excludeFromDocument_feature"), //$NON-NLS-1$
                                                                 // Start customized code
                                                                 getString("XmlDocumentNodeItemProvider._UI_PropertyDescriptor_description_ExcludeFromDocument"), //$NON-NLS-1$ 
                                                                 // Start customized code
                                                                 XmlDocumentPackage.eINSTANCE.getXmlContainerNode_ExcludeFromDocument(),
                                                                 true,
                                                                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Exclude From Document feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addExcludeFromDocumentPropertyDescriptorGen( final Object object ) { // NO_UCD
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_XmlContainerNode_excludeFromDocument_feature"), //$NON-NLS-1$
                                                                 getString("_UI_PropertyDescriptor_description", "_UI_XmlContainerNode_excludeFromDocument_feature", "_UI_XmlContainerNode_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                 XmlDocumentPackage.eINSTANCE.getXmlContainerNode_ExcludeFromDocument(),
                                                                 true,
                                                                 ItemPropertyDescriptor.BOOLEAN_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Max Occurs feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected void addMaxOccursPropertyDescriptor( final Object object ) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_XmlContainerNode_maxOccurs_feature"), //$NON-NLS-1$
                                                                 // Start customized code
                                                                 getString("XmlDocumentNodeItemProvider._UI_PropertyDescriptor_description_MaxOccurs"), //$NON-NLS-1$ 
                                                                 // End customized code
                                                                 XmlDocumentPackage.eINSTANCE.getXmlContainerNode_MaxOccurs(),
                                                                 false,
                                                                 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Max Occurs feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addMaxOccursPropertyDescriptorGen( final Object object ) { // NO_UCD
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_XmlContainerNode_maxOccurs_feature"), //$NON-NLS-1$
                                                                 getString("_UI_PropertyDescriptor_description", "_UI_XmlContainerNode_maxOccurs_feature", "_UI_XmlContainerNode_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                 XmlDocumentPackage.eINSTANCE.getXmlContainerNode_MaxOccurs(),
                                                                 false,
                                                                 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Min Occurs feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected void addMinOccursPropertyDescriptor( final Object object ) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_XmlContainerNode_minOccurs_feature"), //$NON-NLS-1$
                                                                 // Start customized code
                                                                 getString("XmlDocumentNodeItemProvider._UI_PropertyDescriptor_description_MinOccurs"), //$NON-NLS-1$ 
                                                                 // End customized code
                                                                 XmlDocumentPackage.eINSTANCE.getXmlContainerNode_MinOccurs(),
                                                                 false,
                                                                 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Min Occurs feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addMinOccursPropertyDescriptorGen( final Object object ) { // NO_UCD
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_XmlContainerNode_minOccurs_feature"), //$NON-NLS-1$
                                                                 getString("_UI_PropertyDescriptor_description", "_UI_XmlContainerNode_minOccurs_feature", "_UI_XmlContainerNode_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                 XmlDocumentPackage.eINSTANCE.getXmlContainerNode_MinOccurs(),
                                                                 false,
                                                                 ItemPropertyDescriptor.INTEGRAL_VALUE_IMAGE,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Xsd Component feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    protected void addXsdComponentPropertyDescriptor( final Object object ) {
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_XmlContainerNode_xsdComponent_feature"), //$NON-NLS-1$
                                                                 // Start customized code
                                                                 getString("XmlDocumentNodeItemProvider._UI_PropertyDescriptor_description_XsdComponent"), //$NON-NLS-1$ 
                                                                 // End customized code
                                                                 XmlDocumentPackage.eINSTANCE.getXmlContainerNode_XsdComponent(),
                                                                 true,
                                                                 null,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds a property descriptor for the Xsd Component feature. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void addXsdComponentPropertyDescriptorGen( final Object object ) { // NO_UCD
        itemPropertyDescriptors.add(createItemPropertyDescriptor(((ComposeableAdapterFactory)adapterFactory).getRootAdapterFactory(),
                                                                 getResourceLocator(),
                                                                 getString("_UI_XmlContainerNode_xsdComponent_feature"), //$NON-NLS-1$
                                                                 getString("_UI_PropertyDescriptor_description", "_UI_XmlContainerNode_xsdComponent_feature", "_UI_XmlContainerNode_type"), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                                                                 XmlDocumentPackage.eINSTANCE.getXmlContainerNode_XsdComponent(),
                                                                 true,
                                                                 null,
                                                                 null,
                                                                 null));
    }

    /**
     * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s describing all of the children that can
     * be created under this object. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    protected void collectNewChildDescriptors( final Collection newChildDescriptors,
                                               final Object object ) {
        super.collectNewChildDescriptors(newChildDescriptors, object);

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlElement()));

        // newChildDescriptors.add
        // (createChildParameter
        // (XmlDocumentPackage.eINSTANCE.getXmlElementHolder_Elements(),
        // XmlDocumentFactory.eINSTANCE.createXmlRoot()));
        //

        // Not supported in 4.0. Per defect 10240
        // newChildDescriptors.add
        // (createChildParameter
        // (XmlDocumentPackage.eINSTANCE.getXmlElementHolder_Elements(),
        // XmlDocumentFactory.eINSTANCE.createXmlFragmentUse()));

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlSequence()));

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlAll()));

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlChoice()));
    }

    /**
     * This adds to the collection of {@link org.eclipse.emf.edit.command.CommandParameter}s describing all of the children that can
     * be created under this object. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    protected void collectNewChildDescriptorsGen( final Collection newChildDescriptors, // NO_UCD
                                                  final Object object ) {
        super.collectNewChildDescriptors(newChildDescriptors, object);

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlElement()));

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlRoot()));

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlFragmentUse()));

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlSequence()));

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlAll()));

        newChildDescriptors.add(createChildParameter(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities(),
                                                     XmlDocumentFactory.eINSTANCE.createXmlChoice()));
    }

    /**
     * <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    protected EStructuralFeature getChildFeature( final Object object,
                                                  final Object child ) {
        // Check the type of the specified child object and return the proper feature to use for
        // adding (see {@link AddCommand}) it as a child.

        return super.getChildFeature(object, child);
    }

    /**
     * This specifies how to implement {@link #getChildren} and is used to deduce an appropriate feature for an
     * {@link org.eclipse.emf.edit.command.AddCommand}, {@link org.eclipse.emf.edit.command.RemoveCommand} or
     * {@link org.eclipse.emf.edit.command.MoveCommand} in {@link #createCommand}. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Collection getChildrenFeatures( final Object object ) {
        if (childrenFeatures == null) {
            super.getChildrenFeatures(object);
            childrenFeatures.add(XmlDocumentPackage.eINSTANCE.getXmlEntityHolder_Entities());
        }
        return childrenFeatures;
    }

    /**
     * This returns XmlContainerNode.gif. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public Object getImage( final Object object ) {
        return getResourceLocator().getImage("full/obj16/XmlContainerNode"); //$NON-NLS-1$
    }

    /**
     * This returns the property descriptors for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated NOT
     */
    @Override
    public List getPropertyDescriptors( final Object object ) {
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

            addChoiceCriteriaPropertyDescriptor(object);
            // addChoiceOrderPropertyDescriptor(object); // RMH defect 12578
            addDefaultForPropertyDescriptor(object);
            addExcludeFromDocumentPropertyDescriptor(object);
            addMinOccursPropertyDescriptor(object);
            addMaxOccursPropertyDescriptor(object);
            addXsdComponentPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * This returns the property descriptors for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    public List getPropertyDescriptorsGen( final Object object ) { // NO_UCD
        if (itemPropertyDescriptors == null) {
            super.getPropertyDescriptors(object);

            addChoiceCriteriaPropertyDescriptor(object);
            addChoiceOrderPropertyDescriptor(object);
            addDefaultForPropertyDescriptor(object);
            addBuildStatePropertyDescriptor(object);
            addExcludeFromDocumentPropertyDescriptor(object);
            addMinOccursPropertyDescriptor(object);
            addMaxOccursPropertyDescriptor(object);
            addXsdComponentPropertyDescriptor(object);
        }
        return itemPropertyDescriptors;
    }

    /**
     * Return the resource locator for this item provider's resources. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public ResourceLocator getResourceLocator() {
        return XmlDocumentEditPlugin.INSTANCE;
    }

    /**
     * This returns the label text for the adapted class. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public String getText( final Object object ) {
        final String label = ((XmlContainerNode)object).getChoiceCriteria();
        return label == null || label.length() == 0 ? getString("_UI_XmlContainerNode_type") : //$NON-NLS-1$
        getString("_UI_XmlContainerNode_type") + " " + label; //$NON-NLS-1$ //$NON-NLS-2$
    }

    /**
     * This handles model notifications by calling {@link #updateChildren} to update any cached children and by creating a viewer
     * notification, which it passes to {@link #fireNotifyChanged}. <!-- begin-user-doc --> <!-- end-user-doc -->
     * 
     * @generated
     */
    @Override
    public void notifyChanged( final Notification notification ) {
        updateChildren(notification);

        switch (notification.getFeatureID(XmlContainerNode.class)) {
            case XmlDocumentPackage.XML_CONTAINER_NODE__CHOICE_CRITERIA:
            case XmlDocumentPackage.XML_CONTAINER_NODE__CHOICE_ORDER:
            case XmlDocumentPackage.XML_CONTAINER_NODE__BUILD_STATE:
            case XmlDocumentPackage.XML_CONTAINER_NODE__EXCLUDE_FROM_DOCUMENT:
            case XmlDocumentPackage.XML_CONTAINER_NODE__MIN_OCCURS:
            case XmlDocumentPackage.XML_CONTAINER_NODE__MAX_OCCURS:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), false, true));
                return;
            case XmlDocumentPackage.XML_CONTAINER_NODE__ENTITIES:
                fireNotifyChanged(new ViewerNotification(notification, notification.getNotifier(), true, false));
                return;
        }
        super.notifyChanged(notification);
    }

}
