//
// Changes from V1:
//		* Changed token codes from 'int' to 'enum TokenCode'
//		* Removed "string literal" token
//		* Adjusted keyword list
//		* Multiple spaces collapsed into single token
//		* Exceptions are no longer local to this class
//


import java.io.*;
import java.util.HashMap;

class TokenReader {

	//==================//
	// Member Variables //
	//==================//

	// A dictionary that associates keyword string literals
	// (like "IF", "THEN", "ELSE") with their corresponding
	// token codes.
	private HashMap<String, TokenCode> m_keywords;

	// A similar dictionary that stores the full text of
	// operators.
	private HashMap<String, TokenCode> m_operators;

	// A PushbackInputStream is used for reading input. This
	// allows a token to be read and examined, and then replaced
	// in the input stream until the proper token-reading method
	// can be called.
	private PushbackInputStream m_inStream;


	//=========//
	// Methods //
	//=========//

	public void unread(TokenDescriptor descrip)
		throws IOException
	{
		// unread() each char in the token (backwards, so that it
		// reads forward the next time getToken() is called).
		for (int i = descrip.getText().length() - 1; i >= 0 ; i--) {
			char ch = descrip.getText().charAt(i);
			m_inStream.unread(ch);
		}
	}

	//
	// Java's Character class inexplicably has no isPunct()
	// method, so this one will suffice.
	//
	// It only includes characters found in the language's
	// operators.
	//
	public static boolean isPunct(int ch) {
		String pChars = ".*()/+-<>=.:;";
		return pChars.contains(Character.toString((char)ch));
	}

	//
	// Construct a new TokenReader that will read input from
	// "stream".
	//
	public TokenReader(InputStream stream) {
		// Initialize member variables.
		m_inStream = new PushbackInputStream(stream, 30);

		// Keyword dictionary.
		m_keywords = new HashMap<String, TokenCode>();
		m_keywords.put("IF", TokenCode.T_IF);
		m_keywords.put("THEN", TokenCode.T_THEN);
		m_keywords.put("ELSE", TokenCode.T_ELSE);
		m_keywords.put("FI", TokenCode.T_FI);
		m_keywords.put("LOOP", TokenCode.T_LOOP);
		m_keywords.put("BREAK", TokenCode.T_BREAK);
		m_keywords.put("READ", TokenCode.T_READ);
		m_keywords.put("REPEAT", TokenCode.T_REPEAT);
		m_keywords.put("PRINT", TokenCode.T_PRINT);
		m_keywords.put("AND", TokenCode.T_AND);
		m_keywords.put("OR", TokenCode.T_OR);

		// Operator dictionary.
		m_operators = new HashMap<String, TokenCode>();
		m_operators.put(")", TokenCode.T_CLOSE_PAREN);
		m_operators.put("(", TokenCode.T_OPEN_PAREN);
		m_operators.put("/", TokenCode.T_DIVIDE);
		m_operators.put("*", TokenCode.T_MULTIPLY);
		m_operators.put("-", TokenCode.T_SUBTRACT);
		m_operators.put("+", TokenCode.T_ADD);
		m_operators.put("<>", TokenCode.T_NOT_EQUAL);
		m_operators.put(":", TokenCode.T_COLON);
		m_operators.put(">", TokenCode.T_GREATER_THAN);
		m_operators.put(">=", TokenCode.T_GREATER_OR_EQUAL);
		m_operators.put("=", TokenCode.T_EQUAL);
		m_operators.put("<=", TokenCode.T_LESS_OR_EQUAL);
		m_operators.put("<", TokenCode.T_LESS_THAN);
		m_operators.put(":=", TokenCode.T_BECOMES);
		m_operators.put(";", TokenCode.T_SEMICOLON);
		m_operators.put(".", TokenCode.T_PERIOD);
		m_operators.put(",", TokenCode.T_COMMA);
	}

	//
	// The following methods read specific types of tokens
	// and return them verbatim:
	//
	//	readNumber()	- reads numeric literals, including real
	//					  numbers (decimal only)
	//
	//	readAlpha()		- reads "alpha" tokens, like keywords and
	// 					  identifiers
	//
	//	readString()	- reads string literals, e.g. strings
	// 					  enclosed by double-quotations
	//
	// Each of these methods will assert() that they're being
	// called properly, e.g. readNumber() will assert that the
	// first character is a digit, etc.
	//
	// This is because the getToken() method should verify the
	// next token type before calling these.
	//

