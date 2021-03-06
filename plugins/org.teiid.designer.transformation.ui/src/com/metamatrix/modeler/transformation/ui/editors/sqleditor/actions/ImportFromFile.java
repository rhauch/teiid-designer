/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.modeler.transformation.ui.editors.sqleditor.actions;

import java.util.EventObject;
import com.metamatrix.core.event.EventObjectListener;
import com.metamatrix.modeler.transformation.ui.editors.sqleditor.SqlEditorInternalEvent;
import com.metamatrix.modeler.transformation.ui.editors.sqleditor.SqlEditorPanel;
import com.metamatrix.modeler.transformation.ui.UiConstants;
import com.metamatrix.modeler.transformation.ui.UiPlugin;
import com.metamatrix.ui.actions.AbstractAction;

/**
 * The <code>ImportFromFile</code> class is the action that Sets the SqlEditor Text by
 * importing from a User file.
 * @since 4.0
 */
public class ImportFromFile extends AbstractAction implements EventObjectListener {

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // FIELDS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    private SqlEditorPanel panel;

    ///////////////////////////////////////////////////////////////////////////////////////////////
    // CONSTRUCTORS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    public ImportFromFile(SqlEditorPanel sqlPanel) {
        super(UiPlugin.getDefault());
        setImageDescriptor(UiPlugin.getDefault().getImageDescriptor(UiConstants.Images.IMPORT_FROM_FILE));
        this.panel = sqlPanel;
        sqlPanel.addInternalEventListener( this );
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////
    // METHODS
    ///////////////////////////////////////////////////////////////////////////////////////////////
    
    @Override
    protected void doRun() {
        panel.importFromFile();
    }
    
    public void processEvent(EventObject e) {
        //------------------------------------------------
        // respond to internal events from SqlEditorPanel
        // - Cannot import new text if panel is readonly
        //------------------------------------------------
        if (e instanceof SqlEditorInternalEvent) {
            int type = ((SqlEditorInternalEvent)e).getType();
            if( type==SqlEditorInternalEvent.READONLY_CHANGED ) {
                 boolean isEditable = panel.isEditable();
                 setEnabled(isEditable);
            }
        } 
    }
    
        
}
