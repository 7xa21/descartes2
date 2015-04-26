import java.io.IOException;
import java.util.HashMap;

public class ExprNode {

	//==================//
	// Member Variables //
	//==================//

	private BoolTermNode m_boolTerm;
	private BoolTermTailNode m_boolTermTail;


	//=========//
	// Methods //
	//=========//

	public ExprNode(BoolTermNode boolTerm, BoolTermTailNode boolTermTail) {
		m_boolTerm = boolTerm;
		m_boolTermTail = boolTermTail;
	}

	public double getVal(HashMap<String, Double> symTab) {
		// Get the value of the child bool-term.
		double termVal = m_boolTerm.getVal(symTab);

		// If the child bool-term-tail isn't empty, then this is a
		// conditional/boolean expression.
		if (!m_boolTermTail.isEmpty()) {
			// Get the value of the child bool-term-tail.
			double tailVal = m_boolTermTail.getVal(symTab);

			// If either the bool-term or bool-term-tail value are
			// non-zero, return 1; otherwise return 0.
			if (termVal != 0.0 || tailVal != 0.0) {
				termVal = 1.0;
			} else {
				termVal = 0.0;
			}
		}

		return termVal;
	}


	//================//
	// Static Methods //
	//================//

	public static ExprNode parseExpr(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		//
		// GR 23:
		//
		//		expr : bool-term bool-term-tail
		//

		// Read the bool-term and the bool-term-tail.
		BoolTermNode boolTerm = BoolTermNode.parseBoolTerm(tokenReader);
		BoolTermTailNode boolTermTail =
				BoolTermTailNode.parseBoolTermTail(tokenReader);


		return new ExprNode(boolTerm, boolTermTail);
	}
}
