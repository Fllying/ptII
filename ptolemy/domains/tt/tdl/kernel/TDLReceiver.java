package ptolemy.domains.tt.tdl.kernel;

import ptolemy.actor.AbstractReceiver;
import ptolemy.actor.IOPort;
import ptolemy.actor.NoRoomException;
import ptolemy.actor.NoTokenException;
import ptolemy.actor.StateReceiver;
import ptolemy.data.Token;
import ptolemy.kernel.util.IllegalActionException;

/**
 * A TDL receiver stores a token until it gets a new token. A call to hasToken of a
 * TDL receiver will always succeed.
 * The TDL receiver is based on the Giotto receiver.
 * @author Patricia Derler
 *
 */
public class TDLReceiver extends AbstractReceiver implements StateReceiver {

    /** Construct an empty TDLReceiver with no container.
     */
    public TDLReceiver() {
        super();
    }

    /** Construct an empty TDLReceiver with the specified container.
     *  @param container The container.
     *  @exception IllegalActionException If the container does
     *   not accept this receiver.
     */
    public TDLReceiver(IOPort container) throws IllegalActionException {
        super(container);
    }

    ///////////////////////////////////////////////////////////////////
    ////                         public methods                    ////

    /** Clear this receiver of any contained tokens.
     *  FIXME.
     *  Should rename and replace all the reset() with clear().
     */
    public void clear() {
        reset();
    }

    /** Get the contained and available token, i.e., get the last
     *  token that has been put into the receiver before the last
     *  update.
     *  @return A token.
     *  @exception NoTokenException If no token is available.
     */
    public Token get() throws NoTokenException {
        if (_token == null) {
            throw new NoTokenException(getContainer(),
                    "Attempt to get data from an empty receiver.");
        }

        return _token;
    }

    /** Return true, since writing to this receiver is always allowed.
     *  @return True.
     */
    public boolean hasRoom() {
        return true;
    }

    /** Return true, since writing to this receiver is always allowed.
     *  @param numberOfTokens The size of tokens to be written to the receiver.
     *  @return True.
     */
    public final boolean hasRoom(int numberOfTokens) {
        return true;
    }

    /** Return true if there is a token available. A token is
     *  available whenever put() has been called at least once followed
     *  by a call to the update() method.
     *  @return True if there is a token available.
     */
    public boolean hasToken() {
        return (_token != null);
    }

    /** Return true if the receiver has at least one token available.
     *  Any number of calls to get() is allowed and will return the same
     *  available token.
     *  @param numberOfTokens The number of tokens available in this receiver.
     *  @return True if there is a token available.
     */
    public final boolean hasToken(int numberOfTokens) {
        return (_token != null);
    }

    /** Put a token into this receiver. Any token which has been put
     *  into the receiver before without calling update will be lost.
     *  The token becomes available to the get() method only after
     *  update() is called.
     *  <p>
     *  Note that putting a null into this receiver will leave the
     *  receiver empty after update. The receiver does not check
     *  against this but expects that IOPort will always put
     *  non-null tokens into receivers.
     *  @param token The token to be put into this receiver.
     *  @exception NoRoomException Not thrown in this base class.
     */
    public void put(Token token) throws NoRoomException {
        if (token == null)
                return;
        IOPort port = this.getContainer();
        _nextToken = token;
    }

    /** Get the contained and available token, i.e., get the last
     *  token that has been put into the receiver before the last
     *  update and reset the _token only.
     *  @return A token.
     *  @exception NoTokenException If no token is available.
     */
    public Token remove() throws NoTokenException {
        if (_token == null) {
            throw new NoTokenException(getContainer(),
                    "Attempt to get data from an empty receiver.");
        }

        Token buffer = _token;
        _token = null;
        return buffer;
    }

    /** Reset the receiver by removing all tokens from the receiver.
     */
    public void reset() {
        _token = null;
        _nextToken = null;
    }

    /** Update the receiver by making the last token that has been
     *  passed to put() available to get().
     */
    public void update() {
        if (_nextToken == null) // TODO PD added two lines but not sure if right behaviour
            return;
        IOPort port = getContainer();
        _token = _nextToken;
    }
    
    public void init(Token token) {
        _token = token;        
    }

    // TODO remove
    public Token getTok() {
        return _token;
    }

    public void copyTokensTo(TDLReceiver newReceiver) {
        newReceiver._nextToken = this._nextToken;
        newReceiver._token = this._token;
        
    }

    ///////////////////////////////////////////////////////////////////
    ////                         private variables                 ////
    // The next token.
    private Token _nextToken = null;

    // The token available for reading.
    private Token _token = null;


    
}
