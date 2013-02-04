package wisematches.playground.propose.impl.file;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.GameProposal;
import wisematches.playground.propose.ProposalRelation;
import wisematches.playground.propose.impl.AbstractGameProposal;
import wisematches.playground.propose.impl.AbstractProposalManager;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileProposalManager<S extends GameSettings> extends AbstractProposalManager<S> implements Closeable {
	private FileChannel proposalFile;
	private final Lock lock = new ReentrantLock();

	private static final Log log = LogFactory.getLog("wisematches.server.playground.proposal");

	public FileProposalManager() {
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Collection<AbstractGameProposal<S>> loadGameProposals() {
		lock.lock();
		try {
			if (!proposalFile.isOpen() || proposalFile.size() == 0) {
				return Collections.emptyList();
			}
			proposalFile.position(0);
			final ObjectInputStream inputStream = new ObjectInputStream(Channels.newInputStream(proposalFile));
			int count = inputStream.readInt();
			if (count == 0) {
				return Collections.emptyList();
			}

			final Collection<AbstractGameProposal<S>> res = new ArrayList<>(count);
			while (count-- != 0) {
				res.add((AbstractGameProposal<S>) inputStream.readObject());
			}
			return res;
		} catch (IOException | ClassNotFoundException ex) {
			log.error("File proposal can't be loaded", ex);
		} finally {
			lock.unlock();
		}
		return Collections.emptyList();
	}

	@Override
	protected void storeGameProposal(AbstractGameProposal<S> proposal) {
		saveAllProposals();
	}

	@Override
	protected void removeGameProposal(AbstractGameProposal<S> proposal) {
		saveAllProposals();
	}

	private void saveAllProposals() {
		lock.lock();
		try {
			final List<GameProposal<S>> activeProposals = searchEntities(null, ProposalRelation.AVAILABLE, null, null);
			proposalFile.position(0);
			final ObjectOutputStream outputStream = new ObjectOutputStream(Channels.newOutputStream(this.proposalFile));
			outputStream.writeInt(activeProposals.size());
			for (GameProposal proposal : activeProposals) {
				outputStream.writeObject(proposal);
			}
			outputStream.flush();
		} catch (IOException ex) {
			log.error("File proposal can't be stored", ex);
		} finally {
			lock.unlock();
		}
	}

	public void setProposalsResource(File proposalFile) throws IOException {
		lock.lock();
		try {
			if (!proposalFile.exists()) {
				proposalFile.createNewFile();
			}
			this.proposalFile = new RandomAccessFile(proposalFile, "rw").getChannel();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void close() throws IOException {
		lock.lock();
		try {
			if (proposalFile != null) {
				proposalFile.close();
			}
		} finally {
			lock.unlock();
		}
	}
}
