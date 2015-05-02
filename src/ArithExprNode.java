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

	public double getVal(ProgState progState)
			throws DCRuntimeErrorException
	{
		// Get the value of the child term.
		double termVal = m_term.getVal(progState);

		// The value may be modified by the term tail.
		termVal = m_termTail.getVal(termVal, progState);


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