	//
	// Read a numeric constant into a String and return it.
	//
	public String readNumber() throws IOException, DCSyntaxErrorException {
		char ch;
		StringBuilder token = new StringBuilder();
		boolean hasDecimalPoint = false;

		// Ensure we're really reading a numeric literal.
		ch = (char)m_inStream.read();
		assert(Character.isDigit(ch));
		token.append(ch);


		// The loop condition is "more characters available", but
		// it will explicitly break when finished reading the
		// number.
		while (m_inStream.available() > 0) {
			// Get next character
			ch = (char)m_inStream.read();

			// Whether the character is a digit, a decimal point
			// or another type will determine how to proceed.
			if (Character.isDigit(ch)) {
				// Just append digits and keep going.
				token.append(ch);
			} else if (ch == '.') {
				//
				// This numeric literal has a decimal point.
				//

				// Can't have two decimal points in a numeric
				// literal.
				if (hasDecimalPoint) {
					throw new DCSyntaxErrorException(
							"Numeric literal already has decimal point."
					);
				}

				// Append the decimal point to the token.
				token.append(ch);
				hasDecimalPoint = true;

				// Can't end the stream right after a decimal
				// point...
				if (m_inStream.available() == 0) {
					throw new DCSyntaxErrorException(
							"End of file reached; numeric literal expected " +
									"instead."
					);
				}

				//
				// Must have more digits after decimal point;
				// Assignment criteria describes C numeric literal
				// token as
				//
				//    "Numerical constants, which consist of a
				//     non-empty sequence of digits optionally
				//     followed by a decimal point and another
				//     non-empty sequence of digits are classified
				//     as numbers."
				//

				// Get next character and verify that it's a
				// digit.
				ch = (char)m_inStream.read();
				if (!Character.isDigit(ch)) {
					throw new DCSyntaxErrorException(
							"Fractional part of numeric literal expected " +
									"following decimal point, but none found."
					);
				}

				// Append first digit after decimal point
				token.append(ch);
			} else {
				// Not a digit; put it back and exit the loop.
				m_inStream.unread(ch);
				break;
			}
		}

		// Return the assembled numeric literal.
		return token.toString();
	}

	//
	// Read a token that begins with a letter of the alphabet.
	//
	// I considered allowing underscores and numbers after the
	// leading alpha character, but ultimately decided against it.
	// The exact definition of an identifier is not provided, and
	// although whitespace is permitted to avoid ambiguity, there
	// are no examples of such identifiers in the example file.
	//
	public String readAlpha() throws IOException, DCSyntaxErrorException {
		char ch;
		StringBuilder token = new StringBuilder();

		// Ensure that we're really reading an alpha token.
		ch = (char)m_inStream.read();
		assert(Character.isAlphabetic(ch));
		token.append(ch);

		// The loop condition is "more characters available", but
		// it will explicitly break when finished reading the
		// alpha token.
		while (m_inStream.available() > 0) {
			ch = (char)m_inStream.read();
			if (!Character.isAlphabetic(ch) && !Character.isDigit(ch)) {
				// Not an alpha char.
				// Put it back and exit the loop.
				m_inStream.unread(ch);
				break;
			}

			//
			// If we were to allow digits and underscores in
			// identifiers, the if condition would look like
			// this instead:
			//
			//		if (!Character.isAlphabetic(ch) &&
			//			!Character.isDigit(ch) &&
			//			ch != '_') {
			//
			// The first character is validated as an alpha char
			// at the beginning of the method.
			//

			token.append(ch);
		}

		// Return the assembled alpha token.
		return token.toString();
	}

	//
	// Read a string literal and return it, including the
	// surrounding quotation marks.
	//
	/*
	public String readString() throws IOException, DCSyntaxErrorException {
		char ch;
		StringBuilder token = new StringBuilder();

		// Ensure we're really reading a string literal.
		ch = (char)m_inStream.read();
		assert(ch == '\"');
		token.append(ch);

		// Keep reading chars until the closing quotation mark is
		// found.
		do {
			// File should not end in the middle of a string
			// literal.
			if (m_inStream.available() == 0) {
				throw new DCSyntaxErrorException(
						"End of file detected while reading string literal."
				);
			}

			// Get the next character from the input.
			ch = (char)m_inStream.read();

			//
			// Do not allow a string across more than one line, as
			// per the assignment criteria:
			//
			// "...no token extends across more than one line."
			//
			if (ch == '\n') {
				throw new DCSyntaxErrorException(
						"Line break found in string literal."
				);
			}

			// Add the character.
			token.append(ch);
		} while (ch != '\"');

		// Return the assembled string literal token.
		return token.toString();
	}
	*/

	public void readSpace() throws IOException {
		char ch;
		boolean eof = false;

		ch = (char)m_inStream.read();
		assert(ch == ' ' || ch == '\t');

		do {
			if (m_inStream.available() == 0) {
				eof = true;
				break;
			}

			ch = (char)m_inStream.read();
		} while (ch == ' ' || ch == '\t');

		// Only unread() the character if we're NOT at eof
		if (!eof)
			m_inStream.unread(ch);
	}

