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
		// Return the value of the child arith-expr.
		return m_arithExpr.getVal(symTab);
	}


	//================//
	// Static Methods //
	//================//

	public static BoolFactorNode parseBoolFactor(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		//
		// GR 29:
		//
		//		bool-factor : arith-expr relation-option
		//

		// Read the arith-expr and relation-option
		ArithExprNode arithExpr = ArithExprNode.parseArithExpr(tokenReader);
//		RelationOptionNode relationOption =
//		 		RelationOptionNode.parseRelationOption(tokenReader);
		return new BoolFactorNode(arithExpr /*, relationOption */);
	}
}
