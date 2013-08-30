/**
 * 
 */
package dataMining.starter;

import java.util.Vector;

/**
 * @author Sawyer Anderson
 * @date 2013.08.27
 *
 */
public abstract class SupervisedLearner 
{
	public SupervisedLearner(){}
	
	public abstract void train(final Matrix features, final Matrix labels);
	
	public abstract void predict(final Vector<Double> in, Vector<Double> out);
	
}
