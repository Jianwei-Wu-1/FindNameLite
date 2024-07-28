package TestBodyPattern;

import ExtrasForTestPattern.DeclarationAnalyzer_v2;
import ExtrasForTestPattern.NestedStatementsFilter_v1;
import ExtrasForTestPattern.OuterMethodCallAnalyzerForTryCatch_v1;
import com.intellij.psi.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class TryCatchPatternFinder implements PatternFinder {

    //Confined structure:

    //First line -> Scenario statement

    //Second line -> Try catch block

    //Update v3 -> different assertions

    //Update v4 -> test fail statement detection

    //Update v5 -> add constructor analysis

    //Update v6 -> outer methodCall for predicate

    @Override
    public PatternMatch matches(PsiMethod test) {

        PsiMethodCallExpression outer_predicate = new OuterMethodCallAnalyzerForTryCatch_v1()
                .extractMCFromOuterAndMatchedWithName(test.getBody(), test.getName());
        //outer methodCall for predicate

        if (test.getBody() == null) return null;

        List<PsiStatement> statements = Arrays.asList((test.getBody()).getStatements());

        if (statements.size() != 2) return null;

        if (!(statements.get(0) instanceof PsiDeclarationStatement)) return null;

        if (!(statements.get(1) instanceof PsiTryStatement)) return null;

        PsiDeclarationStatement firstLine = (PsiDeclarationStatement) statements.get(0);

        PsiTryStatement tryCatchStatement = (PsiTryStatement) statements.get(1);

        new NestedStatementsFilter_v1();

        if (NestedStatementsFilter_v1.checkTryCatch(tryCatchStatement)) return null;

        //Get to check nested statement

        //Get the most primitive and confined version of try catch pattern

        List<PsiLocalVariable> localVariables = Arrays.stream(firstLine.getDeclaredElements())
                .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                .map(PsiLocalVariable.class::cast)
                .collect(Collectors.toList());

        PsiLocalVariable scenario = null;

        if (localVariables.size() != 0) {
             scenario = localVariables.get(0);
        }
        //Get scenario

        PsiCodeBlock tryBlock = tryCatchStatement.getTryBlock();

        PsiCodeBlock[] catchBlocks = tryCatchStatement.getCatchBlocks();

        //Get try and catch

        String action = "";

        String predicate = "";

        if (tryBlock != null) {

            List<PsiExpressionStatement> tryMethodCalls = Arrays.stream(tryBlock.getStatements())
                    .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                    .map(PsiExpressionStatement.class::cast)
                    .collect(Collectors.toList());

            if (tryMethodCalls.size() != 2) return null;

            PsiExpressionStatement first = tryMethodCalls.get(0);

            PsiExpressionStatement second = tryMethodCalls.get(1);

            List<PsiMethodCallExpression> subFirst = Arrays.stream(first.getChildren())
                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                    .map(PsiMethodCallExpression.class::cast)
                    .collect(Collectors.toList());

            List<PsiMethodCallExpression> subSecond = Arrays.stream(second.getChildren())
                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                    .map(PsiMethodCallExpression.class::cast)
                    .collect(Collectors.toList());

            if (subFirst.size() == 0 || subSecond.size() == 0) return null;

            //Get the first expression and the second expression from the try block

            if (!Objects.equals(subSecond.get(0).getMethodExpression().getReferenceName(), "fail")) return null;

            action =  subFirst.get(0).getMethodExpression().getReferenceName();

            //Get action from the try block (the first method call)
        }

        if (catchBlocks[0] != null){

            List<PsiExpressionStatement> catchMethodCalls = Arrays.stream(catchBlocks[0].getStatements())
                    .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                    .map(PsiExpressionStatement.class::cast)
                    .collect(Collectors.toList());

            if (catchMethodCalls.size() < 1) {

                String finalAction1 = action;

                PsiLocalVariable finalScenario = scenario;
                return  new PatternMatch() {
                    @Override
                    public String getPatternName() {
                        return "TryCatchPattern";
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

                                return finalAction1 + " | " +
                                        declarationAnalyzer_v2.firstMethodCallMatchedWithName(statement, test.getName())
                                                .getMethodExpression().getReferenceName() + " (Constructor-Based)";
                            }

                            if (methodCallExpressions.size() != 0){

                                return finalAction1 + " | " +
                                        methodCallExpressions.get(0).getMethodExpression().getReferenceName();
                            }
                        }
                        //Constructor-Based Action

                        return finalAction1;
                    }

                    @Override
                    public String getPredicate() {
                        return "N/A";
                    }

                    @Override
                    public String getScenario() {
                        if(finalScenario != null) {
                            return finalScenario.getName();
                        }
                        else{
                            return "N/A";
                        }
                    }
                };
            }

            PsiExpressionStatement first = catchMethodCalls.get(0);

            List<PsiMethodCallExpression> subFirst
                    = Arrays.stream(first.getChildren())
                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                    .map(PsiMethodCallExpression.class::cast)
                    .collect(Collectors.toList());

            if (subFirst.size() == 0) return null;

            predicate =  subFirst.get(0).getMethodExpression().getReferenceName();

            //Get predicate from the first catch block

        }

        String finalAction2 = action;

        String finalPredicate = predicate;

        //Finalize all

        PsiLocalVariable finalScenario1 = scenario;
        return new PatternMatch() {

            @Override
            public String getPatternName() {
                return "TryCatchPattern";
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

                        return finalAction2 + " | " +
                                declarationAnalyzer_v2.firstMethodCallMatchedWithName(statement, test.getName())
                                        .getMethodExpression().getReferenceName() + " (Constructor-Based)";
                    }

                    if (methodCallExpressions.size() != 0){

                        return finalAction2 + " | " +
                                methodCallExpressions.get(0).getMethodExpression().getReferenceName();
                    }
                }
                //Constructor-Based Action

                return finalAction2;
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
                if(finalScenario1 != null) {
                    return finalScenario1.getName();
                }
                else{
                    return "N/A";
                }
            }
        };
    }
}
