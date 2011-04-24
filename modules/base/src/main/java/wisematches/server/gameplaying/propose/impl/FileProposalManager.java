package wisematches.server.gameplaying.propose.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import wisematches.server.gameplaying.board.GameSettings;
import wisematches.server.gameplaying.propose.GameProposal;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class FileProposalManager<S extends GameSettings> extends AbstractProposalManager<S> implements Closeable {
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    private static final Log log = LogFactory.getLog("wisematches.server.gameplaying.proposal");

    public FileProposalManager() {
    }

    @Override
    @SuppressWarnings("unchecked")
    protected Collection<GameProposal<S>> loadGameProposals() {
        try {
            return (Collection<GameProposal<S>>) inputStream.readObject();
        } catch (IOException ex) {
            log.error("File proposal can't be loaded", ex);
        } catch (ClassNotFoundException ex) {
            log.error("File proposal can't be loaded", ex);
        }
        return Collections.emptyList();
    }

    @Override
    protected void storeGameProposal(GameProposal<S> sGameProposal) {
        try {
            outputStream.reset();
            outputStream.writeObject(getActiveProposals());
        } catch (IOException ex) {
            log.error("File proposal can't be stored", ex);
        }
    }

    @Override
    protected void removeGameProposal(GameProposal<S> sGameProposal) {
        try {
            outputStream.reset();
            outputStream.writeObject(getActiveProposals());
        } catch (IOException ex) {
            log.error("File proposal can't be stored", ex);
        }
    }

    public void setProposalsResource(Resource resource) throws IOException {
        final URL url = resource.getURL();
        final URLConnection connection = url.openConnection();

        inputStream = new ObjectInputStream(connection.getInputStream());
        outputStream = new ObjectOutputStream(connection.getOutputStream());
    }

    @Override
    public void close() throws IOException {
        inputStream.close();
        outputStream.close();
    }
}
