package evaluate_bad_test_names;

import com.intellij.execution.junit.JUnitUtil;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.search.SearchScope;
import com.intellij.psi.search.searches.AllClassesSearch;

import java.util.Collection;
import java.util.stream.Collectors;

public class ProjectUtils {

    public static Collection<PsiClass> testClasses(Project project) {

        SearchScope scope = GlobalSearchScope.projectScope(project);

        return AllClassesSearch.search(scope, project).findAll().stream()
                .filter(JUnitUtil::isTestClass)
                .collect(Collectors.toSet());
    }
}
