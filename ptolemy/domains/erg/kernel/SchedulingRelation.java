/* Scheduling relations between ERG events.

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

import ptolemy.data.ArrayToken;
import ptolemy.data.BooleanToken;
import ptolemy.data.ScalarToken;
import ptolemy.data.Token;
import ptolemy.data.expr.ASTPtArrayConstructNode;
import ptolemy.data.expr.ASTPtRootNode;
import ptolemy.data.expr.Parameter;
import ptolemy.data.expr.ParseTreeEvaluator;
import ptolemy.data.expr.ParserScope;
import ptolemy.data.expr.PtParser;
import ptolemy.data.type.BaseType;
import ptolemy.domains.fsm.kernel.Transition;
import ptolemy.kernel.util.Attribute;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;
import ptolemy.kernel.util.Settable;
import ptolemy.kernel.util.StringAttribute;
import ptolemy.kernel.util.Workspace;

/**
 A scheduling relation is an edge from one ERG event to another. If it is not a
 cancelling edge, then processing the event at the starting point of the edge
 causes the one at the end point to be scheduled after a certain amount of
 model-time delay, if the guard of the scheduling relation is satisfied. If it
 is a cancelling edge, then processing the first event causes the second one to
 be cancelled if it is already scheduled in the containing ERG controller's
 event queue.
 <p>
 A scheduling relation may carry argument values to be supplied to the event to
 be scheduled, who has parameters defined on it. The number and types of the
 evaluated arguments must match those of the parameters declared by the event.
 <p>
 If the guard of a scheduling relation is omitted, it is defaulted to true,
 which means the scheduling relation is unconditional.

 @author Thomas Huining Feng
 @version $Id$
 @since Ptolemy II 6.1
 @Pt.ProposedRating Red (tfeng)
 @Pt.AcceptedRating Red (tfeng)
 */
public class SchedulingRelation extends Transition {

    /** Construct a scheduling relation with the given name contained by the
     *  specified entity. The container argument must not be null, or a
     *  NullPointerException will be thrown. This transition will use the
     *  workspace of the container for synchronization and version counts.
     *  If the name argument is null, then the name is set to the empty string.
     *
     *  @param container The container.
     *  @param name The name of the scheduling relation.
     *  @exception IllegalActionException If the container is incompatible
     *   with this scheduling relation.
     *  @exception NameDuplicationException If the name coincides with
     *   any relation already in the container.
     */
    public SchedulingRelation(ERGController container, String name)
            throws IllegalActionException, NameDuplicationException {
        super(container, name);

        guardExpression.setDisplayName("condition");
        outputActions.setVisibility(Settable.NONE);
        setActions.setVisibility(Settable.NONE);
        reset.setVisibility(Settable.NONE);
        preemptive.setVisibility(Settable.NONE);
        defaultTransition.setVisibility(Settable.NONE);
        nondeterministic.setVisibility(Settable.NONE);
        refinementName.setVisibility(Settable.NONE);

        delay = new StringAttribute(this, "delay");
        delay.setDisplayName("delay (\u03B4)");
        delay.setExpression("0.0");

        arguments = new StringAttribute(this, "arguments");
        arguments.setExpression("{}");

        canceling = new Parameter(this, "canceling");
        canceling.setTypeEquals(BaseType.BOOLEAN);
        canceling.setExpression("false");
    }

    /** React to a change in an attribute. If the changed attribute is
     *  the <i>arguments</i> parameter, evaluate the arguments. If the changed
     *  attribute is <i>delay</i>, evaluate the delay. Then, check whether the
     *  combination of this scheduling relation's parameters is reasonable. If
     *  the scheduling relation is cancelling, then its <i>arguments</i>
     *  parameter must specify an empty list ("()"), and its delay must be
     *  evaluated to 0.0.
     *
     *  @param attribute The attribute that changed.
     *  @exception IllegalActionException If thrown by the superclass
     *   attributeChanged() method, or the changed attribute is the
     *   <i>arguments</i> parameter or the <i>delay</i> parameter and is given
     *   an expression that does not evaluate to a boolean value, or this
     *   scheduling relation is set to cancelling but the values of
     *   <i>arguments</i> and <i>delay</i> are not acceptable.
     */
    public void attributeChanged(Attribute attribute)
    throws IllegalActionException {
        if (attribute == arguments) {
            _parseArguments();
        } else if (attribute == delay) {
            _parseDelay();
        }

        if (canceling != null && delay != null && isCanceling()
                && getContainer() != null) {
            if (!_isZeroDelay()) {
                throw new IllegalActionException("For a canceling edge, the "
                        + "delay must be const 0.0.");
            } else {
                if (_argumentsTreeVersion != _workspace.getVersion()) {
                    _parseArguments();
                }
                if (_argumentsTree.jjtGetNumChildren() > 0) {
                    throw new IllegalActionException("For a canceling edge, "
                            + "the argument list must be empty.");
                }
            }
        }
    }

