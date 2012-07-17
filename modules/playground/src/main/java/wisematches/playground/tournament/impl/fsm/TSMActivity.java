package wisematches.playground.tournament.impl.fsm;

import wisematches.playground.task.AssuredTaskProcessor;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
enum TSMActivity {
	INITIATE_TOURNAMENT() {
		@Override
		void executeTask(TournamentStateMachine manager, TSMActivityContext context) {
//			manager.processInitiateTournament((TSMInitializingTournament) context);
		}
	},

	INITIATE_ROUND() {
		@Override
		void executeTask(TournamentStateMachine manager, TSMActivityContext context) {
//			manager.processInitiateRound((TSMInitializingRound) context);
		}
	},

	INITIATE_GROUP() {
		@Override
		void executeTask(TournamentStateMachine manager, TSMActivityContext context) {
//			manager.processInitiateGroup((TSMInitializingGroup) context);
		}
	},

	FINALIZE_GAME() {
		@Override
		void executeTask(TournamentStateMachine manager, TSMActivityContext context) {
//			manager.processFinalizeGame((TSMFinalizingGame) context);
		}
	},

	FINALIZE_GROUP() {
		@Override
		void executeTask(TournamentStateMachine manager, TSMActivityContext context) {
//			manager.processFinalizeGroup((TSMFinalizingGroup) context);
		}
	},

	FINALIZE_ROUND() {
		@Override
		void executeTask(TournamentStateMachine manager, TSMActivityContext context) {
//			manager.processFinalizeRound((TSMFinalizingRound) context);
		}
	},

	FINALIZE_TOURNAMENT() {
		@Override
		void executeTask(TournamentStateMachine manager, TSMActivityContext context) {
//			manager.processFinalizeTournament((TSMFinalizingTournament) context);
		}
	};

	protected static AssuredTaskProcessor<TSMActivity, TSMActivityContext> createTaskProcessor(TournamentStateMachine manager) {
		return new TheAssuredTaskProcessor(manager);
	}

	abstract void executeTask(TournamentStateMachine manager, TSMActivityContext context);

	private static class TheAssuredTaskProcessor implements AssuredTaskProcessor<TSMActivity, TSMActivityContext> {
		private final TournamentStateMachine manager;

		private TheAssuredTaskProcessor(TournamentStateMachine manager) {
			this.manager = manager;
		}

		@Override
		public void processAssuredTask(TSMActivity activity, TSMActivityContext activityContext) {
			activity.executeTask(manager, activityContext);
		}
	}
}
