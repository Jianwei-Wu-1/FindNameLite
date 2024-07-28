package TestNamePattern.NamePattern_NLP;


public interface NameValidator {

    interface NameMatch {

        String getPatternName();

        String verifiedActionOfTest();

        String verifiedPredicateOfTest();

        String verifiedScenarioOfTest();
    }

    NameMatch matches(String testName);

}
