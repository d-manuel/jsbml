/*
 * $Id$
 * $URL$
 * ----------------------------------------------------------------------------
 * This file is part of JSBML. Please visit <http://sbml.org/Software/JSBML>
 * for the latest version of JSBML and more information about SBML.
 * 
 * Copyright (C) 2009-2014  jointly by the following organizations:
 * 1. The University of Tuebingen, Germany
 * 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
 * 3. The California Institute of Technology, Pasadena, CA, USA
 * 4. The University of California, San Diego, La Jolla, CA, USA
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation. A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available online as <http://sbml.org/Software/JSBML/License>.
 * ----------------------------------------------------------------------------
 */
package org.sbml.jsbml.validator;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

import org.sbml.jsbml.SBMLDocument;

/**
 * A collection of methods for checking the validity of SBML identifiers.
 * <p>
 * This utility class provides static methods for checking the syntax of
 * identifiers and other text used in an {@link SBMLDocument}. The methods allow
 * callers to verify that {@link String}s such as SBML identifiers and XHTML
 * notes text conform to the SBML specifications.
 * 
 * @author Andreas Dr&auml;ger
 * @version $Rev$
 * @since 1.0
 * @date 28.10.2014
 */
public class SyntaxChecker {

  /**
   * The only instance of this class.
   */
  private static final SyntaxChecker syntaxChecker = new SyntaxChecker();

  /**
   * Definition of valid e-mail address {@link String}s.
   * <table>
   *   <tr>
   *     <th>Pattern</th><th>Explanation</th>
   *   </tr><tr>
   *     <td>{@code ^}                   </td><td>start of the line</td>
   *   </tr><tr>
   *     <td>{@code [_A-Za-z0-9-]+}      </td><td>must start with string in the bracket [ ], must contains one or more (+)</td>
   *   </tr><tr>
   *     <td>{@code (}                   </td><td>start of group #1</td>
   *   </tr><tr>
   *     <td>{@code \\.[_A-Za-z0-9-]+}   </td><td>follow by a dot "." and string in the bracket [ ], must contains one or more (+)</td>
   *   </tr><tr>
   *     <td>{@code )*}                  </td><td>end of group #1, this group is optional (*)</td>
   *   </tr><tr>
   *     <td>{@code @}                   </td><td>must contains a "@" symbol</td>
   *   </tr><tr>
   *     <td>{@code [A-Za-z0-9-]+}       </td><td>follow by string in the bracket [ ], must contains one or more (+)</td>
   *   </tr><tr>
   *     <td>{@code (}                   </td><td>start of group #2 - first level TLD checking</td>
   *   </tr><tr>
   *     <td>{@code \\.[A-Za-z0-9-]+}    </td><td>follow by a dot "." and string in the bracket [ ], must contains one or more (+)</td>
   *   </tr><tr>
   *     <td>{@code )*}                  </td><td>end of group #2, this group is optional (*)</td>
   *   </tr><tr>
   *     <td>{@code (}                   </td><td>start of group #3 - second level TLD checking</td>
   *   </tr><tr>
   *     <td>{@code \\.[A-Za-z]{2,}}     </td><td>follow by a dot "." and string in the bracket [ ], with minimum length of 2</td>
   *   </tr><tr>
   *     <td>{@code )}                   </td><td>end of group #3</td>
   *   </tr><tr>
   *     <td>{@code $}                   </td><td>end of the line</td>
   *   </tr>
   * </table>
   *
   * @param email
   * @return
   */
  public static boolean isValidEmailAddress(String email) {
    if (syntaxChecker.emailPattern == null) {
      syntaxChecker.emailPattern = Pattern.compile("^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$");
    }
    return syntaxChecker.emailPattern.matcher(email).matches();
  }

