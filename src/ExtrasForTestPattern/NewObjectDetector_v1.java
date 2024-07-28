package ExtrasForTestPattern;

import TestNamePattern.RegexForTestNamePattern_v1_1Kt;
import com.intellij.psi.PsiExpressionStatement;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.PsiStatement;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class NewObjectDetector_v1 {

    public boolean isNewObject(PsiStatement statement){

        if (!(statement instanceof PsiExpressionStatement)) return false;

        if (!(statement.getText().startsWith("new "))) return false;

        PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement;

        if (!(expressionStatement.getExpression() instanceof PsiMethodCallExpression)) return false;

        PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getExpression();

        if (new ChainedMethodCallAnalyzer_v1().chainedMethodCallChecker(methodCallExpression)) return false;

        List<PsiMethodCallExpression> mcs = new ChainedMethodCallAnalyzer_v1().getDeChainedMcs(methodCallExpression);

        return mcs.size() != 0;
    }

    public PsiMethodCallExpression extractMatchedMethodCall(PsiStatement statement, String name){

        if (!(statement instanceof PsiExpressionStatement)) return null;

        if (!(statement.getText().startsWith("new "))) return null;

        PsiExpressionStatement expressionStatement = (PsiExpressionStatement) statement;

        if (!(expressionStatement.getExpression() instanceof PsiMethodCallExpression)) return null;

        PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expressionStatement.getExpression();

        ChainedMethodCallAnalyzer_v1 chainedMethodCallAnalyzer_v1 = new ChainedMethodCallAnalyzer_v1();

        if (chainedMethodCallAnalyzer_v1.chainedMethodCallChecker(methodCallExpression)) return null;

        if (!new NameElement_CheckerAndMatcher_v1().checkIfMatchWithName_ChainedMethodCall(methodCallExpression, name)) return null;

        List<PsiMethodCallExpression> deChained_mcs = chainedMethodCallAnalyzer_v1.getDeChainedMcs(methodCallExpression);

        String parsed_name = RegexForTestNamePattern_v1_1Kt.parseName(name);

        List<String> words_from_name = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_name));

        words_from_name = words_from_name.stream().map(String::toLowerCase).collect(Collectors.toList());

        List<String> finalWords_from_name = words_from_name;

        for (PsiMethodCallExpression methodCall : deChained_mcs) {

            String mc = methodCall.getMethodExpression().getReferenceName();

            if (mc != null) {

                String parsed_mc = RegexForTestNamePattern_v1_1Kt.parseName(mc);

                List<String> words_from_mc = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_mc));

                words_from_mc = words_from_mc.stream().map(String::toLowerCase).collect(Collectors.toList());

                if (finalWords_from_name.containsAll(words_from_mc)) {

                    return methodCall;
                }
            }
        }
        return null;
    }
}
