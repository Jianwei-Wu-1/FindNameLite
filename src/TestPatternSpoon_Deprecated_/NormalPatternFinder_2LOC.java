//package TestPatternSpoon_Deprecated_;
//
//import ExtrasForTestPattern.StatementTypeChecker_v2;
//import TestBodyPattern.PatternFinder;
//import com.intellij.psi.*;
//
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
//@Deprecated
//public class NormalPatternFinder_2LOC implements PatternFinder {
//
//    //NMC: Nested Method Calls exist in the assertion -> complete
//
//    //2LOC: be able to handle only two lines of code
//
//    private String action = "";
//
//    private String predicate = "";
//
//    private String scenario = "";
//
//    @Override
//    public PatternMatch matches(PsiMethod test) {
//
//        if (new StatementTypeChecker_v2().check(test)) return null;
//
//        List<PsiStatement> statements =
//                Arrays.asList(Objects.requireNonNull(test.getBody()).getStatements());
//
//        if (statements.size() != 2) return null;
//
//        if (statements.get(0) instanceof PsiExpressionStatement
//                && statements.get(0).getText().contains("assert"))
//            return null;
//
//        //Only 2 lines of code and it doesn't begin with assertion
//
//        if (!(statements.get(0) instanceof PsiDeclarationStatement)
//                && !(statements.get(0) instanceof PsiExpressionStatement)){
//            return null;
//        }
//
//        PsiExpressionStatement action_line = null;
//
//        PsiDeclarationStatement scenario_line = null;
//
//        if (statements.get(0) instanceof PsiDeclarationStatement) {
//
//            scenario_line = (PsiDeclarationStatement) statements.get(0);
//        }
//
//        if (statements.get(0) instanceof PsiExpressionStatement) {
//
//            action_line = (PsiExpressionStatement) statements.get(0);
//        }
//
//        if (scenario_line == null && action_line == null) return null;
//
//        if (scenario_line != null && action_line == null){
//
//            PsiLocalVariable get_scenario = (PsiLocalVariable) scenario_line.getDeclaredElements()[0];
//
//            scenario = get_scenario.getName();
//        }
//
//        if (action_line != null && scenario_line == null){
//
//            List<PsiMethodCallExpression> action_method_calls =
//                    Arrays.stream(action_line.getChildren())
//                            .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                            .map(PsiMethodCallExpression.class::cast)
//                            .collect(Collectors.toList());
//
//            if (action_method_calls.size() == 0) return null;
//
//            action = action_method_calls.get(0).getMethodExpression().getReferenceName();
//
//            PsiExpression scenario_expression = action_method_calls.get(0).getMethodExpression().getQualifierExpression();
//
//            if (scenario_expression == null) return null;
//
//            scenario = scenario_expression.getText();
//        }
//        else{
//
//            action = "N/A";
//        }
//
//        if (!(statements.get(1) instanceof PsiExpressionStatement)) return null;
//
//        //Prevent unexpected test body
//
//        PsiExpressionStatement assertion_line = (PsiExpressionStatement) statements.get(1);
//
//        if (assertion_line == null || !assertion_line.getText().contains("assert")) return null;
//
//        List<PsiMethodCallExpression> predicate_method_calls =
//                Arrays.stream(assertion_line.getChildren())
//                        .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
//                        .map(PsiMethodCallExpression.class::cast)
//                        .collect(Collectors.toList());
//
//        if (predicate_method_calls.size() == 0) return null;
//
//        if (predicate_method_calls.get(0).getArgumentList().getExpressions().length == 0) return null;
//
//        if (!(predicate_method_calls.get(0).getArgumentList().getExpressions()[0] instanceof PsiMethodCallExpression)) return null;
//
//        PsiMethodCallExpression predicate_method_call
//                = (PsiMethodCallExpression) predicate_method_calls.get(0).getArgumentList().getExpressions()[0];
//
//        predicate = predicate_method_call.getMethodExpression().getReferenceName();
//
//        return new PatternMatch() {
//            @Override
//            public String getPatternName() {
//                return "NormalPattern_2LOC";
//            }
//
//            @Override
//            public String getAction() {
//                return action;
//            }
//
//            @Override
//            public String getPredicate() {
//                return predicate;
//            }
//
//            @Override
//            public String getScenario() {
//                return scenario;
//            }
//        };
//    }
//}
