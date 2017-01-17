package MODQlearning;

import ScoutModule.Scout_module;
import UnitManagement.ScoutingUnit;
import bwapi.*;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Silent1 on 17.01.2017.
 */
public class QExecutor {

    public static boolean DEBUG=true;
    public static boolean EXECUTE=false;

    private Scout_module scout_module;
    private Game game;

    private Position safePosition_1;
    private Position safePosition_2;
    private Position safePosition_3;
    private Position safePosition_4;
    private Position safePosition_5;
    private Position safePosition_6;
    private Position safePosition_7;
    private Position safePosition_8;
    private Position safePosition_9;
    private Position safePosition_10;
    private Position safePosition_11;
    private Position safePosition_12;

    private ScoutingUnit scUnit_1;
    private ScoutingUnit scUnit_2;
    private ScoutingUnit scUnit_3;
    private ScoutingUnit scUnit_4;
    private ScoutingUnit scUnit_5;
    private ScoutingUnit scUnit_6;
    private ScoutingUnit scUnit_7;
    private ScoutingUnit scUnit_8;
    private ScoutingUnit scUnit_9;
    private ScoutingUnit scUnit_10;
    private ScoutingUnit scUnit_11;
    private ScoutingUnit scUnit_12;

    private List<Position> safePositions;
    private List<ScoutingUnit> scoutingUnits;

    private int execFrameCount=0;

    public QExecutor(Scout_module pScout_module) {
        scout_module=pScout_module;
        game=pScout_module.getGame();
        safePositions=new LinkedList<>();
        scoutingUnits=new LinkedList<>();
    }

    public void onFrame() {
        if(!QExecutor.EXECUTE) {
            execFrameCount=game.getFrameCount();
        }
//        if(game.getFrameCount()==200) {
//            if(QExecutor.DEBUG) {
//                System.out.println("Executing scouting...");
//            }
//            QExecutor.EXECUTE=true;
//        }
        executeAll();
    }

    public void executeAll() {
        if(QExecutor.EXECUTE) {
            if(game.getFrameCount()==execFrameCount+30) {
                execute_1();
            } else if(game.getFrameCount()==execFrameCount+60) {
                execute_2();
            } else if(game.getFrameCount()==execFrameCount+90) {
                execute_3();
            } else if(game.getFrameCount()==execFrameCount+120) {
                execute_4();
            } else if(game.getFrameCount()==execFrameCount+150) {
                execute_5();
            } else if(game.getFrameCount()==execFrameCount+180) {
                execute_6();
            } else if(game.getFrameCount()==execFrameCount+210) {
                execute_7();
            } else if(game.getFrameCount()==execFrameCount+240) {
                execute_8();
            } else if(game.getFrameCount()==execFrameCount+270) {
                execute_9();
            } else if(game.getFrameCount()==execFrameCount+300) {
                execute_10();
            } else if(game.getFrameCount()==execFrameCount+330) {
                execute_11();
            } else if(game.getFrameCount()==execFrameCount+360) {
                execute_12();
            }

        }
    }

    public void initializeAll() {
        initializeSafePositions();
        initializeScoutingUnits();
    }

