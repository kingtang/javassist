package tang.javasist;

public class BeanTest {
	private Bean bean;
	public BeanTest()
	{
		bean = new Bean("id","address");
	}
	
	private void print()
	{
		System.out.println("Bean values are " + bean.getAddress() + " and " + bean.getId());
	}
	
	private void changeValues(String lead)
	{
		bean.setAddress(lead + "A");
		bean.setId( lead + "B");
	}
	
	public static void main(String[] args) {
		BeanTest test = new BeanTest();
		test.print();
		test.changeValues("new");
		test.print();
	}
}
