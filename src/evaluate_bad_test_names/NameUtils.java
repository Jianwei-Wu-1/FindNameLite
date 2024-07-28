package evaluate_bad_test_names;

import TestNamePattern.NamePattern_NLP.*;
import TestNamePattern.ParseNameMethods;
import TestNamePattern.RegexForTestNamePattern_v1_1Kt;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiMethod;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.DecimalFormat;
import java.util.*;

@SuppressWarnings("WeakerAccess")

class NameUtils {

    public int line_of_code_threshold = 30;

    String nlp_pattern_name;
    String regex_pattern_name;
    String action_NLP;
    String predicate_NLP;
    String scenario_NLP;
    String action_Regex;
    String predicate_Regex;
    String scenario_Regex;
    //Utility Variables

    void writePrintToFile(String prints) throws IOException {
        Path path = Paths.get("/Users/wujianwei/Documents/ideaProjects1" +
                "/FindNameLite/StatisticsForTestNamePattern/NamePatternResult.txt");
        byte[] print = prints.getBytes();
        Files.write(path, print, StandardOpenOption.APPEND);
    }

    void writePrintToFile1(String prints) throws IOException {
        Path path = Paths.get("/Users/wujianwei/Documents/ideaProjects1" +
                "/FindNameLite/StatisticsForTestNamePattern/NewNameResult.txt");
        byte[] print = prints.getBytes();
        Files.write(path, print, StandardOpenOption.APPEND);
    }

