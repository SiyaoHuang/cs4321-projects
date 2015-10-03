package operators;

import java.io.PrintStream;
import java.util.List;

import util.Tuple;

/**
 * The abstract class of all operators.
 * @author Guantian Zheng (gz94)
 *
 */
public abstract class Operator {
	
	/**
	 * Return the next valid tuple.
	 * @return the next tuple
	 */
	public abstract Tuple getNextTuple();
	
	/**
	 * Reset the operator to the start.
	 */
	public abstract void reset();
	
	/**
	 * Return the schema of the current table.
	 * @return the schema as a list of strings
	 */
	public abstract List<String> schema();
	
	protected List<String> schema = null;
	
	/**
	 * Dump every tuple to the print stream.
	 * @param ps the print stream
	 */
	public void dump(PrintStream ps) {
		Tuple tp = null;
		while ((tp = getNextTuple()) != null) {
			tp.dump(ps);
		}
	}
	
}