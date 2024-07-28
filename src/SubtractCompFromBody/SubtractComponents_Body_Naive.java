package SubtractCompFromBody;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static com.intellij.psi.impl.source.tree.JavaElementType.*;

public class SubtractComponents_Body_Naive extends SubtractComponents_Body {

    public SubtractComponents_Body_Naive(String test_name, String test_class) {
        super(test_name, test_class);
    }

    //Setup

    public List<String> PossibleActionMethodCall(){
        HashSet<String> method_set = super.fetchAllMethodCalls();
        List<String> results = new LinkedList<>();
        List<PsiElement> list_elements = super.fetchAllElements();
        //Setup
        for (PsiElement element : list_elements) {
            if (element.getNode().getElementType().equals(METHOD_CALL_EXPRESSION)
                    && !element.getText().startsWith("assert")
                    && !element.getText().startsWith("Assert")){
                results.add(element.toString().replace("PsiMethodCallExpression:",""));
            }
            else if (element.getNode().getElementType().equals(CODE_BLOCK)){
                PsiElement cur[] = element.getChildren();
                for (PsiElement ele1:
                        cur) {
                    if (ele1.getNode().getElementType().equals(EXPRESSION_STATEMENT)
                            && !ele1.getText().startsWith("assert")
                            && !ele1.getText().startsWith("Assert")){
                        ASTNode cur1 = ele1.getNode();
                        for (String method_call : method_set){
                            if (cur1.getText().contains(method_call) && cur1.getText().length() > 1){
                                results.add(method_call);
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    public List<String> PossibleScenarioObject(){
        List<String> results = new LinkedList<>();
        List<PsiElement> list_elements = super.fetchAllElements();
        HashSet<String> method_set = super.fetchAllMethodCalls();
        //Setup
        for (PsiElement element : list_elements) {
            //System.out.println("For one test: " + element.toString());
            if (element.getNode().getElementType().equals(LOCAL_VARIABLE)){
                results.add(element.toString().replace("PsiLocalVariable:",""));
            }
            else if (element.getNode().getElementType().equals(CODE_BLOCK)){
                PsiElement cur[] = element.getChildren();
                for (PsiElement ele1:
                        cur) {
                    if (ele1.getNode().getElementType().equals(DECLARATION_STATEMENT)){
                        results.add(ele1.getNode().getText().substring(0, ele1.getNode().getText().indexOf("=") - 1));
                    }
                }
            }
        }
        return results;
    }

    public List<String> PossibleCoreAssertions(){
        List<String> results = new LinkedList<>();
        List<PsiElement> list_elements = super.fetchAllElements();
        //Setup
        for (PsiElement element : list_elements) {
            if (element.getNode().getElementType().equals(METHOD_CALL_EXPRESSION)
                    && (element.getText().startsWith("assert") || element.getText().startsWith("Assert"))){
                results.add(element.toString().replace("PsiMethodCallExpression:",""));
            }
            else if (element.getNode().getElementType().equals(CODE_BLOCK)){
                PsiElement cur[] = element.getChildren();
                for (PsiElement ele1:
                     cur) {
                    if (ele1.getNode().getElementType().equals(EXPRESSION_STATEMENT)
                            && (ele1.getText().contains("assert") || ele1.getText().contains("Assert"))){
                        results.add(ele1.getText());
                    }
                }
            }
        }
        return results;
    }

    public List<String> PossiblePredicateMethodCalls(){
        HashSet<String> method_set = super.fetchAllMethodCalls();
        List<String> results = new LinkedList<>();
        List<PsiElement> list_elements = super.fetchAllElements();
        for (PsiElement element : list_elements) {
            if (element.getNode().getElementType().equals(METHOD_CALL_EXPRESSION)
                    && (element.getText().startsWith("assert") || element.getText().startsWith("Assert"))){
                ASTNode cur = element.getNode();
                for (String method_call : method_set){
                    if (cur.getText().contains(method_call) && cur.getText().length() > 1){
                        results.add(method_call);
                    }
                }
            }
            else if (element.getNode().getElementType().equals(CODE_BLOCK)){
                PsiElement cur[] = element.getChildren();
                for (PsiElement ele1:
                        cur) {
                    System.out.println("  -> " + ele1.getText() + " - " + ele1.getNode().getElementType());
                    if (ele1.getNode().getElementType().equals(EXPRESSION_STATEMENT)
                            && (ele1.getText().startsWith("assert") || ele1.getText().startsWith("Assert"))){
                        ASTNode cur1 = ele1.getNode();
                        for (String method_call : method_set){
                            if (cur1.getText().contains(method_call) && cur1.getText().length() > 1){
                                results.add(method_call);
                            }
                        }
                    }
                }
            }
        }
        return results;
        //1. Extract all method calls from the project except all test classes (focus on API calls)
        //2. Find the match in the assert excluding the actual assertions
        //3. Return results
    }
}
