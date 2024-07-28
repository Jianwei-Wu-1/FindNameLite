package ExtrasForTestPattern;

import TestNamePattern.RegexForTestNamePattern_v1_1Kt;
import com.intellij.psi.PsiDeclarationStatement;
import com.intellij.psi.PsiMethodCallExpression;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class NameElement_CheckerAndMatcher_v1 {

    public boolean checkIfMatchWithName_Constructor(PsiDeclarationStatement statement, String name){

        AtomicBoolean indicator = new AtomicBoolean();

        List<PsiMethodCallExpression> inside_constructor_mcs = new DeclarationAnalyzer_v2().extractMethodCallsFromConstructor(statement);

        if (inside_constructor_mcs == null) return false;

        String parsed_name = RegexForTestNamePattern_v1_1Kt.parseName(name);

        List<String> words_from_name = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_name));

        words_from_name = words_from_name.stream().map(String::toLowerCase).collect(Collectors.toList());

        System.out.println("name: " + ArrayUtils.toString(words_from_name));

        List<String> finalWords_from_name = words_from_name;
        inside_constructor_mcs.forEach(methodCall -> {

            String mc = methodCall.getMethodExpression().getReferenceName();

            if (mc != null) {

                String parsed_mc = RegexForTestNamePattern_v1_1Kt.parseName(mc);

                List<String> words_from_mc = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_mc));

                words_from_mc = words_from_mc.stream().map(String::toLowerCase).collect(Collectors.toList());

                if (finalWords_from_name.containsAll(words_from_mc)) {
                    indicator.set(true);
                }
            }
        });

        return indicator.get();
    }

    public boolean checkIfMatchWithName_ChainedMethodCall(PsiMethodCallExpression statement, String name){

        AtomicBoolean indicator = new AtomicBoolean();

        List<PsiMethodCallExpression> deChainedMcs = new ChainedMethodCallAnalyzer_v1().getDeChainedMcs(statement);

        if (deChainedMcs == null) return false;

        String parsed_name = RegexForTestNamePattern_v1_1Kt.parseName(name);

        List<String> words_from_name = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_name));

        words_from_name = words_from_name.stream().map(String::toLowerCase).collect(Collectors.toList());

        System.out.println("name: " + ArrayUtils.toString(words_from_name));

        List<String> finalWords_from_name = words_from_name;

        deChainedMcs.forEach(methodCall -> {

            String mc = methodCall.getMethodExpression().getReferenceName();

            if (mc != null) {

                String parsed_mc = RegexForTestNamePattern_v1_1Kt.parseName(mc);

                List<String> words_from_mc = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_mc));

                words_from_mc = words_from_mc.stream().map(String::toLowerCase).collect(Collectors.toList());


                if (finalWords_from_name.containsAll(words_from_mc)){
                    indicator.set(true);
                }
            }
        });
        return indicator.get();
    }

    public boolean checkIfMatchWithName_AllMC(PsiMethodCallExpression statement, String name){

        AtomicBoolean indicator = new AtomicBoolean();

        List<PsiMethodCallExpression> allMethodCalls = new ChainedMethodCallAnalyzer_v1().getAllMethodCalls(statement);

        if (allMethodCalls == null) return false;

        String parsed_name = RegexForTestNamePattern_v1_1Kt.parseName(name);

        List<String> words_from_name = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_name));

        words_from_name = words_from_name.stream().map(String::toLowerCase).collect(Collectors.toList());

        System.out.println("name: " + ArrayUtils.toString(words_from_name));

        List<String> finalWords_from_name = words_from_name;

        allMethodCalls.forEach(methodCall -> {

            String mc = methodCall.getMethodExpression().getReferenceName();

            if (mc != null) {

                String parsed_mc = RegexForTestNamePattern_v1_1Kt.parseName(mc);

                List<String> words_from_mc = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_mc));

                words_from_mc = words_from_mc.stream().map(String::toLowerCase).collect(Collectors.toList());

                if (finalWords_from_name.containsAll(words_from_mc)){
                    indicator.set(true);
                }
            }
        });
        return indicator.get();
    }

    public PsiMethodCallExpression getMatchedMC_AllMC(PsiMethodCallExpression statement, String name){

        AtomicReference<PsiMethodCallExpression> result = new AtomicReference<>();

        List<PsiMethodCallExpression> allMethodCalls = new ChainedMethodCallAnalyzer_v1().getAllMethodCalls(statement);

        if (allMethodCalls == null) return null;

        String parsed_name = RegexForTestNamePattern_v1_1Kt.parseName(name);

        List<String> words_from_name = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_name));

        words_from_name = words_from_name.stream().map(String::toLowerCase).collect(Collectors.toList());

        System.out.println("name: " + ArrayUtils.toString(words_from_name));

        List<String> finalWords_from_name = words_from_name;

        allMethodCalls.forEach(methodCall -> {

            String mc = methodCall.getMethodExpression().getReferenceName();

            if (mc != null) {

                String parsed_mc = RegexForTestNamePattern_v1_1Kt.parseName(mc);

                List<String> words_from_mc = Arrays.asList(StringUtils.splitByCharacterTypeCamelCase(parsed_mc));

                words_from_mc = words_from_mc.stream().map(String::toLowerCase).collect(Collectors.toList());

                if (finalWords_from_name.containsAll(words_from_mc)){
                    result.set(methodCall);
                }
            }
        });
        return result.get();
    }
}
