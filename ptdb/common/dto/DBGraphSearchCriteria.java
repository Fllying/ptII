/*
 * 
 */
package ptdb.common.dto;

import java.util.ArrayList;

import ptolemy.kernel.ComponentEntity;
import ptolemy.kernel.Port;
import ptolemy.kernel.Relation;

///////////////////////////////////////////////////////////////
//// DBGraphSearchCriteria

/**
 * <p>The DTO (data transfer object) that all the search criteria input by the 
 * user, for the graph searching through the XML database.<br>
 * 
 * It is constructed by the GUI layer class to pass the search criteria for 
 * graph DB search.</p>
 * 
 * @author Alek Wang
 * @version $Id$
 * @since Ptolemy II 8.1
 * @Pt.ProposedRating red (wenjiaow)
 * @Pt.AcceptedRating red (wenjiaow)
 *
 */
public class DBGraphSearchCriteria {

    //////////////////////////////////////////////////////////////////////
    ////                public methods                                  //////

    /**
     * Get the component entities from the Graph search pattern. 
     * 
     * @return The component entities from the graph search pattern. 
     */
    public ArrayList<ComponentEntity> getComponentEntitiesList() {
        return _componentEntitiesList;
    }

    /**
     * Get the ports from the Graph search pattern.
     * 
     * @return The ports from the graph search pattern. 
     */
    public ArrayList<Port> getPortsList() {
        return _portsList;
    }

    /**
     * Get the relations from the Graph search pattern.
     * 
     * @return The relations from the graph search pattern. 
     */
    public ArrayList<Relation> getRelationsList() {
        return _relationsList;
    }

    /**
     * Set the component entities from the Graph search pattern. 
     * 
     * @param componentEntitiesList The component entities from the graph 
     *  search pattern. 
     */
    public void setComponentEntitiesList(
            ArrayList<ComponentEntity> componentEntitiesList) {
        _componentEntitiesList = componentEntitiesList;
    }

    /**
     * Set the ports from the Graph search pattern.
     * 
     * @param portsList The ports from the graph search pattern. 
     */
    public void setPortsList(ArrayList<Port> portsList) {
        _portsList = portsList;
    }

    /**
     * Set the relations from the Graph search pattern.
     * 
     * @param relationsList The relations from the graph search pattern. 
     */
    public void setRelationsList(ArrayList<Relation> relationsList) {
        _relationsList = relationsList;
    }

    //////////////////////////////////////////////////////////////////////
    ////		private variables				//////

    private ArrayList<Port> _portsList;

    private ArrayList<Relation> _relationsList;

    private ArrayList<ComponentEntity> _componentEntitiesList;
}
