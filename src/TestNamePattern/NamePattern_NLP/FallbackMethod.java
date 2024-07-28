package TestNamePattern.NamePattern_NLP;

import TestNamePattern.ParseNameMethods;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FallbackMethod implements NameValidator{

    @Override
    public NameMatch matches(String testName) {

        List<Map.Entry<String, String>> coreNLP_results =
                new ParseNameMethods().parseNameWithBasicCoreNLP(testName);

        if (coreNLP_results.size() == 0) return null;

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

        if (verbs.size() == 0 && nouns.size() == 0) return null;

        return new NameMatch() {
            @Override
            public String getPatternName() {
                return "fallback_NLP";
            }

            @Override
            public String verifiedActionOfTest() {

                if (verbs.size() >= 2){
                    return verbs.get(0);
                }
                else if (verbs.size() == 1){
                    return verbs.get(0);
                }
                else {
                    return "N/A";
                }
            }

            @Override
            public String verifiedPredicateOfTest() {

                if (verbs.size() >= 2){
                    return verbs.get(verbs.size() - 1);
                }
                else if (nouns.size() >= 2){
                    return nouns.get(nouns.size() - 1);
                }
                else {
                    return "N/A";
                }
            }

            @Override
            public String verifiedScenarioOfTest() {
                if (nouns.size() >= 1){
                    return nouns.get(0);
                }
                else {
                    return "N/A";
                }
            }
        };
    }
}
