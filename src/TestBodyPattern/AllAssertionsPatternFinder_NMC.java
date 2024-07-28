package TestBodyPattern;

import SubtractCompFromBody.SubtractComponents_Body;
import com.intellij.psi.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AllAssertionsPatternFinder_NMC implements PatternFinder {
    //NMC: Nested Method Calls exist in the assertion

    @Override
    public PatternMatch matches(PsiMethod test) {

        PsiClass original_class = test.getContainingClass();

        if (original_class ==  null) return null;

        if (test.getBody() == null) return null;

        SubtractComponents_Body subtractComponents_body =
                new SubtractComponents_Body(test.getName(), original_class.getName());

        if (subtractComponents_body.fetchAllElements() == null) return null;

        if (subtractComponents_body.fetchAllElements().size() == 0) return null;

        List<PsiElement> elements = subtractComponents_body.fetchAllElements().stream()
                .filter(p -> p instanceof PsiMethodCallExpression)
                .filter(p -> subtractComponents_body.fetchAllElements().indexOf(p)
                        == subtractComponents_body.fetchAllElements().lastIndexOf(p))
                .distinct()
                .collect(Collectors.toList());

        //Get to ensure uniqueness of containing elements

        List<PsiStatement> statements = Arrays
                .stream((test.getBody()).getStatements())
                .filter(psiStatement -> !(psiStatement instanceof PsiJavaToken))
                .collect(Collectors.toList());

        List<PsiStatement> assertion_statements = Arrays
                .stream((test.getBody()).getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .collect(Collectors.toList());

        if (elements.size() == 0) return null;

        if (statements.size() == 0 || assertion_statements.size() == 0) return null;

        if (statements.size() != assertion_statements.size()) return null;

        for (PsiStatement psiStatement : assertion_statements){

            PsiExpressionStatement current_line = (PsiExpressionStatement) psiStatement;

            if (!(current_line.getExpression().getText().contains("assert"))){

                return null;
            }
        }

        //Ensure all statements are assertions and ensure everything exists

        PsiElement[] firstLine_EmbeddedMethodCalls =
                elements.get(0).getChildren();

        PsiElement[] lastLine_EmbeddedMethodCalls =
                elements.get(elements.size() - 1).getChildren();

        List<PsiExpression> Sub_EmbeddedMethodCalls1 = new LinkedList<>();

        List<PsiReference> Sub_EmbeddedMethodReferences1;

        List<PsiExpression> Sub_EmbeddedMethodCalls2 = new LinkedList<>();

        List<PsiReference> Sub_EmbeddedMethodReferences2;

        for (PsiElement psiElement : firstLine_EmbeddedMethodCalls){

            if (!(psiElement instanceof PsiExpressionList)) continue;

            Sub_EmbeddedMethodCalls1
                    = Arrays.stream(((PsiExpressionList) psiElement).getExpressions())
                    .filter(p -> p instanceof PsiMethodCallExpression)
                    .collect(Collectors.toList());

            Sub_EmbeddedMethodReferences1
                    = Arrays.asList(psiElement.getReferences());

            if (Sub_EmbeddedMethodCalls1.size() != 0 && Sub_EmbeddedMethodReferences1.size() != 0) break;
        }


        for (PsiElement psiElement : lastLine_EmbeddedMethodCalls){

            if (!(psiElement instanceof PsiExpressionList)) continue;

            Sub_EmbeddedMethodCalls2
                    = Arrays.stream(((PsiExpressionList) psiElement).getExpressions())
                    .filter(p -> p instanceof PsiMethodCallExpression)
                    .collect(Collectors.toList());

            Sub_EmbeddedMethodReferences2
                    = Arrays.asList(psiElement.getReferences());

            if (Sub_EmbeddedMethodCalls2.size() != 0 && Sub_EmbeddedMethodReferences2.size() != 0) break;
        }

        if (Sub_EmbeddedMethodCalls1.size() == 0 || Sub_EmbeddedMethodCalls2.size() == 0) return null;

        PsiMethodCallExpression action = (PsiMethodCallExpression) Sub_EmbeddedMethodCalls1.get(0);

        if (action.getMethodExpression().getQualifierExpression() == null) return null;

        List<PsiExpressionList> expressionList_ForPredicate =
                Arrays.stream(Sub_EmbeddedMethodCalls2.get(0).getChildren())
                        .filter(psiElement -> psiElement instanceof PsiExpressionList)
                        .map(PsiExpressionList.class::cast)
                        .collect(Collectors.toList());

        if (expressionList_ForPredicate.size() == 0) return null;

        List<PsiMethodCallExpression> subSub_MethodCalls_ForPredicate =
                Arrays.stream(expressionList_ForPredicate.get(0).getExpressions())
                        .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                        .map(PsiMethodCallExpression.class::cast)
                        .collect(Collectors.toList());

        if (expressionList_ForPredicate.size() == 0 || subSub_MethodCalls_ForPredicate.size() == 0) {
            return null;
        }

        PsiMethodCallExpression predicate = subSub_MethodCalls_ForPredicate.get(0);

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
                return action.getMethodExpression().getReferenceName();
            }

            @Override
            public String getPredicate() {
                return predicate.getMethodExpression().getReferenceName();
            }

            @Override
            public String getScenario() {
                return action.getMethodExpression().getQualifierExpression().getText();
            }
        };
    }
}
