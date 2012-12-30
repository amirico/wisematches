package wisematches.server.web.controllers.info;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.personality.Language;
import wisematches.personality.player.Player;
import wisematches.personality.player.computer.robot.RobotPlayer;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.dictionary.Vocabulary;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.Word;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;
import wisematches.playground.scribble.robot.ScribbleRobotBrain;
import wisematches.playground.scribble.settings.BoardSettingsManager;
import wisematches.server.web.controllers.WisematchesController;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/info")
public class BoardExampleController extends WisematchesController {
    private TilesBankingHouse tilesBankingHouse;
    private DictionaryManager dictionaryManager;
    private BoardSettingsManager boardSettingsManager;

    private final Map<Language, BoardWrapper> boardsCache = new HashMap<>();

    public BoardExampleController() {
    }

    @RequestMapping("/move")
    public String showHowToMove(final @RequestParam(value = "plain", required = false) String plain, final Model model, final Locale locale) throws Exception {
        final Language language = Language.byLocale(locale);

        BoardWrapper boardWrapper = boardsCache.get(language);
        if (boardWrapper == null || !boardWrapper.getBoard().isGameActive()) {
            boardWrapper = createNewBoard(language);
            boardsCache.put(language, boardWrapper);
        }

        model.addAttribute("infoId", "move");
        model.addAttribute("viewMode", false);

        model.addAttribute("board", boardWrapper.getBoard());
        model.addAttribute("boardSettings", boardSettingsManager.getScribbleSettings(getPersonality()));

        model.addAttribute("player", RobotPlayer.getComputerPlayer(boardWrapper.getBoard().getPlayerTurn().getPlayerId()));
        model.addAttribute("memoryWords", selectMemoryWords(boardWrapper.getAvailableMoves()));

        if (plain != null) {
            return "/content/info/move";
        } else {
            model.addAttribute("resourceTemplate", "/content/info/move.ftl");
            return "/content/info/help";
        }
    }

    private BoardWrapper createNewBoard(Language language) {
        final List<Player> players = Arrays.<Player>asList(RobotPlayer.DULL, RobotPlayer.EXPERT);
        final Dictionary dictionary = dictionaryManager.getDictionary(language);
        final Vocabulary vocabulary = dictionary.getDefaultVocabulary();
        final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, players.size(), true);

        final ScribbleSettings settings = new ScribbleSettings("Example Scribble Board", language, vocabulary.getCode(), 7, false, false);
        final ScribbleBoard board = new ScribbleBoard(settings, players, tilesBank, vocabulary);

        final ScribbleRobotBrain robotBrain = new ScribbleRobotBrain();
        for (int i = 0; i < 3; i++) {
            RobotPlayer rp = RobotPlayer.getComputerPlayer(board.getPlayerTurn().getPlayerId());
            robotBrain.putInAction(board, rp.getRobotType());
        }

        final List<Word> allMoves = robotBrain.getAvailableMoves(board, board.getPlayerTurn());
        final SortedSet<Word> uniqueMoves = new TreeSet<>(new Comparator<Word>() {
            @Override
            public int compare(Word o1, Word o2) {
                return o1.getText().compareTo(o2.getText());
            }
        });
        uniqueMoves.addAll(allMoves);
        return new BoardWrapper(board, uniqueMoves);
    }

    private Collection<Word> selectMemoryWords(List<Word> words) {
        if (words.size() > 3) {
            return Arrays.asList(words.get(0), words.get(words.size() / 2), words.get(words.size() - 1));
        } else {
            return words;
        }
    }

    @Autowired
    public void setTilesBankingHouse(TilesBankingHouse tilesBankingHouse) {
        this.tilesBankingHouse = tilesBankingHouse;
    }

    @Autowired
    public void setDictionaryManager(DictionaryManager dictionaryManager) {
        this.dictionaryManager = dictionaryManager;
    }

    @Autowired
    public void setBoardSettingsManager(BoardSettingsManager boardSettingsManager) {
        this.boardSettingsManager = boardSettingsManager;
    }

    private static final class BoardWrapper {
        private final ScribbleBoard board;
        private final List<Word> availableMoves;

        private BoardWrapper(ScribbleBoard board, Collection<Word> moves) {
            this.board = board;
            availableMoves = new ArrayList<>(moves);
            Collections.shuffle(availableMoves);
        }

        public ScribbleBoard getBoard() {
            return board;
        }

        public List<Word> getAvailableMoves() {
            return availableMoves;
        }
    }
}
