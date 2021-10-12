package antlr;// Generated from C:/Users/Michael/IdeaProjects/Lambda-Calculus/src/main/antlr4\Lambda.g4 by ANTLR 4.9.1
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link LambdaParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface LambdaVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link LambdaParser#input}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInput(LambdaParser.InputContext ctx);
	/**
	 * Visit a parse tree produced by the {@code SingleBound}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSingleBound(LambdaParser.SingleBoundContext ctx);
	/**
	 * Visit a parse tree produced by the {@code MultiBound}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMultiBound(LambdaParser.MultiBoundContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TermOnly}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermOnly(LambdaParser.TermOnlyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TermVariable}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTermVariable(LambdaParser.TermVariableContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Variables}
	 * labeled alternative in {@link LambdaParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitVariables(LambdaParser.VariablesContext ctx);
}