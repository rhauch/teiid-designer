/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.transformation.ui.reconciler;

import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import com.metamatrix.modeler.internal.transformation.util.TransformationSqlHelper;
import com.metamatrix.modeler.transformation.ui.PluginConstants;
import com.metamatrix.modeler.transformation.ui.UiPlugin;
import org.teiid.query.sql.symbol.AggregateSymbol;
import org.teiid.query.sql.symbol.AliasSymbol;
import org.teiid.query.sql.symbol.Constant;
import org.teiid.query.sql.symbol.ElementSymbol;
import org.teiid.query.sql.symbol.Expression;
import org.teiid.query.sql.symbol.ExpressionSymbol;
import org.teiid.query.sql.symbol.Function;
import org.teiid.query.sql.symbol.SingleElementSymbol;

/**
 * Label provider for the SqlSymbolList - the provided Objects are SingleElementSymbols.
 * ElementSymbols or ExpressionSymbols...
 * 
 * @see org.eclipse.jface.viewers.LabelProvider 
 */
public class SqlLabelProvider 
	extends LabelProvider
	implements ITableLabelProvider, PluginConstants.Images {

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnText(java.lang.Object, int)
	 */
	public String getColumnText(Object element, int columnIndex) {
		String result = PluginConstants.EMPTY_STRING;
        if(columnIndex==0) {
            if(element!=null) {
                // Alias Symbol
                if(element instanceof AliasSymbol) {
                    AliasSymbol aSymbol = (AliasSymbol)element;
                    SingleElementSymbol uSymbol = aSymbol.getSymbol();
                    String symName = TransformationSqlHelper.getSingleElementSymbolShortName(uSymbol,true);
                    result = symName + " AS "+aSymbol.getShortName(); //$NON-NLS-1$
                // SingleElementSymbol
                } else if(element instanceof SingleElementSymbol) {
                    result = TransformationSqlHelper.getSingleElementSymbolShortName((SingleElementSymbol)element,true);
                }
            }
        }
		return result;
	}

	/**
	 * @see org.eclipse.jface.viewers.ITableLabelProvider#getColumnImage(java.lang.Object, int)
	 */
	public Image getColumnImage(Object element, int columnIndex) {
        Image image = null;
        if(columnIndex==0) {
            if(element instanceof ExpressionSymbol) {
                image = UiPlugin.getDefault().getImage(FUNCTION_ICON);
            } else if(element instanceof SingleElementSymbol) {
                // Defect 23945 - added private method to get image for multiple types
                // of SQL symbols
                image = getImageForSymbol((SingleElementSymbol)element);
            }
        }
		return image;
	}
    
    /**
     *  Get the Image for the SingleElementSymbol
     */
    private Image getImageForSymbol(SingleElementSymbol seSymbol) {
        Image result = null;
        
        // If symbol is AliasSymbol, get underlying symbol
        if( seSymbol!=null && seSymbol instanceof AliasSymbol ) {
            seSymbol = ((AliasSymbol)seSymbol).getSymbol();
        }
        // ElementSymbol
        if ( (seSymbol instanceof ElementSymbol) ) {
            result = UiPlugin.getDefault().getImage(SYMBOL_ICON);
        // AggregateSymbol
        } else if ( seSymbol instanceof AggregateSymbol ) {
            result = UiPlugin.getDefault().getImage(FUNCTION_ICON);
        // ExpressionSymbol
        } else if ( seSymbol instanceof ExpressionSymbol ) {
            Expression expression = ((ExpressionSymbol)seSymbol).getExpression();
            if(expression!=null && expression instanceof Constant) {
                result = UiPlugin.getDefault().getImage(CONSTANT_ICON);
            } else if ( expression!=null && expression instanceof Function ) {
                result = UiPlugin.getDefault().getImage(FUNCTION_ICON);
            }
        }
        // Undefined
        if(result==null) {
            result = UiPlugin.getDefault().getImage(UNDEFINED_ICON);
        }
        
        return result; 
    }

}
