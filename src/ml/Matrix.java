package ml;

import java.util.*;

/**
 * @author Sawyer Anderson
 * @date 2013.08.27
 */
public class Matrix {

    public static final Double UNKNOWN_VALUE = Double.NEGATIVE_INFINITY;

    // Data
    private Vector<Vector<Double>> data; // Matrix elements

    //Meta-data
    private String filename;
    private Vector<String> attr_name;
    private Vector<Map<String, Integer>> str_to_enum; // value to enumeration
    private Vector<Map<Integer, String>> enum_to_str; // enumeration to value

    /**
     * Creates a 0x0 matrix. Use loadARFF, setSize, addColumn, or copyMetaData
     * to give this matrix some dimension
     */
    public Matrix() {
    }

    /**
     * Loads the matrix from an ARFF file
     *
     * @param filename
     */
    public void loadARFF(String filename) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Saves the matrix to an ARFF file
     *
     * @param filename
     */
    public void saveARFF(String filename) {
        //FUCK THIS SHIT
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Makes a rows x cols matrix of *ALL CONTINUOUS VALUES*.
     * This method wipes out any data currently in the matrix. It
     * also wipes out any meta-data.
     *
     * @param rows rows size
     * @param cols columns size
     */
    public void setSize(int rows, int cols) {
        //Make space for the data
        data.setSize(rows);
        for (Vector<Double> row : data) {
            row.setSize(cols);
        }

        //Set the meta-data
        filename = "";
        attr_name.setSize(cols);
        str_to_enum.setSize(cols);
        enum_to_str.setSize(cols);

        for (Map<String, Integer> map : str_to_enum) {
            map.clear();
        }

        for (Map<Integer, String> map : enum_to_str) {
            map.clear();
        }
    }

    /**
     * Clears this matrix and copies the meta-data from that matrix.
     * In other words, it makes a zero-row matrix with the same number
     * of columns as "that" matrix. You will need to call newRow or
     * newRows to give the matrix some rows.
     *
     * @param that
     */
    public void copyMetaData(Matrix that) {
        data.clear();
        attr_name = that.attr_name;
        str_to_enum = that.str_to_enum;
        enum_to_str = that.enum_to_str;
    }

    /**
     * Adds a column to this matrix with the specified number of values.
     * Also sets the number of rows to 0, so you will need to call
     * newRow or newRows when you are done adding columns.
     *
     * @param vals
     */
    public void newColumn(int vals) {
        data.clear();
        int c = getCols();
        String name = "col_" + c;
        attr_name.add(name);
        Map<String, Integer> temp_str_to_enum = new HashMap<String, Integer>();
        Map<Integer, String> temp_enum_to_str = new HashMap<Integer, String>();
        for (int i = 0; i < vals; i++) {
            String sVal = "val_" + i;
            temp_str_to_enum.put(sVal, i);
            temp_enum_to_str.put(i, sVal);
        }
        str_to_enum.add(temp_str_to_enum);
        enum_to_str.add(temp_enum_to_str);
    }

    /**
     * Calls newColumn(0)
     */
    public void newColumn() {
        newColumn(0);
    }

    public Vector<Double> newRow() {
        int c = getCols();
        if (c == 0) {
            throw new MLException("You must add some columns before you add any rows.");
        }
        int rc = getRows();
        data.setSize(rc + 1);
        Vector<Double> newRow = data.elementAt(rc);
        newRow.setSize(c);
        return newRow;
    }

    /**
     * Adds n new rows to this matrix
     *
     * @param n
     */
    public void newRows(int n) {
        for (int i = 0; i < n; i++) {
            newRow();
        }
    }

    /**
     * Returns the number of rows in this matrix
     */
    public Integer getRows() {
        return data.size();
    }

    /**
     * Returns the number of columns in this matrix
     */
    public Integer getCols() {
        return attr_name.size();
    }

    /**
     * Returns the name of the specified attribute
     */
    public String attrName(int col) {
        return attr_name.get(col);
    }

    /**
     * Returns the name of the specified value
     */
    public String attrValue(int attr, int val) {
        Map<Integer, String> map = enum_to_str.get(attr);
        if (map == null || map.get(val) == null) {
            throw new MLException("Column or row not found.");
        }
        return map.get(val);
    }

    /**
     * Returns the specified row
     * @param index
     */
    public List<Double> row(int index) {
        return data.get(index);
    }

    /**
     * Swaps the positions of the two specified rows
     *
     * @param a
     * @param b
     */
    public void swapRows(int a, int b) {
        Vector<Double> temp = data.get(a);
        data.set(a, data.get(b));
        data.set(b, temp);
    }

    /**
     * Returns the number of values associated with the specified attribute (or column)
     * 0=continuous, 2=binary, 3=trinary, etc
     *
     * @param column
     */
    public int valueCount(int column) {
        return enum_to_str.get(column).size();
    }

    public boolean isContinuous(int column) {
        return valueCount(column) == 0;
    }

    public boolean isNominal(int column) {
        return valueCount(column) > 0;
    }

    /**
     * Returns the mean of the elements in a specified column
     *
     * @param col
     */
    public double columnMean(int col) {
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
     *
     * @param col
     */
    public double columnMin(int col) {
        double min = Double.MAX_VALUE;
        for (List<Double> row : data) {
            double val = row.get(col);
            if (val != UNKNOWN_VALUE) {
                min = min < val ? min : val;
            }
        }
        return min;
    }

    /**
     * Returns the max elements in the specified column.
     * (Elements with the value UNKNOWN_VALUE are ignored.)
     *
     * @param col
     */
    public double columnMax(int col) {
        double min = Double.MIN_VALUE;
        for (List<Double> row : data) {
            double val = row.get(col);
            if (val != UNKNOWN_VALUE) {
                min = min > val ? min : val;
            }
        }
        return min;
    }

    /**
     * Returns the most common value in the specified column.
     * (Elements with the value UNKNOWN_VALUE are ignored.)
     *
     * @param col
     */
    public double mostCommonValue(int col) {
        Map<Double, Integer> frequencies = new HashMap<Double, Integer>();
        for (List<Double> row : data) {
            double val = row.get(col);

            if (val == UNKNOWN_VALUE) {
                continue;
            }

            if (frequencies.containsKey(val)) {
                frequencies.put(val, frequencies.get(val) + 1);
            } else {
                frequencies.put(val, 1);
            }
        }

        Set<Double> keySet = frequencies.keySet();
        double mostCommonValue = UNKNOWN_VALUE;
        int highestFrequency = 0;

        for (Double key : keySet) {
            int val = frequencies.get(key);
            mostCommonValue = val > highestFrequency ? val : mostCommonValue;
        }
        return mostCommonValue;
    }

    /**
     * Adds a new row to this matrix that is a copy of row.
     *
     * @param row
     */
    public void copyRow(Vector<Double> row) {
        if (row.size() != getCols()) {
            throw new MLException("Number of columns in row is not the same as the number of columns in this Matrix");
        }
        data.add(row);
    }

    /**
     * TODO: NEEDS TESTING
     * Copies the specified rectangular portion of that matrix, and adds it to the bottom of this matrix.
     * (If colCount does not match the number of columns in this matrix, then this matrix will be cleared first.)
     *
     * @param that
     * @param rowBegin
     * @param colBegin
     * @param rowCount
     * @param colCount
     */
    public void copyPart(final Matrix that, int rowBegin, int colBegin, int rowCount, int colCount) {
        if (rowBegin + rowCount > that.getRows() || colBegin + colCount > that.getCols()) {
            throw new UnsupportedOperationException("Out of range.");
        }
        if (getCols() != colCount) {
            setSize(0, colCount);
        }

        //Copy specified region of meta-data
        for (int i = 0; i < colCount; i++) {
            attr_name.add(i, that.attr_name.get(colBegin + i));
            str_to_enum.add(i, that.str_to_enum.get(colBegin + i));
            enum_to_str.add(i, that.enum_to_str.get(colBegin + i));
        }

        //Copy the specified region of data
        int rowsBefore = data.size();
        data.setSize(rowsBefore + rowCount);
        for (int i = 0; i < rowCount; i++) {
            Vector<Double> newRow = new Vector<Double>();
            for (int j = 0; j < colCount; j++) {
                newRow.add(colBegin + j, that.data.get(rowBegin + i).get(colBegin + j));
            }
            data.add(newRow);
        }
    }

    /**
     * Sets every element in the matrix to the specified value.
     *
     * @param val
     */
    public void setAll(double val) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Throws an exception if that has a different number of columns than this,
     * or if one of its columns has a different number of values.
     *
     * @param that
     */
    public void checkCompatibility(Matrix that) {
        int c = getCols();
        if (that.getCols() != c) {
            throw new MLException("Matrices have different number of columns");
        }
        for (int i = 0; i < c; i++) {
            if (valueCount(i) != that.valueCount(i)) {
                throw new MLException("Column " + i + " has mis-matching number of values");
            }
        }
    }
}
