/* An adapter class for ptolemy.actor.lib.Recorder.

 Copyright (c) 2008-2009 The Regents of the University of California.
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
package ptolemy.data.properties.lattice.logicalOrBackward.actor.lib;

import java.util.List;

import ptolemy.data.IntToken;
import ptolemy.data.properties.lattice.PropertyConstraintSolver;
import ptolemy.data.properties.lattice.logicalOrBackward.Lattice;
import ptolemy.kernel.util.IllegalActionException;

///////////////////////////////////////////////////////////////////
//// Recorder

/**
 An adapter class for ptolemy.actor.lib.Recorder.

 @author Thomas Mandl, Man-Kit Leung
 @version $Id$
 @since Ptolemy II 7.1
 @Pt.ProposedRating Red (mankit)
 @Pt.AcceptedRating Red (mankit)
*/
public class Recorder extends Sink {
    /**
     * Construct the Sink property constraint adapter associated
     * with the given component and solver. The constructed adapter
     * implicitly uses the default constraints set by the solver.
     * @param solver The given solver.
     * @param actor The given Recorder actor
     * @exception IllegalActionException If the adapter cannot be
     * initialized in the superclass.
     */
    public Recorder(PropertyConstraintSolver solver,
            ptolemy.actor.lib.Recorder actor) throws IllegalActionException {
        super(solver, actor, false);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /**
     * Return the constraints of this component. The constraints are a list of
     * inequalities.
     * @return The constraints of this component.
     * @exception IllegalActionException If thrown while manipulating the lattice
     * or getting the solver.
     */
    public List<Inequality> constraintList() throws IllegalActionException {
        ptolemy.actor.lib.Recorder actor = (ptolemy.actor.lib.Recorder) getComponent();

        Lattice lattice = (Lattice) getSolver().getLattice();

        if (((IntToken) actor.capacity.getToken()).intValue() > 0) {
            setEquals(actor.input, lattice.TRUE);
        } else {
            setEquals(actor.input, lattice.FALSE);
        }

        return super.constraintList();
    }
}
