package ExtrasForTestPattern.RuleMining;

import ExtrasForTestPattern.RuleMining.RuleMiningVisitor_v2;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import evaluate_bad_test_names.ProjectUtils;
import evaluate_bad_test_names.PsiClassUtils;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
@SuppressWarnings("WeakerAccess")

public class AllMethodsIterator_v2 {

    //v1 -> init
    //v2 -> update
    //v3 -> fix "-1" error

    public static Path path1 = Paths.get("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/RuleMiningData/VisitorResults.txt");

    public static Path path2 = Paths.get("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/RuleMiningData/VisitorResultWithTests.txt");

    public static Path path3 = Paths.get("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/RuleMiningData/TestResults.txt");

    public void startProcess() throws IOException {

        ListMultimap<PsiMethod, String> testsToTokens = ArrayListMultimap.create();

        Map<PsiMethod, Integer> testsCounters = new HashMap<>();

        for (Project project : ProjectManager.getInstance().getOpenProjects()) {
            System.out.println(project.getName() + "\n");
            for (PsiClass testClass : ProjectUtils.testClasses(project)) {
                for (PsiMethod test : PsiClassUtils.testMethods(testClass)) {

                    if (testsToTokens.containsKey(test)) {

                        System.out.println(System.identityHashCode(test));
                        System.out.println("TEST ALREADY EXISTS");
                        testsCounters.put(test, testsCounters.get(test) + 1);
                        continue;
                        //Jump to next

                    } else{
                        testsCounters.put(test, 1);
                    }

                    testsToTokens.putAll(test, RuleMiningVisitor_v2.tokensFor(test));
                    System.out.println(test.getName());
                }
            }
        }

        for (PsiMethod method : testsCounters.keySet()) {
            try (BufferedWriter writer = Files.newBufferedWriter(path3, StandardOpenOption.CREATE,
                    StandardOpenOption.APPEND,
                    StandardOpenOption.WRITE)) {
                writer.write(method.getName());
                writer.newLine();
                writer.write(Objects.requireNonNull(method.getBody()).getText());
                writer.newLine();
                writer.write("# of clones: " + testsCounters.get(method));
                writer.newLine();
            }
        }

        AtomicInteger index = new AtomicInteger();
        Map<String, String> tokenMap = testsToTokens.values().stream()
                .distinct()
                .collect(Collectors.toMap(
                        token -> token,
                        token -> Integer.toString(index.getAndIncrement())
                ));

        try (BufferedWriter writer = Files.newBufferedWriter(path1, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {

            writer.write("@CONVERTED_FROM_TEXT\n");
            for (Map.Entry<String, String> entry : tokenMap.entrySet()) {
                writer.write(String.format("@ITEM=%s=%s\n", entry.getValue(), entry.getKey()));
            }

            writer.write("@ITEM=-1=*\n");

            for (PsiMethod test : testsToTokens.keySet()) {

                for (int i = 0; i < testsCounters.get(test); i++) {

                    List<String> tokens = testsToTokens.get(test);

                    String line = tokens.stream()
                            .map(tokenMap::get)
                            .collect(Collectors.joining(" -1 ", "", " -1 -2"));

                    writer.write(line);
                    writer.newLine();
                }
            }
        }

        try (BufferedWriter writer = Files.newBufferedWriter(path2, StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {

            for (PsiMethod test : testsToTokens.keySet()) {

                for (int i = 0; i < testsCounters.get(test); i++) {

                    List<String> tokens = testsToTokens.get(test);

                    String line = tokens.stream()
                            .collect(Collectors.joining(" * ", "", " *"));

                    writer.write(line);
                    writer.newLine();
                    writer.write(test.getName());
                    writer.newLine();
                    writer.write(test.getText());
                    writer.newLine();
                }
            }
        }
    }
}
