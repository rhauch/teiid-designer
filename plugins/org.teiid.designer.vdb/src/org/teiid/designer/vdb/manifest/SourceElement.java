/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.vdb.manifest;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import org.teiid.designer.vdb.VdbModelEntry;
import org.teiid.designer.vdb.plugin.VdbPlugin;

/**
 * 
 */
@XmlAccessorType( XmlAccessType.NONE )
@XmlType( name = "" )
public class SourceElement implements Serializable {

    private static final long serialVersionUID = 1L;

    @XmlAttribute( name = "name", required = true )
    private String name;

    @XmlAttribute( name = "jndi-name" )
    private String jndiName;

    /**
     * Used by JAXB
     */
    public SourceElement() {
    }

    SourceElement( final VdbModelEntry entry ) {
        name = entry.getDataSource();
        final StringBuilder builder = new StringBuilder(VdbPlugin.workspaceUuid.toString());
        for (final String segment : entry.getName().removeLastSegments(1).segments())
            builder.append('.').append(segment);
        this.jndiName = builder.append(name).toString();
    }

    /**
     * @return jndiName
     */
    public String getJndiName() {
        return jndiName;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }
}