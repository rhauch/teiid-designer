/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */

package com.metamatrix.modeler.transformation.metadata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import org.teiid.api.exception.query.QueryMetadataException;
import org.teiid.core.TeiidComponentException;
import org.teiid.core.TeiidException;
import org.teiid.core.TeiidRuntimeException;
import org.teiid.core.id.UUID;
import org.teiid.core.types.DataTypeManager;
import org.teiid.core.util.ArgCheck;
import org.teiid.core.util.Assertion;
import org.teiid.core.util.StringUtil;
import org.teiid.query.mapping.relational.QueryNode;
import org.teiid.query.mapping.xml.MappingDocument;
import org.teiid.query.mapping.xml.MappingLoader;
import org.teiid.query.mapping.xml.MappingNode;
import org.teiid.query.metadata.BasicQueryMetadata;
import org.teiid.query.metadata.StoredProcedureInfo;
import org.teiid.query.metadata.SupportConstants;
import org.teiid.query.sql.lang.SPParameter;
import com.metamatrix.core.index.CompositeIndexSelector;
import com.metamatrix.core.index.IEntryResult;
import com.metamatrix.core.index.RuntimeIndexSelector;
import com.metamatrix.core.index.SimpleIndexUtil;
import com.metamatrix.core.util.ModelType;
import com.metamatrix.internal.core.index.Index;
import com.metamatrix.metadata.runtime.RuntimeMetadataPlugin;
import com.metamatrix.metadata.runtime.impl.RecordFactory;
import com.metamatrix.modeler.core.index.IndexConstants;
import com.metamatrix.modeler.core.index.IndexSelector;
import com.metamatrix.modeler.core.metadata.runtime.ColumnRecord;
import com.metamatrix.modeler.core.metadata.runtime.ColumnSetRecord;
import com.metamatrix.modeler.core.metadata.runtime.DatatypeRecord;
import com.metamatrix.modeler.core.metadata.runtime.ForeignKeyRecord;
import com.metamatrix.modeler.core.metadata.runtime.MetadataConstants;
import com.metamatrix.modeler.core.metadata.runtime.MetadataRecord;
import com.metamatrix.modeler.core.metadata.runtime.ModelRecord;
import com.metamatrix.modeler.core.metadata.runtime.ProcedureParameterRecord;
import com.metamatrix.modeler.core.metadata.runtime.ProcedureRecord;
import com.metamatrix.modeler.core.metadata.runtime.PropertyRecord;
import com.metamatrix.modeler.core.metadata.runtime.TableRecord;
import com.metamatrix.modeler.core.metadata.runtime.TransformationRecord;
import com.metamatrix.modeler.core.metadata.runtime.VdbRecord;
import com.metamatrix.modeler.core.util.ColumnRecordComparator;
import com.metamatrix.modeler.internal.transformation.util.UuidUtil;

/**
 * Modelers implementation of QueryMetadataInterface that reads columns, groups, models etc. index files for various metadata
 * properties.
 */
public abstract class TransformationMetadata extends BasicQueryMetadata {

    // Fix Me: The following constants come from com.metamatrix.metamodels.relational.NullableType
    private static int NULLABLE = 1;
    private static int NULLABLE_UNKNOWN = 2;
    // Fix Me: The following constants come from com.metamatrix.metamodels.relational.SearchabilityType
    private static int SEARCHABLE = 0;
    private static int ALL_EXCEPT_LIKE = 1;
    private static int LIKE_ONLY = 2;

    /** Delimiter character used when specifying fully qualified entity names */
    public static final char DELIMITER_CHAR = IndexConstants.NAME_DELIM_CHAR;
    public static final String DELIMITER_STRING = StringUtil.Constants.EMPTY_STRING + IndexConstants.NAME_DELIM_CHAR;

    public static ColumnRecordComparator columnComparator = new ColumnRecordComparator();

    // error message cached to avaid i18n lookup each time
    public static String NOT_EXISTS_MESSAGE = StringUtil.Constants.SPACE
                                              + RuntimeMetadataPlugin.Util.getString("TransformationMetadata.does_not_exist._1"); //$NON-NLS-1$

    // context object all the info needed for metadata lookup
    private final QueryMetadataContext context;

    // ==================================================================================
    // C O N S T R U C T O R S
    // ==================================================================================

    /**
     * TransformationMetadata constructor
     * 
     * @param context Object containing the info needed to lookup metadta.
     */
    protected TransformationMetadata( final QueryMetadataContext context ) {
        ArgCheck.isNotNull(context);
        this.context = context;
    }

