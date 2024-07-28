package evaluate_bad_test_names;

import ExtrasForTestPattern.RuleMining.AllMethodsIterator_v2;
import ParseNameExtractComponents.ParseWithStereotype;
import ParseNameExtractComponents.SendResultsToFile;
import SimpleComparison.GenerateResults;
import SimpleComparison.testCounter;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class EvaluateBadTestNames extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {

        new testCounter().countTests();

        long startTime = System.nanoTime();
        GenerateResults simpleComparison = new GenerateResults();
        try {
            simpleComparison.startProcess();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //Start the process
        long endTime   = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println(TimeUnit.MINUTES.convert(totalTime, TimeUnit.NANOSECONDS) + " minutes");

        AllMethodsIterator_v2 allMethodsIterator_v2 = new AllMethodsIterator_v2();

        try {
            allMethodsIterator_v2.startProcess();

        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //Sequential Rule Mining

        NameUtils nameUtils = new NameUtils();

        try {
            nameUtils.parseAllTestNames();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        //TestNamePattern

        PatternUtils patternUtils = new PatternUtils();

        patternUtils.line_of_code_threshold = 30;

        try {
            patternUtils.searchForPatterns();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }
        //TestBodyPattern

        ParseWithStereotype parseBodyWithStereotype1 = new ParseWithStereotype();

        parseBodyWithStereotype1.runStereotypeForAllTestFiles();

        List<String> ForOne = parseBodyWithStereotype1.getTagForCertainName("testUserParserForError");

        System.out.println("Stereotypes for: "+"testUserParserForError");

        SendResultsToFile sendResultsToFile = new SendResultsToFile();
        try {
            sendResultsToFile.local_log("Stereotypes for: "+"testUserParserForError");
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        for(int i = 0; i < ForOne.size(); i++){
            System.out.println("type"+(i+1)+": "+ForOne.get(i));
            try {
                sendResultsToFile.local_log("type"+(i+1)+": "+ForOne.get(i));
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        //Parse Test Body in All test files


//        ParseNameWithBllipParser parseNameWithBllipParser1 = new ParseNameWithBllipParser();
//
//        List<String> ForOne2 = parseNameWithBllipParser1.getResultForOneTestName("testGetNodeFromTree");
//
//        System.out.println("POS tagging for: "+"testUserParserForError");
//
//        for(int i = 0; i < ForOne2.size(); i++){
//            System.out.println("tag"+(i+1)+": "+ForOne2.get(i));
//        }
//        //parseNameWithBllipParser1.parseByCharniakParser();
//        //Parse Test Name with Bllip Parser
//
//
//        ResultFileCleaner resultFileCleaner1 = new ResultFileCleaner();
//        try {
//            resultFileCleaner1.runFileCleaner("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
//                    "/Results/ErrForBllip.txt");
//        } catch (FileNotFoundException e1) {
//            e1.printStackTrace();
//        }
//        //Test Cleaner
//
//        new ParseNameWithPOSSE().runPOSSEWithMethodCommandThenPrintParsedTestName();
//        ParseNameWithPOSSE parseNameWithPOSSE1 = new ParseNameWithPOSSE();
//        List<String> forOne2 = parseNameWithPOSSE1
//                .returnParsedResultForOneName("testIfDepositLessThanMax");
//
//        for (String aForOne2 : forOne2) {
//            System.out.println("POSSE tags: " + aForOne2);
//        }
//        //Use POSSE to parser test name "testGetFilePushFile"
//
//        try {
//
//            JWNL.initialize(
//                    new FileInputStream
//                            ("/Users/wujianwei/Documents/ideaProjects1/FindNameLite" +
//                                    "/libs/jwnl14-rc2/config/file_properties.xml"));
//
//            final Dictionary dictionary = Dictionary.getInstance();
//
//            List baseforms = dictionary.getMorphologicalProcessor().lookupAllBaseForms(POS.VERB, "sent");
//
//            System.out.println("Base-forms: " + baseforms.get(0) +"\n");
//        }
//        catch(FileNotFoundException | JWNLException e1){
//
//            e1.printStackTrace();
//        }
//        //Use JWNL to find base form of a word
//
//
//        GetAllMethodCalls getAllMethodCalls = new GetAllMethodCalls();
//        List<String> get_all = getAllMethodCalls.getStatements();
//        for (String a:
//             get_all) {
//            System.out.println("Statments: "+a+"\n");
//        }
//        List<String> get_all_asserts = getAllMethodCalls.getAsserts();
//        for (String a:
//                get_all_asserts) {
//            System.out.println("Asserts: "+a+"\n");
//        }
//        //Get methods and asserts for a specific test case
//
//        System.out.println("\n\n\n");
//
//        SubtractComponents_Body_Naive subtractMethodCalls_body_naive =
//                new SubtractComponents_Body_Naive("testUserParserForError");
//        List<String> re1 = subtractMethodCalls_body_naive.PossiblePredicateMethodCalls();
//        System.out.println("PredicateMethod: " + ArrayUtils.toString(re1));
//        List<String> re2 = subtractMethodCalls_body_naive.PossibleCoreAssertions();
//        System.out.println("Assertions: " + ArrayUtils.toString(re2));
//        List<String> re3 = subtractMethodCalls_body_naive.PossibleActionMethodCall();
//        System.out.println("ActionMethod: " + ArrayUtils.toString(re3));
//        List<String> re4 = subtractMethodCalls_body_naive.PossibleScenarioObject();
//        System.out.println("Object: " + ArrayUtils.toString(re4));
//        //Test Subtract Class with naive technique v1.2
//
//
//        SubtractComponents_Body_CFG subtractMethodCalls_body_cfg = new SubtractComponents_Body_CFG();
//
//        try {
//            subtractMethodCalls_body_cfg.show_everything();
//        } catch (AnalysisCanceledException e1) {
//            System.out.println("CFG Error!");
//            e1.printStackTrace();
//        }
//        try {
//            System.out.println("\n" +"InstructionClsResult: " +
//                    ArrayUtils.toString(subtractMethodCalls_body_cfg.
//                            getAllInstructionClassPairs("testDeposit", "OverdraftAccountTest")));
//
//            System.out.println("\n" +"NodeRefResult: " +
//                    ArrayUtils.toString(subtractMethodCalls_body_cfg.
//                            getAllNodeReferencePair("testDeposit", "OverdraftAccountTest")));
//
//            System.out.println("\n" +"NodeCallResult: " +
//                    ArrayUtils.toString(subtractMethodCalls_body_cfg.
//                            getAllNodeCallPair("testDeposit", "OverdraftAccountTest")));
//
//            System.out.println("\n" + "AssertionCallResult: " +
//                    subtractMethodCalls_body_cfg.
//                            getAssertionCalls("testDeposit", "OverdraftAccountTest"));
//
//            System.out.println("\n" + "AssertionReferences: " +
//                    subtractMethodCalls_body_cfg.
//                            getAssertionReferences("testDeposit", "OverdraftAccountTest"));
//
//            System.out.println("\n" + "OtherCallResult: " +
//                    subtractMethodCalls_body_cfg.
//                            getOtherCalls("testDeposit", "OverdraftAccountTest"));
//
//
//            //More
//        } catch (AnalysisCanceledException e1) {
//            e1.printStackTrace();
//        }

        System.exit(0);
    }
}
