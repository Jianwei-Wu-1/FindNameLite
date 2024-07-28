package ParseNameExtractComponents;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import edu.stanford.nlp.ling.LabeledWord;
import edu.stanford.nlp.ling.Word;
import edu.stanford.nlp.parser.charniak.CharniakParser;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.WordTokenFactory;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.Trees;
import edu.stanford.nlp.util.ScoredObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class ParseNameWithBllipParser {

    public void parseByCharniakParser(){

        //Fixme: If cloned from Bitbucket, Please change all absolute path

        try {

            File stats = new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/Results/InProcessingDataForBllip.txt");

            FileWriter fw = new FileWriter(stats, true);

            BufferedWriter output = new BufferedWriter(fw);

            Project p = ProjectManager.getInstance().getOpenProjects()[0];

            final Collection<VirtualFile> all_files = FileTypeIndex.getFiles(JavaFileType.INSTANCE,GlobalSearchScope.projectScope(p));

            String parser_dir = "/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/bllip-parser";

            String parser_executable = "/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/bllip-parser/ParseNameMethods-50best.sh";

            CharniakParser charniakParser1 = new CharniakParser(parser_dir, parser_executable);

            //Setup for charniak parser

            for (VirtualFile virtualFile : all_files) {
                
                PsiFile file = PsiManager.getInstance(p).findFile(virtualFile);
                
                if (file instanceof PsiJavaFile &&
                        (file.getName().contains("Test")
                                || file.getName().contains("test"))) {
                    
                    for (PsiClass psiClass : ((PsiJavaFile) file).getClasses()) {
                        
                        for (PsiMethod test : psiClass.getAllMethods()) {

                            SplitTestName splitTestName1 = new SplitTestName();
                            
                            String single_test_name = splitTestName1.nameSplitToString(test.getName());
                            
                            output.write("<s>" + single_test_name + " </s>\n");
                            
                            PTBTokenizer<Word> ptbTokenizer1 = new PTBTokenizer<>(
                                    new StringReader(single_test_name), new WordTokenFactory(), "");
                            
                            List<Word> name1 = new ArrayList<>();
                            
                            for (Word token; ptbTokenizer1.hasNext(); ) {
                                token = ptbTokenizer1.next();
                                name1.add(token);
                                output.write("tokens: " + token.toString() +"\n");
                            }
                            
                            //Setup each test name for individual parsing
                            //charniakParser1.printSentence(name1,"/Users/wujianwei/Desktop/FindName/Output/cur_sentence.txt");
                            //Print current sentence
                            
                            List<ScoredObject<Tree>> trees_20 = charniakParser1.getKBestParses(name1, 3);
                            
                            for (int i = 0; i < 3; i++){
                                
                                output.write("No." + i +"\n");
                                
                                output.write("Parsed score: " + trees_20.get(i).score() + "\n");
                                
                                output.write("Parsed tree: " + trees_20.get(i).object().toString() + "\n");
                                
                                Tree each_tree = Trees.readTree(trees_20.get(i).object().toString());
                                
                                each_tree.indentedListPrint();
                                
                                output.write("Tree size: "+ each_tree.size() +
                                        " value:" + each_tree.value() +
                                        " nodeString: " + each_tree.nodeString() + "\n");
                                
                                for (Tree a : each_tree.getLeaves()
                                        ) {
                                    
                                    output.write("Leaves: "+a.toString() + "\n");
                                }
                            }
                            
                            output.write("End for this test name" + "\n");
                            //Split the test name into words and get tree after parsing
                        }
                    }
                }
            }
            output.close();

            //Complete the stats file

            charniakParser1.runCharniak(
                    50,
                    "/Users/wujianwei/Documents/ideaProjects1/FindNameLite/Results/InProcessingDataForBllip.txt",
                    "/Users/wujianwei/Documents/ideaProjects1/FindNameLite/Results/Run50ResultsForBllip.txt",
                    "/Users/wujianwei/Documents/ideaProjects1/FindNameLite/Results/ErrForBllip.txt");
            //Parse all test names from a single file
        }
        catch (IOException ioe1){
            ioe1.fillInStackTrace();
        }
    }

    public List<String> getResultForOneTestName(String TestName){
        List<String> returnedList = new LinkedList<>();
        try {
            File stats = new File("/Users/wujianwei/Documents/ideaProjects1/FindNameLite/Results/InProcessingDataForBllip.txt");
            FileWriter fw = new FileWriter(stats, true);
            BufferedWriter output = new BufferedWriter(fw);
            Project p = ProjectManager.getInstance().getOpenProjects()[0];
            final Collection<VirtualFile> all_files = FileTypeIndex.getFiles(JavaFileType.INSTANCE, GlobalSearchScope.projectScope(p));
            String parser_dir = "/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/bllip-parser";
            String parser_executable = "/Users/wujianwei/Documents/ideaProjects1/FindNameLite/libs/bllip-parser/ParseNameMethods-50best.sh";
            CharniakParser charniakParser1 = new CharniakParser(parser_dir, parser_executable);
            //Setup for charniak parser
            SplitTestName splitTestName1 = new SplitTestName();
            String single_test_name = splitTestName1.nameSplitToString(TestName);
            output.write("<s>" + single_test_name + " </s>\n");
            //Write output file for Parsing
            PTBTokenizer<Word> ptbTokenizer1 = new PTBTokenizer<>(
                    new StringReader(single_test_name), new WordTokenFactory(), "");
            List<Word> cur_name = new ArrayList<>();
            for (Word token; ptbTokenizer1.hasNext(); ) {
                token = ptbTokenizer1.next();
                cur_name.add(token);
                output.write("tokens: " + token.toString() +"\n");
            }
            output.write("\n\n");
            List<ScoredObject<Tree>> trees_20 = charniakParser1.getKBestParses(cur_name, 1);
            output.write("No." + 0 + "\n");
            output.write("Parsed score: " + trees_20.get(0).score() + "\n");
            output.write("Parsed tree: " + trees_20.get(0).object().toString() + "\n");
            Tree each_tree = Trees.readTree(trees_20.get(0).object().toString());
            each_tree.indentedListPrint();
            output.write("Tree size: " + each_tree.size() +
                    " value:" + each_tree.value() +
                    " nodeString: " + each_tree.nodeString() + "\n");
            for (Tree a : each_tree.getLeaves()) {
                output.write("Leaves: " + a.label().toString()+" "+"\n");
            }
            for (LabeledWord each_word : each_tree.labeledYield()){
                output.write("each word: " + each_word.toString() + "\n");
                returnedList.add(each_word.toString());
            }
            output.write("End for this test name" + "\n");
            //Split the test name into words and get tree after parsing
            output.close();
        }
        catch (Exception e2){
            e2.printStackTrace();
        }
        return returnedList;
    }

}
