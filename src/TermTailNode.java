import java.io.IOException;
import java.util.HashMap;

public class TermTailNode {

	//=============//
	// Local Types //
	//=============//
	public enum Operator {
		ADD,
		SUBTRACT
	}


	//==================//
	// Member Variables //
	//==================//

	private TermNode m_term;
	private TermTailNode m_termTail;

	public Operator m_oper;


	//=========//
	// Methods //
	//=========//

	public TermTailNode(Operator oper, TermNode term, TermTailNode termTail) {
		m_oper = oper;
		m_term = term;
		m_termTail = termTail;
	}

	public TermTailNode() {
		m_oper = null;
		m_term = null;
		m_termTail = null;
	}

	public boolean isEmpty() {
		return m_term == null;
	}

	public double getVal(HashMap<String, Double> symTab) {
		double termVal = m_term.getVal(symTab);

		if (!m_termTail.isEmpty()) {
			double tailVal = m_termTail.getVal(symTab);
			if (m_termTail.m_oper == Operator.ADD) {
				termVal += tailVal;
			} else {
				termVal -= tailVal;
			}
		}

		return termVal;
	}


	//================//
	// Static Methods //
	//================//

	public static TermTailNode parseTermTail(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		// Eat any leading spaces and get next token.
		TokenDescriptor token;
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		// Look for a '+' or a '-'.
		Operator oper = null;
		if (token.getCode() == TokenCode.T_ADD) {
			oper = Operator.ADD;
		} else if (token.getCode() == TokenCode.T_SUBTRACT) {
			oper = Operator.SUBTRACT;
		}

		// Either construct a compound term tail, with an
		// add or subtract operator, or an empty term tail.
		TermTailNode termTail;
		if (oper != null) {
			TermNode term = TermNode.parseTerm(tokenReader);
			TermTailNode nextTermTail =
					TermTailNode.parseTermTail(tokenReader);
			termTail = new TermTailNode(oper, term, nextTermTail);
		} else {
			// This term tail is empty. Since the token we read
			// isn't part of the term tail, unread it.
			tokenReader.unread(token);
			termTail = new TermTailNode();
		}

		return termTail;
	}
}
