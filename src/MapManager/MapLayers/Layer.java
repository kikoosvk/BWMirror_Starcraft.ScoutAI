package MapManager.MapLayers;

import MapManager.GridMap2;
import bwapi.Game;

/**
 * Created by Chudjak Kristián on 27.02.2017.
 */
public class Layer {
    protected Game game;
    protected GridMap2 gridMap;

    public void manage() {

    }

    public void draw(){

    }

    public void add(Layer layer, GridMap2 merged){
        this.gridMap.add(layer.gridMap,merged);
    }

    public void sub(Layer layer, GridMap2 gridMap){

    }


}
