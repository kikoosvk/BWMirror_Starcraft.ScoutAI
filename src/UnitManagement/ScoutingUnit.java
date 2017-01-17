package UnitManagement;

import MODaStar.AStarPathCalculator;
import MODaStar.Block;
import MapManager.*;
import ScoutModule.Scout_module;
import bwapi.Color;
import bwapi.Game;
import bwapi.Unit;
import bwta.BWTA;

import java.util.ArrayList;

/**
 * ScoutingUnit represents exact unit set for scouting tasks. Management of movement is independent of other modules.
 * ScoutingUnit provides reactive behavior and triggers other detection methods.
 */
public class ScoutingUnit {

    public static final int START_SAFEZONERADIUS=500;

    public static final int END_SAFEZONERADIUS=800;

    public static final int LOCALCHECK_PATH_SIZE=61;

    private Unit unit;

    private AStarPathCalculator aStarPathCalculator;

    private AStarPathCalculator microPathCalculator;

    private ArrayList<Block> path;

    private ScoutingArea scoutingArea;

    private ArrayList<Block> microPath;

    private boolean hasOrder;

    private boolean finishedOrder;

    private boolean hasTask;

    private Block microDestinationBlock;


    /* ------------------- Constructors ------------------- */

    public ScoutingUnit(Unit pUnit) {
        path=new ArrayList<>();
        microPath=new ArrayList<>();
        unit=pUnit;
        hasOrder=false;
        finishedOrder=true;
        hasTask=false;

    }


    /* ------------------- main functionality methods ------------------- */

    public void scout(AStarPathCalculator pAStarPathCalculator, boolean pIsTask) {
        finishedOrder=false;
        hasOrder=true;
        hasTask=pIsTask;
        aStarPathCalculator=pAStarPathCalculator;
    }

    public void micro(AStarPathCalculator pMicroPathCalculator) {microPathCalculator=pMicroPathCalculator; }


    /* ------------------- initialising methods ------------------- */


    /* ------------------- real-time management methods ------------------- */

    public void enemyDetected(Unit pUnit, MapManager pMapManager, Game pGame) {
        //do podmienky      microPathCalculator==null&&microPath.isEmpty()&&
        if(unit.getPosition().getDistance(pUnit.getPosition())<unit.getType().sightRange()+pUnit.getType().sightRange()) {
            pMapManager.refreshMap(pGame);
            for(Block b:path) {
                b.setDamage(pMapManager.getaStarModule().getGridMap().getBlockMap()[b.getRow()][b.getColumn()].getDamage());
            }
            if(unit.getType().isFlyer()) {
                if(pUnit.getType().groundWeapon().targetsAir()||pUnit.getType().airWeapon().targetsAir()) {
                    micro(pMapManager,pGame);
                }
            } else {
                if(pUnit.getType().groundWeapon().targetsGround()||pUnit.getType().airWeapon().targetsGround()) {
                    micro(pMapManager,pGame);
                }
            }

        }

    }

    public void manageAll(MapManager pMapManager, Game pGame) {
        manageMovement();
        managePathCalculator();
        manageMicroPathCalculator();
        localDangerCheck(pMapManager, pGame);
        manageIdle();
        manageScoutingArea(pGame, pMapManager);
        manageFinishedSignalization();
    }

    public void managePathCalculator() {
        if(aStarPathCalculator!=null) {
            if(aStarPathCalculator.finished) {
                microDestinationBlock=null;
                microPath=new ArrayList<>();
                path=aStarPathCalculator.getBlockPathArray();
                if(path==null) {
                    System.out.println("Zmierni podmienky");
                    path=new ArrayList<>();
                    hasOrder=false;
                }
                aStarPathCalculator=null;
            }
        }
    }

