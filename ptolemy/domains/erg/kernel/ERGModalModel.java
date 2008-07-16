/* ERG modal models.

@Copyright (c) 2008 The Regents of the University of California.
All rights reserved.

Permission is hereby granted, without written agreement and without
license or royalty fees, to use, copy, modify, and distribute this
software and its documentation for any purpose, provided that the
above copyright notice and the following two paragraphs appear in all
copies of this software.

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

package ptolemy.domains.erg.kernel;

import ptolemy.domains.fsm.modal.ModalModel;
import ptolemy.kernel.ComponentEntity;
import ptolemy.kernel.CompositeEntity;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;

/**
 This is a typed composite actor to be an ERG modal model. An ERG modal model
 has an ERG (Event Relationship Graph) model inside. The ERG model has a number
 of events, and scheduling relations may exist between events, meaning that the
 processing of the starting event causes the ending event to occur after a
 certain amount of model time. A scheduling relation may also be guarded by a
 Boolean expression.
 <p>
 The controller of an ERG modal model is an ERG controller (instance of {@link
 ERGController}). The ERG controller contains an {@link ERGDirector}, which has
 an event queue to store scheduled events in time-stamp order. The ERG modal
 model itself also has an ERG director. The ERG director in the controller
 maintains its own event queue, and schedules itself to be fired by putting the
 controller in the event queue of the ERG modal model's director.
 <p>
 An event can have one or more refinements. A refinement can be an actor model,
 an FSM model, or an ERG model. If a refinement is an ERG model, then its
 controller reports its earliest event to the controller of this modal model,
 which fires it when the model time reaches the reported time in the future. If
 the ERG refinement itself contains an event that has a refinement in ERG, that
 controller reports to the controller one level above, which in turn reports to
 the modal model.

 @author Thomas Huining Feng
 @version $Id$
 @since Ptolemy II 7.1
 @Pt.ProposedRating Red (tfeng)
 @Pt.AcceptedRating Red (tfeng)
 */
public class ERGModalModel extends ModalModel {

	/** Construct an ERG modal model with a name and a container.
     *  The container argument must not be null, or a
     *  NullPointerException will be thrown.
     *  @param container The container.
     *  @param name The name of this actor.
     *  @exception IllegalActionException If the container is incompatible
     *   with this actor.
     *  @exception NameDuplicationException If the name coincides with
     *   an actor already in the container.
     */
    public ERGModalModel(CompositeEntity container, String name)
            throws IllegalActionException, NameDuplicationException {
        super(container, name);

        setClassName("ptolemy.domains.erg.kernel.ERGModalModel");

        directorClass.removeAllChoices();
        directorClass.setExpression("ptolemy.domains.erg.kernel.ERGDirector");

        ComponentEntity controller = getEntity("_Controller");
        if (controller != null) {
            controller.setContainer(null);
        }
        _controller = new ERGController(this, "_Controller");
    }
}
