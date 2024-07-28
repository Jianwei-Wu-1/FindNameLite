package SubtractCompFromBody;

import com.intellij.execution.PsiLocation;
import com.intellij.execution.junit.JUnitUtil;
import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.lang.ASTNode;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.controlFlow.*;
import com.intellij.psi.impl.source.tree.java.PsiLocalVariableImpl;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.tree.IElementType;
import kotlin.Pair;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;

import static com.intellij.psi.impl.source.tree.JavaElementType.METHOD_CALL_EXPRESSION;

@SuppressWarnings({"WeakerAccess", "Duplicates"})
public class SubtractComponents_Body {

    public SubtractComponents_Body(String test_name, String test_class) {

        this.test_name = test_name;

        this.test_class = test_class;
    }

    public SubtractComponents_Body() {

        this.test_name = "";

        this.test_class = "";
    }

    private String test_name;

    private String test_class;

    private Project cur_project = ProjectManager.getInstance().getOpenProjects()[0];

    private List<Pair<String,String>> instruction_class_pair = new LinkedList<>();

    private List<Pair<String,String>> node_reference_pair = new LinkedList<>();

    private List<Pair<String,String>> node_method_call_pair = new LinkedList<>();

    public Project[] cur_projects = ProjectManager.getInstance().getOpenProjects();

    //Main Setup

    private ControlFlowInstructionVisitor visitor1 = new ControlFlowInstructionVisitor(){
        @Override
        public void visitInstruction(Instruction instruction, int offset, int nextOffset) {
            System.out.println("Visited Instruction: " + instruction.getClass().toString() + " with" + offset + "-" + nextOffset);
            if (instruction.getClass().equals(ConditionalThrowToInstruction.class)){
                System.out.println("Match for ThrowTo " + instruction.getClass());
            }
            else if (instruction.getClass().equals(CallInstruction.class)){
                System.out.println("Match for Call " + instruction.getClass());
            }
            else if (instruction.getClass().equals(EmptyInstruction.class)){
                System.out.println("Match for Empty " + instruction.getClass());
            }
            else if (instruction.getClass().equals(CommentInstruction.class)){
                System.out.println("Match for Comment " + instruction.getClass());
            }
            else if (instruction.getClass().equals(ConditionalBranchingInstruction.class)){
                System.out.println("Match for ConditionalBranching " + instruction.getClass());
            }
            else if (instruction.getClass().equals(WriteVariableInstruction.class)){
                System.out.println("Match for WriteVariable " + instruction.getClass());
            }
            else if (instruction.getClass().equals(ReturnInstruction.class)){
                System.out.println("Match for Return " + instruction.getClass());
            }
        }
    };
    //For showControlFlow
    private ControlFlowInstructionVisitor visitor2 = new ControlFlowInstructionVisitor(){
        @Override
        public void visitInstruction(Instruction instruction, int offset, int nextOffset) {
            System.out.println("Visited Instruction: " + instruction.getClass().toString() + " with" + offset + "-" + nextOffset);
            instruction_class_pair.add(new Pair <> (instruction.toString(), instruction.getClass().getCanonicalName()));

            if (instruction.getClass().equals(ConditionalThrowToInstruction.class)){
                System.out.println("Match for ThrowTo " + instruction.getClass());
            }
            else if (instruction.getClass().equals(CallInstruction.class)){
                System.out.println("Match for Call " + instruction.getClass());
            }
            else if (instruction.getClass().equals(EmptyInstruction.class)){
                System.out.println("Match for Empty " + instruction.getClass());
            }
            else if (instruction.getClass().equals(CommentInstruction.class)){
                System.out.println("Match for Comment " + instruction.getClass());
            }
            else if (instruction.getClass().equals(ConditionalBranchingInstruction.class)){
                System.out.println("Match for ConditionalBranching " + instruction.getClass());
            }
            else if (instruction.getClass().equals(WriteVariableInstruction.class)){
                System.out.println("Match for WriteVariable " + instruction.getClass());
            }
            else if (instruction.getClass().equals(ReturnInstruction.class)){
                System.out.println("Match for Return " + instruction.getClass());
            }
        }
    };
    //For fetchAllInstructionPairs

