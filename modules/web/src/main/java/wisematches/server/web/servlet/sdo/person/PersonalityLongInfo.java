package wisematches.server.web.servlet.sdo.person;

import wisematches.playground.GameMessageSource;
import wisematches.server.services.relations.PlayerEntityBean;

import java.util.Locale;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class PersonalityLongInfo {
	private PersonalityInfo player;

	private final Locale locale;
	private final PlayerEntityBean bean;
	private final GameMessageSource messageSource;

	public PersonalityLongInfo(PersonalityInfo player, PlayerEntityBean bean, GameMessageSource messageSource, Locale locale) {
		this.bean = bean;
		this.locale = locale;
		this.player = player;
		this.messageSource = messageSource;
	}

	public PersonalityInfo getPlayer() {
		return player;
	}

	public String getLanguage() {
		if (bean.getLanguage() != null) {
			return messageSource.getMessage("language." + bean.getLanguage().getCode(), locale);
		}
		return messageSource.getMessage("search.err.language", locale);
	}

	public int getRatingG() {
		return bean.getRatingG();
	}

	public int getRatingA() {
		return bean.getRatingA();
	}

	public int getActiveGames() {
		return bean.getActiveGames();
	}

	public int getFinishedGames() {
		return bean.getFinishedGames();
	}

	public String getLastMoveTime() {
		return bean.getLastMoveTime() != null ? messageSource.formatDate(bean.getLastMoveTime(), locale) : messageSource.getMessage("search.err.nomoves", locale);
	}

	public String getAverageMoveTime() {
		return bean.getAverageMoveTime() != 0 ? messageSource.formatTimeMinutes((long) (bean.getAverageMoveTime() / 1000 / 60), locale) : messageSource.getMessage("search.err.nomoves", locale);
	}
}
