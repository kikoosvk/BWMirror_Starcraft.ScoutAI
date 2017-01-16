package MapManager;

import bwapi.*;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;
import bwapi.Color;
import java.util.List;

/**
 * Created by Chudjak Kristián on 05.01.2017.
 */
public class PotentialField {

    private int id;
    private Unit unit;

    private UnitType unitType;

    private double priority;

    private int row;
    private int column;

    /**
     * Center coordinate X
     */
    private int X;

    /**
     * Center coordinate Y
     */
    private int Y;

    /**
     * Radius of the potential field
     */
    private double radius;

    /**
     * Heat value
     */
    private double heat;



    /* ------------------- Constructors ------------------- */

    /**
     * Initialization of PF with given game instance, graphics instance, center X and Y coordiantes and given radius
     *
     * @param game
     * @param X
     * @param Y
     * @param radius
     */
    public PotentialField(Game game, int X, int Y, int radius) {
        this.priority=0;
        this.X=X;
        this.Y=Y;
        this.radius=radius;
        this.heat=0;
        this.id=-1;
        this.row=-1;
        this.column=-1;
    }

    public PotentialField(Game game, Position position, int radius) {
        this.priority=0;
        this.X=position.getX();
        this.Y=position.getY();
        this.radius=radius;
        this.heat=0;
        this.id=-1;
        this.row=-1;
        this.column=-1;
    }

    public PotentialField(Game pGame, Unit pUnit) {
        this.priority=0;
        this.X=pUnit.getPosition().getX();
        this.Y=pUnit.getPosition().getY();
        this.radius=pUnit.getType().sightRange();
        this.heat=0;
        this.id=pUnit.getID();
        this.unit = pUnit;
        this.unitType=pUnit.getType();
        this.row=-1;
        this.column=-1;
        //System.out.println("PF radius = "+radius);
    }

    public PotentialField(Game game, int X, int Y, int radius, double priority) {
        this.priority=priority;
        this.X=X;
        this.Y=Y;
        this.radius=radius;
        this.heat=0;
        this.id=-1;
        this.row=-1;
        this.column=-1;
    }

    public PotentialField(Game game,Position position,int radius, int priority) {
        this.priority=priority;
        this.X=position.getX();
        this.Y=position.getY();
        this.radius=radius;
        this.heat=0;
        this.id=-1;
        this.row=-1;
        this.column=-1;
    }

    public PotentialField(Game game,Position position,int radius, int priority, int pRow, int pColumn) {
        this.priority=priority;
        this.X=position.getX();
        this.Y=position.getY();
        this.radius=radius;
        this.heat=0;
        this.id=-1;
        this.row=pRow;
        this.column=pColumn;
    }


    /* ------------------- Main functonality methods ------------------- */

    /**
     * Returns upper left corner vector with coordinates
     *
     * @return Vector2D
     */
    public Vector2D getLeftUpperCornerBoxVector() {
        return new Vector2D((float)(X-(radius/2)),(float)(Y-(radius/2)));
    }

    /**
     * Returns lower right corner vector with coordinates
     *
     * @return Vector2D
     */
    public Vector2D getRightLowerCornerBoxVector() {
        return new Vector2D((float)(X+(radius/2)),(float)(Y+(radius/2)));
    }

    /**
     * Returns center vector with coordinates
     *
     * @return Vector2D
     */
    public Vector2D getCenterVector() {
        return new Vector2D(X,Y);
    }

    /**
     * If field is visible to allied units
     *
     * @return
     */
    public boolean isVisible(List<Unit> units) {

        for (Unit unit :
                units) {
            if(unit.isVisible()) return true;
        }
        return false;
    }

    public boolean isVisible(Unit unit) {
        if(unit.isVisible()) {
            return true;
        }
        return false;
    }

    public boolean isUnitInRange(Unit pUnit) {
        Position pos=new Position(X,Y);
        if(pos.getDistance(pUnit.getPosition())<=radius&&pUnit.getID()==id) {
            return true;
        }
        return false;
    }

    public boolean isPositionInRange(Position pPosition) {
        Position pos=new Position(X,Y);
        if(pos.getDistance(pPosition)<=radius) {
            return true;
        }
        return false;
    }

    public double getRangeLengthInPercent(Position pPosition) {
        double distance=pPosition.getDistance(new Position(X,Y));
        double percent=100-(100/radius)*distance;
        return percent/100;
    }


    /* ------------------- Real-time management methods ------------------- */

    /**
     * Increases heat by +0,01
     */
    public void increaseHeat() {
        this.heat+=1/(double)100;
    }


    /* ------------------- Getters and Setters ------------------- */

    public Position getPosition() {
        return new Position(X,Y);
    }
    public boolean isVisible(Game game){
        TilePosition upLeft = new TilePosition((int)(row*4),(int)(column*4));
        TilePosition upRight = new TilePosition((int)(row*4),(int)(column*4)+3);
        TilePosition downLeft = new TilePosition((int)(row*4)+3,(int)(column*4));
        TilePosition downRight = new TilePosition((int)(row*4)+3,(int)(column*4)+3);

        return game.isVisible(upLeft) || game.isVisible(upRight) || game.isVisible(downLeft) || game.isVisible(downRight);

    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public UnitType getUnitType() {
        return unitType;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getPriority() {
        return priority;
    }

    public void setPriority(double priority) {
        this.priority = priority;
    }

    public void setHeat(int heat) {
        this.heat=heat/(double)100;
    }

    public double getHeat() {
        return heat;
    }

    public int getX() {
        return X;
    }

    public void setX(int x) {
        X = x;
    }

    public int getY() {
        return Y;
    }

    public void setY(int y) {
        Y = y;
    }

    public double getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public void showGraphicsRectangular(Game pGame, Color color) {
        pGame.drawBoxMap((int) (X - (radius )),
                (int) (Y - (radius)),
                (int) (X + (radius )),
                (int) (Y + (radius )), color);
    }

    public void showGraphicsCircular(Game pGame, Color orange) {
        pGame.drawCircleMap(X,Y,(int)radius,orange);
    }

    public Unit getUnit() {
        return unit;
    }

    public String getXANDY(){
        return "{"+X+','+Y+"}";
    }
}
