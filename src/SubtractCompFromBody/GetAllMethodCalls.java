package SubtractCompFromBody;


import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import static com.intellij.psi.impl.source.tree.JavaElementType.METHOD_CALL_EXPRESSION;

public class GetAllMethodCalls {
    public List<String> getAllMethods(){

        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        final Collection<VirtualFile> all_files = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(p));
        //Get current project
        List<String> results = new LinkedList<>();
        for (VirtualFile virtualFile : all_files) {
            PsiFile file = PsiManager.getInstance(p).findFile(virtualFile);
            if (file instanceof PsiJavaFile &&
                    (file.getName().contains("Test")
                            || file.getName().contains("test"))) {
                for (PsiClass psiClass : ((PsiJavaFile) file).getClasses()) {
                    for (PsiMethod test : psiClass.getAllMethods()) {
                        //Stop at the test case
                        PsiCodeBlock codeBlock = test.getBody();
                        assert codeBlock != null;
                        PsiStatement array[] = codeBlock.getStatements();
                        for (PsiStatement anArray : array) {
                            //System.out.println("Statement: " + anArray.getText() + "\n");
                            PsiElement one[] = anArray.getChildren();
                            for (PsiElement ele:
                                    one) {
                                //System.out.println("SubElements: " + ele.toString() +" Type: " + ele.getNode().getElementType() + "\n");
                                if (ele.getNode().getElementType().equals(METHOD_CALL_EXPRESSION)){
                                    String current = ele.toString().replace("PsiMethodCallExpression:","");
                                    //results.add(current.substring(0, current.indexOf("(")));
                                    results.add(current);
                                }
                            }
                        }
                    }
                }
            }
        }
        return results;
    }
    public List<String> getAsserts(){
        Project p = ProjectManager.getInstance().getOpenProjects()[0];
        final Collection<VirtualFile> all_files = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(p));
        //Get current project
        List<String> results = new LinkedList<>();
        for (VirtualFile virtualFile : all_files) {
            PsiFile file = PsiManager.getInstance(p).findFile(virtualFile);
            if (file instanceof PsiJavaFile &&
                    (file.getName().contains("Test")
                            || file.getName().contains("test"))) {
                for (PsiClass psiClass : ((PsiJavaFile) file).getClasses()) {
                    for (PsiMethod test : psiClass.getAllMethods()) {
                        //Stop at the test case
                        PsiCodeBlock codeBlock = test.getBody();

                        if (codeBlock == null) continue;

                        PsiStatement array[] = codeBlock.getStatements();
                        for (PsiStatement anArray : array) {
                            //System.out.println("Statement: " + anArray.getText() + "\n");
                            if (anArray.getText().startsWith("assert") || anArray.getText().startsWith("Assert")){
                                results.add(anArray.getText());
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    public List<String> getStatements(){
        Project p = ProjectManager.getInstance().getOpenProjects()[0];
        final Collection<VirtualFile> all_files = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(p));
        //Get current project
        List<String> results = new LinkedList<>();
        for (VirtualFile virtualFile : all_files) {
            PsiFile file = PsiManager.getInstance(p).findFile(virtualFile);
            if (file instanceof PsiJavaFile &&
                    (file.getName().contains("Test")
                            || file.getName().contains("test"))) {
                for (PsiClass psiClass : ((PsiJavaFile) file).getClasses()) {
                    for (PsiMethod test : psiClass.getAllMethods()) {
                        //Stop at the test case

                        PsiCodeBlock codeBlock = test.getBody();

                        if (codeBlock == null) continue;

                        PsiStatement array[] = codeBlock.getStatements();

                        for (PsiStatement anArray : array) {
                            //System.out.println("Statement: " + anArray.getText() + "\n");
                            PsiElement one[] = anArray.getChildren();
                            for (PsiElement ele:
                                    one) {
                                    results.add(ele.toString() + " - " + ele.getNode().getElementType());
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

}
