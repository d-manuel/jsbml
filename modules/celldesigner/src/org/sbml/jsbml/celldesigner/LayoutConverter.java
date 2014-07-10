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
 * 6. Marquette University, Milwaukee, WI, USA
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation. A copy of the license agreement is provided
 * in the file named "LICENSE.txt" included with this software distribution
 * and also available online as <http://sbml.org/Software/JSBML/License>.
 * ----------------------------------------------------------------------------
 */
package org.sbml.jsbml.celldesigner;

import java.awt.geom.Point2D;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import jp.sbi.celldesigner.plugin.PluginCompartment;
import jp.sbi.celldesigner.plugin.PluginReaction;
import jp.sbi.celldesigner.plugin.PluginSpecies;
import jp.sbi.celldesigner.plugin.PluginSpeciesAlias;
import jp.sbi.celldesigner.plugin.DataObject.PluginRealLineInformationDataObjOfReactionLink;
import jp.sbi.celldesigner.plugin.util.PluginSystemOutUtils;

import org.sbml.jsbml.ListOf;
import org.sbml.jsbml.ext.layout.BoundingBox;
import org.sbml.jsbml.ext.layout.CompartmentGlyph;
import org.sbml.jsbml.ext.layout.Curve;
import org.sbml.jsbml.ext.layout.Layout;
import org.sbml.jsbml.ext.layout.LineSegment;
import org.sbml.jsbml.ext.layout.Point;
import org.sbml.jsbml.ext.layout.SpeciesGlyph;
import org.sbml.jsbml.ext.layout.SpeciesReferenceGlyph;
import org.sbml.jsbml.ext.layout.SpeciesReferenceRole;
import org.sbml.jsbml.ext.layout.TextGlyph;


/**
 * @author Ibrahim Vazirabad
 * @version $Rev$
 * @since 1.0
 * @date Jun 19, 2014
 */
public class LayoutConverter {

  final static double depth  =  1d;
  final static double z  =  0d;

  /**
   * Extracts Compartment Layout data from CellDesigner
   * @param pCompartment the PluginCompartment
   * @param layout The Layout object
   */
  public static void extractLayout(PluginCompartment pCompartment, Layout layout)
  {
    CompartmentGlyph cGlyph  =  layout.createCompartmentGlyph("cGlyph_" + pCompartment.getId());
    cGlyph.createBoundingBox(pCompartment.getWidth(), pCompartment.getHeight(), depth, pCompartment.getX(), pCompartment.getY(), z);

    TextGlyph tGlyph = layout.createTextGlyph("tGlyph_" + pCompartment.getId());
    BoundingBox bBox  =  tGlyph.createBoundingBox();
    bBox.createPosition(pCompartment.getNamePositionX(), pCompartment.getNamePositionY(), z);
    tGlyph.setOriginOfText(pCompartment.getId());
  }

  /**
   * Extracts Species Layout data from CellDesigner
   * @param pSpecies the PluginSpeciesAlias
   * @param layout The Layout object
   */
  public static void extractLayout(PluginSpeciesAlias pSpeciesAlias, Layout layout)
  {
    SpeciesGlyph sGlyph = layout.createSpeciesGlyph("sGlyph_" + pSpeciesAlias.getSpecies().getId());
    sGlyph.createBoundingBox(pSpeciesAlias.getWidth(), pSpeciesAlias.getHeight(), depth, pSpeciesAlias.getX(), pSpeciesAlias.getY(), z);

    TextGlyph tGlyph = layout.createTextGlyph("tGlyph_" + pSpeciesAlias.getSpecies().getId());
    BoundingBox bBox  =  tGlyph.createBoundingBox(pSpeciesAlias.getWidth(), pSpeciesAlias.getHeight(), depth);
    //setting textGlyph position at center of speciesGlyph
    bBox.createPosition(pSpeciesAlias.getX()+pSpeciesAlias.getWidth()/2, pSpeciesAlias.getY()+pSpeciesAlias.getHeight()/2, z);
    tGlyph.setOriginOfText(pSpeciesAlias.getSpecies().getId());
  }

