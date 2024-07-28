package TestNamePattern;

import com.google.common.base.CaseFormat;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.parser.lexparser.LexicalizedParser;
import edu.stanford.nlp.process.PTBTokenizer;
import edu.stanford.nlp.process.TokenizerFactory;
import edu.stanford.nlp.semgraph.SemanticGraph;
import edu.stanford.nlp.semgraph.SemanticGraphFactory;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.stanford.nlp.trees.GrammaticalStructure;
import edu.stanford.nlp.trees.Tree;
import org.apache.commons.lang.ArrayUtils;

import java.io.StringReader;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ParseNameMethods {

    private static final TokenizerFactory<CoreLabel> TOKENIZER_FACTORY = PTBTokenizer.coreLabelFactory();

    private static final MaxentTagger MAXENT_TAGGER = new MaxentTagger(MaxentTagger.DEFAULT_JAR_PATH);

    private static String convertNameToSentence(String testName) {

        return Optional.ofNullable(testName)
                .map(name -> CaseFormat.LOWER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, name))
                .map(name -> name.replace("_", " "))
                .map(String::toLowerCase)
                .map(s -> String.format("I %s ", s))
                .orElse(null);
    }

    public List<Map.Entry<String, String>> parseNameWithBasicCoreNLP(String testName){

        List<Map.Entry<String, String>> results = new LinkedList<>();

        String sentence = convertNameToSentence(testName);

        List<CoreLabel> labels = TOKENIZER_FACTORY.getTokenizer(new StringReader(sentence)).tokenize();

        MAXENT_TAGGER.tagCoreLabels(labels);

        //Note: Adjust parts of speech here

        for (CoreLabel label : labels) {

            String word = label.get(CoreAnnotations.TextAnnotation.class);

            if (word.equals("changes") || word.equals("forwarding")) {
                label.setTag("VBG-1");
            }
            if (word.equals("set") && labels.indexOf(label) != labels.size()) {
                label.setTag("VB");
            }
            if (word.equals("get")) {
                label.setTag("VB");
            }
            if (word.equals("trim")) {
                label.setTag("VB");
            }
            if (word.equals("clone")) {
                label.setTag("VB");
            }
            if (word.equals("sort")) {
                label.setTag("VB");
            }
            if (word.equals("move")) {
                label.setTag("VB");
            }
            if (word.equals("hash") && (labels.indexOf(label) == 1 || labels.indexOf(label) == 2)) {
                label.setTag("VB");
            }
            if (word.equals("touch")) {
                label.setTag("VB");
            }
            if (word.equals("returns")) {
                label.setTag("VBG");
            }
            if (word.equals("check")) {
                label.setTag("VB");
            }
            if (word.equals("reuse")) {
                label.setTag("VB");
            }
            if (word.equals("instantiate")) {
                label.setTag("VB");
            }
            if (word.equals("resolve")) {
                label.setTag("VB");
            }
            if (word.equals("visit")) {
                label.setTag("VB");
            }
            if (word.equals("concat")) {
                label.setTag("VB");
            }
            if (word.equals("view")) {
                label.setTag("VB");
            }
            if (word.contains("bug") || word.contains("java")
                    || word.contains("constructor") || word.contains("week")
                    || word.contains("serialization") || word.contains("javascript")
                    || word.equals("hashcode") || word.equals("key")
                    || word.contains("correlation")) {
                label.setTag("NN");
            }
            if (word.equals("edges")){
                label.setTag("NNS");
            }
            if (word.equals("default")){
                label.setTag("JJ");
            }
            //Note: Adjust parts of speech here

            String pos = label.get(CoreAnnotations.PartOfSpeechAnnotation.class);

            results.add(new java.util.AbstractMap.SimpleEntry<>(word, pos));
        }
        return results;
    }
}

