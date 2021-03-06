/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.schema.tools.model.schema.impl;

import com.metamatrix.modeler.schema.tools.model.jdbc.internal.ColumnImpl;
import com.metamatrix.modeler.schema.tools.model.schema.Column;
import com.metamatrix.modeler.schema.tools.model.schema.Relationship;

public class MergedColumn extends BaseColumn {
    private Column childCol;

    private Relationship tableRelationship;

    private int iOccurence;

    public MergedColumn( Column childCol,
                         Relationship tableRelationship,
                         int iOccurence ) {
        super(false, childCol.getType());
        this.childCol = childCol;
        this.tableRelationship = tableRelationship;
        this.iOccurence = iOccurence;
    }

    public String getXpath() {
        StringBuffer retval = new StringBuffer();
        String tableXpath = tableRelationship.getChildRelativeXpath();
        retval.append(tableXpath);
        if (iOccurence >= 0) {
            retval.append("[" + iOccurence + "]"); //$NON-NLS-1$ //$NON-NLS-2$
        }
        String childXpath = childCol.getXpath();
        retval.append("/"); //$NON-NLS-1$
        retval.append(childXpath);
        return retval.toString();
    }

    public String getSimpleName() {
        StringBuffer retval = new StringBuffer();
        String tableSimpleName = tableRelationship.getChild().getSimpleName();
        retval.append(tableSimpleName);
        if (iOccurence >= 0) {
            retval.append(iOccurence);
        }
        String childSimpleName = childCol.getSimpleName();
        String sep = SchemaModelImpl.getSeparator();
        retval.append(sep);
        retval.append(childSimpleName);
        if (childCol instanceof TextColumn) {
            int index = retval.lastIndexOf("text"); //$NON-NLS-1$
            return retval.substring(0, index - 1); // remove '_data'
        }
        return retval.toString();
    }

    public Column copy() {
        return new MergedColumn(childCol, tableRelationship, iOccurence);
    }

    public void printDebug() {
        StringBuffer buff = new StringBuffer("\t \t"); //$NON-NLS-1$
        buff.append("MergedColumn: "); //$NON-NLS-1$
        buff.append("SimpleName = " + getSimpleName()); //$NON-NLS-1$
        buff.append(" "); //$NON-NLS-1$
        buff.append("XPath = " + getXpath()); //$NON-NLS-1$
        buff.append(" "); //$NON-NLS-1$
        buff.append("Occurance = " + iOccurence); //$NON-NLS-1$
        buff.append(" "); //$NON-NLS-1$
        buff.append("TableRelationship = " + tableRelationship); //$NON-NLS-1$
        buff.append(" "); //$NON-NLS-1$
        System.out.println(buff.toString());
    }

    public com.metamatrix.modeler.schema.tools.model.jdbc.Column getColumnImplementation() {
        ColumnImpl newColumn = new ColumnImpl();
        newColumn.setDataAttributeName(getSimpleName());
        newColumn.setDataType(((BaseColumn)childCol).getDataType());
        newColumn.setIsAttributeOfParent(false);
        newColumn.setIsInputParameter(false);
        newColumn.setName(getSimpleName());
        newColumn.setOutputXPath(getXpath());
        return newColumn;
    }
}
