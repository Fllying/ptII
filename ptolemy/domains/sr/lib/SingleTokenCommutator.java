/* A commutator  that processes a single token per iteration.

 Copyright (c) 1997-2004 The Regents of the University of California.
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

@ProposedRating Yellow (cxh@eecs.berkeley.edu)
@AcceptedRating Yellow (cxh@eecs.berkeley.edu)
*/

package ptolemy.domains.sr.lib;
import ptolemy.actor.lib.SequenceActor;
import ptolemy.actor.lib.Transformer;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;

//////////////////////////////////////////////////////////////////////////
//// SingleTokenCommutator
/**
The SingleTokenCommutator has a multiport input port and an output
port.  The types of the ports are undeclared and will be resolved by
the type resolution mechanism, with the constraint that the output
type must be greater than or equal to the input type. On each call to
the fire method, the actor reads one token from the current input,
and writes one token to an output channel.  If there is no token on
the input, then it will not produce a token on the output.  In the
next iteration of this actor, it will read the next channel.

@author Paul Whitaker, Mudit Goel, Edward A. Lee, Christopher Hylands, Jim Armstrong
@version $Id$
@since Ptolemy II 2.0
*/
public class  SingleTokenCommutator   extends Transformer
    implements SequenceActor {

    /** Construct an actor in the specified container with the specified
     *  name. Create ports and make the input port a multiport. Create
     *  the actor parameters.
     *
     *  @param container The container.
     *  @param name This is the name of this distributor within the container.
     *  @exception NameDuplicationException If an actor
     *  with an identical name already exists in the container.
     *  @exception IllegalActionException If the actor cannot be contained
     *  by the proposed container.
     */
    public SingleTokenCommutator(CompositeEntity container, String name)
            throws NameDuplicationException, IllegalActionException {
        super(container, name);
        input.setMultiport(true);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Read at most one token from the current input  channel  and write that
     *  token  the  output channel.  If there is no token on the current input
     *  channel, do nothing.
     *  @exception IllegalActionException If there is no director.
     */
    public void fire() throws IllegalActionException {
        if (input.hasToken(_currentInputPosition)) {
            output.send(0, input.get(_currentInputPosition));
        }
    }

    /** Begin execution by setting the current output channel to zero.
     *  @exception IllegalActionException If there is no director.
     */
    public void initialize() throws IllegalActionException {
        super.initialize();
        _currentInputPosition = 0;
    }

    /** Update the output position to equal that determined by the most
     *  recent invocation of the fire() method.  The output position is
     *  the channel number of the output port to which the next input
     *  will be sent.
     *  @exception IllegalActionException If there is no director.
     */
    public boolean postfire() throws IllegalActionException {
        _currentInputPosition++;
        if (_currentInputPosition >= input.getWidth()) {
            _currentInputPosition = 0;
        }

        return super.postfire();
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////

    // The channel number for the next output.
    private int _currentInputPosition;
}
