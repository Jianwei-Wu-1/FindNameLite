package FocalElementDetector;

import com.intellij.psi.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FocalMethodDetector_WithHelper {

    private List<PsiMethod> methodList = new LinkedList<>();

    private PsiMethod cur_test;

    public FocalMethodDetector_WithHelper(PsiMethod test) {

        PsiClass class_under_test = test.getContainingClass();

        if (class_under_test == null) return;

        methodList.addAll(Arrays.asList(class_under_test.getMethods()));

        cur_test = test;
    }

    public List<String> GetAllNamesInHelper(){

        if (cur_test.getBody() ==  null) return null;

        List<PsiExpressionStatement> expressionStatements = Arrays.stream(cur_test.getBody().getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        if (expressionStatements.size() == 0) return null;

        String helper_name = "";

        for (PsiExpressionStatement statement : expressionStatements){

                if (statement.getExpression() instanceof PsiMethodCallExpression){

                    PsiMethodCallExpression methodCallExpression =
                            (PsiMethodCallExpression) statement.getExpression();

                    if (methodCallExpression.getMethodExpression().getReferenceName() == null) return null;

                    if (methodCallExpression.getMethodExpression().getReferenceName().contains("helper") ||
                            methodCallExpression.getMethodExpression().getReferenceName().contains("Helper")){

                        helper_name = methodCallExpression.getMethodExpression().getReferenceName();
                    }
                }

        }
        //Get First Helper

        PsiMethod helper = null;

        for (PsiMethod method : methodList){

            if (method.getName().equals(helper_name)){

                helper = method;
            }
        }

        if (helper == null) return null;

        if (helper.getBody() == null) return null;

        List<PsiExpressionStatement> expressionStatements1 = Arrays.stream(helper.getBody().getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        List<PsiExpression> expressionList = new LinkedList<>();

        for (PsiExpressionStatement statement : expressionStatements1){

            expressionList.add(statement.getExpression());
        }

        List<PsiMethodCallExpression> methodCallExpressions = expressionList.stream()
                .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());

        return methodCallExpressions.stream()
                .map(psiMethodCallExpression -> psiMethodCallExpression.getMethodExpression().getReferenceName())
                .collect(Collectors.toList());
    }

    public List<PsiMethodCallExpression> GetAllMethodInvocationsInHelper(){

        if (cur_test.getBody() ==  null) return null;

        List<PsiExpressionStatement> expressionStatements = Arrays.stream(cur_test.getBody().getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        if (expressionStatements.size() == 0) return null;

        String helper_name = "";

        for (PsiExpressionStatement statement : expressionStatements){

            if (statement.getExpression() instanceof PsiMethodCallExpression){

                PsiMethodCallExpression methodCallExpression =
                        (PsiMethodCallExpression) statement.getExpression();

                if (methodCallExpression.getMethodExpression().getReferenceName() == null) return null;

                if (methodCallExpression.getMethodExpression().getReferenceName().contains("helper") ||
                        methodCallExpression.getMethodExpression().getReferenceName().contains("Helper")){

                    helper_name = methodCallExpression.getMethodExpression().getReferenceName();
                }
            }

        }
        //Get First Helper

        PsiMethod helper = null;

        for (PsiMethod method : methodList){

            if (method.getName().equals(helper_name)){

                helper = method;
            }
        }

        if (helper == null) return null;

        if (helper.getBody() == null) return null;

        List<PsiExpressionStatement> expressionStatements1 = Arrays.stream(helper.getBody().getStatements())
                .filter(psiStatement -> psiStatement instanceof PsiExpressionStatement)
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        List<PsiExpression> expressionList = new LinkedList<>();

        for (PsiExpressionStatement statement : expressionStatements1){

            expressionList.add(statement.getExpression());
        }

        return expressionList.stream()
                .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());
    }

    public String GetMostFrequentMethodName() {

        List<String> names = this.GetAllNamesInHelper();

        Optional<Map.Entry<String, Long>> result = names.stream()
                .collect(Collectors.groupingBy(w -> w, Collectors.counting()))
                .entrySet()
                .stream()
                .max(Comparator.comparing(Map.Entry::getValue));

        return result.map(Map.Entry::getKey).orElse(null);
    }
}
