package dataMining.starter;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * @author Sawyer Anderson
 * @date 2013.08.27
 *
 */
public class Matrix 
{
    public static final Double UNKNOWN_VALUE = Double.NEGATIVE_INFINITY;
	//Data
	private Vector<Vector<Double>> m_data; //matrix elements
	
	//Meta-data
	private String m_filename; //the name of the file
	private Vector<String> m_attr_name; //the name of each attribute
	private Vector<Map<String, Integer>> m_str_to_enum; //value to enumeration
	private Vector<Map<Integer, String>> m_enum_to_str; //enumeration to value
	
	/**
	 * Creates a 0x0 matrix. Use loadARFF, setSize, addColumn, or copyMetaData
	 * to give this matrix some dimension
	 */
	public Matrix()
	{

	}
	
	/**
	 * Loads the matrix from an ARFF file
	 * @param filename
	 */
	public void LoadARFF(String filename)
	{
		throw new UnsupportedOperationException("Not Implemented");
	}
	
	/**
	 * Saves the matrix to an ARFF file
	 * @param filename
	 */
	public void saveARFF(String filename)
	{
		throw new UnsupportedOperationException("Not Implemented");
	}
	
	/**
	 * Makes a rows x cols matrix of *ALL CONTINUOUS VALUES*.
	 * This method wipes out any data currently in the matrix. It
	 * also wipes out any meta-data.
	 * @param rows
	 * @param cols
	 */
	public void setSize(int rows, int cols)
	{
        //Make space for the data
		m_data.setSize(rows);
        for (Vector<Double> row : m_data)
        {
            row.setSize(cols);
        }

        //Set the meta-data
        m_filename = "";
        m_attr_name.setSize(cols);
        m_str_to_enum.setSize(cols);
        m_enum_to_str.setSize(cols);

        for (Map<String, Integer> map : m_str_to_enum)
        {
            map.clear();
        }

        for (Map<Integer, String> map : m_enum_to_str)
        {
            map.clear();
        }
	}
	
	/**
	 * Clears this matrix and copies the meta-data from that matrix.
	 * In other words, it makes a zero-row matrix with the same number
	 * of columns as "that" matrix. You will need to call newRow or
	 * newRows to give the matrix some rows.
	 * @param that
	 */
	public void copyMetaData(final Matrix that)
	{
		m_data.clear();
        m_attr_name = that.m_attr_name;
        m_str_to_enum = that.m_str_to_enum;
        m_enum_to_str = that.m_enum_to_str;
	}
	
	/**
	 * Adds a column to this matrix with the specified number of values.
	 * Also sets the number of rows to 0, so you will need to call
	 * newRow or newRows when you are done adding columns.
	 * @param vals
	 */
	public void newColumn(int vals)
	{
		m_data.clear();
        int c = getCols();
        String name = "col_";
        name += String.valueOf(c);
        m_attr_name.add(name);
        Map<String, Integer> temp_str_to_enum = new HashMap<String, Integer>();
        Map<Integer, String> temp_enum_to_str = new HashMap<Integer, String>();
        for(int i = 0; i < vals; i++)
        {
            String sVal = "val_";
            sVal += String.valueOf(i);
            temp_str_to_enum.put(sVal, i);
            temp_enum_to_str.put(i, sVal);
        }
        m_str_to_enum.add(temp_str_to_enum);
        m_enum_to_str.add(temp_enum_to_str);
	}
	
	/**
	 * Calls newColumn(0)
	 */
	public void newColumn()
	{
		newColumn(0);
	}
	
	public Vector<Double> newRow()
	{
		int c = getCols();
        if (c == 0)
        {
            throw new UnsupportedOperationException("You must add some columns before you add any rows.");
        }
        int rc = getRows();
        m_data.setSize(rc + 1);
        Vector<Double> newRow = m_data.elementAt(rc);
        newRow.setSize(c);
        return newRow;
	}
	
	/**
	 * Adds n new rows to this matrix
	 * @param n
	 * @throws Exception
	 */
	public void newRows(Integer n)
	{
		for (int i = 0; i < n; i++){
            newRow();
        }
	}
	
	/**
	 * Returns the number of rows in this matrix
	 * @return
	 */
	public Integer getRows(){
		return m_data.size();
	}
	
	/**
	 * Returns the number of columns in this matrix
	 * @return
	 */
	public Integer getCols()
	{
		return m_attr_name.size();
	}
	
	/**
	 * Returns the name of the specified attribute
	 * @param col
	 * @return
	 */
	public String attrName(Integer col)
	{
		return m_attr_name.get(col);
	}
	
	/**NOT SURE IF THIS ONE IS RIGHT
	 * Returns the name of the specified value
	 * @param attr
	 * @param val
	 * @return
	 */
	public String attrValue(Integer attr, Integer val)
	{
		return m_enum_to_str.get(attr).get(val);
    }
	
	/**
	 * Returns the specified row
	 * @param index
	 * @return
	 */
	public Vector<Double> row(Integer index)
	{
		return m_data.get(index);
	}

