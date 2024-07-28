package ExtrasForTestPattern;

import FocalElementDetector.FocalMethodDetector_WithHelper;
import com.intellij.psi.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class MethodInvocationAnalyzer_v1 {

    //Readme: Retrieve the most frequent method invocation across all statements in the test body

    //Readme: All analyzers use breadth-first-search algorithm(BFS)

    private <V> Map<V, Long> getMostFrequentOne(final Collection<V> elements) {

        return elements.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy
                        (Function.identity(), Collectors.counting()));
    }
    //Note: this method is counting the most frequent object in the collection

    private static List<PsiElement> getChildrenFromEveryElement(PsiElement element){

        List<PsiElement> subElements = new LinkedList<>();

        if (element != null) {

            subElements.add(element);

            for(int i = 0; i < subElements.size(); ++i) {

                PsiElement element1 = subElements.get(i);

                List<PsiElement> children = Arrays.asList(element1.getChildren());

                if (children.size() != 0) {

                    for (PsiElement child: children) {

                        if (!subElements.contains(child)) {

                            subElements.add(child);
                        }
                    }
                }
            }
        }
        return subElements;
    }
    //Note: breadth-first-search algorithm(BFS)


    public Map<String, Long> getAllEmbeddedMethodInvocations_RankedByFrequency(PsiMethod test){

        if (test.getBody() == null) return null;

        List<PsiStatement> statements = Arrays.asList(test.getBody().getStatements());

        if (statements.size() == 0) return null;

        List<PsiElement> elements = new LinkedList<>();

        for (PsiStatement statement:
             statements) {

            for (PsiElement cur_ele:
                 statement.getChildren()) {

                elements.addAll(getChildrenFromEveryElement(cur_ele));

            }
        }

        List<PsiMethodCallExpression> all_methodInvocations = elements.stream()
                .filter(psiElement -> psiElement instanceof PsiMethodCallExpression)
                .filter(psiElement -> !psiElement.getText().contains("assert"))
                .filter(psiElement -> !psiElement.getText().contains("Assert"))
                .map(PsiMethodCallExpression.class::cast)
                .collect(Collectors.toList());

        List<String> all_methodInvocations_Strings = all_methodInvocations.stream()
                .map(psiMethodCallExpression -> psiMethodCallExpression.getMethodExpression().getReferenceName())
                .collect(Collectors.toList());

        return getMostFrequentOne(all_methodInvocations_Strings);
    }

    public Map<String, Long> getAllObjectsVariableInstance_RankedByFrequency(PsiMethod test){

        if (test.getBody() == null) return null;

        List<PsiStatement> statements = Arrays.asList(test.getBody().getStatements());

        if (statements.size() == 0) return null;

        List<PsiElement> elements = new LinkedList<>();

        for (PsiStatement statement:
                statements) {

            for (PsiElement cur_ele:
                    statement.getChildren()) {

                elements.addAll(getChildrenFromEveryElement(cur_ele));

            }
        }

        List<PsiLocalVariable> all_ObjectVariableInstance = elements.stream()
                .filter(psiElement -> psiElement instanceof PsiLocalVariable)
                .map(PsiLocalVariable.class::cast)
                .collect(Collectors.toList());

        List<String> all_methodInvocations_Strings = all_ObjectVariableInstance.stream()
                .map(PsiNamedElement::getName)
                .collect(Collectors.toList());

        return getMostFrequentOne(all_methodInvocations_Strings);
    }
    //Note: same here for the object / variable / instance

    public List<PsiMethodCallExpression> getAllEmbeddedMethodInvocations_ReferredFromHelpers(PsiMethod test){

        if (test.getBody() == null) return null;

        List<PsiStatement> statements = Arrays.asList(test.getBody().getStatements());

        if (statements.size() == 0) return null;

        FocalMethodDetector_WithHelper focalMethodDetectorWithHelperOrCC =
                new FocalMethodDetector_WithHelper(test);

        return focalMethodDetectorWithHelperOrCC.GetAllMethodInvocationsInHelper();
    }
    //Note: new method for getting all invocations from helpers
}
