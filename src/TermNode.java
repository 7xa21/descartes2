import java.io.IOException;
import java.util.HashMap;

public class TermNode {

	//==================//
	// Member Variables //
	//==================//

	private FactorNode m_factor;
	private FactorTailNode m_factorTail;


	//=========//
	// Methods //
	//=========//

	public TermNode(FactorNode factor, FactorTailNode factorTail) {
		m_factor = factor;
		m_factorTail = factorTail;
	}

	public double getVal(HashMap<String, Double> symTab) {
		// Get the value of the child factor.
		double factorVal = m_factor.getVal(symTab);

		// If the child factor-tail isn't empty, then this is an
		// arithmetic expression
		if (!m_factorTail.isEmpty()) {
			// Get the value of the factor-tail.
			double factorTailVal = m_factorTail.getVal(symTab);

			// Look at the operator of the factor-tail and perform
			// the operation.
			if (m_factorTail.getOper() == FactorTailNode.Operator.MULTIPLY) {
				factorVal *= factorTailVal;
			} else {
				// If the non-empty factor-tail isn't a
				// multiplication operation, then it HAS to be a
				// division operation.
				assert(m_factorTail.getOper()
						== FactorTailNode.Operator.DIVIDE);
				factorVal /= factorTailVal;
			}
		}

		return factorVal;
	}


	//================//
	// Static Methods //
	//================//

	public static TermNode parseTerm(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		//
		// GR 41:
		//
		//		term : factor factor-tail
		//

		// Read the factor and the factor-tail.
		FactorNode factor = FactorNode.parseFactor(tokenReader);
		FactorTailNode factorTail =
				FactorTailNode.parseFactorTail(tokenReader);


		return new TermNode(factor, factorTail);
	}
}
