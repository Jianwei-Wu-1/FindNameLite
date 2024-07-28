package TestNamePattern.NamePattern_NLP;

import TestNamePattern.ParseNameMethods;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class DividedDuelVerbPhrase_VMNPhrase_Validation implements NameValidator{

    @Override
    public NameMatch matches(String testName) {

        String name = testName.replaceFirst("test", "");

        List<Map.Entry<String, String>> NLP_results =
                new ParseNameMethods().parseNameWithBasicCoreNLP(name);

        if (NLP_results.size() != 5) return null;

        List<String> verbs = new LinkedList<>();

        List<String> nouns = new LinkedList<>();

        for (Map.Entry<String, String> entry : NLP_results) {

            if (!entry.getKey().equals("test") && entry.getValue().contains("VB")) {

                verbs.add(entry.getKey());
            }

            if (entry.getValue().contains("NN")) {

                nouns.add(entry.getKey());
            }
        }

        if (verbs.size() == 0 || nouns.size() == 0) return null;

        if (NLP_results.get(2).getValue().equals(NLP_results.get(3).getValue()) &&
                NLP_results.get(3).getValue().equals(NLP_results.get(4).getValue()) &&
                NLP_results.get(4).getValue().equals("NNP")){

            String half_name = NLP_results.get(3).getKey() + NLP_results.get(4).getKey();

            List<Map.Entry<String, String>> NLP_results_half =
                    new ParseNameMethods().parseNameWithBasicCoreNLP(half_name);

            List<String> verbs_half = new LinkedList<>();

            List<String> nouns_half = new LinkedList<>();

            for (Map.Entry<String, String> entry : NLP_results_half) {

                if (!entry.getKey().equals("test") && entry.getValue().contains("VB")) {

                    verbs_half.add(entry.getKey());
                }

                if (entry.getValue().contains("NN")) {

                    nouns_half.add(entry.getKey());
                }
            }

            if (nouns_half.size() == 0) return null;

            if (verbs_half.size() == 0){

                return new NameMatch() {
                    @Override
                    public String getPatternName() {
                        return "VerbWithMultipleNounsPhrase(VMN)";
                    }

                    @Override
                    public String verifiedActionOfTest() {
                        return verbs.get(0);
                    }

                    @Override
                    public String verifiedPredicateOfTest() {
                        return "N/A";
                    }

                    @Override
                    public String verifiedScenarioOfTest() {
                        return NLP_results.get(2).getKey() +
                                NLP_results.get(3).getKey() +
                                NLP_results.get(4).getKey();
                    }
                };
            };

            return new NameMatch() {
                @Override
                public String getPatternName() {
                    return "DividedDuelVerbPhrase";
                }

                @Override
                public String verifiedActionOfTest() {
                    return verbs.get(0);
                }

                @Override
                public String verifiedPredicateOfTest() {
                    return verbs_half.get(0);
                }

                @Override
                public String verifiedScenarioOfTest() { return nouns.get(0)
                            + "||" +nouns_half.get(0);
                }
            };
        }
        else {
            return null;
        }
    }
}
