/* A SchematicElement is an XML element that can appear on Ptoley schematics

 Copyright (c) 1998 The Regents of the University of California.
 All rights reserved.
 Permission is hereby granted, without written agreement and without
 license or royalty fees, to use, copy, modify, and distribute this
 software and its documentation for any purpose, provided that the above
 copyright notice and the following two paragraphs appear in all copies
 of this software.

 IN NO EVENT SHALL THE UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY
 FOR DIRECT, INDIRECT, SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES
 ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
 THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
 SUCH DAMAGE.

 THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY WARRANTIES,
 INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
 PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
 CALIFORNIA HAS NO OBLIGATION TO PROVIDE MAINTENANCE, SUPPORT, UPDATES,
 ENHANCEMENTS, OR MODIFICATIONS.

                                        PT_COPYRIGHT_VERSION_2
                                        COPYRIGHTENDKEY

@ProposedRating Red (eal@eecs.berkeley.edu)
@AcceptedRating Red (johnr@eecs.berkeley.edu)
*/

package ptolemy.schematic;

import java.util.Enumeration;
import collections.HashedMap;

//////////////////////////////////////////////////////////////////////////
//// SchematicElement
/**

A SchematicElement is the superclass of classes that can
appear in a Ptolemy II schematic.  (SchematicEntity, SchematicPort,
SchematicRelation).  All of these have a name and a number of
SchematicParameters associated with them.

@author Steve Neuendorffer, John Reekie
@version $Id$
*/
public class SchematicElement extends XMLElement {

    /**
     * Create a SchematicElement object with the specified element type.
     * The object will have no attributes by default.
     *
     * @param type the element type of the SchematicElement
     */
    public SchematicElement(String type) {
        super(type);
        parameters = (HashedMap) new HashedMap();
        setName("");
    }

    /**
     * Create a SchematicElement object with the specified element type and
     * attributes
     *
     * @param type the element type of the new object
     * @param attributes a HashedMap from a String specifying the name of
     * an attribute to a String specifying the attribute's value.
     */
    public SchematicElement(String type, HashedMap attributes) {
        super(type, attributes);
        parameters = (HashedMap) new HashedMap();
        if(!hasAttribute("name")) setName("");
    }

    /**
     * Add a new parameter to this element. The name
     * of the parameter must be unique in this element.
     */
    public void addParameter (SchematicParameter parameter) {
        addChildElement(parameter);
        parameters.putAt(parameter.getName(), parameter);
    }

   /**
     * Test if there is a parameter with the given name in this
     * element.
     */
    public boolean containsParameter (String name) {
        return parameters.includesKey(name);
    }

   /** Return the name of this element.
     */
    public String getName() {
        return getAttribute("name");
    }

   /**
     * Return the schematic parameter with the given name.
     * Throw an exception if there is no parameter with the
     * given name in this element.
     */
    public SchematicParameter getParameter (String name) {
        return ((SchematicParameter) parameters.at(name));
    }

   /**
     * Return an enumeration over the (top-level) parameters in this
     * element.
     * @return an enumeration of SchematicParameters
     */
    public Enumeration parameters () {
        return parameters.elements();
    }

    /**
     * Remove the parameter with the given name from this element
     */
    public void removeParameter(String name) {
        SchematicParameter p= (SchematicParameter) parameters.at(name);
        removeChildElement(p);
        parameters.removeAt(name);
    }

    /**
     * Set the short name of this element
     */
    public void setName(String s) {
        setAttribute("name", s);
    }

    protected HashedMap parameters;

}