    public void manageMicroPathCalculator() {
        if(microPathCalculator!=null) {
            if(microPathCalculator.finished) {
                if(microPathCalculator.getBlockPathArray()!=null) {
                    microPath = microPathCalculator.getBlockPathArray();
                    for (int i = 0; i < 3; i++) {
                        microPath.remove(microPath.size() - 1);
                    }
                }
                microPathCalculator = null;
            }
        }
    }

    public void manageMovement() {
        if(!microPath.isEmpty()&&path.isEmpty()) {
            manageMicroPath();
        } else {
            managePath();
        }
    }

    public void manageMicroPath() {
        if(microPath.size()>0) {
            hasOrder=true;
            if(unit.getPosition().getDistance(microPath.get(microPath.size()-1).getPosition())<90) {
                unit.move(microPath.get(microPath.size() - 1).getPosition(), false);
                microPath.remove(microPath.size() - 1);
            }
        }
    }

    public void managePath() {
        if(path.size()>0) {
            hasOrder=true;
            finishedOrder=false;
            if(unit.getPosition().getDistance(path.get(path.size()-1).getPosition())<90) {
                unit.move(path.get(path.size() - 1).getPosition(), false);
                path.remove(path.size() - 1);
                if(path.isEmpty()) {
                    hasOrder=false;
                    hasTask=false;
                }
            } else {
                manageMicroPath();
            }
        }
    }

    public void manageIdle() {
        if(microPath.size()>0&&unit.isIdle()) {
            unit.rightClick(microPath.get(microPath.size()-1).getPosition(),false);
        } else if(path.size()>0&&unit.isIdle()) {
            unit.rightClick(path.get(path.size()-1).getPosition(),false);
        }
    }

    public void manageFinishedSignalization() {
        if(hasOrder&&path!=null) {
            if(unit.getPosition().getDistance(path.get(0).getPosition())<100) {
                path=new ArrayList<>();
                microPath=new ArrayList<>();
                finishedOrder=true;
                hasOrder=false;
            }
        }
    }

    public void manageScoutingArea(Game pGame, MapManager pMapManager) {
        if(scoutingArea!=null&&scoutingArea.size()>0) {
            if(aStarPathCalculator==null&&path.size()>0) {
                if(pGame.isVisible(path.get(0).getPosition().toTilePosition())) {
                    scoutingArea.remove(scoutingArea.getNearestField(this,pGame));
                    aStarPathCalculator=pMapManager.buildPath(unit,scoutingArea.getNearestField(this,pGame).getPosition(), Scout_module.SAFETY_LEVEL,unit.getType().isFlyer(),pGame, Color.Yellow);
                }
            } else if(aStarPathCalculator==null) {
                PotentialField field=scoutingArea.getNearestField(this,pGame);
                if(pGame.isVisible(field.getPosition().toTilePosition())) {
                    scoutingArea.remove(field);
                } else {
                    aStarPathCalculator=pMapManager.buildPath(unit,field.getPosition(),Scout_module.SAFETY_LEVEL,unit.getType().isFlyer(),pGame,Color.Yellow);
                }
            }
        }
    }

    /* ------------------- data structure operation methods ------------------- */


    /* ------------------- other methods ------------------- */

    public void scoutingAreaTEST(MapManager pMapManager, Game pGame) {

        if(scoutingArea.size()>0) {
            System.out.println("Scouting area fields = "+scoutingArea.size());
            PotentialField pf=scoutingArea.getNearestField(this,pGame);
            aStarPathCalculator=pMapManager.buildPath(unit,pf.getPosition(), Scout_module.SAFETY_LEVEL, false, pGame, Color.Blue);
            scoutingArea.remove(pf);
            System.out.println("Scouting area fields = " + scoutingArea.size());
        }

    }

