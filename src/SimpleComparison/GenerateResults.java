package SimpleComparison;

import evaluate_bad_test_names.PatternUtils;
import org.junit.Assert;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class GenerateResults {

    private PatternUtils patternUtils;

    public GenerateResults(){

        patternUtils = new PatternUtils();

        patternUtils.line_of_code_threshold = 30;
    }

    public void startProcess() throws IOException {

        List<String> action_of_body;

        List<String> predicate_of_body;

        List<String> scenario_of_body;

        List<String> action_of_name;

        List<String> predicate_of_name;

        List<String> scenario_of_name;

        try {
            patternUtils.searchForPatterns();
        }
        catch (Exception e1) {
            e1.printStackTrace();
        }

        //Test Body Pattern

        action_of_body = patternUtils.test_body_action;

        predicate_of_body = patternUtils.test_body_predicate;

        scenario_of_body = patternUtils.test_body_scenario;

        action_of_name = patternUtils.test_name_action;

        predicate_of_name = patternUtils.test_name_predicate;

        scenario_of_name = patternUtils.test_name_scenario;
        //Test Name Pattern
//        System.out.println("act body: "+action_of_body.size());
//        System.out.println("act name: "+action_of_name.size());
//        System.out.println("pred body: "+predicate_of_body.size());
//        System.out.println("pred name: "+predicate_of_name.size());
//        System.out.println("sc body: "+scenario_of_body.size());
//        System.out.println("sc name: "+scenario_of_name.size());
        //Make sure it has equal length

        for (int i = 0; i < patternUtils.test_names.size(); i++) {

//            Path path = Paths.get("/Users/wujianwei/Documents/ideaProjects1" +
//                    "/FindNameLite/StatisticsForSimpleComparison/FinalResult.txt");

            Path path_new = Paths.get("/Users/wujianwei/Documents/ideaProjects1" +
                    "/FindNameLite/StatisticsForSimpleComparison/FinalResult_new.txt");

            String name = patternUtils.test_classes.get(i) + "." + patternUtils.test_names.get(i) + "\n";
            byte[] print0 = name.getBytes();
            Files.write(path_new, print0, StandardOpenOption.APPEND);

//            byte[] print0_new = name.getBytes();
//            Files.write(path_new, print0_new, StandardOpenOption.APPEND);
//
//            if (action_of_body.size() != 0) {
//                String b1 = "Action: " + action_of_body.get(i) + "\n";
//                byte[] print1 = b1.getBytes();
//                Files.write(path_new, print1, StandardOpenOption.APPEND);
//            }
//            else{
//                String b1 = "Action: " + "N/A" + "\n";
//                byte[] print1 = b1.getBytes();
//                Files.write(path_new, print1, StandardOpenOption.APPEND);
//            }
//
//            if (predicate_of_body.size() != 0) {
//                String b2 = "Body predicate: " + predicate_of_body.get(i) + "\n";
//                byte[] print2 = b2.getBytes();
//                Files.write(path_new, print2, StandardOpenOption.APPEND);
//            }
//            else{
//                String b2 = "Body predicate: " + "N/A" + "\n";
//                byte[] print2 = b2.getBytes();
//                Files.write(path_new, print2, StandardOpenOption.APPEND);
//            }
//
//            if (scenario_of_body.size() != 0) {
//                String b3 = "Body scenario: " + scenario_of_body.get(i) + "\n";
//                byte[] print3 = b3.getBytes();
//                Files.write(path_new, print3, StandardOpenOption.APPEND);
//            }
//            else {
//                String b3 = "Body scenario: " + "N/A"  + "\n";
//                byte[] print3 = b3.getBytes();
//                Files.write(path_new, print3, StandardOpenOption.APPEND);
//            }
//
//            if (action_of_name.size() != 0) {
//                String n1 = "Name action: " + action_of_name.get(i) + "\n";
//                byte[] print4 = n1.getBytes();
//                Files.write(path_new, print4, StandardOpenOption.APPEND);
//            }
//            else {
//                String n1 = "Name action: " + "N/A" + "\n";
//                byte[] print4 = n1.getBytes();
//                Files.write(path_new, print4, StandardOpenOption.APPEND);
//            }
//
//            if (predicate_of_name.size() != 0) {
//                String n2 = "Name predicate: " + predicate_of_name.get(i) + "\n";
//                byte[] print5 = n2.getBytes();
//                Files.write(path_new, print5, StandardOpenOption.APPEND);
//            }
//            else{
//                String n2 = "Name predicate: " + "N/A" + "\n";
//                byte[] print5 = n2.getBytes();
//                Files.write(path_new, print5, StandardOpenOption.APPEND);
//            }
//
//            if (scenario_of_name.size() != 0) {
//                String n3 = "Name scenario: " + scenario_of_name.get(i) + "\n";
//                byte[] print6 = n3.getBytes();
//                Files.write(path_new, print6, StandardOpenOption.APPEND);
//            }
//            else {
//                String n3 = "Name scenario: " + "N/A" + "\n";
//                byte[] print6 = n3.getBytes();
//                Files.write(path_new, print6, StandardOpenOption.APPEND);
//            }
//            Files.write(path_new, Collections.singleton("\n"),StandardOpenOption.APPEND);

            String action_name;
            if (action_of_name.get(i) == null){
                action_name = "N/A";
            }
            else{
                action_name = action_of_name.get(i).toLowerCase().trim();
            }
            String action_body;
            if (action_of_body.get(i) == null){
                action_body = "N/A";
            }
            else{
                action_body = action_of_body.get(i).toLowerCase().trim();
            }
            String predicate_name;
            if (predicate_of_name.get(i) == null){
                predicate_name = "N/A";
            }
            else{
                predicate_name = predicate_of_name.get(i).toLowerCase().trim();
            }
            String predicate_body;
            if (predicate_of_body.get(i) == null){
                predicate_body = "N/A";
            }
            else{
                predicate_body = predicate_of_body.get(i).toLowerCase().trim();
            }
            String scenario_name;
            if (scenario_of_name.get(i) == null){
                scenario_name = "N/A";
            }
            else{
                scenario_name = scenario_of_name.get(i).toLowerCase().trim();
            }
            String scenario_body;
            if (scenario_of_body.get(i) == null){
                scenario_body = "N/A";
            }
            else{
                scenario_body = scenario_of_body.get(i).toLowerCase().trim();
            }
            //Preset the process

            if (action_name.contains("(") && (action_name.indexOf("(") <= action_name.length())){
                action_name = action_name.substring(0, action_name.indexOf("("));
                action_name = action_name.trim();
            }
            if (predicate_name.contains("(") && (predicate_name.indexOf("(") <= predicate_name.length())){
                predicate_name = predicate_name.substring(0, predicate_name.indexOf("("));
                predicate_name = predicate_name.trim();
            }
            if (scenario_name.contains("(") && (scenario_name.indexOf("(") <= scenario_name.length())){
                scenario_name = scenario_name.substring(0, scenario_name.indexOf("("));
                scenario_name = scenario_name.trim();
            }

            String action_body_1 = action_body.trim();
            String predicate_body_1 = predicate_body.trim();
            String scenario_body_1 = scenario_body.trim();

            if (action_body.contains("(") && (action_body.indexOf("(") <= action_body.length())){
                action_body_1 = action_body_1.substring(0, action_body_1.indexOf("("));
                action_body_1 = action_body_1.trim();
            }
            if (predicate_body.contains("(") && (predicate_body.indexOf("(") <= predicate_body.length())){
                predicate_body_1 = predicate_body_1.substring(0, predicate_body_1.indexOf("("));
                predicate_body_1 = predicate_body_1.trim();
            }
            if (scenario_body.contains("(") && (scenario_body.indexOf("(") <= scenario_body.length())){
                scenario_body_1 = scenario_body_1.substring(0, scenario_body_1.indexOf("("));
                scenario_body_1 = scenario_body_1.trim();
            }
            //Action, predicates, scenarios

//            int count_match = 0;

            if (action_body.contains(action_name)) {
                if (action_name.contains("n/a") && action_body.contains("n/a")){
                    String a = "Action: " + "n/a" + " == " + "n/a" + "\n";
                    byte[] print = a.getBytes();
                    Files.write(path_new, print, StandardOpenOption.APPEND);
                }
                else {
                    if (action_body.startsWith(action_name)) {
                        String a = "Action: " + action_body.substring(0, action_name.length()).trim() + " == " + action_of_name.get(i).substring(0, action_of_name.get(i).indexOf("(")).trim() + "\n";
                        byte[] print = a.getBytes();
                        Files.write(path_new, print, StandardOpenOption.APPEND);
                    }
                    else {
                        String a = "Action: " + action_body.substring(action_body.indexOf(action_name), action_body.indexOf(action_name) + action_name.length()) + " == " + action_of_name.get(i).substring(0, action_of_name.get(i).indexOf("(")).trim() + "\n";
                        byte[] print = a.getBytes();
                        Files.write(path_new, print, StandardOpenOption.APPEND);
                    }
                }
            }
            else {
                String a = "Action: " + action_body_1 + " != " + action_name.trim() + "\n";
                byte[] print = a.getBytes();
                Files.write(path_new, print, StandardOpenOption.APPEND);
            }

            if (predicate_body.contains(predicate_name)) {
                if (predicate_name.contains("n/a") && predicate_body.contains("n/a")){
                    String a = "Predicate: " + "n/a" + " == " + "n/a" + "\n";
                    byte[] print = a.getBytes();
                    Files.write(path_new, print, StandardOpenOption.APPEND);
                }
                else {
                    if (predicate_body.startsWith(predicate_name)) {
                        String a = "Predicate: " + predicate_body.substring(0, predicate_name.length()).trim() + " == " + predicate_of_name.get(i).substring(0, predicate_of_name.get(i).indexOf("(")).trim() + "\n";
                        byte[] print = a.getBytes();
                        Files.write(path_new, print, StandardOpenOption.APPEND);
                    }
                    else {
                        String a = "Predicate: " + predicate_body.substring(predicate_body.indexOf(predicate_name), predicate_body.indexOf(predicate_name) + predicate_name.length()) + " == " + predicate_of_name.get(i).substring(0, predicate_of_name.get(i).indexOf("(")).trim() + "\n";
                        byte[] print = a.getBytes();
                        Files.write(path_new, print, StandardOpenOption.APPEND);
                    }
                }
            } else {
                String a = "Predicate: " + predicate_body_1 + " != " + predicate_name.trim() + "\n";
                byte[] print = a.getBytes();
                Files.write(path_new, print, StandardOpenOption.APPEND);
            }

            if (scenario_body.contains(scenario_name)) {
                if(scenario_name.contains("n/a") && scenario_body.contains("n/a")){
                    String a = "Scenario: " + "n/a" + " == " + "n/a" + "\n";
                    byte[] print = a.getBytes();
                    Files.write(path_new, print, StandardOpenOption.APPEND);
                }
                else {
                    if (scenario_body.startsWith(scenario_name)) {
                        String a = "Scenario: " + scenario_body.substring(0, scenario_name.length()).trim() + " == " + scenario_of_name.get(i).substring(0, scenario_of_name.get(i).indexOf("(")).trim() + "\n";
                        byte[] print = a.getBytes();
                        Files.write(path_new, print, StandardOpenOption.APPEND);
                    }
                    else {
                        String a = "Scenario: " + scenario_body.substring(scenario_body.indexOf(scenario_name), scenario_body.indexOf(scenario_name) + scenario_name.length()) + " == " + scenario_of_name.get(i).substring(0, scenario_of_name.get(i).indexOf("(")).trim() + "\n";
                        byte[] print = a.getBytes();
                        Files.write(path_new, print, StandardOpenOption.APPEND);
                    }
                }
            } else {
                String a = "Scenario: " + scenario_body_1 + " != " + scenario_name.trim() + "\n";
                byte[] print = a.getBytes();
                Files.write(path_new, print, StandardOpenOption.APPEND);
            }
            Files.write(path_new, Collections.singleton("\n"),StandardOpenOption.APPEND);
        }
    }
}