  /**
   * Checks whether the given idCandidate is a valid identifier according to
   * the SBML specifications.
   * 
   * @param idCandidate
   *            The {@link String} to be tested.
   * @param level
   *            Level of the SBML to be used.
   * @param version
   *            Version of the SBML to be used.
   * @return True if the argument satisfies the specification of identifiers
   *         in the SBML specifications or false otherwise.
   */
  public static final boolean isValidId(String idCandidate, int level,
    int version) {

    if (level == 1) {
      if ((version == 1) && !syntaxChecker.reservedNamesL1V1.contains(idCandidate)) {
        return syntaxChecker.SNameL1V1.matcher(idCandidate).matches();
      } else if ((version == 2) && !syntaxChecker.reservedNamesL1V2.contains(idCandidate)) {
        return syntaxChecker.SNameL1V2.matcher(idCandidate).matches();
      } else {
        // id is one of the reserved names or doesn't match the name patterns.
        return false;
      }
    }

    // level undefined or level > 1
    return syntaxChecker.SIdL2Pattern.matcher(idCandidate).matches();
  }


  /**
   * Checks if the given identifier candidate satisfies the requirements for a
   * valid meta identifier (see SBML L2V4 p. 12 for details).
   * 
   * @param idCandidate
   * @return {@code true} if the given argument is a valid meta identifier
   *         {@link String}, {@code false} otherwise.
   */
  public static final boolean isValidMetaId(String idCandidate) {
    // In the most cases the first check will be sufficient.
    if (syntaxChecker.simpleMetaIdPattern.matcher(idCandidate).matches()) {
      return true;
    }
    /* In the worst case, we have to perform two checks, but this is
     * hopefully much faster than doing the full check each time.
     * It can be assumed that the majority of allowable characters is
     * not used within these identifiers.
     */
    return syntaxChecker.idPattern.matcher(idCandidate).matches();
  }

  /**
   * Definition of valid e-mail addresses. Initialized upon first use.
   */
  private Pattern emailPattern;

  /**
   * Pattern to recognize valid meta-identifier strings for SBML elements.
   */
  private final Pattern idPattern, simpleMetaIdPattern;

  /**
   * Collections of reserved names in SBML Level 1 that must not be used as
   * identifiers (names) in SBML documents.
   */
  private final Set<String> reservedNamesL1V1, reservedNamesL1V2;

  /**
   * Pattern to recognize valid SIds, i.e., identifier strings for SBML elements.
   */
  private final Pattern SIdL2Pattern;

  /**
   * Name patterns for SBML Level 1 versions 1 and 2.
   */
  private final Pattern SNameL1V1, SNameL1V2;

