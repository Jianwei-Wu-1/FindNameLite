package ExtrasForTestPattern;

import java.text.DecimalFormat;

public class PercentageCalculator_v3 {

    public double matched = 0.0;

    public double not_matched = 0.0;

    public void calculate(){

        System.out.println("\n\n Total # of Test Cases: " + (matched + not_matched));

        System.out.println("\n Total # of Matched Patterns: " + (matched));

        System.out.println("\n" + "***Percentage Result" + ":  "
                + new DecimalFormat("##.##").format(100.0 * (matched / (matched + not_matched))) +  "% ***");
    }

}
