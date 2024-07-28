package evaluate_bad_test_names;

import ExtrasForTestPattern.PercentageCalculator_v3;
import FocalElementDetector.FocalMethodDetector_Simple;
import TestBodyPattern.*;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.*;
import com.intellij.psi.controlFlow.AnalysisCanceledException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class PatternUtils {

    public int line_of_code_threshold = 30;
    public List<String> test_names = new LinkedList<>();
    public List<String> test_classes = new LinkedList<>();
    public List<String> test_body_action = new LinkedList<>();
    public List<String> test_body_predicate = new LinkedList<>();
    public List<String> test_body_scenario = new LinkedList<>();
    public List<String> test_name_action = new LinkedList<>();
    public List<String> test_name_predicate = new LinkedList<>();
    public List<String> test_name_scenario = new LinkedList<>();

    private double name_counts =  0.0;
    private double name_mismatches = 0.0;
    //Default 50 lines of code

    //Start: Add new patterns below

    private static final List<PatternFinder> patterns = Lists.newArrayList(
            new AllAssertionsPatternFinder_NMC(),
            new AllAssertionsPatternFinder_1MC(),
            new IfElsePatternFinder(),
            new LoopPatternFinder(),
            new NoAssertionPatternFinder(),
            new TryCatchPatternFinder(),
            new NormalPatternFinder_2_3LOC(),
            new NormalPatternFinder_AnyLOC(),
            new NoAssertionPatternFinder_AnyLOC(),
            new TryCatchPatternFinder_AnyLOC_EA(),
            new SingleLinePattern_withNewObject_Finder(),
            new AllDecs_AllMCs_PatternFinder(),
            new OneMethodInvocationOnly_PatternFinder(),
            new FocalMethodFinder()
            //Place "OneMethodInvocationOnly_Pattern" and "FocalMethodFinder" here
            //Order matters
    );

    public void searchForPatterns() throws AnalysisCanceledException, IOException {

        new NameUtils().parseAllTestNames();
        //Note: Name first

        //Start: Record all results to separate files

        File AllAssertionPattern_Single =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/AllAssertionPattern_Single.txt");

        FileWriter fileWriter_AllAssertionPattern_1MC =
                new FileWriter(AllAssertionPattern_Single, false);

        BufferedWriter bufferedWriter_AllAssertionPattern_Single =
                new BufferedWriter(fileWriter_AllAssertionPattern_1MC);

        // AllAssertionPattern_Single

        File AllAssertionPattern_Multiple =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/AllAssertionPattern_Multiple.txt");

        FileWriter fileWriter_AllAssertionPattern =
                new FileWriter(AllAssertionPattern_Multiple, false);

        BufferedWriter bufferedWriter_AllAssertionPattern_Multiple =
                new BufferedWriter(fileWriter_AllAssertionPattern);

        // AllAssertionPattern_Multiple

        File IfElsePattern_NoPredicate =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/IfElsePattern_NoPredicate.txt");

        FileWriter fileWriter_IfElsePattern_NoPredicate =
                new FileWriter(IfElsePattern_NoPredicate, false);

        BufferedWriter bufferedWriter_IfElsePattern_NoPredicate =
                new BufferedWriter(fileWriter_IfElsePattern_NoPredicate);

        // IfElsePattern_NoPredicate

        File IfElsePattern_v3 =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/IfElsePattern_v3.txt");

        FileWriter fileWriter_IfElsePattern_v3 =
                new FileWriter(IfElsePattern_v3, false);

        BufferedWriter bufferedWriter_IfElsePattern_v3 =
                new BufferedWriter(fileWriter_IfElsePattern_v3);

        // IfElsePattern_v3

        File LoopPattern_v3 =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/LoopPattern.txt");

        FileWriter fileWriter_LoopPattern_v3 =
                new FileWriter(LoopPattern_v3, false);

        BufferedWriter bufferedWriter_LoopPattern_v3 =
                new BufferedWriter(fileWriter_LoopPattern_v3);

        // LoopPattern_v3

        File NoAssertionPattern_DeclarationOnly_Single =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/NoAssertionPattern_DeclarationOnly_Single.txt");

        FileWriter fileWriter_SingleLinePattern_ScenarioOnly =
                new FileWriter(NoAssertionPattern_DeclarationOnly_Single, false);

        BufferedWriter bufferedWriter_NoAssertionPattern_DeclarationOnly_Single =
                new BufferedWriter(fileWriter_SingleLinePattern_ScenarioOnly);

        // NoAssertionPattern_DeclarationOnly_Single

        File NoAssertionPattern_ActionOnly_Single =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/NoAssertionPattern_ActionOnly_Single.txt");

        FileWriter fileWriter_SingleLinePattern_ActionOnly =
                new FileWriter(NoAssertionPattern_ActionOnly_Single, false);

        BufferedWriter bufferedWriter__NoAssertionPattern_ActionOnly_Single =
                new BufferedWriter(fileWriter_SingleLinePattern_ActionOnly);

        // NoAssertionPattern_ActionOnly_Single

        File NoAssertionPattern_NewObject_Single =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/NoAssertionPattern_NewObject_Single.txt");

        FileWriter fileWriter_NewObjectPattern1LOC =
                new FileWriter(NoAssertionPattern_NewObject_Single, false);

        BufferedWriter bufferedWriter_NoAssertionPattern_NewObject_Single=
                new BufferedWriter(fileWriter_NewObjectPattern1LOC);

        // NoAssertionPattern_NewObject_Single

        File NoAssertionPattern =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/NoAssertionPattern.txt");

        FileWriter fileWriter_NoAssertionPattern =
                new FileWriter(NoAssertionPattern, false);

        BufferedWriter bufferedWriter_NoAssertionPattern =
                new BufferedWriter(fileWriter_NoAssertionPattern);

        // NoAssertionPattern

        File NoAssertionPattern_AnyLOC =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/NoAssertionPattern_AnyLOC.txt");

        FileWriter fileWriter_NoAssertionPattern_AnyLOC =
                new FileWriter(NoAssertionPattern_AnyLOC, false);

        BufferedWriter bufferedWriter_NoAssertionPattern_AnyLOC =
                new BufferedWriter(fileWriter_NoAssertionPattern_AnyLOC);

        // NoAssertionPattern_AnyLOC

        File NormalPattern =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/NormalPattern.txt");

        FileWriter fileWriter_NormalPattern =
                new FileWriter(NormalPattern, false);

        BufferedWriter bufferedWriter_NormalPattern =
                new BufferedWriter(fileWriter_NormalPattern);

        // NormalPattern_2/3LOC

        File NormalPattern_AnyLOC =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/NormalPattern_AnyLOC.txt");

        FileWriter fileWriter_NormalPattern_AnyLOC =
                new FileWriter(NormalPattern_AnyLOC, false);

        BufferedWriter bufferedWriter_NormalPattern_AnyLOC =
                new BufferedWriter(fileWriter_NormalPattern_AnyLOC);

        // NormalPattern_AnyLOC

        File TryCatchPattern =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/TryCatchPattern.txt");

        FileWriter fileWriter_TryCatchPattern =
                new FileWriter(TryCatchPattern, false);

        BufferedWriter bufferedWriter_TryCatchPattern =
                new BufferedWriter(fileWriter_TryCatchPattern);

        // TryCatchPattern

        File TryCatchPattern_oneTryOnly =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/TryCatchPattern_oneTryOnly.txt");

        FileWriter fileWriter_TryCatchPattern_oneTryOnly =
                new FileWriter(TryCatchPattern_oneTryOnly, false);

        BufferedWriter bufferedWriter_TryCatchPattern_oneTryOnly =
                new BufferedWriter(fileWriter_TryCatchPattern_oneTryOnly);

        // TryCatchPattern_oneTryOnly

        File TryCatchPattern_AnyLOC_EmbeddedAssert =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/TryCatchPattern_AnyLOC_EA.txt");

        FileWriter fileWriter_TryCatchPattern_AnyLOC_EmbeddedAssert =
                new FileWriter(TryCatchPattern_AnyLOC_EmbeddedAssert, false);

        BufferedWriter bufferedWriter_TryCatchPattern_AnyLOC_EmbeddedAssert =
                new BufferedWriter(fileWriter_TryCatchPattern_AnyLOC_EmbeddedAssert);

        // TryCatchPattern_AnyLOC_EmbeddedAssertƒ

        File NoAssertionPattern_Multiple =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/NoAssertionPattern_Multiple.txt");

        FileWriter fileWriter_AllDecs_AllMCs_Pattern =
                new FileWriter(NoAssertionPattern_Multiple, false);

        BufferedWriter bufferedWriter_NoAssertionPattern_Multiple =
                new BufferedWriter(fileWriter_AllDecs_AllMCs_Pattern);

        // NoAssertionPattern_Multiple

        File NoAssertionPattern_OneMethodInvocationAcrossAll =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/NoAssertionPattern_OneMethodInvocationAcrossAll.txt");

        FileWriter fileWriter_OneMethodInvocationOnly_Pattern =
                new FileWriter(NoAssertionPattern_OneMethodInvocationAcrossAll, false);

        BufferedWriter bufferedWriter_NoAssertionPattern_OneMethodInvocationAcrossAll =
                new BufferedWriter(fileWriter_OneMethodInvocationOnly_Pattern);

        // NoAssertionPattern_OneMethodInvocationAcrossAll

        File FocalMethod =
                new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/StatisticsForTestBodyPattern/FocalMethod.txt");

        FileWriter fileWriter_FocalMethod =
                new FileWriter(FocalMethod, false);

        BufferedWriter bufferedWriter_FocalMethod =
                new BufferedWriter(fileWriter_FocalMethod);

        // FocalMethod

        //End: Record all results to separate files

        PercentageCalculator_v3 calculator = new PercentageCalculator_v3();

        //Start: Main Loop

        for (Project project : ProjectManager.getInstance().getOpenProjects()) {

            for (PsiClass testClass : ProjectUtils.testClasses(project)) {

                for (PsiMethod test : PsiClassUtils.testMethods(testClass)) {

//                    if (!test.getName().equals("testEntries")) continue;
                    //Note: Quick selection

                    PsiCodeBlock test_body = test.getBody();
                    if (test_body == null) continue;
                    if (test_body.getStatements().length > line_of_code_threshold) continue;
                    System.out.println("\nMethod: " + test.getName());

//                    System.out.println("FailDetector: " + new FailDetector_v1().detectFailStatement(test_body));

                    FocalMethodDetector_Simple focalMethodDetectorSimple =
                            new FocalMethodDetector_Simple(project, test_body);

//                    if (focalMethodDetectorSimple.fetchFocalMethodName() != null){
//                        System.out.println("Focal Object: " + focalMethodDetectorSimple.fetchFocalObject());
//                        System.out.println("Focal Method: " + focalMethodDetectorSimple.fetchFocalMethodName());
//                    }

//                    FocalMethodDetector_WithHelper focalMethodDetectorWithHelperOrCC =
//                            new FocalMethodDetector_WithHelper(test);

                    //Start of Chained MethodCalls Analyzer and Declaration Analyzer
                    List<PsiExpressionStatement> expressionStatements = Arrays.stream(test_body.getStatements())
                            .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                            .map(PsiExpressionStatement.class::cast)
                            .collect(Collectors.toList());

//                    for (PsiStatement statement : test_body.getStatements()) {
//                        System.out.println("new Object: "+new NewObjectDetector_v1().isNewObject(statement));
//                    }

                    List<PsiMethodCallExpression> methodCallExpressions = expressionStatements.stream()
                            .filter(psiExpressionStatement -> psiExpressionStatement.getExpression() instanceof PsiMethodCallExpression)
                            .map(psiExpressionStatement -> (PsiMethodCallExpression) psiExpressionStatement.getExpression())
                            .collect(Collectors.toList());

//                    methodCallExpressions.forEach(methodCall -> {
//                        System.out.println("is chained mc?: " +
//                                new ChainedMethodCallAnalyzer_v1().chainedMethodCallChecker(methodCall));
//                        if (new ChainedMethodCallAnalyzer_v1().getAllMethodCalls(methodCall) != null) {
//                            System.out.println("de-chained mcs: "+
//                                    ArrayUtils.toString(new ChainedMethodCallAnalyzer_v1().getDeChainedMcs(methodCall)));
//                            System.out.println("match with name?: "+new NameElement_CheckerAndMatcher_v1()
//                                    .checkIfMatchWithName_ChainedMethodCall(methodCall, test.getName())+"\n -");
//                        }
//                    });

                    List<PsiDeclarationStatement> declarationStatements = Arrays.stream(test_body.getStatements())
                            .filter(statement -> statement instanceof PsiDeclarationStatement)
                            .map(PsiDeclarationStatement.class::cast)
                            .collect(Collectors.toList());

//                    declarationStatements.forEach(declarationStatement -> {
//                        System.out.println("Extracted MC: "+ArrayUtils.toString(new DeclarationAnalyzer_v2()
//                                .extractMethodCallsFromConstructor(declarationStatement)));
//                        System.out.println("Constructor: "+ArrayUtils.toString(new DeclarationAnalyzer_v2()
//                                .extractConstructor(declarationStatement)));
//                        System.out.println("match with name?: "+new NameElement_CheckerAndMatcher_v1()
//                                .checkIfMatchWithName_Constructor(declarationStatement, test.getName()) +"\n-");
//                    });
                    //End of Chained MethodCalls Analyzer and Declaration Analyzer


//                    if (focalMethodDetectorWithHelperOrCC.GetAllNamesInHelper() != null){
//
//                        System.out.println("Helpers: " + ArrayUtils.toString
//                                (focalMethodDetectorWithHelperOrCC.GetAllNamesInHelper()));
//
//                        System.out.println("MostFrequentHelper: " + ArrayUtils.toString
//                                (focalMethodDetectorWithHelperOrCC.GetMostFrequentMethodName()));
//                    }

                    //Get patterns
                    Optional<PatternFinder.PatternMatch> match = patterns.stream()
                            .map(p -> p.matches(test))
                            .filter(Objects::nonNull)
                            .findFirst();

//                    match.ifPresent(patternMatch -> System.out.println(
//                                    "Pattern Name: " + patternMatch.getPatternName()
//                                            +" - Match Action: " + patternMatch.getAction()
//                                            + " - Match Predicate: " + patternMatch.getPredicate()
//                                            + " - Match Scenario: " + patternMatch.getScenario()));
                    //Get patterns
                    //Start of writing results to files

                    if (match.isPresent()){

                        //Get action/predicate/scenario variables ready
                        AtomicReference<String> pattern_type = new AtomicReference<>("");

                        AtomicReference<String> action = new AtomicReference<>("");

                        AtomicReference<String> predicate = new AtomicReference<>("");

                        AtomicReference<String> scenario = new AtomicReference<>("");

                        match.ifPresent(patternMatch -> pattern_type.set(patternMatch.getPatternName()));

                        match.ifPresent(patternMatch -> action.set(patternMatch.getAction()));

                        match.ifPresent(patternMatch -> predicate.set(patternMatch.getPredicate()));

                        match.ifPresent(patternMatch -> scenario.set(patternMatch.getScenario()));

                        //Note: Name and Body results

                        test_body_action.add(action.get());

                        test_body_predicate.add(predicate.get());

                        test_body_scenario.add(scenario.get());

                        //Note: Name Pattern starts

                        NameUtils nameUtils = new NameUtils();

                        nameUtils.parseOneTestName(test.getName());

                        test_names.add(test.getName());

                        test_classes.add(testClass.getName());

                        System.out.println(nameUtils.action_NLP+nameUtils.predicate_NLP+nameUtils.scenario_NLP);

                        if (nameUtils.regex_pattern_name.equals("RegexMatch_SingleEntity")){

                            if (nameUtils.action_Regex == null){
                                nameUtils.action_Regex = "N/A";
                            }
                            if (nameUtils.predicate_Regex == null){
                                nameUtils.predicate_Regex = "N/A";
                            }
                            if (nameUtils.scenario_Regex == null){
                                nameUtils.scenario_Regex = "N/A";
                            }
                            if (nameUtils.action_NLP == null){
                                nameUtils.action_NLP = "N/A";
                            }
                            if (nameUtils.predicate_NLP == null){
                                nameUtils.predicate_NLP = "N/A";
                            }
                            if (nameUtils.scenario_NLP == null){
                                nameUtils.scenario_NLP = "N/A";
                            }

                            test_name_action.add(nameUtils.action_Regex + "(Regex)");
                            test_name_predicate.add(nameUtils.predicate_Regex + "(Regex)");
                            test_name_scenario.add(nameUtils.scenario_Regex + "(Regex)");
                        }
                        else if (nameUtils.action_Regex != null
                                && action.get().toLowerCase().contains(nameUtils.action_Regex.replace("test", "").toLowerCase())){

                            if (nameUtils.action_Regex == null){
                                nameUtils.action_Regex = "N/A";
                            }
                            if (nameUtils.predicate_Regex == null){
                                nameUtils.predicate_Regex = "N/A";
                            }
                            if (nameUtils.scenario_Regex == null){
                                nameUtils.scenario_Regex = "N/A";
                            }
                            if (nameUtils.action_NLP == null){
                                nameUtils.action_NLP = "N/A";
                            }
                            if (nameUtils.predicate_NLP == null){
                                nameUtils.predicate_NLP = "N/A";
                            }
                            if (nameUtils.scenario_NLP == null){
                                nameUtils.scenario_NLP = "N/A";
                            }

                            test_name_action.add(nameUtils.action_Regex.replace("test", "") + "(Regex)");
                            test_name_predicate.add(nameUtils.predicate_Regex + "(Regex)");
                            test_name_scenario.add(nameUtils.scenario_Regex + "(Regex)");
                        }
                        else if (nameUtils.action_Regex != null
                                && nameUtils.action_Regex.toLowerCase().equals(test.getName().toLowerCase())){

                            if (nameUtils.action_Regex == null){
                                nameUtils.action_Regex = "N/A";
                            }
                            if (nameUtils.predicate_Regex == null){
                                nameUtils.predicate_Regex = "N/A";
                            }
                            if (nameUtils.scenario_Regex == null){
                                nameUtils.scenario_Regex = "N/A";
                            }
                            if (nameUtils.action_NLP == null){
                                nameUtils.action_NLP = "N/A";
                            }
                            if (nameUtils.predicate_NLP == null){
                                nameUtils.predicate_NLP = "N/A";
                            }
                            if (nameUtils.scenario_NLP == null){
                                nameUtils.scenario_NLP = "N/A";
                            }

                            test_name_action.add(nameUtils.action_NLP + "(NLP)");
                            test_name_predicate.add(nameUtils.predicate_NLP + "(NLP)");
                            test_name_scenario.add(nameUtils.scenario_NLP + "(NLP)");
                        }
                        //First 3 Special Cases designed for single entity
                        else {

                            List<String> nlp_aps = new LinkedList<>();
                            if (nameUtils.action_NLP == null){
                                nlp_aps.add("N/A");
                            }
                            else {
                                nlp_aps.add(nameUtils.action_NLP);
                            }
                            if (nameUtils.predicate_NLP == null){
                                nlp_aps.add("N/A");
                            }
                            else {
                                nlp_aps.add(nameUtils.predicate_NLP);
                            }
                            if (nameUtils.scenario_NLP == null){
                                nlp_aps.add("N/A");
                            }
                            else {
                                nlp_aps.add(nameUtils.scenario_NLP);
                            }

                            if (nlp_aps.size() != 0) {
                                if (nlp_aps.stream().filter(s -> s.contains("N/A")).count() <= 2) {
                                    test_name_action.add(nameUtils.action_NLP + "(NLP)");
                                    test_name_predicate.add(nameUtils.predicate_NLP + "(NLP)");
                                    test_name_scenario.add(nameUtils.scenario_NLP + "(NLP)");
                                }
                                else{
                                    test_name_action.add(nameUtils.action_Regex + "(Regex)");
                                    test_name_predicate.add(nameUtils.predicate_Regex + "(Regex)");
                                    test_name_scenario.add(nameUtils.scenario_Regex + "(Regex)");
                                }
                            }
                            else {
                                test_name_action.add(nameUtils.action_Regex + "(Regex)");
                                test_name_predicate.add(nameUtils.predicate_Regex + "(Regex)");
                                test_name_scenario.add(nameUtils.scenario_Regex + "(Regex)");
                            }
                            //Add results
                        }
                        if (nameUtils.action_NLP == null &&
                            nameUtils.action_Regex == null &&
                            nameUtils.predicate_NLP ==  null &&
                            nameUtils.predicate_Regex == null &&
                            nameUtils.scenario_NLP == null &&
                            nameUtils.scenario_Regex == null){
                            name_mismatches++;
                            name_counts++;
                        }
                        else{
                            name_counts++;
                        }
                        //End of Fetching Name Pattern Results

                        //Get action/predicate/scenario variables Ready

                        switch (pattern_type.get()){
                            case "AllAssertionPattern(Single)":
                                bufferedWriter_AllAssertionPattern_Single.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "AllAssertionPattern(Multiple)":
                                bufferedWriter_AllAssertionPattern_Multiple.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "IfElsePattern_NoPredicate":
                                bufferedWriter_IfElsePattern_NoPredicate.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "IfElsePattern_v3":
                                bufferedWriter_IfElsePattern_v3.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "IfElsePattern_NoElseStatement":
                                bufferedWriter_IfElsePattern_v3.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "IfElsePattern_EmptyElseStatement":
                                bufferedWriter_IfElsePattern_v3.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "IfElsePattern_EmptyIfStatement":
                                bufferedWriter_IfElsePattern_v3.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "LoopPattern":
                                bufferedWriter_LoopPattern_v3.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "NoAssertionPattern(DeclarationOnly_Single)":
                                bufferedWriter_NoAssertionPattern_DeclarationOnly_Single.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "NoAssertionPattern(MethodCallOnly_Single)":
                                bufferedWriter__NoAssertionPattern_ActionOnly_Single.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "NoAssertionPattern(NewObject_Single)":
                                bufferedWriter_NoAssertionPattern_NewObject_Single.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "NoAssertionPattern":
                                bufferedWriter_NoAssertionPattern.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "NoAssertionPattern(AnyLOC)":
                                bufferedWriter_NoAssertionPattern_AnyLOC.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "NormalPattern(2-3LOC)":
                                bufferedWriter_NormalPattern.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "NormalPattern(AnyLOC)":
                                bufferedWriter_NormalPattern_AnyLOC.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "TryCatchPattern":
                                bufferedWriter_TryCatchPattern.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "TryCatchPattern_oneTryOnly":
                                bufferedWriter_TryCatchPattern_oneTryOnly.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "TryCatchPattern_AnyLOC_EmbeddedAssert":
                                bufferedWriter_TryCatchPattern_AnyLOC_EmbeddedAssert.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "NoAssertionPattern(MethodCallOnly_Multiple)":
                                bufferedWriter_NoAssertionPattern_Multiple.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case  "NoAssertionPattern(DeclarationOnly_Multiple)":
                                bufferedWriter_NoAssertionPattern_Multiple.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case "NoAssertionPattern(OneMethodInvocationAcrossAll)":
                                bufferedWriter_NoAssertionPattern_OneMethodInvocationAcrossAll.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                            case "FocalMethod_Match":
                                bufferedWriter_FocalMethod.write(
                                        "Project: " + project.getName() + "\n Class: " + testClass.getName()
                                                + "\n Method: " + test.getName()
                                                + "\n Body: " + test_body.getText()
                                                + "\n Pattern Name: " + pattern_type.get()
                                                + " - Action: " + action.get()
                                                + " - Predicate: " + predicate.get()
                                                + " - Scenario: " + scenario.get()
                                                + "\n\n"
                                );
                                break;
                        }
                    }

                    //End of writing results to files

                    if (match.isPresent()){
                        calculator.matched = calculator.matched + 1.0;
                    } else{
                        calculator.not_matched = calculator.not_matched + 1.0;
                    }
                    System.out.println("total: " + (calculator.matched + calculator.not_matched));
                    //Note: calculate statistics
                }
            }
        }

        //End: Main Loop

        //Start: Some extra steps

        bufferedWriter_AllAssertionPattern_Single.close();
        bufferedWriter_AllAssertionPattern_Multiple.close();
        bufferedWriter_IfElsePattern_NoPredicate.close();
        bufferedWriter_IfElsePattern_v3.close();
        bufferedWriter_LoopPattern_v3.close();
        bufferedWriter_NoAssertionPattern_DeclarationOnly_Single.close();
        bufferedWriter__NoAssertionPattern_ActionOnly_Single.close();
        bufferedWriter_NoAssertionPattern_NewObject_Single.close();
        bufferedWriter_NoAssertionPattern.close();
        bufferedWriter_NoAssertionPattern_AnyLOC.close();
        bufferedWriter_NormalPattern.close();
        bufferedWriter_NormalPattern_AnyLOC.close();
        bufferedWriter_TryCatchPattern.close();
        bufferedWriter_TryCatchPattern_oneTryOnly.close();
        bufferedWriter_TryCatchPattern_AnyLOC_EmbeddedAssert.close();
        bufferedWriter_NoAssertionPattern_Multiple.close();
        bufferedWriter_NoAssertionPattern_OneMethodInvocationAcrossAll.close();
        bufferedWriter_FocalMethod.close();

        //Readme: current 18 patterns with 1 fallback technique

//        new NameUtils().writePrintToFile("\nTotal counts: " + name_counts + "\n" +
//                "Total matches: " + (name_counts - name_mismatches) + "\n" +
//                "Coverage: " + new DecimalFormat("##.##").format(100.0 * ((name_counts - name_mismatches) / name_counts)) + "%\n");

        calculator.calculate();

        //End: Calculate test-body and test-name coverages before the analysis ends
    }
}
