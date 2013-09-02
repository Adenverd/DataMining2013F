package ml;

import dataMining.ml.Matrix;

import java.util.List;
import java.util.Vector;

public class NomCat extends UnsupervisedLearner {

    private List<Integer> values = new Vector<Integer>();
    private Matrix template = new Matrix();

    /**
     * Decides how many dimensions are needed for each column.
     */
    @Override
    public void train(Matrix data) {
        int totalValues = 0;
        for (int i = 0; i < data.getCols(); i++) {
            int n = data.valueCount(i);
            n = n == 0 ? 1 : n;
            values.add(n);
            totalValues += n;
        }
        template.setSize(0, totalValues);
    }

    /**
     * Re-represents each nominal attribute with a categorical
     * distribution of continuous values.
     */
    @Override
    public void transform(List<Double> in, List<Double> out) {
        if (in.size() != values.size()) {
            throw new MLException(String.format(
                    "Unexpected in-vector size. Expected: %d, Got: %d", values.size(), in.size()));
        }
        for (int i = 0; i < in.size(); i++) {
            if (values.get(i) == 1) {
                out.add(in.get(i));
            } else {
                int outStart = out.size() - 1;
                for (int j = 0; j < values.get(i); j++) {
                    out.add(0.0);
                }
                if (in.get(i) != Matrix.UNKNOWN_VALUE) {
                    if (in.get(i) >= values.get(i)) {
                        throw new MLException(
                                String.format("Value out of range. Expected: [0-%d]. Got: %d",
                                        values.get(i) - 1, in.get(i)));
                    }
                    out.set(outStart + in.get(i).intValue(), 1.0);
                }
            }
        }
    }

    /**
     * Re-encodes categorical distribution as nominal values by finding the mode.
     */
    @Override
    public void untransform(List<Double> in, List<Double> out) {
        if (in.size() != template.getCols()) {
            throw new MLException(String.format(
                    "Unexpected in-vector size. Expected %d, Got: %d", template.getCols(), in.size()));
        }
        int inStart = 0;
        for (int i = 0; i < values.size(); i++) {
            if  (values.get(i) == 1) {
                out.add(in.get(inStart++));
            } else {
                int startPos = inStart;
                int maxIndex = 0;
                for (int j = 1; j < values.get(i); j++) {
                    if (in.get(inStart) > in.get(startPos + maxIndex)) {
                        maxIndex = inStart - startPos;
                    }
                    inStart++;
                }
                out.add((double) maxIndex);
            }
        }
    }

    /**
     * Returns a zero-row matrix with the number of continuous
     * columns that transform will output.
     */
    @Override
    public Matrix outputTemplate() {
        return template;
    }

}
