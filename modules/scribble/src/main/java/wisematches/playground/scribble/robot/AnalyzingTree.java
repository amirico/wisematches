package wisematches.playground.scribble.robot;

import wisematches.playground.scribble.*;

import java.util.*;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class AnalyzingTree {
	private String currentWord = "";

	private int currentLevel = 0;

	private final boolean firstMove;
	private final List<WorkTile> tiles;
	private final TilesPlacement tilesPlacement;

	private static final Random RANDOM = new Random();

	private List<AnalyzingTreeItem> treeItemList;
	private Deque<List<AnalyzingTreeItem>> levels = new LinkedList<List<AnalyzingTreeItem>>();

	AnalyzingTree(TilesPlacement tilesPlacement, List<WorkTile> tiles) {
		this.tilesPlacement = tilesPlacement;
		this.tiles = tiles;

		boolean first = true;
		for (WorkTile tile : tiles) {
			if (tile.getPosition() != null) {
				first = false;
				break;
			}
		}
		this.firstMove = first;

		treeItemList = new ArrayList<AnalyzingTreeItem>();
	}

	public boolean offerNextChar(char ch) {
		List<AnalyzingTreeItem> newLevel = new ArrayList<AnalyzingTreeItem>();
		if (currentLevel == 0) {
			for (WorkTile tile : tiles) {
				if (tile.getTile().getLetter() == ch) {
					newLevel.add(new AnalyzingTreeItem(null, tile, null));
				} else if (tile.getTile().isWildcard() && tile.getPosition() == null) {
					newLevel.add(new AnalyzingTreeItem(null, tile, tile.getTile().redefine(ch), null));
				}
			}
		} else {
			for (WorkTile tile : tiles) {
				Tile originalTile = tile.getTile();
				if (originalTile.isWildcard() && tile.getPosition() == null) {
					originalTile = originalTile.redefine(ch);
				}

				if (originalTile.getLetter() != ch) {
					continue;
				}

				final Position tilePosition = tile.getPosition();

				for (AnalyzingTreeItem item : treeItemList) {
					if (item.containsWorkTile(tile)) {
						continue;
					}

					if (item.isPlaced()) { //Item is already placed
						if (tilePosition != null) { //Tile has a position
							if (item.direction == null) { //But word doesn't have direction yet
								if (item.row == tilePosition.row - 1 && item.column == tilePosition.column) { // and tile is placed below item
									//when new direction is vertical
									newLevel.add(new AnalyzingTreeItem(item, tile, originalTile, Direction.VERTICAL));
								} else if (item.column == tilePosition.column - 1 && item.row == tilePosition.row) { // but if tile placed on the right
									//when new direction is horizontal
									newLevel.add(new AnalyzingTreeItem(item, tile, originalTile, Direction.HORIZONTAL));
								}
							} else { // If item has direction check that item in the right direction
								if (item.direction == Direction.HORIZONTAL &&
										(item.row != tilePosition.row || item.column + 1 != tilePosition.column)) {
									continue;
								}
								if (item.direction == Direction.VERTICAL &&
										(item.row + 1 != tilePosition.row || item.column != tilePosition.column)) {
									continue;
								}
								newLevel.add(new AnalyzingTreeItem(item, tile, originalTile, item.direction));
							}
						} else { //Tile doesn't have position: it's hand tile.
							//near cell is free by horizontal
							if (item.column + 1 < ScribbleBoard.CELLS_NUMBER &&
									tilesPlacement.getBoardTile(item.row, item.column + 1) == null &&
									(item.direction == null || item.direction == Direction.HORIZONTAL)) {
								newLevel.add(new AnalyzingTreeItem(item, tile, originalTile, Direction.HORIZONTAL));
							}

							//near cell is free by vertical
							if (item.row + 1 < ScribbleBoard.CELLS_NUMBER &&
									tilesPlacement.getBoardTile(item.row + 1, item.column) == null &&
									(item.direction == null || item.direction == Direction.VERTICAL)) {
								newLevel.add(new AnalyzingTreeItem(item, tile, originalTile, Direction.VERTICAL));
							}
						}
					} else { //item is not placed.
						if (tilePosition != null) { //Tile has position.
							// Check that word can be placed vertically and horizontally
							int index = 1;

							boolean vertical = true;
							boolean horizontal = true;
							AnalyzingTreeItem i = item;
							while (i != null) {
								if (tilePosition.row - index < 0 ||
										tilesPlacement.getBoardTile(tilePosition.row - index, tilePosition.column) != null) {
									vertical = false;
								}
								if (tilePosition.column - index < 0 ||
										tilesPlacement.getBoardTile(tilePosition.row, tilePosition.column - index) != null) {
									horizontal = false;
								}
								index++;
								i = i.parent;
							}

							if (vertical) {
								newLevel.add(new AnalyzingTreeItem(item, tile, originalTile, Direction.VERTICAL));
							}
							if (horizontal) {
								newLevel.add(new AnalyzingTreeItem(item, tile, originalTile, Direction.HORIZONTAL));
							}
						} else {
							newLevel.add(new AnalyzingTreeItem(item, tile, originalTile, null));
						}
					}
				}
			}
		}

		levels.addFirst(treeItemList);

		treeItemList = newLevel;
		currentLevel++;
		currentWord += ch;
		return treeItemList.size() != 0;
	}

	public void rollback() {
		treeItemList.clear();
		treeItemList = levels.removeFirst();

		currentLevel--;
		currentWord = currentWord.substring(0, currentWord.length() - 1);
	}

	public void rollback(int level) {
		for (int i = currentLevel; i > level; i--) {
			rollback();
		}
	}

	public String getCurrentWord() {
		return currentWord;
	}

	public int getCurrentLevel() {
		return currentLevel;
	}

	public List<Word> getAcceptableWords() {
		if (treeItemList.size() == 0) {
			return Collections.emptyList();
		}

		final List<Word> res = new ArrayList<Word>(treeItemList.size());
		for (AnalyzingTreeItem item : treeItemList) {
			final Word word = item.getWord(firstMove);
			if (word != null) {
				res.add(word);
			}
		}
		return res;
	}

	static class AnalyzingTreeItem {
		private final int level;
		private final Tile rightTile;
		private final WorkTile workTile;
		private final AnalyzingTreeItem parent;

		private int row = -1;
		private int column = -1;
		private final Direction direction;

		AnalyzingTreeItem(AnalyzingTreeItem parent, WorkTile workTile, Direction direction) {
			this(parent, workTile, workTile.getTile(), direction);
		}

		AnalyzingTreeItem(AnalyzingTreeItem parent, WorkTile workTile, Tile rightTile, Direction direction) {
			this.rightTile = rightTile;

			this.level = (parent != null ? parent.level + 1 : 0);
			this.workTile = workTile;
			this.parent = parent;
			this.direction = direction;

			final Position position = workTile.getPosition();

			if (parent == null || !parent.isPlaced()) {
				if (position != null) {
					row = position.row;
					column = position.column;
				}
			} else {
				if (direction == Direction.VERTICAL) {
					row = parent.row + 1;
					column = parent.column;
				} else if (direction == Direction.HORIZONTAL) {
					row = parent.row;
					column = parent.column + 1;
				}
			}
		}

		int getLevel() {
			return level;
		}

		WorkTile getWorkTile() {
			return workTile;
		}

		AnalyzingTreeItem getParent() {
			return parent;
		}

		Direction getDirection() {
			return direction;
		}

		int getRow() {
			return row;
		}

		int getColumn() {
			return column;
		}

		boolean containsWorkTile(WorkTile workTile) {
			AnalyzingTreeItem item = this;
			while (item != null) {
				if (item.workTile == workTile) {
					return true;
				}
				item = item.parent;
			}
			return false;
		}

		boolean isPlaced() {
			return row != -1 && column != -1;
		}

		Word getWord(boolean firstMove) {
			AnalyzingTreeItem item = this;

			boolean oneFromHand = false;
			boolean oneFromBoard = false;
			final Tile[] tiles = new Tile[level + 1];
			for (int i = tiles.length - 1; i >= 0; i--) {
				tiles[i] = item.rightTile;

				if (item.workTile.getPosition() == null) {
					oneFromHand = true;
				} else {
					oneFromBoard = true;
				}
				item = item.parent;
			}

			if (firstMove) {
				if (direction != null || tiles.length > ScribbleBoard.CELLS_NUMBER) {
					return null;
				}
				int x = ScribbleBoard.CENTER_CELL;
				int y = ScribbleBoard.CENTER_CELL;
				final boolean vertical = RANDOM.nextBoolean();
				final Direction direction = vertical ? Direction.VERTICAL : Direction.HORIZONTAL;

				final int startPosition = ScribbleBoard.CENTER_CELL - tiles.length + 1;
				if (vertical) {
					x = startPosition + RANDOM.nextInt(tiles.length);
				} else {
					y = startPosition + RANDOM.nextInt(tiles.length);
				}
				return new Word(new Position(x, y), direction, tiles);
			} else {
				// No tiles from hand or board
				if (!oneFromBoard || !oneFromHand) {
					return null;
				}

				int x = row - (direction == Direction.VERTICAL ? tiles.length - 1 : 0);
				int y = column - (direction == Direction.HORIZONTAL ? tiles.length - 1 : 0);
				if (direction == null || x < 0 || y < 0 || row >= ScribbleBoard.CELLS_NUMBER || column >= ScribbleBoard.CELLS_NUMBER) {
					return null;
				}
				return new Word(new Position(x, y), direction, tiles);
			}
		}
	}
}
