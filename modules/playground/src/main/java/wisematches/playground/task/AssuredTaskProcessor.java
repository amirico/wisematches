package wisematches.playground.task;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface AssuredTaskProcessor<T extends Serializable, C extends Serializable> {
	void processAssuredTask(T taskId, C taskContext);
}
