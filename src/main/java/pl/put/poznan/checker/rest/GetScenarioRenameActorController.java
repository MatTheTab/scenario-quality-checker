package pl.put.poznan.checker.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import pl.put.poznan.checker.logic.ScenarioActorRenameVisitor;
import pl.put.poznan.checker.model.Scenario;

import java.util.Arrays;

/**
 * REST controller responsible for renaming an actor in a Scenario.
 * Implemented in accordance with REST framework.
 */
@RestController
public class GetScenarioRenameActorController extends ScenarioController {

    /**
     * Constructor for GetScenarioRenameActorController with the given storage and logger.
     *
     * @param storage (ScenarioStorage) the storage to use for scenarios
     * @param logger (ScenarioCheckerLogger) the logger to use for logging
     */
    GetScenarioRenameActorController(ScenarioStorage storage, ScenarioCheckerLogger logger) {
        super(storage, logger);
    }

    /**
     * Handles GET requests to the "/scenarios/{title}/renameactor/{oldactor}/{newactor}" endpoint
     * and renames the given actor in the stored scenario with the given title.
     * The updated scenario with the actor renamed is returned inside the response body as a JSON object.
     *
     * @param title (String) the title of the scenario to rename the actor in, as a path variable
     * @param oldactor (String) the name of the actor to rename, as a path variable
     * @param newactor (String) the new name for the actor, as a path variable
     * @return (Scenario) the updated scenario with the actor renamed
     */
    @GetMapping(value="/scenarios/{title}/renameactor/{oldactor}/{newactor}", produces = "application/JSON")
    public Scenario getScenariosRenameActor(@PathVariable("title")String title, @PathVariable("oldactor")String oldactor, @PathVariable("newactor")String newactor) {

        Scenario scenario = storage.scenarios.get(title);

        ScenarioActorRenameVisitor actorRenameVisitor = new ScenarioActorRenameVisitor(oldactor, newactor);
        ScenarioCheckerLogger.logger.info("Renaming the specified Actor");
        scenario.accept(actorRenameVisitor);
        ScenarioCheckerLogger.logger.debug("Returning scenario with title: {} actors: {} systemActor: {}",
                scenario.getTitle(), Arrays.toString(scenario.getActors()),
                scenario.getSystemActor());
        super.logger.logSteps(scenario.getSteps());
        return actorRenameVisitor.getScenario();
    }
}
