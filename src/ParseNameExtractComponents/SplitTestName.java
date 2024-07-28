package ParseNameExtractComponents;

import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SplitTestName {
    public String[] nameSplitToArray(String name){
        return StringUtils.splitByCharacterTypeCamelCase(name);
    }
    public String nameSplitToString(String name){
        String[] re = StringUtils.splitByCharacterTypeCamelCase(name);
        StringBuilder result = new StringBuilder();
        for (String part: re) {
            result.append(" ").append(part);
        }
        return result.toString();
    }
    public List<String> nameSplitToList(String name){
        String[] re = StringUtils.splitByCharacterTypeCamelCase(name);
        List<String> result = new ArrayList<>(Arrays.asList(re));
        return result;
    }
}