/*
 @Copyright (c) 2003-2006 The Regents of the University of California.
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
package ptolemy.caltrop.util;

import ptolemy.data.Token;
import ptolemy.kernel.util.IllegalActionException;
import caltrop.interpreter.Function;

///////////////////////////////////////////////////////////////////
//// PtCalFunction

/**
 This class is an adapter for {@link caltrop.interpreter.Function
 Function} objects that provides the Ptolemy II
 <tt>ptolemy.data.Function</tt> interface. It allows them to be
 seamlessly used with Ptolemy II-generated function objects.

 @author J&#246;rn W. Janneck
 @version $Id$
 @since Ptolemy II 4.0
 @Pt.ProposedRating Red (cxh)
 @Pt.AcceptedRating Red (cxh)
 @see caltrop.interpreter.Context
 @see caltrop.interpreter.Function
 */
public class PtCalFunction implements ptolemy.data.Function {
    /** Construct a PtCalFunction.
     *  @param function The Function.
     */
    public PtCalFunction(Function function) {
        _function = function;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Apply tokens arguments to the function.
     *  @param args Argument that are applied to the function.
     *  @return The token generated by applying the arguments to the function.
     *  @exception IllegalActionException If thrown by applying the function.
     */
    public Token apply(Token[] args) throws IllegalActionException {
        // FIXME: what does this do?  What are the types of the elements of
        // the list
        // TODO: should we allow non-token returns and tokenize them?
        return (Token) _function.apply(args);
    }

    /** Return the number of arguments.
     *  @return The number of arguments.
     */
    public int getNumberOfArguments() {
        return _function.arity();
    }

    /** Always return false, because the Function token is not congruent.
     *  @param function  The function (currently ignored).
     *  @return Always return false.
     */
    public boolean isCongruent(ptolemy.data.Function function) {
        return false;
    }

    /**
     * Return Function object wrapped by this object.
     *
     * @return The Function object.
     * @see caltrop.interpreter.Function
     */
    public Function getFunction() {
        return _function;
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////
    private Function _function;
}
