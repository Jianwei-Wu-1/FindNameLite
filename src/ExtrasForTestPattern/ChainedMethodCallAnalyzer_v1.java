package ExtrasForTestPattern;

import com.intellij.psi.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class ChainedMethodCallAnalyzer_v1 {

    public boolean chainedMethodCallChecker(PsiMethodCallExpression expression){

        List<PsiMethodCallExpression> all_mcs = new LinkedList<>();
        List<PsiExpression> all_arguments = new LinkedList<>();

        expression.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression1) {
                all_mcs.add(expression1);
                all_arguments.addAll(Arrays.asList(expression1.getArgumentList().getExpressions()));
                super.visitMethodCallExpression(expression1);
            }
        });

        if (all_mcs.size() == 0) return true;

        List<PsiMethodCallExpression> mc_arguments = all_arguments.stream()
                .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());

        List<PsiMethodCallExpression> all_mc_arguments = new LinkedList<>();

        mc_arguments.forEach(mc -> mc.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                all_mc_arguments.add(expression);
                super.visitMethodCallExpression(expression);
            }
        }));

        if (all_mc_arguments.size() == 0) return true;

        all_mc_arguments.forEach(mc ->{
            for (int i = 0; i < all_mcs.size(); i++){
                if (all_mcs.get(i).getMethodExpression().equals(mc.getMethodExpression())){
                    //System.out.println("match:"+ all_mcs.get(i).getMethodExpression().getReferenceName());
                    all_mcs.remove(all_mcs.get(i));
                }
            }
        });

        return all_mcs.size() == 1;
    }

    public List<PsiMethodCallExpression> getAllMethodCalls(PsiMethodCallExpression expression){

        List<PsiMethodCallExpression> all_mcs = new LinkedList<>();

        expression.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression exp) {
                all_mcs.add(exp);
                super.visitMethodCallExpression(exp);
            }
        });

        return all_mcs;
    }

    public List<PsiMethodCallExpression> getDeChainedMcs(PsiMethodCallExpression expression){

        List<PsiMethodCallExpression> all_mcs = new LinkedList<>();
        List<PsiExpression> all_arguments = new LinkedList<>();

        expression.accept(new JavaRecursiveElementVisitor() {
            @Override
            public void visitMethodCallExpression(PsiMethodCallExpression expression1) {
                all_mcs.add(expression1);
                all_arguments.addAll(Arrays.asList(expression1.getArgumentList().getExpressions()));
                super.visitMethodCallExpression(expression1);
            }
        });

        if (all_mcs.size() == 0) return null;

        List<PsiMethodCallExpression> mc_arguments = all_arguments.stream()
                .filter(psiExpression -> psiExpression instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());

        List<PsiMethodCallExpression> all_mc_arguments = new LinkedList<>();

        mc_arguments.forEach(mc ->{
            mc.accept(new JavaRecursiveElementVisitor() {
                @Override
                public void visitMethodCallExpression(PsiMethodCallExpression expression) {
                    all_mc_arguments.add(expression);
                    super.visitMethodCallExpression(expression);
                }
            });
        });

        if (all_mc_arguments.size() == 0) return null;

        all_mc_arguments.forEach(mc ->{
            for (int i = 0; i < all_mcs.size(); i++){
                if (all_mcs.get(i).getMethodExpression().equals(mc.getMethodExpression())){
                    //System.out.println("match:"+ all_mcs.get(i).getMethodExpression().getReferenceName());
                    all_mcs.remove(all_mcs.get(i));
                }
            }
        });

        return all_mcs;
    }
}
