package MapManager.MapLayers;

import MapManager.GridMap2;
import bwapi.Game;

import javax.print.DocFlavor;
import java.util.HashMap;

/**
 * Created by Chudjak Kristián on 25.02.2017.
 */
public class MapLayersManager {
    private Game game;

    private GridMap2 dangerMap;
    private WalkableLayer walkableLayer;
    private HashMap<Integer,GridMap2> layers;

    public static final int DMGGRIDSIZE=18; //18
    public MapLayersManager(Game game) {
        this.game = game;
        this.layers = new HashMap<>();
        walkableLayer = new WalkableLayer(game);
        dangerMap = new GridMap2(DMGGRIDSIZE, game);

    }


    public HashMap<Integer, GridMap2> getLayers() {
        return layers;
    }

    public GridMap2 getDangerMap() {
        return dangerMap;
    }

    public void manage(){

    }

    public void draw() {
        walkableLayer.draw();
    }
}
