package TestBodyPattern;

import ExtrasForTestPattern.*;
import com.intellij.execution.junit.JUnitUtil;
import com.intellij.psi.*;
import org.apache.commons.lang.ArrayUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NormalPatternFinder_AnyLOC implements PatternFinder{

    //NMC: Nested Method Calls exist in the assertion -> complete -> v1

    //AnyLOC: Any Order and Any Line Of Code

    //Starts from the first statement close to the first assertion

    //Find the first declaration(Scenario) and the first expression(Action)

    //Also consider "Assert ..." as the predicate statement -> complete v2

    //Update v3 -> Redesign the action, predicate, and scenario

    //Update v4 -> new-Object detector and analyze it as "Chained MCs"

    //Update v5 -> More details about declarations

    @Override
    public PatternMatch matches(PsiMethod test) {

        String action = "";

        String predicate = "";

        String scenario = "";

        PsiCodeBlock test_body = test.getBody();

        if (test_body == null) return null;

        if (test_body.getStatements().length == 0) return null;

        if (new StatementTypeChecker_v2().check(test)) return null;

        List<PsiExpressionStatement> assertions = Arrays.stream(test_body.getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .filter(psiStatement -> psiStatement.getText().contains("Assert") || psiStatement.getText().contains("assert"))
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        List<PsiAssertStatement> pureAssertStatementList = Arrays.stream(test_body.getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiAssertStatement)
                .map(PsiAssertStatement.class::cast)
                .collect(Collectors.toList());

        if (assertions.size() == 0 && pureAssertStatementList.size() == 0) return null;

        List<PsiExpressionStatement> outside_methodCalls = Arrays.stream(test_body.getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .filter(psiStatement -> !psiStatement.getText().contains("Assert") && !psiStatement.getText().contains("assert"))
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        List<String> outside_methodCalls_names = new ArrayList<>();

        for (PsiExpressionStatement expressionStatement : outside_methodCalls){

            if (expressionStatement.getExpression() instanceof PsiMethodCallExpression){

                PsiMethodCallExpression methodCallExpression =
                        (PsiMethodCallExpression) expressionStatement.getExpression();

                outside_methodCalls_names.add(methodCallExpression.getMethodExpression().getReferenceName());
            }
        }

        Optional<Map.Entry<String, Long>> most_frequent_call = outside_methodCalls_names.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));

        if (most_frequent_call.isPresent()){

            Map.Entry<String, Long> result = most_frequent_call.get();

            scenario = result.getKey();
        }
        else {

            scenario = "N/A";
        }
        //Get scenario

        List<String> actions = new ArrayList<>();

        List<String> predicates = new ArrayList<>();

        List<String> asserts = new ArrayList<>();

        if (pureAssertStatementList.size() == 0) {

            for (PsiExpressionStatement expressionStatement : assertions) {

                List<PsiMethodCallExpression> methodCallExpressionsForAssert = new AssertionHandler_v2().getAllEmbeddedMethodCalls(expressionStatement);

                if (methodCallExpressionsForAssert == null) continue;

                if (methodCallExpressionsForAssert.size() == 0) continue;

                if (methodCallExpressionsForAssert.size() == 1){

                    actions.add(methodCallExpressionsForAssert.get(0).getMethodExpression().getReferenceName());
                    //Note: Duplicates are counting as "actions",
                    //Note: nested method calls will be counted as different method calls(may have same suffix)
                }
                else {
                    actions.add(methodCallExpressionsForAssert.get(methodCallExpressionsForAssert.size() - 1)
                            .getMethodExpression().getReferenceName());

                    predicates.add(methodCallExpressionsForAssert.get(0)
                            .getMethodExpression().getReferenceName());
                }

                if (!(expressionStatement.getExpression() instanceof PsiMethodCallExpression)) continue;

                PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getExpression();

                asserts.add(methodCallExpression.getMethodExpression().getQualifiedName());
            }

            if (actions.size() != 0) {

                Optional<Map.Entry<String, Long>> most_frequent_action = actions.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue));

                List<Map.Entry<String, Long>> most_frequent_action_list = new ArrayList<>(actions.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet());

                if (most_frequent_action_list.size() > 1) {
                    action = most_frequent_action.map(stringLongEntry -> stringLongEntry.getKey() + "[1st] " +
                            most_frequent_action_list.get(1).getKey() + "[2rd]")
                            .orElse("[Refer to Alternative]");
                }
                else{
                    action = most_frequent_action.map(stringLongEntry -> stringLongEntry.getKey() + "[Only]")
                            .orElse("[Refer to Alternative]");
                }
                //Note: Change here for action
            }
            else{

                action = "[Refer to Alternative]";
            }

            if (predicates.size() != 0) {

                Optional<Map.Entry<String, Long>> most_frequent_predicate = predicates.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue));

                List<Map.Entry<String, Long>> most_frequent_predicate_list = new ArrayList<>(predicates.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet());

                if (most_frequent_predicate_list.size() > 1) {
                    predicate = most_frequent_predicate.map(stringLongEntry -> stringLongEntry.getKey() + "[1st] "
                            + most_frequent_predicate_list.get(1).getKey() + "[2rd]")
                            .orElse("[UnableToDecide]");
                }
                else{
                    predicate = most_frequent_predicate.map(stringLongEntry -> stringLongEntry.getKey() + "[Only]")
                            .orElse("[UnableToDecide]");
                }
                //Note: Change here for predicate
            }
            else{
                if (asserts.size() != 0){

                    Optional<Map.Entry<String, Long>> most_frequent_assert = asserts.stream()
                            .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                            .entrySet()
                            .stream()
                            .max(Comparator.comparing(Map.Entry::getValue));

                    List<Map.Entry<String, Long>> most_frequent_assert_list = new ArrayList<>(asserts.stream()
                            .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                            .entrySet());

                    if (most_frequent_assert_list.size() > 1) {
                        predicate = most_frequent_assert.map(stringLongEntry -> stringLongEntry.getKey() + "[1st] "
                                + most_frequent_assert_list.get(1).getKey() + "[2rd]")
                                .orElse("[UnableToDecide]");
                    }
                    else{
                        predicate = most_frequent_assert.map(stringLongEntry -> stringLongEntry.getKey() + "[Only]")
                                .orElse("[UnableToDecide]");
                    }
                }
                else {
                    if (assertions.get(assertions.size() - 1).getExpression() instanceof PsiMethodCallExpression) {
                        PsiMethodCallExpression last_assertion = (PsiMethodCallExpression) assertions.get(assertions.size() - 1).getExpression();
                        predicate = last_assertion.getMethodExpression().getReferenceName();
                    }
                    else {
                        predicate = "[Null]";
                    }
                }
            }
        }
        else {
            for (PsiAssertStatement assertStatement : pureAssertStatementList){

                String methodCallExpressionsForAssert = new AssertionHandler_v2().getFromPsiAssert(assertStatement);

                actions.add(methodCallExpressionsForAssert);
            }

            if (actions.size() != 0) {
                Optional<Map.Entry<String, Long>> most_frequent_action = actions.stream()
                        .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                        .entrySet()
                        .stream()
                        .max(Comparator.comparing(Map.Entry::getValue));

                if (most_frequent_action.isPresent()){

                    action = most_frequent_action.get().getKey();
                }
                else{

                    action = "[Refer to Alternative]";
                }
            }
            else{

                action = "[Refer to Alternative]";
            }
        }
        //End: Detect the assertion - "assert...(expected, actual)" or "Assert." or "Assert.assert...(expected, actual)" or "Assert int != 0"

        //Alternative technique - highest frequency ->
        Stream<Map.Entry<String, Long>> m1 = new MethodInvocationAnalyzer_v1()
                .getAllEmbeddedMethodInvocations_RankedByFrequency(test).entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

        Stream<Map.Entry<String, Long>> m2 = new MethodInvocationAnalyzer_v1()
                .getAllObjectsVariableInstance_RankedByFrequency(test).entrySet().stream()
                .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

        String alter_action = "";

        String alter_scenario = "";

        List<Object> alter_actions = m1.collect(Collectors.toList());

        List<Object> alter_scenarios = m2.collect(Collectors.toList());

        if (alter_actions.size() != 0) {

            alter_action = alter_actions.get(0).toString();
        }

        if (alter_scenarios.size() != 0) {

            alter_scenario = alter_scenarios.get(0).toString();
        }
        //Note: Alternative technique - highest frequency


        String finalAlter_action = alter_action;
        String finalAlter_scenario = alter_scenario;

        String finalAction = action;
        String finalPredicate = predicate;
        String finalScenario = scenario;

        return new PatternMatch() {
            @Override
            public String getPatternName() {
                return "NormalPattern(AnyLOC)";
            }

            @Override
            public String getAction() {

                String new_object_mc = "";
                for (PsiStatement statement : test.getBody().getStatements()){

                    NewObjectDetector_v1 detector_v1 = new NewObjectDetector_v1();

                    if (!detector_v1.isNewObject(statement)) continue;

                    if (detector_v1.extractMatchedMethodCall(statement, test.getName()) == null)
                        continue;

                    new_object_mc = detector_v1.extractMatchedMethodCall(statement,
                            test.getName()).getMethodExpression().getReferenceName();
                }
                if (new_object_mc !=  null){
                    if (!new_object_mc.isEmpty()){
                        return new_object_mc + " (Alternative Action: " + finalAlter_action + " )";
                    }
                }
                //New Object Detection

                DeclarationAnalyzer_v2 declarationAnalyzer_v2 = new DeclarationAnalyzer_v2();

                List<PsiDeclarationStatement> declarationStatements = Arrays.stream(test_body.getStatements())
                        .filter(statement -> statement instanceof PsiDeclarationStatement)
                        .map(PsiDeclarationStatement.class::cast)
                        .collect(Collectors.toList());

                if (declarationStatements.size() != 0){

                    List<PsiMethodCallExpression> methodCallExpressions = new LinkedList<>();

                    for (PsiDeclarationStatement statement : declarationStatements){

                        if (declarationAnalyzer_v2.extractMethodCallsFromConstructor(statement) != null){

                            methodCallExpressions
                                    .addAll(declarationAnalyzer_v2.extractMethodCallsFromConstructor(statement));
                        }

                        if (declarationAnalyzer_v2
                                .firstMethodCallMatchedWithName(statement, test.getName()) == null)
                            continue;

                        return finalAction + " (Alternative Action: " + finalAlter_action + " )" +
                                " | " + declarationAnalyzer_v2.firstMethodCallMatchedWithName(statement, test.getName())
                                .getMethodExpression().getReferenceName() + " (Constructor-Based)";
                    }

                    if (methodCallExpressions.size() != 0){

                        return finalAction + " | " +
                                methodCallExpressions.get(0).getMethodExpression().getReferenceName();
                    }
                }
                //Constructor-Based Action

                return finalAction + " (Alternative Action: " + finalAlter_action + " )"; }

            @Override
            public String getPredicate() {
                return finalPredicate;
            }

            @Override
            public String getScenario() {
                return finalScenario + " (Alternative Scenario: " + finalAlter_scenario + " )";
            }
        };
    }
}
