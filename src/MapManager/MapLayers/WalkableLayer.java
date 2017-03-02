package MapManager.MapLayers;

import MapManager.Block;
import MapManager.GridMap2;
import bwapi.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Chudjak Kristián on 27.02.2017.
 */
public class WalkableLayer extends Layer {
    private static final int TILE_SIZE = 32;
    private static final int STARTING_VALUE = 5;

    public WalkableLayer(Game game) {
        this.game = game;
        System.out.println("Map size: " + game.mapWidth() + "," + game.mapHeight());
        this.gridMap = new GridMap2(game.mapWidth(), game.mapHeight());
        for (int i = 0; i < game.mapWidth(); i++) {
            for (int j = 0; j < game.mapHeight(); j++) {
                Position position = new Position(j * TILE_SIZE + TILE_SIZE / 2, i * TILE_SIZE + TILE_SIZE / 2);
                Block b = new Block(position, TILE_SIZE, i, j);
                b.setValue(isTileWalkable(position) ? 0 : STARTING_VALUE);
                gridMap.getBlockMap()[i][j] = b;
            }
        }

        bwta();

        for (int i = 0; i < game.mapWidth(); i++) {
            for (int j = 0; j < game.mapHeight(); j++) {
                Block b = gridMap.getBlockMap()[i][j];
                if (b.getValue() == STARTING_VALUE) {
                    setWalkableGridMap(b);
                }
            }
        }

    }

    private void bwta() {
        for (Unit unit :
                game.getAllUnits()) {
            setResourcesFields(unit);
            setCommandCentres(unit);
        }
    }


    /*private boolean isTileWalkable(Position position) {
        return game.isWalkable(position.getX() / 8, position.getY() / 8);
    }*/
    private boolean isTileWalkable(Position position) {
        return game.isWalkable((position.getX() / 8) -2, (position.getY() / 8) -1) &&
                game.isWalkable((position.getX() / 8) - 2, (position.getY() / 8) +1) &&
                game.isWalkable((position.getX() / 8) + 1, (position.getY() / 8) -1) &&
                game.isWalkable((position.getX() / 8) + 1, (position.getY() / 8) +1);
    }



    @Override
    public void draw() {
        List<Unit> units = game.getSelectedUnits();

        for (Unit unit :
                units) {
            int row = unit.getPosition().getY() / TILE_SIZE;
            int col = unit.getPosition().getX() / TILE_SIZE;
            for (int i = row - 5; i < row + 5; i++) {
                for (int j = col - 5; j < col + 5; j++) {
                    if (i >= 0 && i < gridMap.getBlockMap().length) {
                        if (j >= 0 && j < gridMap.getBlockMap()[0].length)
                            if (gridMap.getBlockMap()[i][j].getValue() == STARTING_VALUE) {
                                gridMap.getBlockMap()[i][j].drawBlock(Color.Red, game);
                            } else {
                                gridMap.getBlockMap()[i][j].drawBlock(Color.Green, game);
                            }
                    }
                }
            }


        }

    }


    public void setWalkableGridMap(Block block) {
        gridMap.zmenene.clear();


        Queue<Block> blocks = new LinkedList<>();
        Block centerBlock = block;
        gridMap.blokSetValue(centerBlock, STARTING_VALUE);
        ArrayList<Block> neighbour = gridMap.getNeighbourBlocks(centerBlock);
        for (Block b :
                neighbour) {
            blocks.add(b);
            setBlockValue(b, centerBlock);
        }
        while (!blocks.isEmpty()) {
            Block curBlock = blocks.poll();
            neighbour = gridMap.getNeighbourBlocks(curBlock);

            for (Block b :
                    neighbour) {
                if (!b.isSet()) {
                    setBlockValue(b, curBlock);
                    blocks.add(b);
                }

            }

        }

        for (Block b :
                gridMap.zmenene) {
            b.setSet(false);
        }


    }

    private void setBlockValue(Block b, Block centerBlock) {
        gridMap.blokSetValue(b, ((centerBlock.getValue() <= 0) ? 0 : centerBlock.getValue() - 1));
    }


    private void setCommandCentres(Unit unit) {
        if (unit.getType() == UnitType.Terran_Command_Center ||
                unit.getType() == UnitType.Zerg_Infested_Command_Center ||
                unit.getType() == UnitType.Protoss_Nexus){
            Position position = unit.getPosition();
            Block block = gridMap.getBlock(position);
            block.setValue(STARTING_VALUE);

            gridMap.getBlockMap()[block.getRow() - 1][block.getColumn() -2].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow() - 1][block.getColumn() -1].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow() - 1][block.getColumn()].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow() - 1][block.getColumn() + 1].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow()][block.getColumn() - 2].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow()][block.getColumn() - 1].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow()][block.getColumn() + 1].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow() + 1][block.getColumn() - 2].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow() + 1][block.getColumn() - 1].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow() + 1][block.getColumn()].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow() + 1][block.getColumn() + 1].setValue(STARTING_VALUE);

        }
    }


    public boolean isMineralField(Unit unit) {
        if (unit.getType() == UnitType.Resource_Mineral_Field ||
                unit.getType() == UnitType.Resource_Mineral_Field_Type_2 ||
                unit.getType() == UnitType.Resource_Mineral_Field_Type_3) {
            return true;
        }
        return false;
    }

    private void setResourcesFields(Unit unit) {
        // mineral field
        if (isMineralField(unit)) {
            Position position = unit.getPosition();
            Block block = gridMap.getBlock(position);
            block.setValue(STARTING_VALUE);

            if (block.getRow() != 0) {
                gridMap.getBlockMap()[block.getRow() - 1][block.getColumn()].setValue(STARTING_VALUE);
            }
            if (block.getColumn() != 0) {
                gridMap.getBlockMap()[block.getRow()][block.getColumn() - 1].setValue(STARTING_VALUE);
            }

            if (block.getColumn() != 0 && block.getRow() != 0) {
                gridMap.getBlockMap()[block.getRow() - 1][block.getColumn() - 1].setValue(STARTING_VALUE);
            }

        } else if (unit.getType() == UnitType.Resource_Vespene_Geyser) {
            Position position = unit.getPosition();
            Block block = gridMap.getBlock(position);
            block.setValue(STARTING_VALUE);
            System.out.println("size: " + unit.getType().size());
            System.out.println(block.getRow() + "," + block.getColumn());
            gridMap.getBlockMap()[block.getRow() - 1][block.getColumn()].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow() - 1][block.getColumn() - 1].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow() - 1][block.getColumn() - 2].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow()][block.getColumn() - 1].setValue(STARTING_VALUE);
            gridMap.getBlockMap()[block.getRow()][block.getColumn() - 2].setValue(STARTING_VALUE);


        }


    }
}
