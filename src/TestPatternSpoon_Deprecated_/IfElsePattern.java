//package TestPatternSpoon_Deprecated_;
//
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.project.ProjectManager;
//import org.jetbrains.annotations.NotNull;
//import org.junit.Test;
//import java.util.LinkedList;
//
//
//@Deprecated
//public class IfElsePattern implements TestBodyPattern {
//
//    private List<CtElement> elements = new LinkedList<>();
//
//    private void reportNull(){
//        System.out.println("Project is Null!");
//    }
//
//    public IfElsePattern(){
//    }
//
//    @NotNull
//    @Override
//    public List<CtElement> getCtElements() {
//
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
//    @NotNull
//    @Override
//    public String getPatternName() {
//        return "IfElse";
//    }
//
//    @Override
//    public boolean isControlFlowStructure() {
//        return true;
//    }
//
//    @Override
//    public CtMethod fetchModel(String test_class) {
//
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
//        CtMethod<?> first_if_else = null;
//
//        AllLoop:
//
//        for(CtPackage p : model1.getAllPackages()) {
//
//            for (CtType<?> type : p.getTypes()){
//
//                if (type.isClass()
//                        && type.isPublic()){
//                    for (CtMethod<?> method : type.getMethods()){
//
//                        if (method.getAnnotation(Test.class) != null
//                                || method.getSimpleName().contains("test")
//                                || method.getSimpleName().contains("Test")) {
//
//                            for (CtStatement statement : method.getBody().getStatements()){
//
//                                if (statement.getClass().equals(CtIfImpl.class)){
//
//                                    first_if_else = method;
//
//                                    break AllLoop;
//                                }
//
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//        return first_if_else;
//    }
//
//
//    @Deprecated
//    @Override
//    public Pattern fetchPattern(String test_class) {
//
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
//        CtMethod<?> first_if_else = null;
//
//        CtType<?> first_if_else_class = null;
//
//
//        AllLoop:
//
//        for(CtPackage p : model1.getAllPackages()) {
//
//            for (CtType<?> type : p.getTypes()){
//
//                if (type.isClass()
//                        && type.isPublic()){
//                    for (CtMethod<?> method : type.getMethods()){
//
//                        if (method.getAnnotation(Test.class) != null
//                                || method.getSimpleName().contains("test")
//                                || method.getSimpleName().contains("Test")) {
//                            //Get test cases
//
//                            for (CtStatement statement : method.getBody().getStatements()){
//
//                                if (statement.getClass().equals(CtIfImpl.class)){
//
//                                    first_if_else = method;
//
//                                    first_if_else_class = type;
//
//                                    break AllLoop;
//                                }
//
//                            }
//
//                        }
//                    }
//                }
//            }
//        }
//
//        assert first_if_else != null;
//
//        //Use Builder
//
//        return PatternBuilder.
//                create(new PatternBuilderHelper(first_if_else_class).
//                        setBodyOfMethod(first_if_else.getSimpleName()).
//                        getPatternElements()).
//                configurePatternParameters().
//                build();
//    }
//}
