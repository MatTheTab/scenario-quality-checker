package pl.put.poznan.checker.logic;

public interface Visitor {
    void visit(Scenario scenario);
    void visit(Step step);
}
