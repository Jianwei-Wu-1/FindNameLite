package FocalElementDetector;

import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiCodeBlock;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiMethodCallExpression;
import com.intellij.psi.controlFlow.*;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class FocalMethodDetector_Simple {

    private String focalObject = "";

    private String focalMethodName = "";

    private List<Instruction> instructionList;

    private List<PsiElement> elementList = new LinkedList<>();

    public FocalMethodDetector_Simple(Project project, PsiCodeBlock test_body) throws AnalysisCanceledException {

        ControlFlowFactory cfgFactory = ControlFlowFactory.getInstance(project);

        ControlFlow cfg = cfgFactory.getControlFlow(test_body, AllVariablesControlFlowPolicy.getInstance());

        this.instructionList = cfg.getInstructions();

        List<PsiElement> temp1 = new LinkedList<>();

        for (int i = 0; i < cfg.getSize(); i++){
            temp1.add(cfg.getElement(i));
        }

        this.elementList.addAll(temp1);
    }

    public String fetchFocalMethodName(){

        List<String> read_in_instructionList = instructionList.stream()
                .filter(instruction -> instruction instanceof SimpleInstruction)
                .filter(instruction -> !(instruction instanceof CommentInstruction))
                .filter(instruction -> !(instruction instanceof EmptyInstruction))
                .filter(instruction -> !(instruction instanceof WriteVariableInstruction))
                .map(Object::toString)
                .collect(Collectors.toList());

        List<String> write_in_instructionList = instructionList.stream()
                .filter(instruction -> instruction instanceof WriteVariableInstruction)
                .map(Object::toString)
                .collect(Collectors.toList());

        Map<String, Long> read_instruction_occurrences = read_in_instructionList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, Long> write_instruction_occurrences = write_in_instructionList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        //Readme: counting part

        List<String> read_key_set = new LinkedList<>(read_instruction_occurrences.keySet());

        List<String> write_key_set = new LinkedList<>(write_instruction_occurrences.keySet());

        if (read_key_set.size() == 0 || write_key_set.size() == 0) return null;

        String read_top = read_key_set.get(0);

        String write_top = write_key_set.get(0);

        if (!(read_top.substring(5, read_top.length())
                .equals(write_top.substring(6, write_top.length())))){
            return null;
        }

        focalObject = read_top.substring(5, read_top.length());

        //Get to Extract the focalObject

        for (PsiElement element: elementList) {

            if (!(element instanceof PsiMethodCallExpression)) continue;

            if (!(element.getText().contains(focalObject))) continue;

            if ((((PsiMethodCallExpression) element).getMethodExpression().getQualifierExpression()) == null) continue;

            PsiMethodCallExpression first = (PsiMethodCallExpression) element;

            focalMethodName = first.getMethodExpression().getReferenceName();

            if (focalMethodName != null) break;

            //Get to Find the the first method call that contains the focalObject

            //Get to Extract the focal Method call's name
        }

        return focalMethodName;
    }


    public String fetchFocalObject(){

        List<String> read_in_instructionList = instructionList.stream()
                .filter(instruction -> instruction instanceof SimpleInstruction)
                .filter(instruction -> !(instruction instanceof CommentInstruction))
                .filter(instruction -> !(instruction instanceof EmptyInstruction))
                .filter(instruction -> !(instruction instanceof WriteVariableInstruction))
                .map(Object::toString)
                .collect(Collectors.toList());

        List<String> write_in_instructionList = instructionList.stream()
                .filter(instruction -> instruction instanceof WriteVariableInstruction)
                .map(Object::toString)
                .collect(Collectors.toList());


        Map<String, Long> read_instruction_occurrences = read_in_instructionList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        Map<String, Long> write_instruction_occurrences = write_in_instructionList.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

        List<String> read_key_set = new LinkedList<>(read_instruction_occurrences.keySet());

        List<String> write_key_set = new LinkedList<>(write_instruction_occurrences.keySet());

        if (read_key_set.size() == 0 || write_key_set.size() == 0) return null;

        String read_top = read_key_set.get(0);

        String write_top = write_key_set.get(0);

        if (!(read_top.substring(5, read_top.length())
                .equals(write_top.substring(6, write_top.length())))){
            return null;
        }

        focalObject = read_top.substring(5, read_top.length());

        //Get to Extract the focalObject

        return focalObject;
    }
}
