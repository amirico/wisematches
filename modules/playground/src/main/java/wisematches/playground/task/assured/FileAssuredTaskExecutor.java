package wisematches.playground.task.assured;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import wisematches.playground.task.AssuredTaskExecutor;
import wisematches.playground.task.AssuredTaskProcessor;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.*;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileAssuredTaskExecutor<T extends Serializable, C extends Serializable> implements AssuredTaskExecutor<T, C>, InitializingBean, DisposableBean {
	private Executor executor;
	private FileChannel assuredStorage;

	private final Lock lock = new ReentrantLock();

	private final List<AssuredTask> tasksQueue = new LinkedList<AssuredTask>();
	private final Map<String, AssuredTaskProcessor<T, C>> processorMap = new HashMap<String, AssuredTaskProcessor<T, C>>();

	private static final Log log = LogFactory.getLog("wisematches.server.core.task");

	public FileAssuredTaskExecutor() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		lock.lock();
		try {
			tasksQueue.clear();
			tasksQueue.addAll(loadTasks());

			log.debug("Load tasks from disk: " + tasksQueue.size());
			for (String s : processorMap.keySet()) {
				activateTasks(s);
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void destroy() throws Exception {
		lock.lock();
		try {
			log.debug("Destroy tasks: " + tasksQueue.size());

			for (String s : processorMap.keySet()) {
				deactivateTasks(s);
			}
			persistTasks();
			tasksQueue.clear();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void registerProcessor(String name, AssuredTaskProcessor<T, C> processor) {
		lock.lock();
		try {
			final AssuredTaskProcessor<T, C> v = processorMap.get(name);
			if (v != null) {
				throw new IllegalArgumentException("Processor with that name already registered: " + name + " [" + v + "]");
			}
			log.debug("Register processor: " + name + "[" + processor + "]");
			processorMap.put(name, processor);
			activateTasks(name);
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void unregisterProcessor(String name, AssuredTaskProcessor<T, C> processor) {
		lock.lock();
		try {
			log.debug("Unregister processor: " + name + "[" + processor + "]");
			deactivateTasks(name);
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
			log.debug("Execute task: " + task);

			final AssuredTaskProcessor<T, C> taskProcessor;
			lock.lock();
			try {
				if (task.isCancelled()) {
					log.debug("Task " + task + " is cancelled.");
					return;
				}
				taskProcessor = processorMap.get(task.processor);
			} finally {
				lock.unlock();
			}

			if (taskProcessor != null) {
				log.debug("Process task in processor: " + task.processor + "[" + taskProcessor + "]");
				taskProcessor.processAssuredTask((T) task.taskId, (C) task.taskContext);

				lock.lock();
				try {
					removeTask(task);
				} finally {
					lock.unlock();
				}
			}
		} catch (Throwable th) {
			log.debug("Task can't be processed: " + task + "[" + th.getMessage() + "]");
		}
	}

	private void saveTask(AssuredTask task) {
		try {
			log.debug("Add task to queue: " + task);
			if (tasksQueue.add(task)) {
				assuredStorage.position(0);
				persistTasks();
			}
		} catch (IOException ex) {
			log.error("Assured tasks can't be stored", ex);
		}
	}

	private void removeTask(AssuredTask task) {
		try {
			log.debug("Remove task from queue: " + task);
			if (tasksQueue.remove(task)) {
				assuredStorage.position(0);
				persistTasks();
			}
		} catch (IOException ex) {
			log.error("Assured tasks can't be removed", ex);
		}
	}

	@SuppressWarnings("unchecked")
	private void activateTasks(String name) {
		log.debug("Activate tasks for processor: " + name);
		for (AssuredTask assuredTask : tasksQueue) {
			if (assuredTask.processor.equals(name)) {
				assuredTask.cancel(false);
				executor.execute(assuredTask);
			}
		}
	}

	private void deactivateTasks(String name) {
		log.debug("Deactivate tasks for processor: " + name);
		for (AssuredTask assuredTask : tasksQueue) {
			if (assuredTask.processor.equals(name)) {
				assuredTask.cancel(true);
			}
		}
	}

	private void persistTasks() {
		try {
			log.debug("Persist tasks: " + tasksQueue.size());
			final ObjectOutputStream outputStream = new ObjectOutputStream(Channels.newOutputStream(this.assuredStorage));
			outputStream.writeObject(tasksQueue);
			outputStream.flush();
		} catch (IOException ex) {
			log.error("Tasks can't be stored to disk", ex);
		}
	}

	@SuppressWarnings("unchecked")
	private List<AssuredTask> loadTasks() throws IOException {
		final List<AssuredTask> res = new ArrayList<AssuredTask>();
		try {
			final ObjectInputStream inputStream = new ObjectInputStream(Channels.newInputStream(assuredStorage));
			final Collection<AssuredTask> c = (Collection<AssuredTask>) inputStream.readObject();
			if (c != null) {
				for (AssuredTask assuredTask : c) {
					assuredTask.setAssuredTaskExecutor(this);
					res.add(assuredTask);
				}
			}
		} catch (EOFException ex) {
			log.debug("EOF");
		} catch (IOException ex) {
			log.error("File proposal can't be loaded", ex);
			throw ex;
		} catch (ClassNotFoundException ex) {
			log.error("File proposal can't be loaded", ex);
		}
		return res;
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

		private transient boolean cancelled;
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

		public void cancel(boolean cancelled) {
			this.cancelled = cancelled;
		}

		public boolean isCancelled() {
			return cancelled;
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
