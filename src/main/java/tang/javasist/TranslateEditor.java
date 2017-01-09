package tang.javasist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;

/**
 * 使用ExprEditor完成面向切面的工作，实现的功能为
 * 修改BeanTest类的setId方法，将传入的值反转然后赋值给id字段。
 * @author Administrator
 *
 */
public class TranslateEditor {
	public static void main(String[] args) {
		try {
			EditorTranslator xlat = new EditorTranslator();
			//绑定自定义editor
			xlat.setEditor(new FieldSetEditor("id"));
			//设置方法的宿主类
			xlat.setCname(Bean.class.getName());

			//获取对象池
			ClassPool pool = ClassPool.getDefault();
			Loader loader = new Loader(pool);
			//将对象池和translator绑定
			loader.addTranslator(pool, xlat);
			
			String[] args2 = new String[]{};
			//运行BeanTest类的main方法
			loader.run(BeanTest.class.getName(),args2);
			
		} catch (Exception e) {
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public static String reverse(String value) {
		int lenth = value.length();
		StringBuffer buff = new StringBuffer(lenth);
		for (int i = lenth - 1; i >= 0; i--) {
			buff.append(value.charAt(i));
		}
		System.out.println("TranslateEditor.reverse returning " + buff);
		return buff.toString();
	}

	public static class EditorTranslator implements Translator {
		private String cname;
		private ExprEditor editor;

		public void start(ClassPool pool) throws NotFoundException, CannotCompileException {

		}

		public void onLoad(ClassPool pool, String classname) throws NotFoundException, CannotCompileException {
			if (cname.equals(classname)) {
				CtClass clas = pool.get(classname);
				clas.instrument(editor);
			}
		}

		public String getCname() {
			return cname;
		}

		public void setCname(String cname) {
			this.cname = cname;
		}

		public ExprEditor getEditor() {
			return editor;
		}

		public void setEditor(ExprEditor editor) {
			this.editor = editor;
		}
	}

	/**
	 * 自定义editor
	 * @author Administrator
	 *
	 */
	public static class FieldSetEditor extends ExprEditor {
		private String fieldName;

		private FieldSetEditor(String fname) {
			this.fieldName = fname;
		}

		@Override
		public void edit(FieldAccess f) throws CannotCompileException {
			//判断是否是setId方法
			if (f.getFieldName().equals(fieldName) && f.isWriter()) {
				System.out.println(f.getFieldName());
				System.out.println(f.getLineNumber());
				System.out.println(f.getSignature());
				System.out.println(f.getEnclosingClass());
				StringBuffer code = new StringBuffer();
				code.append("$0.");
				code.append(f.getFieldName());
				code.append("=" + TranslateEditor.class.getName()+".reverse($1);");
				System.out.println(code.toString());
				f.replace(code.toString());
			}

		}

		@Override
		public void edit(MethodCall m) throws CannotCompileException {
			// TODO Auto-generated method stub
			super.edit(m);
		}

	}
}
