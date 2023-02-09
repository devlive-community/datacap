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
		T__0=1, SELECT=2, FROM=3, STRING=4, IDENTIFIER=5, BACKQUOTED_IDENTIFIER=6, 
		SIMPLE_COMMENT=7, BRACKETED_EMPTY_COMMENT=8, BRACKETED_COMMENT=9, WS=10;
	public static String[] channelNames = {
		"DEFAULT_TOKEN_CHANNEL", "HIDDEN"
	};

	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	private static String[] makeRuleNames() {
		return new String[] {
			"T__0", "SELECT", "FROM", "DIGIT", "LETTER", "STRING", "IDENTIFIER", 
			"BACKQUOTED_IDENTIFIER", "SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", 
			"BRACKETED_COMMENT", "WS"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'.'", null, null, null, null, null, null, "'/**/'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, null, "SELECT", "FROM", "STRING", "IDENTIFIER", "BACKQUOTED_IDENTIFIER", 
			"SIMPLE_COMMENT", "BRACKETED_EMPTY_COMMENT", "BRACKETED_COMMENT", "WS"
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\2\f\u0084\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\3\2\3\2\3\3\3\3\3\3\3\3\3\3\3\3\3\3\3\4\3\4\3\4"+
		"\3\4\3\4\3\5\3\5\3\6\3\6\3\7\3\7\3\7\3\7\7\7\62\n\7\f\7\16\7\65\13\7\3"+
		"\7\3\7\3\7\3\7\3\7\7\7<\n\7\f\7\16\7?\13\7\3\7\3\7\5\7C\n\7\3\b\3\b\3"+
		"\b\6\bH\n\b\r\b\16\bI\3\t\3\t\3\t\3\t\7\tP\n\t\f\t\16\tS\13\t\3\t\3\t"+
		"\3\n\3\n\3\n\3\n\7\n[\n\n\f\n\16\n^\13\n\3\n\5\na\n\n\3\n\5\nd\n\n\3\n"+
		"\3\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\3\f\3\f\3\f\3\f\3\f\7\ft\n\f\f"+
		"\f\16\fw\13\f\3\f\3\f\3\f\3\f\3\f\3\r\6\r\177\n\r\r\r\16\r\u0080\3\r\3"+
		"\r\3u\2\16\3\3\5\4\7\5\t\2\13\2\r\6\17\7\21\b\23\t\25\n\27\13\31\f\3\2"+
		"\23\4\2UUuu\4\2GGgg\4\2NNnn\4\2EEee\4\2VVvv\4\2HHhh\4\2TTtt\4\2QQqq\4"+
		"\2OOoo\3\2\62;\4\2C\\c|\4\2))^^\4\2$$^^\3\2bb\4\2\f\f\17\17\3\2--\5\2"+
		"\13\f\17\17\"\"\2\u0091\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\r\3\2\2"+
		"\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2"+
		"\31\3\2\2\2\3\33\3\2\2\2\5\35\3\2\2\2\7$\3\2\2\2\t)\3\2\2\2\13+\3\2\2"+
		"\2\rB\3\2\2\2\17G\3\2\2\2\21K\3\2\2\2\23V\3\2\2\2\25g\3\2\2\2\27n\3\2"+
		"\2\2\31~\3\2\2\2\33\34\7\60\2\2\34\4\3\2\2\2\35\36\t\2\2\2\36\37\t\3\2"+
		"\2\37 \t\4\2\2 !\t\3\2\2!\"\t\5\2\2\"#\t\6\2\2#\6\3\2\2\2$%\t\7\2\2%&"+
		"\t\b\2\2&\'\t\t\2\2\'(\t\n\2\2(\b\3\2\2\2)*\t\13\2\2*\n\3\2\2\2+,\t\f"+
		"\2\2,\f\3\2\2\2-\63\7)\2\2.\62\n\r\2\2/\60\7^\2\2\60\62\13\2\2\2\61.\3"+
		"\2\2\2\61/\3\2\2\2\62\65\3\2\2\2\63\61\3\2\2\2\63\64\3\2\2\2\64\66\3\2"+
		"\2\2\65\63\3\2\2\2\66C\7)\2\2\67=\7$\2\28<\n\16\2\29:\7^\2\2:<\13\2\2"+
		"\2;8\3\2\2\2;9\3\2\2\2<?\3\2\2\2=;\3\2\2\2=>\3\2\2\2>@\3\2\2\2?=\3\2\2"+
		"\2@C\7$\2\2AC\7,\2\2B-\3\2\2\2B\67\3\2\2\2BA\3\2\2\2C\16\3\2\2\2DH\5\13"+
		"\6\2EH\5\t\5\2FH\7a\2\2GD\3\2\2\2GE\3\2\2\2GF\3\2\2\2HI\3\2\2\2IG\3\2"+
		"\2\2IJ\3\2\2\2J\20\3\2\2\2KQ\7b\2\2LP\n\17\2\2MN\7b\2\2NP\7b\2\2OL\3\2"+
		"\2\2OM\3\2\2\2PS\3\2\2\2QO\3\2\2\2QR\3\2\2\2RT\3\2\2\2SQ\3\2\2\2TU\7b"+
		"\2\2U\22\3\2\2\2VW\7/\2\2WX\7/\2\2X\\\3\2\2\2Y[\n\20\2\2ZY\3\2\2\2[^\3"+
		"\2\2\2\\Z\3\2\2\2\\]\3\2\2\2]`\3\2\2\2^\\\3\2\2\2_a\7\17\2\2`_\3\2\2\2"+
		"`a\3\2\2\2ac\3\2\2\2bd\7\f\2\2cb\3\2\2\2cd\3\2\2\2de\3\2\2\2ef\b\n\2\2"+
		"f\24\3\2\2\2gh\7\61\2\2hi\7,\2\2ij\7,\2\2jk\7\61\2\2kl\3\2\2\2lm\b\13"+
		"\2\2m\26\3\2\2\2no\7\61\2\2op\7,\2\2pq\3\2\2\2qu\n\21\2\2rt\13\2\2\2s"+
		"r\3\2\2\2tw\3\2\2\2uv\3\2\2\2us\3\2\2\2vx\3\2\2\2wu\3\2\2\2xy\7,\2\2y"+
		"z\7\61\2\2z{\3\2\2\2{|\b\f\2\2|\30\3\2\2\2}\177\t\22\2\2~}\3\2\2\2\177"+
		"\u0080\3\2\2\2\u0080~\3\2\2\2\u0080\u0081\3\2\2\2\u0081\u0082\3\2\2\2"+
		"\u0082\u0083\b\r\2\2\u0083\32\3\2\2\2\21\2\61\63;=BGIOQ\\`cu\u0080\3\2"+
		"\3\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}