/* A CT director that utilizes multiple ODE solvers.

 Copyright (c) 1998-2000 The Regents of the University of California.
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
@ProposedRating Green (liuj@eecs.berkeley.edu)
@AcceptedRating Yellow (johnr@eecs.berkeley.edu)

*/

package ptolemy.domains.ct.kernel;

import ptolemy.kernel.util.Workspace;
import ptolemy.kernel.util.Attribute;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;
import ptolemy.kernel.util.InvalidStateException;
import ptolemy.kernel.util.InternalErrorException;
import ptolemy.kernel.util.NamedObj;
import ptolemy.kernel.util.Nameable;
import ptolemy.kernel.CompositeEntity;
import ptolemy.actor.Actor;
import ptolemy.actor.CompositeActor;
import ptolemy.data.expr.Parameter;
import ptolemy.data.type.BaseType;
import ptolemy.data.StringToken;
import ptolemy.domains.ct.kernel.util.TotallyOrderedSet;

import java.util.Iterator;


//////////////////////////////////////////////////////////////////////////
//// CTMultiSolverDirector
/**
A CTDirector that uses multiple ODE solvers. The reason for switching
solvers is that when abrupt changes in signals or actor functionalities
occur (also called breakpoints), the state of the system
have to be recalculated.
At these points, a special ODE solver, called the "breakpointODESolver"
is used. The simulation is executed as if the breakpoint is a new 
starting point. Typically, breakpointODESolvers do not advance time. 
<P>
This director handles both predictable breakpoints, which are breakpoints
that are registered in the breakpoint table, and unpredictable breakpoints,
which are breakpoints that are not known before hand.
<P>
This director can only be a top-level director. For a CT domain inside
an opaque composite actor, use CTMixedSignalDirector (if the outer
domain is discrete) or CTEmbeddedDirector (if the outer domain is
a CT domain or a HS domain.)
<P>
This director has two more parameters than the CTDirector base
class.<BR>
<UL>
<LI><I>ODESolver</I>: This is the name of the normal ODE solver
used in nonbreakpoint iterations. 
<LI><I>breakpointODESolver</I>: This is the name of the ODE solver that
is used in the iterations just after the breakpoint. The breakpoint
ODE solvers should not require history information (this property
is called self-start). The default is
"ptolemy.domains.ct.kernel.solver.DerivativeResolver"
If there are Dirac impulses in the system, the
"ptolemy.domains.ct.kernel.solver.ImpulseBESolver" may give
a better result.
<LI>All other parameters are maintained by the CTDirector base class. And the
two solvers share them.

@author  Jie Liu
@version $Id$
@see ptolemy.domains.ct.kernel.CTDirector
*/
public class CTMultiSolverDirector extends CTDirector {
    /** Construct a director in the default workspace with an empty string
     *  as its name. The director is added to the list of objects in
     *  the workspace. Increment the version number of the workspace.
     *  All the parameters take their default values.
     */
    public CTMultiSolverDirector() {
        super();
    }

    /** Construct a director in the  workspace with an empty name.
     *  The director is added to the list of objects in the workspace.
     *  Increment the version number of the workspace.
     *  All the parameters take their default values.
     *  @param workspace The workspace of this object.
     */
    public CTMultiSolverDirector(Workspace workspace) {
        super(workspace);
    }

