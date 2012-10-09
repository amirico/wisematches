package wisematches.playground.scribble.bank.impl;

import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import wisematches.personality.Language;
import wisematches.playground.scribble.bank.LettersDistribution;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * This implementation of {@code TilesBankingHouse} loaders tiles from XML file of following structure:
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
public class XMLTilesBankingHouse extends AbstractTilesBankingHouse {
	private Resource banksPath;

	private static final DocumentBuilderFactory BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

	public XMLTilesBankingHouse() {
	}

	@Override
	protected LettersDistribution loadTilesBankInfo(Language language, int playersCount, boolean wildcardAllowed) throws Exception {
		final DocumentBuilder builder = BUILDER_FACTORY.newDocumentBuilder();
		final Document document1 = builder.parse(banksPath.createRelative("bank_" + language.code() + ".xml").getInputStream());

		final NodeList list = document1.getElementsByTagName("letter");
		final TilesBankInfoEditor info = new TilesBankInfoEditor(language);
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

			info.add(ch, resultCount, cost);
		}
		return info.createTilesBankInfo();
	}

	public void setBanksPath(Resource banksPath) {
		this.banksPath = banksPath;
	}
}
