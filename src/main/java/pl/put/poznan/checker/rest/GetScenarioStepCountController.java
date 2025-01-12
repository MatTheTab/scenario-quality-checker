package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.ScenarioStepCountVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * REST controller responsible for performing the step-count operation on a Scenario.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetScenarioStepCountController extends ScenarioController{

    /**
     * Constructor for GetScenarioStepCountController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetScenarioStepCountController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "scenarios/{title}/stepcount" endpoint
     * and returns the step count using visitor for the stored scenario with the given title.
     * Produces application/JSON response
     * @param title the title of the scenario as a path variable
     * @return the step count for the scenario with the given title
     */
    @GetMapping(value = "scenarios/{title}/stepcount", produces = "application/JSON")
    public String getScenarioStepCount(@PathVariable("title")String title) {
        ScenarioCheckerLogger.logger.debug("Getting step count for scenario with title: {}", title);
        Scenario scenario = storage.scenarios.get(title);
        ScenarioCheckerLogger.logger.debug("Received scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        ScenarioStepCountVisitor visitor = new ScenarioStepCountVisitor();
        scenario.accept(visitor);
        Integer stepCount = visitor.getStepCount();
        super.logger.logger.info("Step count for scenario with title {}: {}", title, stepCount);
        return "{\"Step count\": " + stepCount + "}";
    }
}
