package ml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// TODO: Redo this class without Scanner

public class ARFFParser {

    private static final String ATTRIBUTE = "@attribute";
    private static final String DATA = "@data";
    private static final String NUMERIC = "NUMERIC";
    private static final String REAL = "REAL";

    public static MatrixReloaded loadARFF(String filepath)
            throws Exception {

        MatrixReloaded matrix = new MatrixReloaded();

        Scanner in = new Scanner(new File(filepath));

        while (in.hasNext()) {
            String line = in.nextLine().replaceAll("\\s+", " ");

            if (line.toLowerCase().startsWith(ATTRIBUTE)) {
                getAttributes(matrix, line);
            }
            else if (line.toLowerCase().startsWith(DATA)) {
                getData(matrix, line);
            }
        }
        in.close();
        return matrix;
    }

    public static MatrixReloaded saveToARFF(MatrixReloaded matrix, String filepath)
            throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Only handles categorical and continuous attributes
     */
    private static void getAttributes(MatrixReloaded matrix, String line) {
        String[] sp = line.split(" ");
        String name = sp[1], type = sp[2];

        if (type.equals(NUMERIC) || type.equals(REAL)) {
            ColumnAttributes column = new ColumnAttributes(name, ColumnType.Continuous);
            matrix.addColumn(column);
        } else if (type.startsWith("{") && type.endsWith("}")) {
            ColumnAttributes column = new ColumnAttributes(name, ColumnType.Categorical);
            type = type.substring(1, type.length());
            String[] values = type.split(",");
            for (int i = 0; i < values.length; i++) {
                column.addValue(values[i]);
            }
            matrix.addColumn(column);
        }
    }

    private static void getData(MatrixReloaded matrix, String line) {
        String[] cols = line.split(",");
        if (matrix.getNumCols() != cols.length) {
            throw new MLException(String.format(
                    "Number of columns mismatch. Expected: %d, Got: %d", matrix.getNumCols(), cols.length));
        }

        List<Double> row = new ArrayList<Double>();
        for (int i = 0; i < cols.length; i++) {
            if (cols[i] == "?") {
                row.add(Matrix.UNKNOWN_VALUE);
            } else if(matrix.isContinuous(i)) {
                row.add(Double.valueOf(cols[i]));
            } else {
                ColumnAttributes column = matrix.getColumnAttributes(i);
                row.add((double) column.getIndex(cols[i]));
            }
        }
        matrix.addRow(row);
    }
}
