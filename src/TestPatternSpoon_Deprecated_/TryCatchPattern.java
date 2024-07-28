//package TestPatternSpoon_Deprecated_;
//
//import TestPatternSpoon_Deprecated_.TestBodyPattern;
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
//import spoon.support.reflect.code.CtTryImpl;
//
//import java.util.LinkedList;
//import java.util.List;
//
//@Deprecated
//public class TryCatchPattern implements TestBodyPattern {
//    private List<CtElement> elements = new LinkedList<>();
//
//    private void reportNull() {
//        System.out.println("Project is Null!");
//    }
//
//    public TryCatchPattern() {
//    }
//
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
//        } else {
//            reportNull();
//        }
//
//        launcher1.buildModel();
//
//        CtModel model1 = launcher1.getModel();
//
//        CtMethod<?> first_try_catch = null;
//
//        AllLoop:
//
//        for (CtPackage p : model1.getAllPackages()) {
//
//            for (CtType<?> type : p.getTypes()) {
//
//                if (type.isClass()
//                        && type.isPublic()
//                        && type.getSimpleName().equals(test_class)) {
//
//                    //System.out.println("Class: " + type.getSimpleName());
//
//                    for (CtMethod<?> method : type.getMethods()) {
//
//                        if (method.getAnnotation(Test.class) != null
//                                || method.getSimpleName().contains("test")
//                                || method.getSimpleName().contains("Test")) {
//                            //Get test cases
//
//                            for (CtStatement statement : method.getBody().getStatements()) {
//
//                                if (statement.getClass().equals(CtTryImpl.class)) {
//
//                                    first_try_catch = method;
//
//                                    break AllLoop;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return first_try_catch;
//
//
////        assert first_try_catch_class != null;
////        System.out.println("Class: " + first_try_catch_class.getSimpleName());
////        System.out.println("First Name: " + first_try_catch.getSimpleName());
////        CtTry templateRoot = first_try_catch.getBody().getStatement(0);
////        TemplateMatcher matcher_try_catch = new TemplateMatcher(templateRoot);
////        for (CtElement element : matcher_try_catch.find(model1.getRootPackage())) {
////            System.out.println("Match Found: " + element.getShortRepresentation());
////        };
////        //Use matcher
//
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
//        } else {
//            reportNull();
//        }
//
//        launcher1.buildModel();
//
//        CtModel model1 = launcher1.getModel();
//
//        CtMethod<?> first_try_catch = null;
//
//        CtType<?> first_try_catch_class = null;
//
//
//        AllLoop:
//
//        for (CtPackage p : model1.getAllPackages()) {
//
//            for (CtType<?> type : p.getTypes()) {
//
//                if (type.isClass()
//                        && type.isPublic()
//                        && type.getSimpleName().equals(test_class)) {
//                    for (CtMethod<?> method : type.getMethods()) {
//
//                        if (method.getAnnotation(Test.class) != null
//                                || method.getSimpleName().contains("test")
//                                || method.getSimpleName().contains("Test")) {
//
//                            for (CtStatement statement : method.getBody().getStatements()) {
//
//                                if (statement.getClass().equals(CtTryImpl.class)) {
//
//                                    first_try_catch = method;
//
//                                    first_try_catch_class = type;
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
//        assert first_try_catch != null;
//
//        //Use Builder
//
//        return PatternBuilder.
//                create(new PatternBuilderHelper(first_try_catch_class).
//                        setBodyOfMethod(first_try_catch.getSimpleName()).
//                        getPatternElements()).
//                configurePatternParameters().
//                build();
//    }
//
//    @NotNull
//    @Override
//    public String getPatternName() {
//        return "TryCatch";
//    }
//
//    @Override
//    public boolean isControlFlowStructure() {
//        return true;
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
//        } else {
//            reportNull();
//        }
//
//        launcher1.buildModel();
//
//        CtModel model1 = launcher1.getModel();
//
//        for (CtPackage ctPackage : model1.getAllPackages()) {
//            elements.addAll(ctPackage.getTypes());
//        }
//        return elements;
//    }
//
//
//}
