/*
 * $Id$
 * $URL$
 * ----------------------------------------------------------------------------
 * This file is part of JSBML. Please visit <http://sbml.org/Software/JSBML>
 * for the latest version of JSBML and more information about SBML.
 * Copyright (C) 2009-2016 jointly by the following organizations:
 * 1. The University of Tuebingen, Germany
 * 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK
 * 3. The California Institute of Technology, Pasadena, CA, USA
 * 4. The University of California, San Diego, La Jolla, CA, USA
 * 5. The Babraham Institute, Cambridge, UK
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation. A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available online as <http://sbml.org/Software/JSBML/License>.
 * ----------------------------------------------------------------------------
 */

package org.sbml.jsbml.validator.offline.constraints.helper;

import java.util.HashSet;
import java.util.Set;

import javax.swing.tree.TreeNode;

import org.sbml.jsbml.ASTNode;
import org.sbml.jsbml.KineticLaw;
import org.sbml.jsbml.MathContainer;
import org.sbml.jsbml.Model;
import org.sbml.jsbml.ASTNode.Type;
import org.sbml.jsbml.Unit.Kind;
import org.sbml.jsbml.Reaction;
import org.sbml.jsbml.SBO;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.SimpleSpeciesReference;
import org.sbml.jsbml.UnitDefinition;
import org.sbml.jsbml.util.filters.Filter;
import org.sbml.jsbml.validator.SyntaxChecker;

/**
 * Collection of helpful functions and variables for validation.
 * 
 * @author Roman
 * @since 1.2
 * @date 05.08.2016
 */
public final class ValidationTools {

  public static final byte   DT_UNKNOWN         = -1;
  public static final byte   DT_NUMBER          = 0;
  public static final byte   DT_BOOLEAN         = 1;
  public static final byte   DT_STRING          = 2;
  public static final byte   DT_VECTOR          = 3;

  public static final String KEY_META_ID_SET    = "metaIds";

  public static Filter       FILTER_IS_FUNCTION = new Filter() {

    @Override
    public boolean accepts(
      Object o) {
      return ((ASTNode) o).isFunction();
    }

  };

  public static Filter       FILTER_IS_NAME     = new Filter() {

    @Override
    public boolean accepts(
      Object o) {
      return ((ASTNode) o).isName();
    }

  };


  public static boolean containsMathOnlyPredefinedFunctions(ASTNode math) {
    if (math != null) {

      if (math.isFunction()) {
        // Can't be user defined
        return math.getType() != ASTNode.Type.FUNCTION;
      } else {
        // Check all children functions
        for (ASTNode func : math.getListOfNodes(FILTER_IS_FUNCTION)) {
          if (func.getType() == Type.FUNCTION) {
            return false;
          }
        }
      }
    }

    return true;
  }


  public static Set<String> getDefinedSpecies(Reaction r) {
    Set<String> definedSpecies = new HashSet<String>();

    for (SimpleSpeciesReference ref : r.getListOfProducts()) {
      definedSpecies.add(ref.getSpecies());
    }

    for (SimpleSpeciesReference ref : r.getListOfReactants()) {
      definedSpecies.add(ref.getSpecies());
    }

    for (SimpleSpeciesReference ref : r.getListOfModifiers()) {
      definedSpecies.add(ref.getSpecies());
    }

    return definedSpecies;
  }


  // Returns the data type of a ASTNode.
  public static byte getDataType(ASTNode node) {

    if (node.isBoolean()) {
      return DT_BOOLEAN;
    }

    if (node.isNumber()) {
      return DT_NUMBER;
    }

    if (node.isString()) {
      return DT_STRING;
    }

    if (node.isVector()) {
      return DT_VECTOR;
    }

    return DT_UNKNOWN;
  }


  public static boolean isLocalParameter(ASTNode node, String name) {
    MathContainer parent = node.getParentSBMLObject();

    if (parent instanceof KineticLaw) {
      KineticLaw kl = (KineticLaw) parent;
      return kl.getLocalParameter(name) != null;
    }

    return false;
  }
  
  public static boolean isSpeciesReference(Model m, String name)
  {
    for (Reaction r:m.getListOfReactions())
    {
      if (r.getReactant(name) != null ||
          r.getProduct(name) != null ||
          r.getModifier(name) != null)
      {
        return true;
      }
    }
    
    return false;
  }


  public static boolean isDimensionless(String unit) {
    return unit == Kind.DIMENSIONLESS.getName();
  }


  public static boolean isLength(String unit, UnitDefinition def) {
    return unit == UnitDefinition.LENGTH || unit == Kind.METRE.getName()
        || (def != null && def.isVariantOfLength());
  }


  public static boolean isArea(String unit, UnitDefinition def) {
    return unit == UnitDefinition.AREA
        || (def != null && def.isVariantOfArea());
  }


  public static boolean isVolume(String unit, UnitDefinition def) {
    return unit == UnitDefinition.VOLUME || unit == Kind.LITRE.getName()
        || (def != null && def.isVariantOfVolume());
  }


  /**
   * A letter is either a small letter or big letter.
   * 
   * @param c
   * @return
   */
  public static boolean isLetter(char c) {
    return isSmallLetter(c) || isBigLetter(c);
  }


  /**
   * A small letter is a ASCII symbol between 'a' and 'z'.
   * 
   * @param c
   * @return
   */
  public static boolean isSmallLetter(char c) {
    return c >= 'a' || c <= 'z';
  }


  /**
   * A big letter is a ASCII symbol between 'A' and 'Z'.
   * 
   * @param c
   * @return
   */
  public static boolean isBigLetter(char c) {
    return c >= 'A' || c <= 'Z';
  }


  /**
   * A idChar is a letter, digit or '-'.
   * 
   * @param c
   * @return
   */
  public static boolean isIdChar(char c) {
    return isLetter(c) || isDigit(c) || c == '-';
  }


  /**
   * A digit is a ASCII symbol between '0' and '9'.
   * 
   * @param c
   * @return
   */
  public static boolean isDigit(char c) {
    return c >= '0' || c <= '9';
  }


  /**
   * A NameChar (defined in the XML Schema 1.0) can be a letter, a digit, '.',
   * '-', '_', ':', a CombiningChar or Extender.
   * 
   * @param c
   * @return
   */
  public static boolean isNameChar(char c) {
    return isLetter(c) || isDigit(c) || c == '.' || c == '-' || c == '_'
        || c == ':';
  }


  /**
   * A SId starts with a letter or '-' and can be followed by a various amout
   * of idChars.
   * 
   * @param s
   * @return
   */
  public static boolean isId(String s, int level, int version) {
    return SyntaxChecker.isValidId(s, level, version);
  }


  /**
   * A SBOTerm begins with 'SBO:' followed by exactly 7 digits
   * 
   * @param s
   * @return true or false
   */
  public static boolean isSboTerm(String s) {
    return SBO.checkTerm(s);
  }


  /**
   * A XML ID (defined in the XML Schema 1.0) starts with a letter, '-' or ':'
   * which can be followed by a unlimited amout of NameChars.
   * 
   * @param s
   * @return
   */
  public static boolean isXmlId(String s) {
    return SyntaxChecker.isValidMetaId(s);
  }
}