package pl.put.poznan.checker.logic;

//TODO: Review and test implementation
//TODO: Integrate with REST API
public class ScenarioCountVisitor implements Visitor{
    private int stepCount=0;

    public void visit(Scenario scenario) {
    }

    public void visit(Step step) {
        stepCount++;
    }

    public int getStepCount() {
        return stepCount;
    }

}
