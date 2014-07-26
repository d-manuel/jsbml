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
 * 5. The Babraham Institute, Cambridge, UK
 * 6. The University of Toronto, Toronto, ON, Canada
 * 
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation. A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available online as <http://sbml.org/Software/JSBML/License>.
 * ----------------------------------------------------------------------------
 */
package org.sbml.jsbml.math;

import org.apache.log4j.Logger;
import org.sbml.jsbml.ASTNode.Type;
import org.sbml.jsbml.MathContainer;
import org.sbml.jsbml.PropertyUndefinedError;
import org.sbml.jsbml.math.compiler.ASTNode2Compiler;
import org.sbml.jsbml.util.compilers.ASTNodeValue;

/**
 * An Abstract Syntax Tree (AST) node representing a constant number
 * in a mathematical expression.
 * 
 * @author Victor Kofia
 * @version $Rev$
 * @since 1.0
 * @date May 30, 2014
 */
public class ASTConstantNumber extends ASTNumber {

  /**
   * 
   */
  private static final long serialVersionUID = -6872240196225149289L;
  
  /**
   * A {@link Logger} for this class.
   */
  private static final Logger logger = Logger.getLogger(ASTConstantNumber.class);

  /**
   * Creates a new {@link ASTConstantNumber}.
   */
  public ASTConstantNumber() {
    super();
  }
  
  /**
   * Copy constructor; Creates a deep copy of the given {@link ASTConstantNumber}.
   * 
   * @param node
   *            the {@link ASTConstantNumber} to be copied.
   */
  public ASTConstantNumber(ASTConstantNumber node) {
    super(node);
    if (node.isSetValue()) {
      setValue(node.getValue());      
    }
  }
  
  /**
   * Creates a new {@link ASTConstantNumber} with value {@link double}.
   * 
   * @param double value
   */
  public ASTConstantNumber(double value) {
    this();
    setValue(value);
  }

  /**
   * Creates a new {@link ASTConstantNumber} of type {@link Type}. Value
   * associated with type is automatically set.
   * 
   * @param Type type
   */
  public ASTConstantNumber(Type type) {
    this();
    setType(type);
  }

  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.math.ASTNumber#clone()
   */
  @Override
  public ASTConstantNumber clone() {
    return new ASTConstantNumber(this);
  }
  
  /* (non-Javadoc)
   * @see org.sbml.jsbml.math.ASTNode2#compile(org.sbml.jsbml.util.compilers.ASTNode2Compiler)
   */
  @Override
  public ASTNodeValue compile(ASTNode2Compiler compiler) {
    ASTNodeValue value = null;
    switch(getType()) {
    case CONSTANT_PI:
      value = compiler.getConstantPi();
      break;
    case CONSTANT_E:
      value = compiler.getConstantE();
      break;
    case NAME_AVOGADRO:
      value = compiler.getConstantAvogadro();
      break;
    default: // UNKNOWN:
      value = compiler.unknownValue();
      break;
    }
    value.setType(getType());
    MathContainer parent = getParentSBMLObject();
    if (parent != null) {
      value.setLevel(parent.getLevel());
      value.setVersion(parent.getVersion());
    }
    return value;
  }
  
  /**
   * Get the value of this {@link ASTConstantNumber}
   * 
   * @return double value
   */
  public double getValue() {
    if (isSetType()) {
      switch(getType()) {
      case CONSTANT_PI:
        return Math.PI;
      case CONSTANT_E:
        return Math.E;
      case NAME_AVOGADRO:
        return 6.023e23;
      default:
        break;
      }
    }
    PropertyUndefinedError error = new PropertyUndefinedError("value", this);
    if (isStrict()) {
      throw error;
    }
    logger.warn(error);
    return Double.NaN;
  }
  
  /* (non-Javadoc)
   * @see org.sbml.jsbml.math.ASTNode2#isAllowableType(org.sbml.jsbml.ASTNode.Type)
   */
  @Override
  public boolean isAllowableType(Type type) {
    return type == Type.CONSTANT_E || type == Type.CONSTANT_PI 
        || type == Type.NAME_AVOGADRO;
  }

  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.math.AbstractASTNode#isSetType()
   */
  @Override
  public boolean isSetType() {
    return type == Type.CONSTANT_E || type == Type.CONSTANT_PI 
           || type == Type.NAME_AVOGADRO;
  }

  /**
   * Returns true iff value has been set
   * 
   * @return boolean
   */
  private boolean isSetValue() {
    return isSetType();
  }
  
  /**
   * Set the value of this ASTConstantNumber
   * 
   * @param double value
   */
  public void setValue(double value) {
    switch(Double.compare(value, Math.PI)){
    case 0:
      setType(Type.CONSTANT_PI);
      return;
    }
    switch(Double.compare(value, Math.E)){
    case 0:
      setType(Type.CONSTANT_E);
      return;
    }
    switch(Double.compare(value, 6.023e23)){
    case 0:
      setType(Type.NAME_AVOGADRO);
      return;
    }
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ASTConstantNumber [parentSBMLObject=");
    builder.append(parentSBMLObject);
    builder.append(", strict=");
    builder.append(strict);
    builder.append(", type=");
    builder.append(type);
    builder.append(", id=");
    builder.append(id);
    builder.append(", style=");
    builder.append(style);
    builder.append(", listOfListeners=");
    builder.append(listOfListeners);
    builder.append(", parent=");
    builder.append(parent);
    builder.append("]");
    return builder.toString();
  }

}
