package pl.put.poznan.checker.logic;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import pl.put.poznan.checker.model.Scenario;
import pl.put.poznan.checker.model.Step;

/**
 * The JSONParser class is responsible for reading and parsing a specified JSON file
 * in order for it to be used as an instance of Scenario class object.
 */
public class JSONParser {

    /**
     * Method responsible for parsing a provided JSONObject as means
     * of creating an instance of Scenario Class based on provided JSONObject.
     * In order to parse steps the method uses parseScenarioSteps() method of
     * the JSONParser class.
     *
     * @param obj JSONObject to be changed into a Scenario
     * @return Scenario created from the JSONObject
     * @throws JSONException
     */
    public static Scenario parseScenarioObject(JSONObject obj) throws JSONException {
        String title = obj.getString("title");
        JSONArray actorsArray = obj.getJSONArray("actors");
        String systemActor = obj.getString("systemActor");
        JSONArray scenarioSteps = obj.getJSONArray("steps");

        String[] actors = new String[actorsArray.length()];
        for (int i = 0; i < actorsArray.length(); i++) {
            actors[i] = actorsArray.getString(i);
        }
        ArrayList<Step> steps = parseScenarioSteps(scenarioSteps, "1", 0);

        return new Scenario(title, actors, systemActor, steps);
    }

    /**
     * Method responsible for parsing steps into an ArrayList of Steps.
     *
     * @param steps     JSONArray of steps to be parsed
     * @param stepLevel String representing the stepLevel given in the form like: "2.5.3" in this example
     *                  representing depth of 3, 3rd substep of a 5th step,
     *                  which is the substep of a 2nd step
     * @param level     Integer representing level of the step
     * @return ArrayList of steps created based on JSONArray
     */
    public static ArrayList<Step> parseScenarioSteps(JSONArray steps, String stepLevel, int level) {
        ArrayList<Step> newSteps = new ArrayList<Step>();

        for (int i = 0; i < steps.length(); i++) {
            Object stepObject = null;
            String actor = null;
            String keyword = null;
            String action = null;
            ArrayList<Step> substeps = null;

            try {
                stepObject = steps.get(i);
                if (stepObject instanceof JSONObject) {
                    JSONObject stepObj = (JSONObject) stepObject;
                    String key = stepObj.keys().next().toString();

                    // if one of keyword statements
                    if (key.equals("IF") || key.equals("ELSE") || key.equals("FOR EACH")) {
                        try {
                            // try to get the inside object (actor:action)
                            JSONObject conditionObj = stepObj.getJSONObject(key);
                            keyword = key;
                            actor = conditionObj.keys().next().toString();
                            action = conditionObj.getString(actor);
                        } catch (JSONException e2) {
                            // if no nested object
                            keyword = key;
                            action = stepObj.getString(key);
                        }
                    }
                    // if is an actor action
                    else {
                        actor = key;
                        action = stepObj.getString(key);
                    }
                }
                // is substep array
                else if (stepObject instanceof JSONArray) {
                    continue;
                }
                // is a lone action
                else {
                    action = stepObject.toString();
                }

                // if next object is an array of substeps
                if (i < steps.length() - 1 && steps.get(i + 1) instanceof JSONArray) {
                    substeps = parseScenarioSteps((JSONArray) steps.get(i + 1), stepLevel + ".1", level + 1);
                }
            } catch (JSONException e1) {
                throw new RuntimeException(e1);
            }

            newSteps.add(new Step(actor, keyword, action, substeps, stepLevel));
            String[] splitStepLevel = stepLevel.split("\\.");

            if (splitStepLevel.length > 0) {
                String valueToIncrement = splitStepLevel[level];
                Integer newValue = Integer.parseInt(valueToIncrement);
                newValue += 1;
                splitStepLevel[level] = Integer.toString(newValue);
                stepLevel = String.join(".", splitStepLevel);
            } else {
                Integer newValue = Integer.parseInt(stepLevel);
                newValue += 1;
                stepLevel = Integer.toString(newValue);
            }
        }
        return newSteps;
    }
}