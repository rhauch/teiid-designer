package com.metamatrix.metadata.runtime.api;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import com.metamatrix.core.MetaMatrixCoreException;
import com.metamatrix.core.util.ArgCheck;
import com.metamatrix.core.util.Assertion;
import com.metamatrix.core.util.ObjectConverterUtil;
import com.metamatrix.core.util.StringUtil;

public class MetadataSourceUtil {

    public static String getFileContentAsString( String path,
                                                 MetadataSource iss ) throws MetaMatrixCoreException {
        File f = iss.getFile(path);
        if (f == null) {
            return null;
        }
        try {
            return ObjectConverterUtil.convertFileToString(f);
        } catch (IOException e) {
            throw new MetaMatrixCoreException(e, "MetadataSourceUtil.ioExceptionConvertingFileToString");
        }
    }

    /**
     * @throws MetaMatrixCoreException
     * @see com.metamatrix.modeler.core.index.IndexSelector#getFileContent(java.lang.String, java.lang.String[],
     *      java.lang.String[])
     * @since 4.2
     */
    public static InputStream getFileContent( final String path,
                                              MetadataSource iss,
                                              final String[] tokens,
                                              final String[] tokenReplacements ) throws MetaMatrixCoreException {
        ArgCheck.isNotNull(tokens);
        ArgCheck.isNotNull(tokenReplacements);
        Assertion.isEqual(tokens.length, tokenReplacements.length);
        String fileContents = getFileContentAsString(path, iss);
        if (fileContents != null) {
            for (int i = 0; i < tokens.length; i++) {
                final String token = tokens[i];
                final String tokenReplacement = tokenReplacements[i];
                fileContents = StringUtil.replaceAll(fileContents, token, tokenReplacement);
            }
            return new ByteArrayInputStream(fileContents.getBytes());
        }
        return null;
    }

}