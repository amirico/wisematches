package wisematches.playground.scribble.bank.impl;

import wisematches.personality.Language;
import wisematches.playground.scribble.bank.LettersDistribution;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;
import wisematches.playground.scribble.bank.UnsupportedLanguageException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public abstract class AbstractTilesBankingHouse implements TilesBankingHouse {
	private final Lock lock = new ReentrantLock();
	private final Map<TilesBankKey, LettersDistribution> tilesInfoCache = new HashMap<TilesBankKey, LettersDistribution>();

	protected AbstractTilesBankingHouse() {
	}

	@Override
	public final TilesBank createTilesBank(Language language, int playersCount, boolean wildcardAllowed) {
		lock.lock();
		try {
			final TilesBankKey key = new TilesBankKey(language, playersCount, wildcardAllowed);
			LettersDistribution lettersDistribution = tilesInfoCache.get(key);
			if (lettersDistribution == null) {
				lettersDistribution = loadTilesBankInfo(language, playersCount, wildcardAllowed);
				tilesInfoCache.put(key, lettersDistribution);
			}
			return new TilesBank(lettersDistribution);
		} catch (Exception ex) {
			throw new UnsupportedLanguageException("Tiles Bank for language " + language + " can't be loaded", ex);
		} finally {
			lock.unlock();
		}
	}

	protected abstract LettersDistribution loadTilesBankInfo(Language language, int playersCount, boolean wildcardAllowed) throws Exception;

	private static final class TilesBankKey {
		private final Language language;
		private final int playersCount;
		private final boolean wildcardAllowed;

		private TilesBankKey(Language language, int playersCount, boolean wildcardAllowed) {
			this.language = language;
			this.playersCount = playersCount;
			this.wildcardAllowed = wildcardAllowed;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;

			TilesBankKey that = (TilesBankKey) o;
			return playersCount == that.playersCount && wildcardAllowed == that.wildcardAllowed && language == that.language;
		}

		@Override
		public int hashCode() {
			int result = language.hashCode();
			result = 31 * result + playersCount;
			result = 31 * result + (wildcardAllowed ? 1 : 0);
			return result;
		}
	}
}
