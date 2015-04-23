import java.io.IOException;
import java.util.HashMap;

public class StmtNode {

	//=========//
	// Methods //
	//=========//

	public StmtNode() {
	}

	public void execute(HashMap<String, Double> symTab) {
	}


	//================//
	// Static Methods //
	//================//

	public static StmtNode parseStmt(TokenReader tokenReader)
			throws IOException, DCSyntaxErrorException
	{
		//
		// Look for the following:
		//
		//	- if statement
		//	- loop statement
		//	- break statement
		//	X assignment statement
		//	- read statement
		//	- print statement
		//
		// If we don't find one of these, this is an empty
		// statement.
		//

		StmtNode node;

		if (AssignStmtNode.detectAssignStmt(tokenReader)) {
			node = AssignStmtNode.parseAssignStmt(tokenReader);
		}

		return new StmtNode();
	}
}
