package wisematches.playground.task.assured;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.playground.task.AssuredTaskExecutor;
import wisematches.playground.task.AssuredTaskProcessor;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileAssuredTaskExecutor<T extends Serializable, C extends Serializable> implements AssuredTaskExecutor<T, C> {
	private Executor executor;
	private FileChannel assuredStorage;

	private final Lock lock = new ReentrantLock();
	private final List<AssuredTask> tasksQueue = new LinkedList<AssuredTask>();
	private final Map<String, AssuredTaskProcessor<T, C>> processorMap = new HashMap<String, AssuredTaskProcessor<T, C>>();

	private static final Log log = LogFactory.getLog("wisematches.server.core.task");

	public FileAssuredTaskExecutor() {
	}

	@Override
	public void registerProcessor(String name, AssuredTaskProcessor<T, C> processor) {
		lock.lock();
		try {
			final AssuredTaskProcessor<T, C> v = processorMap.get(name);
			if (v != null) {
				throw new IllegalArgumentException("Processor with that name already registered: " + name + " [" + v + "]");
			}
			processorMap.put(name, processor);
			replayTasks(name);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void unregisterProcessor(String name, AssuredTaskProcessor<T, C> processor) {
		lock.lock();
		try {
			processorMap.remove(name);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void execute(String processor, T taskId, C taskContext) {
		final AssuredTask e;
		lock.lock();
		try {
			e = new AssuredTask(processor, taskId, taskContext);
			e.setAssuredTaskExecutor(this);
			saveTask(e);
		} finally {
			lock.unlock();
		}
		executor.execute(e);
	}

	@SuppressWarnings("unchecked")
	protected void processTask(AssuredTask task) {
		try {
			final AssuredTaskProcessor<T, C> taskProcessor;
			lock.lock();
			try {
				taskProcessor = processorMap.get(task.processor);
			} finally {
				lock.unlock();
			}
			if (taskProcessor != null) {
				taskProcessor.processAssuredTask((T) task.taskId, (C) task.taskContext);
			}
			lock.lock();
			try {
				removeTask(task);
			} finally {
				lock.unlock();
			}
		} catch (Throwable th) {
			log.error("Task can't be processed: " + task, th);
		}
	}

	private void saveTask(AssuredTask task) {
		try {
			if (tasksQueue.add(task)) {
				assuredStorage.position(0);
				final ObjectOutputStream outputStream = new ObjectOutputStream(Channels.newOutputStream(this.assuredStorage));
				outputStream.writeInt(tasksQueue.size());
				for (AssuredTask assuredTask : tasksQueue) {
					outputStream.writeObject(assuredTask);
				}
				outputStream.flush();
			}
		} catch (IOException ex) {
			log.error("Assured tasks can't be stored", ex);
		}
	}

	private void removeTask(AssuredTask task) {
		try {
			if (tasksQueue.remove(task)) {
				assuredStorage.position(0);
				final ObjectOutputStream outputStream = new ObjectOutputStream(Channels.newOutputStream(this.assuredStorage));
				outputStream.writeInt(tasksQueue.size());
				for (AssuredTask assuredTask : tasksQueue) {
					outputStream.writeObject(assuredTask);
				}
				outputStream.flush();
			}
		} catch (IOException ex) {
			log.error("Assured tasks can't be removed", ex);
		}
	}

	@SuppressWarnings("unchecked")
	private void replayTasks(String name) {
		try {
			if (assuredStorage.isOpen() && assuredStorage.size() != 0) {
				assuredStorage.position(0);
				final ObjectInputStream inputStream = new ObjectInputStream(Channels.newInputStream(assuredStorage));
				int count = inputStream.readInt();
				while (count-- != 0) {
					AssuredTask task = (AssuredTask) inputStream.readObject();
					if (task.processor.equals(name)) {
						execute(task.processor, (T) task.taskId, (C) task.taskContext);
					}
				}
			}
		} catch (EOFException ex) {
			log.debug("EOF");
		} catch (IOException ex) {
			log.error("File proposal can't be loaded", ex);
		} catch (ClassNotFoundException ex) {
			log.error("File proposal can't be loaded", ex);
		}
	}


	public void setExecutor(Executor executor) {
		this.executor = executor;
	}

	public void setFileStorage(File fileStorage) throws IOException {
		if (!fileStorage.exists() && !fileStorage.createNewFile()) {
			throw new IllegalArgumentException("Storage file can't be created: " + fileStorage);
		}
		this.assuredStorage = new RandomAccessFile(fileStorage, "rw").getChannel();
	}

	private static final class AssuredTask implements Runnable, Serializable {
		private final String processor;
		private final Serializable taskId;
		private final Serializable taskContext;
		private transient FileAssuredTaskExecutor assuredTaskExecutor;

		private AssuredTask(String processor, Serializable taskId, Serializable taskContext) {
			this.processor = processor;
			this.taskId = taskId;
			this.taskContext = taskContext;
		}

		@Override
		public void run() {
			assuredTaskExecutor.processTask(this);
		}

		public void setAssuredTaskExecutor(FileAssuredTaskExecutor assuredTaskExecutor) {
			this.assuredTaskExecutor = assuredTaskExecutor;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("AssuredTask");
			sb.append("{processor='").append(processor).append('\'');
			sb.append(", taskId=").append(taskId);
			sb.append(", taskContext=").append(taskContext);
			sb.append('}');
			return sb.toString();
		}
	}
}
