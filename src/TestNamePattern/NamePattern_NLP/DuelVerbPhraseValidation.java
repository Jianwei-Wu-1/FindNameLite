package TestNamePattern.NamePattern_NLP;

import TestNamePattern.ParseNameMethods;

import java.util.List;
import java.util.Map;

public class DuelVerbPhraseValidation implements NameValidator {

    @Override
    public NameMatch matches(String testName) {

        if (!testName.startsWith("test")) return null;

        testName = testName.replaceFirst("test", "");

        List<Map.Entry<String, String>> coreNLP_results =
                new ParseNameMethods().parseNameWithBasicCoreNLP(testName);

        if (coreNLP_results.size() != 4) return null;

        if (!coreNLP_results.get(1).getValue().contains("VB")) return null;

        if (!coreNLP_results.get(2).getValue().contains("VB")) return null;

        return new NameMatch() {
            @Override
            public String getPatternName() {
                return "DuelVerbPhrase";
            }

            @Override
            public String verifiedActionOfTest() {
                return coreNLP_results.get(1).getKey();
            }

            @Override
            public String verifiedPredicateOfTest() {
                return coreNLP_results.get(2).getKey();
            }

            @Override
            public String verifiedScenarioOfTest() {
                return coreNLP_results.get(3).getKey();
            }
        };
    }
}
