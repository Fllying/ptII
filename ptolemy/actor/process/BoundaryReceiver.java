/* An interface used by receivers that receive data across
composite actor boundaries. 

 Copyright (c) 1997-2000 The Regents of the University of California.
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

@ProposedRating Green (mudit@eecs.berkeley.edu)
@AcceptedRating Yellow

*/

package ptolemy.actor.process;

import ptolemy.data.*;


//////////////////////////////////////////////////////////////////////////
//// BoundaryReceiver
/**
A BoundaryReceiver is an interface used by receivers that receive 
data across composite actor boundaries. 

@author John S. Davis II
@version $Id$

*/
public interface BoundaryReceiver extends ProcessReceiver {

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** 
     */
    public Token get(Branch controllingBranch) throws TerminateBranchException; 

    /** 
     */
    public boolean isConsumerReceiver(); 

    /** 
     */
    public boolean isProducerReceiver(); 

    /** 
     */
    public void put(Token token, Branch controllingBranch) throws TerminateBranchException; 

}
