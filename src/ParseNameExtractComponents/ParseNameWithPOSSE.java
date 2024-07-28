package ParseNameExtractComponents;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;

import java.io.*;
import java.util.*;

public class ParseNameWithPOSSE {

    //Todo: If cloned from Bitbucket, please change all absolute file paths
    private static void watchProcess(final Process process) {
        new Thread(() -> {
            BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            try {
                while ((line = input.readLine()) != null) {
                    System.out.println(line);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void runPOSSEWithMethodCommandThenPrintParsedTestName(){

        Project p = ProjectManager.getInstance().getOpenProjects()[0];

        final Collection<VirtualFile> all_files = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(p));

        try {

            File input_all_names = new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/POSSE/Input/testInput1.input");

            FileWriter fw_all = new FileWriter(input_all_names, true);

            BufferedWriter target = new BufferedWriter(fw_all);

            for (VirtualFile virtualFile : all_files) {

                PsiFile file = PsiManager.getInstance(p).findFile(virtualFile);

                if (file instanceof PsiJavaFile &&
                        (file.getName().contains("Test")
                                || file.getName().contains("test"))) {
                    for (PsiClass psiClass : ((PsiJavaFile) file).getClasses()) {
                        for (PsiMethod test : psiClass.getAllMethods()) {

                            SplitTestName splitTestName1 = new SplitTestName();

                            String split_test_name = splitTestName1.nameSplitToString(test.getName());

                            target.write("void " + test.getName() + " |" + split_test_name + "\n");
                        }
                    }
                }
            }
            target.close();
        } catch (IOException ioe1) {

            ioe1.printStackTrace();
        }
        //Retrieve Files
        try {
            String[] inputs = {
                    "/bin/bash",
                    "-c",
                    "cd /Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/POSSE/Scripts; ./mainParser.pl ../Input/testInput1.input \"M\""
            };

            Process p1 = new ProcessBuilder(inputs).start();

            watchProcess(p1);

            p1.waitFor();

            if (p1.exitValue() == 0){

                System.out.println("Success! With exitValue- "+ p1.exitValue());
            }
            else {

                System.out.println("Fail!");
            }
        }
        catch (Exception e1){
            System.out.println("Execution error!");
            e1.printStackTrace();
        }
        //Use POSSE To Parse Retrieved Test Names
        try {

            File write_file = new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/Results/ResultsForPOSSE.txt");

            FileWriter writer = new FileWriter(write_file);

            File output_all = new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/POSSE/Output/testInput1.input.pos");

            FileReader fileReader_all = new FileReader(output_all);

            BufferedReader reader = new BufferedReader(fileReader_all);

            String result;

            while ((result = reader.readLine()) !=  null){

                System.out.println("POSSE result: "+result);

                writer.append("POSSE result: ").append(result).append("\n");
            }

            writer.close();

            reader.close();
        }
        catch (IOException ioe2){

            ioe2.printStackTrace();
        }

        //Read Results From Output File

        ResultFileCleaner resultFileCleaner = new ResultFileCleaner();

        try {
            resultFileCleaner.runFileCleaner("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/POSSE/Input/testInput1.input");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            resultFileCleaner.runFileCleaner("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/POSSE/Output/testInput1.input.pos");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //Clean input & output files up
    }

    public List<String> returnParsedResultForOneName(String test_name){

        try {
            File input_all_names = new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/POSSE/Input/testInput_OneName.input");

            FileWriter fw_all = new FileWriter(input_all_names, false);

            BufferedWriter target = new BufferedWriter(fw_all);

            SplitTestName splitTestName1 = new SplitTestName();

            String split_test_name = splitTestName1.nameSplitToString(test_name);

            target.write("void " + test_name + " |" + split_test_name + "\n");

            target.close();
        }
        catch (IOException ioe1) {
            ioe1.printStackTrace();
        }

        //Retrieve Name

        try {
            String[] inputs = {
                    "/bin/bash",
                    "-c",
                    "cd /Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/POSSE/Scripts; ./mainParser.pl ../Input/testInput_OneName.input \"M\""
            };

            Process p1 = new ProcessBuilder(inputs).start();

            watchProcess(p1);

            p1.waitFor();

            if (p1.exitValue() == 0){

                System.out.println("Success! With exitValue- "+ p1.exitValue());
            }
            else {

                System.out.println("Fail!");
            }
        }

        catch (Exception e1){
            System.out.println("Execution error!");
            e1.printStackTrace();
        }

        //Use POSSE To Parse Retrieved Test Names

        String returnedString = "";
        try {
            String result = "";

            File output_all = new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/POSSE/Output/testInput_OneName.input.pos");

            FileReader fileReader_all = new FileReader(output_all);

            BufferedReader reader = new BufferedReader(fileReader_all);

            while ((result = reader.readLine()) !=  null){

                System.out.println("POSSE result for one name: " + result);

                returnedString = result.substring(result.lastIndexOf("|") + 2);
            }
        }
        catch (IOException ioe2){

            ioe2.printStackTrace();
        }

        //Read Results From Output File

        return Arrays.asList(Objects.requireNonNull(returnedString).split(","));
    }
}
