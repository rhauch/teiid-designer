/*
 * "The Java Developer's Guide to Eclipse"
 *   by D'Anjou, Fairbrother, Kehn, Kellerman, McCarthy
 * 
 * (C) Copyright International Business Machines Corporation, 2003, 2004. 
 * All Rights Reserved.
 * 
 * Code or samples provided herein are provided without warranty of any kind.
 */ 
package com.metamatrix.query.internal.ui.sqleditor.sql;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.RuleBasedScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.rules.WhitespaceRule;
import org.eclipse.jface.text.rules.WordRule;
import org.eclipse.swt.SWT;

import com.metamatrix.ui.graphics.ColorManager;

/**
 * The SQLCodeScanner is a RuleBaseScanner.This class finds SQL comments and
 * keywords, as the user edits the document. It is "programmed" with a sequence
 * of rules that evaluates and returns the offset and the length of the last
 * found token.
 */
public class SqlCodeScanner extends RuleBasedScanner {

	/**
	 * Constructor for SQLCodeScanner.
	 * The SQLCodeScanner, is a RuleBaseScanner. The code scanner creates tokens 
	 * for keywords, types, and constants. The token is constructed with a 
	 * TextAttribute. The TextAttribute is constructed with a color and font. 
	 * A list of rules with the corresponding token are created. The method ends
	 * with setting the scanner�s set of rules
	 */
	public SqlCodeScanner(ColorManager colorManager) {
        
        // *** NOTE:  DONT USE SWT.BOLD for Keyword,Datatype,Function
        //            Word wrap doesnt work right for mixture of NORMAL AND BOLD 
        //--------------------------------------
        // Keyword TextAttributes
        //--------------------------------------
		IToken keyword =
			new Token(
				new TextAttribute(
					colorManager.getColor(ColorManager.KEYWORD),
                    null,
					SWT.NORMAL));
        //--------------------------------------
        // Datatype TextAttributes
        //--------------------------------------
        IToken datatype =
            new Token(
                new TextAttribute(
                    colorManager.getColor(ColorManager.DATATYPE),
                    null,
                    SWT.NORMAL));
        //--------------------------------------
        // Function TextAttributes
        //--------------------------------------
        IToken function =
            new Token(
                new TextAttribute(
                    colorManager.getColor(ColorManager.FUNCTION),
                    null,
                    SWT.NORMAL));
        //--------------------------------------
        // String TextAttributes
        //--------------------------------------
		IToken string =
			new Token(
				new TextAttribute(
                    colorManager.getColor(ColorManager.STRING),
                    null,
                    SWT.NORMAL));
        //--------------------------------------
        // Comment TextAttributes
        //--------------------------------------
		IToken comment =
			new Token(
				new TextAttribute(
                    colorManager.getColor(ColorManager.SINGLE_LINE_COMMENT),
                    null,
                    SWT.NORMAL));
        //--------------------------------------
        // Default TextAttributes
        //--------------------------------------
		IToken other =
			new Token(
				new TextAttribute(colorManager.getColor(ColorManager.DEFAULT),
                    null,
                    SWT.NORMAL));

		setDefaultReturnToken(other);
		List<IRule> rules = new ArrayList<IRule>();

		// Add rule for single line comments.
		rules.add(new EndOfLineRule("//", comment)); //$NON-NLS-1$

		// Add rule for strings and character constants.
		rules.add(new SingleLineRule("\"", "\"", string, '\\')); //$NON-NLS-1$ //$NON-NLS-2$ 
		rules.add(new SingleLineRule("'", "'", string, '\\')); //$NON-NLS-1$ //$NON-NLS-2$ 
 
		// Add generic whitespace rule.
		rules.add(new WhitespaceRule(new SqlWhiteSpaceDetector()));

		// Add word rule for keywords, datatypes, and function names.
		WordRule wordRule = new CaseInsensitiveWordRule(new SqlWordDetector(), other);
        for (int i = 0; i < SqlSyntax.RESERVED_WORDS.size(); i++)
            wordRule.addWord(SqlSyntax.RESERVED_WORDS.get(i), keyword);
        for (int i = 0; i < SqlSyntax.DATATYPE_NAMES.size(); i++)
            wordRule.addWord(SqlSyntax.DATATYPE_NAMES.get(i), datatype);
        for (int i = 0; i < SqlSyntax.FUNCTION_NAMES.size(); i++)
            wordRule.addWord(SqlSyntax.FUNCTION_NAMES.get(i), function);
		rules.add(wordRule);

		IRule[] result = new IRule[rules.size()];
		rules.toArray(result);
		setRules(result);

	}

}
