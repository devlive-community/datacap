// Generated from io/edurt/datacap/sql/parser/SqlBase.g4 by ANTLR 4.9.3
package io.edurt.datacap.sql.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class SqlBaseLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, SHOW=2, PATHS=3, SELECT=4, FROM=5, STRING=6, IDENTIFIER=7, BACKQUOTED_IDENTIFIER=8, 
		SIMPLE_COMMENT=9, BRACKETED_EMPTY_COMMENT=10, BRACKETED_COMMENT=11, WS=12;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "SHOW", "PATHS", "SELECT", "FROM", "DIGIT", "LETTER", "STRING", 
			"IDENTIFIER", "BACKQUOTED_IDENTIFIER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
			"BRACKETED_COMMENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", null, null, null, null, null, null, null, null, "'/**/'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "SHOW", "PATHS", "SELECT", "FROM", "STRING", "IDENTIFIER", 
			"BACKQUOTED_IDENTIFIER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
			"BRACKETED_COMMENT", "WS"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public SqlBaseLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "SqlBase.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getChannelNames() { return channelNames; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\16\u0093\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\3\2\3\2\3\3\3\3\3\3\3\3\3"+
		"\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5\3\5\3\6\3\6\3\6\3\6"+
		"\3\6\3\7\3\7\3\b\3\b\3\t\3\t\3\t\3\t\7\tA\n\t\f\t\16\tD\13\t\3\t\3\t\3"+
		"\t\3\t\3\t\7\tK\n\t\f\t\16\tN\13\t\3\t\3\t\5\tR\n\t\3\n\3\n\3\n\6\nW\n"+
		"\n\r\n\16\nX\3\13\3\13\3\13\3\13\7\13_\n\13\f\13\16\13b\13\13\3\13\3\13"+
		"\3\f\3\f\3\f\3\f\7\fj\n\f\f\f\16\fm\13\f\3\f\5\fp\n\f\3\f\5\fs\n\f\3\f"+
		"\3\f\3\r\3\r\3\r\3\r\3\r\3\r\3\r\3\16\3\16\3\16\3\16\3\16\7\16\u0083\n"+
		"\16\f\16\16\16\u0086\13\16\3\16\3\16\3\16\3\16\3\16\3\17\6\17\u008e\n"+
		"\17\r\17\16\17\u008f\3\17\3\17\3\u0084\2\20\3\3\5\4\7\5\t\6\13\7\r\2\17"+
		"\2\21\b\23\t\25\n\27\13\31\f\33\r\35\16\3\2\27\4\2UUuu\4\2JJjj\4\2QQq"+
		"q\4\2YYyy\4\2RRrr\4\2CCcc\4\2VVvv\4\2GGgg\4\2NNnn\4\2EEee\4\2HHhh\4\2"+
		"TTtt\4\2OOoo\3\2\62;\4\2C\\c|\4\2))^^\4\2$$^^\3\2bb\4\2\f\f\17\17\3\2"+
		"--\5\2\13\f\17\17\"\"\2\u00a0\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t"+
		"\3\2\2\2\2\13\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2"+
		"\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\3\37\3\2\2\2\5!\3\2\2\2\7"+
		"&\3\2\2\2\t,\3\2\2\2\13\63\3\2\2\2\r8\3\2\2\2\17:\3\2\2\2\21Q\3\2\2\2"+
		"\23V\3\2\2\2\25Z\3\2\2\2\27e\3\2\2\2\31v\3\2\2\2\33}\3\2\2\2\35\u008d"+
		"\3\2\2\2\37 \7\60\2\2 \4\3\2\2\2!\"\t\2\2\2\"#\t\3\2\2#$\t\4\2\2$%\t\5"+
		"\2\2%\6\3\2\2\2&\'\t\6\2\2\'(\t\7\2\2()\t\b\2\2)*\t\3\2\2*+\t\2\2\2+\b"+
		"\3\2\2\2,-\t\2\2\2-.\t\t\2\2./\t\n\2\2/\60\t\t\2\2\60\61\t\13\2\2\61\62"+
		"\t\b\2\2\62\n\3\2\2\2\63\64\t\f\2\2\64\65\t\r\2\2\65\66\t\4\2\2\66\67"+
		"\t\16\2\2\67\f\3\2\2\289\t\17\2\29\16\3\2\2\2:;\t\20\2\2;\20\3\2\2\2<"+
		"B\7)\2\2=A\n\21\2\2>?\7^\2\2?A\13\2\2\2@=\3\2\2\2@>\3\2\2\2AD\3\2\2\2"+
		"B@\3\2\2\2BC\3\2\2\2CE\3\2\2\2DB\3\2\2\2ER\7)\2\2FL\7$\2\2GK\n\22\2\2"+
		"HI\7^\2\2IK\13\2\2\2JG\3\2\2\2JH\3\2\2\2KN\3\2\2\2LJ\3\2\2\2LM\3\2\2\2"+
		"MO\3\2\2\2NL\3\2\2\2OR\7$\2\2PR\7,\2\2Q<\3\2\2\2QF\3\2\2\2QP\3\2\2\2R"+
		"\22\3\2\2\2SW\5\17\b\2TW\5\r\7\2UW\7a\2\2VS\3\2\2\2VT\3\2\2\2VU\3\2\2"+
		"\2WX\3\2\2\2XV\3\2\2\2XY\3\2\2\2Y\24\3\2\2\2Z`\7b\2\2[_\n\23\2\2\\]\7"+
		"b\2\2]_\7b\2\2^[\3\2\2\2^\\\3\2\2\2_b\3\2\2\2`^\3\2\2\2`a\3\2\2\2ac\3"+
		"\2\2\2b`\3\2\2\2cd\7b\2\2d\26\3\2\2\2ef\7/\2\2fg\7/\2\2gk\3\2\2\2hj\n"+
		"\24\2\2ih\3\2\2\2jm\3\2\2\2ki\3\2\2\2kl\3\2\2\2lo\3\2\2\2mk\3\2\2\2np"+
		"\7\17\2\2on\3\2\2\2op\3\2\2\2pr\3\2\2\2qs\7\f\2\2rq\3\2\2\2rs\3\2\2\2"+
		"st\3\2\2\2tu\b\f\2\2u\30\3\2\2\2vw\7\61\2\2wx\7,\2\2xy\7,\2\2yz\7\61\2"+
		"\2z{\3\2\2\2{|\b\r\2\2|\32\3\2\2\2}~\7\61\2\2~\177\7,\2\2\177\u0080\3"+
		"\2\2\2\u0080\u0084\n\25\2\2\u0081\u0083\13\2\2\2\u0082\u0081\3\2\2\2\u0083"+
		"\u0086\3\2\2\2\u0084\u0085\3\2\2\2\u0084\u0082\3\2\2\2\u0085\u0087\3\2"+
		"\2\2\u0086\u0084\3\2\2\2\u0087\u0088\7,\2\2\u0088\u0089\7\61\2\2\u0089"+
		"\u008a\3\2\2\2\u008a\u008b\b\16\2\2\u008b\34\3\2\2\2\u008c\u008e\t\26"+
		"\2\2\u008d\u008c\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u008d\3\2\2\2\u008f"+
		"\u0090\3\2\2\2\u0090\u0091\3\2\2\2\u0091\u0092\b\17\2\2\u0092\36\3\2\2"+
		"\2\21\2@BJLQVX^`kor\u0084\u008f\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}