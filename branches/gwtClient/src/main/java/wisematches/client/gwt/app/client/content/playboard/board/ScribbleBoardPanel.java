package wisematches.client.gwt.app.client.content.playboard.board;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class ScribbleBoardPanel { //extends Panel {
//    private ToolbarButton passTurn;
//    private ToolbarButton makeTurn;
//    private ToolbarButton resign;
//    private ToolbarButton resetTiles;
//    private ToolbarButton exchangeTiles;
//
//    private ScribbleBoardWidget scribbleBoard;
//
//    private boolean disabled;
//
//    private final long currentPlayer;
//    private final PlayboardItemBean playboardItemBean;
//    private final ScribbleBoardCallback scribbleBoardCallback;
//    private final PlayboardServiceAsync playboardService = PlayboardService.App.getInstance();
//
//    public ScribbleBoardPanel(long currentPlayer, PlayboardItemBean playboardItemBean, ScribbleBoardCallback scribbleBoardCallback) {
//        this.currentPlayer = currentPlayer;
//        this.playboardItemBean = playboardItemBean;
//        this.scribbleBoardCallback = scribbleBoardCallback;
//
//        initButtons();
//        initPanel();
//        initPanelState();
//    }
//
//    private void initPanel() {
//        setTitle(PB.ttlGameBoard());
//        setCls("board-wrapper");
//        setFrame(true);
//        setWidth(356);
//        setHeight(446);
//
//        addTool(new Tool(new Tool.ToolType("info"), new ToolHandler() {
//            public void onClick(EventObject eventObject, ExtElement extElement, Panel panel) {
//                final RulesWindow w = new RulesWindow(COMMON.tltScribbleRules(), RuleInfo.SCRIBBLE_RULES);
//                w.setCloseAction(RulesWindow.CLOSE);
//                w.setModal(true);
//                w.show(extElement.getDOM());
//            }
//        }, COMMON.ttpOpenGameRulesDialog()));
//
//        final Toolbar topToolbar = new WMFlatToolbar();
//        topToolbar.addButton(resign);
//        topToolbar.addFill();
//        topToolbar.addButton(makeTurn);
//        topToolbar.addButton(passTurn);
//        setTopToolbar(topToolbar);
//
//        final Toolbar bottomToolbar = new WMFlatToolbar();
//        bottomToolbar.addButton(exchangeTiles);
//        bottomToolbar.addFill();
//        bottomToolbar.addButton(resetTiles);
//        setBottomToolbar(bottomToolbar);
//
//        final TheBoardListener boardListener = new TheBoardListener();
//
//        scribbleBoard = new ScribbleBoardWidget();
//        scribbleBoard.addScribbleWordListener(boardListener);
//        scribbleBoard.addScribbleTileListener(boardListener);
//
//        add(scribbleBoard);
//    }
//
//    private void initButtons() {
//        passTurn = new ToolbarButton(PB.btnPassTurn());
//        passTurn.setTooltip(PB.ttpPassTurn());
//        passTurn.setIcon(GWT.getModuleBaseURL() + "images/playboard/passTurn.png");
//        passTurn.addListener(new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject eventObject) {
//                passTurn();
//            }
//        });
//        passTurn.setDisabled(true);
//
//        makeTurn = new ToolbarButton(PB.btnMakeTurn());
//        makeTurn.setTooltip(PB.ttpMakeTurn());
//        makeTurn.setIcon(GWT.getModuleBaseURL() + "images/playboard/makeTurn.png");
//        makeTurn.addListener(new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject eventObject) {
//                makeTurn();
//            }
//        });
//        makeTurn.setDisabled(true);
//
//        resign = new ToolbarButton(PB.btnResign());
//        resign.setTooltip(PB.ttpResign());
//        resign.setIcon(GWT.getModuleBaseURL() + "images/playboard/resign.png");
//        resign.addListener(new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject eventObject) {
//                resign();
//            }
//        });
//
//        resetTiles = new ToolbarButton(PB.btnResetTiles());
//        resetTiles.setTooltip(PB.ttpResetTiles());
//        resetTiles.setIcon(GWT.getModuleBaseURL() + "images/playboard/clearWord.png");
//        resetTiles.setDisabled(true);
//        resetTiles.addListener(new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject eventObject) {
//                reset();
//            }
//        });
//
//        exchangeTiles = new ToolbarButton(PB.btnExchangeTiles());
//        exchangeTiles.setTooltip(PB.ttpExchangeTiles());
//        exchangeTiles.setIcon(GWT.getModuleBaseURL() + "images/playboard/exchangeTiles.png");
//        exchangeTiles.addListener(new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject eventObject) {
//                exchangeTiles();
//            }
//        });
//    }
//
//    private void initPanelState() {
//        playboardItemBean.addPropertyChangeListener(new TheModelListener());
//
//        final List<PlayerMoveBean> moveBeanList = playboardItemBean.getPlayersMoves();
//        for (PlayerMoveBean bean : moveBeanList) {
//            registerMoveOnBoard(bean);
//        }
//
//        updateHandTiles(playboardItemBean.getHandTiles());
//
//        disableBoard(playboardItemBean.getGameState() != GameboardItemBean.GameState.RUNNING);
//
//        updateButtonsState();
//    }
//
//    public ScribbleBoard getScribbleBoard() {
//        return scribbleBoard;
//    }
//
//    private void disableBoard(boolean disabled) {
//        if (this.disabled != disabled) {
//            this.disabled = disabled;
//            updateButtonsState();
//        }
//    }
//
//    private void makeTurn() {
//        final Word word = scribbleBoard.getSelectedWord();
//        if (word == null) {
//            MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errNoSelectedWordMessage());
//            return;
//        }
//
//        lockBoard(PB.lblMakingTurn());
//        playboardService.makeTurn(playboardItemBean.getBoardId(), word, new AsyncCallback<TurnResult>() {
//            public void onFailure(Throwable throwable) {
//                showMoveError(throwable);
//                unlockBoard();
//            }
//
//            public void onSuccess(TurnResult t) {
//                try {
//                    scribbleBoard.approveSelectedWord();
//                } catch (IncorrectWordException e) {
//                    MessagesBox.showMessage(PB.errBoardStateTitle(), PB.errBoardStateMessage());
//                } catch (IncorrectPositionException e) {
//                    MessagesBox.showMessage(PB.errBoardStateTitle(), PB.errBoardStateMessage());
//                }
//
//                final Tile[] handTile = t.getHandTiles();
//                for (Tile tile : handTile) {
//                    try {
//                        if (!scribbleBoard.isHandTile(tile.getNumber())) {
//                            scribbleBoard.addTileToHand(tile);
//                        }
//                    } catch (IncorrectPositionException e) {
//                        MessagesBox.showMessage(PB.errBoardStateTitle(), PB.errBoardStateMessage());
//                    }
//                }
//                scribbleBoardCallback.playerMoved(currentPlayer, word, t);
//
//                unlockBoard(PB.ttlMakeTurn(), PB.lblMakeTurnSuccess());
//            }
//        });
//    }
//
//    private void passTurn() {
//        MessageBox.confirm(PB.ttlPassTurnConfirm(), PB.lblPassTurnConfirm(), new MessageBox.ConfirmCallback() {
//            public void execute(String s) {
//                if ("yes".equals(s)) {
//                    lockBoard(PB.lblPassingTurn());
//
//                    playboardService.passTurn(playboardItemBean.getBoardId(), new AsyncCallback<TurnResult>() {
//                        public void onFailure(Throwable throwable) {
//                            showMoveError(throwable);
//                            unlockBoard();
//                        }
//
//                        public void onSuccess(TurnResult t) {
//                            scribbleBoardCallback.playerPassed(currentPlayer, t);
//                            unlockBoard(PB.ttlPassTurnSuccess(), PB.lblPassTurnSuccess());
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    private void resign() {
//        MessageBox.confirm(PB.ttlResignConfirm(), PB.lblResignConfirm(), new MessageBox.ConfirmCallback() {
//            public void execute(String s) {
//                if ("yes".equals(s)) {
//                    lockBoard(PB.lblResigningTurn());
//
//                    playboardService.resign(playboardItemBean.getBoardId(), new AsyncCallback<Void>() {
//                        public void onFailure(Throwable throwable) {
//                            showMoveError(throwable);
//                            unlockBoard();
//                        }
//
//                        public void onSuccess(Void t) {
//                            setDisabled(true);
//                            scribbleBoardCallback.playerResigned(currentPlayer);
//                            unlockBoard(PB.ttlPassTurnSuccess(), PB.lblPassTurnSuccess());
//                        }
//                    });
//                }
//            }
//        });
//    }
//
//    private void exchangeTiles() {
//        scribbleBoard.clearSelection();
//
//        final int count = playboardItemBean.getBankCapacity() - playboardItemBean.getTilesInHands() - scribbleBoard.getBoardTilesCount();
//        final ExchangeTilesWindow w = new ExchangeTilesWindow(scribbleBoard, count);
//        w.doExchange(new AsyncCallback<int[]>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(int[] tiles) {
//                exchangeTilesImpl(tiles);
//            }
//        });
//    }
//
//    private void exchangeTilesImpl(int[] tiles) {
//        lockBoard(PB.lblExchangingTurn());
//        playboardService.exchangeTiles(playboardItemBean.getBoardId(), tiles, new AsyncCallback<TurnResult>() {
//            public void onFailure(Throwable throwable) {
//                showMoveError(throwable);
//                unlockBoard();
//            }
//
//            public void onSuccess(TurnResult t) {
//                updateButtonsState();
//
//                scribbleBoard.replaceHandTiles(t.getHandTiles());
//                scribbleBoardCallback.playerExchanged(currentPlayer, t);
//
//                unlockBoard(PB.ttlPassTurnSuccess(), PB.lblPassTurnSuccess());
//            }
//        });
//    }
//
//    private void reset() {
//        scribbleBoard.clearSelection();
//
//        updateButtonsState();
//    }
//
//    private void updateButtonsState() {
//        if (disabled) {
//            resign.setDisabled(true);
//            makeTurn.setDisabled(true);
//            passTurn.setDisabled(true);
//            exchangeTiles.setDisabled(true);
//            resetTiles.setDisabled(true);
//
//            scribbleBoard.clearSelection();
//            scribbleBoard.setDisabled(true);
//        } else {
//            if (currentPlayer == playboardItemBean.getPlayerMove()) {
//                if (scribbleBoard.getSelectedWord() != null) {
//                    makeTurn.setDisabled(false);
//                } else {
//                    makeTurn.setDisabled(true);
//                }
//                passTurn.setDisabled(false);
//                exchangeTiles.setDisabled(false);
//            } else {
//                makeTurn.setDisabled(true);
//                passTurn.setDisabled(true);
//                exchangeTiles.setDisabled(true);
//            }
//
//            if (scribbleBoard.getSelectedTiles().length != 0) {
//                resetTiles.setDisabled(false);
//            } else {
//                resetTiles.setDisabled(true);
//            }
//
//            scribbleBoard.setDisabled(false);
//            resign.setDisabled(false);
//        }
//    }
//
//
//    private void lockBoard(String message) {
//        scribbleBoardCallback.boardLocked(message);
//    }
//
//    private void unlockBoard(String ttl, String message) {
//        scribbleBoardCallback.boardUnlocked();
//        MessagesBox.showMessage(ttl, message);
//    }
//
//    private void unlockBoard() {
//        scribbleBoardCallback.boardUnlocked();
//    }
//
//    /**
//     * Updates hand tiles to specified one.
//     *
//     * @param handTiles the new hand tiles.
//     */
//    private void updateHandTiles(Tile[] handTiles) {
//        for (Tile tile : handTiles) {
//            try {
//                if (!scribbleBoard.isHandTile(tile.getNumber())) {
//                    scribbleBoard.addTileToHand(tile);
//                }
//            } catch (IncorrectPositionException ex) {
//                MessagesBox.showMessage(PB.errBoardStateTitle(), PB.errBoardStateMessage());
//            }
//        }
//    }
//
//    /**
//     * Registers specified word on board after move and increases player points if {@code increasePoints} flag
//     * is on.
//     * <p/>
//     * This method returns id of registred word. This word can be selected on board using this id and
//     * {@code selectHistoryWord(String)} method.
//     *
//     * @param playerMove the word to be registred.
//     */
//    private void registerMoveOnBoard(PlayerMoveBean playerMove) {
//        scribbleBoard.clearSelection();
//
//        final Word word = playerMove.getWord();
//        if (word != null) {
//            for (Word.IteratorItem item : word) {
//                try {
//                    final int row = item.getRow();
//                    final int column = item.getColumn();
//
//                    if (!scribbleBoard.hasBoardTile(row, column)) {
//                        scribbleBoard.addTileToBoard(item.getTile(), new Position(row, column));
//                    }
//                } catch (IncorrectPositionException ex) {
//                    MessagesBox.showMessage(PB.errBoardStateTitle(), PB.errBoardStateMessage());
//                }
//            }
//        }
//    }
//
//
//    private void showMoveError(Throwable th) {
//        if (th instanceof PlayerMoveException) {
//            final PlayerMoveException exception = (PlayerMoveException) th;
//            switch (exception.getErrorCode()) {
//                case GAME_FINISHED:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errGameFinishedMessage());
//                    break;
//                case GAME_NOT_READY:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errGameNotReadyMessage());
//                    break;
//                case CELL_ALREADY_BUSY:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errCellAlreadyBusyMessage());
//                    break;
//                case FIRST_NOT_IN_CENTER:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errFirstMoveNotCenterMessage());
//                    break;
//                case INCORRECT_POSITION:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errIncorrectPositionMessage());
//                    break;
//                case NO_BOARD_TILES:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errNoBoardTilesMessage());
//                    break;
//                case NO_HAND_TILES:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errNoHandTilesMessage());
//                    break;
//                case TILE_ALREADY_PLACED:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errTileAlredyPlacedMessage());
//                    break;
//                case UNKNOWN_TILE:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errUnknownTileMessage());
//                    break;
//                case UNSUITABLE_PLAYER:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errUnsuitablePlayerMessage());
//                    break;
//                case UNKNOWN_WORD:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errUnknownWordMessage());
//                    break;
//                default:
//                    MessagesBox.showMessage(PB.errIncorrectMoveTitle(), PB.errUnknownErrorMessage());
//            }
//        } else {
//            ExceptionHandler.showSystemError(th);
//        }
//    }
//
//
//    private class TheBoardListener implements BoardWordListener, BoardTileListener {
//        public void wordSelected(Word word) {
//            if (!passTurn.isDisabled()) {
//                makeTurn.setDisabled(false);
//            }
//            resetTiles.setDisabled(scribbleBoard.getSelectedTiles().length == 0);
//        }
//
//        public void wordDeselected(Word word) {
//            if (!passTurn.isDisabled()) {
//                makeTurn.setDisabled(true);
//            }
//            resetTiles.setDisabled(scribbleBoard.getSelectedTiles().length == 0);
//        }
//
//        public void tileSelected(Tile tile, boolean selected, boolean handTile) {
//            resetTiles.setDisabled(scribbleBoard.getSelectedTiles().length == 0);
//        }
//
//        public void tileMoved(Tile tile, boolean fromBoard, boolean toBoard) {
//            resetTiles.setDisabled(scribbleBoard.getSelectedTiles().length == 0);
//        }
//    }
//
//    private class TheModelListener implements PropertyChangeListener<PlayboardItemBean> {
//        public void propertyChanged(PlayboardItemBean bean, String property, Object oldValue, Object newValue) {
//            if ("handTiles".equals(property)) {
//                final Tile[] handTiles = (Tile[]) newValue;
//                updateHandTiles(handTiles);
//            } else if ("playersMoves".equals(property)) {
//                @SuppressWarnings("unchecked")
//                final List<PlayerMoveBean> playerMoves = (List<PlayerMoveBean>) newValue;
//                // if list was updated - add last move to board
//                registerMoveOnBoard(playerMoves.get(playerMoves.size() - 1));
//            } else if ("playerMove".equals(property)) {
//                updateButtonsState();
//            } else if ("gameState".equals(property)) {
//                final PlayboardItemBean.GameState gameState = (PlayboardItemBean.GameState) newValue;
//                disableBoard(gameState != GameboardItemBean.GameState.RUNNING);
//            }
//        }
//    }
//
//    public static void updateCellBonus(FlexTable.FlexCellFormatter formatter, int row, int col, ScoreBonus.Type type) {
//        final Element element1 = formatter.getElement(row, col);
//        if (type != null) {
//            element1.setClassName("bonus-cell bonus-cell-" + type.getDisplayName());
//        } else {
//            element1.setClassName("bonus-cell bonus-cell-center");
//        }
//    }
}
