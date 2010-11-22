/* Generated By:JavaCC: Do not edit this line. FormulaParserConstants.java */
package org.sbml.jsbml.text.parser;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface FormulaParserConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int INTEGER = 3;
  /** RegularExpression Id. */
  int NUMBER = 4;
  /** RegularExpression Id. */
  int EXPNUMBER = 5;
  /** RegularExpression Id. */
  int SLPITTER = 6;
  /** RegularExpression Id. */
  int PLUS = 7;
  /** RegularExpression Id. */
  int POWER = 8;
  /** RegularExpression Id. */
  int MINUS = 9;
  /** RegularExpression Id. */
  int TIMES = 10;
  /** RegularExpression Id. */
  int DIVIDE = 11;
  /** RegularExpression Id. */
  int LOG = 12;
  /** RegularExpression Id. */
  int FACULTY = 13;
  /** RegularExpression Id. */
  int OPEN_PAR = 14;
  /** RegularExpression Id. */
  int CLOSE_PAR = 15;
  /** RegularExpression Id. */
  int COMPARISON = 16;
  /** RegularExpression Id. */
  int STRING = 17;
  /** RegularExpression Id. */
  int EOL = 18;

  /** Lexical state. */
  int DEFAULT = 0;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "<INTEGER>",
    "<NUMBER>",
    "<EXPNUMBER>",
    "<SLPITTER>",
    "\"+\"",
    "\"^\"",
    "\"-\"",
    "\"*\"",
    "\"/\"",
    "\"log\"",
    "\"!\"",
    "\"(\"",
    "\")\"",
    "<COMPARISON>",
    "<STRING>",
    "<EOL>",
  };

}