	//
	// Reads a token from the input stream into a TokenDescriptor
	// and returns it.
	//
	public TokenDescriptor getToken()
			throws IOException, DCSyntaxErrorException
	{
		String tokenText;
		TokenCode tokenCode;
		char ch;

		// Get a character from the input.
		ch = (char)m_inStream.read();

		// The genre and/or value of character read will determine
		// how to proceed.
		if (Character.isDigit(ch)) {
			//
			// Numeric constant
			//

			// Put the character back so readNumber() can acquire
			// it.
			m_inStream.unread(ch);

			// Read the numeric literal.
			tokenText = readNumber();
			tokenCode = TokenCode.T_CONST;
		} else if (Character.isAlphabetic(ch)) {
			//
			// Alpha tokens; could be a keyword or an identifier.
			//

			// Put the character back so readAlpha() can acquire
			// it.
			m_inStream.unread(ch);

			// Read all consecutive alpha characters.
			tokenText = readAlpha();

			// If the token appears in the keyword dictionary, the
			// token is clearly a keyword; otherwise it is an
			// identifier.
			if (m_keywords.containsKey(tokenText)) {
				// Keyword; lookup token ID.
				tokenCode = m_keywords.get(tokenText);
			} else {
				// Identifier; token ID is 28.
				tokenCode = TokenCode.T_ID;
			}
		} else if (ch == ' ' || ch == '\t') {
			//
			// Whitespace tokens.
			//

			// Put back the space so readSpace() can acquire it.
			m_inStream.unread(ch);

			// Eat all of the space characters - they are
			// collapsed into one token.
			readSpace();

			tokenText = " ";
			tokenCode = TokenCode.T_SPACE;
		}

		/*
		else if (ch == '\"') {
			//
			// String literal tokens.
			//

			// Put back the opening quotation mark so readString()
			// can acquire it.
			m_inStream.unread(ch);

			// Read the string literal.
			tokenText = readString();
			tokenCode = ;
		}
		*/

		else if (ch == '\n') {
			//
			// Newline token.
			//

			tokenText = "\n";
			// tokenCode = TokenCode.T_EOL;

			//
			// 2015-04-25 (RS) - for Descartes, since spaces and
			//		newlines have the same "authority" to separate
			//		tokens, let's just convert newlines into space
			//		tokens. This unclutters the parsing code a
			// 		bit.
			//

			tokenCode = TokenCode.T_SPACE;
		} else if (isPunct(ch)) {
			//
			// Possibly an operator token. This is different from
			// the other token types because consecutive
			// punctuation characters may be a long series of many
			// different tokens, not simply one long one.
			//

			StringBuilder token = new StringBuilder();
			token.append(ch);

			// If the following char is also a punct char, see if
			// the two characters together form an operator.
			ch = (char)m_inStream.read();
			if (isPunct(ch)) {
				token.append(ch);
				if (!m_operators.containsKey(token.toString())) {
					// The two characters combined are not an
					// operator; remove the second char and put it
					// back in the input stream.
					token.deleteCharAt(1);
					m_inStream.unread(ch);
				}
			} else {
				// The next character isn't a punct char; put it
				// back in the input stream.
				m_inStream.unread(ch);
			}
			tokenText = token.toString();

			// Right now this can only happen if an isolated colon
			// (:) is found in the input. It could happen more
			// often if we used a proper isPunct() method.
			if (!m_operators.containsKey(tokenText)) {
				throw new DCSyntaxErrorException(
						"Unrecognized punctuation token: " +
								"\"" + tokenText + "\""
				);
			}

			// Look up operator tokenCode.
			tokenCode = m_operators.get(tokenText);
		}
		else {
			//
			// Unrecognized character type.
			//

			throw new DCSyntaxErrorException(
					"Unrecognized character in input stream: " +
							"\"" + Character.toString(ch) + "\"" +
							" (ASCII: " + (int)ch + ")"
			);
		}

		return new TokenDescriptor(tokenText, tokenCode);
	}

	//
	// Returns true if there are no more tokens to read from the
	// input stream.
	//
	public boolean atEnd() throws IOException {
		return (m_inStream.available() == 0);
	}


	//================//
	// Program entry. //
	//================//

	//
	// Usage:
	//
	//		java TokenReader [SOURCE_FILE]
	//
	// If no source file is provided, redirected stdin is assumed,
	// e.g.
	//
	//		java TokenReader < token.dat
	//
	public static void main(String[] args) {
		// Open source file (if name
		// provided; otherwise read from stdin).
		InputStream inStream = System.in;
		try {
			if (args.length > 0) {
				inStream = new FileInputStream(args[0]);
			}
		} catch (FileNotFoundException e) {
			System.out.println("Source file not found " + args[0]);
			System.exit(-1);
		}


		// Read tokens from source file.
		TokenReader tokenReader = new TokenReader(inStream);
		TokenDescriptor tokenDesc;
		try {
			while (!tokenReader.atEnd()) {
				tokenDesc = tokenReader.getToken();
				tokenDesc.print();
			}

			// Done. Close the input stream.
			inStream.close();
		} catch (DCSyntaxErrorException e) {
			//
			// A syntax error occurred. Don't need to show a stack
			// trace for that.
			//

			System.out.println(e.getMessage());
		} catch (Exception e) {
			//
			// An unexpected exception was thrown.
			//

			System.out.println("Caught exception " + e.getClass().getName());
			System.out.println(e.getMessage());
			e.printStackTrace();
			System.exit(-2);
		}
	}
}

// (eof)
