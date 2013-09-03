package ml;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MatrixReloaded {

    public static final Double UNKNOWN_VALUE = Double.NEGATIVE_INFINITY;

    //Data
    public List<List<Double>> data;

    //Meta-data
    private int numCols;
    private Map<Integer, ColumnAttributes> columnAttributes; //maps column index to categorical attributes

    /**
     * Returns the number of columns in the matrix
     * @return
     */
    public int getNumCols(){
        return numCols;
    }

    /**
     * Returns the number of rows in the matrix
     * @return
     */
    public int getNumRows(){
        return data.size();
    }

    /**
     * Creates an empty matrix
     */
    public MatrixReloaded(){
        numCols = 0;

        data = new ArrayList<List<Double>>();
        columnAttributes = new HashMap<Integer, ColumnAttributes>();
    }

    /**
     * Appends a row to the end of the matrix
     * @param row
     */
    public void addRow(List<Double> row){
        /* Check to make sure that the number of columns in row matches the number of columns in the matrix */
        if(row.size() != data.size()){
            throw new MLException("Cannot add a row with mismatching number of columns to matrix");
        }

        for (Double value : row){
            if (value == null){
                throw new MLException("Cannot add a row with a null Double value");
            }
        }

        data.add(row);
    }

    /**
     * Appends an empty row to the end of the matrix
     */
    public void addRow(){
        List<Double> newRow = new ArrayList<Double>();
        data.add(newRow);
    }

    /**
     * Adds a new column to the matrix (throws if matrix is not empty)
     * @param attributes
     */
    public void addColumn(ColumnAttributes attributes){
        if (!data.isEmpty()){
            throw new UnsupportedOperationException("Cannot add a column to a matrix that contains rows");
        }

        columnAttributes.put(numCols, attributes);

        //numCols needs to be incremented AFTER putting the column attributes
        numCols++;
    }

    /**
     * Swaps the locations of two rows in the matrix
     * @param row1
     * @param row2
     */
    public void swapRows(int row1, int row2){
        List<Double> tempRow = getRow(row1);

        data.remove(row1);
        data.add(row1, data.get(row2));

        data.remove(row2);
        data.add(row2, tempRow);
    }

    /**
     * Returns the row at index rowNum in the matrix
     * @param row
     * @return
     */
    public List<Double> getRow(int row){
        return data.get(row);
    }

    public ColumnAttributes getColumnAttributes(int col){
        return columnAttributes.get(col);
    }

    /**
     * Returns the type of column col
     * @param col
     * @return
     */
    public ColumnType getColumnType(int col){
        return columnAttributes.get(col).getColumnType();
    }

    /**
     * Returns true if column col is of type ColumnType.Categorical, false otherwise
     * @param col
     * @return
     */
    public boolean isCategorical(int col){
        return columnAttributes.get(col).getColumnType()==ColumnType.Categorical;
    }

    /**
     * Returns true if column col is of type ColumnType.Continuous, false otherwise
     * @param col
     * @return
     */
    public boolean isContinuous(int col){
        return columnAttributes.get(col).getColumnType()==ColumnType.Continuous;
    }

    public Double columnMean(int col){
        if(!isContinuous(col)){
            throw new MLException("Cannot calculate the mean of a non-continuous column");
        }

        double sum = 0.0;
        int count = 0;
        for (List<Double> row : data) {
            double val = row.get(col);
            if (val != UNKNOWN_VALUE) {
                sum += val;
                count++;
            }
        }
        return sum / count;
    }


    public void printMatrix() {

        // Stuffz
        System.out.println(columnAttributes);

        // Column names
        for (int i = 0; i < columnAttributes.size(); i++) {
            System.out.print(columnAttributes.get(i).getName());
        }
        System.out.println();

        for (int i = 0; i < getNumRows(); i++) {
            List<Double> row = getRow(i);
            for (int j = 0; j < getNumCols(); j++) {
                System.out.print(row.get(j));
            }
            System.out.println();
        }
    }

}