    public void initializeScoutingUnits() {
        for(Unit u:game.getAllUnits()) {
            if(u.getPlayer()==game.self()) {
                if(QExecutor.DEBUG) {
                    System.out.println("ID = " + u.getID());
                }

                if(u.getPosition().getDistance(258,1054)<100) {
                    scUnit_1=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_1);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_1);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 1 initialized.");
                    }
                } else if(u.getPosition().getDistance(3204,1064)<100) {
                    scUnit_2=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_2);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_2);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 2 initialized.");
                    }
                } else if(u.getPosition().getDistance(5961,1057)<100) {
                    scUnit_3=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_3);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_3);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 3 initialized.");
                    }
                } else if(u.getPosition().getDistance(319,2288)<100) {
                    scUnit_4=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_4);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_4);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 4 initialized.");
                    }
                } else if(u.getPosition().getDistance(3113,2329)<100) {
                    scUnit_5=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_5);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_5);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 5 initialized.");
                    }
                } else if(u.getPosition().getDistance(5842,2286)<100) {
                    scUnit_6=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_6);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_6);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 6 initialized.");
                    }
                } else if(u.getPosition().getDistance(232,3566)<100) {
                    scUnit_7=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_7);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_7);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 7 initialized.");
                    }
                } else if(u.getPosition().getDistance(2681,3556)<100) {
                    scUnit_8=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_8);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_8);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 8 initialized.");
                    }
                } else if(u.getPosition().getDistance(5676,3556)<100) {
                    scUnit_9=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_9);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_9);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 9 initialized.");
                    }
                } else if(u.getPosition().getDistance(163,4748)<100) {
                    scUnit_10=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_10);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_10);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 10 initialized.");
                    }
                } else if(u.getPosition().getDistance(2883,4765)<100) {
                    scUnit_11=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_11);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_11);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 11 initialized.");
                    }
                } else if(u.getPosition().getDistance(5754,4745)<100) {
                    scUnit_12=new ScoutingUnit(u);
                    scoutingUnits.add(scUnit_12);
                    scout_module.getUnitManager().addScoutingUnit(scUnit_12);
                    if(QExecutor.DEBUG) {
                        System.out.println("ScoutingUnit 12 initialized.");
                    }
                }
            }
        }
        if(QExecutor.DEBUG) {
            System.out.println("Scouting units count = "+scoutingUnits.size());
        }
    }

    public void initializeSafePositions() {
        for(Unit u:game.getAllUnits()) {
            if(u.getPlayer()==game.self()) {
                if(u.getType()==UnitType.Terran_Bunker) {
                    if(u.getPosition().getDistance(1648,1056)<100) {
                        safePosition_1=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 1 initialized.");
                        }
                    } else if(u.getPosition().getDistance(4656,1056)<100) {
                        safePosition_2=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 2 initialized.");
                        }
                    } else if(u.getPosition().getDistance(7632,1088)<100) {
                        safePosition_3=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 3 initialized.");
                        }
                    } else if(u.getPosition().getDistance(1936,2304)<100) {
                        safePosition_4=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 4 initialized.");
                        }
                    } else if(u.getPosition().getDistance(4848,2304)<100) {
                        safePosition_5=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 5 initialized.");
                        }
                    } else if(u.getPosition().getDistance(8016,2304)<100) {
                        safePosition_6=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 6 initialized.");
                        }
                    } else if(u.getPosition().getDistance(1712,3584)<100) {
                        safePosition_7=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 7 initialized.");
                        }
                    } else if(u.getPosition().getDistance(4752,3520)<100) {
                        safePosition_8=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 8 initialized.");
                        }
                    } else if(u.getPosition().getDistance(7920,3584)<100) {
                        safePosition_9=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 9 initialized.");
                        }
                    } else if(u.getPosition().getDistance(1808,4800)<100) {
                        safePosition_10=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 10 initialized.");
                        }
                    } else if(u.getPosition().getDistance(4720,4800)<100) {
                        safePosition_11=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 11 initialized.");
                        }
                    } else if(u.getPosition().getDistance(7696,4768)<100) {
                        safePosition_12=u.getPosition();
                        safePositions.add(u.getPosition());
                        if(QExecutor.DEBUG) {
                            System.out.println("SafePosition 12 initialized.");
                        }
                    }
                }
            }
        }
        if(QExecutor.DEBUG) {
            System.out.println("Safepositions = "+safePositions.size());
        }
    }

    public void execute_1() {
        scout_module.getActionManager().scoutPosition(safePosition_1,scUnit_1,2);
    }

    public void execute_2() {
        scout_module.getActionManager().scoutPosition(safePosition_2,scUnit_2,2);
    }

    public void execute_3() {
        scout_module.getActionManager().scoutPosition(safePosition_3,scUnit_3,2);
    }

    public void execute_4() {
        scout_module.getActionManager().scoutPosition(safePosition_4,scUnit_4,2);
    }

    public void execute_5() {
        scout_module.getActionManager().scoutPosition(safePosition_5,scUnit_5,2);
    }

    public void execute_6() {
        scout_module.getActionManager().scoutPosition(safePosition_6,scUnit_6,2);
    }

    public void execute_7() {
        scout_module.getActionManager().scoutPosition(safePosition_7,scUnit_7,2);
    }

    public void execute_8() {
        scout_module.getActionManager().scoutPosition(safePosition_8,scUnit_8,2);
    }

    public void execute_9() {
        scout_module.getActionManager().scoutPosition(safePosition_9,scUnit_9,2);
    }

    public void execute_10() {
        scout_module.getActionManager().scoutPosition(safePosition_10,scUnit_10,2);
    }

    public void execute_11() {
        scout_module.getActionManager().scoutPosition(safePosition_11,scUnit_11,2);
    }

    public void execute_12() {
        scout_module.getActionManager().scoutPosition(safePosition_12,scUnit_12,2);
    }

    public void drawAll() {
        drawSafePositions();
        drawSelectedIDs();
    }

    public void drawSelectedIDs() {
        for(Unit u: game.getAllUnits()) {
            if(u.isSelected()) {
                game.drawTextMap(u.getPosition(),""+u.getID());
                game.drawTextMap(u.getX(),u.getY()+50,""+u.getPosition().toString());
            }
        }
    }

    public void drawSafePositions() {
        for(Position pos:safePositions) {
            game.drawCircleMap(pos,130, Color.White);
        }
    }

    public void drawScoutingUnits() {
        for(ScoutingUnit scu:scoutingUnits) {
            game.drawBoxMap(scu.getUnit().getPosition().getX()-20,scu.getUnit().getPosition().getY()-20,scu.getUnit().getPosition().getX()+20,scu.getUnit().getPosition().getY()+20,Color.Blue);
        }
    }
}
