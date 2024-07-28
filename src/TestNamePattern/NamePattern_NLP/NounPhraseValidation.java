package TestNamePattern.NamePattern_NLP;

import TestNamePattern.ParseNameMethods;

import java.util.List;
import java.util.Map;

public class NounPhraseValidation implements NameValidator {

    @Override
    public NameMatch matches(String testName) {

        if (!testName.startsWith("test")) return null;

        List<Map.Entry<String, String>> coreNLP_results =
                new ParseNameMethods().parseNameWithBasicCoreNLP(testName);

        if (coreNLP_results.size() != 3) return null;

        if (!coreNLP_results.get(2).getValue().startsWith("NN")) return null;

//        System.out.println("NNPhrase - " +
//                coreNLP_results.get(2).getValue() +coreNLP_results.get(2).getKey());

        return new NameMatch() {
            @Override
            public String getPatternName() {
                return "NounPhrase";
            }

            @Override
            public String verifiedActionOfTest() {
                return "N/A";
            }

            @Override
            public String verifiedPredicateOfTest() {
                return "N/A";
            }

            @Override
            public String verifiedScenarioOfTest() {
                return coreNLP_results.get(2).getKey();
            }
        };
    }
}
