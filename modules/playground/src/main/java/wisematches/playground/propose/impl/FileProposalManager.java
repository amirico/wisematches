package wisematches.playground.propose.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import wisematches.playground.GameSettings;
import wisematches.playground.propose.GameProposal;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileProposalManager<S extends GameSettings> extends AbstractProposalManager<S> implements Closeable {
	private FileChannel proposalFile;

	private static final Log log = LogFactory.getLog("wisematches.server.playground.proposal");

	public FileProposalManager() {
	}

	@Override
	@SuppressWarnings("unchecked")
	protected Collection<GameProposal<S>> loadGameProposals() {
		try {
			if (proposalFile.size() == 0) {
				return Collections.emptyList();
			}
			proposalFile.position(0);
			final ObjectInputStream inputStream = new ObjectInputStream(Channels.newInputStream(proposalFile));
			int count = inputStream.readInt();
			if (count == 0) {
				return Collections.emptyList();
			}

			final Collection<GameProposal<S>> res = new ArrayList<GameProposal<S>>(count);
			while (count-- != 0) {
				res.add((GameProposal<S>) inputStream.readObject());
			}
			return res;
		} catch (EOFException ex) {
			log.error("File proposal can't be loaded", ex);
		} catch (IOException ex) {
			log.error("File proposal can't be loaded", ex);
		} catch (ClassNotFoundException ex) {
			log.error("File proposal can't be loaded", ex);
		}
		return Collections.emptyList();
	}

	@Override
	protected void storeGameProposal(GameProposal<S> sGameProposal) {
		saveAllProposals();
	}

	@Override
	protected void removeGameProposal(GameProposal<S> sGameProposal) {
		saveAllProposals();
	}

	private void saveAllProposals() {
		try {
			final Collection<GameProposal<S>> activeProposals = getActiveProposals();
			proposalFile.position(0);
			final ObjectOutputStream outputStream = new ObjectOutputStream(Channels.newOutputStream(this.proposalFile));
			outputStream.writeInt(activeProposals.size());
			for (GameProposal proposal : activeProposals) {
				outputStream.writeObject(proposal);
			}
			outputStream.flush();
		} catch (IOException ex) {
			log.error("File proposal can't be stored", ex);
		}
	}

	public void setProposalsResource(File proposalFile) throws IOException {
		if (!proposalFile.exists()) {
			proposalFile.createNewFile();
		}
		this.proposalFile = new RandomAccessFile(proposalFile, "rw").getChannel();
	}

	@Override
	public void close() throws IOException {
		proposalFile.close();
	}
}
