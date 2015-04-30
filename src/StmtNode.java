import java.io.IOException;
import java.util.HashMap;

public class StmtNode {

	//=========//
	// Methods //
	//=========//

	public StmtNode() {
	}

	public void execute(HashMap<String, Double> symTab) {
		// This is only used for blank statements (GR 10).
		//
		// It doesn't do anything.
		//
		// Other types of statements use this class as a base;
		// see the execute() method for them instead.
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
		//
		//  GR 4:
		//
		//		stmt : if-stmt
		//
		if(IfStmtNode.detectIfStmt(tokenReader)){
			node = IfStmtNode.parseIf(tokenReader);
		}

		//
		// GR 7:
		//
		//		stmt : assign-stmt
		//
		else if (AssignStmtNode.detectAssignStmt(tokenReader)) {
			node = AssignStmtNode.parseAssignStmt(tokenReader);
		}

		//
		// GR 10:
		//
		//		stmt :
		//

		// If none of the above statement types were found, the
		// statement is empty.
		else {
			node = new StmtNode();
		}

		return node;
	}
}