    public void micro(MapManager pMapManager,Game pGame) {
        Block safeEND=null;
        Block safeSTART=null;
        int removeStart=0;
        int removeEND=0;
        boolean safeDistance=true;
        boolean interruptMicro=true;
        int i;

        if(microDestinationBlock==null) {
            if(path.size()>ScoutingUnit.LOCALCHECK_PATH_SIZE+5) {
                for(i=path.size()-1;i>path.size()-ScoutingUnit.LOCALCHECK_PATH_SIZE;i--) {
                    if(path.get(i).isInPotentialField()||pMapManager.getaStarModule().getGridMap().getBlockMap()[path.get(i).getRow()][path.get(i).getColumn()].isInPotentialField()) {
                        if((unit.getType().isFlyer()&&pMapManager.getaStarModule().getGridMap().getBlockMap()[path.get(i).getRow()][path.get(i).getColumn()].isAirDamage())||!unit.getType().isFlyer()&&pMapManager.getaStarModule().getGridMap().getBlockMap()[path.get(i).getRow()][path.get(i).getColumn()].isGroundDamage()) {
                            for(int k=i;k<path.size();k++) {
                                safeDistance=true;
                                for(PotentialField pf:pMapManager.getDangerFields()) {
                                    if(path.get(k).getPosition().getDistance(pf.getPosition())<pf.getRadius()+ScoutingUnit.START_SAFEZONERADIUS) {
                                        safeDistance=false;
                                        break;
                                    }
                                }
                                if(safeDistance) {
                                    safeSTART=path.get(k);
                                    removeStart=k;
                                    safeSTART.setColor(Color.Orange);
                                    break;
                                }
                            }
                            if(!safeDistance){
                                safeSTART=path.get(path.size()-1);
                                removeStart=path.size()-1;
                                safeSTART.setColor(Color.Orange);
                            }


                            for(int u=i;u>0;u--) {
                                safeDistance=true;
                                for(PotentialField pf:pMapManager.getDangerFields()) {
                                    if(path.get(u).getPosition().getDistance(pf.getPosition())<pf.getRadius()+ScoutingUnit.END_SAFEZONERADIUS) {
                                        safeDistance=false;
                                        break;
                                    }
                                }
                                if(safeDistance) {
                                    safeEND=path.get(u);
                                    removeEND=u;
                                    safeEND.setColor(Color.Orange);
                                    break;
                                }
                            }
                            if(!safeDistance) {
                                safeEND=path.get(1);
                                removeEND=1;
                                safeEND.setColor(Color.Orange);
                            }

                            break;
                        }
                    }
                }

                if(safeSTART!=null&&safeEND!=null) {
                    microDestinationBlock=safeEND;
                    microPathCalculator=pMapManager.buildPath(unit,safeSTART.getPosition(),microDestinationBlock.getPosition(),Scout_module.SAFETY_LEVEL,unit.getType().isFlyer(),pGame,Color.Red);
                    removeFromPath(removeEND,removeStart);
                }

            } else if(path.size()>0) {
                for(i=path.size()-1;i>0;i--) {
                    if((unit.getType().isFlyer()&&pMapManager.getaStarModule().getGridMap().getBlockMap()[path.get(i).getRow()][path.get(i).getColumn()].isAirDamage())||!unit.getType().isFlyer()&&pMapManager.getaStarModule().getGridMap().getBlockMap()[path.get(i).getRow()][path.get(i).getColumn()].isGroundDamage()) {
                        if (path.get(i).isInPotentialField() || pMapManager.getaStarModule().getGridMap().getBlockMap()[path.get(i).getRow()][path.get(i).getColumn()].isInPotentialField()) {
                            microDestinationBlock=path.get(0);
                            microPathCalculator=pMapManager.buildPath(unit,unit.getPosition(),microDestinationBlock.getPosition(),Scout_module.SAFETY_LEVEL,unit.getType().isFlyer(),pGame,Color.Red);
                            Block b=path.get(0);
                            path.clear();
                            path.add(b);
                            break;
                        }
                    }
                }
            }
        } else {
            for(PotentialField pf:pMapManager.getDangerFields()) {
                if(unit.getPosition().getDistance(pf.getPosition())<pf.getRadius()+300) {
                    interruptMicro=false;
                }
            }
            if(interruptMicro&&!unit.isUnderAttack()) {
                microDestinationBlock=null;
                microPath=new ArrayList<>();
                aStarPathCalculator=pMapManager.buildPath(unit,path.get(0).getPosition(),Scout_module.SAFETY_LEVEL,unit.getType().isFlyer(),pGame,Color.Purple);
            } else if(microDestinationBlock!=null&&unit.getPosition().getDistance(microDestinationBlock.getPosition())>250) {
                microPathCalculator=pMapManager.buildPath(unit,microDestinationBlock.getPosition(),Scout_module.SAFETY_LEVEL,unit.getType().isFlyer(),pGame,Color.Red);
            } else if(microDestinationBlock!=null&&microDestinationBlock.isInPotentialField()||pMapManager.getaStarModule().getGridMap().getBlockMap()[microDestinationBlock.getRow()][microDestinationBlock.getColumn()].isInPotentialField()) {
                microDestinationBlock=null;
                micro(pMapManager,pGame);
            } else {
                microDestinationBlock=null;
            }
        }
    }

