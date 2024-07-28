package TestNamePattern.NamePattern_NLP;

import TestNamePattern.ParseNameMethods;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class VerbPhraseWithoutPrependingTest_Validation implements NameValidator{

    private String action;

    private String predicate;

    private String scenario;

    @Override
    public NameMatch matches(String testName) {

        if (testName.startsWith("test")) return null;

        testName = testName.replaceFirst("test", "");
        //Remove "test" to generate usable names

        List<Map.Entry<String, String>> coreNLP_results =
                new ParseNameMethods().parseNameWithBasicCoreNLP(testName);

        if (coreNLP_results.size() < 3) return null;

        if (!coreNLP_results.get(1).getValue().contains("VB")
                && !coreNLP_results.get(2).getValue().contains("NN")) return null;

        //Defined Structure : test + "Verb*" + "Noun*" + ...

        List<String> verbs = new LinkedList<>();

        List<String> nouns = new LinkedList<>();

        for (Map.Entry<String, String> entry : coreNLP_results) {

            if (!entry.getKey().equals("test") && entry.getValue().contains("VB")) {

                verbs.add(entry.getKey());
            }

            if (entry.getValue().contains("NN")) {

                nouns.add(entry.getKey());
            }
        }

        if (verbs.size() == 1 && nouns.size() != 0) {

            action = verbs.get(0);

            scenario = nouns.get(0);

            predicate = "N/A";
        }
        else if (verbs.size() >= 2 && nouns.size() != 0) {

            action = verbs.get(0);

            scenario = nouns.get(0);

            predicate = verbs.get(1);
        }


        return new NameMatch() {
            @Override
            public String getPatternName() {
                return "VerbPhraseWithoutPre_test";
            }

            @Override
            public String verifiedActionOfTest() {
                return action;
            }

            @Override
            public String verifiedPredicateOfTest() {
                return predicate;
            }

            @Override
            public String verifiedScenarioOfTest() {
                return scenario;
            }
        };
    }
}
