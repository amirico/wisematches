package wisematches.kernel.player;

import wisematches.kernel.util.Language;

/**
 * International player. This interface indicates that player can be localized by locale.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface InternationalPlayer extends Player {
    /**
     * Returns player for specified locale.
     *
     * @param language
     * @return the localized player.
     */
    Player getNationalityPlayer(Language language);
}
