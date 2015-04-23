import java.io.IOException;
import java.util.HashMap;

public class ArithExprNode {

	//==================//
	// Member Variables //
	//==================//
//TEST COMMENT
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
		TermNode term = TermNode.parseTerm(tokenReader);
		TermTailNode termTail = TermTailNode.parseTermTail(tokenReader);
		return new ArithExprNode(term, termTail);
	}

}
