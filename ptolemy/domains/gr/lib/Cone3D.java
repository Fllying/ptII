/* A GR Shape consisting of a cone.

Copyright (c) 1998-2004 The Regents of the University of California.
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

*/
package ptolemy.domains.gr.lib;

import javax.media.j3d.Node;

import ptolemy.data.DoubleToken;
import ptolemy.data.IntToken;
import ptolemy.data.expr.Parameter;
import ptolemy.data.type.BaseType;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;

import com.sun.j3d.utils.geometry.Cone;

//////////////////////////////////////////////////////////////////////////
////Cone3D

/** This actor contains the geometry and appearance specifications for a
    cone.  The output port is used to connect this actor to the Java3D scene
    graph. This actor will only have meaning in the GR domain.

    @author C. Fong, Edward A. Lee
    @version $Id$
    @since Ptolemy II 1.0
    @Pt.ProposedRating Red (chf)
    @Pt.AcceptedRating Red (chf)
*/
public class Cone3D extends GRShadedShape {

    /** Construct an actor with the given container and name.
     *  @param container The container.
     *  @param name The name of this actor.
     *  @exception IllegalActionException If the actor cannot be contained
     *   by the proposed container.
     *  @exception NameDuplicationException If the container already has an
     *   actor with this name.
     */
    public Cone3D(CompositeEntity container, String name)
            throws IllegalActionException, NameDuplicationException {

        super(container, name);
        
        radius = new Parameter(this, "radius");
        radius.setExpression("0.5");
        radius.setTypeEquals(BaseType.DOUBLE);
        
        height = new Parameter(this, "height");
        height.setExpression("0.7");
        height.setTypeEquals(BaseType.DOUBLE);
        
        divisions = new Parameter(this, "divisions");
        divisions.setExpression("roundToInt(radius * 60)");
        divisions.setTypeEquals(BaseType.INT);
        
        height.moveToFirst();
        radius.moveToFirst();
    }

    ///////////////////////////////////////////////////////////////////
    ////                     ports and parameters                  ////

    /** The number of divisions in the circle forming the base of the
     *  cone. This is an integer with
     *  default value "roundToInt(radius * 60)". This parameter
     *  determines the resolution of the cone, which is approximated
     *  as a surface composed of triangular facets. Increasing this
     *  value makes the surface smoother, but also increases the cost
     *  of rendering.
     */
    public Parameter divisions;

    /** The height of the cone. This is a double that defaults to 0.5.
     */
    public Parameter height;

    /** The radius of the base of the cone. This is a double that
     *  defaults to 0.7.
     */
    public Parameter radius;

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Return the encapsulated Java3D node of this 3D actor. The encapsulated
     *  node for this actor is a cone.
     *  @return the Java3D cone.
     */
    public Node _getNodeObject() {
        return (Node) _containedNode;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////

    /** Create the shape and appearance of the encapsulated cone
     *  @exception IllegalActionException If the value of some parameters can't
     *   be obtained
     */
    protected void _createModel() throws IllegalActionException {
        super._createModel();
        // NOTE: The "1" here is the number of "y divisions".
        // I can't see any reason why this would need to be anything
        // other than 1, so it's not a parameter.
        int divisionsValue = ((IntToken)divisions.getToken()).intValue();
        _containedNode = new Cone((float)_getRadius(), (float) _getHeight(),
                Cone.GENERATE_NORMALS, divisionsValue, 1, _appearance);
    }

    /**  Return the value of the height parameter
     *  @return the height of the cone
     *  @exception IllegalActionException If the value of some parameters can't
     *   be obtained
     */
    private double _getHeight() throws IllegalActionException  {
        return ((DoubleToken) height.getToken()).doubleValue();
    }

    /**  Return the value of the radius parameter
     *  @return the radius of the base of the cone
     *  @exception IllegalActionException If the value of some parameters can't
     *   be obtained
     */
    private double _getRadius() throws IllegalActionException {
        return ((DoubleToken) radius.getToken()).doubleValue();
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    private Cone _containedNode;
}
