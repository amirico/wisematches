package wisematches.server.web.mail.impl;

import wisematches.kernel.player.Player;
import wisematches.server.web.mail.FromTeam;
import wisematches.server.web.mail.MailSender;
import wisematches.server.web.mail.ToTeam;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public abstract class AbstractMailSender implements MailSender {
    public void sendMail(FromTeam from, Player player, String resource, String... model) {
        sendMail(from, player, resource, convertModel(model));
    }

    public void sendSystemMail(FromTeam from, EnumSet<ToTeam> to, String subject, String template, String... model) {
        sendSystemMail(from, to, subject, template, convertModel(model));
    }

    private Map<String, Object> convertModel(String... model) {
        if (model == null) {
            return null;
        }

        Map<String, Object> mdl = new HashMap<String, Object>();
        for (String s : model) {
            int i = s.indexOf("=");
            if (i != -1) {
                mdl.put(s.substring(0, i), s.substring(i + 1));
            }
        }
        return mdl;
    }
}