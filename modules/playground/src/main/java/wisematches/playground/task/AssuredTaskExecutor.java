package wisematches.playground.task;

import java.io.Serializable;

/**
 * The {@code AssuredTaskExecutor} stores all tasks to a file and replays all tasks
 * in case of any errors.
 *
 * @param <T> type of task ID object. Can be any serializable type.
 * @param <C> type of task context object. Can be any serializable type.
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AssuredTaskExecutor<T extends Serializable, C extends Serializable> {
	/**
	 * Register specified processor in the task executor. After that method
	 * the processor will receive all tasks stored in the executor.
	 *
	 * @param name      the processor name
	 * @param processor the processor callback
	 * @throws IllegalArgumentException if processor with the same name already registered.
	 */
	void registerProcessor(String name, AssuredTaskProcessor<T, C> processor);

	/**
	 * Unregister specified processor in the task executor. After that method
	 * the processor will receive all tasks stored in the executor.
	 *
	 * @param name      the processor name
	 * @param processor the processor callback
	 * @throws IllegalArgumentException if processor with the same name already registered.
	 */
	void unregisterProcessor(String name, AssuredTaskProcessor<T, C> processor);


	/**
	 * Executes new task on specified processor with specified name.
	 *
	 * @param processor   the processor who should process that task.
	 * @param taskId      the task id that will be passed to processor.
	 * @param taskContext the task context that will be passed to processor.
	 */
	void execute(String processor, T taskId, C taskContext);
}