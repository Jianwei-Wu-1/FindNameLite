package ParseNameExtractComponents;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.newvfs.impl.VirtualFileImpl;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiJavaFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.testFramework.LightVirtualFile;
import edu.wm.constants.ConfigConstants;
import edu.wm.core.TestStereotypeAnalyzer;
import org.apache.commons.io.FileUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.apache.commons.io.FileUtils.copyFileToDirectory;

public class ParseWithStereotype {

    public void runStereotypeForAllTestFiles(){

        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        final Collection<VirtualFile> all_files = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(p));

        for (VirtualFile virtualFile : all_files) {
            PsiFile file = PsiManager.getInstance(p).findFile(virtualFile);
            if (file instanceof PsiJavaFile &&
                    (file.getName().contains("Test")
                            || file.getName().contains("test"))) {

                String path = virtualFile instanceof VirtualFileImpl ? virtualFile.getPath()
                        : ((LightVirtualFile) virtualFile).getOriginalFile().getPath();

                File current = new File(path);

                File target = new File ("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
                        "/libs/TestStereotype/src/UnitTests");

                //Fixme: If cloned fron Bitbucket, Please change the abs path
                try {
                    copyFileToDirectory(current, target);
                    System.out.println("File: " + current.getName() + " Copied!");
                }
                catch (IOException file1){
                    file1.printStackTrace();
                }
            }
        }
        //Copy file to TestStereotype
        System.out.println("Run TestStereotypeAnalyzer... ");
        try {
            TestStereotypeAnalyzer analyzer = new TestStereotypeAnalyzer();

            try {

                analyzer.analyze(ConfigConstants.projectLoc);

            } catch (Exception ee1){
                ee1.printStackTrace();
            }
            analyzer.printTestType();
            System.out.println("\n");
            analyzer.printSummary();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        System.out.println("End");
        //Run TestStereotype, code comes from GitHub
        try {
            File testStereotype_UnitTests_dir = new File("/Users/wujianwei/Documents" +
                    "/ideaProjects1/FindNameLite/libs/TestStereotype/src/UnitTests");
            //Todo: If cloned fron Bitbucket, Please change the abs path
            FileUtils.cleanDirectory(testStereotype_UnitTests_dir);
            System.out.println("Dir cleaned");
        }
        catch (IOException folder1){
            System.out.println("Fail to clean!");
            folder1.printStackTrace();
        }
        //Clean the testStereotype target folder
    }

    public List<String> getTagForCertainName(String TestName){
        String tags = "";
        try {
            File result_file = new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/" +
                    "Results/StereotypeResults.txt");
            //Todo: If cloned fron Bitbucket, Please change the abs path
            BufferedReader reader = new BufferedReader(new FileReader(result_file));
            String line;
            while ((line = reader.readLine()) != null){
                if (line.contains(TestName)){
                    tags = reader.readLine().replace("NamePattern_NLP: ","");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Arrays.asList(tags.split(","));
    }

}
