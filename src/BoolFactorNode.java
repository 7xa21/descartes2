import java.io.IOException;
import java.util.HashMap;

public class BoolFactorNode {

	//==================//
	// Member Variables //
	//==================//

	private ArithExprNode m_arithExpr;
	private RelationOptionNode m_relationOption;


	//=========//
	// Methods //
	//=========//

	public BoolFactorNode(ArithExprNode arithExpr,
						  RelationOptionNode relationOption) {
		m_arithExpr = arithExpr;
		m_relationOption = relationOption;
	}

	public double getVal(HashMap<String, Double> symTab) {
		// Return the value of the child arith-expr.
		double arithExprVal = m_arithExpr.getVal(symTab);

		// The value of the arithmetic expression may be modified
		// by the subsequent relation option.
		arithExprVal = m_relationOption.getVal(arithExprVal, symTab);


		return arithExprVal;
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
		RelationOptionNode relationOption =
		 		RelationOptionNode.parseRelationOption(tokenReader);
		return new BoolFactorNode(arithExpr, relationOption);
	}
}
