package ExtrasForTestPattern;

import com.intellij.psi.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ActionScenarioDetector_v2 {

    //This detector is built for if-else pattern and loop pattern

    public PsiDeclarationStatement fetchFirstDeclarationStatement(PsiCodeBlock test_body, int delimiter){

        List<PsiStatement> statements = Arrays.asList(test_body.getStatements());

        PsiDeclarationStatement statement = null;

        for (int i = delimiter - 1; i >= 0; i--) {

            if(statements.get(i) instanceof PsiDeclarationStatement){

                statement = (PsiDeclarationStatement) statements.get(i);
            }

            if (statement != null) break;
        }

        if (statement == null) return null;

        return statement;

    }

    public PsiExpressionStatement fetchFirstExpressionStatement(PsiCodeBlock test_body, int delimiter){

        List<PsiStatement> statements = Arrays.asList(test_body.getStatements());

        PsiExpressionStatement statement = null;

        for (int i = delimiter - 1; i >= 0; i--) {

            if(statements.get(i) instanceof PsiExpressionStatement){

                statement = (PsiExpressionStatement) statements.get(i);
            }

            if (statement !=  null) break;
        }

        if (statement == null) return null;

        return statement;

    }


    public String fetchAction(PsiCodeBlock test_body, int delimiter){

        List<PsiStatement> statements = Arrays.asList(test_body.getStatements());

        PsiExpressionStatement action_statement = null;

        for (int i = delimiter - 1; i >= 0; i--) {

            if(statements.get(i) instanceof PsiExpressionStatement){

                action_statement = (PsiExpressionStatement) statements.get(i);
            }

            if (action_statement != null) break;
        }

        if (action_statement == null) return null;

        List<PsiMethodCallExpression> methodCalls = Arrays.stream(action_statement.getChildren())
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());

        if (methodCalls.size() == 0) return null;


        return methodCalls.get(0).getMethodExpression().getReferenceName();
    }

    public String fetchScenario(PsiCodeBlock test_body, int delimiter){

        List<PsiStatement> statements = Arrays.asList(test_body.getStatements());

        PsiDeclarationStatement scenario_statement = null;

        for (int i = delimiter - 1; i >= 0; i--) {

            if(statements.get(i) instanceof PsiDeclarationStatement){

                scenario_statement = (PsiDeclarationStatement) statements.get(i);
            }

            if (scenario_statement != null) break;
        }

        if (scenario_statement == null) return null;

        List<PsiLocalVariable> localVariables = Arrays.stream(scenario_statement.getChildren())
                .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                .map(PsiLocalVariable.class::cast)
                .collect(Collectors.toList());

        if (localVariables.size() == 0) return null;

        return localVariables.get(0).getName();
    }
}
