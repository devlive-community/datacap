// Generated from io/edurt/datacap/sql/parser/SqlBase.g4 by ANTLR 4.12.0
package io.edurt.datacap.sql.parser;
import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast", "CheckReturnValue"})
public class SqlBaseLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.12.0", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, SHOW=2, PATHS=3, TOPICS=4, CONSUMERS=5, SELECT=6, FROM=7, STRING=8, 
		IDENTIFIER=9, BACKQUOTED_IDENTIFIER=10, SIMPLE_COMMENT=11, BRACKETED_EMPTY_COMMENT=12, 
		BRACKETED_COMMENT=13, WS=14;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "SHOW", "PATHS", "TOPICS", "CONSUMERS", "SELECT", "FROM", "DIGIT", 
			"LETTER", "STRING", "IDENTIFIER", "BACKQUOTED_IDENTIFIER", "SIMPLE_COMMENT", 
			"BRACKETED_EMPTY_COMMENT", "BRACKETED_COMMENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", null, null, "'TOPICS'", "'CONSUMERS'", null, null, null, 
			null, null, null, "'/**/'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "SHOW", "PATHS", "TOPICS", "CONSUMERS", "SELECT", "FROM", 
			"STRING", "IDENTIFIER", "BACKQUOTED_IDENTIFIER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
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
		"\u0004\u0000\u000e\u00a6\u0006\uffff\uffff\u0002\u0000\u0007\u0000\u0002"+
		"\u0001\u0007\u0001\u0002\u0002\u0007\u0002\u0002\u0003\u0007\u0003\u0002"+
		"\u0004\u0007\u0004\u0002\u0005\u0007\u0005\u0002\u0006\u0007\u0006\u0002"+
		"\u0007\u0007\u0007\u0002\b\u0007\b\u0002\t\u0007\t\u0002\n\u0007\n\u0002"+
		"\u000b\u0007\u000b\u0002\f\u0007\f\u0002\r\u0007\r\u0002\u000e\u0007\u000e"+
		"\u0002\u000f\u0007\u000f\u0001\u0000\u0001\u0000\u0001\u0001\u0001\u0001"+
		"\u0001\u0001\u0001\u0001\u0001\u0001\u0001\u0002\u0001\u0002\u0001\u0002"+
		"\u0001\u0002\u0001\u0002\u0001\u0002\u0001\u0003\u0001\u0003\u0001\u0003"+
		"\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0003\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004\u0001\u0004"+
		"\u0001\u0004\u0001\u0004\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0005"+
		"\u0001\u0005\u0001\u0005\u0001\u0005\u0001\u0006\u0001\u0006\u0001\u0006"+
		"\u0001\u0006\u0001\u0006\u0001\u0007\u0001\u0007\u0001\b\u0001\b\u0001"+
		"\t\u0001\t\u0001\t\u0001\t\u0005\tT\b\t\n\t\f\tW\t\t\u0001\t\u0001\t\u0001"+
		"\t\u0001\t\u0001\t\u0005\t^\b\t\n\t\f\ta\t\t\u0001\t\u0001\t\u0003\te"+
		"\b\t\u0001\n\u0001\n\u0001\n\u0004\nj\b\n\u000b\n\f\nk\u0001\u000b\u0001"+
		"\u000b\u0001\u000b\u0001\u000b\u0005\u000br\b\u000b\n\u000b\f\u000bu\t"+
		"\u000b\u0001\u000b\u0001\u000b\u0001\f\u0001\f\u0001\f\u0001\f\u0005\f"+
		"}\b\f\n\f\f\f\u0080\t\f\u0001\f\u0003\f\u0083\b\f\u0001\f\u0003\f\u0086"+
		"\b\f\u0001\f\u0001\f\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001\r\u0001"+
		"\r\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0001\u000e\u0005\u000e"+
		"\u0096\b\u000e\n\u000e\f\u000e\u0099\t\u000e\u0001\u000e\u0001\u000e\u0001"+
		"\u000e\u0001\u000e\u0001\u000e\u0001\u000f\u0004\u000f\u00a1\b\u000f\u000b"+
		"\u000f\f\u000f\u00a2\u0001\u000f\u0001\u000f\u0001\u0097\u0000\u0010\u0001"+
		"\u0001\u0003\u0002\u0005\u0003\u0007\u0004\t\u0005\u000b\u0006\r\u0007"+
		"\u000f\u0000\u0011\u0000\u0013\b\u0015\t\u0017\n\u0019\u000b\u001b\f\u001d"+
		"\r\u001f\u000e\u0001\u0000\u0015\u0002\u0000SSss\u0002\u0000HHhh\u0002"+
		"\u0000OOoo\u0002\u0000WWww\u0002\u0000PPpp\u0002\u0000AAaa\u0002\u0000"+
		"TTtt\u0002\u0000EEee\u0002\u0000LLll\u0002\u0000CCcc\u0002\u0000FFff\u0002"+
		"\u0000RRrr\u0002\u0000MMmm\u0001\u000009\u0002\u0000AZaz\u0002\u0000\'"+
		"\'\\\\\u0002\u0000\"\"\\\\\u0001\u0000``\u0002\u0000\n\n\r\r\u0001\u0000"+
		"++\u0003\u0000\t\n\r\r  \u00b3\u0000\u0001\u0001\u0000\u0000\u0000\u0000"+
		"\u0003\u0001\u0000\u0000\u0000\u0000\u0005\u0001\u0000\u0000\u0000\u0000"+
		"\u0007\u0001\u0000\u0000\u0000\u0000\t\u0001\u0000\u0000\u0000\u0000\u000b"+
		"\u0001\u0000\u0000\u0000\u0000\r\u0001\u0000\u0000\u0000\u0000\u0013\u0001"+
		"\u0000\u0000\u0000\u0000\u0015\u0001\u0000\u0000\u0000\u0000\u0017\u0001"+
		"\u0000\u0000\u0000\u0000\u0019\u0001\u0000\u0000\u0000\u0000\u001b\u0001"+
		"\u0000\u0000\u0000\u0000\u001d\u0001\u0000\u0000\u0000\u0000\u001f\u0001"+
		"\u0000\u0000\u0000\u0001!\u0001\u0000\u0000\u0000\u0003#\u0001\u0000\u0000"+
		"\u0000\u0005(\u0001\u0000\u0000\u0000\u0007.\u0001\u0000\u0000\u0000\t"+
		"5\u0001\u0000\u0000\u0000\u000b?\u0001\u0000\u0000\u0000\rF\u0001\u0000"+
		"\u0000\u0000\u000fK\u0001\u0000\u0000\u0000\u0011M\u0001\u0000\u0000\u0000"+
		"\u0013d\u0001\u0000\u0000\u0000\u0015i\u0001\u0000\u0000\u0000\u0017m"+
		"\u0001\u0000\u0000\u0000\u0019x\u0001\u0000\u0000\u0000\u001b\u0089\u0001"+
		"\u0000\u0000\u0000\u001d\u0090\u0001\u0000\u0000\u0000\u001f\u00a0\u0001"+
		"\u0000\u0000\u0000!\"\u0005.\u0000\u0000\"\u0002\u0001\u0000\u0000\u0000"+
		"#$\u0007\u0000\u0000\u0000$%\u0007\u0001\u0000\u0000%&\u0007\u0002\u0000"+
		"\u0000&\'\u0007\u0003\u0000\u0000\'\u0004\u0001\u0000\u0000\u0000()\u0007"+
		"\u0004\u0000\u0000)*\u0007\u0005\u0000\u0000*+\u0007\u0006\u0000\u0000"+
		"+,\u0007\u0001\u0000\u0000,-\u0007\u0000\u0000\u0000-\u0006\u0001\u0000"+
		"\u0000\u0000./\u0005T\u0000\u0000/0\u0005O\u0000\u000001\u0005P\u0000"+
		"\u000012\u0005I\u0000\u000023\u0005C\u0000\u000034\u0005S\u0000\u0000"+
		"4\b\u0001\u0000\u0000\u000056\u0005C\u0000\u000067\u0005O\u0000\u0000"+
		"78\u0005N\u0000\u000089\u0005S\u0000\u00009:\u0005U\u0000\u0000:;\u0005"+
		"M\u0000\u0000;<\u0005E\u0000\u0000<=\u0005R\u0000\u0000=>\u0005S\u0000"+
		"\u0000>\n\u0001\u0000\u0000\u0000?@\u0007\u0000\u0000\u0000@A\u0007\u0007"+
		"\u0000\u0000AB\u0007\b\u0000\u0000BC\u0007\u0007\u0000\u0000CD\u0007\t"+
		"\u0000\u0000DE\u0007\u0006\u0000\u0000E\f\u0001\u0000\u0000\u0000FG\u0007"+
		"\n\u0000\u0000GH\u0007\u000b\u0000\u0000HI\u0007\u0002\u0000\u0000IJ\u0007"+
		"\f\u0000\u0000J\u000e\u0001\u0000\u0000\u0000KL\u0007\r\u0000\u0000L\u0010"+
		"\u0001\u0000\u0000\u0000MN\u0007\u000e\u0000\u0000N\u0012\u0001\u0000"+
		"\u0000\u0000OU\u0005\'\u0000\u0000PT\b\u000f\u0000\u0000QR\u0005\\\u0000"+
		"\u0000RT\t\u0000\u0000\u0000SP\u0001\u0000\u0000\u0000SQ\u0001\u0000\u0000"+
		"\u0000TW\u0001\u0000\u0000\u0000US\u0001\u0000\u0000\u0000UV\u0001\u0000"+
		"\u0000\u0000VX\u0001\u0000\u0000\u0000WU\u0001\u0000\u0000\u0000Xe\u0005"+
		"\'\u0000\u0000Y_\u0005\"\u0000\u0000Z^\b\u0010\u0000\u0000[\\\u0005\\"+
		"\u0000\u0000\\^\t\u0000\u0000\u0000]Z\u0001\u0000\u0000\u0000][\u0001"+
		"\u0000\u0000\u0000^a\u0001\u0000\u0000\u0000_]\u0001\u0000\u0000\u0000"+
		"_`\u0001\u0000\u0000\u0000`b\u0001\u0000\u0000\u0000a_\u0001\u0000\u0000"+
		"\u0000be\u0005\"\u0000\u0000ce\u0005*\u0000\u0000dO\u0001\u0000\u0000"+
		"\u0000dY\u0001\u0000\u0000\u0000dc\u0001\u0000\u0000\u0000e\u0014\u0001"+
		"\u0000\u0000\u0000fj\u0003\u0011\b\u0000gj\u0003\u000f\u0007\u0000hj\u0005"+
		"_\u0000\u0000if\u0001\u0000\u0000\u0000ig\u0001\u0000\u0000\u0000ih\u0001"+
		"\u0000\u0000\u0000jk\u0001\u0000\u0000\u0000ki\u0001\u0000\u0000\u0000"+
		"kl\u0001\u0000\u0000\u0000l\u0016\u0001\u0000\u0000\u0000ms\u0005`\u0000"+
		"\u0000nr\b\u0011\u0000\u0000op\u0005`\u0000\u0000pr\u0005`\u0000\u0000"+
		"qn\u0001\u0000\u0000\u0000qo\u0001\u0000\u0000\u0000ru\u0001\u0000\u0000"+
		"\u0000sq\u0001\u0000\u0000\u0000st\u0001\u0000\u0000\u0000tv\u0001\u0000"+
		"\u0000\u0000us\u0001\u0000\u0000\u0000vw\u0005`\u0000\u0000w\u0018\u0001"+
		"\u0000\u0000\u0000xy\u0005-\u0000\u0000yz\u0005-\u0000\u0000z~\u0001\u0000"+
		"\u0000\u0000{}\b\u0012\u0000\u0000|{\u0001\u0000\u0000\u0000}\u0080\u0001"+
		"\u0000\u0000\u0000~|\u0001\u0000\u0000\u0000~\u007f\u0001\u0000\u0000"+
		"\u0000\u007f\u0082\u0001\u0000\u0000\u0000\u0080~\u0001\u0000\u0000\u0000"+
		"\u0081\u0083\u0005\r\u0000\u0000\u0082\u0081\u0001\u0000\u0000\u0000\u0082"+
		"\u0083\u0001\u0000\u0000\u0000\u0083\u0085\u0001\u0000\u0000\u0000\u0084"+
		"\u0086\u0005\n\u0000\u0000\u0085\u0084\u0001\u0000\u0000\u0000\u0085\u0086"+
		"\u0001\u0000\u0000\u0000\u0086\u0087\u0001\u0000\u0000\u0000\u0087\u0088"+
		"\u0006\f\u0000\u0000\u0088\u001a\u0001\u0000\u0000\u0000\u0089\u008a\u0005"+
		"/\u0000\u0000\u008a\u008b\u0005*\u0000\u0000\u008b\u008c\u0005*\u0000"+
		"\u0000\u008c\u008d\u0005/\u0000\u0000\u008d\u008e\u0001\u0000\u0000\u0000"+
		"\u008e\u008f\u0006\r\u0000\u0000\u008f\u001c\u0001\u0000\u0000\u0000\u0090"+
		"\u0091\u0005/\u0000\u0000\u0091\u0092\u0005*\u0000\u0000\u0092\u0093\u0001"+
		"\u0000\u0000\u0000\u0093\u0097\b\u0013\u0000\u0000\u0094\u0096\t\u0000"+
		"\u0000\u0000\u0095\u0094\u0001\u0000\u0000\u0000\u0096\u0099\u0001\u0000"+
		"\u0000\u0000\u0097\u0098\u0001\u0000\u0000\u0000\u0097\u0095\u0001\u0000"+
		"\u0000\u0000\u0098\u009a\u0001\u0000\u0000\u0000\u0099\u0097\u0001\u0000"+
		"\u0000\u0000\u009a\u009b\u0005*\u0000\u0000\u009b\u009c\u0005/\u0000\u0000"+
		"\u009c\u009d\u0001\u0000\u0000\u0000\u009d\u009e\u0006\u000e\u0000\u0000"+
		"\u009e\u001e\u0001\u0000\u0000\u0000\u009f\u00a1\u0007\u0014\u0000\u0000"+
		"\u00a0\u009f\u0001\u0000\u0000\u0000\u00a1\u00a2\u0001\u0000\u0000\u0000"+
		"\u00a2\u00a0\u0001\u0000\u0000\u0000\u00a2\u00a3\u0001\u0000\u0000\u0000"+
		"\u00a3\u00a4\u0001\u0000\u0000\u0000\u00a4\u00a5\u0006\u000f\u0000\u0000"+
		"\u00a5 \u0001\u0000\u0000\u0000\u000f\u0000SU]_dikqs~\u0082\u0085\u0097"+
		"\u00a2\u0001\u0000\u0001\u0000";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}