    /** Clone the scheduling relation into the specified workspace. This calls
     *  the base class and then sets the attribute public members to refer to
     *  the attributes of the new scheduling relation.
     *
     *  @param workspace The workspace for the new scheduling relation.
     *  @return A new scheduling relation.
     *  @exception CloneNotSupportedException If a derived class contains
     *   an attribute that cannot be cloned.
     */
    public Object clone(Workspace workspace) throws CloneNotSupportedException {
        SchedulingRelation relation =
            (SchedulingRelation) super.clone(workspace);
        relation._argumentsTree = null;
        relation._argumentsTreeVersion = -1;
        relation._delayTree = null;
        relation._delayTreeVersion = -1;
        relation._parseTreeEvaluator = new ParseTreeEvaluator();
        relation._parser = new PtParser();
        return relation;
    }

    /** Evaluate the parse tree of the arguments and return an ArrayToken that
     *  contains the values of those arguments in the given parser scope.
     *
     *  @param scope The parser scope in which the arguments are evaluated.
     *  @return The ArrayToken containing the values of those arguments.
     *  @exception IllegalActionException If the evaluation is unsuccessful.
     */
    public ArrayToken getArguments(ParserScope scope)
    throws IllegalActionException {
        if (_argumentsTreeVersion != _workspace.getVersion()) {
            _parseArguments();
        }
        Token token = _parseTreeEvaluator.evaluateParseTree(_argumentsTree,
                scope);
        if (token == null || !(token instanceof ArrayToken)) {
            throw new IllegalActionException(this, "Unable to evaluate " +
                    "arguments \"" + arguments.getExpression() + "\".");
        }
        return (ArrayToken) token;
    }

    /** Evaluate the delay parameter in the given parse scope and return its
     *  value.
     *
     *  @param scope The parser scope in which the delay is evaluated.
     *  @return The value of the model-time delay.
     *  @exception IllegalActionException If the evaluation is unsuccessful.
     */
    public double getDelay(ParserScope scope) throws IllegalActionException {
        if (_delayTreeVersion != _workspace.getVersion()) {
            _parseDelay();
        }
        Token token = _parseTreeEvaluator.evaluateParseTree(_delayTree, scope);
        if (token == null || !(token instanceof ScalarToken)) {
            throw new IllegalActionException(this, "Unable to evaluate delay" +
                    "expression \"" + delay.getExpression() + "\".");
        }
        double result = ((ScalarToken) token).doubleValue();
        return result;
    }

    /** Override the superclass to return the guard expression of this
     *  scheduling relation. If the guard is an empty string, the return is
     *  "true"; otherwise, the result of the overridden method in the superclass
     *  is returned.
     *
     *  @return The non-empty guard expression.
     */
    public String getGuardExpression() {
        String guard = super.getGuardExpression();
        if (guard.trim().equals("")) {
            guard = "true";
        }
        return guard;
    }

