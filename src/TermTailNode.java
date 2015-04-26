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

	public Operator getOper() {
		return m_oper;
	}

	public double getVal(HashMap<String, Double> symTab)
	{
		// Get the value of the child term.
		double termVal;
		if (m_term != null) {
			termVal = m_term.getVal(symTab);
		} else {
			termVal = 0.0;

			// If m_term is null, m_termTail should be null too
			assert(m_termTail == null);
		}

		if (m_termTail != null && !m_termTail.isEmpty()) {
			// Get the value of the child term-tail.
			double termTailVal = m_termTail.getVal(symTab);

			// Look at the operator of the factor-tail and perform
			// the operation.
			if (m_termTail.getOper() == Operator.ADD) {
				termVal += termTailVal;
			} else {
				// If this non-empty term-tail isn't an addition
				// operation, then it HAS to be a subtraction
				// operation.
				assert(m_termTail.getOper() == Operator.SUBTRACT);
				termVal -= termTailVal;
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
		TokenDescriptor token;

		// Eat any leading spaces and get next token.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		//
		// GR 38/39:
		//
		//		term-tail : + term term-tail
		//		term-tail : - term term-tail
		//

		// Look for a '+' or a '-' operator.
		Operator oper = null;
		if (token.getCode() == TokenCode.T_ADD) {
			oper = Operator.ADD;
		} else if (token.getCode() == TokenCode.T_SUBTRACT) {
			oper = Operator.SUBTRACT;
		}

		// If there was a '+' or '-' operator, build a non-empty
		// term with the subsequent term and term-tail.
		TermTailNode termTail;
		if (oper != null) {
			// Read the term and term-tail.
			TermNode term = TermNode.parseTerm(tokenReader);
			TermTailNode nextTermTail =
					TermTailNode.parseTermTail(tokenReader);

			termTail = new TermTailNode(oper, term, nextTermTail);
		}

		//
		// GR 40:
		//
		//		term-tail :
		//

		// If there was no '+' or '-' operator found, then this
		// term-tail is empty (and we need to unread() the token
		// back to the TokenReader).
		else {
			tokenReader.unread(token);
			termTail = new TermTailNode();
		}


		return termTail;
	}
}
