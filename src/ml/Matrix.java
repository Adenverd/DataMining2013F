package ml;

import java.util.*;
import ml.ColumnAttributes.ColumnType;

public class Matrix {

    public static final Double UNKNOWN_VALUE = Double.NEGATIVE_INFINITY;

    // Data
    public List<List<Double>> data;

    // Meta-data
    private List<ColumnAttributes> columnAttributes; // maps column index to categorical attributes


    /**
     * Creates an empty matrix
     */
    public Matrix() {
        data = new ArrayList<List<Double>>();
        columnAttributes = new ArrayList<ColumnAttributes>();
    }

    /**
     * Performs a deep-copy of the other Matrix
     * @param other
     */
    public Matrix(Matrix other){
        this.data = new ArrayList<List<Double>>();

        for(ColumnAttributes attr : other.columnAttributes){
            this.columnAttributes.add(new ColumnAttributes(attr));
        }

        for(List<Double> row : other.data){
            this.data.add(new ArrayList<Double>(row));
        }
    }

    /**
     * Returns the number of columns in the matrix
     */
    public int getNumCols() {
        return columnAttributes.size();
    }

    /**
     * Returns the number of rows in the matrix
     */
    public int getNumRows() {
        return data.size();
    }

    /**
     * Appends an empty row to the end of the matrix
     */
    public void addRow() {
        List<Double> newRow = new ArrayList<Double>();
        data.add(newRow);
    }

    /**
     * Appends a row to the end of the matrix
     */
    public void addRow(List<Double> row) {
        // Check to make sure that the number of columns in row matches the number of columns in the matrix
        if (row.size() != getNumCols()) {
            throw new MLException("Cannot add a row with mismatching number of columns to matrix");
        }
        for (Double value : row) {
            if (value == null) {
                throw new MLException("Cannot add a row with a null Double value");
            }
        }
        data.add(row);
    }

    /**
     * Adds a new column to the matrix (throws if matrix is not empty)
     * @param attributes
     */
    public void addColumn(ColumnAttributes attributes) {
        if (!data.isEmpty()) {
            throw new UnsupportedOperationException("Cannot add a column to a matrix that contains rows");
        }
        columnAttributes.add(attributes);
    }

    /**
     * Swaps the locations of two rows in the matrix
     * @param row1
     * @param row2
     */
    public void swapRows(int row1, int row2) {
        List<Double> tempRow = getRow(row1);
        data.set(row1, data.get(row2));
        data.set(row2, tempRow);
    }

    /**
     * Returns the row at index rowNum in the matrix
     *
     * @param row
     */
    public List<Double> getRow(int row) {
        return data.get(row);
    }

    /**
     * Returns the attributes of column col
     *
     * @param col
     */
    public ColumnAttributes getColumnAttributes(int col) {
        return columnAttributes.get(col);
    }

    /**
     * Returns the type of column col
     *
     * @param col
     */
    public ColumnType getColumnType(int col) {
        return columnAttributes.get(col).getColumnType();
    }

    /**
     * Returns true if column col is of type ColumnType.Categorical, false otherwise
     *
     * @param col
     */
    public boolean isCategorical(int col) {
        return getColumnType(col) == ColumnType.CATEGORICAL;
    }

    /**
     * Returns true if column col is of type ColumnType.Continuous, false otherwise
     *
     * @param col
     */
    public boolean isContinuous(int col) {
        return getColumnType(col) == ColumnType.CONTINUOUS;
    }

