package wisematches.client.gwt.app.client.content.playboard;

import wisematches.client.gwt.app.client.content.playboard.board.BoardException;
import wisematches.client.gwt.app.client.content.playboard.board.IncorrectWordException;
import wisematches.client.gwt.core.client.beans.PropertyChangeListener;
import wisematches.server.games.scribble.core.Tile;
import wisematches.server.games.scribble.core.TilesPlacement;
import wisematches.server.games.scribble.core.Word;
import wisematches.server.games.scribble.scores.ScoreEngine;

/**
 * This class fires following property change events: {@code selectedWord}, {@code boardTilesCount}, {@code selectedTiles}
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public interface ScribbleBoard extends TilesPlacement {
    int BOARD_SIZE = 15;

    int HAND_SIZE = 7;


    void addScribbleTileListener(BoardTileListener l);

    void removeBoardTileListener(BoardTileListener l);


    void addScribbleWordListener(BoardWordListener l);

    void removeScribbleWordListener(BoardWordListener l);


    void addPropertyChangeListener(PropertyChangeListener<ScribbleBoard> l);

    void removePropertyChangeListener(PropertyChangeListener<ScribbleBoard> l);


    /**
     * Returns array of selected tiles. Selected tiles is not always make a word.
     *
     * @return the array of selected tiles. If no one tile is selected empty array will be returned.
     * @see #getSelectedWord()
     */
    Tile[] getSelectedTiles();

    /**
     * Returns selected word. If word is selected it doesn't means that move can be done. To check that
     * word is valid for move see {@link #checkMoveValid()}.
     *
     * @return the selected word or <code>null</code> if word isn't selected
     *         or selected tiles are not be taken as word.
     * @see #checkMoveValid()
     */
    Word getSelectedWord();

    /**
     * Selects specified word.
     *
     * @param word the word to be selected
     * @throws wisematches.client.gwt.app.client.content.playboard.board.IncorrectWordException
     *          if word can't be selected because it's not present on the board.
     */
    void setSelectedWord(Word word) throws IncorrectWordException;

    /**
     * Selects specified word.
     *
     * @param word         the word to be selected
     * @param clearByClick
     * @throws wisematches.client.gwt.app.client.content.playboard.board.IncorrectWordException
     *          if word can't be selected because it's not present on the board.
     */
    void setSelectedWord(Word word, boolean clearByClick) throws IncorrectWordException;

    /**
     * Clears selected word. All permanent tiles will be unselected, all
     * hand tiles will be moved back to the hand.
     */
    void clearSelection();


    /**
     * Checks that specified move is valid. Move is valid if at least one tile from hand and
     * one tile from board are used.
     * <p/>
     * Is it's first move tiles must be placed at center cell.
     *
     * @throws IncorrectWordException if there is no word selected or word isn't used at least
     *                                one tile from hand and one tile from board (for not first move).
     * @throws wisematches.client.gwt.app.client.content.playboard.board.IncorrectPositionException
     *                                if selected word placed in incorrect place: first move
     *                                not in center
     * @see #getSelectedWord()
     */
    void checkMoveValid() throws BoardException;

    /**
     * Indicates that selected word is correct and makes all tiles as are
     * permanent tiles.
     *
     * @throws IncorrectWordException if there is no word selected or word isn't used at least
     *                                one tile from hand and one tile from board (for not first move).
     * @throws wisematches.client.gwt.app.client.content.playboard.board.IncorrectPositionException
     *                                if selected word placed in incorrect place: first move
     * @see #checkMoveValid()
     */
    void approveSelectedWord() throws BoardException;


    /**
     * Returns count of tiles placed on board.
     *
     * @return the count of tiles placed on board
     */
    int getBoardTilesCount();

    /**
     * Checks that specified tile is placed on board
     *
     * @param row    the tile's row
     * @param column the tile's column
     * @return <code>true</code> if tile is plcaed on board and it's not a tile from hand.
     */
    boolean hasBoardTile(int row, int column);


    /**
     * Returns count of tiles in the hand.
     *
     * @return count of tiles in the hand.
     */
    int getHandTilesCount();

    /**
     * Returns array of hand tiles.
     *
     * @return the array of hand tiles.
     */
    Tile[] getHandTiles();


    /**
     * Returns score engine for this board.
     *
     * @return the score engine for this board.
     */
    ScoreEngine getScoreEngine();
}
