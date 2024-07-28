package ParseNameExtractComponents;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class ResultFileCleaner {
    public void runFileCleaner(String file) throws FileNotFoundException {
        final File target = new File(file);
        try (PrintWriter writer = new PrintWriter(target))  {
            writer.print(StringUtils.EMPTY);
        }
        System.out.println("File -> {" + file + "} is Cleaned!");
    }
}