    public Double columnMean(int col) {
        if (!isContinuous(col)) {
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

    /**
     * Returns the min elements in the specified column
     * (Elements with the value UNKNOWN_VALUE are ignored.)
     * If no elements in a column have a value, returns
     * UNKNOWN_VALUE
     *
     * @param col
     */
    public Double columnMin(int col) {
        if (!isContinuous(col)) {
            throw new MLException("Cannot calculate the min of a non-continuous column");
        }
        boolean allUnknownValues = true;
        Double min = Double.MAX_VALUE;
        for (List<Double> row : data) {
            double val = row.get(col);
            if (val != UNKNOWN_VALUE) {
                allUnknownValues = false;
                min = min < val ? min : val;
            }
        }
        if (allUnknownValues) {
            return UNKNOWN_VALUE;
        }
        return min;
    }

    /**
     * Returns the max elements in the specified column.
     * (Elements with the value UNKNOWN_VALUE are ignored.)
     * If no element in the column has a value, returns
     * UNKNOWN_VALUE.
     *
     * @param col
     */
    public Double columnMax(int col) {
        if (!isContinuous(col)) {
            throw new MLException("Cannot calculate the max of a non-continuous column");
        }
        boolean allUnknownValues = true;
        Double max = Double.MIN_VALUE;
        for (List<Double> row : data) {
            double val = row.get(col);
            if (val != UNKNOWN_VALUE) {
                allUnknownValues = false;
                max = max > val ? max : val;
            }
        }
        if (allUnknownValues) {
            return UNKNOWN_VALUE;
        }
        return max;
    }

    /**
     * Removes a section of the matrix.
     * @param startRow Start index of section to remove, inclusive.
     * @param endRow End index of section to remove, exclusive.
     */
    public void removeSubMatrix(int startRow, int endRow){
        for (int i = startRow; i < endRow; i++){
            data.remove(i);
        }
    }

    /**
     * Removes a fold from the matrix, returning the sub-Matrix that was removed. Useful for n-fold
     * cross-validation.
     * @param startRow Start index of fold to remove, inclusive.
     * @param endRow End index of fold to remove, exclusive.
     * @return
     */
    public Matrix removeFold(int startRow, int endRow){
        Matrix subMatrix = subMatrixRows(startRow, endRow);
        this.removeSubMatrix(startRow, endRow);
        return subMatrix;
    }

    /**
     * Returns the most common value in the specified column.
     * (Elements with the value UNKNOWN_VALUE are ignored.)
     * If all elements are UNKNOWN_VALUE, returns UNKNOWN_VALUE.
     *
     * @param col
     */
    public Double mostCommonValue(int col) {
        boolean allUnknownValues = true;
        Map<Double, Integer> frequencies = new HashMap<Double, Integer>();
        for (List<Double> row : data) {
            double val = row.get(col);

            if (val != UNKNOWN_VALUE) {
                allUnknownValues = false;
                if (frequencies.containsKey(val)) {
                    frequencies.put(val, frequencies.get(val) + 1);
                } else {
                    frequencies.put(val, 1);
                }
            }
        }

        if (allUnknownValues) {
            return UNKNOWN_VALUE;
        }

        Set<Double> keySet = frequencies.keySet();
        Double mostCommonValue = UNKNOWN_VALUE;
        int highestFrequency = 0;

        for (Double key : keySet) {
            int val = frequencies.get(key);
            if (val > highestFrequency) {
                mostCommonValue = key;
                highestFrequency = val;
            }
        }
        return mostCommonValue;
    }

    /**
     * Returns a section of the matrix as a new matrix, with the same column attributes.
     * Note: This operation does a deep-copy and is expensive.
     *
     * @param startRowIndex Start index row, inclusive.
     * @param endRowIndex End index row, exclusive.
     */
    public Matrix subMatrixRows(int startRowIndex, int endRowIndex) {
        return subMatrix(startRowIndex, endRowIndex, 0, getNumCols());
    }

    /**
     * Returns a section of the matrix as a new matrix, with the row values.
     * Note: This operation does a deep-copy and is expensive.
     *
     * @param startColIndex Start index column, inclusive.
     * @param endColIndex End index column, exclusive.
     */
    public Matrix subMatrixCols(int startColIndex, int endColIndex) {
        return subMatrix(0, getNumRows(), startColIndex, endColIndex);
    }

    /**
     * Returns a section of the matrix as a new matrix
     * Note: This operation does a deep-copy and is expensive.
     *
     * @param startRowIndex Start index row, inclusive.
     * @param endRowIndex End index row, exclusive.
     * @param startColIndex Start index, inclusive.
     * @param endColIndex End index, exclusive.
     */
    public Matrix subMatrix(int startRowIndex, int endRowIndex, int startColIndex, int endColIndex) {

        if (startRowIndex < 0 || endRowIndex > getNumRows()
                || startColIndex < 0 || endColIndex > getNumCols()) {
            throw new IndexOutOfBoundsException("Sub-matrix index out of range");
        }

        Matrix subMatrix = new Matrix();

        for (int i = startColIndex; i < endColIndex; i++) {
            ColumnAttributes columnAttributes = getColumnAttributes(i);
            subMatrix.addColumn(new ColumnAttributes(columnAttributes));
        }

        for (int i = startRowIndex; i < endRowIndex; i++) {
            List<Double> row = getRow(i);
            subMatrix.addRow(new ArrayList<Double>(row));
        }
        return subMatrix;
    }

    /**
     * For debugging purposes
     */
    public void printMatrix() {

        // Column attributes stuff
        System.out.println(columnAttributes);
        System.out.println();

        // Column names
        for (int i = 0; i < getNumCols(); i++) {
            System.out.printf("%15s", columnAttributes.get(i).getName());
        }
        System.out.println();

        // Data
        for (int i = 0; i < getNumRows(); i++) {
            List<Double> row = getRow(i);
            for (int j = 0; j < getNumCols(); j++) {
                System.out.printf("%15s ", row.get(j));
            }
            System.out.println();
        }
    }
}