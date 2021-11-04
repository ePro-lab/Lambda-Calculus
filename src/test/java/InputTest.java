import antlr.LambdaLexer;
import antlr.LambdaParser;
import converter.AntlrToInput;
import model.Input;
import operation.BetaReduction;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class InputTest {
    private final boolean showCalculation = true;

    @Before
    public void clear(){
        BetaReduction.resetCount();
    }

    @Test
    public void test001(){
        String inputString = "(Lx.Lx.Lu.xxuzu)y(Lp.p)f";
        test(inputString,"fzf\r");
    }

    @Test
    public void test002(){
        String inputString = "(Lxxu.xxuzu)y(Lp.p)f";
        test(inputString,"fzf\r");
    }

    @Test
    public void test003(){
        String inputString = "(Lx.Ly.Lz.Lu.xxyzux)yx";
        test(inputString,"(λz.λu.yyxzuy)\r");
    }

    @Test
    public void test004(){
        String inputString = "(Lxyzu.xxyzux)yx";
        test(inputString,"(λzu.yyxzuy)\r");
    }

    @Test
    public void test005(){
        String inputString = "(Lx.x)x";
        test(inputString,"x\r");
    }

    @Test
    public void test006(){
        String inputString = "(La.Lb.a(Lb.Lc.(La.a))ab)cde";
        test(inputString,"(c(λb.λc.(λa.a))cd)e\r");
    }

    @Test
    public void test007(){
        String inputString = "(Lab.a(Lbc.(La.a))ab)cde";
        test(inputString,"(c(λbc.(λa.a))cd)e\r");
    }

    @Test
    public void test008(){
        String inputString = "(Lxy.((Lyz.y)(Lpx.x)))x";
        test(inputString,"(λy.(λz.(λpx.x)))\r");
    }

    @Test
    public void test009(){
        String inputString = "(Lx.(Lx.x)x)ab";
        test(inputString,"ab\r");
    }

    @Test
    public void test010(){
        String inputString = "(Lx.Lz.yx)y";
        test(inputString,"(λz.yy)\r");
    }

    @Test
    public void test011(){
        String inputString = "(Lxz.yx)y";
        test(inputString,"(λz.yy)\r");
    }

    @Test
    public void test012(){
        String inputString = "(Lx.Ly.x)ab(Lx.Ly.xy)ab";
        test(inputString,"a(λx.λy.xy)ab\r");
    }

    @Test
    public void test013(){
        String inputString = "(Lxy.x)ab(Lxy.xy)ab";
        test(inputString,"a(λxy.xy)ab\r");
    }

    @Test
    public void test014(){
        String inputString = "(Lx.Ly.xy)y";
        test(inputString,"(λa.ya)\r");
    }

    @Test
    public void test015(){
        String inputString = "(Lxy.xy)y";
        test(inputString,"(λa.ya)\r");
    }

    @Test
    public void test016(){
        String inputString = "(Lx.Ly.x y )(Lx.zx)  ( (Lx.xx) ( (Ly.Lx.xy)z((Ly.Lx.yx)z) ) )";           //klammer zu viel
        test(inputString,"z(zz)(zz)\r");
    }

    @Test
    public void test017(){
        String inputString = "(Lxy.x y )(Lx.zx)  ( (Lx.xx) ( (Lyx.xy)z((Lyx.yx)z) ) )";
        test(inputString,"z(zz)(zz)\r");
    }

    @Test
    public void test018(){
        String inputString = " (Lx.xx) ( (Ly.Lx.xy) z ((Ly.Lx.yx)z) ) ";
        test(inputString,"(zz)(zz)\r");
    }

    @Test
    public void test019(){
        String inputString = " (Lx.xx) ( (Lyx.xy) z ((Lyx.yx)z) ) ";
        test(inputString,"(zz)(zz)\r");
    }

    @Test
    public void test020(){
        String inputString = "((Lx. Ly.	Lw.		y	x		w		) u (Lv.	v v) z)x";
        test(inputString,"((uu)z)x\r");
    }

    @Test
    public void test021(){
        String inputString = "((Lxyw.		y	x		w		) u (Lv.	v v) z)x";
        test(inputString,"((uu)z)x\r");
    }

    //ignored since term structure is added in 1.2 and it's endless loop, which result depends on the number of circles
    @Deprecated
    @Test
    @Ignore
    public void test022(){
        String inputString = "(Ly.(Lx.Ly.xx)(Lx.Ly.xx))";
        test(inputString,"(λy.(λx.λy.xx)(λx.λy.xx))\r");
    }

    //ignored since term structure is added in 1.2 and it's endless loop, which result depends on the number of circles
    @Deprecated
    @Test
    @Ignore
    public void test023(){
        String inputString = "(Ly.(Lxy.xx)(Lxy.xx))";
        test(inputString,"(λy.(λxy.xx)(λxy.xx))\r");
    }

    @Test
    public void test024(){
        String inputString = "((Lx.x)z(Lx.x))";
        test(inputString,"(z(λx.x))\r");
    }

    @Test
    public void test025(){
        String inputString = "(Lx.Lz.Lu.xxuzu)x(Lp.p)f";
        test(inputString,"xxf(λp.p)f\r");
    }

    @Test
    public void test026(){
        String inputString = "(Lxzu.xxuzu)x(Lp.p)f";
        test(inputString,"xxf(λp.p)f\r");
    }

    @Test
    public void test027(){
        String inputString = "(Lx.Lz.Lu.xxuzu)x(Lp.p)fzftukcgjt";
        test(inputString,"(xxf(λp.p)f)zftukcgjt\r");
    }

    @Test
    public void test028(){
        String inputString = "(Lxzu.xxuzu)x(Lp.p)fzftukcgjt";
        test(inputString,"(xxf(λp.p)f)zftukcgjt\r");
    }

    @Test
    public void test029(){
        String inputString = "(La.Lb.abc)ab";
        test(inputString,"abc\r");
    }

    @Test
    public void test030(){
        String inputString = "(Lab.abc)ab";
        test(inputString,"abc\r");
    }

    @Test
    public void test031(){
        String inputString = "(Lx.Ly.xyz)yx";
        test(inputString,"yxz\r");
    }

    @Test
    public void test032(){
        String inputString = "(Lxy.xyz)yx";
        test(inputString,"yxz\r");
    }

    @Test
    public void test033(){
        String inputString = "((Lx.x)z)";
        test(inputString,"z\r");
    }

    @Test
    public void test034(){
        String inputString = "(La.Lb.a(a(La.Lc.a))ab)cde";
        test(inputString,"(c(c(λa.λc.a))cd)e\r");
    }

    @Test
    public void test035(){
        String inputString = "(Lab.a(a(Lac.a))ab)cde";
        test(inputString,"(c(c(λac.a))cd)e\r");
    }

    @Test
    public void test036(){
        String inputString = "((zz)(zz))";
        test(inputString,"(zz)(zz)\r");
    }

    //example for NoFreeVariableException
    @Test
    public void test037(){
        String inputString = "(Labcdefghijklmnopqrstuvwxyz.xx)x";
        test(inputString,"(λabcdefghijklmnopqrstuvwxyz.xx)x\r");
    }

    @Test
    public void test038(){
        String inputString = "(Lxyzu.(Ly.xy)xxyzux)yx";
        test(inputString,"(λzu.(yy)yxzuy)\r");
    }

    @Test
    public void test039(){
        String inputString = "(Lx.Ly.Lz.Lu.(Ly.xy)xxyzux)yx";
        test(inputString,"(λz.λu.(yy)yxzuy)\r");
    }

    @Test
    public void test040(){
        String inputString = "(Lxyzu.(Lp.xp)xxyzux)yx";
        test(inputString,"(λzu.(yy)yxzuy)\r");
    }
    @Test
    public void test041(){
        String inputString = "(Lx.Ly.Lz.Lu.(Lp.xp)xxyzux)yx";
        test(inputString,"(λz.λu.(yy)yxzuy)\r");
    }

    @Test
    public void test042(){
        String inputString = "(Lxyzu.(Lyx.xy)xxyzux)yx";
        test(inputString,"(λzu.(yy)xzuy)\r");
    }

    @Test
    public void test043(){
        String inputString = "(Lx.Ly.Lz.Lu.(Lyx.xy)xxyzux)yx";
        test(inputString,"(λz.λu.(yy)xzuy)\r");
    }

    @Test
    public void test044(){
        String inputString = "(Lx.xx)(Lx.xx)";
        test(inputString,"(λx.xx)(λx.xx)\r");
    }

    public void test(String inputString, String expected){
        PrintStream stdout = System.out;

        ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
        System.setOut(new java.io.PrintStream(out));

        runTest(inputString);

        String output = out.toString();
        String[] list = output.split("\n");
        String result = list[list.length-1];
        System.err.println(inputString + " - " + result);
        if(showCalculation) {
            System.setOut(stdout);
            System.out.println(output);
        }
        Assert.assertEquals(expected, result);
    }

    public void runTest(String inputString){
        try{
            LambdaParser lambdaParser = getParser(inputString);

            //build ParseTree from start symbol 'input'
            ParseTree antlrAST = lambdaParser.input();
            //create visitor for converting the ParseTree into Input/Term object
            AntlrToInput inputVisitor = new AntlrToInput();
            Input input = inputVisitor.visit(antlrAST);

            System.out.println("output:");
            System.out.println(input.toString());

            BetaReduction.betaReduction(input, null,"","");

        } catch (RecognitionException e) {
            e.printStackTrace();
        }
    }

    private static LambdaParser getParser(String inputString){
        LambdaParser parser;

        CharStream input = CharStreams.fromString(inputString);
        LambdaLexer lexer = new LambdaLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        parser = new LambdaParser(tokens);

        return parser;
    }
}
