/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.transformation.aspects.sql;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EObject;

import com.metamatrix.common.vdb.SystemVdbUtility;
import com.metamatrix.core.util.CoreArgCheck;
import com.metamatrix.metamodels.transformation.InputSet;
import com.metamatrix.metamodels.transformation.MappingClass;
import com.metamatrix.metamodels.transformation.StagingTable;
import com.metamatrix.modeler.core.index.IndexConstants;
import com.metamatrix.modeler.core.metadata.runtime.MetadataConstants;
import com.metamatrix.modeler.core.metamodel.aspect.MetamodelEntity;
import com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect;

/**
 * MappingClassSqlAspect
 */
public class InputSetSqlAspect extends AbstractTransformationSqlAspect implements SqlTableAspect {

    private static final String INPUT_SET_FULL_NAME = "INPUT"; //$NON-NLS-1$

    /**
     * Construct an instance of MappingClassSqlAspect.
     * 
     */
    public InputSetSqlAspect(MetamodelEntity entity) {
        super(entity);
    }
    
    /**
     * @see com.metamatrix.modeler.transformation.aspects.sql.MappingClassObjectSqlAspect#isRecordType(char)
     */
    public boolean isRecordType(char recordType) {
        return (recordType == IndexConstants.RECORD_TYPE.TABLE);
    }

    /** 
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlAspect#isQueryable(org.eclipse.emf.ecore.EObject)
     */
    public boolean isQueryable(final EObject eObject) {
        return false;
    }

    /** 
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#isMaterialized(org.eclipse.emf.ecore.EObject)
     * @since 4.2
     */
    public boolean isMaterialized(EObject eObject) {
        return false;
    }
    
    /** 
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#getMaterializedTableId(org.eclipse.emf.ecore.EObject)
     * @since 4.2
     */
    public String getMaterializedTableId(EObject eObject) {
        return null;
    } 
    
    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#supportsUpdate(org.eclipse.emf.ecore.EObject)
     */
    public boolean supportsUpdate(EObject eObject) {
        return false;
    }

    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#isVirtual(org.eclipse.emf.ecore.EObject)
     */
    public boolean isVirtual(EObject eObject) {
        return true;
    }

    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#isSystem(org.eclipse.emf.ecore.EObject)
     */
    public boolean isSystem(EObject eObject) {
        CoreArgCheck.isInstanceOf(InputSet.class, eObject);
        String modelName = getModelName(eObject);
        if (modelName != null && SystemVdbUtility.isSystemModelWithSystemTableType(modelName)) {
            return true;
        }
        return false;
    }

    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#getColumns(org.eclipse.emf.ecore.EObject)
     */
    public List getColumns(EObject eObject) {
        CoreArgCheck.isInstanceOf(InputSet.class, eObject); 
        InputSet inputSet = (InputSet) eObject;       
        return inputSet.getInputParameters();
    }

    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#getIndexes(org.eclipse.emf.ecore.EObject)
     */
    public Collection getIndexes(EObject eObject) {
        return Collections.EMPTY_LIST;
    }

    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#getUniqueKeys(org.eclipse.emf.ecore.EObject)
     */
    public Collection getUniqueKeys(EObject eObject) {
        return Collections.EMPTY_LIST;
    }

    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#getForeignKeys(org.eclipse.emf.ecore.EObject)
     */
    public Collection getForeignKeys(EObject eObject) {
        return Collections.EMPTY_LIST;
    }

    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#getPrimaryKey(org.eclipse.emf.ecore.EObject)
     */
    public Object getPrimaryKey(EObject eObject) {
        return null;
    }

    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#getAccessPatterns(org.eclipse.emf.ecore.EObject)
     */
    public Collection getAccessPatterns(EObject eObject) {
        return Collections.EMPTY_LIST;
    }

    /**
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#getCardinality(org.eclipse.emf.ecore.EObject)
     */
    public int getCardinality(EObject eObject) {
        return 0;
    }

    /*
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#getTableType(org.eclipse.emf.ecore.EObject)
     */
    public int getTableType(EObject eObject) {
        CoreArgCheck.isInstanceOf(InputSet.class, eObject); 
        return MetadataConstants.TABLE_TYPES.XML_MAPPING_CLASS_TYPE;
    }

    /*
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlColumnSetAspect#getColumnSetType()
     */
    public int getColumnSetType() {
        return MetadataConstants.COLUMN_SET_TYPES.TABLE;
    }

    /*
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#isMappable(org.eclipse.emf.ecore.EObject, int)
     */
    public boolean isMappable(EObject eObject, int mappingType) {
        return false;
    }

    /** 
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#canAcceptTransformationSource(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
     * @since 4.3
     */
    public boolean canAcceptTransformationSource(EObject target, EObject source) {
        CoreArgCheck.isInstanceOf(InputSet.class, target);
        CoreArgCheck.isNotNull(source);
        return false;
    }

    /** 
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#canBeTransformationSource(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
     * @since 4.3
     */
    public boolean canBeTransformationSource(EObject source, EObject target) {
        CoreArgCheck.isInstanceOf(InputSet.class, source);
        CoreArgCheck.isNotNull(target);
        if(target instanceof MappingClass && !(target instanceof StagingTable)) {
            return true;
        }
        return false;
    }

    /*
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlAspect#updateObject(org.eclipse.emf.ecore.EObject, org.eclipse.emf.ecore.EObject)
     */
    public void updateObject(EObject targetObject, EObject sourceObject) {

    }

    /*
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlTableAspect#setSupportsUpdate(org.eclipse.emf.ecore.EObject, boolean)
     */
    public void setSupportsUpdate(EObject eObject, boolean supportsUpdate) {
        // do nothis mapping class is never updatable
    }

    /** 
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlAspect#getName(org.eclipse.emf.ecore.EObject)
     */
    public String getName(EObject eObject) {
        CoreArgCheck.isInstanceOf(InputSet.class, eObject); 
        return INPUT_SET_FULL_NAME;
    }

    /** 
     * @see com.metamatrix.modeler.core.metamodel.aspect.sql.SqlAspect#getNameInSource(org.eclipse.emf.ecore.EObject)
     */
    public String getNameInSource(EObject eObject) {
        return null;
    }

}
