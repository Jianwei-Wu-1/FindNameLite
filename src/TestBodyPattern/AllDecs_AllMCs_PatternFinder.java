package TestBodyPattern;

import ExtrasForTestPattern.ChainedMethodCallAnalyzer_v1;
import ExtrasForTestPattern.DeclarationAnalyzer_v2;
import ExtrasForTestPattern.MethodInvocationAnalyzer_v1;
import com.intellij.psi.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllDecs_AllMCs_PatternFinder implements PatternFinder{

    //v1 -> In order to detect any test case only has all declarations or all method calls

    //v2 -> More details about declarations

    //v3 -> Extend the chained method calls to "action"

    @Override
    public PatternMatch matches(PsiMethod test) {

        PsiCodeBlock test_body = test.getBody();

        if (test_body == null) return null;

        int original_size = test_body.getStatementCount();

        List<PsiExpressionStatement> expressionStatements = Arrays.stream(test_body.getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .filter(psiStatement -> !psiStatement.getText().contains("assert"))
                .filter(psiStatement -> !psiStatement.getText().contains("Assert"))
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        List<PsiDeclarationStatement> declarationStatements = Arrays.stream(test_body.getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiDeclarationStatement)
                .map(PsiDeclarationStatement.class::cast)
                .collect(Collectors.toList());

        List<PsiMethodCallExpression> methodCallExpressions = expressionStatements.stream()
                .filter(psiExpressionStatement -> psiExpressionStatement.getExpression() instanceof PsiMethodCallExpression)
                .map(psiExpressionStatement -> (PsiMethodCallExpression) psiExpressionStatement.getExpression())
                .collect(Collectors.toList());

        List<PsiLocalVariable> localVariables = declarationStatements.stream()
                .filter(psiDeclarationStatement -> psiDeclarationStatement.getDeclaredElements()[0] instanceof PsiLocalVariable)
                .map(psiDeclarationStatement -> (PsiLocalVariable)psiDeclarationStatement.getDeclaredElements()[0])
                .collect(Collectors.toList());

        if (methodCallExpressions.size() !=  original_size && localVariables.size() != original_size) return null;

        //Start: Alternative technique - highest frequency

        String alter_action = "";

        String alter_scenario = "";

        if (new MethodInvocationAnalyzer_v1().getAllEmbeddedMethodInvocations_RankedByFrequency(test) != null &&
                new MethodInvocationAnalyzer_v1().getAllObjectsVariableInstance_RankedByFrequency(test) != null) {

            Stream<Map.Entry<String, Long>> m1 = new MethodInvocationAnalyzer_v1()
                    .getAllEmbeddedMethodInvocations_RankedByFrequency(test).entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

            Stream<Map.Entry<String, Long>> m2 = new MethodInvocationAnalyzer_v1()
                    .getAllObjectsVariableInstance_RankedByFrequency(test).entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()));

            List<Object> alter_actions = m1.collect(Collectors.toList());

            List<Object> alter_scenarios = m2.collect(Collectors.toList());

            if (alter_actions.size() != 0) {

                alter_action = alter_actions.get(0).toString();
            }

            if (alter_scenarios.size() != 0) {

                alter_scenario = alter_scenarios.get(0).toString();
            }
        }

        //Note: Alternative technique - highest frequency

        if (methodCallExpressions.size() == original_size){

            List<PsiMethodCallExpression> mc_withChainedMC = new LinkedList<>();

            ChainedMethodCallAnalyzer_v1 callAnalyzer_v1 = new ChainedMethodCallAnalyzer_v1();

            for (PsiMethodCallExpression mc : methodCallExpressions){
                if (mc == null) continue;

                if (!callAnalyzer_v1.chainedMethodCallChecker(mc)) continue;

                mc_withChainedMC.addAll(callAnalyzer_v1.getAllMethodCalls(mc));
            }
            //Get all sub-MC from all chained MC

            List<String> method_calls = mc_withChainedMC.stream()
                    .map(psiMethodCallExpression -> psiMethodCallExpression.getMethodExpression().getReferenceName())
                    .collect(Collectors.toList());

            Map<String, Long> method_calls_occurrences = method_calls.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            List<String> method_calls_set = new LinkedList<>(method_calls_occurrences.keySet());

            if (method_calls.size() == 0
                    || method_calls_occurrences.size() == 0
                    || method_calls_set.size() == 0) return null;

            Optional<PsiMethodCallExpression> first_method_call =  methodCallExpressions.stream()
                    .filter(psiMethodCallExpression ->
                            Objects.equals(psiMethodCallExpression.
                                    getMethodExpression().getReferenceName(), method_calls_set.get(0)))
                    .findFirst();

            String finalAlter_action = alter_action;
            String finalAlter_scenario = alter_scenario;

            return new PatternMatch() {
                @Override
                public String getPatternName() {
                    if (test_body.getStatementCount() > 1 ) {
                        return "NoAssertionPattern(MethodCallOnly_Multiple)";
                    }
                    else {
                        return "NoAssertionPattern(MethodCallOnly_Single)";
                    }
                }

                @Override
                public String getAction() {
                    return method_calls_set.get(0)
                            + " (Alternative Action: " + finalAlter_action + " )";
                }

                @Override
                public String getPredicate() {
                    return "N/A";
                }

                @Override
                public String getScenario() {

                    if (first_method_call.isPresent()){

                        if (first_method_call.get()
                                .getMethodExpression().getQualifierExpression() == null) return "(N/A)-1"
                                + " (Alternative Scenario: " + finalAlter_scenario + " )";

                        return Objects.requireNonNull(first_method_call.get()
                                .getMethodExpression().getQualifierExpression()).getText()
                                + " (Alternative Scenario: " + finalAlter_scenario + " )";

                    }
                    return "(N/A)-2"
                            + " (Alternative Scenario: " + finalAlter_scenario + " )";
                }
            };
        }

        else if (localVariables.size() == original_size){

            List<PsiMethodCallExpression> embedded_mcs = new DeclarationAnalyzer_v2()
                    .extractAllEmbeddedMethodCalls(localVariables);

            List<String> mc_names = embedded_mcs.stream()
                    .map(psiMethodCallExpression -> psiMethodCallExpression.getMethodExpression().getReferenceName())
                    .collect(Collectors.toList());

            Map<String, Long> mcs_occurrences = mc_names.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            List<String> variables = localVariables.stream()
                    .map(PsiNamedElement::getName)
                    .collect(Collectors.toList());

            Map<String, Long> variables_occurrences = variables.stream()
                    .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

            List<String> variables_occurrences_set = new LinkedList<>(variables_occurrences.keySet());

            List<String> mcs_occurrences_set = new LinkedList<>(mcs_occurrences.keySet());

            if (variables.size() == 0
                    || variables_occurrences.size() == 0
                    || variables_occurrences_set.size() == 0) return null;

            String finalAlter_action = alter_action;
            String finalAlter_scenario = alter_scenario;

            return new PatternMatch() {
                @Override
                public String getPatternName() {
                    if (test_body.getStatementCount() > 1 ) {
                        return "NoAssertionPattern(DeclarationOnly_Multiple)";
                    }
                    else {
                        return "NoAssertionPattern(DeclarationOnly_Single)";
                    }
                }

                @Override
                public String getAction() {
                    if (mcs_occurrences_set.size() != 0) {

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

                                return mcs_occurrences_set.get(0) + " (Alternative Action: " + finalAlter_action + " )" + " | " +
                                        declarationAnalyzer_v2.firstMethodCallMatchedWithName(statement, test.getName())
                                                .getMethodExpression().getReferenceName() + " (Constructor-Based)";
                            }

                            if (methodCallExpressions.size() != 0){

                                return mcs_occurrences_set.get(0) + " (Alternative Action: " + finalAlter_action + " )" + " | " +
                                        methodCallExpressions.get(0).getMethodExpression().getReferenceName();
                            }
                        }
                        //Constructor-Based Action

                        return mcs_occurrences_set.get(0) + " (Alternative Action: " + finalAlter_action + " )";
                    }
                    else {
                        return " (Alternative Action: " + finalAlter_action + " )";
                    }
                }

                @Override
                public String getPredicate() {
                    return "N/A";
                }

                @Override
                public String getScenario() {
                    return variables_occurrences_set.get(0) + " (Alternative Scenario: " + finalAlter_scenario + " )";
                }
            };
        }
        return null;
    }
}