    void showControlFlow() throws AnalysisCanceledException {

        GlobalSearchScope searchScope = GlobalSearchScope.projectScope(cur_project);
        ControlFlowFactory cfgFactory = ControlFlowFactory.getInstance(cur_project);
        //Setup

        for (VirtualFile virtualFile : FileTypeIndex.getFiles(JavaFileType.INSTANCE, searchScope)) {

            PsiFile file = PsiManager.getInstance(cur_project).findFile(virtualFile);

            if (!(file instanceof PsiJavaFile)) continue;

            PsiJavaFile javaFile = (PsiJavaFile) file;

            for (PsiClass cls : javaFile.getClasses()) {
                if(!JUnitUtil.isTestClass(cls)) continue;
                System.out.println("Class: " + cls.getName());
                //Print test class name

                for (PsiMethod method : cls.getAllMethods()) {
                    System.out.println("Test: " + method.getName());
                    //Print test name

                    //if (JUnitUtil.isTestMethod(new PsiLocation<>(method))) continue;

                    System.out.println("PsiLocation: "
                            + JUnitUtil.isTestMethod(PsiLocation.fromPsiElement(method))
                            + " - "
                            + JUnitUtil.isTestMethod(new PsiLocation<>(method)));
                    //Test Code 1 complete

                    if(!JUnitUtil.isTestMethod(PsiLocation.fromPsiElement(method))) continue;
                    PsiCodeBlock cur_testcase = method.getBody();
                    assert cur_testcase != null;
                    //Ensure cur_testcase is not null

                    ControlFlow cfg =
                            cfgFactory.getControlFlow(cur_testcase, AllVariablesControlFlowPolicy.getInstance());
                    System.out.println("cfg size: "+cfg.getSize());
                    for (int i = 0; i < cfg.getSize(); i++){
                        System.out.println("cfg node: "+cfg.getElement(i));
                        if (cfg.getElement(i).getReference() != null) {
                            System.out.println("cfg reference: "
                                    + Objects.requireNonNull
                                    (cfg.getElement(i).getReference()).getCanonicalText());
                        }
                    }
                    int startoffset = cfg.getStartOffset(cur_testcase);
                    int endoffset = cfg.getEndOffset(cur_testcase);
                    System.out.println("Current Start&End OffSet: " + startoffset + " - " + endoffset);
                    System.out.println("All cfg instruction: " + ArrayUtils.toString(cfg.getInstructions()));
                    //Test code 2 complete


                    LocalsControlFlowPolicy localsControlFlowPolicy = new LocalsControlFlowPolicy(method);
                    ControlFlow cfg1 = cfgFactory.getControlFlow(cur_testcase, localsControlFlowPolicy);
                    System.out.println("local cfg instruction: " + ArrayUtils.toString(cfg1.getInstructions()));
//                    PsiReference node_0_ref = cfg.getElement(0).findReferenceAt(0);
//                    assert node_0_ref != null;
//                    assert node_0_ref.getElement() != null;
//                    List<PsiElement> Defs = Arrays.asList
//                            (DefUseUtil.getDefs(cur_testcase, new PsiFieldImpl(cfg.getElement(0).getNode()), node_0_ref.getElement()));
//                    System.out.println("Defs: "+ ArrayUtils.toString(Defs));
//                    List<PsiElement> Refs = Arrays.asList
//                            (DefUseUtil.getRefs(cur_testcase, new PsiFieldImpl(cfg.getElement(0).getNode()), node_0_ref.getElement()));
//                    System.out.println("Refs: "+ ArrayUtils.toString(Refs));
                    System.out.println("- - - - - -");
                    //Test code 3 complete

                    List<Instruction> cur_instructions = cfg.getInstructions();
                    for (Instruction cur_i:
                            cur_instructions) {
                        System.out.println("current instruction: "
                                + cur_i.toString()
                                + " <-> " + cur_i.getClass());
                    }
                    //Test Code 4 complete

                    for (Instruction cur_i:
                            cur_instructions) {
                        cur_i.accept(visitor1,0,1);
                    }
                    //Test Code 5 complete

                    System.out.println("\n");
                    //End
                }
            }
        }
    }

