package ExtrasForTestPattern;

import TestNamePattern.RegexForTestNamePattern_v1_1Kt;
import com.intellij.psi.*;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OuterMethodCallAnalyzerForTryCatch_v1 {

    public PsiMethodCallExpression extractMCFromOuterAndMatchedWithName(PsiCodeBlock codeBlock, String test_name){

        if (codeBlock == null) return null;

        if (Arrays.stream(codeBlock.getStatements())
                .noneMatch(statement -> statement instanceof PsiTryStatement)) return null;

        List<PsiMethodCallExpression> outer_methodCalls = new LinkedList<>();

        Arrays.stream(codeBlock.getStatements())
                .forEach(statement -> {
                    if (statement instanceof PsiExpressionStatement){
                        PsiExpressionStatement statement1 = (PsiExpressionStatement) statement;
                        if (statement1.getExpression() instanceof PsiMethodCallExpression){
                            outer_methodCalls.add((PsiMethodCallExpression) statement1.getExpression());
                        }
                    }
                });

        if (outer_methodCalls.size() == 0) return null;

        String parsed_name = RegexForTestNamePattern_v1_1Kt.parseName(test_name);

        List<String> words_from_name = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_name));

        words_from_name = words_from_name.stream().map(String::toLowerCase).collect(Collectors.toList());

        List<String> finalWords_from_name = words_from_name;

        for (PsiMethodCallExpression methodCall : outer_methodCalls) {
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