    /**
     * Ak je v lokalnom dosahu nejake nebezpecenstvo, vrati prvu bezpecnu poziciu za nebezpecnou zonou
     * @return
     */
    public void localDangerCheck(MapManager pMapManager, Game pGame) {
        if(pGame.getFrameCount()%Scout_module.UNIT_DANGERCHECK_FRAME_COUNT==0&&unit.exists()) {
            micro(pMapManager, pGame);
        }
    }

    public void removeFromPath(int pStartIndex, int pEndIndex) {
        for(int i=pStartIndex;i<=pEndIndex;i++) {
            path.remove(pStartIndex);
        }
    }

    public boolean hasOrder() {
        return hasOrder;
    }

    public boolean finishedOrder() {
        return finishedOrder;
    }


    /* ------------------- Drawing functions ------------------- */

    public void drawAll(Game pGame) {
        if(unit.exists()) {
            drawPath(pGame);
            drawMicroPath(pGame);
            //drawScoutingArea();
        }
    }

//    public void drawScoutingArea(GraphicsExtended graphicsEx) {
//        if(scoutingArea!=null&&unit.isExists()) {
//            scoutingArea.drawScoutingArea(graphicsEx);
//        }
//    }

    public void drawPath(Game pGame) {
        if(path!=null&&path.size()>0&&unit.exists()) {
            for(Block b:path) {
                pGame.drawCircleMap(b.getPosition(),5,b.getColor());
            }
        }
    }

    public void drawMicroPath(Game pGame) {
        if(microPath!=null&&microPath.size()>0&&unit.exists()) {
            for(Block b : microPath) {
                pGame.drawCircleMap(b.getPosition(),5,b.getColor());
            }
        }
    }


    /* ------------------- Getters and Setters ------------------- */

    public boolean isHasTask() {
        return hasTask;
    }

    public void setHasTask(boolean hasTask) {
        this.hasTask = hasTask;
    }

    public ArrayList<Block> getMicroPath() {
        return microPath;
    }

    public void setMicroPath(ArrayList<Block> microPath) {
        this.microPath = microPath;
    }

    public ScoutingArea getScoutingArea() {
        return scoutingArea;
    }

    public void setScoutingArea(ScoutingArea scoutingArea) {
        this.scoutingArea = scoutingArea;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public ArrayList<Block> getPath() {
        return path;
    }

    public void setPath(ArrayList<Block> path) {
        this.path = path;
    }

    public boolean isHasOrder() {
        return hasOrder;
    }

    public void setHasOrder(boolean hasOrder) {
        this.hasOrder = hasOrder;
    }

    public boolean isFinishedOrder() {
        return finishedOrder;
    }

    public void setFinishedOrder(boolean finishedOrder) {
        this.finishedOrder = finishedOrder;
    }
}
