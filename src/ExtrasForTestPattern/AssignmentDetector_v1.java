package ExtrasForTestPattern;

import TestNamePattern.RegexForTestNamePattern_v1_1Kt;
import com.intellij.psi.*;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class AssignmentDetector_v1 {

    public List<PsiMethodCallExpression> assignmentDetector(PsiMethod test){

        if (test.getBody() == null) return null;

        List<PsiMethodCallExpression> result = new LinkedList<>();

        List<PsiStatement> statements = Arrays.asList(test.getBody().getStatements());

        List<PsiExpressionStatement> expressionStatements = statements.stream()
                .filter(statement -> statement instanceof PsiExpressionStatement)
                .map(PsiExpressionStatement.class::cast)
                .collect(Collectors.toList());

        List<PsiAssignmentExpression> psiAssignmentExpressions = new LinkedList<>();

        for (PsiExpressionStatement statement : expressionStatements) {
            if (statement.getExpression() instanceof PsiAssignmentExpression) {
                PsiAssignmentExpression psiAssignmentExpression = (PsiAssignmentExpression) statement.getExpression();
                psiAssignmentExpressions.add(psiAssignmentExpression);
            }
        }

        psiAssignmentExpressions.forEach(assignmentExpression -> {
            if (assignmentExpression.getRExpression() instanceof PsiMethodCallExpression){
                PsiMethodCallExpression mc = (PsiMethodCallExpression) assignmentExpression.getRExpression();
                if (!mc.getArgumentList().isEmpty()){

                    for (PsiExpression expression : mc.getArgumentList().getExpressions()){

                        if (expression instanceof PsiMethodCallExpression){

                            String parsed_name = RegexForTestNamePattern_v1_1Kt.parseName(test.getName());

                            List<String> words_from_name = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_name));

                            words_from_name = words_from_name.stream().map(String::toLowerCase).collect(Collectors.toList());

                            String mc_name = ((PsiMethodCallExpression) expression).getMethodExpression().getReferenceName();

                            if (mc_name != null) {

                                String parsed_mc = RegexForTestNamePattern_v1_1Kt.parseName(mc_name);

                                List<String> words_from_mc = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_mc));

                                words_from_mc = words_from_mc.stream().map(String::toLowerCase).collect(Collectors.toList());

                                if (words_from_name.containsAll(words_from_mc)){

                                    result.add((PsiMethodCallExpression) expression);
                                }
                            }
                        }
                    }
                }
            }
        });
        return result;
    }
}
