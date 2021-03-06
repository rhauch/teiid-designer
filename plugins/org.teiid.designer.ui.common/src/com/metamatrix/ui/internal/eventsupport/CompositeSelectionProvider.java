/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.ui.internal.eventsupport;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;


/**
 * The <code>CompositeSelectionProvider</code> class contains a collection of <code>ISelectionProvider</code>s.
 * All selections generated from the providers in this collection are propagated out to the registered
 * <code>ISelectionChangedListener</code>s using this provider as the event source.
 */
public class CompositeSelectionProvider extends SelectionProvider
                                        implements ISelectionChangedListener {
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // FIELDS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /** Collection of <code>ISelectionProvider</code>s. */
    private List<ISelectionProvider> subProviders;
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * Constructs a <code>CompositeSelectionProvider</code>.
     */
    public CompositeSelectionProvider() {
        subProviders = new ArrayList<ISelectionProvider>();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionProvider#addSelectionChangedListener(org.eclipse.jface.viewers.ISelectionChangedListener)
     */
    @Override
    public synchronized void addSelectionChangedListener(ISelectionChangedListener theListener) {
        super.addSelectionChangedListener(theListener);
        
        // if currently has a selection, let new listener know about it
        ISelection selection = getSelection();
        
        if (selection != null) {
            theListener.selectionChanged(new SelectionChangedEvent(this, selection));
        }
    }

    /**
     * Adds an <code>ISelectionProvider</code>. If this provider has a selection, it is sent to registered
     * listeners.
     */
    public void addSelectionProvider(ISelectionProvider theSelectionProvider) {
        subProviders.add(theSelectionProvider);
        theSelectionProvider.addSelectionChangedListener(this);

        // send current selection out to the listeners if necessary
        ISelection selection = theSelectionProvider.getSelection();
        
        if (selection != null) {
            setSelection(selection, true, this);
        }
    }

    /**
    * Removes an <code>ISelectionProvider</code>.
    * @param 
    */
   public void removeSelectionProvider(ISelectionProvider theSelectionProvider) {
       subProviders.remove(theSelectionProvider);
       theSelectionProvider.removeSelectionChangedListener(this);
   }

    /* (non-Javadoc)
     * @see org.eclipse.jface.viewers.ISelectionChangedListener#selectionChanged(org.eclipse.jface.viewers.SelectionChangedEvent)
     */
    public void selectionChanged(SelectionChangedEvent theEvent) {
        // need to alert selection changed listeners
        setSelection(theEvent.getSelection(), true, this);
    }

}
