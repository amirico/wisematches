package wisematches.client.gwt.app.client.content.playboard.infos;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class MoveInfoPanel { //extends WMInfoWidget {
//    private final ScribbleBoard scribbleBoard;
//
//    private TilesSetWidget selectedTilesWidget = new TilesSetWidget();
//    private TilesSetWidget selectedWordWidget = new TilesSetWidget();
//
//    private Word selectedWord;
//    private ScoreCalculation selectedCalculation;
//
//    public MoveInfoPanel(final ScribbleBoard scribbleBoard) {
//        super(PB.ttlMoveInfo());
//
//        this.scribbleBoard = scribbleBoard;
//
//        initPanel();
//        initScribbleInteration();
//    }
//
//    private void initPanel() {
//        addInfoItem("wordPoints", PB.lblMoveInfoWordPoints(), APP.lblNA());
//        addInfoItem("bonuses", PB.lblMoveInfoBonuses(), APP.lblNA());
//        addSeparator();
//        addInfoItem("selectedTiles", PB.lblMoveInfoSelectedTiles(), "");
//        addInfoItem("selectedTilesWidget", selectedTilesWidget);
//        addInfoItem("selectedWord", PB.lblMoveInfoSelectedWord(), "");
//        addInfoItem("selectedWordWidget", selectedWordWidget);
//    }
//
//    private void initScribbleInteration() {
//        scribbleBoard.addScribbleTileListener(new BoardTileListener() {
//            public void tileSelected(Tile tile, boolean selected, boolean handTile) {
//                updateWord();
//            }
//
//            public void tileMoved(Tile tile, boolean fromBoard, boolean toBoard) {
//                updateWord();
//            }
//
//            private void updateWord() {
//                final Tile[] selectedTiles = scribbleBoard.getSelectedTiles();
//                selectedTilesWidget.setTiles(selectedTiles);
//
//                selectedWord = scribbleBoard.getSelectedWord();
//                final boolean wordPresent = (selectedWord != null);
//
//                if (wordPresent) {
//                    selectedWordWidget.setTiles(scribbleBoard.getSelectedWord().getTiles());
//
//                    final ScoreEngine engine = scribbleBoard.getScoreEngine();
//
//                    selectedCalculation = engine.calculateWordScore(selectedWord, scribbleBoard);
//                    setInfoValue("wordPoints", String.valueOf(selectedCalculation.getPoints()));
//
//                    final StringBuilder bonuses = new StringBuilder();
//                    final ScoreBonus.Type[] types = selectedCalculation.getBonuses();
//                    for (ScoreBonus.Type type : types) {
//                        if (type != null) {
//                            if (bonuses.length() != 0) {
//                                bonuses.append(", ");
//                            }
//
//                            switch (type) {
//                                case DOUBLE_LETTER:
//                                    bonuses.append("2L");
//                                    break;
//                                case TRIPLE_LETTER:
//                                    bonuses.append("3L");
//                                    break;
//                                case DOUBLE_WORD:
//                                    bonuses.append("2W");
//                                    break;
//                                case TRIPLE_WORD:
//                                    bonuses.append("2W");
//                                    break;
//                            }
//                        }
//                    }
//                    if (bonuses.length() != 0) {
//                        setInfoValue("bonuses", bonuses.toString());
//                    } else {
//                        setInfoValue("bonuses", PB.lblNoBonuses());
//                    }
//                } else {
//                    selectedWordWidget.setTiles(null);
//                    setInfoValue("wordPoints", APP.lblNA());
//                    setInfoValue("bonuses", APP.lblNA());
//                }
//            }
//        });
//    }
}
