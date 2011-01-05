package wisematches.client.gwt.app.client.content.playboard.infos;

import com.smartgwt.client.widgets.Canvas;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class PlayersInfoPanel extends Canvas {
/*	private PlayerInfoPanel[] infoPanels;

	private PlayerInfoPanel activePlayerInfo;

	private final PlayboardItemBean playboardItemBean;

	public PlayersInfoPanel(PlayboardItemBean playboardItemBean) {
		super(PB.ttlPlayersInfo());
		this.playboardItemBean = playboardItemBean;
		playboardItemBean.addPropertyChangeListener(new ThePropertyChangeListener());
		initPanel();
		updatePlayerMove();
	}

	private void initPanel() {
		setFrame(true);
		setCollapsible(true);

		setLayout(new RowLayout());

		final PlayerInfoBean[] playerInfoBeans = playboardItemBean.getPlayers();
		final int[] tilesCount = playboardItemBean.getPlayersTilesCount();

		infoPanels = new PlayerInfoPanel[playerInfoBeans.length];
		for (int i = 0; i < playerInfoBeans.length; i++) {
			final PlayerInfoBean playerInfoBean = playerInfoBeans[i];

			final PlayerInfoPanel infoPanel = new PlayerInfoPanel(playerInfoBean);
			infoPanel.setHandTilesCount(tilesCount[i]);
			infoPanels[i] = infoPanel;

			add(infoPanels[i]);
		}
	}

	private void updatePlayerMove() {
		final GameboardItemBean.GameState state = playboardItemBean.getGameState();
		// If not running - disable all
		if (state == GameboardItemBean.GameState.WAITING || state == GameboardItemBean.GameState.FINISHED) {
			for (PlayerInfoPanel pip : infoPanels) {
				pip.setDisabled(true);
			}
		} else {
			final PlayerInfoBean player = playboardItemBean.getPlayerInfoBean(playboardItemBean.getPlayerMove());
			if (activePlayerInfo != null) {
				activePlayerInfo.setDisabled(true);
			}

			activePlayerInfo = null;

			if (player != null) {
				for (PlayerInfoPanel pip : infoPanels) {
					if (pip.playerInfoBean != null && pip.playerInfoBean.getPlayerId() == player.getPlayerId()) {
						activePlayerInfo = pip;
						activePlayerInfo.setDisabled(false);
					}
				}
			}
		}
	}

	private class ThePropertyChangeListener implements PropertyChangeListener<PlayboardItemBean> {
		public void propertyChanged(PlayboardItemBean bean, String property, Object oldValue, Object newValue) {
			if ("playerMove".equals(property)) {
				updatePlayerMove();

				for (PlayerInfoPanel panel : infoPanels) {
					if (panel != null) {
						panel.updateScore();
					}
				}
			} else if ("gameState".equals(property)) {
				updatePlayerMove();
			} else if ("playerScorePoints".equals(property)) {
				for (PlayerInfoPanel panel : infoPanels) {
					if (panel != null) {
						panel.updateScore();
					}
				}
			} else if ("playersTilesCount".equals(property)) {
				int index = 0;
				final int[] tilesCount = bean.getPlayersTilesCount();
				for (int count : tilesCount) {
					final PlayerInfoPanel panel = infoPanels[index++];
					if (panel != null) {
						panel.setHandTilesCount(count);
					}
				}
			} else if ("players".equals(property)) {
				int index = 0;
				final PlayerInfoBean[] playerInfoBeans = bean.getPlayers();
				for (PlayerInfoBean playerInfoBean : playerInfoBeans) {
					final PlayerInfoPanel panel = infoPanels[index++];
					panel.setPlayerInfoBean(playerInfoBean);
				}
			}
		}
	}

	private static class PlayerInfoPanel extends Panel {
		private FlexTable table;
		private PlayerInfoBean playerInfoBean;
		private int tilesInHand;

		private PlayerInfoPanel(PlayerInfoBean playerInfoBean) {
			initPanel();
			setPlayerInfoBean(playerInfoBean);
			setDisabled(true);
		}

		private void initPanel() {
			setBodyBorder(true);
			setHeight(50);
			setWidth(250);

			table = new FlexTable();
			table.setStyleName("player-info-table");
			table.setWidth("100%");
			table.setCellPadding(0);
			table.setCellSpacing(0);
			table.setBorderWidth(0);
			add(table);

			final FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
			formatter.setRowSpan(0, 0, 3);
			formatter.setVerticalAlignment(0, 0, HasAlignment.ALIGN_TOP);
		}

		public void setPlayerInfoBean(PlayerInfoBean player) {
			this.playerInfoBean = player;

			final Image image;
			if (player != null) {
				image = new Image(player.getPlayerIconUrl());
			} else {
				image = new Image(GWT.getModuleBaseURL() + "/images/player/noPlayerIcon.png");
			}

			table.setHTML(0, 1, PlayerGridRenderer.getPlayerInfoHtml(player, APP.lblNoOpponent(), false, true, ""));
			table.setWidget(0, 0, image);

			updateScore();
		}

		public void updateScore() {
			if (playerInfoBean != null) {
				table.setHTML(1, 0, PB.lblScore() + ": " + playerInfoBean.getCurrentRating() + " " + PB.lblPoints());
			} else {
				table.setHTML(1, 0, PB.lblScore() + ": " + APP.lblNA());
			}
		}

		public void setHandTilesCount(int tiles) {
			if (this.tilesInHand == tiles) {
				return;
			}

			this.tilesInHand = tiles;
			table.setHTML(2, 0, PB.lblTilesInHand() + ": " + tiles);
		}

		public int getHandTilesCount() {
			return tilesInHand;
		}

		public PlayerInfoBean getPlayerInfoBean() {
			return playerInfoBean;
		}

		@Override
		public void setDisabled(boolean b) {
			if (isDisabled() == b) {
				return;
			}

			super.setDisabled(b);

			final FlexTable.FlexCellFormatter formatter = table.getFlexCellFormatter();
			if (!b) {
				formatter.addStyleName(0, 0, "player-info-active");
			} else {
				formatter.removeStyleName(0, 0, "player-info-active");
			}
		}

	}*/
}