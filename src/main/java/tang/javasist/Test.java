package tang.javasist;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

public class Test {
	public static void main(String[] args) {
		//writeFile();
	}

	public static void writeFile() {
		ClassPool pool = ClassPool.getDefault();
		try {
			CtClass ctClass = pool.get("tang.javasist.Bean");
			ctClass.setSuperclass(pool.get("tang.javasist.BeanTest"));
			//会持久化到磁盘
			ctClass.writeFile();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
