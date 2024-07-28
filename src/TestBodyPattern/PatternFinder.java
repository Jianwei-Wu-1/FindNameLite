package TestBodyPattern;

import com.intellij.psi.PsiMethod;

public interface PatternFinder {

    interface PatternMatch {

        String getPatternName();

        String getAction();

        String getPredicate();

        String getScenario();
    }

    PatternMatch matches(PsiMethod test);

}
