package wisematches.playground.propose.impl;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ExpiredProposalsTerminator {
/*
    private TaskScheduler taskScheduler;
    private AbstractProposalManager<?> proposalManager;

    private final Lock lock = new ReentrantLock();
    private final Map<Long, ScheduledFuture<?>> features = new HashMap<Long, ScheduledFuture<?>>();
    private final TheGameProposalListener proposalListener = new TheGameProposalListener();

    private static final Log log = LogFactory.getLog("wisematches.server.playground.terminator.proposal");

    public ExpiredProposalsTerminator() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lock.lock();
        try {
            final List<? extends GameProposal<? extends GameSettings>> proposals = proposalManager.searchEntities(null, ProposalRelation.AVAILABLE, null, null, null);
            for (GameProposal<? extends GameSettings> proposal : proposals) {
                processProposalInitiated(proposal);
            }
        } finally {
            lock.unlock();
        }
    }

    protected void processProposalInitiated(GameProposal<?> proposal) {
        if (proposal.getProposalType() != ProposalType.CHALLENGE) {
            return;
        }

        final Date dueDate = new Date(proposal.getCreationDate().getTime() +);
        final ScheduledFuture schedule = taskScheduler.schedule(new ProposalTerminationTask(proposal.getId()), dueDate);
        features.put(proposal.getId(), schedule);
    }

    protected void processProposalFinalized(GameProposal<?> proposal) {
        if (proposal.getProposalType() != ProposalType.CHALLENGE) {
            return;
        }
        ScheduledFuture<?> remove = features.remove(proposal.getId());
        if (remove != null) {
            remove.cancel(false);
        }
    }

    public void setTaskScheduler(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    public void setProposalManager(AbstractProposalManager<?> proposalManager) {
        if (proposalManager != null) {
            proposalManager.removeGameProposalListener(proposalListener);
        }

        this.proposalManager = proposalManager;

        if (proposalManager != null) {
            proposalManager.addGameProposalListener(proposalListener);
        }
    }

    private class ProposalTerminationTask implements Runnable {
        private final long proposal;

        private ProposalTerminationTask(long proposal) {
            this.proposal = proposal;
        }

        @Override
        public void run() {
            lock.lock();
            try {
                proposalManager.terminate(proposal);
            } finally {
                lock.unlock();
            }
        }
    }

    private class TheGameProposalListener implements GameProposalListener {
        private TheGameProposalListener() {
        }

        @Override
        public void gameProposalInitiated(GameProposal proposal) {
            lock.lock();
            try {
                processProposalInitiated(proposal);
            } finally {
                lock.unlock();
            }
        }

        @Override
        public void gameProposalUpdated(GameProposal proposal, Personality player, ProposalDirective directive) {
        }

        @Override
        public void gameProposalFinalized(GameProposal proposal, Personality player, ProposalResolution reason) {
            lock.lock();
            try {
                processProposalFinalized(proposal);
            } finally {
                lock.unlock();
            }
        }
    }
*/
}
