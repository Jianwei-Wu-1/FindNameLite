package TestBodyPattern;

import FocalElementDetector.FocalMethodDetector_Simple;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.controlFlow.AnalysisCanceledException;

public class FocalMethodFinder implements PatternFinder {

    //This is fallback technique for Test Pattern System
    //version 1 -> starting point

    @Override
    public PatternMatch matches(PsiMethod test){

        Project project = test.getProject();

        PsiCodeBlock test_body = test.getBody();

        if (test_body == null) return null;

        try {
            FocalMethodDetector_Simple focalMethodDetector_simple =
                    new FocalMethodDetector_Simple(project, test_body);

            if (focalMethodDetector_simple.fetchFocalMethodName() == null ||
                    focalMethodDetector_simple.fetchFocalObject() == null) return null;

            return new PatternMatch() {

                @Override
                public String getPatternName() {
                    return "FocalMethod_Match";
                }

                @Override
                public String getAction() {
                    return focalMethodDetector_simple.fetchFocalMethodName();
                }

                @Override
                public String getPredicate() {
                    return "N/A";
                }

                @Override
                public String getScenario() {
                    return focalMethodDetector_simple.fetchFocalObject();
                }
            };

        } catch (AnalysisCanceledException e) {
            e.printStackTrace();
        }
        return null;
    }
}
