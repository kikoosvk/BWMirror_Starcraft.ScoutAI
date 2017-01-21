package MODQlearning;

import java.util.HashMap;

/**
 * Created by Peter on 7. 1. 2017.
 */
public class Qlearning {


    private final double alpha = 0.1; // learning rate  0 - no learning
    private final double gamma = 0.9; // discount factor (importance of future rewards) 0 - only-short sighted

    private State states[];
    private Action actions[];

    private double[][] qMatrix;
    private double[][] rewardMatrix;

    private HashMap<State, Integer> stateIndices = new HashMap<>();
    private HashMap<Action, Integer> actionIndices = new HashMap<>();

    public Qlearning(State[] states, Action[] actions, double[][] qMatrix)
    {
        this.states = states;
        this.actions = actions;




        if (qMatrix != null && qMatrix.length == states.length && qMatrix[0].length == actions.length) {
            this.qMatrix = qMatrix;
        } else {
            buildMatrix();
        }

        buildIndices();
    }


    public void buildIndices() {

        for (int i = 0; i < states.length; i++) {
            stateIndices.put(states[i], i);
        }

        for (int i = 0; i < actions.length; i++) {
            actionIndices.put(actions[i], i);
        }
    }

    public void buildMatrix() {
        qMatrix = new double[states.length][actions.length];
    }



    // TODO Optimize, maybe not needed every time, use some kind of caching and invalidation for states.
    public double maxQ(int stateIndex) {
        double maxValue = Double.MIN_VALUE;

        for (int actionIndex = 0; actionIndex < actions.length; actionIndex++) {
            maxValue = Math.max(qMatrix[stateIndex][actionIndex], maxValue);
        }
        return maxValue;
    }


    public void experience(State currentState, Action action, State nextState) {

        int currentStateIndex = stateIndices.get(currentState);
        int nextStateIndex = stateIndices.get(nextState);
        int actionIndex = actionIndices.get(action);

        // Using this possible action, consider to go to the next state
        double q = qMatrix[currentStateIndex][actionIndex];
        double r = rewardMatrix[currentState.getIndex()][action.getIndex()];

        double maxQ = maxQ(nextStateIndex);

        double value = q + alpha * (r + gamma * maxQ - q);

        qMatrix[currentStateIndex][actionIndex] = value;
    }

}
