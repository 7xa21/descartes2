import java.io.IOException;
import java.util.HashMap;

public class FactorNode {

	//==================//
	// Member Variables //
	//==================//

	private AtomNode m_atom;
	private FactorNode m_negFactor;
	private ExprNode m_parenExpr;


	//=========//
	// Methods //
	//=========//

	public FactorNode(AtomNode atom) {
		m_atom = atom;
		m_negFactor = null;
		m_parenExpr = null;
	}

	public FactorNode(FactorNode negFactor) {
		m_atom = null;
		m_negFactor = negFactor;
		m_parenExpr = null;
	}

	public FactorNode(ExprNode parenExpr) {
		m_atom = null;
		m_negFactor = null;
		m_parenExpr = parenExpr;
	}

	public double getVal(HashMap<String, Double> symTab) {
		double value;

		if (m_atom != null) {
			assert (m_negFactor == null);
			assert (m_parenExpr == null);
			value = m_atom.getVal(symTab);
		}
		else if (m_negFactor != null) {
			assert (m_parenExpr == null);
			value = -m_negFactor.getVal(symTab);
		}
		else {
			assert (m_parenExpr != null);
			value = m_parenExpr.getVal(symTab);
		}

		return value;
	}


	//================//
	// Static Methods //
	//================//

	public static FactorNode parseFactor(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{

		TokenDescriptor token;

		// Eat any leading spaces.
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		FactorNode factor;

		//
		// GR 45:
		//
		//		factor : - factor
		//

		// Look for a '-' unary operator.
		if (token.getCode() == TokenCode.T_SUBTRACT) {
			// Eat any spaces before the (negated) factor.
			/*
			do {
				token = tokenReader.getToken();
			} while (token.getCode() == TokenCode.T_SPACE);
			*/

			// Read the (negated) factor.
			FactorNode negFactor = FactorNode.parseFactor(tokenReader);

			factor = new FactorNode(negFactor);
		}

		//
		// GR 47:
		//
		//		factor : ( expr )
		//

		// Look for a '(' character.
		else if (token.getCode() == TokenCode.T_OPEN_PAREN) {
			// Eat any spaces between the open parenthesis and the
			// expression.
			/*
			do {
				token = tokenReader.getToken();
			} while (token.getCode() == TokenCode.T_SPACE);
			*/

			// Read the expression.
			ExprNode expr = ExprNode.parseExpr(tokenReader);

			// Eat any spaces between the expression and the closing
			// parenthesis.
			do {
				token = tokenReader.getToken();
			} while (token.getCode() == TokenCode.T_SPACE);

			// The next token MUST be a closing parenthesis.
			assert(token.getCode() == TokenCode.T_CLOSE_PAREN);

			factor = new FactorNode(expr);
		}

		//
		// GR 46:
		//
		//		factor : atom
		//

		// The factor HAS to be an atom.
		else {
			// The token we read is part of the atom; put it back
			// so the atom parser can read it.
			tokenReader.unread(token);
			AtomNode atom = AtomNode.parseAtom(tokenReader);

			factor = new FactorNode(atom);
		}


		return factor;
	}
}
