package wisematches.server.web.servlet.mvc.account;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import wisematches.server.web.servlet.mvc.WisematchesController;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/account/payment")
public class PaymentController extends WisematchesController {
	public PaymentController() {
	}

	@RequestMapping("subscribe")
	public String subscribePage(Model model) {


		return "/content/account/payment/subscribe";
	}

	@RequestMapping("accepted")
	public String acceptedPage(Model model) {
		return "/content/account/payment/accepted";
	}

	@RequestMapping("rejected")
	public String rejectedPage(Model model) {
		return "/content/account/payment/rejected";
	}
}
