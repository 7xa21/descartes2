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

		// The value of the expression may be modified by the bool
		// term tail.
		termVal = m_boolTermTail.getVal(termVal, symTab);


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
