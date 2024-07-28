//package TestPatternSpoon_Deprecated_;
//
//import com.intellij.openapi.project.Project;
//import com.intellij.openapi.project.ProjectManager;
//import org.jetbrains.annotations.NotNull;
//import org.junit.Test;
//import spoon.Launcher;
//import spoon.pattern.Pattern;
//import spoon.pattern.PatternBuilder;
//import spoon.pattern.PatternBuilderHelper;
//import spoon.reflect.CtModel;
//import spoon.reflect.code.CtStatement;
//import spoon.reflect.declaration.CtElement;
//import spoon.reflect.declaration.CtMethod;
//import spoon.reflect.declaration.CtPackage;
//import spoon.reflect.declaration.CtType;
//import spoon.support.reflect.code.CtInvocationImpl;
//
//import java.util.LinkedList;
//import java.util.List;
//
//public class AllAssertionsPattern implements TestBodyPattern {
//
//    private List<CtElement> elements = new LinkedList<>();
//
//    private void reportNull(){
//        System.out.println("Project is Null!");
//    }
//
//    public AllAssertionsPattern(){
//
//    }
//
//    @NotNull
//    @Override
//    public String getPatternName() {
//        return "AllAssertions";
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
//        CtMethod<?> first_all_asserts = null;
//
//        AllLoop:
//        //Big loop to iterate all test cases
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
//                            int count_statements = 0;
//                            int count_asserts = 0;
//
//                            for (CtStatement statement : method.getBody().getStatements()){
//
//                                //System.out.println("statement class: " + statement.getClass());
//
//                                if (statement.getClass().equals(CtInvocationImpl.class)
//                                        && statement.toString().startsWith("assert")){
//
//                                    count_asserts = count_asserts + 1;
//                                }
//
//                                count_statements = count_statements + 1;
//
//                            }
//
//                            if (count_asserts == count_statements){
//
//                                first_all_asserts = method;
//
//                                break AllLoop;
//
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return first_all_asserts;
//    }
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
//        CtMethod<?> first_all_asserts = null;
//
//        CtType<?> first_all_asserts_class = null;
//
//        AllLoop:
//        //Big loop to iterate all test cases
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
//                            int count_statements = 0;
//                            int count_asserts = 0;
//
//                            for (CtStatement statement : method.getBody().getStatements()){
//
//                                //System.out.println("statement class: " + statement.getClass());
//
//                                if (statement.getClass().equals(CtInvocationImpl.class)
//                                        && statement.toString().startsWith("assert")){
//
//                                    count_asserts = count_asserts + 1;
//                                }
//
//                                count_statements = count_statements + 1;
//
//                            }
//
//                            //System.out.println("method: " + method.getSimpleName() +
//                            // " - nums: " + count_asserts + "-" + count_statements);
//
//                            if (count_asserts == count_statements){
//
//                                first_all_asserts = method;
//
//                                first_all_asserts_class = type;
//
//                                //System.out.println("method: " + first_all_asserts.getSimpleName()
//                                //+ " class: " + first_all_asserts_class.getSimpleName());
//
//                                break AllLoop;
//
//                            }
//                        }
//                    }
//                }
//            }
//        }
//
//        assert first_all_asserts != null;
//
//        //Use Builder
//
//        return PatternBuilder.
//                create(new PatternBuilderHelper(first_all_asserts_class).
//                        setBodyOfMethod(first_all_asserts.getSimpleName()).
//                        getPatternElements()).
//                configurePatternParameters().
//                build();
//    }
//}
