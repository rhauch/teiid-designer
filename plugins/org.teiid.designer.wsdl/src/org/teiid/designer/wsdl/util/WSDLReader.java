/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package org.teiid.designer.wsdl.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.MessageFormat;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.wst.wsdl.validation.internal.IValidationMessage;
import org.eclipse.wst.wsdl.validation.internal.IValidationReport;
import org.eclipse.wst.wsdl.validation.internal.WSDLValidator;
import org.eclipse.wst.wsdl.validation.internal.eclipse.URIResolverWrapper;
import org.teiid.designer.wsdl.WsdlPlugin;
import org.teiid.designer.wsdl.exception.WSDLValidationException;
import org.teiid.designer.wsdl.exception.WSDLValidationMessage;
import org.teiid.designer.wsdl.model.Model;
import org.teiid.designer.wsdl.model.ModelBuilder;
import org.teiid.designer.wsdl.model.ModelGenerationException;


/**
 * This class is responsible for reading WSDL files from a URI or file system validating them and producing an OO representation of
 * the WSDL contents
 */
public class WSDLReader {
	private static final String PREFIX = "WSDLReader."; //$NON-NLS-1$
	
    private String wsdlURI;
	private static WSDLValidator VALIDATOR;

    public static final int VALIDATION_SEVERITY_ERROR = 0;
    public static final int VALIDATION_SEVERITY_WARNING = 1;

    public WSDLReader() {
        this(null);
    }

    /**
     * @param fileUri the URI of the WSDL file
     */
    public WSDLReader( String fileUri ) {
        wsdlURI = fileUri;
    }

    /**
     * @return the Model based on the given WSDL
     * @throws ModelGenerationException
     */
    public Model getModel() throws ModelGenerationException {
        Model theModel = null;
        try {
            theModel = buildWSDLStructures();
        } catch (NullPointerException ex) {
            WsdlPlugin.Util.log(IStatus.ERROR,
                                              ex,
                                              WsdlPlugin.Util.getString("WSDLReader.unexpected.parsing.wsdl")); //$NON-NLS-1$
            Exception e = new Exception(WsdlPlugin.Util.getString("WSDLReader.unexpected.parsing.wsdl")); //$NON-NLS-1$
            throw new ModelGenerationException(e);
        } catch (Exception ex) {
            throw new ModelGenerationException(ex);
        }
        return theModel;
    }

    private Model buildWSDLStructures() throws Exception {
        ModelBuilder builder = new ModelBuilder();
        builder.setWSDL(wsdlURI.replace("%20", " ")); //$NON-NLS-1$//$NON-NLS-2$
        if (!builder.isWSDLParsed()) {
            Exception myEx = builder.getWSDLException();
            throw myEx;
        }
        return builder.getModel();
    }

    /**
     * @return the URI of the given WSDL
     */
    public String getWSDLUri() {
        return wsdlURI;
    }

    /**
     * @param fileUri the URI of the WSDL file
     */
    public void setWSDLUri( String fileUri ) {
        wsdlURI = fileUri;
    }

