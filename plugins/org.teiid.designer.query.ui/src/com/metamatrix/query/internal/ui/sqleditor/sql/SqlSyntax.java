/*
 * JBoss, Home of Professional Open Source.
 *
 * See the LEGAL.txt file distributed with this work for information regarding copyright ownership and licensing.
 *
 * See the AUTHORS.txt file distributed with this work for a full listing of individual contributors.
 */
package com.metamatrix.query.internal.ui.sqleditor.sql;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.teiid.core.types.DataTypeManager;
import org.teiid.designer.udf.UdfManager;
import org.teiid.language.SQLConstants;
import org.teiid.query.function.FunctionForm;
import org.teiid.query.function.FunctionLibrary;
import com.metamatrix.query.ui.UiPlugin;

/**
 * This class provides static Lists of SQL syntax words. RESERVED_WORDS contains reserved SQL words, FUNCTION_NAMES contains
 * system and user defined function names, DATATYPE_NAMES contains datatypes, and ALL_WORDS is a combination of all. All lists are
 * sorted alphabetically.
 */
public class SqlSyntax {
    // Word Lists
    public static final List<String> RESERVED_WORDS = new ArrayList<String>();
    public static final List<String> FUNCTION_NAMES = new ArrayList<String>();
    public static final List<String> DATATYPE_NAMES = new ArrayList<String>();
    public static final List<String> ALL_WORDS = new ArrayList<String>();

    // String with valid starting characters
    public static final List<String> RESERVED_WORD_START_CHARS = new ArrayList<String>();
    public static final List<String> FUNCTION_NAME_START_CHARS = new ArrayList<String>();
    public static final List<String> DATATYPE_NAME_START_CHARS = new ArrayList<String>();
    public static final List<String> ALL_WORD_START_CHARS = new ArrayList<String>();

    static {
    	
        try {
			// RESERVED WORDS List
			for (String reservedWord : SQLConstants.getReservedWords()) {
			    RESERVED_WORDS.add(reservedWord);
			    String start = reservedWord.substring(0, 1);
			    if (!RESERVED_WORD_START_CHARS.contains(start)) {
			        RESERVED_WORD_START_CHARS.add(start);
			    }
			}
			for (String reservedWord : SQLConstants.getNonReservedWords()) {
                RESERVED_WORDS.add(reservedWord);
                String start = reservedWord.substring(0, 1);
                if (!RESERVED_WORD_START_CHARS.contains(start)) {
                    RESERVED_WORD_START_CHARS.add(start);
                }
            }
			Collections.sort(RESERVED_WORDS);
		} catch (Exception e) {
			UiPlugin.Util.log(e);
		}

        Set allFunctionNames = new HashSet();
		Iterator iter;
		try {
			// FUNCTION NAMES List
			FunctionLibrary functionLib = UdfManager.INSTANCE.getSystemFunctionLibrary();
			List allCategories = functionLib.getFunctionCategories();
			iter = allCategories.iterator();
			while (iter.hasNext()) {
			    String catName = (String)iter.next();
			    List catFunctions = functionLib.getFunctionForms(catName);
			    Iterator funcIter = catFunctions.iterator();
			    while (funcIter.hasNext()) {
			        FunctionForm fForm = (FunctionForm)funcIter.next();
			        String fName = fForm.getName();
			        allFunctionNames.add(fName);
			        String start = fName.substring(0, 1);
			        if (!FUNCTION_NAME_START_CHARS.contains(start)) {
			            FUNCTION_NAME_START_CHARS.add(start);
			        }
			    }
			}
			FUNCTION_NAMES.addAll(allFunctionNames);
			Collections.sort(FUNCTION_NAMES);
		} catch (Exception e) {
			UiPlugin.Util.log(e);
		}

        // DATATYPE NAMES List
		Set<String> dataTypes = new HashSet<String>();
		try {
			dataTypes = DataTypeManager.getAllDataTypeNames();
			iter = dataTypes.iterator();
			while (iter.hasNext()) {
			    String dtypeName = (String)iter.next();
			    DATATYPE_NAMES.add(dtypeName);
			    String start = dtypeName.substring(0, 1);
			    if (!DATATYPE_NAME_START_CHARS.contains(start)) {
			        DATATYPE_NAME_START_CHARS.add(start);
			    }
			}
			Collections.sort(DATATYPE_NAMES);
		} catch (Exception e) {
			UiPlugin.Util.log(e);
		}

        // ALL WORDS List
        Set allWords = new HashSet();
        allWords.addAll(RESERVED_WORDS);
        allWords.addAll(allFunctionNames);
        allWords.addAll(dataTypes);
        ALL_WORDS.addAll(allWords);
        Collections.sort(ALL_WORDS);

        // All Starting Char String List
        Set allStartStrs = new HashSet();
        allStartStrs.addAll(RESERVED_WORD_START_CHARS);
        allStartStrs.addAll(FUNCTION_NAME_START_CHARS);
        allStartStrs.addAll(DATATYPE_NAME_START_CHARS);
        ALL_WORD_START_CHARS.addAll(allStartStrs);
    }

    /**
     * Determine if a character is a valid word start
     * 
     * @param c the character to test
     * @return 'true' if the character is a valid word start, 'false' if not.
     */
    public static boolean isSqlWordStart( char c ) {
        // Iterate thru the list of valid starting characters
        Iterator iter = ALL_WORD_START_CHARS.iterator();
        while (iter.hasNext()) {
            String charStr = (String)iter.next();
            if (charStr != null) {
                char start = charStr.charAt(0);
                if (c == start) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determine if a character is a valid word part
     * 
     * @param c the character to test
     * @return 'true' if the character is a valid word part, 'false' if not.
     */
    public static boolean isSqlWordPart( char c ) {
        // Valid word parts are letters, digits, underscores
        boolean isLetter = Character.isLetter(c);
        boolean isDigit = Character.isDigit(c);
        boolean isUnderscore = c == '_';
        if (isLetter || isDigit || isUnderscore) {
            return true;
        }
        return false;
    }

}