  /**
   * This is a singleton class and should only be used through static methods.
   */
  private SyntaxChecker() {
    super();

    /* Build the pattern for metaIds according to the definition in SBML
     * L2V2R1 p. 12, Section 3.1.6 and the definition of the corresponding
     * symbols at http://www.w3.org/TR/2000/REC-xml-20001006#NT-CombiningChar:
     */
    String underscore = "_";
    String dash = "\\-";
    String dot = ".";
    String letter = "a-zA-Z";
    String digit = "0-9";
    String combiningChar = "[\u0300-\u0345]|[\u0360-\u0361]|[\u0483-\u0486]|[\u0591-\u05A1]|[\u05A3-\u05B9]|[\u05BB-\u05BD]|\u05BF|[\u05C1-\u05C2]|\u05C4|[\u064B-\u0652]|\u0670|[\u06D6-\u06DC]|[\u06DD-\u06DF]|[\u06E0-\u06E4]|[\u06E7-\u06E8]|[\u06EA-\u06ED]|[\u0901-\u0903]|\u093C|[\u093E-\u094C]|\u094D|[\u0951-\u0954]|[\u0962-\u0963]|[\u0981-\u0983]|\u09BC|\u09BE|\u09BF|[\u09C0-\u09C4]|[\u09C7-\u09C8]|[\u09CB-\u09CD]|\u09D7|[\u09E2-\u09E3]|\u0A02|\u0A3C|\u0A3E|\u0A3F|[\u0A40-\u0A42]|[\u0A47-\u0A48]|[\u0A4B-\u0A4D]|[\u0A70-\u0A71]|[\u0A81-\u0A83]|\u0ABC|[\u0ABE-\u0AC5]|[\u0AC7-\u0AC9]|[\u0ACB-\u0ACD]|[\u0B01-\u0B03]|\u0B3C|[\u0B3E-\u0B43]|[\u0B47-\u0B48]|[\u0B4B-\u0B4D]|[\u0B56-\u0B57]|[\u0B82-\u0B83]|[\u0BBE-\u0BC2]|[\u0BC6-\u0BC8]|[\u0BCA-\u0BCD]|\u0BD7|[\u0C01-\u0C03]|[\u0C3E-\u0C44]|[\u0C46-\u0C48]|[\u0C4A-\u0C4D]|[\u0C55-\u0C56]|[\u0C82-\u0C83]|[\u0CBE-\u0CC4]|[\u0CC6-\u0CC8]|[\u0CCA-\u0CCD]|[\u0CD5-\u0CD6]|[\u0D02-\u0D03]|[\u0D3E-\u0D43]|[\u0D46-\u0D48]|[\u0D4A-\u0D4D]|\u0D57|\u0E31|[\u0E34-\u0E3A]|[\u0E47-\u0E4E]|\u0EB1|[\u0EB4-\u0EB9]|[\u0EBB-\u0EBC]|[\u0EC8-\u0ECD]|[\u0F18-\u0F19]|\u0F35|\u0F37|\u0F39|\u0F3E|\u0F3F|[\u0F71-\u0F84]|[\u0F86-\u0F8B]|[\u0F90-\u0F95]|\u0F97|[\u0F99-\u0FAD]|[\u0FB1-\u0FB7]|\u0FB9|[\u20D0-\u20DC]|\u20E1|[\u302A-\u302F]|\u3099|\u309A";
    String extender = "\u00B7|\u02D0|\u02D1|\u0387|\u0640|\u0E46|\u0EC6|\u3005|[\u3031-\u3035]|[\u309D-\u309E]|[\u30FC-\u30FE]";
    String ncNameChar = letter + digit + dot + dash + underscore + combiningChar + extender;
    String idChar = '[' + letter + digit + underscore + ']';
    String ID = "^[" + letter + underscore + "][" + ncNameChar + "]*";
    String SIdL2 = "^[" + letter + underscore + "]" + idChar + '*';

    // Create simplified pattern for faster comparison:
    String simpleNameChar = letter + digit + dot + dash + underscore;
    String simpleID = "^[" + letter + underscore + "][" + simpleNameChar + "]*";

    SNameL1V1 = Pattern.compile(underscore + "*[" + letter + "]" + idChar + '*');
    SNameL1V2 = Pattern.compile("[" + letter + underscore + "]" + idChar + '*');
    idPattern = Pattern.compile(ID);
    simpleMetaIdPattern = Pattern.compile(simpleID);
    SIdL2Pattern = Pattern.compile(SIdL2);

    /*
     * These reserved words can occur in case of UnitDefinitions:
     * "substance", "time", "volume"
     */
    reservedNamesL1V1 = new TreeSet<String>(Arrays.asList(new String[] {
      "abs", "acos", "and", "asin", "atan", "ceil", "cos", "exp", "floor",
      "hilli", "hillmmr", "hillmr", "hillr", "isouur", "log", "log10", "massi",
      "massr", "not", "or", "ordbbr", "ordbur", "ordubr", "pow", "ppbr", "sin",
      "sqr", "sqrt", "tan", "uai", "ualii", "uar", "ucii", "ucir", "ucti",
      "uctr", "uhmi", "uhmr", "umai", "umar", "umi", "umr", "unii", "unir",
      "usii", "usir", "uuci", "uucr", "uuhr", "uui", "uur", "xor"}));

    reservedNamesL1V2 = new TreeSet<String>(reservedNamesL1V1);
    reservedNamesL1V2.add("uaii");
  }

}