package TestBodyPattern;

import ExtrasForTestPattern.*;
import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Collectors;

public class NoAssertionPatternFinder implements PatternFinder {

    private String action = "";

    private String predicate = "";

    private String scenario = "";

    //Update v4 -> new-Object detector and analyze it as "Chained MCs"

    //Update v4.1 -> More details about declarations

    //v5 -> Extend the chained method calls to "action"

    @Override
    public PatternMatch matches(PsiMethod test) {

        if (new StatementTypeChecker_v2().check(test)) return null;

        if (test.getBody() == null) return null;

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

        if (statements.size() < 3) return null;

        //At least 3 lines of code

        if ((!(statements.get(0) instanceof PsiDeclarationStatement
                && statements.get(1) instanceof PsiExpressionStatement))
                &&
                (!(statements.get(1) instanceof PsiDeclarationStatement
                        && statements.get(0) instanceof PsiExpressionStatement))) return null;

        if (!(statements.get(2) instanceof PsiExpressionStatement)) return null;

        PsiExpressionStatement action_line;

        PsiDeclarationStatement scenario_line;

        //Init all parameters

        if (statements.get(0) instanceof  PsiExpressionStatement){

            action_line = (PsiExpressionStatement) statements.get(0);

            if (action_line.getText().contains("assert")
                    || action_line.getText().contains("Assert")) return null;
        }
        else{

            action_line = (PsiExpressionStatement) statements.get(1);

            if (action_line.getText().contains("assert")
                    || action_line.getText().contains("Assert")) return null;
        }

        if (statements.get(0) instanceof  PsiDeclarationStatement){

            scenario_line = (PsiDeclarationStatement) statements.get(0);
        }
        else{

            scenario_line = (PsiDeclarationStatement) statements.get(1);
        }

        List<PsiLocalVariable> localVariableList = Arrays.stream(scenario_line.getDeclaredElements())
                .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                .map(PsiLocalVariable.class::cast)
                .collect(Collectors.toList());

        if (localVariableList.size() == 0) return null;

        PsiLocalVariable get_scenario = localVariableList.get(0);

        scenario = get_scenario.getName();

        List<PsiMethodCallExpression> action_method_calls =
                Arrays.stream(action_line.getChildren())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

        if (action_method_calls.size() == 0) return null;

        NameElement_CheckerAndMatcher_v1 checkerAndMatcher_v1 = new NameElement_CheckerAndMatcher_v1();
        ChainedMethodCallAnalyzer_v1 callAnalyzer_v1 = new ChainedMethodCallAnalyzer_v1();

        if (callAnalyzer_v1.chainedMethodCallChecker(action_method_calls.get(0))){
            if (checkerAndMatcher_v1.checkIfMatchWithName_AllMC(action_method_calls.get(0), test.getName())){

                action = checkerAndMatcher_v1
                        .getMatchedMC_AllMC(action_method_calls.get(0), test.getName())
                        .getMethodExpression().getReferenceName();
            }
            else{
                action = action_method_calls.get(0).getMethodExpression().getReferenceName();
            }
        }
        else {
            action = action_method_calls.get(0).getMethodExpression().getReferenceName();
        }
        //New action

        if (statements.get(2).getText().contains("assert")
                || statements.get(2).getText().contains("Assert")) return null;

        if (statements.size() == 3
                && !statements.get(2).getText().contains("assert")) {

            PsiExpressionStatement third_line = (PsiExpressionStatement) statements.get(2);

            List<PsiMethodCallExpression> predicate_method_calls = Arrays.stream(third_line.getChildren())
                            .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                            .map(PsiMethodCallExpression.class::cast)
                            .collect(Collectors.toList());

            predicate = predicate_method_calls.get(0).getMethodExpression().getReferenceName();
        }
        else {

            predicate = "N/A";
        }

        //If there are exactly three lines of code and match the pattern, the third one will be treated as predicate

        return new PatternMatch() {
            @Override
            public String getPatternName() {
                return "NoAssertionPattern";
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
                        return action + " | " + new_object_mc;
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
                            methodCallExpressions.addAll(declarationAnalyzer_v2.extractMethodCallsFromConstructor(statement));
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
                return scenario;
            }
        };
    }
}
