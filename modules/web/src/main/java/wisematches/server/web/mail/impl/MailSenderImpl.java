package wisematches.server.web.mail.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.ui.velocity.VelocityEngineUtils;
import wisematches.kernel.player.Player;
import wisematches.kernel.util.Language;
import wisematches.server.web.ResourceManager;
import wisematches.server.web.mail.FromTeam;
import wisematches.server.web.mail.ToTeam;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MailSenderImpl extends AbstractMailSender {
    private JavaMailSender mailSender;
    private VelocityEngine velocityEngine;

    private final Map<String, String> templateParameters = new HashMap<String, String>();
    private final Map<ToTeam, String[]> teamAddresses = new HashMap<ToTeam, String[]>();

    private static final Log log = LogFactory.getLog(MailSenderImpl.class);

    public MailSenderImpl() {
    }

    public void sendMail(final FromTeam from, final Player player, final String resource, final Map<String, ?> model) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                final Language locale = player.getLanguage();

                Map<String, Object> mdl = new HashMap<String, Object>();
                if (model == null) {
                    mdl = new HashMap<String, Object>();
                }

                mdl.put("player", player);
                mdl.putAll(templateParameters);
                mdl.putAll(model);

                final String subject = ResourceManager.getFormatString(resource + ".subject", locale, mdl);
                final String template = ResourceManager.getFormatString(resource + ".template", locale, mdl);

                try {
                    final InternetAddress to = new InternetAddress(player.getEmail(), player.getUsername(), "UTF-8");

                    MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                    message.setFrom(from.getInternetAddress(locale));
                    message.setTo(to);
                    message.setSubject(subject);

                    message.setText(VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, mdl), true);
                } catch (UnsupportedEncodingException e) {
                    log.error("Incorrect encoding in mail message", e);
                } catch (MessagingException e) {
                    log.error("Mail message can't be sent", e);
                }
            }
        };
        if (log.isDebugEnabled()) {
            log.debug("Send mail from " + from + " to player " + player + " with resource " + resource);
        }
        this.mailSender.send(preparator);
    }

    public void sendSystemMail(final FromTeam from, final EnumSet<ToTeam> to,
                               final String subject, final String template, final Map<String, ?> model) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
                message.setFrom(from.getInternetAddress(Language.ENGLISH));
                message.setTo(getTeamAddresses(to));
                message.setSubject(subject);

                String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, model);
                message.setText(text, true);
            }
        };
        if (log.isDebugEnabled()) {
            log.debug("Send system mail from " + from + " to teams " + to + " with subject " + subject + " and " +
                    "template " + template);
        }
        this.mailSender.send(preparator);
    }

    private String[] getTeamAddresses(EnumSet<ToTeam> toTeam) {
        List<String> addresses = new ArrayList<String>();
        for (ToTeam team : toTeam) {
            final String[] strings = teamAddresses.get(team);
            if (strings != null) {
                addresses.addAll(Arrays.asList(strings));
            }
        }
        return addresses.toArray(new String[addresses.size()]);
    }

    public void setTeamAddresses(Map<ToTeam, String> addresses) {
        for (Map.Entry<ToTeam, String> toTeamStringEntry : addresses.entrySet()) {
            final ToTeam key = toTeamStringEntry.getKey();
            final String value = toTeamStringEntry.getValue();

            int index = 0;
            final StringTokenizer st = new StringTokenizer(value, ",");
            final String[] res = new String[st.countTokens()];
            while (st.hasMoreTokens()) {
                res[index++] = st.nextToken();
            }
            teamAddresses.put(key, res);
        }
    }

    public void setMailSender(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void setVelocityEngine(VelocityEngine velocityEngine) {
        this.velocityEngine = velocityEngine;
    }

    public void setTemplateParameters(Map<String, String> templateParameters) {
        this.templateParameters.putAll(templateParameters);
    }
}
