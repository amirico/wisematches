package wisematches.playground.propose.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.scheduling.TaskScheduler;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.ProposalResolution;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.GameProposalListener;

import java.util.Collection;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class ExpiredProposalsTerminator implements InitializingBean {
    private TaskScheduler taskScheduler;
    private AbstractProposalManager<?> proposalManager;

    private final Lock lock = new ReentrantLock();
    private final TheGameProposalListener proposalListener = new TheGameProposalListener();

    private static final Log log = LogFactory.getLog("wisematches.server.playground.terminator.proposal");

    public ExpiredProposalsTerminator() {
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        lock.lock();
        try {
            final Collection<? extends GameProposal<? extends GameSettings>> proposals = proposalManager.getActiveProposals();
            for (GameProposal<? extends GameSettings> proposal : proposals) {
                processProposalInitiated(proposal);
            }
        } finally {
            lock.unlock();
        }
    }

    protected void processProposalInitiated(GameProposal<?> proposal) {

    }

    protected void processProposalClosed(GameProposal<?> proposal) {

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

    private class TheGameProposalListener implements GameProposalListener {
        private TheGameProposalListener() {
        }

        @Override
        public void gameProposalInitiated(GameProposal proposal) {
            processProposalInitiated(proposal);
        }

        @Override
        public void gameProposalUpdated(GameProposal proposal) {
        }

        @Override
        public void gameProposalFinalized(GameProposal proposal, ProposalResolution reason) {
        }
    }
}
