/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.query.internal.ui.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.teiid.query.sql.lang.Command;
import org.teiid.query.sql.lang.Criteria;
import org.teiid.query.sql.lang.From;
import org.teiid.query.sql.lang.FromClause;
import org.teiid.query.sql.lang.JoinPredicate;
import org.teiid.query.sql.lang.Query;
import org.teiid.query.sql.lang.SetQuery;
import org.teiid.query.sql.lang.SubqueryContainer;
import org.teiid.query.sql.lang.SubqueryFromClause;
import org.teiid.query.sql.lang.UnaryFromClause;
import org.teiid.query.sql.visitor.ValueIteratorProviderCollectorVisitor;

import com.metamatrix.modeler.core.query.SetQueryUtil;


/** 
 * QueryTreeContentProvider is the content provider for the QueryTreeViewer.  It handles
 * LanguageObjects in a Command and breaks out individual Query objects inside a SetQuery
 * and From and Criteria objects inside a Query.
 * @since 4.2
 */
public class QueryTreeContentProvider implements ITreeContentProvider {

    ////////////////////////////////////////////////////////////////////////////////////////
    // STATIC VARIABLES
    ////////////////////////////////////////////////////////////////////////////////////////
    public static QueryTreeContentProvider instance = null;
    
    ////////////////////////////////////////////////////////////////////////////////////////
    // STATIC METHODS
    ////////////////////////////////////////////////////////////////////////////////////////
    public static QueryTreeContentProvider getInstance() {
        if (instance == null) {
            instance = new QueryTreeContentProvider();
        }
        return instance;
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ////////////////////////////////////////////////////////////////////////////////////////
    private QueryTreeContentProvider() {
        super();
    }
    
    /** 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getChildren(java.lang.Object)
     * @since 4.2
     */
    public Object[] getChildren(Object obj) {
        if ( obj instanceof SetQuery) {
            return SetQueryUtil.getQueryList((SetQuery)obj).toArray();
        } else if ( obj instanceof Query ) {
            if ( ((Query) obj).getCriteria() == null ) {
                return new Object[] { ((Query) obj).getFrom() };
            }
            return new Object[] { ((Query) obj).getFrom(), ((Query) obj).getCriteria() };
        } else if ( obj instanceof From ) {
            Collection clauses = ((From) obj).getClauses();
            ArrayList children = new ArrayList(clauses.size());
            for ( Iterator iter = clauses.iterator() ; iter.hasNext() ; ) {
                FromClause clause = (FromClause) iter.next();
                if ( clause instanceof UnaryFromClause ) {
                    children.add(((UnaryFromClause) clause).getGroup());
                } else {
                    children.add(clause);
                }
            }
            return children.toArray(); 
        } else if ( obj instanceof FromClause ) {
            if ( obj instanceof UnaryFromClause ) {
                return new Object[] { };
            } else if ( obj instanceof JoinPredicate ) {
                return new Object[] { ((JoinPredicate) obj).getLeftClause(), ((JoinPredicate) obj).getRightClause() };
            } else if ( obj instanceof SubqueryFromClause ) {
                return new Object[] { };
            }            
        } else if ( obj instanceof Criteria ) {
            List<SubqueryContainer> containers = ValueIteratorProviderCollectorVisitor.getValueIteratorProviders((Criteria)obj);
            List<Command> commands = new ArrayList<Command>();
            
            for (SubqueryContainer container : containers) {
                commands.add(container.getCommand());
            }
            
            return commands.toArray();
        }

        return null;
    }

    /** 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#getParent(java.lang.Object)
     * @since 4.2
     */
    public Object getParent(Object element) {
        return null;
    }

    /** 
     * @see org.eclipse.jface.viewers.ITreeContentProvider#hasChildren(java.lang.Object)
     * @since 4.2
     */
    public boolean hasChildren(Object element) {
        Object[] children = getChildren(element);
        if ( children == null || children.length == 0 ) {
            return false;
        }
        return true;
    }    

    /** 
     * @see org.eclipse.jface.viewers.IStructuredContentProvider#getElements(java.lang.Object)
     * @since 4.2
     */
    public Object[] getElements(Object inputElement) {
        if ( inputElement instanceof ArrayList ) {
            return ((ArrayList) inputElement).toArray();
        }
        
        return getChildren(inputElement);
    }

    /** 
     * @see org.eclipse.jface.viewers.IContentProvider#dispose()
     * @since 4.2
     */
    public void dispose() {
    }

    /** 
     * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
     * @since 4.2
     */
    public void inputChanged(Viewer viewer,
                             Object oldInput,
                             Object newInput) {
    }

}
