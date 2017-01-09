package tang.javasist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.Expr;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import javassist.expr.NewExpr;

/**
 * Editor
 * 
 * @author Administrator
 *
 */
public class VerboseEditorTest {
	public static void main(String[] args) {
		try {
			Translator xlat = new DissectionTranslator();
			ClassPool pool = ClassPool.getDefault();
			Loader loader = new Loader(pool);
			loader.addTranslator(pool, xlat);
			String[] args2 = new String[] {};
			loader.run(BeanTest.class.getName(), args2);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static class DissectionTranslator implements Translator {

		public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
			System.out.println("onLoad() " + classname);
			CtClass clas = pool.get(classname);
			clas.instrument(new VerboseEditor());
		}

		public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

		}
	}

	public static class VerboseEditor extends ExprEditor {

		private String from(Expr expr) {
			CtBehavior source = expr.where();

			return " in " + source.getName() + "(" + expr.getFileName() + ":" + expr.getLineNumber() + ")";
		}

		@Override
		public void edit(NewExpr e) throws CannotCompileException {
			System.out.println(" new " + e.getClassName() + from(e));
		}

		@Override
		public void edit(MethodCall m) throws CannotCompileException {
			System.out.println(" call to " + m.getClassName() + "." + m.getMethodName() + from(m));
		}

		@Override
		public void edit(FieldAccess f) throws CannotCompileException {
			String dir = f.isReader() ? "read" : "write";
			System.out.println(" " + dir + " of " + f.getClassName() + "." + f.getFieldName() + from(f));
		}

	}
}
