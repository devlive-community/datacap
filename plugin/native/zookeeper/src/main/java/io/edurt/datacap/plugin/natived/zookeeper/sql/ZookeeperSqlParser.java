package io.edurt.datacap.plugin.natived.zookeeper.sql;
// Generated from ZookeeperSql.g4 by ANTLR 4.9.3

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.Vocabulary;
import org.antlr.v4.runtime.VocabularyImpl;
import org.antlr.v4.runtime.atn.ATN;
import org.antlr.v4.runtime.atn.ATNDeserializer;
import org.antlr.v4.runtime.atn.ParserATNSimulator;
import org.antlr.v4.runtime.atn.PredictionContextCache;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.tree.ParseTreeListener;
import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.List;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class ZookeeperSqlParser
        extends Parser
{
    static {
        RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION);
    }

    protected static final DFA[] _decisionToDFA;
    protected static final PredictionContextCache _sharedContextCache =
            new PredictionContextCache();
    public static final int
            T__0 = 1, T__1 = 2, T__2 = 3, T__3 = 4, ID = 5, WS = 6, IDENTIFIER = 7, QUOTED_IDENTIFIER = 8,
            BACKQUOTED_IDENTIFIER = 9, DIGIT_IDENTIFIER = 10;
    public static final int
            RULE_singleStatement = 0, RULE_statement = 1, RULE_fromClause = 2, RULE_selectElements = 3,
            RULE_qualifiedName = 4, RULE_identifier = 5;

    private static String[] makeRuleNames()
    {
        return new String[] {
                "singleStatement", "statement", "fromClause", "selectElements", "qualifiedName",
                "identifier"
        };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames()
    {
        return new String[] {
                null, "'SELECT'", "'FROM'", "'*'", "'.'"
        };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames()
    {
        return new String[] {
                null, null, null, null, null, "ID", "WS", "IDENTIFIER", "QUOTED_IDENTIFIER",
                "BACKQUOTED_IDENTIFIER", "DIGIT_IDENTIFIER"
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
    public String[] getTokenNames()
    {
        return tokenNames;
    }

    @Override

    public Vocabulary getVocabulary()
    {
        return VOCABULARY;
    }

    @Override
    public String getGrammarFileName() {return "ZookeeperSql.g4";}

    @Override
    public String[] getRuleNames() {return ruleNames;}

    @Override
    public String getSerializedATN() {return _serializedATN;}

    @Override
    public ATN getATN() {return _ATN;}

    public ZookeeperSqlParser(TokenStream input)
    {
        super(input);
        _interp = new ParserATNSimulator(this, _ATN, _decisionToDFA, _sharedContextCache);
    }

    public static class SingleStatementContext
            extends ParserRuleContext
    {
        public StatementContext statement()
        {
            return getRuleContext(StatementContext.class, 0);
        }

        public TerminalNode EOF() {return getToken(ZookeeperSqlParser.EOF, 0);}

        public SingleStatementContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_singleStatement;}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).enterSingleStatement(this);
			}
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).exitSingleStatement(this);
			}
        }
    }

    public final SingleStatementContext singleStatement()
            throws RecognitionException
    {
        SingleStatementContext _localctx = new SingleStatementContext(_ctx, getState());
        enterRule(_localctx, 0, RULE_singleStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(12);
                statement();
                setState(13);
                match(EOF);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class StatementContext
            extends ParserRuleContext
    {
        public SelectElementsContext selectElements()
        {
            return getRuleContext(SelectElementsContext.class, 0);
        }

        public FromClauseContext fromClause()
        {
            return getRuleContext(FromClauseContext.class, 0);
        }

        public StatementContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_statement;}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).enterStatement(this);
			}
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).exitStatement(this);
			}
        }
    }

    public final StatementContext statement()
            throws RecognitionException
    {
        StatementContext _localctx = new StatementContext(_ctx, getState());
        enterRule(_localctx, 2, RULE_statement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(15);
                match(T__0);
                setState(16);
                selectElements();
                setState(17);
                fromClause();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class FromClauseContext
            extends ParserRuleContext
    {
        public QualifiedNameContext qualifiedName()
        {
            return getRuleContext(QualifiedNameContext.class, 0);
        }

        public FromClauseContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_fromClause;}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).enterFromClause(this);
			}
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).exitFromClause(this);
			}
        }
    }

    public final FromClauseContext fromClause()
            throws RecognitionException
    {
        FromClauseContext _localctx = new FromClauseContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_fromClause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(19);
                match(T__1);
                setState(20);
                qualifiedName();
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class SelectElementsContext
            extends ParserRuleContext
    {
        public TerminalNode ID() {return getToken(ZookeeperSqlParser.ID, 0);}

        public SelectElementsContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_selectElements;}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).enterSelectElements(this);
			}
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).exitSelectElements(this);
			}
        }
    }

    public final SelectElementsContext selectElements()
            throws RecognitionException
    {
        SelectElementsContext _localctx = new SelectElementsContext(_ctx, getState());
        enterRule(_localctx, 6, RULE_selectElements);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(22);
                _la = _input.LA(1);
                if (!(_la == T__2 || _la == ID)) {
                    _errHandler.recoverInline(this);
                }
                else {
					if (_input.LA(1) == Token.EOF) {
						matchedEOF = true;
					}
                    _errHandler.reportMatch(this);
                    consume();
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class QualifiedNameContext
            extends ParserRuleContext
    {
        public List<IdentifierContext> identifier()
        {
            return getRuleContexts(IdentifierContext.class);
        }

        public IdentifierContext identifier(int i)
        {
            return getRuleContext(IdentifierContext.class, i);
        }

        public QualifiedNameContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_qualifiedName;}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).enterQualifiedName(this);
			}
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).exitQualifiedName(this);
			}
        }
    }

    public final QualifiedNameContext qualifiedName()
            throws RecognitionException
    {
        QualifiedNameContext _localctx = new QualifiedNameContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_qualifiedName);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(24);
                identifier();
                setState(29);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__3) {
                    {
                        {
                            setState(25);
                            match(T__3);
                            setState(26);
                            identifier();
                        }
                    }
                    setState(31);
                    _errHandler.sync(this);
                    _la = _input.LA(1);
                }
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static class IdentifierContext
            extends ParserRuleContext
    {
        public IdentifierContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_identifier;}

        public IdentifierContext() {}

        public void copyFrom(IdentifierContext ctx)
        {
            super.copyFrom(ctx);
        }
    }

    public static class BackQuotedIdentifierContext
            extends IdentifierContext
    {
        public TerminalNode BACKQUOTED_IDENTIFIER() {return getToken(ZookeeperSqlParser.BACKQUOTED_IDENTIFIER, 0);}

        public BackQuotedIdentifierContext(IdentifierContext ctx) {copyFrom(ctx);}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).enterBackQuotedIdentifier(this);
			}
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).exitBackQuotedIdentifier(this);
			}
        }
    }

    public static class QuotedIdentifierContext
            extends IdentifierContext
    {
        public TerminalNode QUOTED_IDENTIFIER() {return getToken(ZookeeperSqlParser.QUOTED_IDENTIFIER, 0);}

        public QuotedIdentifierContext(IdentifierContext ctx) {copyFrom(ctx);}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).enterQuotedIdentifier(this);
			}
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).exitQuotedIdentifier(this);
			}
        }
    }

    public static class DigitIdentifierContext
            extends IdentifierContext
    {
        public TerminalNode DIGIT_IDENTIFIER() {return getToken(ZookeeperSqlParser.DIGIT_IDENTIFIER, 0);}

        public DigitIdentifierContext(IdentifierContext ctx) {copyFrom(ctx);}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).enterDigitIdentifier(this);
			}
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).exitDigitIdentifier(this);
			}
        }
    }

    public static class UnquotedIdentifierContext
            extends IdentifierContext
    {
        public TerminalNode IDENTIFIER() {return getToken(ZookeeperSqlParser.IDENTIFIER, 0);}

        public UnquotedIdentifierContext(IdentifierContext ctx) {copyFrom(ctx);}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).enterUnquotedIdentifier(this);
			}
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
			if (listener instanceof ZookeeperSqlListener) {
				((ZookeeperSqlListener) listener).exitUnquotedIdentifier(this);
			}
        }
    }

    public final IdentifierContext identifier()
            throws RecognitionException
    {
        IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_identifier);
        try {
            setState(36);
            _errHandler.sync(this);
            switch (_input.LA(1)) {
                case IDENTIFIER:
                    _localctx = new UnquotedIdentifierContext(_localctx);
                    enterOuterAlt(_localctx, 1);
                {
                    setState(32);
                    match(IDENTIFIER);
                }
                break;
                case QUOTED_IDENTIFIER:
                    _localctx = new QuotedIdentifierContext(_localctx);
                    enterOuterAlt(_localctx, 2);
                {
                    setState(33);
                    match(QUOTED_IDENTIFIER);
                }
                break;
                case BACKQUOTED_IDENTIFIER:
                    _localctx = new BackQuotedIdentifierContext(_localctx);
                    enterOuterAlt(_localctx, 3);
                {
                    setState(34);
                    match(BACKQUOTED_IDENTIFIER);
                }
                break;
                case DIGIT_IDENTIFIER:
                    _localctx = new DigitIdentifierContext(_localctx);
                    enterOuterAlt(_localctx, 4);
                {
                    setState(35);
                    match(DIGIT_IDENTIFIER);
                }
                break;
                default:
                    throw new NoViableAltException(this);
            }
        }
        catch (RecognitionException re) {
            _localctx.exception = re;
            _errHandler.reportError(this, re);
            _errHandler.recover(this, re);
        }
        finally {
            exitRule();
        }
        return _localctx;
    }

    public static final String _serializedATN =
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\f)\4\2\t\2\4\3\t" +
                    "\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\3\2\3\2\3\2\3\3\3\3\3\3\3\3\3\4\3\4" +
                    "\3\4\3\5\3\5\3\6\3\6\3\6\7\6\36\n\6\f\6\16\6!\13\6\3\7\3\7\3\7\3\7\5\7" +
                    "\'\n\7\3\7\2\2\b\2\4\6\b\n\f\2\3\4\2\5\5\7\7\2&\2\16\3\2\2\2\4\21\3\2" +
                    "\2\2\6\25\3\2\2\2\b\30\3\2\2\2\n\32\3\2\2\2\f&\3\2\2\2\16\17\5\4\3\2\17" +
                    "\20\7\2\2\3\20\3\3\2\2\2\21\22\7\3\2\2\22\23\5\b\5\2\23\24\5\6\4\2\24" +
                    "\5\3\2\2\2\25\26\7\4\2\2\26\27\5\n\6\2\27\7\3\2\2\2\30\31\t\2\2\2\31\t" +
                    "\3\2\2\2\32\37\5\f\7\2\33\34\7\6\2\2\34\36\5\f\7\2\35\33\3\2\2\2\36!\3" +
                    "\2\2\2\37\35\3\2\2\2\37 \3\2\2\2 \13\3\2\2\2!\37\3\2\2\2\"\'\7\t\2\2#" +
                    "\'\7\n\2\2$\'\7\13\2\2%\'\7\f\2\2&\"\3\2\2\2&#\3\2\2\2&$\3\2\2\2&%\3\2" +
                    "\2\2\'\r\3\2\2\2\4\37&";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
