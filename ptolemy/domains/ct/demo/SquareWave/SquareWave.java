/* The square wave response of a second order CT system.

 Copyright (c) 1998-2002 The Regents of the University of California.
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

@ProposedRating Red (liuj@eecs.berkeley.edu)
@AcceptedRating Red (cxh@eecs.berkeley.edu)
*/

package ptolemy.domains.ct.demo.SquareWave;

import ptolemy.kernel.util.*;
import ptolemy.actor.*;
import ptolemy.gui.Query;
import ptolemy.actor.gui.*;
import ptolemy.actor.lib.*;
import ptolemy.actor.lib.gui.TimedPlotter;
import ptolemy.actor.lib.gui.XYPlotter;
import ptolemy.data.*;
import ptolemy.data.expr.Parameter;
import ptolemy.data.type.BaseType;
import ptolemy.domains.ct.kernel.*;
import ptolemy.domains.ct.kernel.solver.*;
import ptolemy.domains.ct.lib.*;
import ptolemy.plot.Plot;


//////////////////////////////////////////////////////////////////////////
//// SquareWave
/**
The square wave response of any transfer function. This simple CT
system demonstrate the use of ODE solvers and the
ContinuousTransferFunction actor in the CT domain.  The solvers are
not allowed to change during the execution.  It is also useful for
correctness and performance testing.

@author  Jie Liu
@version $Id$
*/
public class SquareWave extends TypedCompositeActor {

    /** Construct the model
     */
    public SquareWave(Workspace workspace)
            throws IllegalActionException, NameDuplicationException {

        // Create the model.
        super(workspace);
        setName("LinearSystem");
        CTMultiSolverDirector dir= new CTMultiSolverDirector(this, "DIR");
        //dir.addDebugListener(new StreamListener());

        // Top level Parameters
        period = new Parameter(this, "period", new DoubleToken(2.0));

        numerator = new Parameter(this, "numerator");
        numerator.setExpression("{500.0}");
        denominator = new Parameter(this, "denominator");
        denominator.setExpression("{1.0, 10.0, 1000.0}");
 
        Clock sqwv = new Clock(this, "SQWV");
        sqwv.period.setExpression("period");
        sqwv.values.setExpression("{2.0, -2.0}");
        ContinuousTransferFunction tf = new ContinuousTransferFunction(
                this, "TransferFunction");
        tf.numerator.setExpression("numerator");
        tf.denominator.setExpression("denominator");
        TimedPlotter responsePlot = new TimedPlotter(this, "Plot");
        responsePlot.plot = new Plot();
        responsePlot.plot.setGrid(true);
        responsePlot.plot.setXRange(0.0, 6.0);
        responsePlot.plot.setYRange(-2.0, 2.0);
        responsePlot.plot.setSize(500, 350);
        responsePlot.plot.addLegend(0,"response");

        IORelation r1 = (IORelation)connect(sqwv.output, tf.input, "R1");
        connect(tf.output, responsePlot.input, "R2");
        responsePlot.input.link(r1);

        dir.startTime.setToken(new DoubleToken(0.0));

        dir.initStepSize.setToken(new DoubleToken(0.000001));

        dir.minStepSize.setToken(new DoubleToken(1e-6));

        dir.stopTime.setToken(new DoubleToken(6.0));
    }



    ///////////////////////////////////////////////////////////////////
    ////                         parameters                        ////

    public Parameter period;

    public Parameter numerator;

    public Parameter denominator;

}