    List<Pair<String,String>> fetchAllInstructionPairs(String test_name, String test_class) throws AnalysisCanceledException {

        GlobalSearchScope searchScope = GlobalSearchScope.projectScope(cur_project);
        ControlFlowFactory cfgFactory = ControlFlowFactory.getInstance(cur_project);
        //Setup

        for (VirtualFile virtualFile : FileTypeIndex.getFiles(JavaFileType.INSTANCE, searchScope)) {

            PsiFile file = PsiManager.getInstance(cur_project).findFile(virtualFile);

            if (!(file instanceof PsiJavaFile)) continue;

            PsiJavaFile javaFile = (PsiJavaFile) file;

            for (PsiClass cls : javaFile.getClasses()) {

                if (!Objects.equals(cls.getName(), test_class)) continue;

                if(!JUnitUtil.isTestClass(cls)) continue;

                for (PsiMethod method : cls.getAllMethods()) {
                    if(!method.getName().equals(test_name)) continue;

                    if(!JUnitUtil.isTestMethod(PsiLocation.fromPsiElement(method))) continue;

                    PsiCodeBlock cur_testcase = method.getBody();

                    assert cur_testcase != null;
                    ControlFlow cfg = cfgFactory.getControlFlow(cur_testcase, AllVariablesControlFlowPolicy.getInstance());


                    List<Instruction> cur_instructions = cfg.getInstructions();
                    for (Instruction cur_i:
                            cur_instructions) {
                        cur_i.accept(visitor2,0,1);
                    }
                }
            }
        }
        return instruction_class_pair;
    }


    List<Pair<String,String>> fetchAllNodeReferencePair(String test_name, String test_class) throws AnalysisCanceledException {

        GlobalSearchScope searchScope = GlobalSearchScope.projectScope(cur_project);

        ControlFlowFactory cfgFactory = ControlFlowFactory.getInstance(cur_project);

        //Setup
        for (VirtualFile virtualFile : FileTypeIndex.getFiles(JavaFileType.INSTANCE, searchScope)) {

            PsiFile file = PsiManager.getInstance(cur_project).findFile(virtualFile);

            if (!(file instanceof PsiJavaFile)) continue;

            PsiJavaFile javaFile = (PsiJavaFile) file;

            for (PsiClass cls : javaFile.getClasses()) {

                if (!Objects.equals(cls.getName(), test_class)) continue;

                if(!JUnitUtil.isTestClass(cls)) continue;

                for (PsiMethod method : cls.getAllMethods()) {

                    if(!method.getName().equals(test_name)) continue;

                    if(!JUnitUtil.isTestMethod(PsiLocation.fromPsiElement(method))) continue;

                    PsiCodeBlock cur_testcase = method.getBody();

                    assert cur_testcase != null;

                    ControlFlow cfg = cfgFactory.getControlFlow(cur_testcase, AllVariablesControlFlowPolicy.getInstance());


                    for (int i = 0; i < cfg.getSize(); i++){
                        if (cfg.getElement(i).getReference() != null) {
                            node_reference_pair.
                                    add(new Pair <> (cfg.getElement(i).toString(),
                                            Objects.requireNonNull(cfg.getElement(i).getReference()).getCanonicalText()));
                        }
                    }
                }
            }
        }
        return node_reference_pair;
    }


    List<Pair<String,String>> fetchAllNodeMethodCallPair(String test_name, String test_class) throws AnalysisCanceledException {
        GlobalSearchScope searchScope = GlobalSearchScope.projectScope(cur_project);
        ControlFlowFactory cfgFactory = ControlFlowFactory.getInstance(cur_project);
        //Setup
        for (VirtualFile virtualFile : FileTypeIndex.getFiles(JavaFileType.INSTANCE, searchScope)) {
            PsiFile file = PsiManager.getInstance(cur_project).findFile(virtualFile);

            if (!(file instanceof PsiJavaFile)) continue;

            PsiJavaFile javaFile = (PsiJavaFile) file;

            for (PsiClass cls : javaFile.getClasses()) {
                if (!Objects.equals(cls.getName(), test_class)) continue;

                if(!JUnitUtil.isTestClass(cls)) continue;

                for (PsiMethod method : cls.getAllMethods()) {

                    if(!method.getName().equals(test_name)) continue;

                    if(!JUnitUtil.isTestMethod(PsiLocation.fromPsiElement(method))) continue;

                    PsiCodeBlock cur_testcase = method.getBody();

                    assert cur_testcase != null;

                    ControlFlow cfg = cfgFactory.getControlFlow(cur_testcase, AllVariablesControlFlowPolicy.getInstance());

                    for (int i = 0; i < cfg.getSize(); i++){
                        IElementType cur_type = cfg.getElement(i).getNode().getElementType();
                        if (cur_type.equals(METHOD_CALL_EXPRESSION)) {
                            node_method_call_pair.
                                    add(new Pair<>(cfg.getElement(i).getNode().toString(), cur_type.toString()));
                        }

                    }
                }
            }
        }
        return node_method_call_pair;
    }


