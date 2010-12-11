package wisematches.server.games.scribble.bank.impl;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import wisematches.server.games.scribble.bank.TilesBank;
import wisematches.server.games.scribble.bank.TilesBankingHouse;
import wisematches.server.games.scribble.bank.UnsupportedLocaleException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Locale;

/**
 * This implementation of {@code TilesBankingHouse} loades tiles from XML file of following structure:
 * <pre>
 * &lt;?xml version="1.0" encoding="UTF-8"?&gt;
 * &lt;bank version="1.0"&gt;
 *    &lt;letter char="A" count="9" cost="1"/&gt;
 *    ...
 *    &lt;letter char="Z" count="1" cost="10"/&gt;
 * &lt;/bank&gt;
 * </pre>
 * <p/>
 * Special letter '*' indicates wildcard tile.
 * <p/>
 * This implementations ignores number of players.
 *
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class XMLTilesBankingHouse implements TilesBankingHouse {
    private Resource banksPath;

    private static final DocumentBuilderFactory BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    public TilesBank createTilesBank(Locale locale, int playersCount, boolean wildcardAllowed) {
        try {
            final Resource bankFile = getBankResource(locale);
            final Collection<TilesBank.TilesInfo> infoCollection = getTilesInfos(bankFile, playersCount, wildcardAllowed);
            return new TilesBank(infoCollection.toArray(new TilesBank.TilesInfo[infoCollection.size()]));
        } catch (Exception ex) {
            throw new UnsupportedLocaleException("Tiles Bank for locale " + locale + " can't be loaded", ex);
        }
    }

    private Collection<TilesBank.TilesInfo> getTilesInfos(Resource resource, int playersCount, boolean wildcardAllowed)
            throws ParserConfigurationException, IOException, SAXException {
        final DocumentBuilder builder = BUILDER_FACTORY.newDocumentBuilder();

        final Document document1 = builder.parse(resource.getInputStream());

        final NodeList list = document1.getElementsByTagName("letter");
        Collection<TilesBank.TilesInfo> letters = new ArrayList<TilesBank.TilesInfo>(list.getLength());
        for (int i = 0; i < list.getLength(); i++) {
            final Element e = (Element) list.item(i);
            final char ch = Character.toLowerCase(e.getAttribute("char").charAt(0));
            final int count = Integer.parseInt(e.getAttribute("count"));
            final int cost = Integer.parseInt(e.getAttribute("cost"));
            if (ch == '*' && !wildcardAllowed) {
                continue;
            }

            final boolean multiple = Boolean.parseBoolean(e.getAttribute("forEachPlayer"));
            final int resultCount;
            if (multiple) {
                resultCount = count * playersCount;
            } else {
                resultCount = count;
            }

            letters.add(new TilesBank.TilesInfo(ch, resultCount, cost));
        }
        return letters;
    }

    private Resource getBankResource(Locale locale) throws IOException {
        return banksPath.createRelative("bank_" + locale.toString() + ".xml");
    }

    public void setBanksPath(Resource banksPath) {
        this.banksPath = banksPath;
    }
}
