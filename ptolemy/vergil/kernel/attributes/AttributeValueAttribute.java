/* An attribute that displays the value of an attribute of the container.

Copyright (c) 2003-2004 The Regents of the University of California.
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

package ptolemy.vergil.kernel.attributes;

import ptolemy.data.IntToken;
import ptolemy.data.expr.ModelScope;
import ptolemy.data.expr.Variable;
import ptolemy.data.expr.Parameter;
import ptolemy.data.type.BaseType;
import ptolemy.kernel.util.Attribute;
import ptolemy.kernel.util.Settable;
import ptolemy.kernel.util.StringAttribute;
import ptolemy.kernel.util.IllegalActionException;
import ptolemy.kernel.util.NameDuplicationException;
import ptolemy.kernel.util.NamedObj;
import ptolemy.kernel.util.ValueListener;

//////////////////////////////////////////////////////////////////////////
//// AttributeValueAttribute
/**
   This is a text attribute whose text string is derived from the
   value of a parameter. <p>

   @author Steve Neuendorffer
   @version $Id$
   @since Ptolemy II 4.0
   @Pt.ProposedRating Yellow (eal)
   @Pt.AcceptedRating Red (cxh)
*/
public class AttributeValueAttribute extends AbstractTextAttribute
    implements ValueListener, Settable {
    // NOTE: This attribute only implements settable as a workaround
    // to ensure that it gets notified of the start of execution.
    // Unfortunately, most of the code in the Variable class is
    // written to be specific to Variables, making it difficult for
    // this class to properly listen to a variable by name.

    /** Construct an attribute with the given name contained by the
     *  specified container. The container argument must not be null, or a
     *  NullPointerException will be thrown.  This attribute will use the
     *  workspace of the container for synchronization and version counts.
     *  If the name argument is null, then the name is set to the empty
     *  string. Increment the version of the workspace.
     *  @param container The container.
     *  @param name The name of this attribute.
     *  @exception IllegalActionException If the attribute is not of an
     *   acceptable class for the container, or if the name contains a period.
     *  @exception NameDuplicationException If the name coincides with
     *   an attribute already in the container.
     */
    public AttributeValueAttribute(NamedObj container, String name)
            throws IllegalActionException, NameDuplicationException {
        super(container, name);

        attributeName = new StringAttribute(this, "attributeName");
        displayWidth = new Parameter(this, "displayWidth");
        displayWidth.setExpression("6");
        displayWidth.setTypeEquals(BaseType.INT);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         parameters                        ////

    /** The name of the attribute of the container whose value to display. */
    public StringAttribute attributeName;

    /** The number of characters to display. This is an integer, with
     *  default value 6.
     */
    public Parameter displayWidth;

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** React to a changes in the attributes by changing the icon.
     *  @param attribute The attribute that changed.
     *  @exception IllegalActionException If the change is not acceptable
     *   to this container (should not be thrown).
     */
    public void attributeChanged(Attribute attribute)
            throws IllegalActionException {
        if (attribute == attributeName) {
            _setAttributeName(attributeName.getExpression());
        } else if(attribute == displayWidth) {
            _displayWidth =
                ((IntToken) displayWidth.getToken()).intValue();
            _icon.setText(_getText());
        } else {
            super.attributeChanged(attribute);
        }
    }

    /** React to a change in the value of the associated attribute.
     */
    public void valueChanged(Settable settable) {
        try {
            _setAttributeName(attributeName.getExpression());
        } catch(IllegalActionException ex) {
            // Ignore?  or reset attribute?
        }
    }

    /** Add a listener to be notified when the value of this settable
     *  object changes. An implementation of this method should ignore
     *  the call if the specified listener is already on the list of
     *  listeners.  In other words, it should not be possible for the
     *  same listener to be notified twice of a value update.
     *  @param listener The listener to add.
     */
    public void addValueListener(ValueListener listener) {
    }
    
    /** Return the default value of this attribute, if there is
     *  one, or null if there is none.
     *  @return The default value of this attribute, or null
     *   if there is none.
     */
    public String getDefaultExpression() {
        return "";
    }

    /** Get the value of the attribute that has been set by setExpression(),
     *  or null if there is none.
     *  @return The expression.
     */
    public String getExpression() {
        return "";
    }

    /** Get the visibility of this Settable, as set by setVisibility().
     *  If setVisibility() has not been called, then implementations of
     *  this interface should return some default, not null, indicating
     *  user-level visibility. The returned value is one of the static
     *  instances of the Visibility inner class.
     *  @return The visibility of this Settable.
     */
    public Settable.Visibility getVisibility() {
        return Settable.NONE;
    }

    /** Remove a listener from the list of listeners that are
     *  notified when the value of this variable changes.  If no such listener
     *  exists, do nothing.
     *  @param listener The listener to remove.
     */
    public void removeValueListener(ValueListener listener) {
    }
    
    /** Set the value of the attribute by giving some expression.
     *  In some implementations, the listeners and the container will
     *  be notified immediately.  However, some implementations may
     *  defer notification until validate() is called.
     *  @param expression The value of the attribute.
     *  @exception IllegalActionException If the expression is invalid.
     */
    public void setExpression(String expression) throws IllegalActionException {
    }

    /** Set the visibility of this Settable.  The argument should be one
     *  of the static public instances of the inner class Visibility.
     *  This is enforced by making it impossible to construct instances
     *  of this inner class outside this interface definition.
     *  If this method is not called, then implementations of
     *  this interface should return some default, not null.
     *  @param visibility The visibility of this Settable.
     */
    public void setVisibility(Settable.Visibility visibility) {
    }

    /** Check the validity of the expression set in setExpression().
     *  Implementations of this method should notify the container
     *  by calling attributeChanged(), unless the container has already
     *  been notified in setExpression().  They should also notify any
     *  registered value listeners if they have not already been notified.
     *  @exception IllegalActionException If the expression is not valid, or
     *   its value is not acceptable to the container or the listeners.
     */
    public void validate() throws IllegalActionException {
        _setAttributeName(attributeName.getExpression());
    }

    ///////////////////////////////////////////////////////////////////
    ////                        protected methods                  ////

    protected void _setAttributeName(String attributeName) 
            throws IllegalActionException {
        NamedObj container = (NamedObj)getContainer();
        if (container != null) {
            Attribute newAttribute = ModelScope.getScopedVariable(
                    null, container, attributeName);
            if(_attribute != newAttribute) {
                if(_attribute != null) {
                    _attribute.removeValueListener(this);
                }
                if(newAttribute instanceof Settable) {
                    _attribute = (Settable)newAttribute;
                    _attribute.addValueListener(this);
                } else {
                    _attribute = null;
                }
            }
        }
        _icon.setText(_getText());
    }
    
    /** Return the a new string that contains the expression of the
     *  referred to attribute.
     *  @return A new shape.
     */
    protected String _getText() {
        NamedObj container = (NamedObj)getContainer();
        try {
            if (container != null) {
                if (_attribute instanceof Variable) {
                    String value = 
                        ((Variable)_attribute).getToken().toString();
                    String truncated = value;
                    int width = _displayWidth;
                    if (value.length() > width) {
                        truncated = value.substring(0, width) + "...";
                    }
                    if (truncated.length() == 0) {
                        truncated = " ";
                    }
                    return truncated;
                } else if (_attribute instanceof Settable) {
                    String value = ((Settable)_attribute).getExpression();
                    String truncated = value;
                    int width = _displayWidth;
                    if (value.length() > width) {
                        truncated = value.substring(0, width) + "...";
                    }
                    if (truncated.length() == 0) {
                        truncated = " ";
                    }
                    return truncated;
                }
            }
        } catch (Exception ex) {
            return "???";
        }
        return "???";
    }

    ///////////////////////////////////////////////////////////////////
    ////                        protected members                  ////

    /** Most recent value of the rounding parameter. */
    protected int _displayWidth = 0;

    /** The associated attribute. */
    protected Settable _attribute = null;
}
