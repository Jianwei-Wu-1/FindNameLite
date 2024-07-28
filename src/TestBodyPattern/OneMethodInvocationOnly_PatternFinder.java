package TestBodyPattern;

import ExtrasForTestPattern.SubMethodCallExtractor_v2;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import evaluate_bad_test_names.TestPattern_ClassUnderTestUtils;
import org.apache.commons.lang.ArrayUtils;

import java.util.*;
import java.util.stream.Collectors;

public class OneMethodInvocationOnly_PatternFinder implements PatternFinder {

    @Override
    public PatternMatch matches(PsiMethod test) {

        PsiClass testClass = test.getContainingClass();

        Project currentProject = test.getProject();

        if (testClass == null || test.getBody() == null || testClass.getName() == null) return null;

        List<PsiStatement> statements = Arrays.asList(test.getBody().getStatements());

        if (statements.size() == 1) return null;

        List<PsiStatement> filtered_statements = statements.stream()
                .filter(psiStatement -> !(psiStatement instanceof PsiIfStatement))
                .filter(psiStatement -> !(psiStatement instanceof PsiTryStatement))
                .filter(psiStatement -> !(psiStatement instanceof PsiLoopStatement))
                .collect(Collectors.toList());

        if (filtered_statements.size() !=  statements.size()) return null;

        //Forbid the following types of statements:

        //if else statement, try catch statement, loop statement or single line statement

        String class_under_test = testClass.getName().replace("Test","");

        Collection<PsiClass> all_classes_excludeTestClass =
                TestPattern_ClassUnderTestUtils.ClassUnderTest(currentProject);

        List<PsiExpression> pattern_expressions = new LinkedList<>();

        for (PsiStatement statement:
             filtered_statements) {

            if (statement instanceof PsiExpressionStatement){

                pattern_expressions.addAll(new SubMethodCallExtractor_v2().extractAllSubMethodCalls_Expression((PsiExpressionStatement) statement));
            }
            else if (statement instanceof PsiAssertStatement){

                pattern_expressions.addAll(new SubMethodCallExtractor_v2().extractAllSubMethodCalls_Assert((PsiAssertStatement) statement));
            }
            else if (statement instanceof PsiDeclarationStatement){

                pattern_expressions.addAll(new SubMethodCallExtractor_v2().extractAllSubMethodCalls_Declaration((PsiDeclarationStatement) statement));
            }
        }

        List<PsiMethod> CUT_methods = new LinkedList<>();

        for (PsiClass cls:
             all_classes_excludeTestClass) {

            if (Objects.equals(cls.getName(), class_under_test)){

                CUT_methods.addAll(TestPattern_ClassUnderTestUtils.getAllMethodsForCUT(cls));
            }
        }

        List<String> pattern_method_invocations = new LinkedList<>();

        List<String> pattern_objects = new LinkedList<>();

        List<String> CUT_method_invocations = new LinkedList<>();

        for (PsiMethod method:
             CUT_methods) {

            CUT_method_invocations.add(method.getName());
        }
        //Get all Class Under Test method invocations

        for (PsiExpression expression:
             pattern_expressions) {

            if (expression instanceof PsiMethodCallExpression){

                PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expression;

                pattern_method_invocations.add(methodCallExpression.getMethodExpression().getReferenceName());
            }
            else if (expression instanceof PsiReferenceExpression){

                PsiReferenceExpression referenceExpression = (PsiReferenceExpression) expression;

                pattern_method_invocations.add(referenceExpression.getReferenceName());
            }
        }
        //Get pattern method invocations

        for (PsiExpression expression:
                pattern_expressions) {

            if (expression instanceof PsiMethodCallExpression){

                PsiMethodCallExpression methodCallExpression = (PsiMethodCallExpression) expression;

                if (methodCallExpression.getMethodExpression().getQualifierExpression() != null) {
                    pattern_objects.add(methodCallExpression.getMethodExpression().getQualifierExpression().getText());
                }
            }
            else if (expression instanceof PsiReferenceExpression){

                PsiReferenceExpression referenceExpression = (PsiReferenceExpression) expression;

                if (referenceExpression.getQualifierExpression() !=  null) {
                    pattern_objects.add(referenceExpression.getQualifierExpression().getText());
                }
            }
        }

        if (CUT_method_invocations.isEmpty()
                ||  pattern_method_invocations.isEmpty()
                ||  pattern_objects.isEmpty()) return null;

        boolean method_invocations_equal = pattern_method_invocations.stream()
                .allMatch(pattern_method_invocations.get(0)::equals);

        boolean objects_equal = pattern_objects.stream()
                .allMatch(pattern_objects.get(0)::equals);

        if (!method_invocations_equal || !objects_equal) return null;

        if (!CUT_method_invocations.contains(pattern_method_invocations.get(0))) return null;

        return new PatternMatch() {
            @Override
            public String getPatternName() {
                return "NoAssertionPattern(OneMethodInvocationAcrossAll)";
            }

            @Override
            public String getAction() {
                return pattern_method_invocations.get(0);
            }

            @Override
            public String getPredicate() {
                return "N/A";
            }

            @Override
            public String getScenario() {
                return pattern_objects.get(0);
            }
        };
    }
}
