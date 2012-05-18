/*
 * $Id$
 * $URL$
 *
 * ---------------------------------------------------------------------------- 
 * This file is part of JSBML. Please visit <http://sbml.org/Software/JSBML> 
 * for the latest version of JSBML and more information about SBML. 
 * 
 * Copyright (C) 2009-2012 jointly by the following organizations: 
 * 1. The University of Tuebingen, Germany 
 * 2. EMBL European Bioinformatics Institute (EBML-EBI), Hinxton, UK 
 * 3. The California Institute of Technology, Pasadena, CA, USA 
 * 
 * This library is free software; you can redistribute it and/or modify it 
 * under the terms of the GNU Lesser General Public License as published by 
 * the Free Software Foundation. A copy of the license agreement is provided 
 * in the file named "LICENSE.txt" included with this software distribution 
 * and also available online as <http://sbml.org/Software/JSBML/License>. 
 * ---------------------------------------------------------------------------- 
 */ 
package org.sbml.jsbml.ext.render;

import java.text.MessageFormat;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.SBase;
import org.sbml.jsbml.ext.layout.Layout;


/**
 * @author Jakob Matthes
 * @version $Rev$
 * @since 1.0
 * @date 16.05.2012
 */
public class RenderLayoutPlugin extends AbstractRenderPlugin {
  /**
   * 
   */
  private static final long serialVersionUID = 6636572993878851570L;
  private ListOf<LocalRenderInformation> listOfLocalRenderInformation;
  
  /**
   * Creates an RenderLayoutPlugin instance 
   */
  public RenderLayoutPlugin(Layout layout) {
    super(layout);
    initDefaults();
  }


  /**
   * Clone constructor
   */
  public RenderLayoutPlugin(RenderLayoutPlugin obj) {
    super(obj);
    // TODO: copy all class attributes, e.g.:
    // bar = obj.bar;
  }


  /**
   * clones this class
   */
  public RenderLayoutPlugin clone() {
    return new RenderLayoutPlugin(this);
  }


  /**
   * Initializes the default values using the namespace.
   */
  public void initDefaults() {
//    addNamespace(RenderConstants.namespaceURI);
    // TODO: init default values here if necessary, e.g.:
    // bar = null;
  }

  public static final int MIN_SBML_LEVEL = 3;
  public static final int MIN_SBML_VERSION = 1;
  
  @Override
  public boolean getAllowsChildren() {
    return true;
  }

  @Override
  public int getChildCount() {
    int count = super.getChildCount();
    if (isSetListOfLocalRenderInformation()) {
      count++;
    }
    return count;
  }

  @Override
  public SBase getChildAt(int childIndex) {
    if (childIndex < 0) {
      throw new IndexOutOfBoundsException(childIndex + " < 0");
    }
    int pos = 0;
     if (isSetListOfLocalRenderInformation()) {
       if (pos == childIndex) {
         return getListOfLocalRenderInformation();
       }
       pos++;
     }
    throw new IndexOutOfBoundsException(MessageFormat.format(
      "Index {0,number,integer} >= {1,number,integer}", childIndex,
      +((int) Math.min(pos, 0))));
  }
  
  
  /**
   * @return <code>true</code>, if listOfLocalRenderInformation contains at least one element, 
   *         otherwise <code>false</code>
   */
  public boolean isSetListOfLocalRenderInformation() {
    if ((listOfLocalRenderInformation == null) || listOfLocalRenderInformation.isEmpty()) {
      return false;
    }
    return true;
  }


  /**
   * @return the listOfLocalRenderInformation
   */
  public ListOf<LocalRenderInformation> getListOfLocalRenderInformation() {
    if (!isSetListOfLocalRenderInformation()) {
      SBase sBase = getExtendedSBase();
      listOfLocalRenderInformation = new ListOf<LocalRenderInformation>(sBase.getLevel(), sBase.getVersion());
      listOfLocalRenderInformation.addNamespace(RenderConstants.namespaceURI);
      listOfLocalRenderInformation.setSBaseListType(ListOf.Type.other);
      sBase.registerChild(listOfLocalRenderInformation);
    }
    return listOfLocalRenderInformation;
  }
  
  /**
   * 
   * @param i
   * @return
   */
  public LocalRenderInformation getLocalRenderInformation(int i) {
    return getListOfLocalRenderInformation().get(i);
  }


  /**
   * @param listOfLocalRenderInformation
   */
  public void setListOfLocalRenderInformation(ListOf<LocalRenderInformation> listOfLocalRenderInformation) {
    unsetListOfLocalRenderInformation();
    this.listOfLocalRenderInformation = listOfLocalRenderInformation;
    getExtendedSBase().registerChild(this.listOfLocalRenderInformation);
  }


  /**
   * @return <code>true</code>, if listOfLocalRenderInformation contained at least one element, 
   *         otherwise <code>false</code>
   */
  public boolean unsetListOfLocalRenderInformation() {
    if (isSetListOfLocalRenderInformation()) {
      ListOf<LocalRenderInformation> oldLocalRenderInformation = this.listOfLocalRenderInformation;
      this.listOfLocalRenderInformation = null;
      oldLocalRenderInformation.fireNodeRemovedEvent();
      return true;
    }
    return false;
  }


  /**
   * @param field
   */
  public boolean addLocalRenderInformation(LocalRenderInformation field) {
    return getListOfLocalRenderInformation().add(field);
  }


  /**
   * @param field
   */
  public boolean removeLocalRenderInformation(LocalRenderInformation field) {
    if (isSetListOfLocalRenderInformation()) {
      return getListOfLocalRenderInformation().remove(field);
    }
    return false;
  }


  /**
   * @param i
   */
  public void removeLocalRenderInformation(int i) {
    if (!isSetListOfLocalRenderInformation()) {
      throw new IndexOutOfBoundsException(Integer.toString(i));
    }
    getListOfLocalRenderInformation().remove(i);
  }


  /**
   * TODO: if the ID is mandatory for LocalRenderInformation objects, 
   * one should also add this methods
   */
  //public void removeLocalRenderInformation(String id) {
  //  getListOfLocalRenderInformation().removeFirst(new NameFilter(id));
  //}
  /**
   * create a new LocalRenderInformation element and adds it to the ListOfLocalRenderInformation list
   * <p><b>NOTE:</b>
   * only use this method, if ID is not mandatory in LocalRenderInformation
   * otherwise use @see createLocalRenderInformation(String id)!</p>
   */
  public LocalRenderInformation createLocalRenderInformation() {
    return createLocalRenderInformation(null);
  }


  /**
   * create a new LocalRenderInformation element and adds it to the ListOfLocalRenderInformation list
   */
  public LocalRenderInformation createLocalRenderInformation(String id) {
    SBase sBase = getExtendedSBase();
    LocalRenderInformation field = new LocalRenderInformation(id, sBase.getLevel(), sBase.getVersion());
    addLocalRenderInformation(field);
    return field;
  }

  /**
   * TODO: optionally, create additional create methods with more
   * variables, for instance "bar" variable
   */
  // public LocalRenderInformation createLocalRenderInformation(String id, int bar) {
  //   LocalRenderInformation field = createLocalRenderInformation(id);
  //   field.setBar(bar);
  //   return field;
  // }
  
}
