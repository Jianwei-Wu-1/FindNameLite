package SimpleComparison;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import evaluate_bad_test_names.ProjectUtils;
import evaluate_bad_test_names.PsiClassUtils;

public class testCounter {

    public void countTests() {

        int num_tests = 0;

        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            for (PsiClass testClass : ProjectUtils.testClasses(project)) {
                for (PsiMethod test : PsiClassUtils.testMethods(testClass)) {

                    num_tests += 1;
                }
            }
        }

        System.out.println("# Of Tests: " + num_tests);
    }
}
