/* Base class for integrators in the CT domain.

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

@ProposedRating Yellow (liuj@eecs.berkeley.edu)
@AcceptedRating Yellow (johnr@eecs.berkeley.edu)
*/

package ptolemy.domains.ct.kernel;
import ptolemy.kernel.util.*;
import ptolemy.kernel.*;
import ptolemy.data.expr.*;
import ptolemy.data.*;
import ptolemy.data.type.BaseType;
import ptolemy.actor.*;
import ptolemy.actor.lib.TimedActor;
import java.util.LinkedList;
import java.util.Iterator;

//////////////////////////////////////////////////////////////////////////
//// CTBaseIntegrator
/**
Base class for integrators for continuous time simulation.
An integrator has one input port and one output port. Conceptually,
the input is the differential of the output. So an ordinary
differential equation dx/dt = f(x, t) can be represented by:
<P>
<pre>
<pre>               +---------------+
<pre>        dx/dt  |               |   x
<pre>    +--------->|   Integrator  |---------+----->
<pre>    |          |               |         |
<pre>    |          +---------------+         |
<pre>    |                                    |
<pre>    |             |---------|            |
<pre>    +-------------| f(x, t) |<-----------+
<pre>                  |---------|
</pre></pre></pre></pre></pre></pre></pre></pre></pre></pre>

<P>
An integrator
is a dynamic actor that emit a token (internal state) at the beginning
of the simulation. An integrator is an step size actor that can control
the accuracy of the ODE solution by adjusting step sizes. An integrator has
one memory, which is its state.
<P>
To help resolving the new state, a set of variables are used:<BR>
state: This is the new state at a time point, which has beed confirmed
by all the step size control actors.
tentative state: This is the resolved state which has not been confirmed.
It is a starting point for other actor to control the successfulness
of this integration step.
history: The previous states and their derivatives. They may be used 
by multistep methods.
<P>
For different ODE solving methods, the functionality
of an integrator may be different. This class provide a basic
implementation of the integrator, and some solver-dependent methods are
delegated to the ODE solvers.
<P>
An integrator has one parameter: <code>initialState</code>. At the
initialization stage of the simulation, the state of the integrator is
set to the initial state. Changing the initialState does not impact 
the simulation after the simulation starts. 
The default value of the parameter is 0.
<P>
An integrator can possibly have several auxiliary variables--
<code>_auxVariables</code>. The number of <code>_auxVariables</code> is get
from the ODE solver.
<P>

@author Jie Liu
@version $Id$
@see ODESolver
@see CTDirector
*/
public class CTBaseIntegrator extends TypedAtomicActor 
    implements TimedActor, CTStepSizeControlActor, 
               CTDynamicActor, CTStatefulActor {
    /** Construct an integrator in the default workspace with an
     *  empty string name.
     *  An integrator has one single input port and one single
     *  output port.
     *  The object is added to the workspace directory.
     *  Increment the version number of the workspace.
     *
     *  @exception NameDuplicationException Not thrown in this base class.
     *  @exception IllegalActionException Not thrown in this base class.
     *
    public CTBaseIntegrator()
            throws NameDuplicationException, IllegalActionException {
	super();
        input = new TypedIOPort(this, "input");
        input.setInput(true);
        input.setOutput(false);
        input.setMultiport(false);
        input.setTypeEquals(BaseType.DOUBLE);
        output = new TypedIOPort(this, "output");
        output.setOutput(true);
        output.setTypeEquals(BaseType.DOUBLE);
        _initState = 0.0;
        initialState = new Parameter(this, "initialState",
                new DoubleToken(_initState));
    }

    /** Construct an integrator in the specified workspace with an empty
     *  string as the name. You can then change the name with setName().
     *  A integrator has one single input port and one single
     *  output port.
     *  If the workspace argument is null, then use the default workspace.
     *  The object is added to the workspace directory.
     *  Increment the version number of the workspace.
     *
     *  @param workspace The workspace that will list the entity.
     *  @exception NameDuplicationException Not thrown in this base class.
     *  @exception IllegalActionException Not thrown in this base class.
     *
    public CTBaseIntegrator(Workspace workspace)
            throws NameDuplicationException, IllegalActionException {
        super(workspace);
        input = new TypedIOPort(this, "input");
        input.setInput(true);
        input.setOutput(false);
        input.setMultiport(false);
        input.setTypeEquals(BaseType.DOUBLE);
        output = new TypedIOPort(this, "output");
        output.setOutput(true);
        output.setTypeEquals(BaseType.DOUBLE);
        initialState = new Parameter(this, "initialState",
                new DoubleToken(_initState));
    }
    */
    /** Construct an integrator, with a name, a input port, a output port
     *  and a TypedCompositeActor as the container.
     *  A integrator has one single input port and one single
     *  output port.
     *
     * @param container The TypedCompositeActor that contains this integrator.
     * @param name The name
     * @return The CTBaseIntegrator
     * @exception NameDuplicationException If the name is used by another
     *            actor.
     * @exception IllegalActionException If the entity cannot be contained
     *   by the proposed container. FIXME: never happens?
     */
    public CTBaseIntegrator(TypedCompositeActor container, String name)
            throws NameDuplicationException, IllegalActionException {
        super(container, name);
        input = new TypedIOPort(this, "input");
        input.setInput(true);
        input.setOutput(false);
        input.setMultiport(false);
        input.setTypeEquals(BaseType.DOUBLE);
        output = new TypedIOPort(this, "output");
        output.setOutput(true);
        output.setTypeEquals(BaseType.DOUBLE);
        initialState = new Parameter(this, "initialState",
                new DoubleToken(_initState));
        _history = new History(this);
    }

    ///////////////////////////////////////////////////////////////////
    ////                     ports and parameters                  ////

    /** Input port. Finals means they are not changeable once created.
     */
    public final TypedIOPort input;

    /** Input port. Finals means they are not changeable once created.
     */
    public final TypedIOPort output;

    /** Initial State
     */
    public Parameter initialState;


    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Clean the history information.
     */
    public void clearHistory() {
        _history.clear();
    }

    /** This method delegates to the integratorFire() of the 
     *  current ODE solver. The existance of director and ODE solver
     *  is not checked, since they are checked in the preinitalize()
     *  method.
     *
     *  @exception IllegalActionException If thrown by integratorFire()
     *       of the solver.
     */
    public void fire() throws  IllegalActionException {
        CTDirector dir = (CTDirector)getDirector();
        ODESolver solver = (ODESolver)dir.getCurrentODESolver();
        if(_debugging) _debug(getName() + "using solver:", solver.getName());
        solver.integratorFire(this);
    }

    /** Return the auxVariables in a double array. This method get the
     *  auxVariables stored in the integrator. auxVariables are created
     *  in the prefire() method. Return null if the
     *  auxVariables have never been created.
     *
     *  @return The auxVariables in a double array.
     */
    public double[] getAuxVariables() {
        return _auxVariables;
    }

    /** Return the derivative of the latest updated state.
     *  @return The derivative of the most recent resolved state.
     */
    public final double getDerivative() {
        return _derivative;
    }

    /** Return the history information of the last index-th step.
     *  The index start from 0, and getHistory(0) gives the 
     *  state and derivative of time t(n-1), assuming the current
     *  time is t(n). If the index is less than 0 or grater than
     *  the history capacity, an runtime IndexOutOfBoundsException
     *  will be thrown.
     *  The returned history array has length 2, where the first 
     *  element is the history state, and the second element is 
     *  the corresponding derivative.
     *  
     *  @param index The index
     *  @return The history array at that index point.
     */
    public double[] getHistory(int index) { 
        return _history.getEntry(index);
    }

    /** Return the history capacity.
     *  @return The maximum capacity of the history list.
     */
    public final int getHistoryCapacity() {
        return _history.getCapacity();
    }
    
    /** Return the initial state.
     *
     *  @return the initial state.
     */
    public final double getInitialState() {
        return _initState;
    }

    /** Return the state of the integrator. The returned state is the 
     *  latest confirmed state. This is the same as getHistory(0)[0].
     *
     *  @return A double number as the state of the integrator.
     */
    public final double getState() {
        return _state;
    }

    /** Return the tentative state.
     *
     *  @return the tentative state.
     */
    public double getTentativeState() {
        return _tentativeState;
    }

    /** Return the number of valid history entries. This number is
     *  always less than or equal to the history capacity.
     *  @return The number of valid history entries.
     */
    public final int getValidHistoryCount() {
        return _history.getValidEntryCount();
    }

    /** Initialize the integrator. Check for director and ODE 
     *  solver.
     *  Update initial state parameter. Set the initial state to
     *  the tentative state and the state. Set tentative 
     *  derivative to 0.0.
     *
     *  @exception IllegalActionException If there's no director or
     *       the director has no ODE solver, or thrown by the
     *       integratorInitialize() of the solver.
     */
    public void initialize() throws IllegalActionException {
        CTDirector dir = (CTDirector)getDirector();
        if(dir == null) {
            throw new IllegalActionException( this,
                    " no director available");
        }
        ODESolver solver = (ODESolver)dir.getCurrentODESolver();
        if(solver == null) {
            throw new IllegalActionException( this,
                    " no ODE solver available");
        }
        super.initialize();
        _initState = ((DoubleToken)initialState.getToken()).doubleValue();
        _tentativeState = _initState;
        _tentativeDerivative = 0.0;
        _state = _tentativeState;
        _derivative = _tentativeDerivative;
        if(_debugging) _debug(getName(), " init, token = " + _initState);
        _history.clear();
    }

    /** Emit the tentative output, which is the tentative state of the
     *  integrator.
     *  @exception IllegalActionException If the data transfer is not
     *  completed.
     */
    public void emitTentativeOutputs() throws IllegalActionException {
        output.send(0, new DoubleToken(_tentativeState));
    }

    /** Return true if the last integration step is successful.
     *  This method delegates to the integratorIsSuccessful() method of
     *  the current ODE solver.
     *  @return True if the last integration step is successful.
     */
    public boolean isThisStepSuccessful() {
        ODESolver solver = ((CTDirector)getDirector()).getCurrentODESolver();
        _successful = solver.integratorIsSuccessful(this);
        return _successful;
    }

    /** Updates the states and push the current state and its derivative
     *  into history.
     *  @return True always.
     *  @exception Never thrown
     */
    public boolean postfire() throws IllegalActionException {
        _state = _tentativeState;
        _derivative = _tentativeDerivative;
        if(_debugging) _debug(getName(), " state: " + _state +
                              " derivative: " + _derivative);
        _pushHistory(_tentativeState, _tentativeDerivative);
        return true;
    }

    /** Return the predicted next step size. This method delegates to
     *  the integratorPredictedStepSize() method of the current ODESolver.
     *  @return The predicteded next step size.
     */
    public double predictedStepSize() {
        ODESolver solver = ((CTDirector)getDirector()).getCurrentODESolver();
        return solver.integratorPredictedStepSize(this);
    }

    /** Setup the actor to operate with the current ODE solver.
     *  This method checks
     *  if there enough auxVariables in the integrator given the
     *  current ODE solver. If not, create more auxVariables.
     *  The existance of director and ODE solver is not checked,
     *  since they are checked in preinitialize()
     *  @return True always.
     *  @exception IllegalActionException If there's no director or
     *       the director has no ODE solver.
     */
    public boolean prefire() throws IllegalActionException {
        CTDirector dir = (CTDirector)getDirector();
        ODESolver solver = (ODESolver)dir.getCurrentODESolver();
        int n = solver.getIntegratorAuxVariableCount();
        if((_auxVariables == null) || (_auxVariables.length < n)) {
            _auxVariables = new double[n];
        }
        if(getHistoryCapacity() != solver.getHistoryCapacityRequirement()) {
            setHistoryCapacity(solver.getHistoryCapacityRequirement());
        }
        if(getValidHistoryCount()>=2) {
            _history.rebalance(dir.getCurrentStepSize());
        }
        return true;
    }

    /** Return the prediction of the refined next step size. 
     *  If this integrator considers the last step to be successful,
     *  then return the current step size, else return half the
     *  current step size.
     *  @return The predicteded next step size.
     */
    public double refinedStepSize() {
        double curstep = ((CTDirector)getDirector()).getCurrentStepSize();
        if(_successful) {
            return curstep;
        }else {
            return (double)0.5*curstep;
        }
    }

    /** Go to the marked state. This method may be used
     *  for rollback the simulation from a previous time point.
     */
    public void goToMarkedState() {
        _setState(_storedState);
        setTentativeState(_storedState);
    }

    /** Mark the current state. This remembered state can be
     *  retrieved by the goToMarkedState() method. The marked state
     *  may be used for back up simulation from past.
     */
    public void markState() {
        _storedState = getState();
    }

    /** Set the value of an auxVariable.  If the index is out of
     *  the bound of the auxVariables
     *  array, an IllegalActionException is thrown to indicate a error
     *  in the integration method.
     *
     *  @param index The index in the auxVariables array.
     *  @param value The value to be set.
     *  @exception IllegalActionException If the index is out of the range
     *       of the temporary states array.
     */
    public void setAuxVariables(int index, double value)
            throws IllegalActionException {
        try {
            _auxVariables[index] = value;
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new IllegalActionException(this,
                    "index out of the range of auxVariables.");
        }
    }

    /** Set history capacity. This will typically be set by the ODE solvers
     *  that uses the history. If the argument is less than 0,
     *  the capacity is set to 0.
     *  @param cap The capacity.
     */
    public final void setHistoryCapacity(int cap) {
        _history.setCapacity(cap);
    }

    /** Set the tentative state. Tentative state is the state that
     *  the ODE solver think to be the new state for the integrator.
     *  It may not
     *  be the final state due to the event detection.
     *  @param value The value to be set.
     */
    public final void setTentativeState(double value) {
        _tentativeState = value;
    }

    /** Set the tentative derivative dx/dt. Tentative derivative
     *  is the derivative of the state that
     *  the ODE solver think to be at the fixed point. This may not
     *  be the final derivative due to the event detection.
     *  @param value The value to be set.
     */
    public final void setTentativeDerivative(double value) {
        _tentativeDerivative = value;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         protected methods                 ////
    /** Push the state and derivative into the history storage.
     *  If the history capacity is full, then the oldest history
     *  will be lost. The contents of the history storage can be
     *  retrieved by getHistory() method. 
     */
    protected void _pushHistory(double state, double derivative) {
        _history.pushEntry(state, derivative);
    }      

    /** Set the state of the integrator.
     *
     *  @param value The value to be set to the state.
     */
    protected final void _setState(double value) {
        _state = value;
    }

    /** Rebalance the history. This method is here for the test purpose.
     *  @param stepsize The new step size.
     */
    protected void _rebalanceHistory(double stepsize) {
        _history.rebalance(stepsize);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    // Parameter initial state.

    private double _initState;

    private boolean _successful = false;
    // Temporary states array.
    private double[] _auxVariables;
    // State.
    private double _state;
    // Derivative.
    private double _derivative;
    // tentative state;
    private double _tentativeState;
    // Tentative derivative;
    private double _tentativeDerivative;

    // The state stored, may be used for back up simulation
    private double _storedState;

    // The history states and its derivative.
    // This variable is needed by Linear Multistep (LMS) methods,
    // like Trapezoidal rule and backward defferential formula.
    private History _history;
   
    ///////////////////////////////////////////////////////////////////
    ////                           Inner Class                     ////
    
    /** The history information, state and derivatives, at equidistance
     *  past time points.
     */
    public class History {
        
        /** Construt the history.
         */
        public History (CTBaseIntegrator container) {
            _container = container;
            _entries = new LinkedList();
            _capacity = 0;
            _stepsize = 0.0;
        }

        ///////////////////////////////////////////////////////////////
        ////                         public methods                ////

        /** Remove all history informaion.
         */
        public void clear() {
            _entries.clear();
        }

        /** Return the maximum capacity.
         *  @return The capacity.
         */
        public int getCapacity() {
            return _capacity;
        }
        
        /** Return the number of valid entries.
         *  @return The number of valid history entries.
         */
        public int getValidEntryCount() {
            return _entries.size();
        }

        public double[] getEntry(int index) {
            return ((DoubleDouble)_entries.get(index)).toArray();
        }

        /** Push the new state-derivative pair into the history.
         *  If after the push, the number of entries exceed the 
         *  capacity, then the olded entry will be lost.
         *  @param state The state.
         *  @param derivative THe derivative of the state.
         */
        public void pushEntry(double state, double derivative) {
            if (_capacity >0) {
                DoubleDouble entry = new DoubleDouble(state, derivative);    
                if (_entries.size() >= _capacity) {
                    // the history list has achieved its capacity, 
                    // so remove the oldest entry.
                    _entries.removeLast();
                }
                _entries.addFirst(entry);
                _stepsize = 
                   ((CTDirector)_container.getDirector()).getCurrentStepSize();
            }
        }

        /**
         *  Rebalances the history information 
         *  with repect to the current step size, such that the information
         *  in the history list are equaly distanced, and the the 
         *  distance is the current step size. 
         *  If the current step size is less than the history step size
         *  used in the history list, then a 4-th order Hermite 
         *  interpolation is used for every two consecutive points.
         *  If the current step size is larger than the history step size,
         *  then a linear extrapolation is used.
         *  @param currentStepSize The current step size.
         */
        public void rebalance(double currentStepSize) {
            double timeresolution = 
                ((CTDirector) _container.getDirector()).getTimeResolution();
            if(Math.abs(currentStepSize - _stepsize)>timeresolution) {
                double[][] history = toArray();
                int size = _entries.size();
                for (int i = 0; i < size-1; i++) {
                    _entries.removeLast();
                }
                double ratio = currentStepSize/_stepsize; 
                for (int i = 1; i < size; i++) {
                    double[] newentry;
                    int bin = (int)Math.floor(i*ratio);
                    if (bin < size) {
                        // interpolation as much as possible.
                        double rem = i*ratio - (double)bin;
                        newentry = _Hermite(history[bin+1], history[bin], 1-rem);
                    } else {
                        // extrapolation
                        newentry = _extrapolation(history[size-2], 
                                history[size-1], i*ratio-size+1);
                    }
                    _entries.addLast(new DoubleDouble
                            (newentry[0], newentry[1]));
                } 
                _stepsize = currentStepSize;
            }
        }

        /** Set the history capacity. The enties exceed the capacity
         *  will be lost. If the argument is less than 0, it is set 
         *  to 0.
         *  @param capacity The new capacity.
         */
        public void setCapacity(int capacity) {
            _capacity = (capacity>0) ? capacity : 0;
            while (_entries.size() > capacity) {
                _entries.removeLast();
            }
        }

        /** Return the history information in an array format. The 
         *  entries are ordered in their backward chronological order.
         */
        public double[][] toArray() {
            double[][] array = new double[_entries.size()][2];
            Iterator objs = _entries.iterator();
            int i =0;
            while(objs.hasNext()) {
                DoubleDouble entry = (DoubleDouble) objs.next();
                array[i++] = entry.toArray();
            }  
            return array;
        }
        
        ///////////////////////////////////////////////////////////////
        ////                        private variables               ////
        
        // Hermite interpolation.
        // @param p1 Point1, state and derivative.
        // @param p2 Point2, state and derivative.
        // @param s The interpolation point.
        private double[] _Hermite(double[] p1, double[] p2, double s) {
            double s3 = s*s*s;
            double s2 = s*s;
            double h1 = 2*s3 - 3*s2 + 1;
            double h2 = -2*s3 + 3*s2;
            double h3 = s3- 2*s2 + s;
            double h4 = s3 - s2;
            double g1 = 6*s2 - 6*s;
            double g2 = -6*s2 + 6*s;
            double g3 = 3*s2 -4*s +1;
            double g4 = 3*s2 -2*s;
            double[] result = new double[2];
            result[0] = h1*p1[0] + h2*p2[0] + h3*p1[1] + h4*p2[1];
            result[1] = g1*p1[0] + g2*p2[0] + g3*p1[1] + g4*p2[1];
            return result;
        }

        // Linear extrapolation.
        // @param p1 Point1, state and derivative.
        // @param p2 Point2, state and derivative.
        // @param s The extrapolation ration.
        private double[] _extrapolation(double[] p1, double[] p2, double s) {
            
            double[] result = new double[2];
            result[0] = p2[0] - (p1[0] - p2[0])*s;
            result[1] = p2[1] - (p1[1] - p2[1])*s;
            return result;
        }

        ///////////////////////////////////////////////////////////////
        ////                        private variables               ////
        
        // The container.
        CTBaseIntegrator _container;
        
        // The linked list storing the entries
        LinkedList _entries;
        
        // The capacity.
        int _capacity;

        // The step size that the entries are based on.
        double _stepsize;
    }
        

    ///////////////////////////////////////////////////////////////////
    ////                           Inner Class                     ////
    
    /** A data structure for storing two double numbers
     */
    public class DoubleDouble {
        
        /** construct the Double pair.
         */
        public DoubleDouble(double first, double second) {
            _data[0] = first;
            _data[1] = second;
        }

        ///////////////////////////////////////////////////////////////
        ////                         public methods                ////
        
        /** Return the data as a double array.
         */
        public double[] toArray() {
            return _data;
        }

        ///////////////////////////////////////////////////////////////
        ////                        private variables               ////
        
        // The data as a form of a double array of two elements
        private double[] _data = new double[2];
    }
}
