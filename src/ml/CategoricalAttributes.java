package ml;

import java.util.ArrayList;
import java.util.List;

public class CategoricalAttributes {
    //Represents the mappings between numbers and string values for a category
    //e.g. if values.get(3) returns "iris-setosa", then the fourth possible value
    //of this enumeration is "iris-setosa"
    private List<String> values;

    public CategoricalAttributes(){
        values = new ArrayList<String>();
    }

    public String getValue(int index){
        return values.get(index);
    }

    public int getIndex(String value){
        return values.indexOf(value);
    }
}
