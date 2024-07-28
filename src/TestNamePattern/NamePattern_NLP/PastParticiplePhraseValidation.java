package TestNamePattern.NamePattern_NLP;

import TestNamePattern.ParseNameMethods;

import java.util.List;
import java.util.Map;

public class PastParticiplePhraseValidation implements NameValidator {

    @Override
    public NameMatch matches(String testName) {

        if (!testName.startsWith("test")) return null;

        List<Map.Entry<String, String>> coreNLP_results =
                new ParseNameMethods().parseNameWithBasicCoreNLP(testName);

        if (coreNLP_results.size() < 3) return null;

        if (!coreNLP_results.get(1).getValue().equals("VB")) return null;

        if (!coreNLP_results.get(2).getValue().equals("VBN")) return null;
        //Set the "Is & Past Participle Phrase"'s Structure

        return new NameMatch() {
            @Override
            public String getPatternName() {
                return "IsAndPastParticiplePhrase";
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
                return "N/A";
            }
        };
    }
}
