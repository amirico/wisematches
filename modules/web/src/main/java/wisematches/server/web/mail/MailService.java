package wisematches.server.web.mail;

import wisematches.server.player.Player;

import java.util.Map;

/**
 * This is service interface under Spring and javax mail senders that has only a few specific methods.
 *
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public interface MailService {
	/**
	 * Sends mail from specified account to specified player. Each mail represented by special
	 * {@code msgCode} that will be mapped to appropriate message sources.
	 * <p/>
	 * This is asynchronous method that returns immediately after mail template has been prepared. Please
	 * check {@link #sendWarrantyMail(SenderAccount, wisematches.server.player.Player, String, java.util.Map)} to be
	 * sure that email has been sent.
	 *
	 * @param from	required parameter that describes account name for sender.
	 * @param to	  required parameter that contains player who should receive this message.
	 * @param msgCode the mail message code. By the fact it's resource name.
	 * @param model   the model with mail parameters. Can be null if no parameters required.
	 * @throws IllegalArgumentException if {@code from} sender is null, {@code player} is null or {@code msgCode} is null
	 */
	public void sendMail(SenderAccount from, Player to, String msgCode, Map<String, ?> model);

	/**
	 * Sends mail from specified account to specified player. Each mail represented by special
	 * {@code msgCode} that will be mapped to appropriate message sources.
	 * <p/>
	 * This is synchronous method gets warranty that email was sent.
	 *
	 * @param from	required parameter that describes account name for sender.
	 * @param to	  required parameter that contains player who should receive this message.
	 * @param msgCode the mail message code. By the fact it's resource name.
	 * @param model   the model with mail parameters. Can be null if no parameters required.
	 * @throws MailException if msgCode can't be mapped to exist resources
	 */
	public void sendWarrantyMail(SenderAccount from, Player to, String msgCode, Map<String, ?> model) throws MailException;

	/**
	 * Sends support request to support team. This method always send email to {@code support@} email for
	 * this site and always using english language.
	 *
	 * @param subject the mail message subject.
	 * @param msgCode the mail message code. By the fact it's resource name.
	 * @param model   the model with mail parameters. Can be null if no parameters required.
	 * @throws MailException if msgCode can't be mapped to exist resources
	 */
	public void sendSupportRequest(String subject, String msgCode, Map<String, ?> model) throws MailException;
}
