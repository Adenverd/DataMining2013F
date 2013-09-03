package ml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MatrixReloaded {

    //Data
    public List<List<Double>> data;

    //Meta-data
    private int numCols;
    private int numRows;

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

    /**
     * Not implemented yet, needs design.
     */
    public void addColumn(){
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Appends a row to the end of the matrix
     * @param row
     */
    public void addRow(List<Double> row){
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Appends an empty row to the end of the matrix
     */
    public void addRow(){
        throw new UnsupportedOperationException("Not Implemented");
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
        throw new UnsupportedOperationException("Not Implemented");
    }


}
