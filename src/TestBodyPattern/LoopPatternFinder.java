package TestBodyPattern;

import ExtrasForTestPattern.ActionScenarioDetector_v2;
import ExtrasForTestPattern.AssertionHandler_v2;
import ExtrasForTestPattern.NestedStatementsFilter_v1;
import com.intellij.psi.*;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class LoopPatternFinder implements PatternFinder{

    //v2 -> more coverage

    //v3 -> different assertions

    //v4 -> test fail statement detection

    private String action = "";

    private String predicate = "";

    private String scenario = "";

    @Override
    public PatternMatch matches(PsiMethod test) {

        if (test.getBody() == null) return null;

        List<PsiStatement> statements = Arrays.asList((test.getBody()).getStatements());

        if (statements.size() < 1) return null;

        ActionScenarioDetector_v2 actionScenarioDetector_v2 = new ActionScenarioDetector_v2();

        int first_loop = -1;

        int counter = 0;

        for (int i = 0; i < statements.size(); i++) {

            if (statements.get(i) instanceof PsiLoopStatement){

                first_loop = i;

                break;
            }
        }

        for (PsiStatement statement : statements) {

            if (statement instanceof PsiLoopStatement) {

                counter++;
            }
            else if (statement instanceof PsiTryStatement){

                counter++;
            }
            else if (statement instanceof PsiIfStatement){

                counter++;
            }
        }

        if (counter > 1) return null;

        //Get to exclude more unwanted statements

        if (first_loop < 0) return null;

        String action_outer = actionScenarioDetector_v2.fetchAction(test.getBody(), first_loop);

        String scenario_outer = actionScenarioDetector_v2.fetchScenario(test.getBody(), first_loop);

        PsiLoopStatement loopStatement = (PsiLoopStatement) statements.get(first_loop);

        new NestedStatementsFilter_v1();

        if (NestedStatementsFilter_v1.checkLoop(loopStatement)) return null;

        //Get to check nested statements

        if (loopStatement instanceof PsiForStatement){

            PsiForStatement loop = (PsiForStatement) loopStatement;

            if (loop.getBody() == null) return null;

            if (loop.getChildren().length == 0) return null;

            if (loop.getCondition() == null) return null;

            List<PsiElement> subCondition = Arrays
                    .stream((loop.getCondition()).getChildren())
                    .filter(psiElement -> psiElement instanceof PsiReferenceExpression)
                    .collect(Collectors.toList());

            if (subCondition.size() < 2) return null;

            scenario = Objects.requireNonNull(subCondition.get(1).getReference()).getCanonicalText();

            List<PsiCodeBlock> codeBlocks = Arrays.stream((loop.getBody()).getChildren())
                    .filter(psiElement -> psiElement instanceof PsiCodeBlock)
                    .map(PsiCodeBlock.class::cast)
                    .collect(Collectors.toList());

            if (codeBlocks.size() == 0) return null;

            PsiCodeBlock block = codeBlocks.get(0);

            List<PsiElement> subLoop = Arrays.stream(block.getChildren())
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .collect(Collectors.toList());

            if (subLoop.size() == 0) return null;

//            if (subLoop.stream().noneMatch(psiElement -> !psiElement.getText().contains("assert")
//                    && !psiElement.getText().contains("Assert"))) return null;
//
//            if (subLoop.stream().noneMatch(psiElement -> psiElement.getText().contains("assert")
//                    || psiElement.getText().contains("Assert"))) return null;

            Optional<PsiExpressionStatement> first = subLoop.stream()
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .filter(psiElement -> !psiElement.getText().contains("assert")
                            && !psiElement.getText().contains("Assert"))
                    .map(PsiExpressionStatement.class::cast)
                    .findFirst();
            //Get the action inside the loop body -> first

            Optional<PsiExpressionStatement> second = subLoop.stream()
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .filter(psiElement -> psiElement.getText().contains("assert")
                            || psiElement.getText().contains("Assert"))
                    .map(PsiExpressionStatement.class::cast)
                    .findFirst();
            //Get the predicate inside the embedded assertion -> second

            if (!first.isPresent() && !second.isPresent()) return null;

            if (first.isPresent() && !second.isPresent()) {

                List<PsiMethodCallExpression> subFirst = Arrays.stream(first.get().getChildren())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

                if (subFirst.size() == 0) return null;

                action = subFirst.get(0).getMethodExpression().getReferenceName();

                predicate = "N/A";
            }

            else if (!first.isPresent() && second.isPresent()){

                PsiMethodCallExpression predicate_method_call =
                        new AssertionHandler_v2().getFirstEmbeddedMethodCall(second.get());

                action = "N/A";

                if (predicate_method_call == null) return null;

                predicate = predicate_method_call.getMethodExpression().getReferenceName();
            }

            else {

                List<PsiMethodCallExpression> subFirst = Arrays.stream(first.get().getChildren())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

                if (subFirst.size() == 0) return null;

                PsiMethodCallExpression predicate_method_call =
                        new AssertionHandler_v2().getFirstEmbeddedMethodCall(second.get());

                action = subFirst.get(0).getMethodExpression().getReferenceName();

                if (predicate_method_call == null) return null;

                predicate = predicate_method_call.getMethodExpression().getReferenceName();
            }

//            List<PsiMethodCallExpression> subSecond
//                    = Arrays.stream(second.get().getChildren())
//                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                    .map(PsiMethodCallExpression.class::cast)
//                    .collect(Collectors.toList());
//
//            PsiExpressionList methodCallsInAssert = (PsiExpressionList) subSecond.get(0).getChildren()[1];
//
//            List<PsiMethodCallExpression> subSubSecond
//                    = Arrays.stream(methodCallsInAssert.getChildren())
//                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                    .map(PsiMethodCallExpression.class::cast)
//                    .collect(Collectors.toList());
//
//            if (subSubSecond.size() == 0) return null;
//
//            action = subFirst.get(0).getMethodExpression().getReferenceName();
//
//            predicate = subSubSecond.get(0).getMethodExpression().getReferenceName(); -> v2
//
//            predicate = predicate_method_call.getMethodExpression().getReferenceName(); -> v3
        }

        else if (loopStatement instanceof PsiForeachStatement){

            PsiForeachStatement loop = (PsiForeachStatement) loopStatement;

            if (loop.getBody() == null) return null;

            if (loop.getChildren().length == 0) return null;

            scenario = loop.getIterationParameter().getName();

            PsiElement block = (loop.getBody()).getChildren()[0];

            List<PsiElement> subLoop = Arrays.stream(block.getChildren())
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .collect(Collectors.toList());

            if (subLoop.size() == 0) return null;

//            if (subLoop.stream().noneMatch(psiElement -> !psiElement.getText().contains("assert")
//                    && !psiElement.getText().contains("Assert"))) return null;
//
//            if (subLoop.stream().noneMatch(psiElement -> psiElement.getText().contains("assert")
//                    || psiElement.getText().contains("Assert"))) return null;

            Optional<PsiExpressionStatement> first = subLoop.stream()
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .filter(psiElement -> !psiElement.getText().contains("assert")
                            && !psiElement.getText().contains("Assert"))
                    .map(PsiExpressionStatement.class::cast)
                    .findFirst();
            //Get the action inside the loop body -> first

            Optional<PsiExpressionStatement> second = subLoop.stream()
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .filter(psiElement -> psiElement.getText().contains("assert")
                            || psiElement.getText().contains("Assert"))
                    .map(PsiExpressionStatement.class::cast)
                    .findFirst();
            //Get the predicate inside the embedded assertion -> second

            if (!first.isPresent() && !second.isPresent()) return null;

            if (first.isPresent() && !second.isPresent()) {

                List<PsiMethodCallExpression> subFirst = Arrays.stream(first.get().getChildren())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

                if (subFirst.size() == 0) return null;

                action = subFirst.get(0).getMethodExpression().getReferenceName();

                predicate = "N/A";
            }

            else if (!first.isPresent() && second.isPresent()){

                PsiMethodCallExpression predicate_method_call =
                        new AssertionHandler_v2().getFirstEmbeddedMethodCall(second.get());

                action = "N/A";

                if (predicate_method_call == null) return null;

                predicate = predicate_method_call.getMethodExpression().getReferenceName();
            }

            else {

                List<PsiMethodCallExpression> subFirst = Arrays.stream(first.get().getChildren())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

                if (subFirst.size() == 0) return null;

                PsiMethodCallExpression predicate_method_call =
                        new AssertionHandler_v2().getFirstEmbeddedMethodCall(second.get());

                action = subFirst.get(0).getMethodExpression().getReferenceName();

                if (predicate_method_call == null) return null;

                predicate = predicate_method_call.getMethodExpression().getReferenceName();
            }

//            List<PsiMethodCallExpression> subSecond
//                    = Arrays.stream(second.get().getChildren())
//                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                    .map(PsiMethodCallExpression.class::cast)
//                    .collect(Collectors.toList());
//
//            PsiExpressionList methodCallsInAssert = (PsiExpressionList) subSecond.get(0).getChildren()[1];
//
//            List<PsiMethodCallExpression> subSubSecond
//                    = Arrays.stream(methodCallsInAssert.getChildren())
//                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                    .map(PsiMethodCallExpression.class::cast)
//                    .collect(Collectors.toList());

//            if (subSubSecond.size() == 0) return null;

//            action = subFirst.get(0).getMethodExpression().getReferenceName();

//            predicate = subSubSecond.get(0).getMethodExpression().getReferenceName();

//            predicate = predicate_method_call.getMethodExpression().getReferenceName();
        }

        else if (loopStatement instanceof PsiDoWhileStatement){

            PsiDoWhileStatement loop = (PsiDoWhileStatement) loopStatement;

            if (loop.getBody() == null) return null;

            if (loop.getChildren().length == 0) return null;

            if (loop.getCondition() == null) return null;

            List<PsiElement> subCondition = Arrays
                    .stream(Objects.requireNonNull(loop.getCondition()).getChildren())
                    .filter(psiElement -> psiElement instanceof PsiReferenceExpression)
                    .collect(Collectors.toList());

            if (subCondition.size() == 0) return null;

            scenario = Objects.requireNonNull(subCondition.get(0).getReference()).getCanonicalText();

            PsiCodeBlock block = (PsiCodeBlock) Objects.requireNonNull(loop.getBody()).getChildren()[0];

            List<PsiElement> subLoop = Arrays.stream(block.getChildren())
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .collect(Collectors.toList());

            if (subLoop.size() == 0) return null;

//            if (subLoop.stream().noneMatch(psiElement -> !psiElement.getText().contains("assert")
//                    && !psiElement.getText().contains("Assert"))) return null;
//
//            if (subLoop.stream().noneMatch(psiElement -> psiElement.getText().contains("assert")
//                    || psiElement.getText().contains("Assert"))) return null;

            Optional<PsiExpressionStatement> first = subLoop.stream()
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .filter(psiElement -> !psiElement.getText().contains("assert")
                            && !psiElement.getText().contains("Assert"))
                    .map(PsiExpressionStatement.class::cast)
                    .findFirst();
            //Get the action inside the loop body -> first

            Optional<PsiExpressionStatement> second = subLoop.stream()
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .filter(psiElement -> psiElement.getText().contains("assert")
                            || psiElement.getText().contains("Assert"))
                    .map(PsiExpressionStatement.class::cast)
                    .findFirst();
            //Get the predicate inside the embedded assertion -> second

            if (!first.isPresent() && !second.isPresent()) return null;

            if (first.isPresent() && !second.isPresent()) {

                List<PsiMethodCallExpression> subFirst = Arrays.stream(first.get().getChildren())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

                if (subFirst.size() == 0) return null;

                action = subFirst.get(0).getMethodExpression().getReferenceName();

                predicate = "N/A";
            }

            else if (!first.isPresent() && second.isPresent()){

                PsiMethodCallExpression predicate_method_call =
                        new AssertionHandler_v2().getFirstEmbeddedMethodCall(second.get());

                action = "N/A";

                if (predicate_method_call == null) return null;

                predicate = predicate_method_call.getMethodExpression().getReferenceName();
            }

            else {

                List<PsiMethodCallExpression> subFirst = Arrays.stream(first.get().getChildren())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

                if (subFirst.size() == 0) return null;

                PsiMethodCallExpression predicate_method_call =
                        new AssertionHandler_v2().getFirstEmbeddedMethodCall(second.get());

                action = subFirst.get(0).getMethodExpression().getReferenceName();

                if (predicate_method_call == null) return null;

                predicate = predicate_method_call.getMethodExpression().getReferenceName();
            }

//            List<PsiMethodCallExpression> subSecond
//                    = Arrays.stream(second.get().getChildren())
//                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                    .map(PsiMethodCallExpression.class::cast)
//                    .collect(Collectors.toList());
//
//            PsiExpressionList methodCallsInAssert = (PsiExpressionList) subSecond.get(0).getChildren()[1];
//
//            List<PsiMethodCallExpression> subSubSecond
//                    = Arrays.stream(methodCallsInAssert.getChildren())
//                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                    .map(PsiMethodCallExpression.class::cast)
//                    .collect(Collectors.toList());
//
//            action = subFirst.get(0).getMethodExpression().getReferenceName();
//
//            predicate = subSubSecond.get(0).getMethodExpression().getReferenceName();
//
//            predicate = predicate_method_call.getMethodExpression().getReferenceName();
        }

        else if (loopStatement instanceof PsiWhileStatement){

            PsiWhileStatement loop = (PsiWhileStatement) loopStatement;

            if (loop.getBody() == null) return null;

            if (loop.getChildren().length == 0) return null;

            if (loop.getCondition() == null) return null;

            List<PsiElement> subCondition = Arrays
                    .stream(Objects.requireNonNull(loop.getCondition()).getChildren())
                    .filter(psiElement -> psiElement instanceof PsiReferenceExpression)
                    .collect(Collectors.toList());

            if (subCondition.size() == 0) return null;

            scenario = Objects.requireNonNull(subCondition.get(0).getReference()).getCanonicalText();

            PsiCodeBlock block = (PsiCodeBlock) Objects.requireNonNull(loop.getBody()).getChildren()[0];

            List<PsiElement> subLoop = Arrays.stream(block.getChildren())
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .collect(Collectors.toList());

            if (subLoop.size() == 0) return null;

//            if (subLoop.stream().noneMatch(psiElement -> !psiElement.getText().contains("assert")
//                    && !psiElement.getText().contains("Assert"))) return null;
//
//            if (subLoop.stream().noneMatch(psiElement -> psiElement.getText().contains("assert")
//                    || psiElement.getText().contains("Assert"))) return null;

            Optional<PsiExpressionStatement> first = subLoop.stream()
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .filter(psiElement -> !psiElement.getText().contains("assert")
                            && !psiElement.getText().contains("Assert"))
                    .map(PsiExpressionStatement.class::cast)
                    .findFirst();
            //Get the action inside the loop body -> first

            Optional<PsiExpressionStatement> second = subLoop.stream()
                    .filter(psiElement -> psiElement instanceof PsiExpressionStatement)
                    .filter(psiElement -> psiElement.getText().contains("assert")
                            || psiElement.getText().contains("Assert"))
                    .map(PsiExpressionStatement.class::cast)
                    .findFirst();
            //Get the predicate inside the embedded assertion -> second

            if (!first.isPresent() && !second.isPresent()) return null;

            if (first.isPresent() && !second.isPresent()) {

                List<PsiMethodCallExpression> subFirst = Arrays.stream(first.get().getChildren())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

                if (subFirst.size() == 0) return null;

                action = subFirst.get(0).getMethodExpression().getReferenceName();

                predicate = "N/A";
            }

            else if (!first.isPresent() && second.isPresent()){

                PsiMethodCallExpression predicate_method_call =
                        new AssertionHandler_v2().getFirstEmbeddedMethodCall(second.get());

                action = "N/A";

                if (predicate_method_call == null) return null;

                predicate = predicate_method_call.getMethodExpression().getReferenceName();
            }

            else {

                List<PsiMethodCallExpression> subFirst = Arrays.stream(first.get().getChildren())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

                if (subFirst.size() == 0) return null;

                PsiMethodCallExpression predicate_method_call =
                        new AssertionHandler_v2().getFirstEmbeddedMethodCall(second.get());

                action = subFirst.get(0).getMethodExpression().getReferenceName();

                if (predicate_method_call == null) return null;

                predicate = predicate_method_call.getMethodExpression().getReferenceName();
            }

//            List<PsiMethodCallExpression> subSecond
//                    = Arrays.stream(second.get().getChildren())
//                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                    .map(PsiMethodCallExpression.class::cast)
//                    .collect(Collectors.toList());
//
//            PsiExpressionList methodCallsInAssert = (PsiExpressionList) subSecond.get(0).getChildren()[1];
//
//            List<PsiMethodCallExpression> subSubSecond
//                    = Arrays.stream(methodCallsInAssert.getChildren())
//                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                    .map(PsiMethodCallExpression.class::cast)
//                    .collect(Collectors.toList());
//
//            action = subFirst.get(0).getMethodExpression().getReferenceName();
//
//            predicate = subSubSecond.get(0).getMethodExpression().getReferenceName();
//
//            predicate = predicate_method_call.getMethodExpression().getReferenceName();
        }

        if (action_outer != null){

            if (Objects.equals(action_outer, action)){

                action = action + "(matched)";
            }
            else {

                action = action + "(unmatched with outside)";
            }
        }

        if (scenario_outer != null){

            if (Objects.equals(scenario_outer, scenario)){

                scenario = scenario + "(matched)";
            }
            else {

                scenario = scenario + "(unmatched with outside)";
            }
        }
        //End of analysis

        String finalScenario = scenario;

        String finalPredicate = predicate;

        String finalAction = action;

        //End: Finalize all variables

        return new PatternMatch() {

            @Override
            public String getPatternName() {
                return "LoopPattern";
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
