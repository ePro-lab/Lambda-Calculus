package antlr;// Generated from C:/Users/Michael/IdeaProjects/Lambda-Calculus/src/main/antlr4\Lambda.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class LambdaParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		VARIABLE=1, SINGLEBOUND=2, MULTIBOUND=3, LAMBDA=4, LPAREN=5, RPAREN=6, 
		DOT=7, WS=8;
	public static final int
		RULE_input = 0, RULE_term = 1;
	private static String[] makeRuleNames() {
		return new String[] {
			"input", "term"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, null, null, null, null, "'('", "')'", "'.'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "VARIABLE", "SINGLEBOUND", "MULTIBOUND", "LAMBDA", "LPAREN", "RPAREN", 
			"DOT", "WS"
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

	@Override
	public String getGrammarFileName() { return "Lambda.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public LambdaParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class InputContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(LambdaParser.EOF, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> VARIABLE() { return getTokens(LambdaParser.VARIABLE); }
		public TerminalNode VARIABLE(int i) {
			return getToken(LambdaParser.VARIABLE, i);
		}
		public InputContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_input; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).enterInput(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).exitInput(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LambdaVisitor ) return ((LambdaVisitor<? extends T>)visitor).visitInput(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InputContext input() throws RecognitionException {
		InputContext _localctx = new InputContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_input);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(6); 
			_errHandler.sync(this);
			_la = _input.LA(1);
			do {
				{
				setState(6);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case LPAREN:
					{
					setState(4);
					term();
					}
					break;
				case VARIABLE:
					{
					setState(5);
					match(VARIABLE);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(8); 
				_errHandler.sync(this);
				_la = _input.LA(1);
			} while ( _la==VARIABLE || _la==LPAREN );
			setState(10);
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

	public static class TermContext extends ParserRuleContext {
		public TermContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_term; }
	 
		public TermContext() { }
		public void copyFrom(TermContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class SingleBoundContext extends TermContext {
		public TerminalNode LPAREN() { return getToken(LambdaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(LambdaParser.RPAREN, 0); }
		public List<TerminalNode> SINGLEBOUND() { return getTokens(LambdaParser.SINGLEBOUND); }
		public TerminalNode SINGLEBOUND(int i) {
			return getToken(LambdaParser.SINGLEBOUND, i);
		}
		public List<TerminalNode> VARIABLE() { return getTokens(LambdaParser.VARIABLE); }
		public TerminalNode VARIABLE(int i) {
			return getToken(LambdaParser.VARIABLE, i);
		}
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public SingleBoundContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).enterSingleBound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).exitSingleBound(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LambdaVisitor ) return ((LambdaVisitor<? extends T>)visitor).visitSingleBound(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class VariablesContext extends TermContext {
		public TerminalNode LPAREN() { return getToken(LambdaParser.LPAREN, 0); }
		public List<TerminalNode> VARIABLE() { return getTokens(LambdaParser.VARIABLE); }
		public TerminalNode VARIABLE(int i) {
			return getToken(LambdaParser.VARIABLE, i);
		}
		public TerminalNode RPAREN() { return getToken(LambdaParser.RPAREN, 0); }
		public VariablesContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).enterVariables(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).exitVariables(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LambdaVisitor ) return ((LambdaVisitor<? extends T>)visitor).visitVariables(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TermVariableContext extends TermContext {
		public TerminalNode LPAREN() { return getToken(LambdaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(LambdaParser.RPAREN, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public List<TerminalNode> VARIABLE() { return getTokens(LambdaParser.VARIABLE); }
		public TerminalNode VARIABLE(int i) {
			return getToken(LambdaParser.VARIABLE, i);
		}
		public TermVariableContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).enterTermVariable(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).exitTermVariable(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LambdaVisitor ) return ((LambdaVisitor<? extends T>)visitor).visitTermVariable(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class MultiBoundContext extends TermContext {
		public TerminalNode LPAREN() { return getToken(LambdaParser.LPAREN, 0); }
		public TerminalNode MULTIBOUND() { return getToken(LambdaParser.MULTIBOUND, 0); }
		public TerminalNode RPAREN() { return getToken(LambdaParser.RPAREN, 0); }
		public List<TerminalNode> VARIABLE() { return getTokens(LambdaParser.VARIABLE); }
		public TerminalNode VARIABLE(int i) {
			return getToken(LambdaParser.VARIABLE, i);
		}
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public MultiBoundContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).enterMultiBound(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).exitMultiBound(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LambdaVisitor ) return ((LambdaVisitor<? extends T>)visitor).visitMultiBound(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class TermOnlyContext extends TermContext {
		public TerminalNode LPAREN() { return getToken(LambdaParser.LPAREN, 0); }
		public TerminalNode RPAREN() { return getToken(LambdaParser.RPAREN, 0); }
		public List<TermContext> term() {
			return getRuleContexts(TermContext.class);
		}
		public TermContext term(int i) {
			return getRuleContext(TermContext.class,i);
		}
		public TermOnlyContext(TermContext ctx) { copyFrom(ctx); }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).enterTermOnly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof LambdaListener ) ((LambdaListener)listener).exitTermOnly(this);
		}
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof LambdaVisitor ) return ((LambdaVisitor<? extends T>)visitor).visitTermOnly(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TermContext term() throws RecognitionException {
		TermContext _localctx = new TermContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_term);
		int _la;
		try {
			setState(58);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,11,_ctx) ) {
			case 1:
				_localctx = new SingleBoundContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(12);
				match(LPAREN);
				setState(14); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(13);
					match(SINGLEBOUND);
					}
					}
					setState(16); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==SINGLEBOUND );
				setState(20); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					setState(20);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case VARIABLE:
						{
						setState(18);
						match(VARIABLE);
						}
						break;
					case LPAREN:
						{
						setState(19);
						term();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(22); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==VARIABLE || _la==LPAREN );
				setState(24);
				match(RPAREN);
				}
				break;
			case 2:
				_localctx = new MultiBoundContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(25);
				match(LPAREN);
				setState(26);
				match(MULTIBOUND);
				setState(29); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					setState(29);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case VARIABLE:
						{
						setState(27);
						match(VARIABLE);
						}
						break;
					case LPAREN:
						{
						setState(28);
						term();
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(31); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==VARIABLE || _la==LPAREN );
				setState(33);
				match(RPAREN);
				}
				break;
			case 3:
				_localctx = new TermOnlyContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(34);
				match(LPAREN);
				setState(36); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(35);
					term();
					}
					}
					setState(38); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==LPAREN );
				setState(40);
				match(RPAREN);
				}
				break;
			case 4:
				_localctx = new TermVariableContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(42);
				match(LPAREN);
				setState(45); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					setState(45);
					_errHandler.sync(this);
					switch (_input.LA(1)) {
					case LPAREN:
						{
						setState(43);
						term();
						}
						break;
					case VARIABLE:
						{
						setState(44);
						match(VARIABLE);
						}
						break;
					default:
						throw new NoViableAltException(this);
					}
					}
					setState(47); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==VARIABLE || _la==LPAREN );
				setState(49);
				match(RPAREN);
				}
				break;
			case 5:
				_localctx = new VariablesContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(50);
				match(LPAREN);
				setState(51);
				match(VARIABLE);
				setState(53); 
				_errHandler.sync(this);
				_la = _input.LA(1);
				do {
					{
					{
					setState(52);
					match(VARIABLE);
					}
					}
					setState(55); 
					_errHandler.sync(this);
					_la = _input.LA(1);
				} while ( _la==VARIABLE );
				setState(57);
				match(RPAREN);
				}
				break;
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
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\n?\4\2\t\2\4\3\t"+
		"\3\3\2\3\2\6\2\t\n\2\r\2\16\2\n\3\2\3\2\3\3\3\3\6\3\21\n\3\r\3\16\3\22"+
		"\3\3\3\3\6\3\27\n\3\r\3\16\3\30\3\3\3\3\3\3\3\3\3\3\6\3 \n\3\r\3\16\3"+
		"!\3\3\3\3\3\3\6\3\'\n\3\r\3\16\3(\3\3\3\3\3\3\3\3\3\3\6\3\60\n\3\r\3\16"+
		"\3\61\3\3\3\3\3\3\3\3\6\38\n\3\r\3\16\39\3\3\5\3=\n\3\3\3\2\2\4\2\4\2"+
		"\2\2K\2\b\3\2\2\2\4<\3\2\2\2\6\t\5\4\3\2\7\t\7\3\2\2\b\6\3\2\2\2\b\7\3"+
		"\2\2\2\t\n\3\2\2\2\n\b\3\2\2\2\n\13\3\2\2\2\13\f\3\2\2\2\f\r\7\2\2\3\r"+
		"\3\3\2\2\2\16\20\7\7\2\2\17\21\7\4\2\2\20\17\3\2\2\2\21\22\3\2\2\2\22"+
		"\20\3\2\2\2\22\23\3\2\2\2\23\26\3\2\2\2\24\27\7\3\2\2\25\27\5\4\3\2\26"+
		"\24\3\2\2\2\26\25\3\2\2\2\27\30\3\2\2\2\30\26\3\2\2\2\30\31\3\2\2\2\31"+
		"\32\3\2\2\2\32=\7\b\2\2\33\34\7\7\2\2\34\37\7\5\2\2\35 \7\3\2\2\36 \5"+
		"\4\3\2\37\35\3\2\2\2\37\36\3\2\2\2 !\3\2\2\2!\37\3\2\2\2!\"\3\2\2\2\""+
		"#\3\2\2\2#=\7\b\2\2$&\7\7\2\2%\'\5\4\3\2&%\3\2\2\2\'(\3\2\2\2(&\3\2\2"+
		"\2()\3\2\2\2)*\3\2\2\2*+\7\b\2\2+=\3\2\2\2,/\7\7\2\2-\60\5\4\3\2.\60\7"+
		"\3\2\2/-\3\2\2\2/.\3\2\2\2\60\61\3\2\2\2\61/\3\2\2\2\61\62\3\2\2\2\62"+
		"\63\3\2\2\2\63=\7\b\2\2\64\65\7\7\2\2\65\67\7\3\2\2\668\7\3\2\2\67\66"+
		"\3\2\2\289\3\2\2\29\67\3\2\2\29:\3\2\2\2:;\3\2\2\2;=\7\b\2\2<\16\3\2\2"+
		"\2<\33\3\2\2\2<$\3\2\2\2<,\3\2\2\2<\64\3\2\2\2=\5\3\2\2\2\16\b\n\22\26"+
		"\30\37!(/\619<";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}