package wisematches.client.android.app.playground.scribble.model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class JSONScribbleParser {
	public static ScribbleGame parseGame(JSONObject data) throws JSONException {
		final long id = data.getLong("id");

		final JSONArray jsonBonuses = data.getJSONArray("bonuses");
		final ScoreBonus[] bonuses = new ScoreBonus[jsonBonuses.length()];
		for (int i = 0; i < bonuses.length; i++) {
			JSONObject o = jsonBonuses.getJSONObject(i);
			bonuses[i] = new ScoreBonus(o.getInt("row"), o.getInt("column"), ScoreBonus.Type.valueOf(o.getString("type")));
		}

		final JSONArray jsonPlayers = data.getJSONArray("players");
		final List<ScribblePlayer> players = new ArrayList<>();
		final Map<Long, ScribblePlayer> playersMap = new HashMap<>();
		for (int i = 0; i < jsonPlayers.length(); i++) {
			final JSONObject jsonPlayer = jsonPlayers.getJSONObject(i);

			final long pid = jsonPlayer.getLong("id");

			final JSONObject jsonInfo = jsonPlayer.getJSONObject("info");

			final JSONObject jsonScore = jsonPlayer.getJSONObject("score");

			ScribblePlayer p = new ScribblePlayer(pid,
					jsonInfo.getString("nickname"),
					jsonInfo.getString("type"),
					jsonInfo.getString("membership"),
					jsonInfo.getBoolean("online"));

			p.setPoints(jsonScore.getInt("points"));
			p.setNewRating(jsonScore.getInt("newRating"));
			p.setOldRating(jsonScore.getInt("oldRating"));
			p.setWinner(jsonScore.getBoolean("winner"));

			players.add(p);
			playersMap.put(p.getId(), p);
		}

		final JSONArray jsonMoves = data.getJSONArray("moves");
		List<ScribbleMove> moves = new ArrayList<>();
		for (int i = 0; i < jsonMoves.length(); i++) {
			final JSONObject jsonMove = jsonMoves.getJSONObject(i);
			final int number = jsonMove.getInt("number");
			final int points = jsonMove.getInt("points");
			final long pid = jsonMove.getLong("player");
			final Date time = new Date(jsonMove.getJSONObject("time").getLong("millis"));

			final MoveType moveType = MoveType.valueOf(jsonMove.getString("type"));

			ScribbleMove move = null;
			switch (moveType) {
				case MAKE:
					final JSONObject jsonWord = jsonMove.getJSONObject("word");

					final JSONObject jsonPosition = jsonWord.getJSONObject("position");

					final JSONArray jsonTiles = jsonWord.getJSONArray("tiles");
					final ScribbleTile[] tiles = new ScribbleTile[jsonTiles.length()];
					for (int j = 0; j < jsonTiles.length(); j++) {
						final JSONObject jsonTile = jsonTiles.getJSONObject(j);
						tiles[j] = new ScribbleTile(
								jsonTile.getInt("cost"),
								jsonTile.getInt("number"),
								jsonTile.getString("letter"));
					}
					final ScribbleWord w = new ScribbleWord(
							jsonPosition.getInt("row"),
							jsonPosition.getInt("column"),
							WordDirection.valueOf(jsonWord.getString("direction")),
							tiles);
					move = new ScribbleMove.Make(number, points, time, playersMap.get(pid), w);
					break;
				case PASS:
					move = new ScribbleMove.Pass(number, points, time, playersMap.get(pid));
					break;
				case EXCHANGE:
					move = new ScribbleMove.Exchange(number, points, time, playersMap.get(pid));
					break;
			}
			moves.add(move);
		}

		final JSONObject jsonSettings = data.getJSONObject("settings");
		final ScribbleSettings settings = new ScribbleSettings(
				jsonSettings.getString("title"),
				jsonSettings.getString("language"),
				jsonSettings.getInt("daysPerMove"),
				jsonSettings.getBoolean("scratch"));

		final JSONObject jsonBank = data.getJSONObject("bank");
		final JSONArray letterDescriptions = jsonBank.getJSONArray("letterDescriptions");

		final Collection<ScribbleLetter> letters = new ArrayList<>(letterDescriptions.length());
		for (int i = 0; i < letterDescriptions.length(); i++) {
			final JSONObject jsonLetter = letterDescriptions.getJSONObject(i);
			letters.add(new ScribbleLetter(
					jsonLetter.getString("letter").charAt(0),
					jsonLetter.getInt("count"),
					jsonLetter.getInt("cost")));
		}
		final ScribbleBank scribbleBank = new ScribbleBank(letters);

		return new ScribbleGame(id, settings, scribbleBank, players, moves, new ScoreEngine(bonuses, 30));
	}
}
