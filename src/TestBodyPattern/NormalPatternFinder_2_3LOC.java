package TestBodyPattern;

import ExtrasForTestPattern.*;
import com.intellij.psi.*;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NormalPatternFinder_2_3LOC implements PatternFinder {

    //Update v2 -> 2/3LOC

    //Update v2.1 -> NMC: Nested Method Calls exist in the assertion

    //Update v3 -> 2LOC or 3LOC: be able to handle only two or three lines of code

    //Update v3.1 -> Redesign the action, predicate, and scenario

    //Update v4 -> new-Object detector and analyze it as "Chained MCs"

    //Update v5 -> More details about declarations

    //v6 -> Extend the chained method calls to "action"

    //v7 -> Change scenario

    private String action = "";

    private String predicate = "";

    private String scenario = "";

    @Override
    public PatternMatch matches(PsiMethod test) {

        if (new StatementTypeChecker_v2().check(test)) return null;

        if (test.getBody() == null) return null;

        List<PsiStatement> statements =
                Arrays.asList((test.getBody()).getStatements());

        if (statements.size() != 3 && statements.size() != 2) return null;

        List<PsiExpressionStatement> assertions = statements.stream()
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .filter(psiStatement -> psiStatement.getText().contains("assert")
                        || psiStatement.getText().contains("Assert"))
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        if (assertions.size() == 0) return null;

        List<PsiStatement> statements_without_assertions = statements.stream()
                .filter(psiStatement -> !psiStatement.getText().contains("assert") &&
                        !psiStatement.getText().contains("Assert"))
                .collect(Collectors.toList());

        List<PsiDeclarationStatement> declarationStatements = statements_without_assertions.stream()
                .filter(psiStatement -> psiStatement instanceof PsiDeclarationStatement)
                .map(PsiDeclarationStatement.class::cast)
                .collect(Collectors.toList());

        if (statements_without_assertions.size() == 0 || declarationStatements.size() == 0) return null;

        //Make sure one or more assertions exist and remove it/them from the list

        if (statements.size() == 2){

            PsiDeclarationStatement declarationStatement = declarationStatements.get(0);

            Collection<PsiExpression> methodCalls_first_declaration =
                    new SubMethodCallExtractor_v2().extractAllSubMethodCalls_Declaration(declarationStatement);

            List<PsiReferenceExpression> referenceExpressions = methodCalls_first_declaration.stream()
                    .filter(psiExpression -> psiExpression instanceof PsiReferenceExpression)
                    .map(PsiReferenceExpression.class::cast)
                    .collect(Collectors.toList());

            if (referenceExpressions.size() != 0) {

                String method_call_declaration = referenceExpressions.get(0).getReferenceName();

                Collection<PsiMethodCallExpression> methodCalls_InFirstAssertion =
                        new SubMethodCallExtractor_v2().extractAllSubMethodCalls_Expression(assertions.get(0));

                LinkedList<PsiMethodCallExpression> methodCallExpressions_InAssertion
                        = new LinkedList<>(methodCalls_InFirstAssertion);

                if (methodCallExpressions_InAssertion.size() == 2) {

                    String second_methodCall = methodCallExpressions_InAssertion
                            .getLast().getMethodExpression().getReferenceName();

                    String starter;

                    PsiMethodCallExpression assertion = (PsiMethodCallExpression) assertions.get(0).getExpression();

                    if (assertion.getMethodExpression().getReferenceName() != null){
                        starter = assertion.getMethodExpression().getReferenceName();
                    }
                    else{
                        starter = "[n/a]";
                    }
                    //The name of assertion

                    predicate = starter;

                    if (second_methodCall != null && second_methodCall.equals(method_call_declaration)) {
                        action = second_methodCall + "(matched)";
                    } else {
                        action = second_methodCall + "(unmatched)";
                    }

                    List<PsiLocalVariable> localVariables = Arrays.stream(declarationStatement.getDeclaredElements())
                            .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                            .map(PsiLocalVariable.class::cast)
                            .collect(Collectors.toList());

                    if (localVariables.size() != 0) {

                        return new PatternMatch() {
                            @Override
                            public String getPatternName() {
                                return "NormalPattern(2-3LOC)";
                            }

                            @Override
                            public String getAction() {

                                String new_object_mc = "";
                                for (PsiStatement statement : statements){

                                    NewObjectDetector_v1 detector_v1 = new NewObjectDetector_v1();

                                    if (!detector_v1.isNewObject(statement)) continue;

                                    if (detector_v1.extractMatchedMethodCall(statement, test.getName()) == null)
                                        continue;

                                    new_object_mc = detector_v1.extractMatchedMethodCall(statement,
                                            test.getName()).getMethodExpression().getReferenceName();
                                }
                                if (new_object_mc !=  null){
                                    if (!new_object_mc.isEmpty()){
                                        return action + " | " +new_object_mc +"(new object based)";
                                    }
                                }
                                //New Object Detection

                                DeclarationAnalyzer_v2 declarationAnalyzer_v2 = new DeclarationAnalyzer_v2();

                                List<PsiDeclarationStatement> declarationStatements = statements.stream()
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

                                        return action + " | " +
                                                declarationAnalyzer_v2.firstMethodCallMatchedWithName(statement, test.getName())
                                                        .getMethodExpression().getReferenceName() + " (Constructor-Based)";
                                    }

                                    if (methodCallExpressions.size() != 0){

                                        return action + " | " + methodCallExpressions.get(0).getMethodExpression().getReferenceName();
                                    }
                                }
                                //Constructor-Based Action

                                return action;
                            }

                            @Override
                            public String getPredicate() {
                                return predicate;
                            }

                            @Override
                            public String getScenario() {
                                return  methodCallExpressions_InAssertion.getFirst()
                                        .getMethodExpression().getReferenceName() + "-" + localVariables.get(0).getName();
                            }
                        };
                    }
                }
            }
        }

        action = "";
        predicate = "";
        scenario = "";
        //Reset all

        PsiExpressionStatement assertion_line = assertions.get(assertions.size() - 1);

        List<PsiMethodCallExpression> methodCalls_InAssertion =
                new AssertionHandler_v2().getAllEmbeddedMethodCalls(assertion_line);

        if (methodCalls_InAssertion == null) return null;

        PsiMethodCallExpression old_action = methodCalls_InAssertion.get(methodCalls_InAssertion.size() - 1);
        NameElement_CheckerAndMatcher_v1 checkerAndMatcher_v1 = new NameElement_CheckerAndMatcher_v1();
        ChainedMethodCallAnalyzer_v1 callAnalyzer_v1 = new ChainedMethodCallAnalyzer_v1();

        if (callAnalyzer_v1.chainedMethodCallChecker(old_action)){
            if (checkerAndMatcher_v1.checkIfMatchWithName_AllMC(old_action, test.getName())){

                action = checkerAndMatcher_v1
                        .getMatchedMC_AllMC(old_action, test.getName())
                        .getMethodExpression().getReferenceName();
            }
            else{
                action = old_action.getMethodExpression().getReferenceName();
            }
        }
        else {
            action = old_action.getMethodExpression().getReferenceName();
        }
        //New action

        List<PsiExpressionStatement> predicate_lines = new LinkedList<>();

        List<PsiDeclarationStatement> scenario_lines = new LinkedList<>();

        //Init all parameters

        for (PsiStatement s:
             statements_without_assertions) {

            if (s instanceof PsiDeclarationStatement){

                scenario_lines.add((PsiDeclarationStatement) s);
            }

            if (s instanceof PsiExpressionStatement){

                predicate_lines.add((PsiExpressionStatement) s);
            }
        }

        if (predicate_lines.size() == 0 && scenario_lines.size() == 0) return null;

        if (scenario_lines.size() != 0) {

            List<PsiLocalVariable> localVariables = Arrays.stream(scenario_lines.get(0).getDeclaredElements())
                    .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                    .map(PsiLocalVariable.class::cast)
                    .collect(Collectors.toList());

            if (localVariables.size() == 0) return null;

            PsiLocalVariable get_scenario =
                    localVariables.get(0);

            scenario = get_scenario.getName();
        }
        else{
            scenario = "N/A";
        }
        //Get scenario

        String starter;

        PsiMethodCallExpression assertion = (PsiMethodCallExpression) assertions.get(assertions.size() - 1).getExpression();

        if (assertion.getMethodExpression().getReferenceName() != null){
            starter = assertion.getMethodExpression().getReferenceName();
        }
        else{
            starter = "[n/a]";
        }
        //The name of assertion

        if (predicate_lines.size() != 0) {

            List<PsiMethodCallExpression> action_method_calls =
                    Arrays.stream(predicate_lines.get(0).getChildren())
                            .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                            .map(PsiMethodCallExpression.class::cast)
                            .collect(Collectors.toList());

            if (action_method_calls.size() == 0) return null;

            PsiMethodCallExpression first_methodCall_Scenario = action_method_calls.get(0);

            predicate = starter;

            if (first_methodCall_Scenario.getMethodExpression().getReferenceName() != null){

                scenario = first_methodCall_Scenario.getMethodExpression().getReferenceName();
            }
        }
        else if (methodCalls_InAssertion.size() > 1){
            predicate = starter + "-" + methodCalls_InAssertion.get(0).getMethodExpression().getReferenceName();
        }
        else {
            predicate = starter;
        }
        //Get predicate and reset scenario


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

        return new PatternMatch() {

            @Override
            public String getPatternName() {
                return "NormalPattern(2-3LOC)";
            }

            @Override
            public String getAction() {

                String new_object_mc = "";
                for (PsiStatement statement : statements){

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

                List<PsiDeclarationStatement> declarationStatements = statements.stream()
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

                        return action + " | " +
                                declarationAnalyzer_v2.firstMethodCallMatchedWithName(statement, test.getName())
                                        .getMethodExpression().getReferenceName() + " (Constructor-Based)";
                    }

                    if (methodCallExpressions.size() != 0){

                        return action + " | " +
                                methodCallExpressions.get(0).getMethodExpression().getReferenceName();
                    }
                }
                //Constructor-Based Action

                return action + " (Alternative Action: " + finalAlter_action + " )";
            }

            @Override
            public String getPredicate() {
                return predicate;
            }

            @Override
            public String getScenario() {
                return scenario + " (Alternative Scenario: " + finalAlter_scenario + " )";
            }
        };
    }
}