  /**
   * Extracts Reaction Layout data from CellDesigner
   * @param pReaction  the PluginReaction
   * @param layout The Layout object
   */
  public static void extractLayout(PluginReaction pReaction, Layout layout)
  {
    //create ReactionGlyph
    layout.createReactionGlyph("rGlyph_"+pReaction.getId());

    //create SpeciesReferenceGlyphs for reactants
    for (int i = 0;i<pReaction.getNumReactants();i++)
    {
      SpeciesReferenceGlyph srGlyph = new SpeciesReferenceGlyph("srGlyphReactant_"+pReaction.getId()+"_"+
          pReaction.getReactant(i).getSpeciesInstance().getId());
      srGlyph.setRole(SpeciesReferenceRole.SUBSTRATE);

      PluginSpecies pSpecies = pReaction.getReactant(i).getSpeciesInstance();
      srGlyph.setSpeciesGlyph("sGlyph_"+pSpecies.getId());
      layout.getReactionGlyph("rGlyph_"+pReaction.getId()).addSpeciesReferenceGlyph(srGlyph);
    }

    //create SpeciesReferenceGlyphs for products
    for (int i = 0;i<pReaction.getNumProducts();i++)
    {
      SpeciesReferenceGlyph srGlyph = new SpeciesReferenceGlyph("srGlyphProduct_" + pReaction.getId() + "_" +
          pReaction.getProduct(i).getSpeciesInstance().getId());
      srGlyph.setRole(SpeciesReferenceRole.PRODUCT);

      PluginSpecies pSpecies = pReaction.getProduct(i).getSpeciesInstance();

      srGlyph.setSpeciesGlyph("sGlyph_"+pSpecies.getId());
      layout.getReactionGlyph("rGlyph_"+pReaction.getId()).addSpeciesReferenceGlyph(srGlyph);
    }

    //create SpeciesReferenceGlyphs for modifiers
    for (int i = 0;i<pReaction.getNumModifiers();i++)
    {
      SpeciesReferenceGlyph srGlyph = new SpeciesReferenceGlyph("srGlyphModifier_" + pReaction.getId()+ "_" +
          pReaction.getModifier(i).getSpeciesInstance().getId());
      srGlyph.setRole(SpeciesReferenceRole.MODIFIER);

      PluginSpecies pSpecies = pReaction.getModifier(i).getSpeciesInstance();
      srGlyph.setSpeciesGlyph("sGlyph_"+pSpecies.getId());
      layout.getReactionGlyph("rGlyph_"+pReaction.getId()).addSpeciesReferenceGlyph(srGlyph);
    }

    TextGlyph tGlyph = layout.createTextGlyph("tGlyph_" + pReaction.getId());
    // tGlyph.createBoundingBox(15, 10, depth, reactionXCoordinate-10, reactionYCoordinate-10, z);
    tGlyph.setOriginOfText(pReaction.getId());

    //starting the reaction position algorithm
    PluginRealLineInformationDataObjOfReactionLink inputInfo = pReaction.getAllMyPostionInfomations();
    if (inputInfo.getPointsInLine() != null) {
      //grabbing main reaction axis (usually first Vector in getPointsInLine()
      Vector firstReactionPoints = (Vector)inputInfo.getPointsInLine().get(0);
      ListOf<SpeciesReferenceGlyph> list = layout.getReactionGlyph("rGlyph_"+pReaction.getId()).getListOfSpeciesReferenceGlyphs();
      if ((firstReactionPoints != null) && (firstReactionPoints.size() > 0)) {
        SpeciesReferenceGlyph srGlyph;
        Curve curve;
        LineSegment reactantSegment = new LineSegment();
        LineSegment productSegment = new LineSegment();

        if (firstReactionPoints.size()==2 && inputInfo.getMidPointInLine()!=null && inputInfo.getPointsInLine().size()==3)
        {
          //need to get all points because 3 pronged reactions seem to be programmed oddly
          List<Point2D.Double> allReactionPoints = new Vector<Point2D.Double>();
          for (int i=0; i<inputInfo.getPointsInLine().size(); i++)
          {
            Vector vector = (Vector)inputInfo.getPointsInLine().get(i);
            for (Object pointDouble: vector)
            {
              if (pointDouble instanceof Point2D.Double) {
                allReactionPoints.add((Point2D.Double) pointDouble);
              }
            }
          }
          Point2D.Double reactionMidpoint = inputInfo.getMidPointInLine(); //centerpoint of reaction

          if (pReaction.getNumReactants()>pReaction.getNumProducts()) //6 points divided by 2 = 3 coordinates
          {
            LineSegment reactantSegment2 = new LineSegment();
            reactantSegment.createStart(allReactionPoints.get(1).x, allReactionPoints.get(1).y, z);
            reactantSegment.createEnd(reactionMidpoint.x, reactionMidpoint.y, z);
            reactantSegment2.createStart(allReactionPoints.get(3).x, allReactionPoints.get(3).y, z);
            reactantSegment2.createEnd(reactionMidpoint.x, reactionMidpoint.y, z);
            productSegment.createStart(reactionMidpoint.x, reactionMidpoint.y, z);
            productSegment.createEnd(allReactionPoints.get(5).x, allReactionPoints.get(5).y, z);
            //first reactant
            srGlyph = list.get("srGlyphReactant_"+pReaction.getId()+"_"+pReaction.getReactant(0).getSpeciesInstance().getId());
            curve = new Curve();
            curve.addCurveSegment(reactantSegment);
            srGlyph.setCurve(curve);
            //first product
            srGlyph = list.get("srGlyphProduct_"+pReaction.getId()+"_"+pReaction.getProduct(0).getSpeciesInstance().getId());
            curve = new Curve();
            curve.addCurveSegment(productSegment);
            srGlyph.setCurve(curve);
            //second reactant
            srGlyph = list.get("srGlyphReactant_"+pReaction.getId()+"_"+pReaction.getReactant(1).getSpeciesInstance().getId());
            curve = new Curve();
            curve.addCurveSegment(reactantSegment2);
            srGlyph.setCurve(curve);
            //curve will now describe ReactionGlyph
            curve = new Curve();
            LineSegment rGlyphSegment=new LineSegment();
            rGlyphSegment.setStart(new Point((allReactionPoints.get(4).x+allReactionPoints.get(5).x)/2-6,
              (allReactionPoints.get(4).y+allReactionPoints.get(5).y)/2));
            rGlyphSegment.setEnd(new Point((allReactionPoints.get(4).x+allReactionPoints.get(5).x)/2+6,
              (allReactionPoints.get(4).y+allReactionPoints.get(5).y)/2));
            curve.addCurveSegment(rGlyphSegment);
            layout.getReactionGlyph("rGlyph_"+pReaction.getId()).setCurve(curve);
          }

          else if (pReaction.getNumReactants()<pReaction.getNumProducts())
          {
            LineSegment productSegment2 = new LineSegment();
            reactantSegment.createStart(reactionMidpoint.x, reactionMidpoint.y, z);
            reactantSegment.createEnd(allReactionPoints.get(1).x, allReactionPoints.get(1).y, z);
            productSegment.createStart(reactionMidpoint.x, reactionMidpoint.y, z);
            productSegment.createEnd(allReactionPoints.get(3).x, allReactionPoints.get(3).y, z);
            productSegment2.createStart(reactionMidpoint.x, reactionMidpoint.y, z);
            productSegment2.createEnd(allReactionPoints.get(5).x, allReactionPoints.get(5).y, z);
            //first reactant
            srGlyph = list.get("srGlyphReactant_"+pReaction.getId()+"_"
                +pReaction.getReactant(0).getSpeciesInstance().getId());
            curve = new Curve();
            curve.addCurveSegment(reactantSegment);
            srGlyph.setCurve(curve);
            //first product
            srGlyph = list.get("srGlyphProduct_"+pReaction.getId()+"_"+pReaction.getProduct(0).getSpeciesInstance().getId());
            curve = new Curve();
            curve.addCurveSegment(productSegment);
            srGlyph.setCurve(curve);
            //second product
            srGlyph = list.get("srGlyphProduct_"+pReaction.getId()+"_"+pReaction.getProduct(1).getSpeciesInstance().getId());
            curve = new Curve();
            curve.addCurveSegment(productSegment2);
            srGlyph.setCurve(curve);
            //curve will now describe ReactionGlyph
            curve = new Curve();
            LineSegment rGlyphSegment=new LineSegment();
            rGlyphSegment.setStart(new Point((allReactionPoints.get(0).x+allReactionPoints.get(1).x)/2-6,
              (allReactionPoints.get(0).y+allReactionPoints.get(1).y)/2));
            rGlyphSegment.setEnd(new Point((allReactionPoints.get(0).x+allReactionPoints.get(1).x)/2+6,
              (allReactionPoints.get(0).y+allReactionPoints.get(1).y)/2));
            curve.addCurveSegment(rGlyphSegment);
            layout.getReactionGlyph("rGlyph_"+pReaction.getId()).setCurve(curve);
          }
        }

        //reactions with only a start and end point
        else if (firstReactionPoints.size()==2 && inputInfo.getPointsInLine().size()==1)
        {
          //JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea("Hello, you are in 2")));
          Point2D.Double coordinatesReactant = (Point2D.Double) firstReactionPoints.get(0);
          Point2D.Double coordinatesProduct = (Point2D.Double) firstReactionPoints.get(1);
          int modifier=0;
          if (coordinatesReactant.x<=coordinatesProduct.x) { //making room for reactionGlyph (which is 12px in CellDesigner)
            modifier = 6;
          } else {
            modifier = -6;
          }
          reactantSegment = new LineSegment();
          reactantSegment.setStart(new Point(coordinatesReactant.x, coordinatesReactant.y, z));
          reactantSegment.setEnd(new Point((coordinatesReactant.x+coordinatesProduct.x)/2-modifier,
            (coordinatesReactant.y+coordinatesProduct.y)/2, z));
          productSegment.createStart((coordinatesReactant.x+coordinatesProduct.x)/2+modifier,
            (coordinatesReactant.y+coordinatesProduct.y)/2, z);
          productSegment.createEnd(coordinatesProduct.x, coordinatesProduct.y, z);
          //srGlyph will be first reactant
          srGlyph = list.get("srGlyphReactant_"+pReaction.getId()+"_"+pReaction.getReactant(0).getSpeciesInstance().getId());
          curve = new Curve();
          curve.addCurveSegment(reactantSegment);
          srGlyph.setCurve(curve);
          //srGlyph will be first product
          srGlyph = list.get("srGlyphProduct_"+pReaction.getId()+"_"+pReaction.getProduct(0).getSpeciesInstance().getId());
          curve = new Curve();
          curve.addCurveSegment(productSegment);
          srGlyph.setCurve(curve);
          //curve will now describe ReactionGlyph
          curve = new Curve();
          LineSegment rGlyphSegment=new LineSegment();
          rGlyphSegment.setStart(reactantSegment.getEnd());
          rGlyphSegment.setEnd(productSegment.getStart());
          curve.addCurveSegment(rGlyphSegment);
          layout.getReactionGlyph("rGlyph_"+pReaction.getId()).setCurve(curve);
        }

        //reactions with four points describing the reaction (common type)
        else if (firstReactionPoints.size()==4)
        {
          //JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea("Hello, you are in 4")));
          List<Point2D.Double> listOfCoordinates = firstReactionPoints;
          reactantSegment.createStart(listOfCoordinates.get(0).x, listOfCoordinates.get(0).y, z);
          reactantSegment.createEnd(listOfCoordinates.get(1).x, listOfCoordinates.get(1).y, z);
          productSegment.createStart(listOfCoordinates.get(2).x, listOfCoordinates.get(2).y, z);
          productSegment.createEnd(listOfCoordinates.get(3).x, listOfCoordinates.get(3).y, z);
          //first reactant
          srGlyph = list.get("srGlyphReactant_"+pReaction.getId()+"_"+pReaction.getReactant(0).getSpeciesInstance().getId());
          curve = new Curve();
          curve.addCurveSegment(reactantSegment);
          srGlyph.setCurve(curve);
          //first product
          srGlyph = list.get("srGlyphProduct_"+pReaction.getId()+"_"+pReaction.getProduct(0).getSpeciesInstance().getId());
          curve = new Curve();
          curve.addCurveSegment(productSegment);
          srGlyph.setCurve(curve);
          //curve will now describe ReactionGlyph
          curve = new Curve();
          LineSegment rGlyphSegment=new LineSegment();
          rGlyphSegment.setStart(new Point((reactantSegment.getEnd().x()+productSegment.getStart().x())/2-6,
            (reactantSegment.getEnd().y()+productSegment.getStart().y())/2));
          rGlyphSegment.setEnd(new Point((reactantSegment.getEnd().x()+productSegment.getStart().x())/2+6,
            (reactantSegment.getEnd().y()+productSegment.getStart().y())/2));
          curve.addCurveSegment(rGlyphSegment);
          layout.getReactionGlyph("rGlyph_"+pReaction.getId()).setCurve(curve);
        }

        //collecting GLogicGate information
        for (int i = 0; i < inputInfo.getReactionLinkMembers().size(); i++)
        {
          // JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea(""+inputInfo.getReactionLinkMembers().size())));
          PluginRealLineInformationDataObjOfReactionLink rtnLinesInfo =
              (PluginRealLineInformationDataObjOfReactionLink)inputInfo.getReactionLinkMembers().get(i);

          if (rtnLinesInfo.getPointsInLine().size()>0) {
            firstReactionPoints = (Vector) rtnLinesInfo.getPointsInLine().get(0);
            JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea("i: "+i+"\tnumModifiers: "+pReaction.getNumModifiers())));
            if (pReaction.getNumReactants()>1) //that means that firstReactionPoints is current a reactant
            {
              if (firstReactionPoints.size()==2)
              {

                Point2D.Double reactantBegin = (Point2D.Double) firstReactionPoints.get(0);
                Point2D.Double reactantEnd = (Point2D.Double) firstReactionPoints.get(1);
                reactantSegment.createStart(reactantBegin.x, reactantBegin.y, z);
                reactantSegment.createEnd(reactantEnd.x, reactantBegin.y, z);
                //adding next reactant
                for (int j = 0; j < pReaction.getNumReactants(); j++)
                {
                  srGlyph = list.get("srGlyphReactant_"+pReaction.getId()+"_"
                      +pReaction.getReactant(j).getSpeciesInstance().getId());
                  if (!srGlyph.isSetCurve())
                  {
                    curve = new Curve();
                    curve.addCurveSegment(reactantSegment);
                    srGlyph.setCurve(curve);
                    break;
                  }
                }
              }
              else if (firstReactionPoints.size()>2)
              {
                Point2D.Double reactant1 = (Point2D.Double) firstReactionPoints.get(0);
                Point2D.Double reactant2 = (Point2D.Double) firstReactionPoints.get(1);
                LineSegment segment = new LineSegment();
                segment.createStart(reactant1.x, reactant1.y, z);
                segment.createEnd(reactant2.x, reactant2.y, z);
                curve = new Curve();
                curve.addCurveSegment(segment);
                for (int j = 1; j<firstReactionPoints.size()-1; j++)
                {
                  reactant1 = (Point2D.Double) firstReactionPoints.get(j);
                  reactant2 = (Point2D.Double) firstReactionPoints.get(j+1);
                  segment = new LineSegment();
                  segment.createStart(reactant1.x, reactant1.y, z);
                  segment.createEnd(reactant2.x, reactant2.y, z);
                  curve.addCurveSegment(segment);
                }
                JOptionPane.showMessageDialog(null, new JScrollPane(new JTextArea("i: "+i+"\tnumReactants: "+pReaction.getNumReactants())));
                for (int j = 0; j < pReaction.getNumReactants(); j++)
                {
                  srGlyph = list.get("srGlyphReactant_"+pReaction.getId()+"_"
                      +pReaction.getReactant(j).getSpeciesInstance().getId());
                  if (!srGlyph.isSetCurve())
                  {
                    srGlyph.setCurve(curve);
                    break;
                  }
                }
              }
            }
            else if (pReaction.getNumReactants()>=1 || pReaction.getNumProducts()>1)
            {
              if (firstReactionPoints.size()==2)
              {
                Point2D.Double productBegin = (Point2D.Double) firstReactionPoints.get(0);
                Point2D.Double productEnd = (Point2D.Double) firstReactionPoints.get(1);

                reactantSegment.createStart(productBegin.x, productBegin.y, z);
                reactantSegment.createEnd(productEnd.x, productBegin.y, z);
                //adding next product
                for (int j = 0; j < pReaction.getNumProducts(); j++)
                {
                  srGlyph = list.get("srGlyphProduct_"+pReaction.getId()+"_"
                      +pReaction.getProduct(j).getSpeciesInstance().getId());
                  if (!srGlyph.isSetCurve())
                  {
                    curve = new Curve();
                    curve.addCurveSegment(reactantSegment);
                    srGlyph.setCurve(curve);
                    break;
                  }
                }
              }
              else if (firstReactionPoints.size()>2)
              {
                Point2D.Double product1 = (Point2D.Double) firstReactionPoints.get(0);
                Point2D.Double product2 = (Point2D.Double) firstReactionPoints.get(1);
                LineSegment segment = new LineSegment();
                segment.createStart(product1.x, product1.y, z);
                segment.createEnd(product2.x, product2.y, z);
                curve = new Curve();
                curve.addCurveSegment(segment);
                for (int j = 1; j<firstReactionPoints.size()-1; j++)
                {
                  product1 = (Point2D.Double) firstReactionPoints.get(i);
                  product2 = (Point2D.Double) firstReactionPoints.get(i+1);
                  segment = new LineSegment();
                  segment.createStart(product1.x, product1.y, z);
                  segment.createEnd(product2.x, product2.y, z);
                  curve.addCurveSegment(segment);
                }

                for (int j = 0; j < pReaction.getNumProducts(); j++)
                {
                  srGlyph = list.get("srGlyphProduct_"+pReaction.getId()+"_"
                      +pReaction.getProduct(j).getSpeciesInstance().getId());
                  if (!srGlyph.isSetCurve())
                  {
                    srGlyph.setCurve(curve);
                    break;
                  }
                }
              }
            }
            else if (pReaction.getNumModifiers()>0)
            {
              if (firstReactionPoints.size()==2)
              {
                Point2D.Double modifierBegin = (Point2D.Double) firstReactionPoints.get(0);
                Point2D.Double modifierEnd = (Point2D.Double) firstReactionPoints.get(1);

                reactantSegment.createStart(modifierBegin.x, modifierBegin.y, z);
                reactantSegment.createEnd(modifierEnd.x, modifierBegin.y, z);
                //adding modifier
                for (int j = 0; j < pReaction.getNumModifiers(); j++)
                {
                  srGlyph = list.get("srGlyphModifier_"+pReaction.getId()+"_"
                      +pReaction.getModifier(j).getSpeciesInstance().getId());
                  if (!srGlyph.isSetCurve())
                  {
                    curve = new Curve();
                    curve.addCurveSegment(reactantSegment);
                    srGlyph.setCurve(curve);
                    break;
                  }
                }
              }
              else if (firstReactionPoints.size()>2)
              {
                Point2D.Double modifier1 = (Point2D.Double) firstReactionPoints.get(0);
                Point2D.Double modifier2 = (Point2D.Double) firstReactionPoints.get(1);
                LineSegment segment = new LineSegment();
                segment.createStart(modifier1.x, modifier1.y, z);
                segment.createEnd(modifier2.x, modifier2.y, z);
                curve = new Curve();
                curve.addCurveSegment(segment);
                for (int j = 1; j<=firstReactionPoints.size()-1; j++)
                {
                  modifier1 = (Point2D.Double) firstReactionPoints.get(i);
                  modifier2 = (Point2D.Double) firstReactionPoints.get(i+1);
                  segment = new LineSegment();
                  segment.createStart(modifier1.x, modifier1.y, z);
                  segment.createEnd(modifier2.x, modifier2.y, z);
                  curve.addCurveSegment(segment);
                }
                for (int j = 0; j < pReaction.getNumModifiers(); j++)
                {
                  srGlyph = list.get("srGlyphModifier_"+pReaction.getId()+"_"
                      +pReaction.getModifier(j).getSpeciesInstance().getId());
                  if (!srGlyph.isSetCurve())
                  {
                    srGlyph.setCurve(curve);
                    break;
                  }
                }
              }
            }
          }
        }
      }
    }
    PrintStream oldOut = System.out;
    JTextArea textArea = new JTextArea();
    try {
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      PrintStream newOut = new PrintStream(baos);

      System.setOut(newOut);
      PluginSystemOutUtils.printDebugInfoOfRealLineInformationOfReactionLink(inputInfo, 0);
      textArea.setText(pReaction.getId()+"\n"+baos.toString());
    } finally {
      System.setOut(oldOut);
    }
    //JOptionPane.showMessageDialog(null, new JScrollPane(textArea));
  }
}

