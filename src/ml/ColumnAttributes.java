package ml;

import java.util.ArrayList;
import java.util.List;

public class ColumnAttributes {

    private String columnName;
    private ColumnType columnType;

    //Represents the mappings between numbers and string values for a category
    //e.g. if values.get(3) returns "iris-setosa", then the fourth possible value
    //of this enumeration is "iris-setosa"
    private List<String> values;

    public ColumnAttributes(String columnName, ColumnType columnType){
        this.columnName = columnName;
        this.columnType = columnType;

        if(columnType == ColumnType.Categorical){
            values = new ArrayList<String>();
        }
    }

    public void addValue(String value){
        values.add(value);
    }

    public String getValue(int index){
        return values.get(index);
    }

    public int getIndex(String value){
        return values.indexOf(value);
    }

    public ColumnType getColumnType(){
        return columnType;
    }
}