    void parseAllTestNames() throws IOException {

        double mismatches = 0;
        double total = 0;

        for (Project project : ProjectManager.getInstance().getOpenProjects()) {

            for (PsiClass testClass : ProjectUtils.testClasses(project)) {

                for (PsiMethod test : PsiClassUtils.testMethods(testClass)){

                    PsiCodeBlock test_body = test.getBody();
                    if (test_body == null) continue;
                    if (test_body.getStatements().length > line_of_code_threshold) continue;

//                    if (!testName.equals("testEntries")) continue;
                    //Note: quick access

                    //Note: Name Patterns Setup ->

                    System.out.println(test.getName() + "-"+ total);

                    List<NameValidator> name_patterns_NLP = Lists.newArrayList(
                            new DividedDuelVerbPhrase_VMNPhrase_Validation(),
                            new VerbPhraseWithPrependingTest_Validation(),
                            new VerbPhraseWithoutPrependingTest_Validation(),
                            new PastParticiplePhraseValidation(),
                            new NounPhraseValidation(),
                            new DuelVerbPhraseValidation()
//                            new FallbackMethod()
                    );
                    List<NameValidator> name_patterns_Regex = Lists.newArrayList(
                            new RegexValidation());
                    //End of Setup

                    Optional<NameValidator.NameMatch> match_NLP = name_patterns_NLP.stream()
                            .map(p -> p.matches(test.getName()))
                            .filter(Objects::nonNull)
                            .findFirst();

                    Optional<NameValidator.NameMatch> match_Regex = name_patterns_Regex.stream()
                            .map(p -> p.matches(test.getName()))
                            .filter(Objects::nonNull)
                            .findFirst();

                    if (match_Regex.isPresent() && match_NLP.isPresent()){

                        List<String> nlp = new LinkedList<>();
                        if (match_NLP.get().verifiedActionOfTest() == null){
                            nlp.add("N/A");
                        }
                        else {
                            nlp.add(match_NLP.get().verifiedActionOfTest());
                        }
                        if (match_NLP.get().verifiedPredicateOfTest() == null){
                            nlp.add("N/A");
                        }
                        else {
                            nlp.add(match_NLP.get().verifiedPredicateOfTest());
                        }
                        if (match_NLP.get().verifiedScenarioOfTest() == null){
                            nlp.add("N/A");
                        }
                        else {
                            nlp.add(match_NLP.get().verifiedScenarioOfTest());
                        }

                        if (match_Regex.get().getPatternName().equals("RegexMatch_SingleEntity")){
                            match_Regex.ifPresent(nameMatch -> {
                                try {
                                    this.writePrintToFile1(
                                            test.getName() + " -> " +
                                                    "Regex Match: " + nameMatch.getPatternName()
                                                    + " - Match Action: " + nameMatch.verifiedActionOfTest()
                                                    + " - Match Predicate: " + nameMatch.verifiedPredicateOfTest()
                                                    + " - Match Scenario: " + nameMatch.verifiedScenarioOfTest()
                                                    + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        else if (nlp.stream().filter(s -> s.contains("N/A")).count() <= 2){
                            match_NLP.ifPresent(nameMatch -> {
                                try {
                                    this.writePrintToFile1(
                                            test.getName() + " -> "
                                                    + "NLP Match: " + nameMatch.getPatternName()
                                                    + " - Match Action: " + nameMatch.verifiedActionOfTest()
                                                    + " - Match Predicate: " + nameMatch.verifiedPredicateOfTest()
                                                    + " - Match Scenario: " + nameMatch.verifiedScenarioOfTest()
                                                    + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                        else{
                            match_Regex.ifPresent(nameMatch -> {
                                try {
                                    this.writePrintToFile1(
                                            test.getName() + " -> " +
                                                    "Regex Match: " + nameMatch.getPatternName()
                                                    + " - Match Action: " + nameMatch.verifiedActionOfTest()
                                                    + " - Match Predicate: " + nameMatch.verifiedPredicateOfTest()
                                                    + " - Match Scenario: " + nameMatch.verifiedScenarioOfTest()
                                                    + "\n");
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                        }
                    }
                    else if (match_NLP.isPresent()) {
                        match_NLP.ifPresent(nameMatch -> {
                            try {
                                this.writePrintToFile1(
                                        test.getName() + " -> "
                                                + "NLP Match: " + nameMatch.getPatternName()
                                                + " - Match Action: " + nameMatch.verifiedActionOfTest()
                                                + " - Match Predicate: " + nameMatch.verifiedPredicateOfTest()
                                                + " - Match Scenario: " + nameMatch.verifiedScenarioOfTest()
                                                + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else if (match_Regex.isPresent()) {
                        match_Regex.ifPresent(nameMatch -> {
                            try {
                                this.writePrintToFile1(
                                        test.getName() + " -> " +
                                                "Regex Match: " + nameMatch.getPatternName()
                                                + " - Match Action: " + nameMatch.verifiedActionOfTest()
                                                + " - Match Predicate: " + nameMatch.verifiedPredicateOfTest()
                                                + " - Match Scenario: " + nameMatch.verifiedScenarioOfTest()
                                                + "\n");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    else {
                        try {
                            this.writePrintToFile1(
                                    test.getName() + " -> " + "No name pattern match" + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (!match_Regex.isPresent() && !match_NLP.isPresent()){
                        mismatches++;
                        total++;
                    }
                    else{
                        total++;
                    }
                }
            }
        }
        this.writePrintToFile1("\n" + "matches: " + (total - mismatches) + " total: " + total + "\n");
        this.writePrintToFile1("Coverage: " + new DecimalFormat("##.##").format(100.0 * ((total - mismatches) / total)) + "%");
    }

    void parseOneTestName(String name) {

//        Map<String, String> regex_map = RegexForTestNamePattern_v1_1Kt.getMatch(name);
//
//        for (String a :
//                regex_map.keySet()) {
//            System.out.println("RegexPair: " + a + " - " + regex_map.get(a));
//        }

        //Name Patterns Setup ->
        List<NameValidator> name_patterns_NLP = Lists.newArrayList(
                new DividedDuelVerbPhrase_VMNPhrase_Validation(),
                new VerbPhraseWithPrependingTest_Validation(),
                new VerbPhraseWithoutPrependingTest_Validation(),
                new PastParticiplePhraseValidation(),
                new NounPhraseValidation(),
                new DuelVerbPhraseValidation()
//                new FallbackMethod()
        );
        List<NameValidator> name_patterns_Regex = Lists.newArrayList(
                new RegexValidation());
        //End of Setup

        Optional<NameValidator.NameMatch> match_NLP = name_patterns_NLP.stream()
                .map(p -> p.matches(name))
                .filter(Objects::nonNull)
                .findFirst();

        Optional<NameValidator.NameMatch> match_Regex = name_patterns_Regex.stream()
                .map(p -> p.matches(name))
                .filter(Objects::nonNull)
                .findFirst();


//        if (match_Regex.isPresent()) {
//            match_Regex.ifPresent(nameMatch -> {
//                try {
//                    this.writePrintToFile(
//                            name + " -> " +
//                                    "Regex Match: " + nameMatch.getPatternName()
//                                    + " - Match Action: " + nameMatch.verifiedActionOfTest()
//                                    + " - Match Predicate: " + nameMatch.verifiedPredicateOfTest()
//                                    + " - Match Scenario: " + nameMatch.verifiedScenarioOfTest()
//                                    + "\n");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//        else if (match_NLP.isPresent()) {
//                match_NLP.ifPresent(nameMatch -> {
//                    try {
//                        this.writePrintToFile(
//                                name + " -> "
//                                        + "NLP Match: " + nameMatch.getPatternName()
//                                        + " - Match Action: " + nameMatch.verifiedActionOfTest()
//                                        + " - Match Predicate: " + nameMatch.verifiedPredicateOfTest()
//                                        + " - Match Scenario: " + nameMatch.verifiedScenarioOfTest()
//                                        + "\n");
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                });
//            }
//        else {
//            try {
//                this.writePrintToFile(
//                        name + " -> " + "No name pattern match" + "\n");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        match_NLP.ifPresent(nameMatch -> nlp_pattern_name = nameMatch.getPatternName());
        match_NLP.ifPresent(nameMatch -> action_NLP = nameMatch.verifiedActionOfTest());
        match_NLP.ifPresent(nameMatch -> predicate_NLP = nameMatch.verifiedPredicateOfTest());
        match_NLP.ifPresent(nameMatch -> scenario_NLP = nameMatch.verifiedScenarioOfTest());
        match_Regex.ifPresent(nameMatch -> regex_pattern_name = nameMatch.getPatternName());
        match_Regex.ifPresent(nameMatch -> action_Regex = nameMatch.verifiedActionOfTest());
        match_Regex.ifPresent(nameMatch -> predicate_Regex = nameMatch.verifiedPredicateOfTest());
        match_Regex.ifPresent(nameMatch -> scenario_Regex = nameMatch.verifiedScenarioOfTest());
        //Setup action/predicate/scenario for NLP match
    }
}