    /** Return a string describing this scheduling relation. The string has up
     *  to three lines. The first line is the guard expression, preceded
     *  by "guard: ". The second line is the delay expression, preceded by
     *  "\u03B4: " (unicode for the delta character). The third line is the
     *  list of arguments, preceded by "arguments: ". If any of these
     *  is missing, then the corresponding line is omitted.
     *
     *  @return A string describing this transition.
     */
    public String getLabel() {
        StringBuffer buffer = new StringBuffer(super.getLabel());

        String delayExpression = delay.getExpression();
        if ((delayExpression != null) && !_isZeroDelay()) {
            if (buffer.length() > 0) {
                buffer.append("\n");
            }
            buffer.append("\u03B4: "); // Unicode for \delta
            buffer.append(delayExpression);
        }

        String argumentsExpression = arguments.getExpression();
        if (argumentsExpression != null) {
            String trimmedArguments = argumentsExpression.trim();
            boolean emptyArguments = trimmedArguments.startsWith("{") &&
                    trimmedArguments.endsWith("}") &&
                    trimmedArguments.substring(1, trimmedArguments.length() - 1)
                            .trim().equals("");
            if (!emptyArguments) {
                if (buffer.length() > 0) {
                    buffer.append("\n");
                }
                buffer.append("arguments: ");
                buffer.append(argumentsExpression);
            }
        }

        return buffer.toString();
    }

    /** Return whether this scheduling relation is cancelling.
     *
     *  @return True if this scheduling relation is cancelling.
     */
    public boolean isCanceling() {
        try {
            return ((BooleanToken) canceling.getToken()).booleanValue();
        } catch (IllegalActionException e) {
            return false; // Assume it is not canceling edge by default.
        }
    }

    /** Evaluate the guard in the given parser scope, and return whether this
     *  scheduling relation is enabled (with its guard evaluated to true).
     *
     *  @param scope The parser scope in which the guard is to be evaluated.
     *  @return True if the transition is enabled and some event is detected.
     *  @exception IllegalActionException If thrown when evaluating the guard.
     */
    public boolean isEnabled(ParserScope scope) throws IllegalActionException {
        String guard = getGuardExpression();
        if (guard.trim().equals("")) {
            return true;
        } else {
            return super.isEnabled(scope);
        }
    }

    /** The attribute for arguments. Its value must be evaluated to an
        ArrayToken, though this evaluation is performed only when this
        scheduling relation is to be considered by the starting event but not
        when this attribute is set by the designer. */
    public StringAttribute arguments;

    /** A Boolean-valued parameter that defines whether this scheduling relation
        is cancelling. */
    public Parameter canceling;

    /** The attribute for the model-time delay. Its value must be evaluated to a
        ScalarToken, though this evaluation is performed only when this
        scheduling relation is to be considered by the starting event but not
        when this attribute is set by the designer. */
    public StringAttribute delay;

    /** Return whether the delay is statically equal to 0.0.
     *
     *  @return True if the delay is statically equal to 0.0.
     */
    private boolean _isZeroDelay() {
        try {
            double d =
                getDelay(((ERGController) getContainer()).getPortScope());
            if (d == 0.0) {
                return true;
            }
        } catch (IllegalActionException e) {
        }
        return false;
    }

    /** Parse the arguments.
     *
     *  @exception IllegalActionException If thrown when when parsing the
     *   arguments.
     */
    private void _parseArguments() throws IllegalActionException {
        try {
            _argumentsTree = (ASTPtArrayConstructNode)
                    _parser.generateParseTree(arguments.getExpression());
            _argumentsTreeVersion = _workspace.getVersion();
        } catch (ClassCastException e) {
            throw new IllegalActionException(this, "The arguments for a " +
                    "scheduling edge must be in an array in the form of " +
                    "{...}.");
        }
    }

    /** Parse the delay.
     *
     *  @exception IllegalActionException If thrown when when parsing the delay.
     */
    private void _parseDelay() throws IllegalActionException {
        _delayTree = _parser.generateParseTree(delay.getExpression());
        _delayTreeVersion = _workspace.getVersion();
    }

    /** The parse tree of arguments. */
    private ASTPtArrayConstructNode _argumentsTree;

    /** Version of _argumentsTree. */
    private long _argumentsTreeVersion = -1;

    /** The parse tree of delay. */
    private ASTPtRootNode _delayTree;

    /** Version of _delayTree. */
    private long _delayTreeVersion = -1;

    /** The evaluated to evaluate all parse trees. */
    private ParseTreeEvaluator _parseTreeEvaluator = new ParseTreeEvaluator();

    /** The parser to generate all parse trees. */
    private PtParser _parser = new PtParser();
}
