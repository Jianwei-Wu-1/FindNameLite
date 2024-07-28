package ExtrasForTestPattern;

import TestNamePattern.RegexForTestNamePattern_v1_1Kt;
import com.intellij.psi.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

//v1 -> Analyze Declaration Statements and Extract All Embedded Method Invocations

//v2 -> adjustments and add to Normal Pattern, No Assertion Pattern, and try-catch pattern

public class DeclarationAnalyzer_v2 {

    public List<PsiMethodCallExpression> extractAllEmbeddedMethodCalls(List<PsiLocalVariable> inputs){

        List<PsiMethodCallExpression> methodCallExpressions = new LinkedList<>();

        if (inputs.size() == 0) return null;

        PsiLocalVariable localVariable = inputs.get(0);

        if (!localVariable.hasInitializer()) return null;

        PsiExpression right_side_expression = localVariable.getInitializer();

        if (right_side_expression == null) return null;

        if (right_side_expression.getChildren().length == 0) return null;

        for(PsiElement element : right_side_expression.getChildren()){

            if (!(element instanceof PsiExpressionList)) continue;

            PsiExpressionList expressionList = (PsiExpressionList) element;

            if (expressionList.getExpressions().length != 0) {

                methodCallExpressions = Arrays.stream(expressionList.getExpressions())
                        .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());
            }
        }
        return methodCallExpressions;
    }

    public List<PsiMethodCallExpression> extractMethodCallsFromConstructor(PsiDeclarationStatement statement){

        List<PsiLocalVariable> localVariables = Arrays.stream(statement.getDeclaredElements())
                .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                .map(PsiLocalVariable.class::cast)
                .collect(Collectors.toList());

        if (localVariables.size() == 0) return null;

        List<PsiMethodCallExpression> result = new LinkedList<>();

        localVariables.forEach(local -> {

            if (local.hasInitializer()){

                PsiExpression expression = local.getInitializer();

                if (expression instanceof PsiMethodCallExpression){

                    PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expression;

                    if (methodCallExpression.getArgumentList().getExpressions().length != 0){

                        for (PsiExpression inner_exp : methodCallExpression.getArgumentList().getExpressions()){

                            if (!(inner_exp instanceof PsiMethodCallExpression)) continue;

                            result.add((PsiMethodCallExpression) inner_exp);
                        }
                    }
                }
            }
        });
        return result;
    }

    public PsiMethodCallExpression firstMethodCallMatchedWithName(PsiDeclarationStatement statement, String name){

        List<PsiLocalVariable> localVariables = Arrays.stream(statement.getDeclaredElements())
                .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                .map(PsiLocalVariable.class::cast)
                .collect(Collectors.toList());

        if (localVariables.size() == 0) return null;

        String parsed_name = RegexForTestNamePattern_v1_1Kt.parseName(name);

        List<String> words_from_name = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_name));

        words_from_name = words_from_name.stream().map(String::toLowerCase).collect(Collectors.toList());

        for (PsiLocalVariable local : localVariables) {
            if (local.hasInitializer()) {

                PsiExpression expression = local.getInitializer();

                if (expression instanceof PsiMethodCallExpression) {

                    PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expression;

                    if (methodCallExpression.getArgumentList().getExpressions().length != 0) {

                        for (PsiExpression inner_exp : methodCallExpression.getArgumentList().getExpressions()) {

                            if (!(inner_exp instanceof PsiMethodCallExpression)) continue;

                            String mc = ((PsiMethodCallExpression) inner_exp).getMethodExpression().getReferenceName();

                            if (mc == null) continue;

                            String parsed_mc = RegexForTestNamePattern_v1_1Kt.parseName(mc);

                            List<String> words_from_mc = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_mc));

                            words_from_mc = words_from_mc.stream().map(String::toLowerCase).collect(Collectors.toList());

                            if (!words_from_name.containsAll(words_from_mc)) continue;

                            return ((PsiMethodCallExpression) inner_exp);
                        }
                    }
                }
            }
        }
        return null;
    }

    public List<PsiMethodCallExpression> extractConstructor(PsiDeclarationStatement statement){

        List<PsiLocalVariable> localVariables = Arrays.stream(statement.getDeclaredElements())
                .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                .map(PsiLocalVariable.class::cast)
                .collect(Collectors.toList());

        if (localVariables.size() == 0) return null;

        List<PsiMethodCallExpression> result = new LinkedList<>();

        localVariables.forEach(local -> {

            if (local.hasInitializer()){

                PsiExpression expression = local.getInitializer();

                if (expression instanceof PsiMethodCallExpression){

                    result.add((PsiMethodCallExpression) expression);
                }
            }
        });
        return result;
    }
}
