package ParseNameExtractComponents;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class SendResultsToFile {
    public void local_log(String message) throws IOException {
        //Todo: If cloned fron Bitbucket, Please change the abs path
        File file1 = new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/Results/ResultsForStereotype.txt");
        PrintWriter out = new PrintWriter(new FileWriter(file1, true), true);
        out.write(message + "\n");
        out.close();
    }
}
