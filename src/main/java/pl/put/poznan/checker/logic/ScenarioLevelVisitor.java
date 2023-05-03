package pl.put.poznan.checker.logic;

import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

import java.util.ArrayList;

/**
 * Class Responsible for creating and returning a Scenario with the depth limited to that
 * specified by the user. Meaning that all sub-scenarios will be truncated if they appear beyond
 * a certain depth threshold. The class contains field referring to maximum level tolerated
 * when truncating scenarios. It also contains fields responsible for holding the currently
 * saved Scenario and an ArrayList of steps relating to the saved Scenario. The class
 * implements a Visitor interface.
 *
 * <p>Example usage of the class to create a truncated scenario steps
 * for some previously created Scenario class instance called my_scenario:
 * <pre>
 * {@code
 * ScenarioLevelVisitor visitor = new ScenarioLevelVisitor();
 * visitor.setMaxLevel(2);
 * my_scenario.accept(visitor);
 * Scenario result = visitor.getScenario;
 *  }
 * </pre>
 */
public class ScenarioLevelVisitor implements Visitor{
    private int maxLevel=1;
    private Scenario scenario;
    private ArrayList<Step> steps;

    /**
     * Constructor allows to pass the expected maximum depth level
     * for the truncated Scenario.
     * @param maxLevel Integer specifying the expected depth, for maxLevel=1, only the
     *                 main scenario will remain
     */
    public ScenarioLevelVisitor(int maxLevel){
        this.maxLevel = maxLevel;
    }

    /**
     * Parameterless constructor for class
     */
    public ScenarioLevelVisitor(){
    }

    /**
     * Method allowing for manually setting the maximum expected depth for
     * the truncated scenario.
     * @param maxLevel Integer specifying the expected depth, for maxLevel=1, only the
     *                 main scenario will remain
     */
    public void setMaxLevel(int maxLevel){
        this.maxLevel=maxLevel;
    }

    /**
     * Method responsible for visiting a Scenario class instance,
     * assigns to the private field scenario the values
     * of the title, actors and systemActors of the given visited scenario.
     * @param scenario Scenario to be visited
     */
    public void visit(Scenario scenario){
        this.scenario = new Scenario(scenario.getTitle(), scenario.getActors(),
                scenario.getSystemActor(), null);
        this.steps = new ArrayList<Step>();
    }

    /**
     * Method responsible for visiting the Step class instance,
     * checks the current depth, compares it against specified maximum allowed depth
     * and decides if it should assign the given step and its substeps based on that information.
     * @param step Step to be visited
     */
    public void visit(Step step){
        int currentLevel=step.getStepLevel().split("\\.").length;
        if(currentLevel<=this.maxLevel){
            if(currentLevel+1>this.maxLevel){
                step.setSubsteps(null);
            }
            if(currentLevel==1) {
                this.steps.add(step);
            }
        }
    }

    /**
     * Returns the new truncated Scenario after assigning to it the new, truncated steps
     * @return scenario - containing the new truncated scenario
     */
    public Scenario getScenario(){
        scenario.setSteps(this.steps);
        return scenario;
    }
}
