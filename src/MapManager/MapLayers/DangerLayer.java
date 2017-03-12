package MapManager.MapLayers;

import MapManager.Block;
import MapManager.PotentialField;
import bwapi.Color;
import bwapi.Game;
import bwapi.Unit;
import bwapi.UnitType;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by Chudjak Kristián on 25.02.2017.
 */
public class DangerLayer extends Layer {
    private static final int BLOCK_SIZE = 16;
    private ArrayList<PotentialField> dangerFields;
    public static final double DAMAGE_MODIFIER = 0.3;

    public DangerLayer(Game game) {
        super(game, BLOCK_SIZE);
        this.dangerFields = new ArrayList<>();

    }

    @Override
    public void manage() {
        if (game.getFrameCount() % 20 == 0) {
            gridMap.refreshStart();
            for (PotentialField pf :
                    dangerFields) {
                refreshGridMapNonRecursive(pf);

            }
        }

    }


    @Override
    public void draw() {
        for(PotentialField pf:dangerFields) {
            pf.showGraphicsCircular(Color.Orange);
        }
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

    public void refreshGridMapNonRecursive(PotentialField pf) {
        LinkedList<Block> zmenene = gridMap.zmenene;
        Queue<Block> blocks = gridMap.blocks;
        zmenene.clear();
        blocks.clear();
        Block centerBlock = gridMap.getBlockByPosition_blockMap(pf.getPosition());
        UnitType unit = pf.getUnitType();
        pf.setCenterValue(getBlokValue(unit));
        gridMap.blokSetValue(centerBlock, pf.getCenterValue());
        setValuesForBlock(centerBlock,pf);
        ArrayList<Block> neighbour = gridMap.getNeighbourBlocks(centerBlock);
        for (Block b :
                neighbour) {
            blocks.add(b);
            gridMap.blokSetValue(b, pf.getFunction().getBlockValue(pf, b));
            setValuesForBlock(b,pf);
        }

        while (!blocks.isEmpty()) {
            Block block = blocks.poll();
            neighbour = gridMap.getNeighbourBlocks(block);

            for (Block b :
                    neighbour) {
                if (pf.isPositionInRange(b.getPosition())) {
                    if (!b.isSet()) {
                        gridMap.blokSetValue(b, pf.getFunction().getBlockValue(pf, b));
                        setValuesForBlock(b,pf);
                        blocks.add(b);
                    }
                }
            }

        }


        for (Block block :
                zmenene) {
            block.setSet(false);
        }
    }


    public void setValuesForBlock(Block blok, PotentialField pf) {
        blok.setInPotentialField(true);
        boolean airToAir = pf.getUnitType().airWeapon().targetsAir();
        boolean airToGround = pf.getUnitType().airWeapon().targetsGround();
        boolean groundToGround = pf.getUnitType().groundWeapon().targetsGround();
        boolean groundToAir = pf.getUnitType().groundWeapon().targetsAir();

        if (airToAir || groundToAir) {
            blok.setAirDamage(true);
        }
        if (airToGround || groundToGround) {
            blok.setGroundDamage(true);
        }

        //System.out.println("Air DMG = "+blockMap[row][col].isAirDamage());
        //System.out.println("Ground DMG = "+blockMap[row][col].isGroundDamage());

        if (pf.getUnitType().groundWeapon().maxRange() > 30 || pf.getUnitType().airWeapon().maxRange() > 30) {
            blok.setValue(blok.getValue() + DAMAGE_MODIFIER * pf.getUnitType().groundWeapon().damageAmount());
        } else {
            blok.setValue(blok.getValue() + DAMAGE_MODIFIER * (pf.getRangeLengthInPercent(blok.getPosition()) * (pf.getUnitType().groundWeapon().damageAmount())));
        }
    }

    public double getBlokValue(UnitType unit) {
        double value = unit.airWeapon().damageAmount() / (1 + unit.airWeapon().damageCooldown()) * 10;
        value += unit.groundWeapon().damageAmount() / (1 + unit.airWeapon().damageCooldown()) * 10;
        return value;
    }


    public ArrayList<PotentialField> getDangerFields() {
        return dangerFields;
    }
}
