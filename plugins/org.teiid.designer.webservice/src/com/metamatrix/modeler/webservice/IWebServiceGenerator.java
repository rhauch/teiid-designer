/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.webservice;

import java.util.Collection;
import java.util.List;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xsd.XSDSchema;
import com.metamatrix.metamodels.wsdl.Definitions;


/**
 * Interface for a component that generates a 
 * {@link com.metamatrix.metamodels.webservice.WebServicePackage Web Service model} given
 * a set of {@link Definitions WSDL definitions} and {@link XSDSchema XML Schemas}.
 * @since 4.2
 */
public interface IWebServiceGenerator {
    
    /**
     * Set the Web Service model into which will be generated new web service components,
     * such as {@link com.metamatrix.metamodels.webservice.Interface Interfaces},
     * {@link com.metamatrix.metamodels.webservice.Operation Operations}, etc.
     * @param wsModel the EMF resource; may not be null
     * @see #getWebServiceResource()
     * @since 4.2
     */
    void setWebServiceResource( Resource wsModel);
    
    /**
     * Return the Web Service model into which will be generated new web service components,
     * such as {@link com.metamatrix.metamodels.webservice.Interface Interfaces},
     * {@link com.metamatrix.metamodels.webservice.Operation Operations}, etc.
     * @return the EMF resource for the web service mode
     * @see #setWebServiceResource(Resource)
     * @since 4.2
     */
    Resource getWebServiceResource();
    
    /**
     * Add a {@link Definitions} object that should be used to 
     * {@link #generate(IProgressMonitor) generate} the Web Service model components.
     * @param wsdlDefinitions the WSDL Definitions being added; may not be null
     * @see #addWsdlDefinitions(List)
     * @see #getWsdlDefinitions()
     * @since 4.2
     */
    void addWsdlDefinitions( Definitions wsdlDefinitions );

    /**
     * Add multiple {@link Definitions} objects that should be used to 
     * {@link #generate(IProgressMonitor) generate} the Web Service model components.
     * @param wsdlDefinitions the list of WSDL Definitions being added; may not be null
     * @see #addWsdlDefinitions(WebServiceComponent)
     * @see #getWsdlDefinitions()
     * @since 4.2
     */
    void addWsdlDefinitions( List wsdlDefinitions );

    /**
     * Return the list of {@link WebServiceComponent} objects that will be 
     * {@link #generate(IProgressMonitor) generate} the Web Service model components.
     * @return the List of WebServiceComponent objects used in generation; never null
     * @see #addWsdlDefinitions(Definitions)
     * @see #addWsdlDefinitions(List)
     * @since 4.2
     */
    List getWsdlDefinitions();

    /**
     * Add an {@link XSDSchema XML Schema} that is available when 
     * {@link #generate(IProgressMonitor) generating} the Web Service model components.
     * @param schema the XML Schema; may not be null
     * @see #addXsdSchemas(List)
     * @see #getXsdSchemas()
     * @since 4.2
     */
    void addXsdSchema( XSDSchema schema );

    /**
     * Add multiple {@link XSDSchema XML Schemas} that are available when 
     * {@link #generate(IProgressMonitor) generating} the Web Service model components.
     * @param schemas the list of XSDSchema instances; may not be null
     * @see #addXsdSchema(XSDSchema)
     * @see #getXsdSchemas()
     * @since 4.2
     */
    void addXsdSchemas( List schemas );

    /**
     * Return the list of {@link XSDSchema XML Schemas} that are available when 
     * {@link #generate(IProgressMonitor) generating} the Web Service model components.
     * @return the list of XSDSchema instances; never null
     * @see #addXsdSchema(XSDSchema)
     * @see #addXsdSchemas(List)
     * @since 4.2
     */
    List getXsdSchemas();

    /**
     * Generate the web service objects and place in the {@link #getWebServiceResource() web service model}. 
     * @param monitor the progress monitor; may be null
     * @return the status with any information about the generation process, 
     * will be {@link IStatus#isOK() OK} if the generation was successful and
     * had no warnings or errors; never null
     * @see #generate(IProgressMonitor, List)
     * @since 4.2
     */
    IStatus generate( IProgressMonitor monitor );
    
    /**
     * Generate the web service objects and place in the {@link #getWebServiceResource() web service model}. 
     * @param monitor the progress monitor; may be null
     * @param problems the list into which {@link IStatus problems} about the generation process should be placed;
     * may not be null
     * @see #generate(IProgressMonitor)
     * @since 4.2
     */
    void generate( IProgressMonitor monitor, List problems );
    
    void setSelectedOperations(Collection operations);
    
    Collection getSelectedOperations();
}
