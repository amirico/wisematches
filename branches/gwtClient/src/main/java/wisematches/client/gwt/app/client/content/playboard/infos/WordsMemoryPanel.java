package wisematches.client.gwt.app.client.content.playboard.infos;

import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class WordsMemoryPanel extends Canvas {
//    private Store wordsStore;
//    private RecordDef recordDef;
//
//    private int wordId = 0;
//    private boolean disabled = false;
//
//    private ToolbarButton clearMemory;
//    private ToolbarButton rememberWord;
//
//    private final ScribbleBoard scribbleBoard;
//    private final PlayboardItemBean playboardItemBean;
//    private final MemoryWordsServiceAsync memoryWordsService;
//
//    public WordsMemoryPanel(PlayboardItemBean playboardItemBean, ScribbleBoard scribbleBoard, MemoryWordsServiceAsync memoryWordsService) {
//        super(PB.ttlWordsMemory());
//        this.playboardItemBean = playboardItemBean;
//        this.memoryWordsService = memoryWordsService;
//        this.scribbleBoard = scribbleBoard;
//
//        initPanel();
//        loadMemoryWords();
//        initBoardListener();
//
//        setDisabled(playboardItemBean.getGameState() != GameboardItemBean.GameState.RUNNING);
//
//        playboardItemBean.addPropertyChangeListener(new PropertyChangeListener<PlayboardItemBean>() {
//            public void propertyChanged(PlayboardItemBean bean, String property, Object oldValue, Object newValue) {
//                if ("gameState".equals(property)) {
//                    final PlayboardItemBean.GameState gameState = (PlayboardItemBean.GameState) newValue;
//                    setDisabled(gameState != GameboardItemBean.GameState.RUNNING);
//                }
//            }
//        });
//    }
//
//    private void initPanel() {
//        setFrame(true);
//        setCollapsible(true);
//        setLayout(new FitLayout());
//
//        createToolbarButtons();
//
//        final Toolbar toolbar = new Toolbar();
//        toolbar.addButton(rememberWord);
//        toolbar.addFill();
//        toolbar.addButton(clearMemory);
//
//        recordDef = new RecordDef(
//                new FieldDef[]{
//                        new StringFieldDef("word"),
//                        new ObjectFieldDef("memoryWord"),
//                        new StringFieldDef("points"),
//                        new StringFieldDef("actions")
//                }
//        );
//
//        final GridPanel grid = new GridPanel();
//
//        wordsStore = new Store(recordDef);
//        wordsStore.addStoreListener(new TheClearMemoryListener());
//
//        grid.setStore(wordsStore);
//
//        final BaseColumnConfig[] columns = new BaseColumnConfig[]{
//                new ColumnConfig(PB.lblWordsMemoryWord(), "word", 160, true, null, "word"),
//                new ColumnConfig(PB.lblWordsMemoryPoints(), "points", 45),
//                new ColumnConfig(PB.lblWordsMemoryActions(), "actions", 45, false, new ActionRenderer())
//        };
//
//        grid.setStripeRows(true);
//        grid.setColumnModel(new ColumnModel(columns));
//        grid.setAutoExpandColumn("word");
//        grid.setSelectionModel(new RowSelectionModel(false));
//        grid.setTopToolbar(toolbar);
//
//        add(grid);
//    }
//
//    private void createToolbarButtons() {
//        rememberWord = new ToolbarButton(PB.btnRememberWord());
//        rememberWord.setTooltip(PB.ttpRememberWord());
//        rememberWord.setIcon(GWT.getModuleBaseURL() + "images/playboard/rememberWord.png");
//        rememberWord.setDisabled(true);
//        rememberWord.addListener(new ButtonListenerAdapter() {
//            public void onClick(Button button, EventObject eventObject) {
//                addWord(scribbleBoard.getSelectedWord());
//                scribbleBoard.clearSelection();
//            }
//        });
//
//        clearMemory = new ToolbarButton(PB.btnClearMemoryWords());
//        clearMemory.setTooltip(PB.ttpClearMemoryWords());
//        clearMemory.setIcon(GWT.getModuleBaseURL() + "images/playboard/clearMemory.png");
//        clearMemory.setDisabled(true);
//        clearMemory.addListener(new ButtonListenerAdapter() {
//            @Override
//            public void onClick(Button button, EventObject eventObject) {
//                clearMemoryWords();
//            }
//        });
//    }
//
//    private void initBoardListener() {
//        scribbleBoard.addScribbleWordListener(new BoardWordListener() {
//            public void wordSelected(Word word) {
//                if (!disabled) {
//                    rememberWord.setDisabled(false);
//                }
//            }
//
//            public void wordDeselected(Word word) {
//                if (!disabled) {
//                    rememberWord.setDisabled(true);
//                }
//            }
//        });
//    }
//
//    private void loadMemoryWords() {
//        memoryWordsService.getMemoryWords(playboardItemBean.getBoardId(), new AsyncCallback<MemoryWord[]>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(MemoryWord[] memoryWords) {
//                for (MemoryWord mw : memoryWords) {
//                    final int number = mw.getNumber();
//                    final Word word = mw.getWord();
//                    final ScoreCalculation c = scribbleBoard.getScoreEngine().calculateWordScore(word, scribbleBoard);
//                    wordsStore.add(recordDef.createRecord(String.valueOf(number),
//                            new Object[]{word.toStringWord(), mw, c.getPoints(), ""})
//                    );
//                    if (wordId < number) {
//                        wordId = number + 1;
//                    }
//                }
//                wordsStore.commitChanges();
//            }
//        });
//    }
//
//    /**
//     * Adds specified word to this memory and returns it's registration number.
//     * <p/>
//     * If word is already added nothing is happend and exist registration number is returned.
//     *
//     * @param word the word to be added.
//     * @return the registration number.
//     */
//    public int addWord(final Word word) {
//        final int number = ++wordId;
//        final MemoryWord mw = new MemoryWord(number, word);
//
//        memoryWordsService.addMemoryWord(playboardItemBean.getBoardId(), mw, new AsyncCallback<Void>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(Void aVoid) {
//                final ScoreCalculation c = scribbleBoard.getScoreEngine().calculateWordScore(word, scribbleBoard);
//                wordsStore.add(recordDef.createRecord(String.valueOf(number),
//                        new Object[]{word.toStringWord(), mw, c.getPoints(), ""})
//                );
//                wordsStore.commitChanges();
//            }
//        });
//        return number;
//    }
//
//    /**
//     * Removes word with specified number from memory. If word with specified number is not exist
//     * nothing is happend.
//     *
//     * @param wordId the registration id of the word.
//     */
//    public void removeWord(int wordId) {
//        final Record record = wordsStore.getById(String.valueOf(wordId));
//        final MemoryWord mw = (MemoryWord) record.getAsObject("memoryWord");
//        if (mw == null) {
//            return;
//        }
//
//        memoryWordsService.removeMemoryWord(playboardItemBean.getBoardId(), mw.getNumber(), new AsyncCallback<Void>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(Void aVoid) {
//                wordsStore.remove(record);
//                wordsStore.commitChanges();
//            }
//        });
//    }
//
//    /**
//     * Removes all words from memory. If memory does not contains any words nothing is happend.
//     */
//    public void clearMemoryWords() {
//        if (wordsStore.getCount() == 0) {
//            return;
//        }
//
//        memoryWordsService.clearMemoryWords(playboardItemBean.getBoardId(), new AsyncCallback<Void>() {
//            public void onFailure(Throwable throwable) {
//                ExceptionHandler.showSystemError(throwable);
//            }
//
//            public void onSuccess(Void aVoid) {
//                wordsStore.removeAll();
//                wordsStore.commitChanges();
//            }
//        });
//    }
//
//    /**
//     * Returns word by it's registration id.
//     *
//     * @param wordId the registration word id.
//     * @return the word by specified id or {@code null} if word with specified registration is not exist.
//     */
//    public Word getWord(int wordId) {
//        final Record record = wordsStore.getById(String.valueOf(wordId));
//        final MemoryWord mw = (MemoryWord) record.getAsObject("memoryWord");
//        if (mw != null) {
//            return mw.getWord();
//        }
//        return null;
//    }
//
//    @Override
//    public void setDisabled(boolean disabled) {
//        if (this.disabled == disabled) {
//            return;
//        }
//        this.disabled = disabled;
//
//        if (!disabled && scribbleBoard.getSelectedWord() != null) {
//            rememberWord.setDisabled(false);
//        } else {
//            rememberWord.setDisabled(true);
//        }
//
//        if (!disabled && wordsStore.getCount() != 0) {
//            clearMemory.setDisabled(false);
//        } else {
//            clearMemory.setDisabled(true);
//        }
//    }
//
//    /**
//     * This listener extends {@code StoreListener} and updates state of {@code ClearMemory} button after
//     * {@code Store} was changed.
//     */
//    private class TheClearMemoryListener extends StoreListenerAdapter {
//        @Override
//        public void onAdd(Store store, Record[] records, int i) {
//            clearMemory.setDisabled(false);
//        }
//
//        @Override
//        public void onClear(Store store) {
//            clearMemory.setDisabled(true);
//        }
//
//        @Override
//        public void onDataChanged(Store store) {
//            clearMemory.setDisabled(wordsStore.getCount() == 0);
//        }
//
//        @Override
//        public void onRemove(Store store, Record record, int i) {
//            clearMemory.setDisabled(wordsStore.getCount() == 0);
//        }
//
//        @Override
//        public void onUpdate(Store store, Record record, Record.Operation operation) {
//            clearMemory.setDisabled(wordsStore.getCount() == 0);
//        }
//    }
//
//    private class ActionRenderer implements Renderer {
//        public String render(Object o, CellMetadata cellMetadata, Record record, int i, int i1, Store store) {
//            final String boardId = String.valueOf(playboardItemBean.getBoardId());
//            final String wordId = record.getId();
//            return "<div>" +
//                    "<a href=\"javascript: playboardSelectWord(" + boardId + ", " + wordId + ");\"><img ext:qtip=\"" + PB.ttpRestoreWord() + "\" src=\"" + GWT.getModuleBaseURL() + "images/playboard/selectWord.png\"/></a>" +
//                    "&nbsp;&nbsp;" +
//                    "<a href=\"javascript: playboardRemoveWord(" + boardId + ", " + wordId + ");\"><img ext:qtip=\"" + PB.ttpRemoveWord() + "\" src=\"" + GWT.getModuleBaseURL() + "images/playboard/removeWord.png\"/></a>" +
//                    "</div>";
//        }
//    }
}