    /**
     * Swaps the positions of the two specified rows
     * @param a
     * @param b
     */
    public void swapRows(int a, int b)
    {
        Vector<Double> temp = m_data.get(a);
        m_data.set(a, m_data.get(b));
        m_data.set(b, temp);
    }

    /**
     * Returns the number of values associated with the specified attribute (or column)
     * 0=continuous, 2=binary, 3=trinary, etc
     * @param attr
     * @return
     */
    public int valueCount(int attr)
    {
        return m_enum_to_str.get(attr).size();
    }

    /**
     * Returns the mean of the elements in a specified column
     * @param col
     * @return
     */
	public double columnMean(int col)
    {
        double sum = 0.0;
        int count = 0;
        for (Vector<Double> row : m_data)
        {
            double val = row.elementAt(col);
            if(val != UNKNOWN_VALUE)
            {
                sum += val;
                count++;
            }
        }
        return sum/count;
    }

    /**
     * Returns the min elements in the specified column (Elements with the value UNKNOWN_VALUE are ignored.)
     * @param col
     * @return
     */
    public double columnMin(int col)
    {
        double m = Double.MAX_VALUE;
        for (Vector<Double> row : m_data)
        {
            double val = row.elementAt(col);
            if(val != UNKNOWN_VALUE)
            {
                m = (m < val ? m : val);
            }
        }
        return m;
    }

    /**
     * Returns the max elements in the specified column. (Elements with the value UNKNOWN_VALUE are ignored.)
     * @param col
     * @return
     */
    public double columnMax(int col)
    {
        double m = Double.MIN_VALUE;
        for (Vector<Double> row : m_data)
        {
            double val = row.elementAt(col);
            if(val != UNKNOWN_VALUE)
            {
                m = (m > val ? m : val);
            }
        }
        return m;
    }

    /**
     * Returns the most common value in the specified column. (Elements with the value UNKNOWN_VALUE are ignored.)
     * @param col
     * @return
     */
    public double mostCommonValue(int col)
    {
        HashMap<Double, Integer> frequencies = new HashMap<Double, Integer>();
        for (Vector<Double> row : m_data)
        {
            Double val = row.get(col);
            if (frequencies.containsKey(val))
            {
                frequencies.put(val, frequencies.get(val)+1);
            }
            else
            {
                frequencies.put(val, 1);
            }
        }
        Iterator it = frequencies.entrySet().iterator();
        Double mostCommonValue = UNKNOWN_VALUE;
        Integer highestFrequency = 0;
        while(it.hasNext()){
            Map.Entry<Double,Integer> entry = (Map.Entry<Double,Integer>)it.next();
            if(entry.getValue() > highestFrequency)
            {
                mostCommonValue = entry.getKey();
            }
        }

        return mostCommonValue;
    }

    /**
     * Adds a new row to this matrix that is a copy of row.
     * @param row
     */
    public void copyRow(Vector<Double> row)
    {
        if(row.size() != getCols())
        {
            throw new UnsupportedOperationException("Number of columns in row is not the same as the number of columns in this Matrix");
        }
        m_data.add(row);
    }

    /** NEEDS TESTING
     * Copies the specified rectangular portion of that matrix, and adds it to the bottom of this matrix.
     * (If colCount does not match the number of columns in this matrix, then this matrix will be cleared first.)
     * @param that
     * @param rowBegin
     * @param colBegin
     * @param rowCount
     * @param colCount
     */
    public void copyPart(final Matrix that, int rowBegin, int colBegin, int rowCount, int colCount)
    {
        if (rowBegin + rowCount > that.getRows() || colBegin + colCount > that.getCols())
        {
            throw new UnsupportedOperationException("Out of range.");
        }
        if(getCols() != colCount)
        {
            setSize(0, colCount);
        }

        //Copy specified region of meta-data
        for (int i =0; i < colCount; i++)
        {
            m_attr_name.add(i, that.m_attr_name.get(colBegin + i));
            m_str_to_enum.add(i, that.m_str_to_enum.get(colBegin + i));
            m_enum_to_str.add(i, that.m_enum_to_str.get(colBegin + i));
        }

        //Copy the specified region of data
        int rowsBefore = m_data.size();
        m_data.setSize(rowsBefore + rowCount);
        for (int i = 0; i < rowCount; i++)
        {
            Vector<Double> newRow = new Vector<Double>();
            for (int j = 0; j < colCount; j++)
            {
                newRow.add(colBegin + j, that.m_data.get(rowBegin + i).get(colBegin + j));
            }
            m_data.add(newRow);
        }
    }

    /**
     * Sets every element in the matrix to the specified value.
     * @param val
     */
    public void setAll(double val)
    {
        throw new UnsupportedOperationException("Not Implemented");
    }

    /**
     * Throws an exception if that has a different number of columns than this,
     * or if one of its columns has a different number of values.
     * @param that
     */
    public void checkCompatibility(final Matrix that)
    {
        int c = getCols();
        if(that.getCols() != c)
        {
            throw new IllegalArgumentException("Matrices have different number of columns");
        }
        for (int i = 0; i< c; i++)
        {
            if (valueCount(i) != that.valueCount(i))
            {
                throw new IllegalArgumentException("Column " + String.valueOf(i) + " has mis-matching number of values");
            }
        }
    }
}
