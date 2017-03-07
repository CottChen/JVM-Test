import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import sun.misc.Unsafe;

import com.namibank.df.common.zkclient.NamiZKEvent;
import com.namibank.df.common.zkclient.NamiZKListener;
import com.namibank.df.common.zkclient.NamiZkClient;

public class JVMTest {
	private static final int _1MB = 1024 * 1024 * 1024;

	public static void main(String[] args) throws IllegalArgumentException,
			IllegalAccessException {
		test003();
	}

	public static void test001() {
		JVMTest r = new JVMTest();
		TestZkClient testNamiZkClient = r.new TestZkClient();
		testNamiZkClient.test001();
	}

	public static void test002() {
		List list = new ArrayList();
		int i = 0;
		while (true) {
			System.out.println("add a num " + i);
			list.add(String.valueOf(i++).intern());
		}
	}

	public static void test003() throws IllegalArgumentException,
			IllegalAccessException {
		Field unsafeField = Unsafe.class.getDeclaredFields()[0];
		unsafeField.setAccessible(true);
		Unsafe unsafe = (Unsafe) unsafeField.get(null);
		while (true) {
			System.out.println("allocate memory...");
			// unsafe直接想操作系统申请内存
			unsafe.allocateMemory(_1MB);
		}
	}

	public class TestZkClient {
		private NamiZkClient testNamiZkClient = new NamiZkClient();

		public TestZkClient() {
			testNamiZkClient.setZkAddress("zk:2181");
			testNamiZkClient.initClient();
		}

		public NamiZkClient getNamiZkClient() {
			return this.testNamiZkClient;
		}

		public void test001() {
			int i = 0;
			while (true) {
				i++;
				System.out.println("create listener " + i);
				testNamiZkClient.watchNodeChange("/nami/risk",
						new NamiZKListener() {
							@Override
							public void invoke(NamiZKEvent event) {
								System.out.println("get change");
								testNamiZkClient.removeWatch(this);
							}
						});
			}
		}
	}
}
