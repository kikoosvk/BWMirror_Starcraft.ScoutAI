package MapManager;

import bwapi.Position;
import bwapi.PositionedObject;
import bwapi.Unit;

import java.util.Random;

/**
 * Created by Chudjak Kristián on 05.01.2017.
 */
public class Vector2DMath {

    static private Random random = new Random();

    /**
     * Creates a vector with random distribution between (0, 0) and (1, 1)
     *
     * @return
     */
    static public Vector2D randomVector() {
        return randomVector(random);
    }

    /**
     * Creates a vector with random distribution between (0, 0) and (1, 1)
     *
     * @param random provided random sequence
     * @return
     */
    static public Vector2D randomVector(Random random) {
        return new Vector2D(random.nextFloat(), random.nextFloat());
    }

    /**
     * Calculates a dot product of two vectors.
     *
     * @param vectorA
     * @param vectorB
     * @return dot product
     */
    static public float dotProduct(Vector2D vectorA, Vector2D vectorB) {
        return vectorA.x * vectorB.x + vectorA.y * vectorB.y;
    }

    public static Vector2D createVector(Unit from, Unit to) {
        Position fromPosition = from.getPosition();
        Position toPosition = to.getPosition();

        Vector2D fromVector = toVector(fromPosition);
        Vector2D toVector = toVector(toPosition);



        return toVector.sub(fromVector);


    }

    public static Vector2D toVector(Position position) {
        int x = position.getX();
        int y = position.getY();

        return new Vector2D(x, y);
    }


    public static Position toPosition(Vector2D vector) {
        int x = (int) vector.getX();
        int y = (int) vector.getY();

        return new Position(x, y);
    }

    public static Vector2D add(Vector2D leftVector, Vector2D rightVector) {
        float x = leftVector.getX() + rightVector.getX();
        float y = leftVector.getY() + rightVector.getY();

        return new Vector2D(x, y);
    }

    public static Vector2D sub(Vector2D leftVector, Vector2D rightVector) {
        float x = leftVector.getX() - rightVector.getX();
        float y = leftVector.getY() - rightVector.getY();

        return new Vector2D(x, y);
    }

    public static Vector2D scale(Vector2D vector, float scale) {
        return scale(vector, scale, scale);
    }

    public static Vector2D scale(Vector2D vector, float scaleX, float scaleY) {
        float x = vector.getX() * scaleX;
        float y = vector.getY() * scaleY;

        return new Vector2D(x, y);
    }

    public static Vector2D scale(Vector2D vector, Vector2D scalingVector) {
        return scale(vector, scalingVector.getX(), scalingVector.getY());
    }

    public static Vector2D normalize(Vector2D vector) {
        float length = vector.getLength();

        if (length == 0) {
            return vector;
        }

        float x = vector.getX() / length;
        float y = vector.getY() / length;

        return new Vector2D(x, y);
    }

    public static Vector2D invert(Vector2D vector) {
        float x = -vector.getX();
        float y = -vector.getY();

        return new Vector2D(x, y);
    }

    public static Vector2D[] getOrthogonal(Vector2D vector) {
        float x = vector.getX();
        float y = vector.getY();

        return new Vector2D[]{new Vector2D(-y, x), new Vector2D(y, -x)};
    }
}

