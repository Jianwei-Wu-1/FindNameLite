package ExtrasForTestPattern;

import com.intellij.psi.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AssertionHandler_v2 {

    //Readme: This handler is aiming to process the following three kinds of special assertions:

    //Readme: "Assert.assertEquals(...)" , "assertEquals(...)" , "Assert 1 == 1"

    //Readme: This handler is mainly for future use and the primitive "NormalPattern"

    //Readme: v2 -> add support for pure assert like "assert 1 != 2;"

    public PsiMethodCallExpression getFirstEmbeddedMethodCall(PsiExpressionStatement assertion){

        String text = assertion.getText();

        Optional<PsiMethodCallExpression> assert_call = Arrays.stream(assertion.getChildren())
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .findFirst();

        if (text.startsWith("assert") && text.endsWith(");") && assert_call.isPresent()) {

            if (assert_call.get().getArgumentList().getExpressionCount() == 0)
                return null;

            List<PsiMethodCallExpression> sub_expressions = Arrays.stream(assert_call.get().getArgumentList().getExpressions())
                    .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                    .map(PsiMethodCallExpression.class::cast)
                    .collect(Collectors.toList());

            if (sub_expressions.size() == 0) return null;

            return sub_expressions.get(0);
        }

        else if (text.startsWith("Assert.assert") && text.endsWith(");") && assert_call.isPresent()){

            if (assert_call.get().getArgumentList().getExpressionCount() == 0)
                return null;

            List<PsiMethodCallExpression> sub_expressions = Arrays.asList(assert_call.get().getArgumentList().getExpressions()).stream()
                    .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                    .map(PsiMethodCallExpression.class::cast)
                    .collect(Collectors.toList());

            if (sub_expressions.size() == 0) return null;

            return sub_expressions.get(0);
        }

        else if (text.startsWith("Assert.") && !text.contains("assert") && assert_call.isPresent()){

            List<PsiMethodCallExpression> expressions = Arrays.stream(assertion.getChildren())
                    .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                    .map(PsiMethodCallExpression.class::cast)
                    .collect(Collectors.toList());

            return expressions.get(0);
            //readme: s1 -> special case 1
        }

        else{

            return null;
        }
    }

    public String getFromPsiAssert(PsiAssertStatement assertStatement){

            List<PsiElement> elements = Arrays.stream(assertStatement.getChildren())
                    .collect(Collectors.toList());

            for (PsiElement e:
                    elements) {

                if (e instanceof PsiBinaryExpression){

                    PsiBinaryExpression binary_expression = (PsiBinaryExpression) e;

                    PsiExpression expression_left =  binary_expression.getLOperand();

                    PsiExpression expression_right =  binary_expression.getROperand();

                    if (expression_left instanceof PsiMethodCallExpression){

                        PsiMethodCallExpression embedded_methodCall = (PsiMethodCallExpression) expression_left;

                        return embedded_methodCall.getMethodExpression().getReferenceName();
                    }
                    else{

                        if (expression_right instanceof PsiMethodCallExpression){

                            PsiMethodCallExpression embedded_methodCall = (PsiMethodCallExpression) expression_right;

                            return embedded_methodCall.getMethodExpression().getReferenceName();
                        }
                        else {

                            break;
                        }
                    }
                }
            }
            return "N/A (No Invocation in assertion)";
    }

    public List<PsiMethodCallExpression> getAllEmbeddedMethodCalls(PsiExpressionStatement assertion){

        String text = assertion.getText();

        Optional<PsiMethodCallExpression> assert_call = Arrays.stream(assertion.getChildren())
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .findFirst();

        if (text.startsWith("assert") && text.endsWith(");") && assert_call.isPresent()) {

            if (assert_call.get().getArgumentList().getExpressionCount() == 0)
                return null;

            List<PsiMethodCallExpression> sub_expressions = Arrays.stream(assert_call.get().getArgumentList().getExpressions())
                    .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                    .map(PsiMethodCallExpression.class::cast)
                    .collect(Collectors.toList());

            if (sub_expressions.size() == 0) return null;

            return sub_expressions;
        }

        if (text.startsWith("Assert.assert") && text.endsWith(");") && assert_call.isPresent()){

            if (assert_call.get().getArgumentList().getExpressionCount() == 0)
                return null;

            List<PsiMethodCallExpression> sub_expressions = Arrays.stream(assert_call.get().getArgumentList().getExpressions())
                    .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                    .map(PsiMethodCallExpression.class::cast)
                    .collect(Collectors.toList());

            if (sub_expressions.size() == 0) return null;

            return sub_expressions;
        }

        return null;
    }
}
