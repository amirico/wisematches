package wisematches.server.deprecated.web.server.pproc;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * Page pre-processing engine interface. Object of this interface can be added into {@code ApplicationMappingController}
 * to process GWT htmls before sending to client.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 * @see wisematches.server.web.ApplicationMappingController
 */
public interface PagePreProcessor {
	/**
	 * Checks should this pre-processor insert some information after specified line or not.
	 * <p/>
	 * Usually line for pre-processor contains name of preprocessor in commented code. For example:
	 * <pre>
	 *     &lt;html&gt;
	 *       ...
	 *     &lt;body&gt;
	 *       ...
	 *       &lt;!-- My Pre-Processor --&gt;
	 *       ....
	 *     &lt;/body&gt;
	 *     &lt;/html&gt;
	 * </pre>
	 * <p/>
	 * For this example this method should check is specified line contains text "{@code My Pre-Processor}"
	 * and returns {@code true} if yes. After that {@code processFile} will be invoked.
	 *
	 * @param line the line to be checked.
	 * @return {@code true} is file contains information for pre-processing; {@code false} - otherwise.
	 */
	boolean isPreProcessorEnabled(String line);

	/*
		 * TODO: add description here
		 * Indicates that this processor should insert some code in specified page.
		 *
		 * @param
		 */
	void processPageContent(PrintWriter writer, HttpServletRequest request, HttpServletResponse response);
}