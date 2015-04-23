import java.io.IOException;
import java.util.HashMap;

public class BoolFactorNode {

	//==================//
	// Member Variables //
	//==================//

	private ArithExprNode m_arithExpr;


	//=========//
	// Methods //
	//=========//

	public BoolFactorNode(ArithExprNode arithExpr) {
		m_arithExpr = arithExpr;
	}

	public double getVal(HashMap<String, Double> symTab) {
		return m_arithExpr.getVal(symTab);
	}


	//================//
	// Static Methods //
	//================//

	public static BoolFactorNode parseBoolFactor(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		ArithExprNode arithExpr = ArithExprNode.parseArithExpr(tokenReader);
		return new BoolFactorNode(arithExpr);
	}
}
