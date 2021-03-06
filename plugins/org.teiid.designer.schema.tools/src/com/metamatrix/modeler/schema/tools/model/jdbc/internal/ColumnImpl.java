/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.schema.tools.model.jdbc.internal;

import com.metamatrix.modeler.schema.tools.model.jdbc.Column;
import com.metamatrix.modeler.schema.tools.model.jdbc.DataType;

public class ColumnImpl extends DatabaseElementImpl implements Column {

	private boolean m_attrOfParent;

	private boolean m_inputParam;

	private boolean m_required;

	private String m_attributeName;

	private Integer m_multiValues;

	private Integer m_role;

	private DataType m_dataType;
	
	private boolean m_isForeignKey;
	
	public ColumnImpl() {
		initDefaults();
	}

	private void initDefaults() {
		m_attrOfParent = false;
		m_inputParam = false;
		m_isForeignKey = false;
		m_required = false;
		m_role = 0; //data role
	}

	public boolean isAttributeOfParent() {
		return m_attrOfParent;
	}

	public void setIsAttributeOfParent(boolean isAttributeOfParent) {
		m_attrOfParent = isAttributeOfParent;
	}

	public String getDataAttributeName() {
		return m_attributeName;
	}

	public void setDataAttributeName(String name) {
		m_attributeName = name;
	}

	public boolean isInputParameter() {
		return m_inputParam;
	}

	public void setIsInputParameter(boolean isInputParameter) {
		m_inputParam = isInputParameter;
	}

	public Integer getMultipleValues() {
		return m_multiValues;
	}

	public void setMultipleValues(Integer multiValues) {
		m_multiValues = multiValues;
	}

	public boolean isRequiredValue() {
		return m_required;
	}

	public void setIsRequiredValue(boolean isRequired) {
		m_required = isRequired;
	}

	public Integer getRole() {
		return m_role;
	}

	public void setRole(Integer role) {
		m_role = role;
	}

	public void setDataType(DataType type) {
		m_dataType = type;
	}

	public DataType getDataType() {
		return m_dataType;
	}
	
	public void setIsForeignKey(boolean isForeignKey) {
		m_isForeignKey = isForeignKey;
	}
	
	public boolean isForeignKey() {
		return m_isForeignKey;
	}

	@Override
    public String toString() {
		return this.getName();
	}

}
