package TestBodyPattern;

import ExtrasForTestPattern.*;
import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NoAssertionPatternFinder_AnyLOC implements PatternFinder {

    //AnyLOC: Any Order and Any Line Of Code
    //-> starts from the first statement close to the first assertion
    //-> find the first declaration(Scenario) and the first expression(Action)

    //Update v3 -> new Object detector and analyze it as "Chained MCs"

    //Update v4 -> More details about declarations

    //Update v5 -> Assignment statement

    //v6 -> Extend the chained method calls to "action"

    @Override
    public PatternMatch matches(PsiMethod test) {

        if (new StatementTypeChecker_v2().check(test)) return null;

        PsiCodeBlock test_body = test.getBody();

        if (test_body == null) return null;

        List<PsiStatement> statements = Arrays.asList((test.getBody()).getStatements());

        List<PsiStatement> filtering_assertions1 = Arrays.stream(test.getBody().getStatements())
                .filter(psiStatement -> psiStatement.getText().contains("assert"))
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .collect(Collectors.toList());

        List<PsiStatement> filtering_assertions2 = Arrays.stream(test.getBody().getStatements())
                .filter(psiStatement -> psiStatement.getText().contains("Assert"))
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .collect(Collectors.toList());

        if (filtering_assertions1.size() != 0 || filtering_assertions2.size() != 0) return null;

        //Ensure there is no assertion

        PsiStatement[] all_statements = test_body.getStatements();

        PsiExpressionStatement action_statement = null;

        PsiDeclarationStatement scenario_statement = null;

        for (PsiStatement statement: all_statements) {

            if (!(statement instanceof PsiDeclarationStatement)
                    && !(statement instanceof PsiExpressionStatement)) continue;

            if (statement instanceof PsiExpressionStatement && action_statement == null){

                PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement;

                if (expressionStatement.getExpression() instanceof PsiMethodCallExpression){
                    action_statement = expressionStatement;
                }
            }

            if (statement instanceof PsiDeclarationStatement && scenario_statement == null){

                scenario_statement = (PsiDeclarationStatement) statement;
            }

            if (action_statement != null && scenario_statement != null) break;
        }

        //Get the first dec & mc statement matches the correct class type

        if (action_statement == null || scenario_statement == null) return null;

        Optional<PsiMethodCallExpression> methodCallExpression =
                Arrays.stream(action_statement.getChildren())
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .findFirst();

        Optional<PsiLocalVariable> localVariable =
                Arrays.stream(scenario_statement.getChildren())
                        .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                        .map(PsiLocalVariable.class::cast)
                        .findFirst();

        if (!methodCallExpression.isPresent() || !localVariable.isPresent()) return null;

        //Start: Alternative technique - highest frequency

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
                return "NoAssertionPattern(AnyLOC)";
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

                        if (declarationAnalyzer_v2.extractMethodCallsFromConstructor(statement)!= null){

                            methodCallExpressions
                                    .addAll(declarationAnalyzer_v2.extractMethodCallsFromConstructor(statement));
                        }

                        if (declarationAnalyzer_v2
                                .firstMethodCallMatchedWithName(statement, test.getName()) == null)
                            continue;

                        return methodCallExpression.get().getMethodExpression().getReferenceName() + " | " +
                                declarationAnalyzer_v2.firstMethodCallMatchedWithName(statement, test.getName())
                                        .getMethodExpression().getReferenceName() + " (Constructor-Based)";
                    }

                    if (methodCallExpressions.size() != 0){

                        return methodCallExpression.get().getMethodExpression().getReferenceName()
                                + " | " + methodCallExpressions.get(0).getMethodExpression().getReferenceName();
                    }
                }
                //Constructor-Based Action

                List<PsiMethodCallExpression> assignment_mcs = new AssignmentDetector_v1().assignmentDetector(test);

                if (assignment_mcs.size() != 0){

                    return methodCallExpression.get().getMethodExpression().getReferenceName()
                            + " (Alternative Action: " + finalAlter_action + " )" + " | "
                            + assignment_mcs.get(0).getMethodExpression().getReferenceName() + " (Assignment-Based)";
                }
                //Assignment-Based Action

                NameElement_CheckerAndMatcher_v1 checkerAndMatcher_v1 = new NameElement_CheckerAndMatcher_v1();
                ChainedMethodCallAnalyzer_v1 callAnalyzer_v1 = new ChainedMethodCallAnalyzer_v1();

                String action = "";

                if (callAnalyzer_v1.chainedMethodCallChecker(methodCallExpression.get())){
                    if (checkerAndMatcher_v1.checkIfMatchWithName_AllMC(methodCallExpression.get(), test.getName())){

                        action = checkerAndMatcher_v1
                                .getMatchedMC_AllMC(methodCallExpression.get(), test.getName())
                                .getMethodExpression().getReferenceName();
                    }
                    else{
                        action = methodCallExpression.get().getMethodExpression().getReferenceName();
                    }
                }
                else {
                    action = methodCallExpression.get().getMethodExpression().getReferenceName();
                }
                //New action

                return action
                        + " (Alternative Action: " + finalAlter_action + " )";
            }

            @Override
            public String getPredicate() {
                return "N/A";
            }

            @Override
            public String getScenario() {
                return localVariable.get().getName()
                        + " (Alternative Scenario: " + finalAlter_scenario + " )";
            }
        };
    }
}
