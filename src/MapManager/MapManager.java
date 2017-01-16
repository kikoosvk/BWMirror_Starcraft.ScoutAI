package MapManager;

import MODaStar.AStarModule;
import MODaStar.AStarPathCalculator;
import MODaStar.GridMap;
import bwapi.*;
import bwta.BWTA;
import bwta.BaseLocation;
import bwta.Chokepoint;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chudjak Kristián on 05.01.2017.
 */
public class MapManager {
    /**
     * Size of the edge of grid block
     */
    public static final int GRIDEDGESIZE = 18; //18

    private AStarModule aStarModule;

    private HeatMap heatMap;

    /**
     * Postiton of my base
     */
    private Position myBasePosition;

    /**
     * List of enemy base positons
     */
    private ArrayList<PotentialField> enemyBasePositions;

    /**
     * List of expansion positons
     */
    private ArrayList<PotentialField> expansionPositions;

    /**
     * List of danger fields
     */
    private ArrayList<PotentialField> dangerFields;

    /**
     * List of retreat fields
     */
    private ArrayList<PotentialField> retreatFields;

    private List<Chokepoint> chokePoints;

    private ArrayList<ScoutingArea> scoutingAreas;

    private ArrayList<ScoutingArea> armyArea;

    private AStarPathCalculator staticPathCalculator;


    /**
     * Creates the instance of MapManager and initializes variables
     */
    public MapManager(Game game) {
        heatMap = new HeatMap(game);
        enemyBasePositions = new ArrayList<>();
        expansionPositions = new ArrayList<>();
        dangerFields = new ArrayList<>();
        retreatFields = new ArrayList<>();
        scoutingAreas = new ArrayList<>();
        aStarModule = new AStarModule(new GridMap(MapManager.GRIDEDGESIZE, game));
    }

    public MapManager(Game game, AStarModule pAStarModule, HeatMap pHeatMap) {
        enemyBasePositions = new ArrayList<>();
        expansionPositions = new ArrayList<>();
        dangerFields = new ArrayList<>();
        retreatFields = new ArrayList<>();
        scoutingAreas = new ArrayList<>();
        aStarModule = pAStarModule;
        heatMap = pHeatMap;
    }

    /**
     * Creates the instance of MapManager with given lists of enemy base positions and expansion positions
     *
     * @param pEnemyBasePositions
     * @param pExpansionPositions
     */
    public MapManager(ArrayList pEnemyBasePositions, ArrayList pExpansionPositions, Game game) {
        enemyBasePositions = pEnemyBasePositions;
        expansionPositions = pExpansionPositions;
        dangerFields = new ArrayList<>();
        retreatFields = new ArrayList<>();
    }


    public void refreshMap(Game pGame) {
        Player player = pGame.self();
        boolean isEnemy;
        boolean canAttack;


        for (PotentialField pf :
                dangerFields) {
            Unit unit = pf.getUnit();
            if (!unit.isVisible()) {
                aStarModule.getGridMap().refreshGridMap(pf);
                dangerFields.remove(pf);
                break;
            }
            pf.setX(unit.getX());
            pf.setY(unit.getY());
            break;
        }

        for (Unit pUnit : pGame.enemy().getUnits()) {
            isEnemy = pUnit.getPlayer().isEnemy(player);
            canAttack = pUnit.getType().canAttack();
            if (pUnit.isVisible() && canAttack) {// && !pUnit.getType().isWorker()
                PotentialField pf = getDangerFieldByID(pUnit.getID());
                if (pf != null) {
                    if (pf.getX() != pUnit.getPosition().getX() || pf.getY() != pUnit.getPosition().getY()) {

                        aStarModule.getGridMap().refreshGridMap(pf);
                        pf.setX(pUnit.getPosition().getX());
                        pf.setY(pUnit.getPosition().getY());
                        aStarModule.getGridMap().updateGridMap(pf);
                        System.out.println("moved: " + pf.getX() + " " + pf.getY());
                    }
                } else {
                    pf = new PotentialField(pGame, pUnit);
                    System.out.println("added field");
                    dangerFields.add(pf);
                    aStarModule.getGridMap().updateGridMap(pf);
                }
            }
        }


/*
        for(Unit pUnit:pGame.enemy().getUnits()) {
            isEnemy = pUnit.getPlayer().isEnemy(player);
            canAttack = pUnit.getType().canAttack();
            if (isEnemy  ) {// && !pUnit.getType().isWorker()
                PotentialField pf = getDangerFieldByID(pUnit.getID());
                if (pf != null) {
                    if(pf.getX()!=pUnit.getPosition().getX()||pf.getY()!=pUnit.getPosition().getY()) {

                        aStarModule.getGridMap().refreshGridMap(pf);
                        pf.setX(pUnit.getPosition().getX());
                        pf.setY(pUnit.getPosition().getY());
                        aStarModule.getGridMap().updateGridMap(pf);
                        System.out.println("moved: "+pf.getX()+" "+pf.getY());
                    }
                } else {
                    pf = new PotentialField(pGame, pUnit);
                    System.out.println("added field");
                    dangerFields.add(pf);
                    aStarModule.getGridMap().updateGridMap(pf);
                }
            }
        }




        for(PotentialField pf:dangerFields) {
            if(pf.isVisible(pGame.self().getUnits())) {
                if(pGame.enemy().getUnits().size()<1 || removeField(pGame,pf)) {
                    aStarModule.getGridMap().refreshGridMap(pf);
                    System.out.println("removed field");
                    dangerFields.remove(pf);
                }
            }
        }

*/

    }


