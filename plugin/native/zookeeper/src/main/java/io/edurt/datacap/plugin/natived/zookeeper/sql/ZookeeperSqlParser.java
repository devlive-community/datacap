package io.edurt.datacap.plugin.natived.zookeeper.sql;
// Generated from ZookeeperSql.g4 by ANTLR 4.9.3

import org.antlr.v4.runtime.NoViableAltException;
import org.antlr.v4.runtime.Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.RuntimeMetaData;
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
            T__0 = 1, SELECT = 2, FROM = 3, STRING = 4, IDENTIFIER = 5, BACKQUOTED_IDENTIFIER = 6,
            SIMPLE_COMMENT = 7, BRACKETED_EMPTY_COMMENT = 8, BRACKETED_COMMENT = 9, WS = 10;
    public static final int
            RULE_singleStatement = 0, RULE_statement = 1, RULE_columnStatement = 2,
            RULE_fromClause = 3, RULE_tableName = 4, RULE_identifier = 5, RULE_quotedIdentifier = 6;

    private static String[] makeRuleNames()
    {
        return new String[] {
                "singleStatement", "statement", "columnStatement", "fromClause", "tableName",
                "identifier", "quotedIdentifier"
        };
    }

    public static final String[] ruleNames = makeRuleNames();

    private static String[] makeLiteralNames()
    {
        return new String[] {
                null, "'.'", null, null, null, null, null, null, "'/**/'"
        };
    }

    private static final String[] _LITERAL_NAMES = makeLiteralNames();

    private static String[] makeSymbolicNames()
    {
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
        public List<StatementContext> statement()
        {
            return getRuleContexts(StatementContext.class);
        }

        public StatementContext statement(int i)
        {
            return getRuleContext(StatementContext.class, i);
        }

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
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(17);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == SELECT) {
                    {
                        {
                            setState(14);
                            statement();
                        }
                    }
                    setState(19);
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

    public static class StatementContext
            extends ParserRuleContext
    {
        public TerminalNode SELECT() {return getToken(ZookeeperSqlParser.SELECT, 0);}

        public ColumnStatementContext columnStatement()
        {
            return getRuleContext(ColumnStatementContext.class, 0);
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
                setState(20);
                match(SELECT);
                setState(21);
                columnStatement();
                setState(22);
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

    public static class ColumnStatementContext
            extends ParserRuleContext
    {
        public IdentifierContext identifier()
        {
            return getRuleContext(IdentifierContext.class, 0);
        }

        public ColumnStatementContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_columnStatement;}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
            if (listener instanceof ZookeeperSqlListener) {
                ((ZookeeperSqlListener) listener).enterColumnStatement(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
            if (listener instanceof ZookeeperSqlListener) {
                ((ZookeeperSqlListener) listener).exitColumnStatement(this);
            }
        }
    }

    public final ColumnStatementContext columnStatement()
            throws RecognitionException
    {
        ColumnStatementContext _localctx = new ColumnStatementContext(_ctx, getState());
        enterRule(_localctx, 4, RULE_columnStatement);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(24);
                identifier();
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
        public TerminalNode FROM() {return getToken(ZookeeperSqlParser.FROM, 0);}

        public TableNameContext tableName()
        {
            return getRuleContext(TableNameContext.class, 0);
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
        enterRule(_localctx, 6, RULE_fromClause);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(26);
                match(FROM);
                setState(27);
                tableName();
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

    public static class TableNameContext
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

        public TableNameContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_tableName;}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
            if (listener instanceof ZookeeperSqlListener) {
                ((ZookeeperSqlListener) listener).enterTableName(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
            if (listener instanceof ZookeeperSqlListener) {
                ((ZookeeperSqlListener) listener).exitTableName(this);
            }
        }
    }

    public final TableNameContext tableName()
            throws RecognitionException
    {
        TableNameContext _localctx = new TableNameContext(_ctx, getState());
        enterRule(_localctx, 8, RULE_tableName);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(29);
                identifier();
                setState(34);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while (_la == T__0) {
                    {
                        {
                            setState(30);
                            match(T__0);
                            setState(31);
                            identifier();
                        }
                    }
                    setState(36);
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
        public List<TerminalNode> IDENTIFIER() {return getTokens(ZookeeperSqlParser.IDENTIFIER);}

        public TerminalNode IDENTIFIER(int i)
        {
            return getToken(ZookeeperSqlParser.IDENTIFIER, i);
        }

        public List<TerminalNode> STRING() {return getTokens(ZookeeperSqlParser.STRING);}

        public TerminalNode STRING(int i)
        {
            return getToken(ZookeeperSqlParser.STRING, i);
        }

        public List<QuotedIdentifierContext> quotedIdentifier()
        {
            return getRuleContexts(QuotedIdentifierContext.class);
        }

        public QuotedIdentifierContext quotedIdentifier(int i)
        {
            return getRuleContext(QuotedIdentifierContext.class, i);
        }

        public IdentifierContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_identifier;}

        @Override
        public void enterRule(ParseTreeListener listener)
        {
            if (listener instanceof ZookeeperSqlListener) {
                ((ZookeeperSqlListener) listener).enterIdentifier(this);
            }
        }

        @Override
        public void exitRule(ParseTreeListener listener)
        {
            if (listener instanceof ZookeeperSqlListener) {
                ((ZookeeperSqlListener) listener).exitIdentifier(this);
            }
        }
    }

    public final IdentifierContext identifier()
            throws RecognitionException
    {
        IdentifierContext _localctx = new IdentifierContext(_ctx, getState());
        enterRule(_localctx, 10, RULE_identifier);
        int _la;
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(42);
                _errHandler.sync(this);
                _la = _input.LA(1);
                while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << STRING) | (1L << IDENTIFIER) | (1L << BACKQUOTED_IDENTIFIER))) != 0)) {
                    {
                        setState(40);
                        _errHandler.sync(this);
                        switch (_input.LA(1)) {
                            case IDENTIFIER: {
                                setState(37);
                                match(IDENTIFIER);
                            }
                            break;
                            case STRING: {
                                setState(38);
                                match(STRING);
                            }
                            break;
                            case BACKQUOTED_IDENTIFIER: {
                                setState(39);
                                quotedIdentifier();
                            }
                            break;
                            default:
                                throw new NoViableAltException(this);
                        }
                    }
                    setState(44);
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

    public static class QuotedIdentifierContext
            extends ParserRuleContext
    {
        public TerminalNode BACKQUOTED_IDENTIFIER() {return getToken(ZookeeperSqlParser.BACKQUOTED_IDENTIFIER, 0);}

        public QuotedIdentifierContext(ParserRuleContext parent, int invokingState)
        {
            super(parent, invokingState);
        }

        @Override
        public int getRuleIndex() {return RULE_quotedIdentifier;}

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

    public final QuotedIdentifierContext quotedIdentifier()
            throws RecognitionException
    {
        QuotedIdentifierContext _localctx = new QuotedIdentifierContext(_ctx, getState());
        enterRule(_localctx, 12, RULE_quotedIdentifier);
        try {
            enterOuterAlt(_localctx, 1);
            {
                setState(45);
                match(BACKQUOTED_IDENTIFIER);
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
            "\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\f\62\4\2\t\2\4\3" +
                    "\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\3\2\7\2\22\n\2\f\2\16\2\25" +
                    "\13\2\3\3\3\3\3\3\3\3\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\7\6#\n\6\f\6\16" +
                    "\6&\13\6\3\7\3\7\3\7\7\7+\n\7\f\7\16\7.\13\7\3\b\3\b\3\b\2\2\t\2\4\6\b" +
                    "\n\f\16\2\2\2/\2\23\3\2\2\2\4\26\3\2\2\2\6\32\3\2\2\2\b\34\3\2\2\2\n\37" +
                    "\3\2\2\2\f,\3\2\2\2\16/\3\2\2\2\20\22\5\4\3\2\21\20\3\2\2\2\22\25\3\2" +
                    "\2\2\23\21\3\2\2\2\23\24\3\2\2\2\24\3\3\2\2\2\25\23\3\2\2\2\26\27\7\4" +
                    "\2\2\27\30\5\6\4\2\30\31\5\b\5\2\31\5\3\2\2\2\32\33\5\f\7\2\33\7\3\2\2" +
                    "\2\34\35\7\5\2\2\35\36\5\n\6\2\36\t\3\2\2\2\37$\5\f\7\2 !\7\3\2\2!#\5" +
                    "\f\7\2\" \3\2\2\2#&\3\2\2\2$\"\3\2\2\2$%\3\2\2\2%\13\3\2\2\2&$\3\2\2\2" +
                    "\'+\7\7\2\2(+\7\6\2\2)+\5\16\b\2*\'\3\2\2\2*(\3\2\2\2*)\3\2\2\2+.\3\2" +
                    "\2\2,*\3\2\2\2,-\3\2\2\2-\r\3\2\2\2.,\3\2\2\2/\60\7\b\2\2\60\17\3\2\2" +
                    "\2\6\23$*,";
    public static final ATN _ATN =
            new ATNDeserializer().deserialize(_serializedATN.toCharArray());

    static {
        _decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
        for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
            _decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
        }
    }
}
