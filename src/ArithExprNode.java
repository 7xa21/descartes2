import java.io.IOException;
import java.util.HashMap;

public class ArithExprNode {

	//==================//
	// Member Variables //
	//==================//

	private TermNode m_term;
	private TermTailNode m_termTail;


	//=========//
	// Methods //
	//=========//

	public ArithExprNode(TermNode term, TermTailNode termTail) {
		m_term = term;
		m_termTail = termTail;
	}

	public double getVal(HashMap<String, Double> symTab) {
		// Get the value of the child term.
		double termVal = m_term.getVal(symTab);

		// Check if the child term-tail is not empty.
		if (!m_termTail.isEmpty()) {
			double tailVal = m_termTail.getVal(symTab);
			if (m_termTail.getOper() == TermTailNode.Operator.ADD) {
				termVal += tailVal;
			} else {
				// If the non-empty term-tail isn't an addition
				// operation then it HAS to be a subtraction
				// operation.
				termVal -= tailVal;
			}
		}


		return termVal;
	}


	//================//
	// Static Methods //
	//================//

	public static ArithExprNode parseArithExpr(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		//
		// GR 37:
		//
		//		arith-expr : term term-tail
		//

		// Read the term and the term-tail.
		TermNode term = TermNode.parseTerm(tokenReader);
		TermTailNode termTail = TermTailNode.parseTermTail(tokenReader);
		return new ArithExprNode(term, termTail);
	}

}
