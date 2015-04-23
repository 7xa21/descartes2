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
		double termVal = m_boolTerm.getVal(symTab);

		if (!m_boolTermTail.isEmpty()) {
			double tailVal = m_boolTermTail.getVal(symTab);
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
		return null;
	}
}
