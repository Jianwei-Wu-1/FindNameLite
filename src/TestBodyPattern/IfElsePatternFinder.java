package TestBodyPattern;

import ExtrasForTestPattern.ActionScenarioDetector_v2;
import ExtrasForTestPattern.NestedStatementsFilter_v1;
import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Collectors;

public class IfElsePatternFinder implements PatternFinder{

    //Update v2 -> detailed match

    //Update v3 -> different assertions

    //Update v3.1 -> exclude other statements

    //Add support for incomplete if else statements

    //Update v4 -> test fail statement detection

    Object[] CrypticalIfElseStatement(PsiConditionalExpression element){
        //Todo: Integrate this part to if else pattern in next update
        return Arrays.stream(element.getChildren()).filter(p -> p instanceof PsiExpression).toArray();
    }

    @Override
    public PatternMatch matches(PsiMethod test) {

        if (test.getBody() == null) return null;

        List<PsiStatement> statements = Arrays.asList((test.getBody()).getStatements());

        if (statements.size() < 2) return null;

        ActionScenarioDetector_v2 actionScenarioDetector_v2 = new ActionScenarioDetector_v2();

        int first_if_else = -1;

        int counter = 0;

        for (int i = 0; i < statements.size(); i++) {
            if (statements.get(i) instanceof PsiIfStatement){
                first_if_else = i;
                break;
            }
        }

        for (PsiStatement statement1 : statements) {

            if (statement1 instanceof PsiIfStatement) {
                counter++;
            }
            else if (statement1 instanceof PsiTryStatement) {
                counter++;
            }
            else if (statement1 instanceof PsiLoopStatement) {
                counter++;
            }
        }

        if (counter > 1) return null;

        //Get to exclude more unwanted statements

        String action_outer = actionScenarioDetector_v2.fetchAction(test.getBody(), first_if_else);

        PsiDeclarationStatement first_declaration = actionScenarioDetector_v2.fetchFirstDeclarationStatement(test.getBody(), first_if_else);

        if (first_declaration == null) return null;

        if (first_if_else < 0) return null;

        PsiIfStatement ifElseStatement = (PsiIfStatement) statements.get(first_if_else);

        new NestedStatementsFilter_v1();

        if (NestedStatementsFilter_v1.checkIfElse(ifElseStatement)) return null;

        //Get to check nested statements

        if (ifElseStatement.getThenBranch() == null) return null;

        if (ifElseStatement.getThenBranch().getChildren()[0] instanceof PsiCodeBlock) {

            if (((PsiCodeBlock) ifElseStatement.getThenBranch().getChildren()[0]).getStatements().length == 0
                    && ifElseStatement.getElseBranch() != null) {

                String scenario_outer = actionScenarioDetector_v2.fetchScenario(test.getBody(), first_if_else);

                //No need for inner action and scenario

                PsiCodeBlock subElse = (PsiCodeBlock) (ifElseStatement.getElseBranch()).getChildren()[0];

                String predicate = "";

                for (PsiElement statement : subElse.getStatements()) {

                    if (!(statement instanceof PsiExpressionStatement)) continue;

                    if (!(statement.getText().contains("assert"))) continue;

                    PsiExpressionStatement predicate_expression = (PsiExpressionStatement) statement;

                    PsiElement[] expressionChildren = predicate_expression.getChildren();

                    PsiMethodCallExpression actual_predicate = null;

                    //Find the first statement which is a expression and it could be a possible assertion

                    for (PsiElement element : expressionChildren) {

                        if (!(element instanceof PsiMethodCallExpression)) continue;

                        if (!element.getText().contains("assert")) continue;

                        PsiElement[] sub_exp = element.getChildren();

                        //Get the first assert part in the else body (Exclude elements like ";")

                        for (PsiElement element1 : sub_exp) {

                            if (!(element1 instanceof PsiExpressionList)) continue;

                            PsiExpressionList cur_list = (PsiExpressionList) element1;

                            List<PsiExpression> callExpressions = Arrays.stream(cur_list.getExpressions())
                                    .filter(p -> p instanceof PsiMethodCallExpression)
                                    .collect(Collectors.toList());

                            if (callExpressions.size() == 0) return null;

                            if (callExpressions.get(0) == null) return null;

                            actual_predicate = (PsiMethodCallExpression) callExpressions.get(0);

                            //Find all embedded method call in the assertion

                        }
                    }

                    if (actual_predicate == null) return null;

                    predicate = (actual_predicate).getMethodExpression().getReferenceName();

                    if (predicate != null) break;

                }

                //Get Predicate

                String finalPredicate = predicate;

                return new PatternMatch() {

                    @Override
                    public String getPatternName() {
                        return "IfElsePattern_EmptyIfStatement";
                    }

                    @Override
                    public String getAction() {

                        if (action_outer == null) {

                            return "N/A (outer)";
                        }

                        return action_outer + "(outer)";
                    }

                    @Override
                    public String getPredicate() {
                        return finalPredicate;
                    }

                    @Override
                    public String getScenario() {
                        return scenario_outer + "(outer)";
                    }
                };
            }
        }

        //Get empty if statement case

        if (ifElseStatement.getElseBranch() == null) {

            if (ifElseStatement.getCondition() == null) return null;

            String ifCondition = (ifElseStatement.getCondition()).getText();

            String scenario_outer = actionScenarioDetector_v2.fetchScenario(test.getBody(), first_if_else);

            //Get the first local variable

            if (scenario_outer == null) return null;

            if (!ifCondition.contains((scenario_outer))) return null;

            List<PsiCodeBlock> codeBlocks = Arrays.stream(ifElseStatement.getThenBranch().getChildren())
                    .filter(psiElement -> psiElement instanceof PsiCodeBlock)
                    .map(PsiCodeBlock.class::cast)
                    .collect(Collectors.toList());

            if (codeBlocks.size() == 0) return null;

            PsiCodeBlock subIf = codeBlocks.get(0);

            //Get the first code block

            String action_inner = "";

            String scenario_inner = "";

            for (PsiStatement statement : subIf.getStatements()) {

                if (!(statement instanceof PsiExpressionStatement)) continue;

                PsiExpressionStatement action_expression = (PsiExpressionStatement) statement;

                PsiElement[] exp = action_expression.getChildren();

                PsiMethodCallExpression actual_action = null;

                for (PsiElement element : exp) {

                    if (!(element instanceof PsiMethodCallExpression)) continue;

                    if (element.getText().contains("Assert") || element.getText().contains("assert")) continue;

                    actual_action = (PsiMethodCallExpression) element;
                }

                if (actual_action == null) continue;

                action_inner = (actual_action).getMethodExpression().getReferenceName();

                if (actual_action.getMethodExpression().getQualifierExpression() == null) continue;

                scenario_inner = actual_action.getMethodExpression().getQualifierExpression().getText();

                if (action_inner != null) break;

                //Get the first method call from the first expression statement in the if body
            }

            if (action_outer != null) {

                if (Objects.equals(action_inner, action_outer)) {

                    action_inner = action_inner + "(matched)";
                } else {

                    action_inner = action_inner + "(unmatched)";
                }
            }

            String scenario = "";

            if (scenario_inner != null) {

                if (Objects.equals(scenario_inner, scenario_outer)) {

                    scenario = scenario_outer + "(full matched)";

                    //Match all elements (include embedded ones in the if body)
                } else {

                    scenario = scenario_outer + "(partial unmatched)";

                    //Match up to the condition of the first if else statement
                    //But it's not matching elements inside the if body
                }
            }

            //Readme: The matching process for scenario is checking if the object in the if block->
            //Readme: is matching to the object used in the if condition

            String finalAction = action_inner;

            String finalScenario = scenario;

            return new PatternMatch() {

                @Override
                public String getPatternName() {
                    return "IfElsePattern_NoElseStatement";
                }

                @Override
                public String getAction() {
                    return finalAction;
                }

                @Override
                public String getPredicate() {
                    return "N/A";
                }

                @Override
                public String getScenario() {
                    return finalScenario;
                }
            };
        }

        //Get no else statement case

        if (ifElseStatement.getElseBranch().getChildren()[0] instanceof PsiCodeBlock
                || ifElseStatement.getElseBranch() == null) {

            if (((PsiCodeBlock) (ifElseStatement.getElseBranch().getChildren()[0])).getStatements().length == 0
                    && ifElseStatement.getThenBranch() != null) {

                if (ifElseStatement.getCondition() == null) return null;

                String ifCondition = (ifElseStatement.getCondition()).getText();

                String scenario_outer = actionScenarioDetector_v2.fetchScenario(test.getBody(), first_if_else);

                //Get the first local variable

                if (scenario_outer == null) return null;

                if (!ifCondition.contains((scenario_outer))) return null;

                PsiCodeBlock subIf = (PsiCodeBlock) (ifElseStatement.getThenBranch()).getChildren()[0];

                String action_inner = "";

                String scenario_inner = "";

                for (PsiStatement statement : subIf.getStatements()) {

                    if (!(statement instanceof PsiExpressionStatement)) continue;

                    PsiExpressionStatement action_expression = (PsiExpressionStatement) statement;

                    PsiElement[] exp = action_expression.getChildren();

                    PsiMethodCallExpression actual_action = null;

                    for (PsiElement element : exp) {

                        if (!(element instanceof PsiMethodCallExpression)) continue;

                        if (element.getText().contains("Assert") || element.getText().contains("assert")) continue;

                        actual_action = (PsiMethodCallExpression) element;
                    }

                    if (actual_action == null) continue;

                    action_inner = (actual_action).getMethodExpression().getReferenceName();

                    if (actual_action.getMethodExpression().getQualifierExpression() == null) continue;

                    scenario_inner = actual_action.getMethodExpression().getQualifierExpression().getText();

                    if (action_inner != null) break;

                    //Get the first method call from the first expression statement in the if body
                }

                if (action_outer != null) {

                    if (Objects.equals(action_inner, action_outer)) {

                        action_inner = action_inner + "(matched)";
                    } else {

                        action_inner = action_inner + "(unmatched)";
                    }
                }

                String scenario = "";

                if (scenario_inner != null) {

                    if (Objects.equals(scenario_inner, scenario_outer)) {

                        scenario = scenario_outer + "(full matched)";

                        //Match all elements (include embedded ones in the if body)
                    } else {

                        scenario = scenario_outer + "(partial unmatched)";

                        //Match up to the condition of the first if else statement
                        //But it's not matching elements inside the if body
                    }
                }

                //Readme: The matching process for scenario is checking if the object in the if block->
                //Readme: is matching to the object used in the if condition

                String finalAction = action_inner;

                String finalScenario = scenario;

                return new PatternMatch() {

                    @Override
                    public String getPatternName() {
                        return "IfElsePattern_EmptyElseStatement";
                    }

                    @Override
                    public String getAction() {
                        return finalAction;
                    }

                    @Override
                    public String getPredicate() {
                        return "N/A";
                    }

                    @Override
                    public String getScenario() {
                        return finalScenario;
                    }
                };
            }
        }
        //Get empty else statement case

        //Readme: -> Support incomplete statement

        if (ifElseStatement.getCondition() == null) return null;

        String ifCondition = (ifElseStatement.getCondition()).getText();

        String scenario_outer = actionScenarioDetector_v2.fetchScenario(test.getBody(), first_if_else);

        //Get the first local variable

        if (scenario_outer == null) return null;

        if (!ifCondition.contains((scenario_outer))) return null;

        List<PsiCodeBlock> codeBlocks = Arrays.stream((ifElseStatement.getThenBranch()).getChildren())
                .filter(psiElement -> psiElement instanceof PsiCodeBlock)
                .map(PsiCodeBlock.class::cast)
                .collect(Collectors.toList());

        if (codeBlocks.size() == 0) return null;

        PsiCodeBlock subIf = codeBlocks.get(0);

        String action_inner = "";

        String scenario_inner = "";

        for (PsiStatement statement : subIf.getStatements()){

            if (!(statement instanceof PsiExpressionStatement)) continue;

            PsiExpressionStatement action_expression = (PsiExpressionStatement) statement;

            PsiElement[] exp = action_expression.getChildren();

            PsiMethodCallExpression actual_action = null;

            for (PsiElement element : exp){

                if (!(element instanceof PsiMethodCallExpression)) continue;

                if (element.getText().contains("Assert") || element.getText().contains("assert")) continue;

                actual_action = (PsiMethodCallExpression) element;
            }

            if (actual_action == null) continue;

            action_inner = (actual_action).getMethodExpression().getReferenceName();

            if ( actual_action.getMethodExpression().getQualifierExpression() == null) continue;

            scenario_inner = actual_action.getMethodExpression().getQualifierExpression().getText();

            if (action_inner != null) break;

            //Get the first method call from the first expression statement in the if body
            //Get the object/variable related to the first method call as the "scenario"
        }

        //Completed to find the action_inner of the test

        List<PsiCodeBlock> subElse_blocks = Arrays.stream((ifElseStatement.getElseBranch()).getChildren())
                .filter(psiElement -> psiElement instanceof PsiCodeBlock)
                .map(PsiCodeBlock.class::cast)
                .collect(Collectors.toList());

        PsiCodeBlock subElse = subElse_blocks.get(0);

        String predicate = "";

        int num_of_assertion = 0;

        for (PsiElement statement : subElse.getStatements()){

            if (!(statement instanceof PsiExpressionStatement)) continue;

            if (!(statement.getText().contains("assert"))) continue;

            num_of_assertion++;

            PsiExpressionStatement predicate_expression = (PsiExpressionStatement) statement;

            PsiElement[] expressionChildren = predicate_expression.getChildren();

            PsiMethodCallExpression actual_predicate = null;

            //Find the first statement which is a expression and it could be a possible assertion

            for (PsiElement element : expressionChildren){

                if (!(element instanceof PsiMethodCallExpression)) continue;

                if (!element.getText().contains("assert")) continue;

                PsiElement[] sub_exp = element.getChildren();

                //Get the first assert part in the else body (Exclude elements like ";")

                for (PsiElement element1 : sub_exp){

                    if (!(element1 instanceof PsiExpressionList)) continue;

                    PsiExpressionList cur_list = (PsiExpressionList) element1;

                    List<PsiExpression> callExpressions = Arrays.stream(cur_list.getExpressions())
                            .filter(p -> p instanceof PsiMethodCallExpression)
                            .collect(Collectors.toList());

                    if (callExpressions.size() == 0) return null;

                    if (callExpressions.get(0) == null) return null;

                    actual_predicate = (PsiMethodCallExpression) callExpressions.get(0);

                    //Find all embedded method call in the assertion

                }
            }

            if (actual_predicate == null) return null;

            predicate = (actual_predicate).getMethodExpression().getReferenceName();

            if (predicate != null) break;

        }

        if (num_of_assertion == 0) {

            if (action_outer != null) {

                if (Objects.equals(action_inner, action_outer)) {

                    action_inner = action_inner + "(matched)";
                } else {

                    action_inner = action_inner + "(unmatched)";
                }
            }

            String scenario = "";

            if (scenario_inner!= null) {

                if (Objects.equals(scenario_inner, scenario_outer)) {

                    scenario = scenario_outer + "(full matched)";

                    //Match all elements (include embedded ones in the if body)
                } else {

                    scenario = scenario_outer + "(partial unmatched)";

                    //Match up to the condition of the first if else statement
                    //But it's not matching elements inside the if body
                }
            }

            String finalScenario = scenario;

            String finalAction_inner = action_inner;

            return new PatternMatch() {
                @Override
                public String getPatternName() {
                    return "IfElsePattern_NoPredicate";
                }

                @Override
                public String getAction() {
                    return finalAction_inner;
                }

                @Override
                public String getPredicate() {
                    return "N/A";
                }

                @Override
                public String getScenario() {
                    return finalScenario;
                }
            };
        }

        //For "no predicate" case and completed to find the predicate of the test

        if (action_outer != null) {
            
            if (Objects.equals(action_inner, action_outer)) {

                action_inner = action_inner + "(matched)";
            } else {

                action_inner = action_inner + "(unmatched)";
            }
        }

        String scenario = "";

        if (scenario_inner!= null) {
            
            if (Objects.equals(scenario_inner, scenario_outer)) {

                scenario = scenario_outer + "(full matched)";

                //Match all elements (include embedded ones in the if body)
            } else {

                scenario = scenario_outer + "(partial unmatched)";

                //Match up to the condition of the first if else statement
                //But it's not matching elements inside the if body
            }
        }

        //Readme: The matching process for scenario is checking if the object in the if block->
        //Readme: is matching to the object used in the if condition

        String finalAction = action_inner;

        String finalPredicate = predicate;

        String finalScenario = scenario;

        //Finalize results
        return new PatternMatch() {

            @Override
            public String getPatternName() {
                return "IfElsePattern_v3";
            }

            @Override
            public String getAction() {
                return finalAction;
            }

            @Override
            public String getPredicate() {
                return finalPredicate;
            }

            @Override
            public String getScenario() {
                return finalScenario;
            }
        };
    }

}
