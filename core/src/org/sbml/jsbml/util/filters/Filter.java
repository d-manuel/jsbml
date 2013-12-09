/*
 * $Id$
 * $URL$
 * ----------------------------------------------------------------------------
 * This file is part of JSBML. Please visit <http://sbml.org/Software/JSBML>
 * for the latest version of JSBML and more information about SBML.
 *
 * Copyright (C) 2009-2013 jointly by the following organizations:
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

package org.sbml.jsbml.util.filters;

/**
 * A filter is a general interface that allows to check an object for a certain
 * property. An implementing class may check for the type of the object and then
 * check some of its field values.
 * 
 * @author Andreas Dr&auml;ger
 * @date 2010-05-19
 * @since 0.8
 * @version $Rev$
 */
public interface Filter {

	/**
	 * This method checks whether the given object is of the correct type and
	 * has the desired properties set to be acceptable.
	 * 
	 * @param o
	 *            some object whose properties are to be checked.
	 * @return True if the object is sufficient to be acceptable or false if at
	 *         least one of its properties or its class name does not fit into
	 *         this filter criterion.
	 */
	public boolean accepts(Object o);

}