    /** Construct a director in the given container with the given name.
     *  The container argument must not be null, or a
     *  NullPointerException will be thrown.
     *  If the name argument is null, then the name is set to the
     *  empty string. Increment the version number of the workspace.
     *  All the parameters take their default values.
     *  @param workspace Object for synchronization and version tracking.
     *  @param name Name of this director.
     *  @exception IllegalActionException If the director is not compatible
     *   with the specified container.  May be thrown in derived classes.
     *  @exception NameDuplicationException If the container is not a
     *   CompositeActor and the name collides with an entity in the container.
     */
    public CTMultiSolverDirector(CompositeEntity container, String name)
            throws IllegalActionException, NameDuplicationException {
        super(container, name);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         parameters                        ////

    /** The name of the normal ODE solver
     *  used in nonbreakpoint iterations. The default is a String
     *  "ptolemy.domains.ct.kernel.solver.ExplicitRK23Solver"
     */
    public Parameter ODESolver;

    /** The name of the ODE solver that
     *  is used in the iterations just after the breakpoint. The default is
     *  "ptolemy.domains.ct.kernel.solver.DerivativeResolver"
     */
    public Parameter breakpointODESolver;

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** React to a change in an attribute. If the changed attribute matches
     *  a parameter of the director, then the corresponding private copy of the
     *  parameter value will be updated.
     *  @param param The changed parameter.
     *  @exception IllegalActionException If the new solver that is specified
     *   is not valid.
     */
    public void attributeChanged(Attribute attribute)
            throws IllegalActionException {
        if(attribute == ODESolver) {
            if(_debugging) _debug(getFullName() + " updating  ODE solver...");
            _solverClassName =
                ((StringToken)ODESolver.getToken()).stringValue();
            _defaultSolver = _instantiateODESolver(_solverClassName);
            _setCurrentODESolver(_defaultSolver);
        } else if (attribute == breakpointODESolver) {
            if(_debugging) _debug(getName() +" updating breakpoint solver...");
            _breakpointSolverClassName =
                ((StringToken)breakpointODESolver.getToken()).stringValue();
            _breakpointSolver =
                _instantiateODESolver(_breakpointSolverClassName);
        } else {
            super.attributeChanged(attribute);
        }
    }

    /** Return false always, since this director cannot be an inside director.
     *  @return false.
     */
    public boolean canBeInsideDirector() {
        return false;
    }

    /** Return true since this director can be a top-level director.
     *  @return true.
     */
    public boolean canBeTopLevelDirector() {
        return true;
    }

    /** Clone the director into the specified workspace. This calls the
     *  base class and then copies the parameter of this director.  The new
     *  actor will have the same parameter values as the old.
     *  Note that ODE solvers are stateless, so we only clone the class 
     *  name of the solvers.
     *  @param workspace The workspace for the new object.
     *  @return A new director.
     *  @exception CloneNotSupportedException If one of the attributes
     *   cannot be cloned.
     */
    public Object clone(Workspace workspace)
            throws CloneNotSupportedException {
        CTMultiSolverDirector newobj = 
            (CTMultiSolverDirector)(super.clone(workspace));
        newobj.ODESolver = 
            (Parameter)newobj.getAttribute("ODESolver");
        newobj.breakpointODESolver =
            (Parameter)newobj.getAttribute("breakpointODESolver");
        return newobj;
    }

    /** Fire the system for one iteration. One iteration is defined as
     *  simulating the system at one time point, which includes
     *  processing discrete events, resolving states,
     *  and producing outputs.
     *  <P>
     *  An iteration begins with processing events, which includes
     *  that all waveform generators consuming current input events
     *  and all event generators producing current output events.
     *  Then the new values of the state variables are resolved.
     *  If the state is resolved successfully, the outputs are produced.
     *  <P>
     *  The step size of one iteration is determined by the suggested
     *  next step size and the breakpoints. If the first breakpoint in
     *  the breakpoint table is in the middle of the "intended" step.
     *  Then the current step size is reduced to the <code>
     *  breakpoint - current time </code>.
     *  The result of such a step is the left limit of the states
     *  at the breakpoint.
     *  <P>
     *  The new state is resolved by the resolveStates() method of the
     *  current ODE solver. After that, the step size control actors
     *  in the dynamic actor schedule and the state transition schedule
     *  are checked for the accuracy
     *  of the this step. If any one of the step size control
     *  actors do not think it is accurate, then the integration step
     *  will be restarted with a refined step size, which is the minimum
     *  of the refinedStepSize() from all step size control actors in
     *  the dynamic actor schedule and the state transition schedule.
     *  If all the actors in the dynamic actor and the state transition
     *  schedules think the current step is accurate, then the actors
     *  in the output path will be fired according to the output
     *  schedule. Then the step size control actors in the output
     *  path will be checked for accuracy. The above procedure is
     *  followed again to refine the step size and restart the iteration.
     *  The iteration is complete when all actors agree that the step
     *  is accurate.
     *  <P>
     *  All the actors are prefired before an iteration begins. If
     *  any one of them returns false, then the iteration is
     *  cancelled, and the function returns.
     *
     *  @exception IllegalActionException If thrown by the ODE solver.
     */
    public void fire() throws IllegalActionException {
        // event phase;
        _eventPhaseExecution();
        // continuous phase;
        _setIterationBeginTime(getCurrentTime());
        setCurrentStepSize(getSuggestedNextStepSize());
        _processBreakpoints();
        if(_debugging) _debug("execute the system from "+
                getCurrentTime() + " step size" + getCurrentStepSize()
                + " using solver " + getCurrentODESolver().getName());
        _fireOneIteration();
    }

    /** Return the ODE solver.
     *  @return The default ODE solver
     */
    public ODESolver getODESolver() {
        return _defaultSolver;
    }

    /** Return the breakpoint ODE solver.
     *  @return The breakpoint ODE solver.
     */
    public ODESolver getBreakpointSolver() {
        return _breakpointSolver;
    }

    /** Initialization after type resolution.
     *  It sets the step size and the suggested next step size
     *  to the initial step size. The ODE solver  and the
     *  breakpoint ODE solver are instantiated.
     *  Set the current time to be the start time of the simulation.
     *  Both the current time and the stop time are registered
     *  as a breakpoint.
     *  It invokes the initialize() method for all the Actors in the
     *  container.
     *
     *  @exception IllegalActionException If the instantiation of the solvers
     *  does not succeed or one of the directed actors throws it.
     */
    public void initialize() throws IllegalActionException {
        if(_debugging) _debug(getFullName(), " initializing:");
        // synchronized on time and initialize all actors
        if(_debugging) _debug(getFullName(), " initialize directed actors: ");
        super.initialize();
        // set step sizes
        setCurrentStepSize(getInitialStepSize());
        if(_debugging) _debug(getFullName(), " set current step size to "
                + getCurrentStepSize());
        setSuggestedNextStepSize(getInitialStepSize());
        if(_debugging) {
            _debug(getFullName(), " set suggested next step size to "
                    + getSuggestedNextStepSize());
        }
        if(_debugging)
            _debug(getFullName(), " set the current time as a break point: " +
                    getCurrentTime());
        fireAt(null, getCurrentTime());
        if(_debugging)
            _debug(getFullName(), " set the stop time as a break point: " +
                    getStopTime());
        fireAt(null, getStopTime());

        if(_debugging) _debug(getFullName() + " End of Initialization.");
    }

    /** Return false if the stop time is reached.
     *  Check if the current time is
     *  the stop time. If so, return false ( for stop further simulation).
     *  Otherwise, returns true.
     *  @return false If the simulation is finished.
     *  @exception IllegalActionException If thrown by registering
     *  breakpoint
     */
    public boolean postfire() throws IllegalActionException {
        if(Math.abs(getCurrentTime() - getStopTime()) < getTimeResolution()) {
            return false;
        }
        if(getStopTime() < getCurrentTime()) {
            throw new InvalidStateException(this,
                    " stop time is less than the current time.");
        }
        if(_debugging) _debug(getFullName() + " postfire returns true at: " +
                getCurrentTime());
        return true;
    }

   /**  Return true always, indicating that the system is always ready
     *  for one iteration. Increase the count of integration step for 
     *  statistics. Note that the actors are not prefired in this method.
     *
     *  @return True Always.
     *  @exception IllegalActionException Not thrown in this base class.
     */
    public boolean prefire() throws IllegalActionException {
        if(_debugging) _debug(this.getFullName(), "prefire.");
        if(STAT) {
            NSTEP++;
        }
        return true;
    }

    /** After preforming super.preinitialize(),
     *  instantiate all the solvers.
     *  @exception IllegalActionException If thrown by one
     *  of the actors.
     */
    public void preinitialize() throws IllegalActionException {
        super.preinitialize();
        // Instantiate ODE solvers
        if(_debugging) _debug(getFullName(), " instantiating the ODE solver ",
                _solverClassName);
        _defaultSolver = _instantiateODESolver(_solverClassName);
        _setCurrentODESolver(_defaultSolver);

        if(_debugging) _debug(getFullName(), "instantiating the " +
                " breakpoint solver ", _breakpointSolverClassName);
        _breakpointSolver =
            _instantiateODESolver(_breakpointSolverClassName);
    }

    ////////////////////////////////////////////////////////////////////////
    ////                         protected methods                      ////

    /** Process discrete events in the system. All the event generators
     *  will produce current events, and event interpreters will
     *  consume current events.
     *  @exception IllegalActionException If one of the event generators
     *     or waveform generators throws it.
     */
    protected void _eventPhaseExecution() throws IllegalActionException {
        CTScheduler scheduler = (CTScheduler)getScheduler();
        Iterator eventGenerators = scheduler.eventGeneratorList().iterator();
        while(eventGenerators.hasNext()) {
            CTEventGenerator generator = 
                (CTEventGenerator) eventGenerators.next();
            generator.emitCurrentEvents();
        }
        // FIXME: Fire all discrete actors here.
        Iterator waveGenerators = scheduler.waveformGeneratorList().iterator();
        while(waveGenerators.hasNext()) {
            CTWaveformGenerator generator = 
                (CTWaveformGenerator) waveGenerators.next();
            generator.consumeCurrentEvents();
        }
    }

    /** Fire one iteration. Return immediately if any actor returns false
     *  in their prefire() method. The time is advanced by the
     *  step size used.
     *  @exception IllegalActionException If one the actors throws it
     *    in its execution methods.
     */
    protected void _fireOneIteration() throws IllegalActionException {
        if(_debugging) _debug(getFullName(),
                "Fire one iteration from " + getCurrentTime(),
                "Using step size" + getCurrentStepSize());
        ODESolver solver = getCurrentODESolver();
        if(_debugging) _debug( "Using ODE solver", solver.getName());
        while (true) {
            while (_prefireSystem()) {
                if (solver.resolveStates()) {
                    if(_debugging) _debug("state resolved.");
                    // Check if this step is acceptable
                    if (!_isStateAccurate()) {
                        if(_debugging) _debug(getName(), "state not accurate.");
                        setCurrentTime(getIterationBeginTime());
                        setCurrentStepSize(_refinedStepWRTState());
                        if(_debugging) _debug("execute the system from "+
                                getCurrentTime() +" step size" +
                                getCurrentStepSize());
                        if(STAT) {
                            NFAIL++;
                        }
                    } else {
                        break;
                    }
                } else { 
                    // Resolve state failed, e.g. in implicit methods.
                    if(getCurrentStepSize() < 0.5*getMinStepSize()) {
                        throw new IllegalActionException(this,
                                "Cannot resolve new states even using "+
                                "the minimum step size, at time "+
                                getCurrentTime());
                    }
                    setCurrentTime(getIterationBeginTime());
                    setCurrentStepSize(0.5*getCurrentStepSize());
                }
            }
            produceOutput();
            if (!_isOutputAccurate()) {
                if(_debugging) _debug(getName(), "output not accurate.");
                setCurrentTime(getIterationBeginTime());
                setCurrentStepSize(_refinedStepWRTOutput());
                if(STAT) {
                    NFAIL++;
                }
            }else {
                break;
            }
        }
        setSuggestedNextStepSize(_predictNextStepSize());
        updateStates(); // call postfire on all actors
    }

    /** Initialize parameters to their default values.
     */
    protected void _initParameters() {
        super._initParameters();
        try {
            _solverClassName =
                "ptolemy.domains.ct.kernel.solver.ExplicitRK23Solver";
            ODESolver = new Parameter(
                    this, "ODESolver", new StringToken(_solverClassName));
            ODESolver.setTypeEquals(BaseType.STRING);
            _breakpointSolverClassName =
                "ptolemy.domains.ct.kernel.solver.DerivativeResolver";
            breakpointODESolver = new Parameter(this, "breakpointODESolver",
                    new StringToken(_breakpointSolverClassName));
            breakpointODESolver.setTypeEquals(BaseType.STRING);
        } catch (IllegalActionException e) {
            //Should never happens. The parameters are always compatible.
            throw new InternalErrorException("Parameter creation error.");
        } catch (NameDuplicationException ex) {
            throw new InvalidStateException(this,
                    "Parameter name duplication.");
        }
    }

    /** Return true if all step size control actors in the output
     *  schedule agree that the current step is accurate.
     *  @exception IllegalActionException If the scheduler throws it.
     */
    protected boolean _isOutputAccurate() throws IllegalActionException {
        boolean accurate = true;
        CTScheduler scheduler = (CTScheduler)getScheduler();
        Iterator actors = scheduler.outputSSCActorList().iterator();
        while (actors.hasNext()) {
            CTStepSizeControlActor actor =
                (CTStepSizeControlActor) actors.next();
            boolean thisAccurate = actor.isThisStepAccurate();
            //if(_debugging) _debug("Checking Output Step Size Control Actor: "
            //        + ((NamedObj)actor).getName() + " " + thisAccurate);
            accurate = accurate && thisAccurate;
        }
        if(_debugging) 
            _debug(getFullName() + " output accurate is " + accurate); 
        return accurate;
    }

    /** Return true if all step size control actors in the dynamic actor
     *  schedule and the state transition schedule agree that
     *  the current step is accurate.
     *  @exception IllegalActionException If the scheduler throws it.
     */
    protected boolean _isStateAccurate() throws IllegalActionException {
        boolean accurate = true;
        CTScheduler scheduler = (CTScheduler)getScheduler();
        Iterator actors = scheduler.stateTransitionSSCActorList().iterator();
        while (actors.hasNext()) {
            CTStepSizeControlActor actor =
                (CTStepSizeControlActor) actors.next();
            boolean thisAccurate = actor.isThisStepAccurate();
            //if(_debugging) _debug("Checking State Step Size Control Actor: " +
            //        ((NamedObj)actor).getName() + " " + thisAccurate);
            accurate = accurate && thisAccurate;
            
        }
        if(_debugging) 
            _debug(getFullName() + " state accurate is " + accurate); 
        return accurate;
    }

    /** Return true if the prefire() methods of all the actors in the system
     *  return true.
     *  @return The logical AND of the prefire() of all actors.
     */
    protected boolean _prefireSystem() throws IllegalActionException {
        boolean ready = true;
        CompositeActor container = (CompositeActor) getContainer();
        Iterator actors = container.deepEntityList().iterator();
        while(actors.hasNext()) {
            Actor actor = (Actor) actors.next();
            ready = ready && actor.prefire();
            if(_debugging) _debug("Prefire "+((Nameable)actor).getName() +
                    " returns" + ready);
        }
        return ready;
    }

    /** Clear obsolete breakpoints, switch to breakpointODESolver if this
     *  is the first fire after a breakpoint, and adjust step sizes
     *  accordingly.
     *  @exception IllegalActionException If the breakpoint solver is
     *     illegal.
     */
    protected void _processBreakpoints() throws IllegalActionException  {
        double point;
        TotallyOrderedSet breakPoints = getBreakPoints();
        Double now = new Double(getCurrentTime());
        _setBreakpointIteration(false);
        // Choose ODE solver
        _setCurrentODESolver(getODESolver());
        // If now is a break point, remove the break point from table;
        if(breakPoints != null && !breakPoints.isEmpty()) {
            breakPoints.removeAllLessThan(now);
            if(breakPoints.contains(now)) {
                // now is a break point.
                breakPoints.removeFirst();
                _setBreakpointIteration(true);
                _setCurrentODESolver(_breakpointSolver);
                // The step size in the breakpoint iteration is controlled
                // by the breakpoint ODE solver.
                if(_debugging) _debug(getFullName(),
                        "in BREAKPOINT iteration.");
            }else {
                // Adjust step size so that the first breakpoint is 
                // not in the middle of this step.
                point = ((Double)breakPoints.first()).doubleValue();
                double iterEndTime = getCurrentTime() + getCurrentStepSize();
                if (iterEndTime > point) {
                    setCurrentStepSize(point-getCurrentTime());
                }
            }
        }
    }

    /** Predict the next step size. This method should be called if the
     *  current integration step is accurate to estimate the step size
     *  for the next step. The predicted step size
     *  is the minimum of all predictions from step size control actors,
     *  and it never exceeds 10 times this step size.
     *  @exception IllegalActionException If the scheduler throws it.
     */
    protected double _predictNextStepSize() throws IllegalActionException {
        if(!isBreakpointIteration()) {
            double predictedStep = 10.0*getCurrentStepSize();
            CTScheduler scheduler = (CTScheduler)getScheduler();
            Iterator actors = 
                scheduler.stateTransitionSSCActorList().iterator();
            while (actors.hasNext()) {
                CTStepSizeControlActor actor =
                    (CTStepSizeControlActor) actors.next();
                predictedStep = Math.min(predictedStep, 
                        actor.predictedStepSize());
            }
            actors = scheduler.outputSSCActorList().iterator();
            while (actors.hasNext()) {
                CTStepSizeControlActor actor =
                    (CTStepSizeControlActor) actors.next();
                predictedStep = Math.min(predictedStep, 
                        actor.predictedStepSize());
            }
            return predictedStep;
        } else {
            // The first iteration after a breakpoint iteration.
            // Use the initial step size.
            return getInitialStepSize();
        }
    }

    /** Return the refined step size with respect to resolving the
     *  new state.
     *  It asks all the step size control actors in the state transition
     *  and dynamic actor schedule for the refined step size, and takes the
     *  minimum of them. This method does not check whether the 
     *  refined step size is less than the minimum step size.
     *  @return The refined step size.
     *  @exception IllegalActionException If the scheduler throws it.
     */
    protected double _refinedStepWRTState() throws IllegalActionException {
        if(_debugging) _debug(getFullName() + "refine step wrt state.");
        double refinedStep = getCurrentStepSize();
        CTScheduler scheduler = (CTScheduler)getScheduler();
        Iterator actors = scheduler.stateTransitionSSCActorList().iterator();
        while (actors.hasNext()) {
            CTStepSizeControlActor actor =
                (CTStepSizeControlActor)actors.next();
            double size = actor.refinedStepSize();
            if(_debugging) _debug(((Nameable)actor).getName(),
                    "refines step size to "
                    + size);
            refinedStep = Math.min(refinedStep, size);
        }
        return refinedStep;
    }

    /** Return the refined the step size with respect to the outputs.
     *  It asks all the step size control actors in the state transition
     *  and dynamic schedule for the refined step size, and take the
     *  minimum of them.
     *  @return The refined step size.
     *  @exception IllegalActionException If the scheduler throws it.
     */
    protected double _refinedStepWRTOutput() throws IllegalActionException {
        double refinedStep = getCurrentStepSize();
        CTScheduler scheduler = (CTScheduler)getScheduler();
        Iterator actors = scheduler.outputSSCActorList().iterator();
        while (actors.hasNext()) {
            CTStepSizeControlActor actor =
                (CTStepSizeControlActor)actors.next();
            refinedStep = Math.min(refinedStep, actor.refinedStepSize());
        }
        return refinedStep;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    // The classname of the ODE solver.
    private String _solverClassName;

    // The default solver.
    private ODESolver _defaultSolver = null;

    // The classname of the default ODE solver
    private String _breakpointSolverClassName;

    // The default solver.
    private ODESolver _breakpointSolver = null;
}