    List<Pair<ASTNode,IElementType>> fetchOriginalNodePair(String test_name, String test_class) throws AnalysisCanceledException {
        List<Pair<ASTNode,IElementType>> original_node_pair = new LinkedList<>();

        GlobalSearchScope searchScope = GlobalSearchScope.projectScope(cur_project);

        ControlFlowFactory cfgFactory = ControlFlowFactory.getInstance(cur_project);
        //Setup

        for (VirtualFile virtualFile : FileTypeIndex.getFiles(JavaFileType.INSTANCE, searchScope)) {
            PsiFile file = PsiManager.getInstance(cur_project).findFile(virtualFile);

            if (!(file instanceof PsiJavaFile)) continue;

            PsiJavaFile javaFile = (PsiJavaFile) file;

            for (PsiClass cls : javaFile.getClasses()) {
                if (!Objects.equals(cls.getName(), test_class)) continue;

                if(!JUnitUtil.isTestClass(cls)) continue;

                for (PsiMethod method : cls.getAllMethods()) {

                    if(!method.getName().equals(test_name)) continue;

                    if(!JUnitUtil.isTestMethod(PsiLocation.fromPsiElement(method))) continue;

                    PsiCodeBlock cur_testcase = method.getBody();

                    assert cur_testcase != null;

                    ControlFlow cfg = cfgFactory.getControlFlow(cur_testcase, AllVariablesControlFlowPolicy.getInstance());

                    for (int i = 0; i < cfg.getSize(); i++){
                        IElementType cur_type = cfg.getElement(i).getNode().getElementType();
                        original_node_pair.add(new Pair<>(cfg.getElement(i).getNode(), cur_type));
                    }
                }
            }
        }
        return original_node_pair;
    }


    public List<PsiElement> fetchAllElements() {

        final Collection<VirtualFile> all_files =
                FileTypeIndex.getFiles(JavaFileType.INSTANCE,
                        GlobalSearchScope.projectScope(cur_project));

        List<PsiElement> results = new LinkedList<>();

        for (VirtualFile virtualFile : all_files) {

            PsiFile file = PsiManager.getInstance(cur_project).findFile(virtualFile);

            if (file instanceof PsiJavaFile &&
                    (file.getName().contains("Test")
                            || file.getName().contains("test"))) {

                for (PsiClass psiClass : ((PsiJavaFile) file).getClasses()) {

                    if (!Objects.equals(psiClass.getName(), test_class)) continue;

                    for (PsiMethod test : psiClass.getAllMethods()) {

                        if (!test.getName().equals(test_name)) continue;


                        if (test.getBody() == null) return null;

                        PsiCodeBlock codeBlock = test.getBody();

                        if (codeBlock == null) return null;

                        PsiStatement[] array = codeBlock.getStatements();

                        for (PsiStatement statement : array) {

                            PsiElement one[] = statement.getChildren();

                            results.addAll(Arrays.asList(one));

                        }
                    }
                }
            }
        }
        return results;
    }

    public HashSet<String> fetchAllMethodCalls() {

        HashSet<String> set = new HashSet<>();

        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        final Collection<VirtualFile> all_files =
                FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(p));
        //Setup

        for (VirtualFile virtualFile : all_files) {
            PsiFile file = PsiManager.getInstance(p).findFile(virtualFile);

            if (file instanceof PsiJavaFile) {

                for (PsiClass psiClass : ((PsiJavaFile) file).getClasses()) {

                    for (PsiMethod method : psiClass.getAllMethods()) {

                        set.add(method.getName());
                    }
                }
            }

        }
        return set;
    }


    Project getCur_project(){
        return cur_project;
    }

    public String getTest_name() {
        return test_name;
    }

    public void setTest_name(String test_name) {
        this.test_name = test_name;
    }
}




//Todo: Legacy Code
//        GlobalSearchScope searchScope = GlobalSearchScope.projectScope(p);
//        ControlFlowFactory cfgFactory = ControlFlowFactory.getInstance(p);
//
//        for (VirtualFile virtualFile : FileTypeIndex.getFiles(JavaFileType.INSTANCE, searchScope)) {
//            PsiFile file = PsiManager.getInstance(p).findFile(virtualFile);
//
//            if (!(file instanceof PsiJavaFile)) continue;
//
//            PsiJavaFile javaFile = (PsiJavaFile) file;
//
//            for (PsiClass cls : javaFile.getClasses()) {
//                if(!JUnitUtil.isTestClass(cls)) continue;
//
//                for (PsiMethod method : cls.getAllMethods()) {
//
//                    if(!JUnitUtil.isTestMethod(PsiLocation.fromPsiElement(method))) continue;
//
//                    ControlFlow cfg = cfgFactory.getControlFlow(method, AllVariablesControlFlowPolicy.getInstance());
//
//                }
//            }
//
//        }