    public void refreshDangerField(Game pGame) {
        if (pGame.getFrameCount() % 30 == 0) {
            refreshMap(pGame);
        }
    }

    private boolean removeField(Game pGame, PotentialField pf) {
        for (Unit unit :
                pGame.enemy().getUnits()) {
            if (unit.getID() == pf.getId()) {
                return false;
            }
        }
        return true;
    }


    public PotentialField getDangerFieldByID(int pID) {
        for (PotentialField pf : dangerFields) {
            if (pf.getId() == pID) {
                return pf;
            }
        }
        return null;
    }

    public AStarModule getaStarModule() {
        return aStarModule;
    }


    public AStarPathCalculator buildPath(Unit pUnit, Position pDestination, int pLevelOfSafety, boolean pAirPath, Game game, Color color) {
        return aStarModule.buildPath(pUnit.getPosition(), pDestination, pUnit.getHitPoints(), pLevelOfSafety, pAirPath, game, color);
    }


    public AStarPathCalculator buildPath(Unit pUnit, Position pStart, Position pDestination, int pLevelOfSafety, boolean pAirPath, Game game, Color pColor) {
        return aStarModule.buildPath(pStart, pDestination, pUnit.getHitPoints(), pLevelOfSafety, pAirPath, game, pColor);
    }


    public ArrayList<PotentialField> getDangerFields() {
        return dangerFields;
    }

    public GridMap getGridMap() {
        return aStarModule.getGridMap();
    }

    ;

    public void initializeAll(Game game) {
        initializeHeatMap(game);
        initializeChokePoints(game);
        initializeEnemyBaseLocations(game);
        initializeScoutingAreas(game);
    }


    public void initializeHeatMap(Game game) {
        heatMap.initializeHeatMap(500, game);
    }


    /**
     * Initializes chokepoints from the map
     *
     * @param game
     */
    public void initializeChokePoints(Game game) {
        chokePoints=BWTA.getChokepoints();

        for(Chokepoint choke:chokePoints) {
            /*CONSOLE LOG*/
            System.out.println("Added chokepoint at position :"+choke.getCenter().toString());
            /*END CONSOLE LOG*/
        }

    }

    public void drawAll(Game game) {
        drawHeatMap(game);
        drawDangerFields(game);
    }

    public void drawHeatMap(Game game) {
        heatMap.drawHeatMap(game);
    }

    public void drawDangerFields(Game pGame) {
        for (PotentialField pf : dangerFields) {
            pf.showGraphicsCircular(pGame, Color.Orange);
        }
    }

    public void manageAll(Game game) {
        manageHeatMap(game);
        refreshDangerField(game);
    }

    public void manageHeatMap(Game game) {
        heatMap.heatManagement(game);
    }

    public ScoutingArea getEnemyArmyArea() {
        ScoutingArea area=new ScoutingArea();
        PotentialField centerBlock;
        int cRow;
        int cCol;
        for(PotentialField pf:dangerFields) {
            centerBlock=heatMap.getHeatBlockContainingPosition(pf.getPosition());
            cRow=centerBlock.getRow();
            cCol=centerBlock.getColumn();
            for(int i=cRow-1;i<=cRow+1;i++) {
                if(i>=0&&i<heatMap.getColumns()) {
                    for(int j=cCol-1;j<=cCol+1;j++) {
                        if(j>=0&&j<heatMap.getColumns()) {
                            if(!area.getFieldArray().contains(heatMap.getHeatBlock(i,j))) {
                                area.insert(heatMap.getHeatBlock(i,j));
                            }
                        }
                    }
                }
            }
        }
        if(area.getFieldArray().size()>0) {
            return area;
        }
        return null;
    }

