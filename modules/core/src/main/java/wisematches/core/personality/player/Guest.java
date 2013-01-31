package wisematches.core.personality.player;

import wisematches.core.Language;
import wisematches.core.Player;
import wisematches.core.PlayerType;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class Guest extends Player {
	private final Language language;

	private static final Map<Language, Guest> CACHE = new HashMap<>();

	private Guest(Long id, Language language) {
		super(id);
		this.language = language;
	}

	public static Guest byLanguage(Language language) {
		return CACHE.get(language);
	}

	@Override
	public String getNickname() {
		return "guest." + language.getCode();
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public PlayerType getPlayerType() {
		return PlayerType.GUEST;
	}

	static Collection<Guest> init(Language[] languages) {
		for (Language language : languages) {
			CACHE.put(language, new Guest((long) (200 + language.ordinal()), language));
		}
		return CACHE.values();
	}
}
