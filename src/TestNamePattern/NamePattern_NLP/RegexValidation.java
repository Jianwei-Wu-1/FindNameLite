package TestNamePattern.NamePattern_NLP;

import SubtractCompFromBody.SubtractComponents_Body;
import TestNamePattern.RegexForTestNamePattern_v1_1Kt;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiClass;
import com.intellij.psi.PsiMethod;
import evaluate_bad_test_names.TestPattern_ClassUnderTestUtils;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang3.text.WordUtils;

import java.util.*;

public class RegexValidation implements NameValidator{

    private String action;

    private String predicate;

    private String scenario;

    private boolean AllMethodUnderTestHelper (List<PsiMethod> methods, String name){

        Map<String, String> regex_results_entityOnly = RegexForTestNamePattern_v1_1Kt.getEntityMatch(name);

        String entity = WordUtils.uncapitalize(regex_results_entityOnly.get("subject"));

        Set<String> method_names = new HashSet<>();

        for (PsiMethod method : methods){

            method_names.add(method.getName());
        }

        if (method_names.contains(entity)){
            action = entity;
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public NameMatch matches(String testName) {

        Map<String, String> regex_results = RegexForTestNamePattern_v1_1Kt.getMatch(testName);

        action = regex_results.get("subject");

        predicate = regex_results.get("predicate");

        scenario = regex_results.get("scenario");

        String name = testName.replace("_", " ");

        Project[] current_projects = new SubtractComponents_Body().cur_projects;

        List<PsiMethod> MUT = new LinkedList<>();

        for (Project project : current_projects){

            for (PsiClass cls : TestPattern_ClassUnderTestUtils.ClassUnderTest(project)){

                MUT.addAll(TestPattern_ClassUnderTestUtils.getAllMethodsForCUT(cls));
            }
        }

        if (this.AllMethodUnderTestHelper(MUT, testName) && action != null) {

            return new NameMatch() {
                @Override
                public String getPatternName() {
                    return "RegexMatch_SingleEntity";
                }

                @Override
                public String verifiedActionOfTest() {
                    return action;
                }

                @Override
                public String verifiedPredicateOfTest() {
                    return "N/A";
                }

                @Override
                public String verifiedScenarioOfTest() {
                    return "N/A";
                }
            };
        }

        String[] words = name.split(" ");

        if (predicate != null) {
            for (String word :
                    words) {

                if (word.startsWith(predicate)) {

                    predicate = word;
                }
            }
        }

        if (action != null) {
            for (String word :
                    words) {

                if (word.contains(action)
                        &&
                    action.length() > (word.length()/2)) {

                    action = word;
                }
            }
        }

        return new NameMatch() {
            @Override
            public String getPatternName() {
                return "RegexMatch";
            }

            @Override
            public String verifiedActionOfTest() {
                return action;
            }

            @Override
            public String verifiedPredicateOfTest() {
                return predicate;
            }

            @Override
            public String verifiedScenarioOfTest() {
                return scenario;
            }
        };
    }
}
