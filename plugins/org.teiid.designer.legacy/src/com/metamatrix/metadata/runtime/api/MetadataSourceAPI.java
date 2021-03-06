/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */

package com.metamatrix.metadata.runtime.api;

import java.util.Collection;
import java.util.List;
import com.metamatrix.metadata.runtime.exception.VirtualDatabaseDoesNotExistException;

/**
 * The DataAccessAPI is the common interface to access runtime metadata.
 */
public interface MetadataSourceAPI {

    /**
     * Returns <code>true</code> if the metadata for the groups and elements have been loaded. The loading of the details are only
     * used when the metadata tree is beind displayed. So to cut down on the overhead when it's never been asked for, the details
     * are not loaded when only the models are needed by query processing.
     * 
     * @return true if the metadata details have been loaded.
     * @since 4.2
     */
    boolean isModelDetailsLoaded();

    /**
     * Returns the <code>VirtualDatabase</code> based on the virtual database id.
     * 
     * @throws VirtualDatabaseException if an error occurs while trying to read the data.
     * @return VirtualDatabase
     */
    VirtualDatabase getVirtualDatabase() throws VirtualDatabaseException;

    /**
     * Returns the <code>VirtualDatabaseID</code>. This method does validate the existance of the virtual database by reading from
     * the persistance storage before creating the id.
     * 
     * @throws VirtualDatabaseDoesNotExistException exception if the virtual database does not exist
     * @throws VirtualDatabaseException if an error occurs while trying to read the data.
     * @return VirtualDatabaseID
     */
    VirtualDatabaseID getVirtualDatabaseID() throws VirtualDatabaseDoesNotExistException, VirtualDatabaseException;

    /**
     * Returns a <code>Collection</code> of <code>Model</code>s that represents all the models
     * 
     * @return Collection of type Model
     * @throws VirtualDatabaseException an error occurs while trying to read the data.
     */
    Collection getAllModels() throws VirtualDatabaseException;

    /**
     * Returns a <code>Collection</code> of type <code>Model</code> that represents all the models that are considered displayable
     * to the console
     * 
     * @param vdbID is the VirtualDatabaseID
     * @return Collection of type Model
     * @throws VirtualDatabaseException an error occurs while trying to read the data.
     */
    Collection getDisplayableModels() throws VirtualDatabaseException;

    /**
     * Returns the visibility for a resource path that exist this vdb
     * 
     * @param resourcePath
     * @return <code>true</code> if the resource is visible.
     * @since 4.2
     */
    boolean isVisible( String resourcePath );

    /**
     * Returns a <code>Collection</code> of type <code>Model</code> that represents all the models that are either visible or not
     * visible
     * 
     * @return Collection of type Model
     * @throws VirtualDatabaseException an error occurs while trying to read the data.
     */
    Collection getModelsForVisibility( boolean isVisible ) throws VirtualDatabaseException;

    /**
     * return the specified model
     * 
     * @param modelID is the unique id for the model
     * @return Model
     * @throws VirtualDatabaseException an error occurs while trying to read the data.
     */
    Model getModel( ModelID modelID ) throws VirtualDatabaseException;

    /**
     * Returns an ordered <code>List</code> of type <code>Element</code> that are contained within the specified key id.
     * 
     * @param groupID is the group from which the elements are contained
     * @param vdbID is the VirtualDatabaseID
     * @return List of type Element
     * @throws InvalidRuntimeIDException if the group does not exist
     * @throws VirtualDatabaseTransactionException an error occurs while trying to read the data.
     */
    List getElementsInGroup( GroupID groupID ) throws VirtualDatabaseException;

    /**
     * /** Returns a <code>Collection</code> of type <code>Procedure</code> for the specified model id.
     * 
     * @param modelName is the name of the Model
     * @return Collection of type Procedure
     * @throws VirtualDatabaseException an error occurs while trying to read the data.
     */
    Collection getProceduresInModel( ModelID modelID ) throws VirtualDatabaseException;

    Collection getGroupsInModel( ModelID modelID ) throws VirtualDatabaseException;

}
