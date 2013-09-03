package ml;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// TODO: Redo this class without Scanner

public class ARFFParser {

    private static final String ATTRIBUTE = "@attribute";
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
            else if (line.toLowerCase().startsWith("@data")) {
                List<Double> row = new ArrayList<Double>();
                matrix.addRow(row);

            }
        }

        return matrix;
    }

    public static MatrixReloaded saveToARFF(MatrixReloaded matrix, String filepath)
            throws Exception {
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Only handles categorical and numerical attributes
     */
    private static void getAttributes(MatrixReloaded matrix, String line) {
        String[] sp = line.split(" ");
        String name = sp[1], type = sp[2];
        if (type.equals(NUMERIC) || type.equals(REAL)) {
            Column column = new Column(name, type);
        } else {
            if (type.startsWith("{") && type.endsWith("}")) {
                type = type.substring(1, type.length());
            }
        }
    }
}
