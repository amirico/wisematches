package wisematches.server.web.servlet.mvc.assistance;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import wisematches.core.*;
import wisematches.playground.dictionary.Dictionary;
import wisematches.playground.dictionary.DictionaryManager;
import wisematches.playground.scribble.ScribbleBoard;
import wisematches.playground.scribble.ScribbleSettings;
import wisematches.playground.scribble.Tile;
import wisematches.playground.scribble.Word;
import wisematches.playground.scribble.bank.TilesBank;
import wisematches.playground.scribble.bank.TilesBankingHouse;
import wisematches.playground.scribble.robot.ScribbleRobotBrain;
import wisematches.playground.scribble.settings.BoardSettingsManager;
import wisematches.server.web.servlet.mvc.WisematchesController;
import wisematches.server.web.servlet.sdo.scribble.board.BoardInfo;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
@Controller
@RequestMapping("/info")
public class ExampleController extends WisematchesController {
    private TilesBankingHouse tilesBankingHouse;
    private DictionaryManager dictionaryManager;
    private BoardSettingsManager boardSettingsManager;

    private final Map<Language, BoardWrapper> boardsCache = new HashMap<>();

    private final ObjectMapper jsonObjectConverter = new ObjectMapper();

    public ExampleController() {
    }

    @RequestMapping("/move")
    public String howToMovePage(final @RequestParam(value = "plain", required = false) String plain, final Model model, final Locale locale) throws Exception {
        final Language language = Language.byLocale(locale);
        final Personality personality = getPrincipal();

        BoardWrapper boardWrapper = boardsCache.get(language);
        if (boardWrapper == null || !boardWrapper.getBoard().isActive()) {
            boardWrapper = createNewBoard(language);
            boardsCache.put(language, boardWrapper);
        }

        model.addAttribute("viewMode", false);
        model.addAttribute("staticContentId", "move");

        final ScribbleBoard board = boardWrapper.getBoard();
        final Tile[] handTiles = board.getPlayerHand(board.getPlayerTurn()).getTiles();
        final BoardInfo info = new BoardInfo(board, handTiles, playerStateManager, messageSource, locale);
        model.addAttribute("boardInfo", info);
        model.addAttribute("boardInfoJSON", jsonObjectConverter.writeValueAsString(info));

        model.addAttribute("player", board.getPlayerTurn());
        model.addAttribute("memoryWords", selectMemoryWords(boardWrapper.getAvailableMoves()));

        if (personality == null || !(personality instanceof Member)) {
            model.addAttribute("boardSettings", boardSettingsManager.getDefaultSettings());
        } else {
            model.addAttribute("boardSettings", boardSettingsManager.getScribbleSettings((Player) personality));
        }

        if (plain != null) {
            return "/content/assistance/move";
        } else {
            model.addAttribute("resourceTemplate", "/content/assistance/move.ftl");
            return "/content/assistance/help";
        }
    }

    private BoardWrapper createNewBoard(Language language) {
        final List<Robot> players = new ArrayList<>();
        for (RobotType robotType : EnumSet.of(RobotType.DULL, RobotType.EXPERT)) {
            players.add(personalityManager.getRobot(robotType));
        }

        final Dictionary dictionary = dictionaryManager.getDictionary(language);
        final TilesBank tilesBank = tilesBankingHouse.createTilesBank(language, players.size(), true);

        final ScribbleSettings settings = new ScribbleSettings("Example Scribble Board", language, 7, false, false);
        final ScribbleBoard board = new ScribbleBoard(settings, players, tilesBank, dictionary);

        final ScribbleRobotBrain robotBrain = new ScribbleRobotBrain();
        for (int i = 0; i < 3; i++) {
            Robot rp = (Robot) board.getPlayerTurn();
            robotBrain.putInAction(board, rp.getRobotType());
        }

        final List<Word> allMoves = robotBrain.getAvailableMoves(board, board.getPlayerHand(board.getPlayerTurn()).getTiles());
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