    /**
     * @return true if the wsdl is valid, false otherwise
     */
	public MultiStatus validateWSDL(IProgressMonitor monitor) {
    	
		monitor.beginTask(WsdlPlugin.Util
				.getString("WSDLReader.validating.wsdl"), //$NON-NLS-1$
				IProgressMonitor.UNKNOWN);
		monitor.worked(1);
		if (VALIDATOR == null) {
			VALIDATOR = new WSDLValidator();
			VALIDATOR.addURIResolver(new URIResolverWrapper());
        }
		
		String wsdlUri = getWSDLUri();
		if(!wsdlUri.startsWith("http")) { //$NON-NLS-1$
			try {
				wsdlUri =  new File(wsdlUri).toURI().toURL().toString();
			    setWSDLUri(wsdlUri);
			} catch (MalformedURLException e) {
				throw new RuntimeException(e);
			}
		}
		
        monitor.worked(1);
		IValidationReport report = VALIDATOR.validate(wsdlUri);
		monitor.worked(1);
		boolean success = !report.hasErrors() && report.isWSDLValid();

		MultiStatus status;
        if (success) {
            final int code = 100;
            status = new MultiStatus(WsdlPlugin.PLUGIN_ID, code, new IStatus[0],
            		WsdlPlugin.Util
							.getString("WSDLReader.validation.passed"), null); //$NON-NLS-1$
        } else {
            boolean warningsOnly = report.isWSDLValid();
            final int code = 500;
            IValidationMessage[] vmessages = report.getValidationMessages();
            IStatus[] messages = new WSDLValidationMessage[vmessages.length];
            for (int i = 0; i < vmessages.length; i++) {
                String message = buildValidationMessageString(vmessages[i]);
                int severity = vmessages[i].getSeverity();
                if (severity == IValidationMessage.SEV_ERROR) warningsOnly = false;
                int newSeverity = (severity == IValidationMessage.SEV_ERROR ? IStatus.ERROR : IStatus.WARNING);
                messages[i] = new WSDLValidationMessage(message, newSeverity);
            }
            if (warningsOnly) {
            	status = createMultiStatus(code, messages, getMessage("validation.warning", null), null);  //$NON-NLS-1$
            } else {
				try {
					URL url = new URL(wsdlUri);
					URLConnection connection = url.openConnection();
					String contentType = connection.getContentType();
					if (messages.length == 0) {
						if ((null != contentType)
								&& (!contentType.contains("text/xml") || !contentType.contains("application/xml"))) { //$NON-NLS-1$ //$NON-NLS-2$
							String messageSuffix = MessageFormat
									.format(WsdlPlugin.Util
													.getString("WSDLReader.validation.content.type.error"), //$NON-NLS-1$
											wsdlUri, contentType);
							String fullMessage = WsdlPlugin.Util
									.getString("WSDLReader.wsdl.invalid") + messageSuffix; //$NON-NLS-1$
							messages = new WSDLValidationMessage[]{new WSDLValidationMessage(
									fullMessage, IStatus.ERROR)};
							status = createMultiStatus(code, messages, fullMessage ,new WSDLValidationException());
							status = new MultiStatus(
									WsdlPlugin.PLUGIN_ID, code,
									messages, fullMessage,
									new WSDLValidationException());
						} else {
							messages = new WSDLValidationMessage[]{new WSDLValidationMessage(
									WsdlPlugin.Util.getString("WSDLReader.wsdl.invalid"), IStatus.ERROR)}; //$NON-NLS-1$
							status = createMultiStatus(code, messages, getMessage("wsdl.invalid", null),new WSDLValidationException());  //$NON-NLS-1$
						}
					} else {
						if (!url.getProtocol().equals("file") && !contentType.contains("text/xml") //$NON-NLS-1$//$NON-NLS-2$
								|| !contentType.equals("application/xml")) { //$NON-NLS-1$
							String messageSuffix = MessageFormat
									.format(
											WsdlPlugin.Util.getString("WSDLReader.validation.content.type.error"), //$NON-NLS-1$
											wsdlUri, contentType);
							status = createMultiStatus(code, messages, getMessage("open.connection.error", messageSuffix),new WSDLValidationException());  //$NON-NLS-1$

						} else {
							status = createMultiStatus(code, messages, getMessage("open.connection.error", null),new WSDLValidationException());  //$NON-NLS-1$
						}
					}
				} catch (Exception e) { 
					if (messages.length == 0) {
						messages = new WSDLValidationMessage[1];
						messages[0] = new WSDLValidationMessage(
								WsdlPlugin.Util.getString("WSDLReader.open.connection.error"), //$NON-NLS-1$
								IStatus.ERROR);
					}
					status = createMultiStatus(code, messages, getMessage("open.connection.error", null),new WSDLValidationException());  //$NON-NLS-1$
				}
			}
		}
		monitor.worked(1);
		monitor.done();
        return status;
    }
	
	private static String getMessage(String id, String suffix) {
		String value = WsdlPlugin.Util.getString(PREFIX + id);
		if( suffix != null ) {
			value += suffix;
		}
		return value;
	}
	
	private MultiStatus createMultiStatus( int code, IStatus[] messages, String message, Throwable exception) {
		return new MultiStatus(WsdlPlugin.PLUGIN_ID, code, messages, message, new WSDLValidationException());
	}

	private String buildValidationMessageString(IValidationMessage vmessage) {
		StringBuffer buff = new StringBuffer();
		buff.append(vmessage.getLine());
		buff.append(":"); //$NON-NLS-1$
		buff.append(vmessage.getColumn());
		buff.append(" - "); //$NON-NLS-1$
		buff.append(vmessage.getMessage());
		return buff.toString();
	}
}
