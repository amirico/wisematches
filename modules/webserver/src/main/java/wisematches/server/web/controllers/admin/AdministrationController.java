package wisematches.server.web.controllers.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.playground.scribble.tracking.impl.PlayerStatisticValidator;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/admin")
public class AdministrationController {
    @Autowired
    private PlayerStatisticValidator scribbleStatisticValidator;

    public AdministrationController() {
    }

    @RequestMapping("/main")
    public String mainPage() {
        return "/content/admin/main";
    }

    @RequestMapping("/regenerateStatistic")
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String regenerateStatistic() {
        scribbleStatisticValidator.recalculateStatistics();
        return "/content/admin/main";
    }
}
