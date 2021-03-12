package antlr;// Generated from C:/Users/Michael/IdeaProjects/Lambda-Calculus/src/main/antlr4\Lambda.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeListener;

/**
 * This interface defines a complete listener for a parse tree produced by
 * {@link LambdaParser}.
 */
public interface LambdaListener extends ParseTreeListener {
	/**
	 * Enter a parse tree produced by {@link LambdaParser#input}.
	 * @param ctx the parse tree
	 */
	void enterInput(LambdaParser.InputContext ctx);
	/**
	 * Exit a parse tree produced by {@link LambdaParser#input}.
	 * @param ctx the parse tree
	 */
	void exitInput(LambdaParser.InputContext ctx);
	/**
	 * Enter a parse tree produced by the {@code SingleBound}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void enterSingleBound(LambdaParser.SingleBoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code SingleBound}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void exitSingleBound(LambdaParser.SingleBoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code MultiBound}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void enterMultiBound(LambdaParser.MultiBoundContext ctx);
	/**
	 * Exit a parse tree produced by the {@code MultiBound}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void exitMultiBound(LambdaParser.MultiBoundContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TermOnly}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTermOnly(LambdaParser.TermOnlyContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TermOnly}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTermOnly(LambdaParser.TermOnlyContext ctx);
	/**
	 * Enter a parse tree produced by the {@code TermVariable}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void enterTermVariable(LambdaParser.TermVariableContext ctx);
	/**
	 * Exit a parse tree produced by the {@code TermVariable}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void exitTermVariable(LambdaParser.TermVariableContext ctx);
	/**
	 * Enter a parse tree produced by the {@code Variables}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void enterVariables(LambdaParser.VariablesContext ctx);
	/**
	 * Exit a parse tree produced by the {@code Variables}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 */
	void exitVariables(LambdaParser.VariablesContext ctx);
}