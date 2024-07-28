package SubtractCompFromBody;


import com.intellij.lang.ASTNode;
import com.intellij.psi.controlFlow.AnalysisCanceledException;
import com.intellij.psi.tree.IElementType;
import kotlin.Pair;

import java.util.*;

import static com.intellij.psi.impl.source.tree.JavaElementType.EXPRESSION_LIST;
import static com.intellij.psi.impl.source.tree.JavaElementType.METHOD_CALL_EXPRESSION;
import static com.intellij.psi.impl.source.tree.JavaElementType.REFERENCE_EXPRESSION;

public class SubtractComponents_Body_CFG extends SubtractComponents_Body {

    public SubtractComponents_Body_CFG(String test_name, String test_class) {
        super(test_name, test_class);
    }


    public SubtractComponents_Body_CFG(){
        super("", "");
    }


    public void show_everything () throws AnalysisCanceledException {
        super.showControlFlow();
    }


    public List<Pair<String, String>> getAllInstructionClassPairs
            (String test_name, String test_class) throws AnalysisCanceledException {
        return super.fetchAllInstructionPairs(test_name, test_class);
    }


    public List<Pair<String, String>> getAllNodeReferencePair
            (String test_name, String test_class) throws AnalysisCanceledException {
        return super.fetchAllNodeReferencePair(test_name, test_class);
    }


    public List<Pair<String, String>> getAllNodeCallPair
            (String test_name, String test_class) throws AnalysisCanceledException {
        return super.fetchAllNodeMethodCallPair(test_name, test_class);
    }


    private List<String> getAssertionCalls(String test_name, String test_class) throws AnalysisCanceledException{
        List<String> new_result = new LinkedList<>();
        List<Pair<ASTNode,IElementType>> old_pair = super.fetchOriginalNodePair(test_name, test_class);
        for (Pair<ASTNode,IElementType> cur_pair:
             old_pair) {
            //System.out.println("\n" + cur_pair.component1().toString());

            if (!cur_pair.component2().equals(METHOD_CALL_EXPRESSION)) continue;

            ASTNode cur_node = cur_pair.component1();
            ASTNode[] cur_cd = cur_node.getChildren(null);
            //System.out.println("ChildSet: " + ArrayUtils.toString(cur_cd) + " for: " + cur_node.getText());

            if (cur_node.getText().contains("assert") || cur_node.getText().contains("Assert")) {
                for (ASTNode node :
                        cur_cd) {
                    //System.out.println("SubNode: " + node.getText() + " - " + node.getElementType());

                    if (node.getElementType().equals(EXPRESSION_LIST)) {
                        ASTNode[] cur_sub_cd = node.getChildren(null);
                        for (ASTNode inner_node:
                             cur_sub_cd) {
                            //System.out.println("InnerNode: " + inner_node.getText() + " - " +inner_node.getElementType());

                            if (inner_node.getElementType().equals(METHOD_CALL_EXPRESSION)){
                                new_result.add(inner_node.getText());
                            }
                        }
                    }
                }
            }

        }
        return new_result;
        //Retrieve all method calls in all assertions for a specific test case found by its test name and test class
    }


    public List<String> getOtherCalls(String test_name, String test_class) throws AnalysisCanceledException{
        List<String> new_result = new LinkedList<>();
        List<Pair<ASTNode,IElementType>> old_pair = super.fetchOriginalNodePair(test_name, test_class);
        for (Pair<ASTNode,IElementType> cur_pair:
                old_pair) {
            //System.out.println("\n" + cur_pair.component1().toString());

            if (!cur_pair.component2().equals(METHOD_CALL_EXPRESSION)) continue;

            ASTNode cur_node = cur_pair.component1();
            //System.out.println("ChildSet: " + ArrayUtils.toString(cur_cd) + " for: " + cur_node.getText());

            List<String> re1 = this.getAssertionCalls(test_name,test_class);
            if (!cur_node.getText().contains("assert") && !cur_node.getText().contains("Assert")
                    && ! re1.contains(cur_node.getText())) {
                new_result.add(cur_node.getText());
            }


        }
        return new_result;
        //Retrieve all method calls in all assertions for a specific test case found by its test name and test class
    }


    public List<String> getAssertionReferences(String test_name, String test_class) throws AnalysisCanceledException{

        List<String> new_result = new LinkedList<>();
        List<String> new_result1 = new LinkedList<>();
        List<Pair<ASTNode,IElementType>> old_pair = super.fetchOriginalNodePair(test_name, test_class);
        for (Pair<ASTNode,IElementType> cur_pair:
                old_pair) {

            //System.out.println("Node: " + cur_pair.component1().toString() + " " + cur_pair.component1().getElementType());
            if (!cur_pair.component2().equals(REFERENCE_EXPRESSION)) continue;
            ASTNode cur_node = cur_pair.component1();
            String reference = cur_node.getPsi().getText();
            //System.out.println("Ref: " + reference);
            new_result.add(reference);
        }
        for (Pair<ASTNode,IElementType> cur_pair:
                old_pair) {
            if (!cur_pair.component2().equals(METHOD_CALL_EXPRESSION)) continue;

            ASTNode cur_node = cur_pair.component1();

            if (cur_node.getText().contains("assert") || cur_node.getText().contains("Assert")) {
                //System.out.println("New New New: " +cur_node.getText());
                for (String ref:
                     new_result) {
                    if (cur_node.getText().contains(ref) && !new_result1.contains(ref)){
                        new_result1.add(ref);
                    }
                }
            }

        }
        return new_result1;
        //Retrieve all references in all assertions for a specific test case found by its test name and test class
    }

}
