package pl.put.poznan.checker.rest;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.JSONParser;
import pl.put.poznan.checker.logic.ScenarioKeyWordCountVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * REST controller for handling GET requests to perform keyword count on a Scenario.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetRequestScenarioKeywordCountController extends ScenarioController{

    /**
     * Constructor for GetRequestScenarioKeywordCountController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetRequestScenarioKeywordCountController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenario/keywordcount" endpoint
     * and returns the keyword count for the scenario provided in the request body.
     * Produces application/JSON response.
     * @param scenarioContent a JSON representation of the scenario as a request body
     * @return the keyword count for the provided scenario
     */
    @GetMapping(value="/scenario/keywordcount", produces = "application/JSON")
    public String getRequestScenarioKeywordCount(@RequestBody String scenarioContent) {
        Scenario newScenario = null;

        try {
            JSONObject scenarioObj = new JSONObject(scenarioContent);
            newScenario = JSONParser.parseScenarioObject(scenarioObj);
            ScenarioCheckerLogger.logger.info("Received scenario with title: {} actors: {} systemActor: {}",
                    newScenario.getTitle(), Arrays.toString(newScenario.getActors()),
                    newScenario.getSystemActor());
            super.logger.logSteps(newScenario.getSteps());
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        ScenarioKeyWordCountVisitor visitor = new ScenarioKeyWordCountVisitor();
        newScenario.accept(visitor);
        ScenarioCheckerLogger.logger.info("Returning keyword count {}",visitor.getKeyWordCount());
        return "{\"Keyword count\": " + visitor.getKeyWordCount() + "}";
    }
}