    public ScoutingArea getLeastVisitedEnemyArmyArea() {
        ScoutingArea area = new ScoutingArea();
        PotentialField centerBlock;
        int cRow;
        int cCol;
        double heatLvl = Double.MIN_VALUE;
        int index = -1;
        for (int i = 0; i < dangerFields.size(); i++) {
            if (dangerFields.get(i).getHeat() > heatLvl) {
                heatLvl = dangerFields.get(i).getHeat();
                index = i;
            }

        }
        if (index != -1) {
            centerBlock = heatMap.getHeatBlockContainingPosition(dangerFields.get(index).getPosition());
            cRow = centerBlock.getRow();
            cCol = centerBlock.getColumn();
            for (int i = cRow - 1; i <= cRow + 1; i++) {
                if (i >= 0 && i < heatMap.getColumns()) {
                    for (int j = cCol - 1; j <= cCol + 1; j++) {
                        if (j >= 0 && j < heatMap.getColumns()) {
                            if (!area.getFieldArray().contains(heatMap.getHeatBlock(i, j))) {
                                area.insert(heatMap.getHeatBlock(i, j));
                            }
                        }
                    }
                }
            }
        }
        if (area.getFieldArray().size() > 0) {
            return area;
        }
        return null;
    }

    public void initializeEnemyBaseLocations(Game pGame) {
        /*CONSOLE LOG*/
        System.out.println("Initializing base locations...");
        /*END CONSOLE LOG*/

        for(BaseLocation b: BWTA.getBaseLocations()) {
            if(!pGame.isVisible(b.getTilePosition())) {
                if(b.isStartLocation()) {
                    addEnemyBasePosition(pGame,b.getPosition());
                    /*CONSOLE LOG*/
                    System.out.println("Added base location at "+b.getPosition().toString());
                    /*END CONSOLE LOG*/
                } else {
                    addExpansionPosition(pGame,b.getPosition());
                    /*CONSOLE LOG*/
                    System.out.println("Added expansion position at " + b.getPosition().toString());
                    /*END CONSOLE LOG*/
                }
            } else {
                if(b.isStartLocation()) {
                    myBasePosition=b.getPosition();
                    /*CONSOLE LOG*/
                    System.out.println("Added home base location at "+b.getPosition().toString());
                    /*END CONSOLE LOG*/
                }

            }
        }
    }

    public void initializeScoutingAreas(Game pGame) {
        /* Initialize enemy base area */
        Position eBasePosition=enemyBasePositions.get(0).getPosition();
        PotentialField centerBlock=heatMap.getHeatBlockContainingPosition(eBasePosition);
        int cRow=centerBlock.getRow();
        int cCol=centerBlock.getColumn();
        ScoutingArea eBaseArea=new ScoutingArea();

        for(int i=cRow-1;i<=cRow+1;i++) {
            if(i>=0&&i<heatMap.getColumns()) {
                for(int j=cCol-1;j<=cCol+1;j++) {
                    if(j>=0&&j<heatMap.getColumns()) {
                        eBaseArea.insert(heatMap.getHeatBlock(i,j));
                    }
                }
            }
        }
        System.out.println("eBase area size = " + eBaseArea.size() + " block");
        for(PotentialField pf:eBaseArea.getFieldArray()) {
            System.out.println("Field ["+pf.getRow()+";"+pf.getColumn()+"]");
        }
        eBaseArea.setID(ScoutingArea.BASEAREA);
        scoutingAreas.add(eBaseArea);


        Unit u = null;
        for (Unit unit :
                pGame.self().getUnits()) {
            if(unit.getType() == UnitType.Terran_SCV) {
                u = unit;
                break;
            }
        }
        if(staticPathCalculator==null) {
            staticPathCalculator = buildPath(u, enemyBasePositions.get(0).getPosition(),
                    1, false, pGame, Color.Green);
        }

        //ToDo: initialization of other paths
    }


    /**
     * Adds enemy base position to the list
     *
     * @param basePosition
     */
    public void addEnemyBasePosition(Game game, Position basePosition) {
        PotentialField basePF=new PotentialField(game, basePosition, UnitType.Terran_Command_Center.sightRange());
        if(!enemyBasePositions.contains(basePF)) {
            enemyBasePositions.add(basePF);
        }
    }

    /**
     * Adds expansion position to the list
     *
     * @param expansionPosition
     */
    public void addExpansionPosition(Game game, Position expansionPosition) {
        PotentialField expansionPF=new PotentialField(game,expansionPosition,UnitType.Terran_Command_Center.sightRange());
        if(!expansionPositions.contains(expansionPF)) {
            expansionPositions.add(expansionPF);
        }
    }



}
