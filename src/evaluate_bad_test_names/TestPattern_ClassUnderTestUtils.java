package evaluate_bad_test_names;

import com.intellij.execution.junit.JUnitUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AllClassesSearch;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class TestPattern_ClassUnderTestUtils {

    public static Collection<PsiClass> ClassUnderTest(Project project){

        SearchScope scope = GlobalSearchScope.projectScope(project);

        return AllClassesSearch.search(scope, project).findAll().stream()
                .filter(psiClass -> !(JUnitUtil.isTestClass(psiClass)))
                .collect(Collectors.toList());
        //Get all test classes excluded
    }

    public static Collection<PsiMethod> getAllMethodsForCUT(PsiClass psiClass){

        return Arrays.stream(psiClass.getMethods())
                .filter(psiMethod -> !psiMethod.getName().equals(psiClass.getName()))
                .collect(Collectors.toList());
        //Get all constructors excluded
    }
}
