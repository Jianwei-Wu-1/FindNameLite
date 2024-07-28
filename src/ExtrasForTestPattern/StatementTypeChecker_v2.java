package ExtrasForTestPattern;

import com.intellij.psi.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StatementTypeChecker_v2 {

    //Readme: build for NormalPattern series and NoAssertionPattern series

    //Readme: add PsiAssertStatement -> v2

    public boolean check(PsiMethod test){

        if (test.getBody() == null) return false;

        List<PsiStatement> original = Arrays.asList(test.getBody().getStatements());

        List<PsiStatement> filtered = original.stream()
                .filter(psiStatement -> !(psiStatement instanceof PsiDeclarationStatement))
                .filter(psiStatement -> !(psiStatement instanceof PsiExpressionStatement))
                .filter(psiStatement -> !(psiStatement instanceof PsiAssertStatement))
                .collect(Collectors.toList());

        return filtered.size() != 0;

    }
}
