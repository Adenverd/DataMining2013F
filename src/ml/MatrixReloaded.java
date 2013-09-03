package ml;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MatrixReloaded {

    //Data
    public List<List<Double>> data;

    //Meta-data
    private int numCols;
    private int numRows;
    private Map<Integer, ColumnAttributes> columnAttributes; //maps column index to categorical attributes

    public int getNumCols(){
        return numCols;
    }

    public int getNumRows(){
        return numRows;
    }

    public MatrixReloaded(){
        numCols = 0;
        numRows = 0;

        data = new ArrayList<List<Double>>();
    }

    public void loadARFF(String filepath){
        throw new UnsupportedOperationException("Not Implemented");
    }

    public void saveARFF(String filepath){
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Appends a row to the end of the matrix
     * @param row
     */
    public void addRow(List<Double> row){
        /* Check to make sure that the number of columns in row matches the number of columns in the matrix */
        if(row.size() != data.size()){
            throw new MLException("Cannot add a row that doesn't match number of columns in matrix");
        }
    }

    /**
     * Appends an empty row to the end of the matrix
     */
    public void addRow(){
        List<Double> newRow = new ArrayList<Double>();
        data.add(newRow);
        numRows++;
    }

    public void addColumn(ColumnAttributes attributes){
        if (!data.isEmpty()){
            throw new UnsupportedOperationException("Cannot add a column to a matrix that contains rows");
        }

        columnAttributes.put(numCols, attributes);

        //numCols needs to be incremented AFTER putting the attributes
        numCols++;
    }

    /**
     * Swaps the locations of two rows in the matrix
     * @param row1
     * @param row2
     */
    public void swapRows(int row1, int row2){
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Returns the row at index rowNum in the matrix
     * @param row
     * @return
     */
    public List<Double> getRow(int row){
        return data.get(row);
    }

    public ColumnType getColumnType(int col){
        return columnAttributes.get(col).getColumnType();
    }

    public boolean isCategorical(int col){
        return columnAttributes.get(col).getColumnType()==ColumnType.Categorical;
    }

    public boolean isContinuous(int col){
        return columnAttributes.get(col).getColumnType()==ColumnType.Continuous;
    }

}
