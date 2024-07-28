package TestBodyPattern;

import ExtrasForTestPattern.DeclarationAnalyzer_v2;
import ExtrasForTestPattern.NestedStatementsFilter_v1;
import ExtrasForTestPattern.OuterMethodCallAnalyzerForTryCatch_v1;
import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Collectors;

public class TryCatchPatternFinder_AnyLOC_EA implements PatternFinder {

    //Less confined structure ->

    //Only one try catch block in the test body

    //The closet declaration and expression will be scenario statement and action statement

    //The action and scenario must be contained in the try catch block

    //AnyLOC: Any Order and Any Line Of Code

    //EA: the test body could contain embedded assertions in the catch part of the try catch block

    //Try Catch block need to be decomposed and only useful parts will be extracted

    //Update v3 -> different assertions

    //Update v4 -> test fail statement detection

    //Update v5 -> add constructor analysis

    //Update v6 -> outer methodCall for predicate

    private PsiMethodCallExpression firstSubMethodCall(PsiExpressionStatement expressionStatement){

        if (!expressionStatement.getText().contains("assert")
                || expressionStatement.getChildren().length == 0) return null;

        Optional<PsiMethodCallExpression> methodCallExpressions = Arrays.stream(expressionStatement.getChildren())
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .findAny();

        if (!methodCallExpressions.isPresent()) return null;

        if (methodCallExpressions.get().getChildren().length == 0) return null;

        Optional<PsiExpressionList> expressionList = Arrays.stream(methodCallExpressions.get().getChildren())
                .filter(psiElement -> psiElement instanceof PsiExpressionList)
                .map(PsiExpressionList.class::cast)
                .findAny();

        if (!expressionList.isPresent()) return null;

        if (Arrays.stream(expressionList.get().getExpressions())
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .filter(psiElement -> !psiElement.getText().contains("assert"))
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList()).size() == 0) return null;

        Optional<PsiMethodCallExpression> resultMethodCall = Arrays.stream(expressionList.get().getExpressions())
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .filter(psiElement -> !psiElement.getText().contains("assert"))
                .map(PsiMethodCallExpression.class::cast)
                .findAny();

