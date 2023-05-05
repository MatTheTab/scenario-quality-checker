package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.model.Scenario;

import java.util.Map;

@RestController
public class GetScenariosController extends ScenarioController{
    GetScenariosController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenarios" endpoint and returns all scenarios in the storage.
     * Produces application/JSON response
     * @return a map containing all scenarios in the storage
     */
    @GetMapping(value = "/scenarios", produces = "application/JSON")
    public Map<String, Scenario> getScenarios() {
        ScenarioCheckerLogger.logger.info("Getting Scenarios");
        return storage.scenarios;
    }
}
