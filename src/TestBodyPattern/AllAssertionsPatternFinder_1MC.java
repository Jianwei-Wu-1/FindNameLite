package TestBodyPattern;

import ExtrasForTestPattern.MethodInvocationAnalyzer_v1;
import ExtrasForTestPattern.SubMethodCallExtractor_v2;
import SubtractCompFromBody.SubtractComponents_Body;
import TestNamePattern.RegexForTestNamePattern_v1_1Kt;
import com.intellij.psi.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AllAssertionsPatternFinder_1MC implements PatternFinder{

    private String assertionHelper(PsiMethodCallExpression expression, String test_name){

        String assertion =  expression.getMethodExpression().getReferenceName();

        if (assertion == null) return null;

        if (assertion.startsWith("assert")){

            String parsed_name = RegexForTestNamePattern_v1_1Kt.parseName(test_name);

            List<String> words_from_name =
                    Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_name));

            words_from_name =
                    words_from_name.stream().map(String::toLowerCase).collect(Collectors.toList());

            String parsed_mc =
                    RegexForTestNamePattern_v1_1Kt
                            .parseName(assertion.replace(assertion.substring(0, 6), ""));

            List<String> words_from_mc =
                    Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_mc));

            words_from_mc = words_from_mc.stream().map(String::toLowerCase).collect(Collectors.toList());

            if (words_from_name.containsAll(words_from_mc)) {

                return assertion.replace(assertion.substring(0, 6), "");
            }
        }
        return null;
    }

    @Override
    public PatternMatch matches(PsiMethod test) {

        PsiClass original_class = test.getContainingClass();

        if (original_class ==  null) return null;

        if (test.getBody() == null) return null;

        SubtractComponents_Body subtractComponents_body =
                new SubtractComponents_Body(test.getName(), original_class.getName());

        if (subtractComponents_body.fetchAllElements() == null) return null;

        List<PsiElement> elements = subtractComponents_body.fetchAllElements().stream()
                .filter(p -> p instanceof PsiMethodCallExpression)
                .collect(Collectors.toList());

        List<PsiStatement> statements = Arrays.stream((test.getBody()).getStatements())
                .filter(psiStatement -> !(psiStatement instanceof PsiJavaToken))
                .collect(Collectors.toList());

        List<PsiExpressionStatement> assertion_statements = Arrays.stream((test.getBody()).getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        if (statements.size() == 0 || assertion_statements.size() == 0) return null;

        if (statements.size() != assertion_statements.size()) return null;

        //Ensure all statements are assertions

        if (elements.size() == 0) return null;

        for (PsiStatement psiStatement : assertion_statements){

            PsiExpressionStatement current_line = (PsiExpressionStatement) psiStatement;

            if (!(current_line.getExpression().getText().contains("assert"))){
                return null;
            }
        }

        //When any expression line does NOT contain "assert", it won't be a "AllAssertionPattern"

        PsiElement[] firstLine_EmbeddedMethodCalls = elements.get(0).getChildren();

        List<PsiExpression> Sub_EmbeddedMethodCalls = new LinkedList<>();

        List<PsiReference> Sub_EmbeddedMethodReferences;

        for (PsiElement psiElement : firstLine_EmbeddedMethodCalls){

            if (!(psiElement instanceof PsiExpressionList)) continue;

            Sub_EmbeddedMethodCalls = Arrays
                    .stream(((PsiExpressionList) psiElement).getExpressions())
                    .filter(p -> p instanceof PsiMethodCallExpression)
                    .collect(Collectors.toList());

            //Todo: Modify here if some method calls are not captured

            Sub_EmbeddedMethodReferences
                    = Arrays.asList(psiElement.getReferences());

            if (Sub_EmbeddedMethodCalls.size() != 0 && Sub_EmbeddedMethodReferences.size() != 0) break;
        }

        if (Sub_EmbeddedMethodCalls.size() < 1) return null;

        if (Sub_EmbeddedMethodCalls.size() == 1){

            List<PsiExpression> finalSub_EmbeddedMethodCalls = Sub_EmbeddedMethodCalls;

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
                    if (assertion_statements.size() > 1){
                        return "AllAssertionPattern(Multiple)";
                    }
                    else {
                        return "AllAssertionPattern(Single)";
                    }
                }

                @Override
                public String getAction() {
                    return ((PsiMethodCallExpression) finalSub_EmbeddedMethodCalls.get(0))
                            .getMethodExpression().getReferenceName()
                            + " (Alternative Action: " + finalAlter_action + " )";
                }

                @Override
                public String getPredicate() {

                    PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) elements.get(0);

                    if (assertionHelper(methodCallExpression, test.getName()) != null){
                        return assertionHelper(methodCallExpression, test.getName());
                    }
                    //Assertion Helper

                    return "N/A";
                }

                @Override
                public String getScenario() {

                    PsiExpression scenario_expression =
                            ((PsiMethodCallExpression) finalSub_EmbeddedMethodCalls.get(0))
                            .getMethodExpression().getQualifierExpression();

                    if (scenario_expression == null){
                        return null;
                    }

                    return scenario_expression.getText() + " (Alternative Scenario: " + finalAlter_scenario + " )";
                }
            };
        }

        PsiMethodCallExpression first = (PsiMethodCallExpression) Sub_EmbeddedMethodCalls.get(0);

        PsiMethodCallExpression last = (PsiMethodCallExpression) Sub_EmbeddedMethodCalls.get(Sub_EmbeddedMethodCalls.size() - 1);

        Collection<PsiMethodCallExpression> methodCallExpressions =
                new SubMethodCallExtractor_v2().extractAllSubMethodCalls_Expression(assertion_statements.get(0));

        List<PsiMethodCallExpression> list_of_mc = new LinkedList<>(methodCallExpressions);

        if (list_of_mc.size() ==  2){

            PsiMethodCallExpression first_mc = list_of_mc.get(0);

            PsiMethodCallExpression second_mc = list_of_mc.get(1);

            String scenario = "N/A";

            if (second_mc.getMethodExpression().getQualifierExpression() != null){
                scenario = second_mc.getMethodExpression().getQualifierExpression().getText();
            }

            String finalScenario = scenario;
            return new PatternMatch() {
                @Override
                public String getPatternName() {
                    if (assertion_statements.size() > 1){
                        return "AllAssertionPattern(Multiple)";
                    }
                    else {
                        return "AllAssertionPattern(Single)";
                    }
                }

                @Override
                public String getAction() {
                    return second_mc.getMethodExpression().getReferenceName();
                }

                @Override
                public String getPredicate() {
                    return first_mc.getMethodExpression().getReferenceName();
                }

                @Override
                public String getScenario() {
                    return finalScenario;
                }
            };
        }


        String scenario = "";

        if (first.getMethodExpression().getQualifierExpression() == null &&
                last.getMethodExpression().getQualifierExpression() == null) return null;

        if (first.getMethodExpression().getQualifierExpression() != null){

            scenario = first.getMethodExpression().getQualifierExpression().getText();
        }

        if (last.getMethodExpression().getQualifierExpression() != null){

            scenario = last.getMethodExpression().getQualifierExpression().getText();
        }

        if (first.getMethodExpression().getQualifierExpression() != null
                && last.getMethodExpression().getQualifierExpression() != null) {

            if (first.getMethodExpression().getQualifierExpression().getText()
                    .equals(last.getMethodExpression().getQualifierExpression().getText())) {

                scenario = first.getMethodExpression().getQualifierExpression().getText() + "(Matched)";
            }
        }

        //The "QualifierExpression" is the method call's original object that invokes the method call

        String finalScenario = scenario;
        //Finalize All components: Action, Predicate, Scenario
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

        String final_alter_action = alter_action;

        String final_alter_scenario = alter_scenario;

        return new PatternMatch() {

            @Override
            public String getPatternName() {
                if (assertion_statements.size() > 1){
                    return "AllAssertionPattern(Multiple)";
                }
                else {
                    return "AllAssertionPattern(Single)";
                }
            }

            @Override
            public String getAction() {
                return first.getMethodExpression().getReferenceName()
                        + " (Alternative Action: " + final_alter_action + " )";
            }

            @Override
            public String getPredicate() {
                return last.getMethodExpression().getReferenceName();
            }

            @Override
            public String getScenario() {
                return finalScenario
                        + " (Alternative Scenario: " + final_alter_scenario + " )";
            }
        };
    }
}
