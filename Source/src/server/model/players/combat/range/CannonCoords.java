package server.model.players.combat.range;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import server.model.players.Player;
import server.util.Direction;

/**
 * For this to work and to be able to find all the npcs around within a 12 tile distance, and 
 * find the npcs when the cannon is facing their way, we are going to preload all the coords
 * into this map to be grabbed later on to check if any npcs are around.
 * 
 * @author Rodrigo Molina
 */
public class CannonCoords {

        private Map<Direction, List<Integer>> coords = new HashMap<Direction, List<Integer>>();
        
        private static final int MAX_DISTANCE = 12;
        
        @SuppressWarnings("unused")
        private Player player;
        
        public CannonCoords(Player player) {
                this.player = player;
        }
        

        /**
         * Checks if the coords inside the map equals to the npc x and y.
         * @param s
         * @param npcX
         * @param npcY
         * @return s
         */
        public boolean checkCoords(Direction s, int npcX, int npcY) {
                return coords.get(s).contains(npcX) || coords.get(s).contains(npcY);
        }
        
        /**
         * Fills up the map with the specified coords around the cannon.
         * @param obX
         * @param obY
         */
        public void fillUp(int obX, int obY) {
                coords.put(Direction.NORTH, new LinkedList<Integer>());
                coords.put(Direction.NORTH_WEST, new LinkedList<Integer>());
                coords.put(Direction.NORTH_EAST, new LinkedList<Integer>());
                coords.put(Direction.SOUTH, new LinkedList<Integer>());
                coords.put(Direction.SOUTH_EAST, new LinkedList<Integer>());
                coords.put(Direction.SOUTH_WEST, new LinkedList<Integer>());
                coords.put(Direction.EAST, new LinkedList<Integer>());
                coords.put(Direction.WEST, new LinkedList<Integer>());
                if(!coords.get(Direction.NORTH).isEmpty()) 
                    return;
                addCoords(obX, obY);
        }

        public void addCoords(final int ob1, final int ob2) {
                int obX = ob1;
                int obY = ob2;
                for(int j = obX; j <= obX + MAX_DISTANCE; j--) {
                        if(j < obX - MAX_DISTANCE) break;
                        coords.get(Direction.WEST).add(j);
                        coords.get(Direction.SOUTH_WEST).add(j);
                }
                
                for(int j = obY; j <= obY + MAX_DISTANCE; j--) {
                        if(j < obY - MAX_DISTANCE) break;
                        coords.get(Direction.SOUTH).add(j);
                        coords.get(Direction.SOUTH_EAST).add(j);
                }
                
                for(int j = obX; j <= obX + MAX_DISTANCE; j++) {
                        if(j < obX - MAX_DISTANCE) break;
                        coords.get(Direction.EAST).add(j);
                        coords.get(Direction.NORTH_EAST).add(j);
                }
                
                for(int j = obY; j <= obY + MAX_DISTANCE; j++) {
                        if(j < obY - MAX_DISTANCE) break;
                        coords.get(Direction.NORTH).add(j);
                        coords.get(Direction.NORTH_WEST).add(j);
                }
        }
        public void clear() {
            coords.clear();
        }
}