        return resultMethodCall.orElse(null);

    }

    private List<String> oneTryOnly(PsiTryStatement test_body){

        List<String> results = new LinkedList<>();

        PsiCodeBlock try_block = test_body.getTryBlock();

        List<PsiCodeBlock> catch_blocks = Arrays.asList(test_body.getCatchBlocks());

        if (catch_blocks.size() == 0) return null;

        if (try_block == null || catch_blocks.get(0) == null) return null;

        //Only consider the first catch block

        List<PsiStatement> psiStatements_try = Arrays.asList(try_block.getStatements());

        if (psiStatements_try.size() == 0) return null;

        PsiExpressionStatement action_in_block = null;

        //Extract sub components from the try statements

        if (!psiStatements_try.get(psiStatements_try.size() - 1).getText().contains("fail")) return null;

        for (int i = psiStatements_try.size() - 2; i >= 0; i--) {

            if (psiStatements_try.get(i) instanceof PsiExpressionStatement
                    && !psiStatements_try.get(i).getText().contains("assert")){

                action_in_block = (PsiExpressionStatement) psiStatements_try.get(i);
            }

            if (action_in_block != null) break;
        }

        if (action_in_block == null) return null;

        if (!(action_in_block.getExpression() instanceof PsiMethodCallExpression)) return null;

        PsiMethodCallExpression action = (PsiMethodCallExpression) action_in_block.getExpression();

        results.add(action.getMethodExpression().getReferenceName());

        //Get action from try block

        if (action.getMethodExpression().getQualifierExpression() == null) return null;

        results.add(action.getMethodExpression().getQualifierExpression().getText());

        //Get scenario from try block

        List<PsiStatement> psiStatements_catch = Arrays.asList(catch_blocks.get(0).getStatements());

        if (psiStatements_catch.size() == 0) {

            results.add("N/A");

            return results;
        }

        if (!(psiStatements_catch.get(0) instanceof PsiExpressionStatement)) return null;

        PsiExpressionStatement predicate_in_block = (PsiExpressionStatement) psiStatements_catch.get(0);

        if (firstSubMethodCall(predicate_in_block) == null) return null;

        PsiMethodCallExpression predicate_expression = firstSubMethodCall(predicate_in_block);

        if (!(predicate_in_block.getExpression() instanceof PsiMethodCallExpression)) return null;

        if (predicate_expression == null) return null;

        results.add(predicate_expression.getMethodExpression().getReferenceName());

        //Get the first expression statement in the catch as predicate

        return results;

        //Order: action, scenario, predicate
    }


    private String firstMethodCall(PsiExpressionStatement statement){

        List<PsiMethodCallExpression> methodCalls = Arrays.stream(statement.getChildren())
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());

        if (methodCalls.size() == 0) return "N/A";

        List<PsiMethodCallExpression> inner_methodCalls =
                Arrays.stream(methodCalls.get(0).getArgumentList().getExpressions())
                        .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

        if (inner_methodCalls.size() == 0) return "N/A";

        return inner_methodCalls.get(0).getMethodExpression().getReferenceName();

        //The first method call inside the assertion
    }


    @Override
    public PatternMatch matches(PsiMethod test) {

        PsiMethodCallExpression outer_predicate = new OuterMethodCallAnalyzerForTryCatch_v1()
                .extractMCFromOuterAndMatchedWithName(test.getBody(), test.getName());
        //outer methodCall for predicate

        String action = "";

        String predicate;

        String scenario = "";

        int try_catch_position = -5;

        int counter1 = 0;

        if (test.getBody() == null) return null;

        List<PsiStatement> statements = Arrays.asList(test.getBody().getStatements());

        for (int i = 0; i < statements.size(); i++) {

            if (statements.get(i) instanceof PsiTryStatement){

                try_catch_position = i;

                break;
            }
        }

        for (PsiStatement statement : statements) {

            if (statement instanceof PsiTryStatement) {

                counter1++;
            }
            else if (statement instanceof PsiLoopStatement) {

                counter1++;
            }
            else if (statement instanceof PsiIfStatement) {

                counter1++;
            }
        }
        //Get to exclude more unwanted statements
        //Count the number of try catch blocks

        if (try_catch_position < 0 || counter1 != 1) return null;

        //Pinpoint the position of the try catch block

        PsiDeclarationStatement scenarioStatement_outside = null;

        PsiExpressionStatement actionStatement_outside = null;

        PsiExpressionStatement predicateStatement = null;

        PsiTryStatement tryCatchStatement = (PsiTryStatement) statements.get(try_catch_position);

        if (tryCatchStatement == null) return null;

        new NestedStatementsFilter_v1();

        if (NestedStatementsFilter_v1.checkTryCatch(tryCatchStatement)) return null;

        //Get to check nested statement

        for (int i = try_catch_position - 1; i >= 0; i--) {

            if (statements.get(i) instanceof PsiExpressionStatement
                    && actionStatement_outside == null
                    && !statements.get(i).getText().contains("assert")){

                actionStatement_outside = (PsiExpressionStatement) statements.get(i);
            }

            if (statements.get(i) instanceof PsiDeclarationStatement
                    && scenarioStatement_outside == null){

                scenarioStatement_outside = (PsiDeclarationStatement) statements.get(i);
            }
        }

        for (PsiStatement statement: statements) {

            if (statement instanceof PsiExpressionStatement
                    && predicateStatement == null
                    && statement.getText().contains("assert")){

                predicateStatement = (PsiExpressionStatement) statement;

                //First assertion outside
            }
        }

        if (actionStatement_outside == null && scenarioStatement_outside == null) {

            List<String> results = oneTryOnly(tryCatchStatement);

            if (results == null) return null;

            return new PatternMatch() {

                @Override
                public String getPatternName() {
                    return "TryCatchPattern_oneTryOnly";
                }

                @Override
                public String getAction() {
                    return results.get(0);
                }

                @Override
                public String getPredicate() {
                    return results.get(2);
                }

                @Override
                public String getScenario() {
                    return results.get(1);
                }
            };
        }

        //Get all three statements: action, scenario and try catch

        PsiCodeBlock try_block = tryCatchStatement.getTryBlock();

        List<PsiCodeBlock> catch_blocks = Arrays.asList(tryCatchStatement.getCatchBlocks());

        if (catch_blocks.size() == 0) return null;

        if (try_block == null || catch_blocks.get(0) == null) return null;

        //Only consider the first catch block

        List<PsiStatement> psiStatements_try = Arrays.asList(try_block.getStatements());

        PsiStatement[] psiStatements_catch = catch_blocks.get(0).getStatements();

        PsiExpressionStatement action_in_block = null;

        PsiExpressionStatement predicate_in_block = null;

        //Extract sub components from the try statements

        if (!psiStatements_try.get(psiStatements_try.size() - 1).getText().contains("fail")) return null;

        for (int i = psiStatements_try.size() - 2; i >= 0; i--) {

            if (psiStatements_try.get(i) instanceof PsiExpressionStatement
                    && !psiStatements_try.get(i).getText().contains("assert")){

                action_in_block = (PsiExpressionStatement) psiStatements_try.get(i);
            }

            if (action_in_block != null) break;
        }

        if (action_in_block == null) return null;

        //Get action from try block

        for (PsiStatement statement:
                psiStatements_catch) {

            if (statement instanceof PsiExpressionStatement
                    && statement.getText().contains("assert")){


                predicate_in_block = (PsiExpressionStatement) statement;
            }

            if (predicate_in_block != null) break;

            //First assertion inside the try catch block
        }

        if (predicateStatement == null && predicate_in_block == null){

            predicate = "N/A";
        }
        else if (predicateStatement == null){

            predicate = firstMethodCall(predicate_in_block) + "(inner)";
        }
        else if (predicate_in_block == null){

            predicate = firstMethodCall(predicateStatement) + "(outer)";
        }
        else{

            String pre1 = firstMethodCall(predicateStatement);

            String pre2 = firstMethodCall(predicate_in_block);

            if (Objects.equals(pre1, pre2)){

                predicate = pre1 + "(matched)";
            }
            else{

                predicate = pre2 + "(unmatched)";
            }
        }
        //Settle predicate

        if (actionStatement_outside != null){

            if (!(actionStatement_outside.getExpression() instanceof PsiMethodCallExpression)
                    || !(action_in_block.getExpression() instanceof PsiMethodCallExpression)) return null;

            String action_outside = ((PsiMethodCallExpression)actionStatement_outside.getExpression())
                    .getMethodExpression().getReferenceName();

            String action_inside = ((PsiMethodCallExpression)action_in_block.getExpression())
                    .getMethodExpression().getReferenceName();

            if (Objects.equals(action_inside, action_outside)) action = action_inside + "(matched)";

            if (!Objects.equals(action_inside, action_outside)) action = action_inside + "(unmatched)";

            PsiMethodCallExpression first_method_call =
                    (PsiMethodCallExpression) action_in_block.getFirstChild();

            if (first_method_call.getMethodExpression().getQualifierExpression() == null) {

                scenario = "N/A";

            } else {

                scenario = first_method_call.getMethodExpression().getQualifierExpression().getText();
            }
        }
        else {

            if (!(action_in_block.getExpression() instanceof PsiMethodCallExpression)) return null;

            action = ((PsiMethodCallExpression)action_in_block.getExpression())
                    .getMethodExpression().getReferenceName()+ "(oneMC)";
        }

        //Settle action

        if (scenarioStatement_outside != null && !scenario.equals("")) {

            PsiLocalVariable first_element = (PsiLocalVariable) scenarioStatement_outside.getDeclaredElements()[0];

            if (Objects.equals(first_element.getName(), scenario)){

                scenario = scenario + "(matched - new object)";
            }
            else{

                scenario = scenario + "(unmatched - new object)";
            }
        }

        if (scenarioStatement_outside != null
                && scenario.equals("")
                && scenarioStatement_outside.getDeclaredElements()[0] instanceof PsiLocalVariable){

            PsiLocalVariable first_element = (PsiLocalVariable) scenarioStatement_outside.getDeclaredElements()[0];

            scenario = (first_element).getName() + "(outer1)";
        }

        //Add fix for class casting, Dec 21.

        //Condition 1: Settle scenario with comparing scenario outside and inside the try catch block

        if (scenarioStatement_outside == null) {

            PsiExpression scenario_outside = ((PsiMethodCallExpression) actionStatement_outside.getExpression())
                    .getMethodExpression().getQualifierExpression();

            PsiExpression scenario_inside = ((PsiMethodCallExpression) action_in_block.getExpression())
                    .getMethodExpression().getQualifierExpression();

            if (scenario_inside == null || scenario_outside == null) {

                if(scenario_inside !=  null){

                    scenario = scenario_inside.getText() + "(inner2)";
                }
                else if(scenario_outside != null){

                    scenario = scenario_outside.getText() + "(outer2)";
                }
                else {
                    scenario = "N/A";
                }
            }
            else{
                if (Objects.equals(scenario_outside.getText(), scenario_inside.getText())) {

                    scenario = scenario_inside.getText() + "(matched - actionReference)";
                }
                else {

                    scenario = scenario_inside.getText() + "(unmatched - actionReference)";
                }
            }

        }

        //Condition 2: Settle scenario without scenario statement from outside of the try catch block

        String finalAction = action;

        String finalScenario = scenario;

        if (predicate.contains("N/A") && tryCatchStatement.getFinallyBlock() != null){

            PsiCodeBlock finallyBlock = tryCatchStatement.getFinallyBlock();

            for (PsiStatement statement : finallyBlock.getStatements()){

                if (statement instanceof PsiExpressionStatement && statement.getText().contains("assert")){

                    PsiMethodCallExpression methodCallExpression =
                            firstSubMethodCall((PsiExpressionStatement) statement);

                    if (methodCallExpression == null) continue;

                    predicate = methodCallExpression.getMethodExpression().getReferenceName();

                    if (predicate != null) break;
                }
            }
        }

        String finalPredicate = predicate;

        //End

        return new PatternMatch() {
            @Override
            public String getPatternName() {
                return "TryCatchPattern_AnyLOC_EmbeddedAssert";
            }

            @Override
            public String getAction() {

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

                        return finalAction + " | " +
                                declarationAnalyzer_v2.firstMethodCallMatchedWithName(statement, test.getName())
                                        .getMethodExpression().getReferenceName() + " (Constructor-Based)";
                    }

                    if (methodCallExpressions.size() != 0){

                        return finalAction + " | " +
                                methodCallExpressions.get(0).getMethodExpression().getReferenceName();
                    }
                }
                //Constructor-Based Action

                return finalAction;
            }

            @Override
            public String getPredicate() {

                if (finalPredicate != null && outer_predicate != null && finalPredicate.equals("N/A")) {
                    return outer_predicate.getMethodExpression().getReferenceName() + " (outer_predicate)";
                }

                return finalPredicate;
            }

            @Override
            public String getScenario() {
                return finalScenario;
            }
        };
    }
}
