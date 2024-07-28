package evaluate_bad_test_names;

import com.intellij.execution.PsiLocation;
import com.intellij.execution.junit.JUnitUtil;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;

import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public class TestNameUtils {

    static Collection<String> testNames(PsiClass cls){

        return Arrays.stream(cls.getAllMethods())
                .filter(m -> JUnitUtil.isTestMethod(new PsiLocation<>(m)))
                .map(PsiMethod::getName)
                .collect(Collectors.toSet());
    }
}
