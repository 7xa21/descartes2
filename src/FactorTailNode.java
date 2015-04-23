import java.io.IOException;
import java.util.HashMap;

public class FactorTailNode {

	//=============//
	// Local Types //
	//=============//
	public enum Operator {
		MULTIPLY,
		DIVIDE
	}


	//==================//
	// Member Variables //
	//==================//

	private FactorNode m_factor;
	private FactorTailNode m_factorTail;

	public Operator m_oper;


	//=========//
	// Methods //
	//=========//

	public FactorTailNode(Operator oper,
						  FactorNode factor,
						  FactorTailNode factorTail)
	{
		m_oper = oper;
		m_factor = factor;
		m_factorTail = factorTail;
	}

	public FactorTailNode() {
		m_oper = null;
		m_factor = null;
		m_factorTail = null;
	}

	public boolean isEmpty() {
		return m_factor == null;
	}

	public double getVal(HashMap<String, Double> symTab) {
		double factorVal = m_factor.getVal(symTab);

		if (!m_factorTail.isEmpty()) {
			double tailVal = m_factorTail.getVal(symTab);
			if (m_factorTail.m_oper == Operator.MULTIPLY) {
				factorVal *= tailVal;
			} else {
				factorVal /= tailVal;
			}
		}

		return factorVal;
	}


	//================//
	// Static Methods //
	//================//

	public static FactorTailNode parseFactorTail(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		// Eat any leading spaces and get next token.
		TokenDescriptor token;
		do {
			token = tokenReader.getToken();
		} while (token.getCode() == TokenCode.T_SPACE);

		// Look for a '*' or a '/'.
		Operator oper = null;
		if (token.getCode() == TokenCode.T_MULTIPLY) {
			oper = Operator.MULTIPLY;
		} else if (token.getCode() == TokenCode.T_DIVIDE) {
			oper = Operator.DIVIDE;
		}

		// Either construct a compound factor tail, with a
		// multiply or divide operator, or an empty factor tail.
		FactorTailNode factorTail;
		if (oper != null) {
			FactorNode factor = FactorNode.parseFactor(tokenReader);
			FactorTailNode nextFactorTail =
					FactorTailNode.parseFactorTail(tokenReader);
			factorTail = new FactorTailNode(oper, factor, nextFactorTail);
		} else {
			// This factor tail is empty. Since the token we read
			// isn't part of the factor tail, unread it.
			tokenReader.unread(token);
			factorTail = new FactorTailNode();
		}

		return factorTail;
	}
}
