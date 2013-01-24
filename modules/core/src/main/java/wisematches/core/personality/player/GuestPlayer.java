package wisematches.core.personality.player;

import wisematches.core.Language;
import wisematches.core.Membership;
import wisematches.core.Player;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class GuestPlayer extends Player {
	private final Language language;

	private static final Map<Language, GuestPlayer> CACHE = new HashMap<>();

	private GuestPlayer(Long id, Language language) {
		super(id);
		this.language = language;
	}

	public static GuestPlayer byLanguage(Language language) {
		return CACHE.get(language);
	}

	@Override
	public String getNickname() {
		return "guest." + language.code();
	}

	@Override
	public Language getLanguage() {
		return language;
	}

	@Override
	public Membership getMembership() {
		return Membership.GUEST;
	}

	static Collection<GuestPlayer> init(Language[] languages) {
		for (Language language : languages) {
			CACHE.put(language, new GuestPlayer((long) (200 + language.ordinal()), language));
		}
		return CACHE.values();
	}
}
