package MapManager.MapLayers;

import MapManager.GridBasedMap;
import bwapi.Color;
import bwapi.Game;

import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by Chudjak Kristián on 25.02.2017.
 */
public class MapLayersManager {
    private Game game;

    private DangerLayer dangerLayer;
    private WalkableLayer walkableLayer;
    private StaticWalkableLayer walkableLayerStatic;

    private HashMap<Integer, Layer> layers;


    public static final int DMGGRIDSIZE=18; //18
    public MapLayersManager(Game game) {
        this.game = game;
        this.layers = new HashMap<>();
        walkableLayer = new WalkableLayer(game);
        walkableLayerStatic = new StaticWalkableLayer(game);
        dangerLayer = new DangerLayer(game);



    }


    public HashMap<Integer, Layer> getLayers() {
        return layers;
    }

    public DangerLayer getDangerLayer() {
        return dangerLayer;
    }

    public void manage(){
        //walkableLayer.manage();
        dangerLayer.manage();
        for (Layer layer :
                layers.values()) {
            layer.manage();

        }
    }

    public void draw() {
        //walkableLayerStatic.draw();
        walkableLayer.draw();
        dangerLayer.draw();
        for (Layer layer :
                layers.values()) {
            layer.draw();

        }
    }

    public void testMethod() {
        long milis = System.currentTimeMillis();
        System.out.println("[14,1]= "+walkableLayerStatic.gridMap.getBlockMap()[14][1].getValue());
        System.out.println("[14,1]= "+walkableLayer.gridMap.getBlockMap()[14][1].getValue());
        Layer l = walkableLayerStatic.add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).add(walkableLayer).
                add(walkableLayer).add(walkableLayer);
        GridBasedMap map = l.gridMap;

        System.out.println("after add");
        System.out.println("[14,1]= "+walkableLayerStatic.gridMap.getBlockMap()[14][1].getValue());
        System.out.println("[14,1]= "+walkableLayer.gridMap.getBlockMap()[14][1].getValue());
        System.out.println("[14,1]= "+map.getBlockMap()[14][1].getValue());

        System.out.println("TEST: milis: "+(System.currentTimeMillis() - milis));
    }

    public StaticWalkableLayer getWalkableLayerStatic() {
        return walkableLayerStatic;
    }

    public WalkableLayer getWalkableLayer() {
        return walkableLayer;
    }
}
