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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\20\u00a8\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\3\2\3"+
		"\2\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\5\3\5\3\5"+
		"\3\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\3\7\3\7\3"+
		"\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\13\3\13\3\13\3\13\7\13V\n\13"+
		"\f\13\16\13Y\13\13\3\13\3\13\3\13\3\13\3\13\7\13`\n\13\f\13\16\13c\13"+
		"\13\3\13\3\13\5\13g\n\13\3\f\3\f\3\f\6\fl\n\f\r\f\16\fm\3\r\3\r\3\r\3"+
		"\r\7\rt\n\r\f\r\16\rw\13\r\3\r\3\r\3\16\3\16\3\16\3\16\7\16\177\n\16\f"+
		"\16\16\16\u0082\13\16\3\16\5\16\u0085\n\16\3\16\5\16\u0088\n\16\3\16\3"+
		"\16\3\17\3\17\3\17\3\17\3\17\3\17\3\17\3\20\3\20\3\20\3\20\3\20\7\20\u0098"+
		"\n\20\f\20\16\20\u009b\13\20\3\20\3\20\3\20\3\20\3\20\3\21\6\21\u00a3"+
		"\n\21\r\21\16\21\u00a4\3\21\3\21\3\u0099\2\22\3\3\5\4\7\5\t\6\13\7\r\b"+
		"\17\t\21\2\23\2\25\n\27\13\31\f\33\r\35\16\37\17!\20\3\2\27\4\2UUuu\4"+
		"\2JJjj\4\2QQqq\4\2YYyy\4\2RRrr\4\2CCcc\4\2VVvv\4\2GGgg\4\2NNnn\4\2EEe"+
		"e\4\2HHhh\4\2TTtt\4\2OOoo\3\2\62;\4\2C\\c|\4\2))^^\4\2$$^^\3\2bb\4\2\f"+
		"\f\17\17\3\2--\5\2\13\f\17\17\"\"\2\u00b5\2\3\3\2\2\2\2\5\3\2\2\2\2\7"+
		"\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2\2\2\2\25\3\2\2"+
		"\2\2\27\3\2\2\2\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2"+
		"!\3\2\2\2\3#\3\2\2\2\5%\3\2\2\2\7*\3\2\2\2\t\60\3\2\2\2\13\67\3\2\2\2"+
		"\rA\3\2\2\2\17H\3\2\2\2\21M\3\2\2\2\23O\3\2\2\2\25f\3\2\2\2\27k\3\2\2"+
		"\2\31o\3\2\2\2\33z\3\2\2\2\35\u008b\3\2\2\2\37\u0092\3\2\2\2!\u00a2\3"+
		"\2\2\2#$\7\60\2\2$\4\3\2\2\2%&\t\2\2\2&\'\t\3\2\2\'(\t\4\2\2()\t\5\2\2"+
		")\6\3\2\2\2*+\t\6\2\2+,\t\7\2\2,-\t\b\2\2-.\t\3\2\2./\t\2\2\2/\b\3\2\2"+
		"\2\60\61\7V\2\2\61\62\7Q\2\2\62\63\7R\2\2\63\64\7K\2\2\64\65\7E\2\2\65"+
		"\66\7U\2\2\66\n\3\2\2\2\678\7E\2\289\7Q\2\29:\7P\2\2:;\7U\2\2;<\7W\2\2"+
		"<=\7O\2\2=>\7G\2\2>?\7T\2\2?@\7U\2\2@\f\3\2\2\2AB\t\2\2\2BC\t\t\2\2CD"+
		"\t\n\2\2DE\t\t\2\2EF\t\13\2\2FG\t\b\2\2G\16\3\2\2\2HI\t\f\2\2IJ\t\r\2"+
		"\2JK\t\4\2\2KL\t\16\2\2L\20\3\2\2\2MN\t\17\2\2N\22\3\2\2\2OP\t\20\2\2"+
		"P\24\3\2\2\2QW\7)\2\2RV\n\21\2\2ST\7^\2\2TV\13\2\2\2UR\3\2\2\2US\3\2\2"+
		"\2VY\3\2\2\2WU\3\2\2\2WX\3\2\2\2XZ\3\2\2\2YW\3\2\2\2Zg\7)\2\2[a\7$\2\2"+
		"\\`\n\22\2\2]^\7^\2\2^`\13\2\2\2_\\\3\2\2\2_]\3\2\2\2`c\3\2\2\2a_\3\2"+
		"\2\2ab\3\2\2\2bd\3\2\2\2ca\3\2\2\2dg\7$\2\2eg\7,\2\2fQ\3\2\2\2f[\3\2\2"+
		"\2fe\3\2\2\2g\26\3\2\2\2hl\5\23\n\2il\5\21\t\2jl\7a\2\2kh\3\2\2\2ki\3"+
		"\2\2\2kj\3\2\2\2lm\3\2\2\2mk\3\2\2\2mn\3\2\2\2n\30\3\2\2\2ou\7b\2\2pt"+
		"\n\23\2\2qr\7b\2\2rt\7b\2\2sp\3\2\2\2sq\3\2\2\2tw\3\2\2\2us\3\2\2\2uv"+
		"\3\2\2\2vx\3\2\2\2wu\3\2\2\2xy\7b\2\2y\32\3\2\2\2z{\7/\2\2{|\7/\2\2|\u0080"+
		"\3\2\2\2}\177\n\24\2\2~}\3\2\2\2\177\u0082\3\2\2\2\u0080~\3\2\2\2\u0080"+
		"\u0081\3\2\2\2\u0081\u0084\3\2\2\2\u0082\u0080\3\2\2\2\u0083\u0085\7\17"+
		"\2\2\u0084\u0083\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0087\3\2\2\2\u0086"+
		"\u0088\7\f\2\2\u0087\u0086\3\2\2\2\u0087\u0088\3\2\2\2\u0088\u0089\3\2"+
		"\2\2\u0089\u008a\b\16\2\2\u008a\34\3\2\2\2\u008b\u008c\7\61\2\2\u008c"+
		"\u008d\7,\2\2\u008d\u008e\7,\2\2\u008e\u008f\7\61\2\2\u008f\u0090\3\2"+
		"\2\2\u0090\u0091\b\17\2\2\u0091\36\3\2\2\2\u0092\u0093\7\61\2\2\u0093"+
		"\u0094\7,\2\2\u0094\u0095\3\2\2\2\u0095\u0099\n\25\2\2\u0096\u0098\13"+
		"\2\2\2\u0097\u0096\3\2\2\2\u0098\u009b\3\2\2\2\u0099\u009a\3\2\2\2\u0099"+
		"\u0097\3\2\2\2\u009a\u009c\3\2\2\2\u009b\u0099\3\2\2\2\u009c\u009d\7,"+
		"\2\2\u009d\u009e\7\61\2\2\u009e\u009f\3\2\2\2\u009f\u00a0\b\20\2\2\u00a0"+
		" \3\2\2\2\u00a1\u00a3\t\26\2\2\u00a2\u00a1\3\2\2\2\u00a3\u00a4\3\2\2\2"+
		"\u00a4\u00a2\3\2\2\2\u00a4\u00a5\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7"+
		"\b\21\2\2\u00a7\"\3\2\2\2\21\2UW_afkmsu\u0080\u0084\u0087\u0099\u00a4"+
		"\3\2\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}