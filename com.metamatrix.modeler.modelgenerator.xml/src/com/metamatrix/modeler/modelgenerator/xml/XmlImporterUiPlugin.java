/* ================================================================================== 
 * JBoss, Home of Professional Open Source. 
 * 
 * Copyright (c) 2000, 2009 MetaMatrix, Inc. and Red Hat, Inc. 
 * 
 * Some portions of this file may be copyrighted by other 
 * contributors and licensed to Red Hat, Inc. under one or more 
 * contributor license agreements. See the copyright.txt file in the 
 * distribution for a full listing of individual contributors. 
 * 
 * This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * ================================================================================== */

package com.metamatrix.modeler.modelgenerator.xml;

import org.eclipse.ui.IWorkbenchPage;
import org.osgi.framework.BundleContext;
import com.metamatrix.core.PluginUtil;
import com.metamatrix.core.util.PluginUtilImpl;
import com.metamatrix.ui.AbstractUiPlugin;
import com.metamatrix.ui.actions.ActionService;

/**
 * The main plugin class to be used in the desktop.
 */
public class XmlImporterUiPlugin extends AbstractUiPlugin
// implements IInternalUiConstants {
{
    // The shared instance.
    private static XmlImporterUiPlugin plugin;
    // Resource bundle.

    public static final String C_threshold = "c_Threshold"; //$NON-NLS-1$
    public static final String P_threshold = "p_Threshold"; //$NON-NLS-1$
    public static final String F_threshold = "f_Threshold"; //$NON-NLS-1$
    public static final String requestTable = "requestTable"; //$NON-NLS-1$
    public static final String mergedChildSep = "mergedChildSep"; //$NON-NLS-1$

    /**
     * Provides access to the plugin's log, internationalized properties, and debugger.
     * 
     * @since 4.0
     */
    private Util util;

    /**
     * The constructor.
     */
    public XmlImporterUiPlugin() {
        // Even though we would like to call the single argument constructor of PluginUtilImpl,
        // since we are following the naming guideline, we can't because it is in another
        // classloader and so won't find the resource bundle.
        // We can't even use the two argument constructor because it appears to have a bug that
        // causes the bundle name to be i18n.i18n instead of <pluginid>.i18n
        // util = new PluginUtilImpl(IUiConstants.PLUGIN_ID, ResourceBundle.getBundle(IUiConstants.BUNDLE_NAME));
        util = new Util();
        plugin = this;
    }

    @Override
    public void start( BundleContext context ) throws Exception {
        super.start(context);
        // Initialize logging/i18n utility
        ((PluginUtilImpl)util).initializePlatformLogger(this);
    }

    /**
     * <p>
     * </p>
     * 
     * @see com.metamatrix.ui.AbstractUiPlugin#createActionService(org.eclipse.ui.IWorkbenchPage)
     * @since 4.0
     */
    @Override
    protected ActionService createActionService( IWorkbenchPage page ) {
        return null;
    }

    /**
     * @see com.metamatrix.ui.AbstractUiPlugin#getPluginUtil()
     * @since 4.0
     */
    @Override
    public PluginUtil getPluginUtil() {
        return util;
    }

    /**
     * Returns the shared instance.
     * 
     * @since 4.0
     */
    public static XmlImporterUiPlugin getDefault() {
        return plugin;
    }
}
