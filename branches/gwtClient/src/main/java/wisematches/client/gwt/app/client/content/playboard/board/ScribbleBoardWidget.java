/*
 * Copyright (c) 2009, WiseMatches (by Sergey Klimenko).
 */

package wisematches.client.gwt.app.client.content.playboard.board;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
class ScribbleBoardWidget {//extends FlowPanel implements ScribbleBoard, TilesPlacement {
//    private FlexTable boardTable;
//    private FlexTable handTable;
//
//    private TableCellDropController boardTableDropController;
//    private TableCellDropController handTableDropController;
//
//    private PickupDragController dragController;
//
//    private int boardTilesCount = 0;
//    private Collection<BoardTileListener> tilesSelectionListeners;
//    private Collection<BoardWordListener> wordsSelectionListeners;
//
//    private boolean handTilesDisabled = false;
//    private boolean boardTilesDisabled = false;
//    private boolean clearSelectionByClick = false;
//
//    private Word previousSelectedWord;
//    private ScoreEngine scoreEngine = new ScribbleScoreEngine();
//
//    private final Set<TileWidget> selectedTiles = new HashSet<TileWidget>();
//    private final TheTileSelectionCallback tileSelectionCallback = new TheTileSelectionCallback();
//
//    private final PropertyChangeSupport<ScribbleBoard> propertyChangeSupport = new PropertyChangeSupport<ScribbleBoard>(this);
//
//    public ScribbleBoardWidget() {
//        initPanel();
//    }
//
//    public void addPropertyChangeListener(PropertyChangeListener<ScribbleBoard> l) {
//        propertyChangeSupport.addPropertyChangeListener(l);
//    }
//
//    public void removePropertyChangeListener(PropertyChangeListener<ScribbleBoard> l) {
//        propertyChangeSupport.removePropertyChangeListener(l);
//    }
//
//    private void initPanel() {
//        setStyleName("board");
//        getElement().getStyle().setPropertyPx("width", 342);
//        getElement().getStyle().setPropertyPx("height", 366);
//        getElement().getStyle().setProperty("backgroundImage", "url(\"" + GWT.getModuleBaseURL() + "images/board/scribble_field.png\")");
//
//        final AbsolutePanel boundaryPanel = new AbsolutePanel();
//
//        dragController = new TableCellDragController(boundaryPanel, false);
//        dragController.setBehaviorMultipleSelection(false);
//        dragController.addDragHandler(new BoardDragHandler());
//
//        boardTable = createFlexTable(BOARD_SIZE, BOARD_SIZE, "board-main");
//        boardTableDropController = new TableCellDropController(boardTable);
//        dragController.registerDropController(boardTableDropController);
//
//        handTable = createFlexTable(1, HAND_SIZE, "board-hand");
//        handTableDropController = new TableCellDropController(handTable);
//        dragController.registerDropController(handTableDropController);
//
//        sinkEvents(Event.ONCLICK);
//
//        initScoresPanel();
//
//        boundaryPanel.add(boardTable);
//        boundaryPanel.add(handTable);
//        add(boundaryPanel);
//    }
//
//    private void initScoresPanel() {
//        final FlexTable.FlexCellFormatter formatter = boardTable.getFlexCellFormatter();
//
//        ScribbleBoardPanel.updateCellBonus(formatter, 7, 7, null);
//
//        final ScoreBonus[] placements = scoreEngine.getScoreBonuses();
//        for (final ScoreBonus placement : placements) {
//            final int row = placement.getRow();
//            final int col = placement.getColumn();
//
//
//            final ScoreBonus.Type type = placement.getType();
//
//            if (row != 7 || col != 7) {
//                ScribbleBoardPanel.updateCellBonus(formatter, row, col, type);
//                ScribbleBoardPanel.updateCellBonus(formatter, col, row, type);
//                ScribbleBoardPanel.updateCellBonus(formatter, BOARD_SIZE - row - 1, col, type);
//                ScribbleBoardPanel.updateCellBonus(formatter, col, BOARD_SIZE - row - 1, type);
//                ScribbleBoardPanel.updateCellBonus(formatter, row, BOARD_SIZE - col - 1, type);
//                ScribbleBoardPanel.updateCellBonus(formatter, BOARD_SIZE - col - 1, row, type);
//                ScribbleBoardPanel.updateCellBonus(formatter, BOARD_SIZE - row - 1, BOARD_SIZE - col - 1, type);
//                ScribbleBoardPanel.updateCellBonus(formatter, BOARD_SIZE - col - 1, BOARD_SIZE - row - 1, type);
//            }
//        }
//    }
//
//
//    public void addScribbleTileListener(BoardTileListener l) {
//        if (tilesSelectionListeners == null) {
//            tilesSelectionListeners = new HashSet<BoardTileListener>();
//        }
//        tilesSelectionListeners.add(l);
//    }
//
//    public void removeBoardTileListener(BoardTileListener l) {
//        if (tilesSelectionListeners != null) {
//            tilesSelectionListeners.remove(l);
//        }
//    }
//
//    public void addScribbleWordListener(BoardWordListener l) {
//        if (wordsSelectionListeners == null) {
//            wordsSelectionListeners = new HashSet<BoardWordListener>();
//        }
//        wordsSelectionListeners.add(l);
//    }
//
//    public void removeScribbleWordListener(BoardWordListener l) {
//        if (wordsSelectionListeners != null) {
//            wordsSelectionListeners.remove(l);
//        }
//    }
//
//
//    public Word getSelectedWord() {
//        if (selectedTiles.size() < 2) {
//            return null;
//        }
//
//        Direction direction = null;
//        final Iterator<TileWidget> iterator = selectedTiles.iterator();
//        TileWidget firstTile = iterator.next();
//        while (iterator.hasNext()) {
//            TileWidget tw = iterator.next();
//            if (direction == null) {
//                if (firstTile.getPosition().row == tw.getPosition().row) {
//                    direction = Direction.HORIZONTAL;
//                } else if (firstTile.getPosition().column == tw.getPosition().column) {
//                    direction = Direction.VERTICAL;
//                } else {
//                    return null;
//                }
//            }
//
//            if (direction == Direction.HORIZONTAL) {
//                if (firstTile.getPosition().row != tw.getPosition().row) {
//                    return null;
//                } else if (tw.getPosition().column < firstTile.getPosition().column) {
//                    firstTile = tw;
//                }
//            } else {
//                if (firstTile.getPosition().column != tw.getPosition().column) {
//                    return null;
//                } else if (tw.getPosition().row < firstTile.getPosition().row) {
//                    firstTile = tw;
//                }
//            }
//        }
//
//        final int shift = (direction == Direction.HORIZONTAL ?
//                firstTile.getPosition().column :
//                firstTile.getPosition().row);
//        final Tile[] tiles = new Tile[selectedTiles.size()];
//        for (TileWidget tw : selectedTiles) {
//            final Position position = tw.getPosition();
//            int index;
//            if (direction == Direction.HORIZONTAL) {
//                index = position.column - shift;
//            } else {
//                index = position.row - shift;
//            }
//            if (index < 0 || index >= tiles.length) {
//                return null;
//            }
//            tiles[index] = tw.getTile();
//        }
//
//        return new Word(firstTile.getPosition(), direction, tiles);
//    }
//
//    public Tile[] getSelectedTiles() {
//        int index = 0;
//        Tile[] res = new Tile[selectedTiles.size()];
//        for (TileWidget selectedTile : selectedTiles) {
//            res[index++] = selectedTile.getTile();
//        }
//        return res;
//    }
//
//    public void approveSelectedWord() throws IncorrectWordException, IncorrectPositionException {
//        final Word word = getSelectedWord();
//        if (word == null) {
//            throw new IncorrectWordException(PB.errNoSelectedWordMessage());
//        }
//
//        final int oldBoardTilesCount = boardTilesCount;
//        final Tile[] oldHandTiles = getHandTiles();
//        final int oldHandTilesCount = oldHandTiles.length;
//        for (Word.IteratorItem item : word) {
//            int row = item.getRow();
//            int column = item.getColumn();
//
//            TileWidget tw = (TileWidget) boardTable.getWidget(row, column);
//            if (tw == null) {
//                throw new IncorrectPositionException(row, column, false);
//            }
//
//            if (!tw.isPinned()) {
//                tw.setPinned(true);
//                tw.setSelectionEnabled(true);
//                dragController.makeNotDraggable(tw);
//                boardTilesCount++;
//            }
//            tw.setSelected(false);
//        }
//        propertyChangeSupport.firePropertyChanged("boardTilesCount", oldBoardTilesCount, boardTilesCount);
//        propertyChangeSupport.firePropertyChanged("handTiles", oldHandTiles, getHandTiles());
//        propertyChangeSupport.firePropertyChanged("tilesInHands", oldHandTilesCount, getHandTilesCount());
//    }
//
//    /**
//     * TODO: this method is not oprimazed because it fires a lot of tiles selected events....
//     *
//     * @param word the word to be selected
//     * @throws IncorrectWordException if word can't be selected
//     */
//    public void setSelectedWord(Word word) throws IncorrectWordException {
//        setSelectedWord(word, false);
//    }
//
//    public void setSelectedWord(Word word, boolean clearByClick) throws IncorrectWordException {
//        clearSelection();
//
//        clearSelectionByClick = clearByClick;
//        for (Word.IteratorItem item : word) {
//            final Tile tile = item.getTile();
//            final int row = item.getRow();
//            final int col = item.getColumn();
//
//            TileWidget tw = (TileWidget) boardTable.getWidget(row, col);
//            if (tw != null) {
//                if (!tw.getTile().equals(tile)) {
//                    clearSelection();
//                    throw new IncorrectWordException("Board has another tile at position " + tw.getPosition());
//                }
//                tw.setSelected(true);
//            } else {
//                tw = getHandTileWidget(tile);
//                if (tw == null) {
//                    clearSelection();
//                    throw new IncorrectWordException("No required tile in hand");
//                }
//                moveTileToBoard(tw, new Position(row, col), false, tile.getLetter());
//            }
//        }
//    }
//
//    public void clearSelection() {
//        clearSelectionByClick = false;
//
//        final Set<TileWidget> tiles = new HashSet<TileWidget>(selectedTiles);
//        for (Iterator<TileWidget> iterator = tiles.iterator(); iterator.hasNext();) {
//            final TileWidget tw = iterator.next();
//            if (!tw.isPinned()) {
//                try {
//                    addTileToHand(tw);
//                } catch (IncorrectPositionException ex) {
//                    ;//just ignore
//                }
//            } else {
//                tw.setSelected(false);
//            }
//            iterator.remove();
//        }
//    }
//
//
//    public void addTileToBoard(Tile tile, Position position) throws IncorrectPositionException {
//        final int row = position.getRow();
//        final int column = position.getColumn();
//
//        if (boardTable.getWidget(row, column) != null) {
//            throw new IncorrectPositionException(row, column, true);
//        }
//
//        final TileWidget tw = createTileWidget(tile);
//        tw.setPinned(true);
//        tw.setPosition(position);
//        tw.setSelectionEnabled(true);
//        tw.setSelected(false);
//        boardTable.setWidget(row, column, tw);
//
//        boardTilesCount++;
//        propertyChangeSupport.firePropertyChanged("boardTilesCount", boardTilesCount - 1, boardTilesCount);
//    }
//
//    public void addTileToHand(Tile tile) throws IncorrectPositionException {
//        if (Log.isInfoEnabled()) {
//            Log.info("Add tile to hand: " + tile);
//        }
//        final Tile[] oldHandTiles = getHandTiles();
//
//        final TileWidget tileWidget = createTileWidget(tile);
//        dragController.makeDraggable(tileWidget);
//        addTileToHand(tileWidget);
//
//        propertyChangeSupport.firePropertyChanged("handTiles", oldHandTiles, getHandTiles());
//        propertyChangeSupport.firePropertyChanged("tilesInHands", oldHandTiles.length, oldHandTiles.length + 1);
//    }
//
//    public void replaceHandTiles(Tile[] newTiles) {
//        if (Log.isDebugEnabled()) {
//            Log.debug("Replace hand tiles to new");
//        }
//
//        final Tile[] oldHandTiles = getHandTiles();
//        for (int i = 0, newTilesLength = newTiles.length; i < newTilesLength; i++) {
//            final Tile newTile = newTiles[i];
//            final TileWidget widget = (TileWidget) handTable.getWidget(0, i);
//            if (Log.isDebugEnabled()) {
//                Log.debug("Add replaced tile to hand: " + newTile + " and replace old: " + (widget == null ? "no" : widget.getTile()));
//            }
//            final TileWidget tileWidget = createTileWidget(newTile);
//            tileWidget.setPosition(new Position(0, i));
//            dragController.makeDraggable(tileWidget);
//            handTable.setWidget(0, i, tileWidget);
//        }
//        // Tiles count is not changed
//        propertyChangeSupport.firePropertyChanged("handTiles", oldHandTiles, getHandTiles());
//    }
//
//    public Tile getBoardTile(int row, int column) {
//        final TileWidget widget = (TileWidget) boardTable.getWidget(row, column);
//        if (widget == null) {
//            return null;
//        }
//        return widget.getTile();
//    }
//
//    public boolean hasBoardTile(int row, int column) {
//        final TileWidget tileWidget = (TileWidget) boardTable.getWidget(row, column);
//        return tileWidget != null && tileWidget.isPinned();
//    }
//
//    public boolean isBoardTile(int tileNumber) {
//        for (int i = 0; i < BOARD_SIZE; i++) {
//            for (int j = 0; j < BOARD_SIZE; j++) {
//                final TileWidget tw = (TileWidget) boardTable.getWidget(i, j);
//                if (tw != null && tw.getTile().getNumber() == tileNumber && tw.isPinned()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public int getBoardTilesCount() {
//        return boardTilesCount;
//    }
//
//
//    public Tile[] getHandTiles() {
//        int index = 0;
//        final Tile[] tiles = new Tile[getHandTilesCount()];
//        for (int i = 0; i < HAND_SIZE; i++) {
//            final TileWidget widget = (TileWidget) handTable.getWidget(0, i);
//            if (widget != null) {
//                tiles[index++] = widget.getTile();
//            }
//        }
//        return tiles;
//    }
//
//    public int getHandTilesCount() {
//        int count = 0;
//        for (int i = 0; i < HAND_SIZE; i++) {
//            if (handTable.getWidget(0, i) != null) {
//                count++;
//            }
//        }
//        return count;
//    }
//
//    public boolean isHandTile(int number) {
//        for (int i = 0; i < HAND_SIZE; i++) {
//            final TileWidget widget = (TileWidget) handTable.getWidget(0, i);
//            if (widget != null) {
//                final Tile tile = widget.getTile();
//                if (tile.getNumber() == number) {
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    public ScoreEngine getScoreEngine() {
//        return scoreEngine;
//    }
//
//
//    /**
//     * Disables or enables all scribble board. This method disabled or enables hand tiles and board tiles.
//     *
//     * @param disabled {@code true} to disable board; {@code false} otherwise.
//     * @see #setHandTilesDisabled(boolean)
//     * @see #setBoardsTilesDisabled(boolean)
//     */
//    public void setDisabled(boolean disabled) {
//        setHandTilesDisabled(disabled);
//        setBoardsTilesDisabled(disabled);
//    }
//
//    /**
//     * Disables operation with hand tiles. This method makes draggable or not draggable.
//     *
//     * @param disabled {@code true} to disable hand tiles; {@code false} - otherwise.
//     */
//    public void setHandTilesDisabled(boolean disabled) {
//        if (handTilesDisabled == disabled) {
//            return;
//        }
//
//        for (int i = 0; i < HAND_SIZE; i++) {
//            final TileWidget w = (TileWidget) handTable.getWidget(0, i);
//            if (w != null) {
//                w.setDisabled(disabled);
//                if (disabled) {
//                    dragController.makeNotDraggable(w);
//                } else {
//                    dragController.makeDraggable(w);
//                }
//            }
//        }
//
//        handTilesDisabled = disabled;
//    }
//
//    /**
//     * Disables board tiles making its not selectable by mouse clicking.
//     * <p/>
//     * Tiles still can be selected by board methods like {@code selectTiles} and so on.
//     *
//     * @param disabled {@code true} to disable board tiles; {@code false} - to enable.
//     */
//    public void setBoardsTilesDisabled(boolean disabled) {
//        if (boardTilesDisabled == disabled) {
//            return;
//        }
//
//        for (int i = 0; i < BOARD_SIZE; i++) {
//            for (int j = 0; j < BOARD_SIZE; j++) {
//                final TileWidget tw = (TileWidget) boardTable.getWidget(i, j);
//                if (tw != null && tw.isPinned()) {
//                    tw.setDisabled(disabled);
//                    if (disabled) {
//                        tw.setSelectionEnabled(false);
//                    } else {
//                        tw.setSelectionEnabled(true);
//                    }
//                }
//            }
//        }
//        boardTilesDisabled = disabled;
//    }
//
//
//    public void checkMoveValid() throws IncorrectWordException, IncorrectPositionException {
//        boolean centerCell = false;
//        boolean tileFromHand = false;
//        boolean tileOnBoard = false;
//
//        for (TileWidget tile : selectedTiles) {
//            if (tile.isPinned()) {
//                tileOnBoard = true;
//            } else {
//                tileFromHand = true;
//            }
//
//            if (boardTilesCount == 0) {
//                if (tile.getPosition().row == BOARD_SIZE / 2 && tile.getPosition().column == BOARD_SIZE / 2) {
//                    centerCell = true;
//                }
//            }
//        }
//
//        if (!tileFromHand) {
//            throw new IncorrectWordException(PB.errNoHandTilesMessage());
//        }
//        if (boardTilesCount != 0 && !tileOnBoard) {
//            throw new IncorrectWordException(PB.errNoBoardTilesMessage());
//        }
//        if (boardTilesCount == 0 && !centerCell) {
//            throw new IncorrectPositionException(PB.errFirstMoveNotCenterMessage());
//        }
//        if (getSelectedWord() == null) {
//            throw new IncorrectWordException(PB.errNoSelectedWordMessage());
//        }
//    }
//
//
//    @Override
//    public void onBrowserEvent(Event event) {
//        switch (DOM.eventGetType(event)) {
//            case Event.ONCLICK:
//                if (clearSelectionByClick) {
//                    clearSelection();
//                }
//                break;
//        }
//    }
//
//
//    protected TileWidget createTileWidget(Tile tile) {
//        TileWidget tw = new TileWidget(tile);
//        tw.setSelectionCallback(tileSelectionCallback);
//        return tw;
//    }
//
//    private TileWidget getHandTileWidget(Tile tile) {
//        for (int i = 0; i < HAND_SIZE; i++) {
//            final TileWidget widget = (TileWidget) handTable.getWidget(0, i);
//            if (widget != null) {
//                final Tile tile1 = widget.getTile();
//                if (tile1.getCost() == tile.getCost()) {
//                    if (tile1.isWildcard()) {
//                        return widget;
//                    } else if (tile1.getLetter() == tile.getLetter()) {
//                        return widget;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    private FlexTable createFlexTable(int rows, int cols, String tableStyle) {
//        FlexTable table = new FlexTable();
//        table.setBorderWidth(0);
//        table.setCellPadding(0);
//        table.setCellSpacing(0);
//        table.setStyleName(tableStyle);
//
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                table.setHTML(i, j, "");
//            }
//        }
//        return table;
//    }
//
//    private void smartSelection(TileWidget tileWidget) {
//        if (!tileWidget.isPinned()) {
//            return;
//        }
//
//        boolean select = false;
//        final Position pos = tileWidget.getPosition();
//        for (int i = 0; i < pos.column; i++) {
//            final TileWidget widget = (TileWidget) boardTable.getWidget(pos.row, i);
//            if (widget == null) {
//                continue;
//            }
//
//            if (widget.isSelected()) {
//                select = true;
//            } else if (select) {
//                widget.setSelected(true);
//            }
//        }
//
//        if (!select) {
//            for (int i = BOARD_SIZE - 1; i > pos.column; i--) {
//                final TileWidget widget = (TileWidget) boardTable.getWidget(pos.row, i);
//                if (widget == null) {
//                    continue;
//                }
//
//                if (widget.isSelected()) {
//                    select = true;
//                } else if (select) {
//                    widget.setSelected(true);
//                }
//            }
//        }
//
//        if (!select) {
//            for (int i = 0; i < pos.row; i++) {
//                final TileWidget widget = (TileWidget) boardTable.getWidget(i, pos.column);
//                if (widget == null) {
//                    continue;
//                }
//                if (widget.isSelected()) {
//                    select = true;
//                } else if (select) {
//                    widget.setSelected(true);
//                }
//            }
//        }
//
//        if (!select) {
//            for (int i = BOARD_SIZE - 1; i > pos.row; i--) {
//                final TileWidget widget = (TileWidget) boardTable.getWidget(i, pos.column);
//                if (widget == null) {
//                    continue;
//                }
//                if (widget.isSelected()) {
//                    select = true;
//                } else if (select) {
//                    widget.setSelected(true);
//                }
//            }
//        }
//    }
//
//    private void addTileToHand(TileWidget tw) throws IncorrectPositionException {
//        for (int i = 0; i < HAND_SIZE; i++) {
//            final TileWidget w = (TileWidget) handTable.getWidget(0, i);
//            if (w == null) {
//                if (Log.isDebugEnabled()) {
//                    Log.debug("Set tile's position to: " + i);
//                }
//                tw.setSelected(false);
//                tw.setPosition(new Position(0, i));
//                if (tw.getTile().isWildcard()) {
//                    tw.setTileCharacter(Character.toUpperCase('*'));
//                }
//                handTable.setWidget(0, i, tw);
//                return;
//            }
//        }
//        throw new IncorrectPositionException(PB.errHandIsFull());
//    }
//
//    private void moveTileToBoard(TileWidget tw, Position position, boolean fromBoard, char anyChar) {
//        if (Log.isDebugEnabled()) {
//            Log.debug("Move tile " + tw.getTile() + " from " + (fromBoard ? "board" : "hand") + " to board at " + position);
//        }
//        tw.setSelected(true);
//        tw.setPosition(position);
//        if (tw.getTile().isWildcard()) {
//            if (Log.isDebugEnabled()) {
//                Log.debug("Redefine placed tile's letter to: " + anyChar);
//            }
//            tw.setTileCharacter(anyChar);
//        }
//        if (tw.getParent() == handTable) {
//            tw.removeFromParent();
//            boardTable.setWidget(position.row, position.column, tw);
//        }
//        processTileMoved(tw.getTile(), fromBoard, true);
//    }
//
//    private class BoardDragHandler extends DragHandlerAdapter {
//        private boolean startDragFromBoard;
//
//        @Override
//        public void onDragStart(DragStartEvent event) {
//            startDragFromBoard = (event.getContext().draggable.getParent() == boardTable);
//        }
//
//        public void onDragEnd(DragEndEvent event) {
////            final DragContext context = event.getContext();
////            final TileWidget tw = (TileWidget) context.draggable;
////            if (context.finalDropController == boardTableDropController) {
////                if (tw.getTile().isWildcard()) {
////                    MessageBox.prompt(PB.ttlCastWildcardPromt(), PB.lblCastWildcardPromt(),
////                            new MessageBox.PromptCallback() {
////                                public void execute(String btnId, String s1) {
////                                    if (MessageBox.OK.getID().equalsIgnoreCase(btnId)) {
////                                        final char any = s1.trim().charAt(0);
////                                        if (s1.length() != 1) {
////                                            MessagesBox.showMessage(PB.errCastWildcardTitle(), PB.errCastWildcardMessage());
////                                        } else {
////                                            moveTileToBoard(tw, boardTableDropController.getPosition(context), startDragFromBoard, any);
////                                        }
////                                    } else {
////                                        try {
////                                            addTileToHand(tw);
////                                        } catch (IncorrectPositionException ex) {
////                                            ;
////                                        }
////                                    }
////                                }
////                            });
////                } else {
////                    moveTileToBoard(tw, boardTableDropController.getPosition(context), startDragFromBoard, ' ');
////                }
////            } else if (context.finalDropController == handTableDropController) {
////                tw.setSelected(false);
////                tw.setPosition(handTableDropController.getPosition(context));
////                if (tw.getTile().isWildcard()) {
////                    tw.setTileCharacter(Character.toUpperCase('*'));
////                }
////                processTileMoved(tw.getTile(), startDragFromBoard, false);
////            }
//        }
//    }
//
//    // ================ Listener methods =====================
//    protected void processTileSelected(Tile tile, boolean selected, boolean handTile) {
//        final Tile[] oldSelectedTiles = getSelectedTiles();
//        fireTileSelected(tile, selected, handTile);
//        propertyChangeSupport.firePropertyChanged("selectedTiles", oldSelectedTiles, getSelectedTiles());
//        processWordSelection();
//    }
//
//    protected void processTileMoved(Tile tile, boolean fromBoard, boolean toBoard) {
//        fireTileMoved(tile, fromBoard, toBoard);
//        processWordSelection();
//    }
//
//    protected void processWordSelection() {
//        final Word word = getSelectedWord();
//        if (word == null && previousSelectedWord != null) {
//            fireWordDeselected(previousSelectedWord);
//            propertyChangeSupport.firePropertyChanged("selectedWord", previousSelectedWord, word);
//        } else if (word != null && !word.equals(previousSelectedWord)) {
//            fireWordSelected(word);
//            propertyChangeSupport.firePropertyChanged("selectedWord", previousSelectedWord, word);
//        }
//        previousSelectedWord = word;
//    }
//
//
//    protected void fireTileSelected(Tile tile, boolean selected, boolean handTile) {
//        if (tilesSelectionListeners != null) {
//            for (BoardTileListener selectionListener : tilesSelectionListeners) {
//                selectionListener.tileSelected(tile, selected, handTile);
//            }
//        }
//    }
//
//    protected void fireTileMoved(Tile tile, boolean fromBoard, boolean toBoard) {
//        if (tilesSelectionListeners != null) {
//            for (BoardTileListener selectionListener : tilesSelectionListeners) {
//                selectionListener.tileMoved(tile, fromBoard, toBoard);
//            }
//        }
//    }
//
//    protected void fireWordSelected(Word word) {
//        for (BoardWordListener listener : wordsSelectionListeners) {
//            listener.wordSelected(word);
//        }
//    }
//
//    protected void fireWordDeselected(Word word) {
//        for (BoardWordListener listener : wordsSelectionListeners) {
//            listener.wordDeselected(word);
//        }
//    }
//
//    private class TheTileSelectionCallback implements TileSelectionCallback {
//        private boolean smartSelection = false;
//
//        public void tileSelected(TileWidget tileWidget, boolean selected) {
//            if (selected) {
//                selectedTiles.add(tileWidget);
//                if (!smartSelection) {
//                    smartSelection = true;
//                    smartSelection(tileWidget);
//                    smartSelection = false;
//                }
//            } else {
//                selectedTiles.remove(tileWidget);
//            }
//            processTileSelected(tileWidget.getTile(), selected, tileWidget.isPinned());
//        }
//    }
}