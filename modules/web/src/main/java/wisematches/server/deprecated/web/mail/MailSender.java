package wisematches.server.deprecated.web.mail;

import wisematches.server.personality.player.Player;

import java.util.EnumSet;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
@Deprecated
public interface MailSender {
	public void sendMail(FromTeam from, Player player, String resource, String... model);

	/**
	 * Sends mail from specified address to specified player.
	 *
	 * @param from
	 * @param player
	 * @param resource
	 * @param model
	 */
	public void sendMail(FromTeam from, Player player, String resource, Map<String, ?> model);


	/**
	 * This is system function and should be used only to notify developers about any problems.
	 * <p/>
	 * This method takes {@code ToTeam} enumeration that contains group of developer teams that should
	 * received this message.
	 *
	 * @param from	 the from address.
	 * @param to	   the enumeration of teams that should received this message.
	 * @param subject  the subject of email
	 * @param template the resource that contains subject and description of problem.
	 * @param model	the model that contains data for template.
	 */
	public void sendSystemMail(FromTeam from, EnumSet<ToTeam> to, String subject, String template, String... model);

	/**
	 * This is system function and should be used only to notify developers about any problems.
	 * <p/>
	 * This method takes {@code ToTeam} enumeration that contains group of developer teams that should
	 * received this message.
	 *
	 * @param from	 the from address.
	 * @param to	   the enumeration of teams that should received this message.
	 * @param subject  the subject of email
	 * @param template the resource that contains subject and description of problem.
	 * @param model	the model that contains data for template.
	 */
	public void sendSystemMail(FromTeam from, EnumSet<ToTeam> to, String subject, String template, Map<String, ?> model);
}
