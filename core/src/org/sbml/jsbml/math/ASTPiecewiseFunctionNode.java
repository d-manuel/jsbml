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

import org.sbml.jsbml.ASTNode.Type;
import org.sbml.jsbml.MathContainer;
import org.sbml.jsbml.math.compiler.ASTNode2Compiler;
import org.sbml.jsbml.util.compilers.ASTNodeValue;


/**
 * An Abstract Syntax Tree (AST) node representing a MathML piecewise
 * element in a mathematical expression.
 * 
 * @author Victor Kofia
 * @version $Rev$
 * @since 1.0
 * @date May 30, 2014
 */
public class ASTPiecewiseFunctionNode extends ASTFunction {

  /**
   * 
   */
  private static final long serialVersionUID = -8915335867694118907L;

  /**
   * Creates a new {@link ASTPiecewiseFunctionNode}.
   */
  public ASTPiecewiseFunctionNode() {
    super();
    setType(Type.FUNCTION_PIECEWISE);
  }

  /**
   * Copy constructor; Creates a deep copy of the given {@link ASTPiecewiseFunctionNode}.
   * 
   * @param node
   *            the {@link ASTPiecewiseFunctionNode} to be copied.
   */
  public ASTPiecewiseFunctionNode(ASTPiecewiseFunctionNode node) {
    super(node);
  }
  
  /*
   * (non-Javadoc)
   * @see org.sbml.jsbml.math.ASTFunction#clone()
   */
  @Override
  public ASTPiecewiseFunctionNode clone() {
    return new ASTPiecewiseFunctionNode(this);
  }
  
  /* (non-Javadoc)
   * @see org.sbml.jsbml.math.ASTNode2#compile(org.sbml.jsbml.util.compilers.ASTNode2Compiler)
   */
  @Override
  public ASTNodeValue compile(ASTNode2Compiler compiler) {
    ASTNodeValue value = null;
    value = compiler.piecewise(getChildren());
    value.setUIFlag(getChildCount() <= 1);
    value.setType(getType());
    MathContainer parent = getParentSBMLObject();
    if (parent != null) {
      value.setLevel(parent.getLevel());
      value.setVersion(parent.getVersion());
    }
    return value;
  }
  /**
   * Get the number of piece elements in this {@link ASTPiecewiseFunctionNode}
   * 
   * @return Integer numPiece
   */
  public int getNumPiece() {
    return getChildCount();
  }

  /**
   * Returns True iff piecewise element contains an otherwise element
   * 
   * @return boolean hasOtherwise
   */
  public boolean hasOtherwise() {
    return (getChildCount() / 2) > 0;
  }

  /* (non-Javadoc)
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("ASTPiecewiseFunctionNode [listOfNodes=");
    builder.append(listOfNodes);
    builder.append(", parentSBMLObject=");
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