    // ==================================================================================
    // I N T E R F A C E M E T H O D S
    // ==================================================================================

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getElementID(java.lang.String)
     */
    @Override
    public Object getElementID( final String elementName ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isNotEmpty(elementName);

        // elementfull names always contain atlest 3 segments(modelname.groupName.elementName)
        if (StringUtil.startsWithIgnoreCase(elementName, UUID.PROTOCOL)
            || StringUtil.getTokens(elementName, DELIMITER_STRING).size() >= 3) {
            // Query the index files
            return getRecordByType(elementName, IndexConstants.RECORD_TYPE.COLUMN);
        }
        throw new QueryMetadataException(elementName + NOT_EXISTS_MESSAGE);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getGroupID(java.lang.String)
     */
    @Override
    public Object getGroupID( final String groupName ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isNotEmpty(groupName);

        // groupfull names always contain atlest 2 segments(modelname.groupName)
        if (StringUtil.startsWithIgnoreCase(groupName, UUID.PROTOCOL)
            || StringUtil.getTokens(groupName, DELIMITER_STRING).size() >= 2) {
            // Query the index files
            return getRecordByType(groupName, IndexConstants.RECORD_TYPE.TABLE);
        }
        throw new QueryMetadataException(groupName + NOT_EXISTS_MESSAGE);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getGroupsForPartialName(java.lang.String)
     */
    @Override
    public Collection getGroupsForPartialName( final String partialGroupName )
        throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isNotEmpty(partialGroupName);

        Collection tableRecords = null;

        String partialName = partialGroupName;
        // if it the group is a UUID
        if (!StringUtil.startsWithIgnoreCase(partialGroupName, UUID.PROTOCOL)) {
            // Prepend a "." so only match full part names
            partialName = DELIMITER_CHAR + partialGroupName;
        }

        // Query the index files
        tableRecords = findMetadataRecords(IndexConstants.RECORD_TYPE.TABLE, partialName, true);

        // Extract the fully qualified names to return
        final Collection tableNames = new ArrayList(tableRecords.size());
        for (Iterator recordIter = tableRecords.iterator(); recordIter.hasNext();) {
            // get the table record for this result
            TableRecord tableRecord = (TableRecord)recordIter.next();
            tableNames.add(getFullName(tableRecord));
        }
        return tableNames;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getModelID(java.lang.Object)
     */
    @Override
    public Object getModelID( final Object groupOrElementID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(MetadataRecord.class, groupOrElementID);
        MetadataRecord metadataRecord = (MetadataRecord)groupOrElementID;

        // get modelName
        String modelName = metadataRecord.getModelName();

        // Query the index files
        return getRecordByType(modelName, IndexConstants.RECORD_TYPE.MODEL);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getFullName(java.lang.Object)
     */
    @Override
    public String getFullName( final Object metadataID ) {
        ArgCheck.isInstanceOf(MetadataRecord.class, metadataID);
        MetadataRecord metadataRecord = (MetadataRecord)metadataID;
        return metadataRecord.getFullName();
    }
    
    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getShortElementName(java.lang.String)
     */
    public String getShortElementName( final String fullElementName ) {
        ArgCheck.isNotEmpty(fullElementName);
        if (UuidUtil.isStringifiedUUID(fullElementName)) {
            return UuidUtil.stripPrefixFromUUID(fullElementName);
        }
        int index = fullElementName.lastIndexOf(DELIMITER_CHAR);
        if (index >= 0) {
            return fullElementName.substring(index + 1);
        }
        return fullElementName;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getElementIDsInGroupID(java.lang.Object)
     */
    @Override
    public List getElementIDsInGroupID( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;

        // Query the index files
        final String groupName = tableRecord.getFullName();
        final String groupUUID = tableRecord.getUUID();
        Assertion.isNotNull(groupUUID);
        final Collection results = findChildRecords(tableRecord, IndexConstants.RECORD_TYPE.COLUMN);
        if (results.isEmpty()) {
            throw new QueryMetadataException(
                                             RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Group(0}_does_not_have_elements", groupName)); //$NON-NLS-1$
        }

        List columnRecords = new ArrayList(results);

        // Sort the column records according to their positions
        Collections.sort(columnRecords, columnComparator);
        return columnRecords;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getGroupIDForElementID(java.lang.Object)
     */
    @Override
    public Object getGroupIDForElementID( final Object elementID ) throws TeiidComponentException, QueryMetadataException {
        if (elementID instanceof ColumnRecord) {
            ColumnRecord columnRecord = (ColumnRecord)elementID;
            String tableUUID = columnRecord.getParentUUID();
            return this.getGroupID(tableUUID);
        } else if (elementID instanceof ProcedureParameterRecord) {
            ProcedureParameterRecord columnRecord = (ProcedureParameterRecord)elementID;
            String tableUUID = columnRecord.getParentUUID();
            return this.getGroupID(tableUUID);
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getStoredProcedureInfoForProcedure(java.lang.String)
     */
    @Override
    public StoredProcedureInfo getStoredProcedureInfoForProcedure( final String procedureName )
        throws TeiidComponentException, QueryMetadataException {
        StoredProcedureInfo result = getStoredProcInfoDirect(procedureName);
        if (result == null) {
            throw new QueryMetadataException(procedureName + NOT_EXISTS_MESSAGE);
        }
        return result;
    }
    
    public StoredProcedureInfo getStoredProcInfoDirect( final String procedureName )
        throws TeiidComponentException, QueryMetadataException {

        ArgCheck.isNotEmpty(procedureName);

        ProcedureRecord procRecord = null;
        
        // procedure full names always contain atlest 2 segments(modelname.procedureName)
        if (StringUtil.startsWithIgnoreCase(procedureName, UUID.PROTOCOL)
            || StringUtil.getTokens(procedureName, DELIMITER_STRING).size() >= 2) {

            final Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.CALLABLE, procedureName, false);

            int resultSize = results.size();
            if (resultSize == 1) {
                // get the columnset record for this result
                procRecord = (ProcedureRecord)results.iterator().next();
            } else if (resultSize == 0) {
                if (StringUtil.startsWithIgnoreCase(procedureName, UUID.PROTOCOL)) {
                    return null;
                }
            } else {
                // there should be only one for the full name
                throw new QueryMetadataException(RuntimeMetadataPlugin.Util.getString("TransformationMetadata.0", procedureName)); //$NON-NLS-1$
            }
        }
        
        if (procRecord == null) {
        
            String partialName = DELIMITER_CHAR + procedureName;
    
            final Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.CALLABLE, partialName, true);
            
            int resultSize = results.size();
            if (resultSize == 1) {
                // get the columnset record for this result
                procRecord = (ProcedureRecord)results.iterator().next();
            } else if (resultSize == 0) {
                return null;
            } else {
                // there should be only one for the UUID
                throw new QueryMetadataException(RuntimeMetadataPlugin.Util.getString("TransformationMetadata.0", procedureName)); //$NON-NLS-1$
            }
        }

        String procedureFullName = procRecord.getFullName();

        // create the storedProcedure info object that would hold procedure's metadata
        StoredProcedureInfo procInfo = new StoredProcedureInfo();
        procInfo.setProcedureCallableName(procedureFullName);
        procInfo.setProcedureID(procRecord);

        // modelID for the procedure
        MetadataRecord modelRecord = (MetadataRecord)this.getModelID(procRecord);
        procInfo.setModelID(modelRecord);

        // get the parameter metadata info
        for (Iterator paramIter = procRecord.getParameterIDs().iterator(); paramIter.hasNext();) {
            String paramID = (String)paramIter.next();
            ProcedureParameterRecord paramRecord = (ProcedureParameterRecord)this.getRecordByType(paramID,
                                                                                                  IndexConstants.RECORD_TYPE.CALLABLE_PARAMETER);
            String runtimeType = paramRecord.getRuntimeType();
            int direction = this.convertParamRecordTypeToStoredProcedureType(paramRecord.getType());
            // create a parameter and add it to the procedure object
            SPParameter spParam = new SPParameter(paramRecord.getPosition(), direction, paramRecord.getFullName());
            spParam.setMetadataID(paramRecord);
            spParam.setClassType(DataTypeManager.getDataTypeClass(runtimeType));
            procInfo.addParameter(spParam);
        }

        // if the procedure returns a resultSet, obtain resultSet metadata
        String resultID = (String)procRecord.getResultSetID();
        if (resultID != null) {
            try {
                ColumnSetRecord resultRecord = (ColumnSetRecord)this.getRecordByType(resultID,
                                                                                     IndexConstants.RECORD_TYPE.RESULT_SET);
                // resultSet is the last parameter in the procedure
                int lastParamIndex = procInfo.getParameters().size() + 1;
                SPParameter param = new SPParameter(lastParamIndex, SPParameter.RESULT_SET, resultRecord.getFullName());
                param.setClassType(java.sql.ResultSet.class);
                param.setMetadataID(resultRecord);

                ColumnRecord[] columnRecords = getColumnRecordsForUUIDs(resultRecord.getColumnIDs());
                for (int i = 0; i < columnRecords.length; i++) {
                    String colType = columnRecords[i].getRuntimeType();
                    param.addResultSetColumn(columnRecords[i].getFullName(),
                                             DataTypeManager.getDataTypeClass(colType),
                                             columnRecords[i]);
                }

                procInfo.addParameter(param);
            } catch (QueryMetadataException e) {
                // it is ok to fail here. it will happen when a
                // virtual stored procedure is created from a
                // physical stored procedrue without a result set
                // TODO: find a better fix for this
            }
        }

        // if this is a virtual procedure get the procedure plan
        if (procRecord.isVirtual()) {
            String procedurePlan = getProcedurePlan(procedureFullName);
            if (procedurePlan != null) {
                QueryNode queryNode = new QueryNode(procedurePlan);
                procInfo.setQueryPlan(queryNode);
            }
        }

        // subtract 1, to match up with the server
        procInfo.setUpdateCount(procRecord.getUpdateCount() - 1);

        return procInfo;
    }

    /**
     * Method to convert the parameter type returned from a ProcedureParameterRecord to the parameter type expected by
     * StoredProcedureInfo
     * 
     * @param parameterType
     * @return
     */
    private int convertParamRecordTypeToStoredProcedureType( final int parameterType ) {
        switch (parameterType) {
            case MetadataConstants.PARAMETER_TYPES.IN_PARM:
                return SPParameter.IN;
            case MetadataConstants.PARAMETER_TYPES.OUT_PARM:
                return SPParameter.OUT;
            case MetadataConstants.PARAMETER_TYPES.INOUT_PARM:
                return SPParameter.INOUT;
            case MetadataConstants.PARAMETER_TYPES.RETURN_VALUE:
                return SPParameter.RETURN_VALUE;
            case MetadataConstants.PARAMETER_TYPES.RESULT_SET:
                return SPParameter.RESULT_SET;
            default:
                return -1;
        }
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getElementType(java.lang.Object)
     */
    @Override
    public String getElementType( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getRuntimeType();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return ((ProcedureParameterRecord)elementID).getRuntimeType();
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getDefaultValue(java.lang.String)
     */
    @Override
    public Object getDefaultValue( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getDefaultValue();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return ((ProcedureParameterRecord)elementID).getDefaultValue();
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    @Override
    public Object getMinimumValue( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getMinValue();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return null;
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    @Override
    public Object getMaximumValue( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getMaxValue();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return null;
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#isVirtualGroup(java.lang.Object)
     */
    @Override
    public boolean isVirtualGroup( final Object groupID ) {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        return ((TableRecord)groupID).isVirtual();
    }

    /**
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#isProcedureInputElement(java.lang.Object)
     * @since 4.2
     */
    @Override
    public boolean isProcedure( final Object groupID ) {
        if (groupID instanceof ProcedureRecord) {
            return true;
        }
        if (groupID instanceof TableRecord) {
            return false;
        }
        throw createInvalidRecordTypeException(groupID);
    }

    @Override
    public boolean isVirtualModel( final Object modelID ) {
        ArgCheck.isInstanceOf(ModelRecord.class, modelID);
        ModelRecord modelRecord = (ModelRecord)modelID;
        return (modelRecord.getModelType() == ModelType.VIRTUAL);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getVirtualPlan(java.lang.Object)
     */
    @Override
    public QueryNode getVirtualPlan( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);

        TableRecord tableRecord = (TableRecord)groupID;
        final String groupName = tableRecord.getFullName();
        if (tableRecord.isVirtual()) {
            // Query the index files
            Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.SELECT_TRANSFORM, groupName, false);
            // this group may be an xml document
            if (results.isEmpty()) {
                results = findMetadataRecords(IndexConstants.RECORD_TYPE.MAPPING_TRANSFORM, groupName, false);
            }
            int resultSize = results.size();
            if (resultSize == 1) {
                // get the transform record for this result
                final TransformationRecord transformRecord = (TransformationRecord)results.iterator().next();

                String transQuery = transformRecord.getTransformation();
                QueryNode queryNode = new QueryNode(transQuery);

                // get any bindings and add them onto the query node
                List bindings = transformRecord.getBindings();
                if (bindings != null) {
                    for (Iterator bindIter = bindings.iterator(); bindIter.hasNext();) {
                        queryNode.addBinding((String)bindIter.next());
                    }
                }
                return queryNode;
            }
            // no transfomation available
            if (resultSize == 0) {
                throw new QueryMetadataException(
                                                 RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Could_not_find_query_plan_for_the_group__5") + groupName); //$NON-NLS-1$
            }
            // there should be only one result entry for a fully qualified name
            if (resultSize > 1) {
                throw new TeiidComponentException(
                                                       RuntimeMetadataPlugin.Util.getString("TransformationMetadata.GroupID_ambiguous_there_are_multiple_virtual_plans_available_for_this_groupID__1") + groupName); //$NON-NLS-1$
            }
        }
        throw new QueryMetadataException(
                                         RuntimeMetadataPlugin.Util.getString("TransformationMetadata.QueryPlan_could_not_be_found_for_physical_group__6") + groupName); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getInsertPlan(java.lang.Object)
     */
    @Override
    public String getInsertPlan( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);

        TableRecord tableRecord = (TableRecord)groupID;
        final String groupName = tableRecord.getFullName();
        if (tableRecord.isVirtual()) {
            // Query the index files
            Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.INSERT_TRANSFORM, groupName, false);
            int resultSize = results.size();
            if (resultSize == 1) {
                // get the transform record for this result
                final TransformationRecord transformRecord = (TransformationRecord)results.iterator().next();
                return transformRecord.getTransformation();
            }
            // no transfomation available
            if (resultSize == 0) {
                return null;
            }
            // there should be only one result entry for a fully qualified name
            if (resultSize > 1) {
                throw new TeiidComponentException(
                                                       RuntimeMetadataPlugin.Util.getString("TransformationMetadata.GroupID_ambiguous_there_are_multiple_insert_plans_available_for_this_groupID__2") + groupName); //$NON-NLS-1$
            }
        }
        throw new QueryMetadataException(
                                         RuntimeMetadataPlugin.Util.getString("TransformationMetadata.InsertPlan_could_not_be_found_for_physical_group__8") + groupName); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getUpdatePlan(java.lang.Object)
     */
    @Override
    public String getUpdatePlan( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);

        TableRecord tableRecord = (TableRecord)groupID;
        final String groupName = tableRecord.getFullName();
        if (tableRecord.isVirtual()) {
            // Query the index files
            Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.UPDATE_TRANSFORM, groupName, false);
            int resultSize = results.size();
            if (resultSize == 1) {
                // get the transform record for this result
                final TransformationRecord transformRecord = (TransformationRecord)results.iterator().next();
                return transformRecord.getTransformation();
            }
            // no transfomation available
            if (resultSize == 0) {
                return null;
            }
            // there should be only one result entry for a fully qualified name
            if (resultSize > 1) {
                throw new TeiidComponentException(
                                                       RuntimeMetadataPlugin.Util.getString("TransformationMetadata.GroupID_ambiguous_there_are_multiple_update_plans_available_for_this_groupID__3") + groupName); //$NON-NLS-1$
            }
        }

        throw new QueryMetadataException(
                                         RuntimeMetadataPlugin.Util.getString("TransformationMetadata.InsertPlan_could_not_be_found_for_physical_group__10") + groupName); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getDeletePlan(java.lang.Object)
     */
    @Override
    public String getDeletePlan( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);

        TableRecord tableRecord = (TableRecord)groupID;
        final String groupName = tableRecord.getFullName();
        if (tableRecord.isVirtual()) {
            // Query the index files
            Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.DELETE_TRANSFORM, groupName, false);
            int resultSize = results.size();
            if (resultSize == 1) {
                // get the transform record for this result
                final TransformationRecord transformRecord = (TransformationRecord)results.iterator().next();
                return transformRecord.getTransformation();
            }
            // no transfomation available
            if (resultSize == 0) {
                return null;
            }
            // there should be only one result entry for a fully qualified name
            if (resultSize > 1) {
                throw new TeiidComponentException(
                                                       RuntimeMetadataPlugin.Util.getString("TransformationMetadata.GroupID_ambiguous_there_are_multiple_delete_plans_available_for_this_groupID__4") + groupName); //$NON-NLS-1$
            }
        }
        throw new QueryMetadataException(
                                         RuntimeMetadataPlugin.Util.getString("TransformationMetadata.DeletePlan_could_not_be_found_for_physical_group__12") + groupName); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#modelSupports(java.lang.Object, int)
     */
    @Override
    public boolean modelSupports( final Object modelID,
                                  final int modelConstant ) {
        ArgCheck.isInstanceOf(ModelRecord.class, modelID);

        switch (modelConstant) {
            default:
                throw new UnsupportedOperationException(
                                                        RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Unknown_support_constant___12") + modelConstant); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#groupSupports(java.lang.Object, int)
     */
    @Override
    public boolean groupSupports( final Object groupID,
                                  final int groupConstant ) {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;

        switch (groupConstant) {
            case SupportConstants.Group.UPDATE:
                return tableRecord.supportsUpdate();
            default:
                throw new UnsupportedOperationException(
                                                        RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Unknown_support_constant___12") + groupConstant); //$NON-NLS-1$
        }
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#elementSupports(java.lang.Object, int)
     */
    @Override
    public boolean elementSupports( final Object elementID,
                                    final int elementConstant ) {

        if (elementID instanceof ColumnRecord) {
            ColumnRecord columnRecord = (ColumnRecord)elementID;
            switch (elementConstant) {
                case SupportConstants.Element.NULL:
                    int ntype1 = columnRecord.getNullType();
                    return (ntype1 == NULLABLE);
                case SupportConstants.Element.NULL_UNKNOWN:
                    int ntype2 = columnRecord.getNullType();
                    return (ntype2 == NULLABLE_UNKNOWN);
                case SupportConstants.Element.SEARCHABLE_COMPARE:
                    int stype1 = columnRecord.getSearchType();
                    return (stype1 == SEARCHABLE || stype1 == ALL_EXCEPT_LIKE);
                case SupportConstants.Element.SEARCHABLE_LIKE:
                    int stype2 = columnRecord.getSearchType();
                    return (stype2 == SEARCHABLE || stype2 == LIKE_ONLY);
                case SupportConstants.Element.SELECT:
                    return columnRecord.isSelectable();
                case SupportConstants.Element.UPDATE:
                    return columnRecord.isUpdatable();
                case SupportConstants.Element.DEFAULT_VALUE:
                    Object defaultValue = columnRecord.getDefaultValue();
                    if (defaultValue == null) {
                        return false;
                    }
                    return true;
                case SupportConstants.Element.AUTO_INCREMENT:
                    return columnRecord.isAutoIncrementable();
                case SupportConstants.Element.CASE_SENSITIVE:
                    return columnRecord.isCaseSensitive();
                case SupportConstants.Element.SIGNED:
                    return columnRecord.isSigned();
                default:
                    throw new UnsupportedOperationException(
                                                            RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Unknown_support_constant___12") + elementConstant); //$NON-NLS-1$
            }
        } else if (elementID instanceof ProcedureParameterRecord) {
            ProcedureParameterRecord columnRecord = (ProcedureParameterRecord)elementID;
            switch (elementConstant) {
                case SupportConstants.Element.NULL:
                    int ntype1 = columnRecord.getNullType();
                    return (ntype1 == NULLABLE);
                case SupportConstants.Element.NULL_UNKNOWN:
                    int ntype2 = columnRecord.getNullType();
                    return (ntype2 == NULLABLE_UNKNOWN);
                case SupportConstants.Element.SEARCHABLE_COMPARE:
                case SupportConstants.Element.SEARCHABLE_LIKE:
                    return false;
                case SupportConstants.Element.SELECT:

                    if (columnRecord.getType() == MetadataConstants.PARAMETER_TYPES.IN_PARM) {
                        return false;
                    }

                    return true;
                case SupportConstants.Element.UPDATE:
                    return false;
                case SupportConstants.Element.DEFAULT_VALUE:
                    Object defaultValue = columnRecord.getDefaultValue();
                    if (defaultValue == null) {
                        return false;
                    }
                    return true;
                case SupportConstants.Element.AUTO_INCREMENT:
                    return false;
                case SupportConstants.Element.CASE_SENSITIVE:
                    return false;
                case SupportConstants.Element.SIGNED:
                    return true;
                default:
                    throw new UnsupportedOperationException(
                                                            RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Unknown_support_constant___12") + elementConstant); //$NON-NLS-1$
            }

        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    private IllegalArgumentException createInvalidRecordTypeException( Object elementID ) {
        return new IllegalArgumentException(
                                            RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Invalid_type", elementID.getClass().getName())); //$NON-NLS-1$
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getMaxSetSize(java.lang.Object)
     */
    @Override
    public int getMaxSetSize( final Object modelID ) {
        ArgCheck.isInstanceOf(ModelRecord.class, modelID);
        return ((ModelRecord)modelID).getMaxSetSize();
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getIndexesInGroup(java.lang.Object)
     */
    @Override
    public Collection getIndexesInGroup( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;

        final String groupUUID = tableRecord.getUUID();
        Assertion.isNotNull(groupUUID);

        // get the indexIDs
        Collection indexIDs = tableRecord.getIndexIDs();
        Collection indexRecords = new HashSet(indexIDs.size());
        // find a index record for each ID
        for (Iterator indexIter = tableRecord.getIndexIDs().iterator(); indexIter.hasNext();) {
            String indexID = (String)indexIter.next();
            // Query the index files
            final Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.INDEX, indexID, false);
            if (results.size() != 1) {
                if (results.isEmpty()) {
                    throw new QueryMetadataException(
                                                     RuntimeMetadataPlugin.Util.getString("TransformationMetadata.No_metadata_info_available_for_the_index_with_UUID_{0}._1", indexID)); //$NON-NLS-1$
                }
                throw new QueryMetadataException(
                                                 RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Ambigous_index_with_UUID_{0},_found_multiple_indexes_with_the_given_UUID._2", indexID)); //$NON-NLS-1$
            }
            indexRecords.addAll(results);
        }

        return indexRecords;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getUniqueKeysInGroup(java.lang.Object)
     */
    @Override
    public Collection getUniqueKeysInGroup( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;

        final String groupUUID = tableRecord.getUUID();
        Assertion.isNotNull(groupUUID);
        // Query the index files
        // find all unique keys
        return findChildRecords(tableRecord, IndexConstants.RECORD_TYPE.UNIQUE_KEY);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getForeignKeysInGroup(java.lang.Object)
     */
    @Override
    public Collection getForeignKeysInGroup( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;

        // Query the index files
        final String groupUUID = tableRecord.getUUID();
        Assertion.isNotNull(groupUUID);

        return findChildRecords(tableRecord, IndexConstants.RECORD_TYPE.FOREIGN_KEY);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getPrimaryKeyIDForForeignKeyID(java.lang.Object)
     */
    @Override
    public Object getPrimaryKeyIDForForeignKeyID( final Object foreignKeyID )
        throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(ForeignKeyRecord.class, foreignKeyID);
        ForeignKeyRecord fkRecord = (ForeignKeyRecord)foreignKeyID;

        String uuid = (String)fkRecord.getUniqueKeyID();
        return this.getRecordByType(uuid, IndexConstants.RECORD_TYPE.PRIMARY_KEY);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getAccessPatternsInGroup(java.lang.Object)
     */
    @Override
    public Collection getAccessPatternsInGroup( final Object groupID )
        throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;

        // Query the index files
        final String groupUUID = tableRecord.getUUID();
        Assertion.isNotNull(groupUUID);
        return findChildRecords(tableRecord, IndexConstants.RECORD_TYPE.ACCESS_PATTERN);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getElementIDsInIndex(java.lang.Object)
     */
    @Override
    public List getElementIDsInIndex( final Object index ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(ColumnSetRecord.class, index);
        ColumnSetRecord indexRecord = (ColumnSetRecord)index;

        boolean recordMatch = (indexRecord.getRecordType() == IndexConstants.RECORD_TYPE.INDEX);

        if (!recordMatch) {
            throw new QueryMetadataException(
                                             RuntimeMetadataPlugin.Util.getString("TransformationMetadata.The_metadataID_passed_does_not_match_a_index_record._1")); //$NON-NLS-1$
        }

        List uuids = indexRecord.getColumnIDs();
        List columnRecords = new ArrayList(uuids.size());

        for (Iterator uuidIter = uuids.iterator(); uuidIter.hasNext();) {
            String uuid = (String)uuidIter.next();
            columnRecords.add(this.getElementID(uuid));
        }

        return columnRecords;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getElementIDsInKey(java.lang.Object)
     */
    @Override
    public List getElementIDsInKey( final Object key ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(ColumnSetRecord.class, key);
        ColumnSetRecord keyRecord = (ColumnSetRecord)key;

        boolean recordMatch = (keyRecord.getRecordType() == IndexConstants.RECORD_TYPE.FOREIGN_KEY
                               || keyRecord.getRecordType() == IndexConstants.RECORD_TYPE.PRIMARY_KEY || keyRecord.getRecordType() == IndexConstants.RECORD_TYPE.UNIQUE_KEY);
        if (!recordMatch) {
            throw new QueryMetadataException(
                                             RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Expected_id_of_the_type_key_record_as_the_argument_2")); //$NON-NLS-1$
        }

        List uuids = keyRecord.getColumnIDs();

        // Get the table record for this key
        final String groupUUID = keyRecord.getParentUUID();
        Assertion.isNotNull(groupUUID);
        final TableRecord tableRecord = (TableRecord)this.getGroupID(groupUUID);

        // Query the index files
        final Collection results = findChildRecordsForColumns(tableRecord, IndexConstants.RECORD_TYPE.COLUMN, uuids);
        if (results.isEmpty()) {
            throw new QueryMetadataException(tableRecord.getFullName() + NOT_EXISTS_MESSAGE);
        }

        return new ArrayList(results);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getElementIDsInAccessPattern(java.lang.Object)
     */
    @Override
    public List getElementIDsInAccessPattern( final Object accessPattern )
        throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(ColumnSetRecord.class, accessPattern);
        ColumnSetRecord accessRecord = (ColumnSetRecord)accessPattern;

        boolean recordMatch = (accessRecord.getRecordType() == IndexConstants.RECORD_TYPE.ACCESS_PATTERN);
        if (!recordMatch) {
            throw new QueryMetadataException(
                                             RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Expected_id_of_the_type_accesspattern_record_as_the_argument_3")); //$NON-NLS-1$
        }

        List uuids = accessRecord.getColumnIDs();

        // Get the table record for this key
        final String groupUUID = accessRecord.getParentUUID();
        Assertion.isNotNull(groupUUID);
        final TableRecord tableRecord = (TableRecord)this.getGroupID(groupUUID);

        // Query the index files
        final Collection results = findChildRecordsForColumns(tableRecord, IndexConstants.RECORD_TYPE.COLUMN, uuids);
        if (results.isEmpty()) {
            throw new QueryMetadataException(tableRecord.getFullName() + NOT_EXISTS_MESSAGE);
        }

        return new ArrayList(results);
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#isXMLGroup(java.lang.Object)
     */
    @Override
    public boolean isXMLGroup( final Object groupID ) {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);

        TableRecord tableRecord = (TableRecord)groupID;
        if (tableRecord.getTableType() == MetadataConstants.TABLE_TYPES.DOCUMENT_TYPE) {
            return true;
        }
        return false;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#isTemporaryGroup(java.lang.Object)
     */
    public boolean isTemporaryGroup( final Object groupID ) {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);

        TableRecord tableRecord = (TableRecord)groupID;
        if (tableRecord.getTableType() == MetadataConstants.TABLE_TYPES.XML_STAGING_TABLE_TYPE) {
            return true;
        }
        return false;
    }

    /**
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#hasMaterialization(java.lang.Object)
     * @since 4.2
     */
    @Override
    public boolean hasMaterialization( final Object groupID ) {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;
        return tableRecord.isMaterialized();
    }

    /**
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getMaterialization(java.lang.Object)
     * @since 4.2
     */
    @Override
    public Object getMaterialization( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;
        if (tableRecord.isMaterialized()) {
            String uuid = (String)tableRecord.getMaterializedTableID();
            return this.getGroupID(uuid);
        }
        return null;
    }

    /**
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getMaterializationStage(java.lang.Object)
     * @since 4.2
     */
    @Override
    public Object getMaterializationStage( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;
        if (tableRecord.isMaterialized()) {
            String uuid = (String)tableRecord.getMaterializedStageTableID();
            return this.getGroupID(uuid);
        }
        return null;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getMappingNode(java.lang.Object)
     */
    @Override
    public MappingNode getMappingNode( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);

        TableRecord tableRecord = (TableRecord)groupID;
        final String groupName = tableRecord.getFullName();
        if (tableRecord.isVirtual()) {
            // get the transform record for this group
            TransformationRecord transformRecord = null;
            // Query the index files
            Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.MAPPING_TRANSFORM, groupName, false);
            int resultSize = results.size();
            if (resultSize == 1) {
                // get the columnset record for this result
                transformRecord = (TransformationRecord)results.iterator().next();
            } else {
                if (resultSize == 0) {
                    throw new QueryMetadataException(
                                                     RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Could_not_find_transformation_record_for_the_group__1") + groupName); //$NON-NLS-1$
                }
                // there should be only one for a fully qualified elementName
                if (resultSize > 1) {
                    throw new TeiidComponentException(
                                                           RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Multiple_transformation_records_found_for_the_group___1") + groupName); //$NON-NLS-1$
                }
            }
            // get mappin transform
            String document = transformRecord.getTransformation();
            InputStream inputStream = new ByteArrayInputStream(document.getBytes());
            MappingLoader reader = new MappingLoader();
            MappingDocument mappingDoc = null;
            try {
                mappingDoc = reader.loadDocument(inputStream);
                mappingDoc.setName(groupName);
            } catch (Exception e) {
                throw new TeiidComponentException(
                                                       e,
                                                       RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Error_trying_to_read_virtual_document_{0},_with_body__n{1}_1", groupName, mappingDoc)); //$NON-NLS-1$
            } finally {
                try {
                    inputStream.close();
                } catch (Exception e) {
                }
            }
            return mappingDoc;
        }

        return null;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getVirtualDatabaseName()
     */
    @Override
    public String getVirtualDatabaseName() throws TeiidComponentException, QueryMetadataException {
        // Query the index files
        try {
            final VdbRecord vdbRecord = (VdbRecord)this.getRecordByType(null, IndexConstants.RECORD_TYPE.VDB_ARCHIVE);
            return vdbRecord.getName();
        } catch (QueryMetadataException e) {
            return null;
        }
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getXMLTempGroups(java.lang.Object)
     */
    @Override
    public Collection getXMLTempGroups( final Object groupID ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;

        int tableType = tableRecord.getTableType();
        if (tableType == MetadataConstants.TABLE_TYPES.DOCUMENT_TYPE) {
            // Query the index files
            final Collection results = findChildRecordsWithoutFiltering(tableRecord, IndexConstants.RECORD_TYPE.TABLE);
            if (!results.isEmpty()) {
                Collection tempGroups = new HashSet(results.size());
                for (Iterator resultIter = results.iterator(); resultIter.hasNext();) {
                    TableRecord record = (TableRecord)resultIter.next();
                    if (record.getTableType() == MetadataConstants.TABLE_TYPES.XML_STAGING_TABLE_TYPE) {
                        tempGroups.add(record);
                    }
                }
                return tempGroups;
            }
        }
        return Collections.EMPTY_SET;
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getCardinality(java.lang.Object)
     */
    @Override
    public int getCardinality( final Object groupID ) {
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        return ((TableRecord)groupID).getCardinality();
    }

    /* (non-Javadoc)
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getXMLSchemas(java.lang.Object)
     */
    @Override
    public List getXMLSchemas( final Object groupID ) throws TeiidComponentException, QueryMetadataException {

        if (!(getIndexSelector() instanceof CompositeIndexSelector || getIndexSelector() instanceof RuntimeIndexSelector)) {
            return Collections.EMPTY_LIST;
        }
        ArgCheck.isInstanceOf(TableRecord.class, groupID);
        TableRecord tableRecord = (TableRecord)groupID;

        // lookup transformation record for the group
        String groupName = tableRecord.getFullName();
        TransformationRecord transformRecord = null;

        // Query the index files
        Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.MAPPING_TRANSFORM, groupName, false);
        int resultSize = results.size();
        if (resultSize == 1) {
            // get the columnset record for this result
            transformRecord = (TransformationRecord)results.iterator().next();
        } else {
            if (resultSize == 0) {
                throw new QueryMetadataException(
                                                 RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Could_not_find_transformation_record_for_the_group__1") + groupName); //$NON-NLS-1$
            }
            // there should be only one for a fully qualified elementName
            if (resultSize > 1) {
                throw new TeiidComponentException(
                                                       RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Multiple_transformation_records_found_for_the_group___1") + groupName); //$NON-NLS-1$
            }
        }

        // get the schema Paths
        List<String> schemaPaths = transformRecord.getSchemaPaths();

        List<String> fullPaths = new LinkedList<String>();

        for (String string : schemaPaths) {
            fullPaths.add(string);
        }

        // get schema contents
        List schemas = getIndexSelector().getFileContentsAsString(fullPaths);
        if (schemas == null || schemas.isEmpty()) {
            schemas = getIndexSelector().getFileContentsAsString(schemaPaths);
            if (schemas == null || schemas.isEmpty()) {
                throw new TeiidComponentException(
                                                       RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Error_trying_to_read_schemas_for_the_document/table____1") + groupName); //$NON-NLS-1$
            }
        }

        return schemas;
    }

    @Override
    public String getNameInSource( final Object metadataID ) {
        ArgCheck.isInstanceOf(MetadataRecord.class, metadataID);
        return ((MetadataRecord)metadataID).getNameInSource();
    }

    @Override
    public int getElementLength( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getLength();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return ((ProcedureParameterRecord)elementID).getLength();
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    @Override
    public int getPosition( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getPosition();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return ((ProcedureParameterRecord)elementID).getPosition();
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    @Override
    public int getPrecision( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getPrecision();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return ((ProcedureParameterRecord)elementID).getPrecision();
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    @Override
    public int getRadix( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getRadix();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return ((ProcedureParameterRecord)elementID).getRadix();
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    @Override
    public String getFormat( Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getFormat();
        }
        throw createInvalidRecordTypeException(elementID);
    }

    @Override
    public int getScale( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getScale();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return ((ProcedureParameterRecord)elementID).getScale();
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    @Override
    public int getDistinctValues( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getDistinctValues();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return -1;
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    @Override
    public int getNullValues( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getNullValues();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return -1;
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    @Override
    public String getNativeType( final Object elementID ) {
        if (elementID instanceof ColumnRecord) {
            return ((ColumnRecord)elementID).getNativeType();
        } else if (elementID instanceof ProcedureParameterRecord) {
            return null;
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    /* 
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getExtensionProperties(java.lang.Object)
     */
    @Override
    public Properties getExtensionProperties( final Object metadataID )
        throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isInstanceOf(MetadataRecord.class, metadataID);
        MetadataRecord metadataRecord = (MetadataRecord)metadataID;

        Properties extProps = new Properties();

        // find the entities properties records
        String uuid = metadataRecord.getUUID();
        String prefixString = getUUIDPrefixPattern(IndexConstants.RECORD_TYPE.PROPERTY, uuid);

        Index[] indexes = getIndexes(IndexConstants.RECORD_TYPE.PROPERTY, getIndexSelector());
        IEntryResult[] results = queryIndex(indexes, prefixString.toCharArray(), true, true);

        if (results != null && results.length > 0) {
            // get the property records for this result
            for (int i = 0; i < results.length; i++) {
                if (results[i] != null) {
                    PropertyRecord record = (PropertyRecord)findMetadataRecord(results[i]);
                    if (record != null) {
                        extProps.setProperty(record.getPropertyName(), record.getPropertyValue());
                    }
                }
            }
        }

        return extProps;
    }

    /**
     * @see com.metamatrix.query.metadata.BasicQueryMetadata#getBinaryVDBResource(java.lang.String)
     * @since 4.3
     */
    @Override
    public byte[] getBinaryVDBResource( String resourcePath ) throws TeiidComponentException, QueryMetadataException {
        String content = this.getCharacterVDBResource(resourcePath);
        if (content != null) {
            return content.getBytes();
        }
        return null;
    }

    /**
     * @see com.metamatrix.query.metadata.BasicQueryMetadata#getCharacterVDBResource(java.lang.String)
     * @since 4.3
     */
    @Override
    public String getCharacterVDBResource( String resourcePath ) throws TeiidComponentException, QueryMetadataException {
        IndexSelector selector = this.getIndexSelector();
        // make sure the selector is initialized
        try {
            selector.getIndexes();
        } catch (IOException e) {
            throw new QueryMetadataException(
                                             e,
                                             RuntimeMetadataPlugin.Util.getString("TransformationMetadata.error_intialize_selector")); //$NON-NLS-1$
        }
        // look for the resource in only the first available indexSelector
        // built in assumption is that first selector is always for the vdb logged in
        if (selector instanceof CompositeIndexSelector) {
            CompositeIndexSelector compSelector = (CompositeIndexSelector)selector;
            List selectors = compSelector.getIndexSelectors();
            if (selectors.size() > 0) {
                IndexSelector firstSelector = (IndexSelector)selectors.get(0);
                return firstSelector.getFileContentAsString(resourcePath);
            }
        }
        return selector.getFileContentAsString(resourcePath);
    }

    /**
     * @see com.metamatrix.query.metadata.BasicQueryMetadata#getVDBResourcePaths()
     * @since 4.3
     */
    @Override
    public String[] getVDBResourcePaths() throws TeiidComponentException, QueryMetadataException {
        IndexSelector selector = this.getIndexSelector();
        // make sure the selector is initialized
        try {
            selector.getIndexes();
        } catch (IOException e) {
            throw new QueryMetadataException(
                                             e,
                                             RuntimeMetadataPlugin.Util.getString("TransformationMetadata.error_intialize_selector")); //$NON-NLS-1$
        }
        // look for the resource in only the first available indexSelector
        // built in assumption is that first selector is always for the vdb logged in
        if (selector instanceof CompositeIndexSelector) {
            CompositeIndexSelector compSelector = (CompositeIndexSelector)selector;
            List selectors = compSelector.getIndexSelectors();
            if (selectors.size() > 0) {
                IndexSelector firstSelector = (IndexSelector)selectors.get(0);

                return firstSelector.getFilePaths();
            }
        }
        return selector.getFilePaths();
    }

    /**
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getModeledType(java.lang.Object)
     * @since 5.0
     */
    @Override
    public String getModeledType( final Object elementID ) throws TeiidComponentException, QueryMetadataException {
        DatatypeRecord record = getDatatypeRecord(elementID);
        if (record != null) {
            return record.getDatatypeID();
        }
        return null;
    }

    /**
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getModeledBaseType(java.lang.Object)
     * @since 5.0
     */
    @Override
    public String getModeledBaseType( final Object elementID ) throws TeiidComponentException, QueryMetadataException {
        DatatypeRecord record = getDatatypeRecord(elementID);
        if (record != null) {
            return record.getBasetypeID();
        }
        return null;
    }

    /**
     * @see com.metamatrix.query.metadata.QueryMetadataInterface#getModeledPrimitiveType(java.lang.Object)
     * @since 5.0
     */
    @Override
    public String getModeledPrimitiveType( final Object elementID ) throws TeiidComponentException, QueryMetadataException {
        DatatypeRecord record = getDatatypeRecord(elementID);
        if (record != null) {
            return record.getPrimitiveTypeID();
        }
        return null;
    }

    // ==================================================================================
    // P R O T E C T E D M E T H O D S
    // ==================================================================================

    protected DatatypeRecord getDatatypeRecord( final Object elementID )
        throws TeiidComponentException, QueryMetadataException {
        if (elementID instanceof ColumnRecord) {
            String uuid = ((ColumnRecord)elementID).getDatatypeUUID();
            if (!StringUtil.isEmpty(uuid)) {
                // Query the index files
                Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.DATATYPE, uuid, false);
                int resultSize = results.size();
                if (resultSize == 1) {
                    // get the datatype record for this result
                    return (DatatypeRecord)results.iterator().next();
                } else if (resultSize == 0) {
                    // there should be only one for the UUID
                    throw new QueryMetadataException(uuid + NOT_EXISTS_MESSAGE);
                } else {
                    // there should be only one for the UUID
                    throw new QueryMetadataException(RuntimeMetadataPlugin.Util.getString("TransformationMetadata.0", uuid)); //$NON-NLS-1$
                }
            }
            return null;
        } else if (elementID instanceof ProcedureParameterRecord) {
            String uuid = ((ProcedureParameterRecord)elementID).getDatatypeUUID();
            if (!StringUtil.isEmpty(uuid)) {
                // Query the index files
                Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.DATATYPE, uuid, false);
                int resultSize = results.size();
                if (resultSize == 1) {
                    // get the datatype record for this result
                    return (DatatypeRecord)results.iterator().next();
                } else if (resultSize == 0) {
                    // there should be only one for the UUID
                    throw new QueryMetadataException(uuid + NOT_EXISTS_MESSAGE);
                } else {
                    // there should be only one for the UUID
                    throw new QueryMetadataException(RuntimeMetadataPlugin.Util.getString("TransformationMetadata.0", uuid)); //$NON-NLS-1$
                }
            }
            return null;
        } else {
            throw createInvalidRecordTypeException(elementID);
        }
    }

    /**
     * Return the array of MtkIndex instances representing temporary indexes
     * 
     * @param selector
     * @return
     * @throws QueryMetadataException
     */
    protected Index[] getIndexes( final char recordType,
                                  final IndexSelector selector ) throws TeiidComponentException {
        try {
            return selector.getIndexes();
        } catch (IOException e) {
            throw new TeiidComponentException(
                                                   e,
                                                   RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Error_trying_to_obtain_index_file_using_IndexSelector_1", selector)); //$NON-NLS-1$
        }
    }

    /**
     * Return the pattern match string that could be used to match a UUID in a datatype index record. The RECORD_TYPE.DATATYPE
     * records contain a header portion of the form: recordType|datatypeID|basetypeID|fullName|objectID|nameInSource|...
     * 
     * @param uuid The UUID for which the pattern match string is to be constructed.
     * @return The pattern match string of the form: recordType|*|*|*|uuid|*
     */
    protected String getDatatypeUUIDMatchPattern( final String uuid ) {
        ArgCheck.isNotNull(uuid);
        String uuidString = uuid;
        if (StringUtil.startsWithIgnoreCase(uuid, UUID.PROTOCOL)) {
            uuidString = uuid.toLowerCase();
        }
        // construct the pattern string
        String patternStr = "" //$NON-NLS-1$
                            + IndexConstants.RECORD_TYPE.DATATYPE // recordType
                            + IndexConstants.RECORD_STRING.RECORD_DELIMITER + IndexConstants.RECORD_STRING.MATCH_CHAR // datatypeID
                            + IndexConstants.RECORD_STRING.RECORD_DELIMITER + IndexConstants.RECORD_STRING.MATCH_CHAR // basetypeID
                            + IndexConstants.RECORD_STRING.RECORD_DELIMITER + IndexConstants.RECORD_STRING.MATCH_CHAR // fullName
                            + IndexConstants.RECORD_STRING.RECORD_DELIMITER + uuidString // objectID
                            + IndexConstants.RECORD_STRING.RECORD_DELIMITER + IndexConstants.RECORD_STRING.MATCH_CHAR;
        return patternStr;
    }

    /**
     * Return the pattern match string that could be used to match a UUID in an index record. All index records contain a header
     * portion of the form: recordType|pathInModel|UUID|nameInSource|parentObjectID|
     * 
     * @param uuid The UUID for which the pattern match string is to be constructed.
     * @return The pattern match string of the form: recordType|*|uuid|*
     */
    protected String getUUIDMatchPattern( final char recordType,
                                          final String uuid ) {
        ArgCheck.isNotNull(uuid);
        String uuidString = uuid;
        if (StringUtil.startsWithIgnoreCase(uuid, UUID.PROTOCOL)) {
            uuidString = uuid.toLowerCase();
        }
        // construct the pattern string
        String patternStr = "" //$NON-NLS-1$
                            + recordType
                            + IndexConstants.RECORD_STRING.RECORD_DELIMITER
                            + IndexConstants.RECORD_STRING.MATCH_CHAR
                            + IndexConstants.RECORD_STRING.RECORD_DELIMITER
                            + uuidString
                            + IndexConstants.RECORD_STRING.RECORD_DELIMITER
                            + IndexConstants.RECORD_STRING.MATCH_CHAR;
        return patternStr;
    }

    /**
     * Return the pattern match string that could be used to match a partially/fully qualified entity name in an index record. All
     * index records contain a header portion of the form: recordType|pathInModel|UUID|nameInSource|parentObjectID|
     * 
     * @param name The partially/fully qualified name for which the pattern match string is to be constructed.
     * @return The pattern match string of the form: recordType|*name|*
     */
    protected String getMatchPattern( final char recordType,
                                      final String name ) {
        ArgCheck.isNotNull(name);

        // construct the pattern string
        String patternStr = "" //$NON-NLS-1$
                            + recordType
                            + IndexConstants.RECORD_STRING.RECORD_DELIMITER
                            + IndexConstants.RECORD_STRING.MATCH_CHAR;
        if (name != null) {
            patternStr = patternStr + name.trim().toUpperCase() + IndexConstants.RECORD_STRING.RECORD_DELIMITER
                         + IndexConstants.RECORD_STRING.MATCH_CHAR;
        }
        return patternStr;
    }

    /**
     * Return the prefix match string that could be used to exactly match a fully qualified entity name in an index record. All
     * index records contain a header portion of the form: recordType|pathInModel|UUID|nameInSource|parentObjectID|
     * 
     * @param name The fully qualified name for which the prefix match string is to be constructed.
     * @return The pattern match string of the form: recordType|name|
     */
    protected String getPrefixPattern( final char recordType,
                                       final String name ) {

        // construct the pattern string
        String patternStr = "" //$NON-NLS-1$
                            + recordType + IndexConstants.RECORD_STRING.RECORD_DELIMITER;
        if (name != null) {
            patternStr = patternStr + name.trim().toUpperCase() + IndexConstants.RECORD_STRING.RECORD_DELIMITER;
        }

        return patternStr;
    }

    /**
     * Return the prefix match string that could be used to exactly match a uuid in an index record.
     * 
     * @param uuid The uuid for which the prefix match string is to be constructed.
     * @return The pattern match string of the form: recordType|uuid|
     */
    protected String getUUIDPrefixPattern( final char recordType,
                                           final String uuid ) {

        // construct the pattern string
        String patternStr = "" //$NON-NLS-1$
                            + recordType + IndexConstants.RECORD_STRING.RECORD_DELIMITER;
        if (uuid != null) {
            patternStr = patternStr + uuid.trim() + IndexConstants.RECORD_STRING.RECORD_DELIMITER;
        }

        return patternStr;
    }

    /**
     * Return the prefix match string that could be used to exactly match a fully qualified entity name in an index record. All
     * index records contain a header portion of the form: recordType|pathInModel|UUID|nameInSource|parentObjectID|
     * 
     * @param name The fully qualified name for which the prefix match string is to be constructed.
     * @return The pattern match string of the form: recordType|name|
     */
    protected String getParentPrefixPattern( final char recordType,
                                             final String name ) {

        // construct the pattern string
        String patternStr = "" //$NON-NLS-1$
                            + recordType + IndexConstants.RECORD_STRING.RECORD_DELIMITER;
        if (name != null) {
            patternStr = patternStr + name.trim().toUpperCase() + DELIMITER_CHAR;
        }

        return patternStr;
    }

    /**
     * Get a MetadataRecord object given a entityName/UUID.
     * 
     * @param entityName String representing an entity, may be null(vdbs)
     * @param recordType The record type for the entity
     * @return A MetadataRecord object for a given entityName/UUID
     * @throws TeiidComponentException
     * @throws QueryMetadataException
     */
    protected MetadataRecord getRecordByType( final String entityName,
                                              final char recordType ) throws TeiidComponentException, QueryMetadataException {

        // Query the index files
        final Collection results = findMetadataRecords(recordType, entityName, false);

        int resultSize = results.size();
        if (resultSize == 1) {
            // get the columnset record for this result
            return (MetadataRecord)results.iterator().next();
        } else if (resultSize == 0) {
            // there should be only one for the UUID
            throw new QueryMetadataException(entityName + NOT_EXISTS_MESSAGE);
        } else {
            // there should be only one for the UUID
            throw new QueryMetadataException(RuntimeMetadataPlugin.Util.getString("TransformationMetadata.0", entityName)); //$NON-NLS-1$
        }
    }

    /**
     * Remove any MetadataRecord instances that do not match the specified uuid Due to the pattern matching used to query index
     * files if an index record matched the specified uuid string anywhere in that record it would be returned in the results (for
     * example, if the parent ObjectID in the index record matched the specified uuid).
     * 
     * @param uuid
     * @param records
     * @since 4.2
     */
    protected void filterMetadataRecordForUUID( final String uuid,
                                                Collection records ) {
        if (uuid != null && records != null) {
            for (final Iterator iter = records.iterator(); iter.hasNext();) {
                final MetadataRecord record = (MetadataRecord)iter.next();
                if (record == null || !uuid.equals(record.getUUID())) {
                    iter.remove();
                }
            }
        }
    }

    /**
     * Return the IndexSelector reference
     * 
     * @return
     */
    protected IndexSelector getIndexSelector() {
        return this.context.getIndexSelector();
    }

    /**
     * Return the QueryMetadataContext reference
     * 
     * @return
     */
    protected QueryMetadataContext getContext() {
        return this.context;
    }

    /**
     * Return all index file records that match the specified entity name, filtering by matching on parent uuid
     * 
     * @param parentRecord
     * @param childRecordType the type of the child to seek uuids
     * @param uuids to filter just the objects we want
     * @return columnRecords
     * @throws TeiidComponentException
     */
    protected Collection findChildRecords( final MetadataRecord parentRecord,
                                           final char childRecordType ) throws TeiidComponentException {
        IEntryResult[] results = queryIndexByParentPath(childRecordType, parentRecord.getFullName());
        Collection records = findMetadataRecords(results);

        // if uniquekey records are being returned, also return primary key records,
        // as primary keys are unique keys
        if (childRecordType == IndexConstants.RECORD_TYPE.UNIQUE_KEY) {
            Collection primarKeyRecords = findMetadataRecords(queryIndexByParentPath(IndexConstants.RECORD_TYPE.PRIMARY_KEY,
                                                                                     parentRecord.getFullName()));
            records.addAll(primarKeyRecords);
        }

        // jh Case 5092: Moved filtering down into this method (findChildRecords()).
        // (It used to be done in each method that calls this one.)

        // filtering records (records with different parents could begin with same name)
        final String groupUUID = parentRecord.getUUID();

        List filteredRecords = new ArrayList(records.size());

        for (Iterator resultsIter = records.iterator(); resultsIter.hasNext();) {
            MetadataRecord record = (MetadataRecord)resultsIter.next();
            String parentUUID = record.getParentUUID();

            if (parentUUID != null && parentUUID.equalsIgnoreCase(groupUUID)) {
                filteredRecords.add(record);
            }
        }

        return filteredRecords;
    }

    /**
     * Return all index file records that match the specified entity name, filtering by matching on the child uuids
     * 
     * @param parentRecord
     * @param childRecordType the type of the child to seek uuids
     * @param uuids to filter just the objects we want
     * @return columnRecords
     * @throws TeiidComponentException
     */
    protected Collection findChildRecordsForColumns( final MetadataRecord parentRecord,
                                                     final char childRecordType,
                                                     final List uuids ) throws TeiidComponentException {
        IEntryResult[] results = queryIndexByParentPath(childRecordType, parentRecord.getFullName());
        Collection records = findMetadataRecords(results);

        // if uniquekey records are being returned, also return primary key records,
        // as primary keys are unique keys
        if (childRecordType == IndexConstants.RECORD_TYPE.UNIQUE_KEY) {
            Collection primarKeyRecords = findMetadataRecords(queryIndexByParentPath(IndexConstants.RECORD_TYPE.PRIMARY_KEY,
                                                                                     parentRecord.getFullName()));
            records.addAll(primarKeyRecords);
        }

        List columnRecords = new ArrayList(uuids.size());

        // filtering recods (records with differrent parents could
        // begin with same name)
        for (Iterator resultIter = records.iterator(); resultIter.hasNext();) {
            MetadataRecord record = (MetadataRecord)resultIter.next();
            if (record != null && uuids.contains(record.getUUID())) {
                columnRecords.add(record);
            }
        }
        return columnRecords;
    }

    /**
     * Return all index file records that match the specified entity name, without filtering
     * 
     * @param parentRecord
     * @param childRecordType the type of the child to seek
     * @return records
     * @throws TeiidComponentException
     */
    protected Collection findChildRecordsWithoutFiltering( final MetadataRecord parentRecord,
                                                           final char childRecordType ) throws TeiidComponentException {
        IEntryResult[] results = queryIndexByParentPath(childRecordType, parentRecord.getFullName());
        Collection records = findMetadataRecords(results);

        // if uniquekey records are being returned, also return primary key records,
        // as primary keys are unique keys
        if (childRecordType == IndexConstants.RECORD_TYPE.UNIQUE_KEY) {
            Collection primarKeyRecords = findMetadataRecords(queryIndexByParentPath(IndexConstants.RECORD_TYPE.PRIMARY_KEY,
                                                                                     parentRecord.getFullName()));
            records.addAll(primarKeyRecords);
        }

        return records;
    }

    /**
     * Return all index file records that match the specified entity name
     * 
     * @param indexName
     * @param entityName the name to match
     * @param isPartialName true if the entity name is a partially qualified
     * @return results
     * @throws QueryMetadataException
     */
    protected Collection findMetadataRecords( final IEntryResult[] results ) {
        return RecordFactory.getMetadataRecord(results, null);
    }

    protected MetadataRecord findMetadataRecord( final IEntryResult result ) {
        return RecordFactory.getMetadataRecord(result, null);
    }

    /**
     * Return all index file records that match the specified entity name
     * 
     * @param indexName
     * @param entityName the name to match
     * @param isPartialName true if the entity name is a partially qualified
     * @return results
     * @throws QueryMetadataException
     */
    protected Collection findMetadataRecords( final char recordType,
                                              final String entityName,
                                              final boolean isPartialName ) throws TeiidComponentException {

        IEntryResult[] results = queryIndex(recordType, entityName, isPartialName);
        Collection records = findMetadataRecords(results);

        if (StringUtil.startsWithIgnoreCase(entityName, UUID.PROTOCOL)) {
            // Filter out ColumnRecord instances that do not match the specified uuid.
            // Due to the pattern matching used to query index files if an index record
            // matched the specified uuid string anywhere in that record it would be returned
            // in the results (for example, if the parent ObjectID in the index record
            // matched the specified uuid).
            this.filterMetadataRecordForUUID(entityName, records);
        }
        return records;
    }

    /**
     * Return all index file records that match the specified entity name
     * 
     * @param indexName
     * @param entityName the name to match
     * @param isPartialName true if the entity name is a partially qualified
     * @return results
     * @throws QueryMetadataException
     */
    protected IEntryResult[] queryIndex( final char recordType,
                                         final String entityName,
                                         final boolean isPartialName ) throws TeiidComponentException {

        IEntryResult[] results = null;
        Index[] indexes = getIndexes(recordType, getIndexSelector());

        // Query based on UUID
        if (StringUtil.startsWithIgnoreCase(entityName, UUID.PROTOCOL)) {
            String patternString = null;
            if (recordType == IndexConstants.RECORD_TYPE.DATATYPE) {
                patternString = getDatatypeUUIDMatchPattern(entityName);
            } else {
                patternString = getUUIDMatchPattern(recordType, entityName);
            }
            results = queryIndex(indexes, patternString.toCharArray(), false, true);
        }

        // Query based on partially qualified name
        else if (isPartialName) {
            String patternString = getMatchPattern(recordType, entityName);
            results = queryIndex(indexes, patternString.toCharArray(), false, false);
        }

        // Query based on fully qualified name
        else {
            String prefixString = getPrefixPattern(recordType, entityName);
            results = queryIndex(indexes, prefixString.toCharArray(), true, true);
        }

        return results;
    }

    /**
     * Return all index file records that match the specified record pattern.
     * 
     * @param indexes the array of MtkIndex instances to query
     * @param pattern
     * @return results
     * @throws QueryMetadataException
     */
    protected IEntryResult[] queryIndex( final Index[] indexes,
                                         final char[] pattern,
                                         boolean isPrefix,
                                         boolean returnFirstMatch ) throws TeiidComponentException {
        try {
            return SimpleIndexUtil.queryIndex(indexes, pattern, isPrefix, returnFirstMatch);
        } catch (TeiidException e) {
            throw new TeiidComponentException(e, e.getMessage());
        }
    }

    /**
     * Return all index file records that match the specified record pattern.
     * 
     * @param indexes the array of MtkIndex instances to query
     * @param pattern
     * @return results
     * @throws QueryMetadataException
     */
    protected IEntryResult[] queryIndex( final Index[] indexes,
                                         final char[] pattern,
                                         boolean isPrefix,
                                         boolean isCaseSensitive,
                                         boolean returnFirstMatch ) throws TeiidComponentException {
        try {
            return SimpleIndexUtil.queryIndex(null, indexes, pattern, isPrefix, isCaseSensitive, returnFirstMatch);
        } catch (TeiidException e) {
            throw new TeiidComponentException(e, e.getMessage());
        }
    }

    // ==================================================================================
    // P A C K A G E M E T H O D S
    // ==================================================================================

    // ==================================================================================
    // P R I V A T E M E T H O D S
    // ==================================================================================

    /**
     * Looks up procedure plan in the transformations index for a given procedure.
     */
    private String getProcedurePlan( final String procedureName ) throws TeiidComponentException, QueryMetadataException {
        ArgCheck.isNotEmpty(procedureName);

        // Query the index files
        Collection results = findMetadataRecords(IndexConstants.RECORD_TYPE.PROC_TRANSFORM, procedureName, false);
        int resultSize = results.size();
        if (resultSize == 1) {
            // get the transform record for this result
            final TransformationRecord transformRecord = (TransformationRecord)results.iterator().next();
            return transformRecord.getTransformation();
        }
        // there should be only one result entry for a fully qualified name
        if (resultSize > 1) {
            throw new TeiidComponentException(
                                                   RuntimeMetadataPlugin.Util.getString("TransformationMetadata.Procedure_ambiguous_there_are_multiple_procedure_plans_available_for_this_name___4") + procedureName); //$NON-NLS-1$
        }

        // no transfomation available, this may not be a virtual procedure
        return null;
    }

    /**
     * Helper method to get back an array of ColumnRecords given a list of UUIDs.
     */
    private ColumnRecord[] getColumnRecordsForUUIDs( final List uuids )
        throws TeiidComponentException, QueryMetadataException {

        ColumnRecord[] columnRecords = new ColumnRecord[uuids.size()];

        for (int i = 0; i < uuids.size(); i++) {
            String colUUID = (String)uuids.get(i);
            columnRecords[i] = (ColumnRecord)this.getElementID(colUUID);
        }

        return columnRecords;
    }

    /**
     * Return all index file records that match the specified entity name
     * 
     * @param indexName
     * @param entityName the name to match
     * @param isPartialName true if the entity name is a partially qualified
     * @return results
     * @throws QueryMetadataException
     */
    private IEntryResult[] queryIndexByParentPath( final char recordType,
                                                   final String parentFullName ) throws TeiidComponentException {

        // Query based on fully qualified name
        String prefixString = getParentPrefixPattern(recordType, parentFullName);

        // Query the model index files
        Index[] indexes = getIndexes(recordType, getIndexSelector());
        IEntryResult[] results = queryIndex(indexes, prefixString.toCharArray(), true, false);

        return results;
    }
    
    /**
     * {@inheritDoc}
     *
     * @see org.teiid.query.metadata.BasicQueryMetadata#getPrimaryKey(java.lang.Object)
     */
    @Override
    public Object getPrimaryKey( Object metadataID ) {
        ArgCheck.isInstanceOf(TableRecord.class, metadataID);
        TableRecord tableRecord = (TableRecord)metadataID;

        final String groupUUID = tableRecord.getUUID();
        Assertion.isNotNull(groupUUID);

        Collection pk;
        try {
            pk = findChildRecords(tableRecord, IndexConstants.RECORD_TYPE.PRIMARY_KEY);
        } catch (TeiidComponentException e) {
            throw new TeiidRuntimeException(e);
        }
        if (pk.size() > 1) {
            throw new TeiidRuntimeException("Multiple primary keys for table");
        }
        return pk.iterator().next();
    }
    
    /**
     * {@inheritDoc}
     *
     * @see org.teiid.query.metadata.BasicQueryMetadata#getName(java.lang.Object)
     */
    @Override
    public String getName( Object metadataID ) {
        ArgCheck.isInstanceOf(MetadataRecord.class, metadataID);
        MetadataRecord metadataRecord = (MetadataRecord)metadataID;
        return metadataRecord.getName(); 
    }
    
    /**
     * {@inheritDoc}
     *
     * @see org.teiid.query.metadata.BasicQueryMetadata#hasProcedure(java.lang.String)
     */
    @Override
    public boolean hasProcedure( String name ) throws TeiidComponentException {
        try {
            return getStoredProcInfoDirect(name) != null;
        } catch (QueryMetadataException e) {
            return true;
        }
    }

}
