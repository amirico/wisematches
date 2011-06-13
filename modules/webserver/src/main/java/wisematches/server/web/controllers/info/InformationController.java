package wisematches.server.web.controllers.info;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.server.web.controllers.AbstractInfoController;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping({"/info"})
public class InformationController extends AbstractInfoController {
    public InformationController() {
        super("classpath:/i18n/server/info/");
    }

    @RequestMapping("/{pageName}")
    public String infoPages(@PathVariable String pageName,
                            @RequestParam(value = "plain", required = false) String plain,
                            Model model, Locale locale) {
        if (!processInfoPage(pageName, model, locale)) {
            return null;
        }

        if (plain != null) {
            return "/content/templates/resources";
        } else {
            return "/content/templates/helpCenter";
        }
    }
}
