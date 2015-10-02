package operators;

import java.util.List;

import net.sf.jsqlparser.expression.Expression;
import util.Tuple;
import visitors.JoinExpVisitor;

public class JoinOperator extends BinaryOperator {

	Expression joinCond = null;
	Tuple curLeft = null, curRight = null;
	JoinExpVisitor visitor = null;
	
	private Tuple joinTp(Tuple tp1, Tuple tp2) {
		int[] cols = new int[tp1.length() + tp2.length()];
		int i = 0;
		for (int val : tp1.cols)
			cols[i++] = val;
		for (int val : tp2.cols)
			cols[i++] = val;
		
		return new Tuple(cols);
	}
	
	@Override
	public Tuple getNextTuple() {
		while (curLeft != null && curRight != null) {
			if (joinCond == null) {
				Tuple ret = joinTp(curLeft, curRight);
				next();
				return ret;
			}
			
			visitor.setTuple(curLeft, curRight);
			joinCond.accept(visitor);
			if (visitor.getFinalCondition()) {
				Tuple ret = joinTp(curLeft, curRight);
				next();
				return ret;
			}
			
			next();
		}
		
		return null;
	}
	
	@Override
	public void reset() {
		left.reset();
		right.reset();
	}

	public void next() {
		if (curLeft == null) return;
		if (curRight != null) {
			curRight = right.getNextTuple();
		}
		if (curRight == null) {
			curLeft = left.getNextTuple();
			right.reset();
			curRight = right.getNextTuple();
		}
	}
	
	public JoinOperator(Operator left, Operator right, Expression joinCond) {
		super(left, right);
		this.joinCond = joinCond;
		curLeft = left.getNextTuple();
		curRight = right.getNextTuple();
		visitor = new JoinExpVisitor(null, left.schema(), null, right.schema());
	}
	
}
