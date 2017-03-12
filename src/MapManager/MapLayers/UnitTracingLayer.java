package MapManager.MapLayers;

import MapManager.Block;
import MapManager.PotentialField;
import bwapi.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Chudjak Kristián on 12.03.2017.
 */
public class UnitTracingLayer extends Layer {
    private PotentialField pf;
    private Unit unit;

    public UnitTracingLayer(Game game, int size, Unit unit) {
        super(game, size);
        this.unit = unit;
        pf = new PotentialField(game, unit, 200, false);
        pf.setCenterValue(10);
    }

    @Override
    public void manage() {
        pf.refreshPosition(unit);
        if (game.getFrameCount() % 20 == 0)
            this.gridMap.addPfToGridMap(pf);
    }

    @Override
    public void draw() {
        pf.showGraphicsCircular(Color.Cyan);

        List<Unit> units = game.getSelectedUnits();

        for (Unit unit :
                units) {
            int row = unit.getPosition().getY() / 16;
            int col = unit.getPosition().getX() / 16;
            for (int i = row - 5; i < row + 5; i++) {
                for (int j = col - 5; j < col + 5; j++) {
                    if (i >= 0 && i < gridMap.getBlockMap().length) {
                        if (j >= 0 && j < gridMap.getBlockMap()[0].length)
                            gridMap.getBlockMap()[i][j].drawBlock(Color.Green, game);

                    }
                }
            }


        }


    }
}
