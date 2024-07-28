//package TestPatternSpoon_Deprecated_;
//
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.project.ProjectManager;
//import org.jetbrains.annotations.NotNull;
//import spoon.Launcher;
//import spoon.pattern.Pattern;
//import spoon.reflect.CtModel;
//import spoon.reflect.declaration.CtElement;
//import spoon.reflect.declaration.CtMethod;
//import spoon.reflect.declaration.CtPackage;
//
//import java.util.LinkedList;
//import java.util.List;
//
//@Deprecated
//public class NormalPattern implements TestBodyPattern {
//
//    private List<CtElement> elements = new LinkedList<>();
//
//    private void reportNull(){
//        System.out.println("Project is Null!");
//    }
//
//    public  NormalPattern(){
//
//    }
//
//    @NotNull
//    @Override
//    public String getPatternName() {
//        return "NormalForm";
//    }
//
//    @Override
//    public List<CtElement> getCtElements() {
//        Project cur_project = ProjectManager.getInstance().getOpenProjects()[0];
//
//        String current_project = cur_project.getBasePath();
//
//        Launcher launcher1 = new Launcher();
//
//        if (current_project != null) {
//            launcher1.addInputResource(current_project);
//        }
//        else{
//            reportNull();
//        }
//
//        launcher1.buildModel();
//
//        CtModel model1 = launcher1.getModel();
//
//        for (CtPackage ctPackage : model1.getAllPackages()){
//            elements.addAll(ctPackage.getTypes());
//        }
//        return elements;
//    }
//
//    @Override
//    public boolean isControlFlowStructure() {
//        return false;
//    }
//
//    @Override
//    public CtMethod fetchModel(String test_class) {
//        return null;
//    }
//
//    @Override
//    public Pattern fetchPattern(String test_class) {
//        return null;
//    }
//}
