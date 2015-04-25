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
		return m_term.getVal(symTab) + m_termTail.getVal(symTab);
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
