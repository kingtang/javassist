package tang.javasist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CodeConverter;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Loader;
import javassist.NotFoundException;
import javassist.Translator;

/**
 * javassist实现简单的AOP
 * 实现的功能：在方法setId之前插入切面逻辑，reportSet
 * 但是该实现有缺陷：
 * 其一切面逻辑方法必须为static
 * 其二切面逻辑方法接收的参数需要为被代理类和被代理方法同样数量和类型的参数列表
 */
public class CodeConverterTest 
{
    public static void main( String[] args )
    {
    	//自定义translator，当类被加载时其中的onLoad方法将会执行
        ConverterTranslator xlat = new ConverterTranslator();
        //获取class池
        ClassPool pool = ClassPool.getDefault();
        CodeConverter convert = new CodeConverter();
        try {
        	//需要实现AOP的方法
			CtMethod smeth = pool.get(Bean.class.getName()).getDeclaredMethod("setId");
			//被插入的逻辑
			CtMethod pmeth = pool.get(CodeConverterTest.class.getName()).getDeclaredMethod("reportSet");
			//织入方法前
			convert.insertBeforeMethod(smeth, pmeth);
			//为Translator指定converter
			xlat.setConverter(convert);
			Loader loader = new Loader(pool);
			loader.addTranslator(pool, xlat);
			String[] args2 = new String[]{};
			loader.run(BeanTest.class.getName(),args2);
		} catch (NotFoundException e) {
			e.printStackTrace();
		} catch (CannotCompileException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void reportSet(Bean target,String value)
    {
    	System.out.println("Call to set value " + value);
    }
    
    public static class ConverterTranslator implements Translator
    {
    	private CodeConverter converter;
    	
    	public void setConverter(CodeConverter converter)
    	{
    		this.converter = converter;
    	}
    	
		public void onLoad(ClassPool pool, String cName) throws NotFoundException, CannotCompileException {
			try {
				CtClass clas = pool.get(cName);
				clas.instrument(converter);
			} catch (NotFoundException e) {
				e.printStackTrace();
			} catch (CannotCompileException e) {
				e.printStackTrace();
			}
		}

		public void start(ClassPool arg0) throws NotFoundException, CannotCompileException {
		}
    	
    }
}
