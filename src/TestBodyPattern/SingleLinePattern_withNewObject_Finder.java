package TestBodyPattern;

import ExtrasForTestPattern.ChainedMethodCallAnalyzer_v1;
import ExtrasForTestPattern.DeclarationAnalyzer_v2;
import ExtrasForTestPattern.NameElement_CheckerAndMatcher_v1;
import ExtrasForTestPattern.NewObjectDetector_v1;
import com.intellij.psi.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SingleLinePattern_withNewObject_Finder implements PatternFinder{

    //v1 -> init construct

    //v2 -> more details about declarations

    //v3 -> update new-Object detector

    @Override
    public PatternMatch matches(PsiMethod test) {

        PsiCodeBlock test_body = test.getBody();

        if (test_body == null) return null;

        if (test_body.getStatements().length != 1) return null;

        if (test_body.getStatements()[0] instanceof  PsiDeclarationStatement){

            PsiDeclarationStatement declarationStatement =
                    (PsiDeclarationStatement) test_body.getStatements()[0];

            if (declarationStatement ==  null) return null;

            if (declarationStatement.getDeclaredElements().length == 0) return null;

            if (!(declarationStatement.getDeclaredElements()[0] instanceof PsiLocalVariable)) return null;

            String object_1 = ((PsiLocalVariable) declarationStatement.getDeclaredElements()[0]).getName();

            List<PsiLocalVariable> localVariables = Arrays.stream(declarationStatement.getChildren())
                    .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                    .map(PsiLocalVariable.class::cast)
                    .collect(Collectors.toList());

            return new PatternMatch() {
                @Override
                public String getPatternName() {
                    return "NoAssertionPattern(DeclarationOnly_Single)";
                }

                @Override
                public String getAction() {

                    List<PsiMethodCallExpression> embedded_mcs = new DeclarationAnalyzer_v2().extractAllEmbeddedMethodCalls(localVariables);

                    List<String> mc_names = embedded_mcs.stream()
                            .map(psiMethodCallExpression -> psiMethodCallExpression.getMethodExpression().getReferenceName())
                            .collect(Collectors.toList());

                    Map<String, Long> mcs_occurrences = mc_names.stream()
                            .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

                    List<String> mcs_occurrences_set = new LinkedList<>(mcs_occurrences.keySet());
                    //DecAnalyzer

                    if (mcs_occurrences_set.size() != 0) {
                        return mcs_occurrences_set.get(0);
                    }
                    else {
                        return "N/A";
                    }
                }

                @Override
                public String getPredicate() {
                    return "N/A";
                }

                @Override
                public String getScenario() {
                    return object_1;
                }
            };
        }

        if (!(test_body.getStatements()[0] instanceof PsiExpressionStatement)) return null;
        PsiExpressionStatement expressionStatement = (PsiExpressionStatement) test_body.getStatements()[0];

        if (!(expressionStatement.getChildren()[0] instanceof PsiMethodCallExpression)) return null;
        PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getChildren()[0];

        if (methodCallExpression.getMethodExpression().getQualifierExpression() == null){

            List<PsiExpression> expressionList =
                    Arrays.asList(methodCallExpression.getArgumentList().getExpressions());

            if (expressionList.size() == 0) return null;

            if (!(expressionList.get(0) instanceof PsiMethodCallExpression)) return null;

            PsiMethodCallExpression subMethodCall_v1 =  (PsiMethodCallExpression) expressionList.get(0);

            return new PatternMatch() {
                @Override
                public String getPatternName() {
                    return "NoAssertionPattern(MethodCallOnly_Single)";
                }

                @Override
                public String getAction() {
                    return methodCallExpression.getMethodExpression().getReferenceName();
                }

                @Override
                public String getPredicate() {
                    return subMethodCall_v1.getMethodExpression().getReferenceName();
                }

                @Override
                public String getScenario() {
                    return "N/A";
                }
            };
        }

        PsiStatement statement = test_body.getStatements()[0];

        if (!(new NewObjectDetector_v1().isNewObject(statement))) return null;

        PsiExpression psiExpression = methodCallExpression.getMethodExpression().getQualifierExpression();

        List<PsiElement>  psiExpressionListList =
                Arrays.stream(psiExpression.getChildren())
                        .filter(psiElement -> psiElement instanceof PsiReferenceExpression)
                        .collect(Collectors.toList());

        if (psiExpressionListList.size() == 0) return null;

        PsiReferenceExpression psiReferenceExpression = (PsiReferenceExpression) psiExpressionListList.get(0);

        return new PatternMatch() {
            @Override
            public String getPatternName() {
                return "NoAssertionPattern(NewObject_Single)";
            }

            @Override
            public String getAction() {
                return methodCallExpression.getMethodExpression().getReferenceName();
            }

            @Override
            public String getPredicate() {
                return psiReferenceExpression.getReferenceName();
            }

            @Override
            public String getScenario() {
                return methodCallExpression.getMethodExpression().getQualifierExpression().getText();
            }
        };
    }
}
