package wisematches.playground.task.assured;

import org.easymock.IAnswer;
import org.junit.Test;
import wisematches.playground.task.AssuredTaskProcessor;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.easymock.EasyMock.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@SuppressWarnings("unchecked")
public class FileAssuredTaskExecutorTest {
	private final Lock lock = new ReentrantLock();
	private final Condition condition = lock.newCondition();

	public FileAssuredTaskExecutorTest() {
	}

	@Test
	public void test() throws IOException, InterruptedException {
		final File wm = File.createTempFile("wisematches", "FileAssuredTaskExecutorTest");
		try {
			final ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 1000000, TimeUnit.DAYS, new ArrayBlockingQueue<Runnable>(100));

			FileAssuredTaskExecutor taskExecutor = new FileAssuredTaskExecutor();
			taskExecutor.setExecutor(executor);
			taskExecutor.setFileStorage(wm);

			final AssuredTaskProcessor tp1 = createStrictMock(AssuredTaskProcessor.class);
			tp1.processAssuredTask("mock1", "c1");
			expectLastCall().andAnswer(new IAnswer<Object>() {
				@Override
				public Object answer() throws Throwable {
					lock.lock();
					try {
						condition.await();
					} catch (InterruptedException ex) {
						throw new IllegalStateException("ex");
					}
					lock.unlock();
					return null;
				}
			});
			tp1.processAssuredTask("mock1", "c1");
			replay(tp1);

			final AssuredTaskProcessor tp2 = createStrictMock(AssuredTaskProcessor.class);
			tp2.processAssuredTask("mock1", "c1");
			expectLastCall().andAnswer(new IAnswer<Object>() {
				@Override
				public Object answer() throws Throwable {
					lock.lock();
					try {
						condition.await();
					} catch (InterruptedException ex) {
						throw new IllegalStateException("ex");
					}
					lock.unlock();
					return null;
				}
			});
			tp2.processAssuredTask("mock1", "c1");
			replay(tp2);

			taskExecutor.registerProcessor("mock1", tp1);
			taskExecutor.registerProcessor("mock2", tp2);

			taskExecutor.execute("mock1", "mock1", "c1");
			taskExecutor.execute("mock2", "mock1", "c1");

			Thread.sleep(200);

			executor.shutdown();

			taskExecutor.unregisterProcessor("mock1", tp1);
			taskExecutor.unregisterProcessor("mock2", tp2);

			final ThreadPoolExecutor executor2 = new ThreadPoolExecutor(2, 2, 1000000, TimeUnit.DAYS, new ArrayBlockingQueue<Runnable>(100));
			final FileAssuredTaskExecutor taskExecutor2 = new FileAssuredTaskExecutor();
			taskExecutor2.setExecutor(executor2);
			taskExecutor2.setFileStorage(wm);

			taskExecutor2.registerProcessor("mock1", tp1);
			taskExecutor2.registerProcessor("mock2", tp2);

			Thread.sleep(200);
			verify(tp1, tp2);
		} finally {
			wm.delete();
		}
	}
}
