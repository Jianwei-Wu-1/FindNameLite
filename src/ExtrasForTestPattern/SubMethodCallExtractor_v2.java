package ExtrasForTestPattern;

import com.intellij.psi.*;

import java.util.*;
import java.util.stream.Collectors;

public class SubMethodCallExtractor_v2 {

    //Embedded Method Invocation in PsiStatement Extractor

    //version 1 -> init

    //version 2 -> Add more support for different type of PsiStatement

    public Collection<PsiMethodCallExpression> extractAllSubMethodCalls_Expression(PsiExpressionStatement statement){

        List<PsiMethodCallExpression> intermediate_list_MC = Arrays.stream((statement).getChildren())
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());

        List<PsiMethodCallExpression> results = new LinkedList<>();

        if (intermediate_list_MC.size() != 0) {

            for (PsiMethodCallExpression MethodCallExpression:
                 intermediate_list_MC){

                results.addAll(Arrays.stream(MethodCallExpression.getArgumentList().getExpressions())
                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList()));
            }
        }

        List<PsiNewExpression> intermediate_list_new = Arrays.stream((statement).getChildren())
                .filter(psiElement -> psiElement instanceof PsiNewExpression)
                .map(PsiNewExpression.class::cast)
                .collect(Collectors.toList());

        if (intermediate_list_new.size() != 0){

            for (PsiNewExpression newExpression:
                 intermediate_list_new) {

                if (newExpression.getArgumentList() != null) {

                    results.addAll(Arrays.stream(newExpression.getArgumentList().getExpressions())
                            .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                            .map(PsiMethodCallExpression.class::cast)
                            .collect(Collectors.toList()));
                }
            }
        }

        return results;
    }

    public Collection<PsiReferenceExpression> extractAllSubMethodCalls_Assert(PsiAssertStatement statement){

        List<PsiBinaryExpression> intermediate_list_Binary = Arrays.stream(statement.getChildren())
                .filter(psiElement -> psiElement instanceof PsiBinaryExpression)
                .map(PsiBinaryExpression.class::cast)
                .collect(Collectors.toList());

        List<PsiReferenceExpression> results = new LinkedList<>();

        if (intermediate_list_Binary.size() != 0) {

            for (PsiBinaryExpression binaryExpression:
                    intermediate_list_Binary) {

                List<PsiReferenceExpression> left_side = Arrays.stream(binaryExpression.getLOperand().getChildren())
                        .filter(psiElement -> psiElement instanceof PsiReferenceExpression)
                        .map(PsiReferenceExpression.class::cast)
                        .collect(Collectors.toList());

                List<PsiReferenceExpression> right_side = new LinkedList<>();

                if (binaryExpression.getROperand() != null) {

                    right_side = Arrays.stream(binaryExpression.getROperand().getChildren())
                            .filter(psiElement -> psiElement instanceof PsiReferenceExpression)
                            .map(PsiReferenceExpression.class::cast)
                            .collect(Collectors.toList());
                }

                if (left_side.size() != 0) {

                    results.addAll(left_side);
                }

                if (right_side.size() != 0) {

                    results.addAll(right_side);
                }
            }
        }

        return results;
    }

    public Collection<PsiExpression> extractAllSubMethodCalls_Declaration(PsiDeclarationStatement statement){

        List<PsiLocalVariable> intermediate_list_Local = Arrays.stream(statement.getDeclaredElements())
                .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                .map(PsiLocalVariable.class::cast)
                .collect(Collectors.toList());

        List<PsiExpression> results = new LinkedList<>();

        if (intermediate_list_Local.size() != 0) {

            for (PsiLocalVariable localVariable :
                    intermediate_list_Local) {

                if (localVariable.getInitializer() != null) {

                    List<PsiReferenceExpression> referenceExpressions = Arrays.stream(localVariable.getInitializer().getChildren())
                            .filter(psiElement -> psiElement instanceof PsiReferenceExpression)
                            .map(PsiReferenceExpression.class::cast)
                            .collect(Collectors.toList());

                    results.addAll(referenceExpressions);

                    List<PsiExpressionList> expressionLists = Arrays.stream(localVariable.getInitializer().getChildren())
                            .filter(psiElement -> psiElement instanceof PsiExpressionList)
                            .map(PsiExpressionList.class::cast)
                            .collect(Collectors.toList());

                    if (expressionLists.size() != 0) {

                        for (PsiExpressionList list:
                             expressionLists) {

                            List<PsiMethodCallExpression> mc_list = Arrays.stream(list.getExpressions())
                                    .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                                    .map(PsiMethodCallExpression.class::cast)
                                    .collect(Collectors.toList());

                            results.addAll(mc_list);
                        }
                    }
                }
            }
        }
        return results;
    }
}
