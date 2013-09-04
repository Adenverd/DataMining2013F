package ml;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import ml.ColumnAttributes.ColumnType;

// TODO: Redo this class without Scanner
public class ARFFParser {

    public static void main(String[] args) throws Exception {
        Matrix matrix = ARFFParser.loadARFF("/Users/dev/workspace/DataMining2013F/iris.arff");

        matrix.printMatrix();
    }

    private static final String ATTRIBUTE = "@attribute";
    private static final String DATA = "@data";
    private static final String RELATION = "@relation";
    private static final String COMMENT = "%";
    private static final String NUMERIC = "NUMERIC";
    private static final String REAL = "REAL";

    /**
     * Parses a ARFF file into a Matrix.
     * Only Numeric (Real) and categorical attributes are allowed.
     *
     * @param filepath
     * @throws FileNotFoundException if file not found
     * @throws MLException with a detailed message if parsing fails
     */
    public static Matrix loadARFF(String filepath)
            throws FileNotFoundException {

        Scanner in = new Scanner(new File(filepath));
        Matrix matrix = new Matrix();
        boolean isProcessingData = false;

        while (in.hasNext()) {
            String line = in.nextLine().replaceAll("\\s+", " ");
            String lineLowercase = line.toLowerCase();

            if (lineLowercase.isEmpty() || lineLowercase.startsWith(COMMENT) || lineLowercase.startsWith(RELATION)) {
                continue;
            } else if (lineLowercase.startsWith(ATTRIBUTE)) {
                getAttributes(matrix, line);
            } else if (lineLowercase.startsWith(DATA)) {
                isProcessingData = true;
            } else if (isProcessingData) {
                getData(matrix, line);
            } else {
                throw new MLException("Unrecognized file format, line: " + line);
            }
        }
        in.close();
        return matrix;
    }

    public static Matrix saveToARFF(Matrix matrix, String filepath)
            throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Add an @attribute as a column to the matrix
     *
     * @throws MLException if attribute type is not NUMERIC, REAL, or categorical
     */
    private static void getAttributes(Matrix matrix, String line) {
        String[] sp = line.split(" ");
        String name = sp[1], type = sp[2];

        if (type.equals(NUMERIC) || type.equals(REAL)) {
            ColumnAttributes column = new ColumnAttributes(name, ColumnType.CONTINUOUS);
            matrix.addColumn(column);
        } else if (type.startsWith("{") && type.endsWith("}")) {
            ColumnAttributes column = new ColumnAttributes(name, ColumnType.CATEGORICAL);
            type = type.substring(1, type.length() - 1);
            String[] values = type.split(",");
            for (int i = 0; i < values.length; i++) {
                column.addValue(values[i]);
            }
            matrix.addColumn(column);
        } else {
            throw new MLException("Unrecognized attribute type: " + type);
        }
    }

    /**
     * Adds a row of data to the matrix
     *
     * @throws MLException if a value of a categorical column is not found
     *                     or if the number of columns doesn't match the number
     *                     of columns in the matrix
     */
    private static void getData(Matrix matrix, String line) {
        String[] cols = line.split(",");
        if (matrix.getNumCols() != cols.length) {
            throw new MLException(String.format(
                    "Number of columns mismatch. Expected: %d, Got: %d", matrix.getNumCols(), cols.length));
        }

        List<Double> row = new ArrayList<Double>();
        for (int i = 0; i < cols.length; i++) {
            if (cols[i] == "?") {
                row.add(Matrix.UNKNOWN_VALUE);
            } else if (matrix.isContinuous(i)) {
                row.add(Double.valueOf(cols[i]));
            } else {
                ColumnAttributes column = matrix.getColumnAttributes(i);
                int valueIndex = column.getIndex(cols[i]);

                if (valueIndex < 0) {
                    throw new MLException("Value index not found: " + cols[i]);
                }

                row.add((double) valueIndex);
            }
        }
        matrix.addRow(row);
    }
}
