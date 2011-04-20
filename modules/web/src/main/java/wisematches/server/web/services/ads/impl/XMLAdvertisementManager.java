package wisematches.server.web.services.ads.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import wisematches.server.personality.account.Language;
import wisematches.server.web.services.ads.AdvertisementBlock;
import wisematches.server.web.services.ads.AdvertisementManager;
import wisematches.server.web.services.ads.AdvertisementProvider;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class XMLAdvertisementManager implements AdvertisementManager {
	private final Map<String, AdvertisementBlock> advertisementBlocks = new HashMap<String, AdvertisementBlock>();

	private static final Log log = LogFactory.getLog("wisematches.server.web.adds");

	private static final DocumentBuilderFactory BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

	public XMLAdvertisementManager() {
	}

	@Override
	public AdvertisementBlock getAdvertisementBlock(String name, Language language) {
		AdvertisementBlock advertisementBlock = advertisementBlocks.get(name + "_" + language);
		if (advertisementBlock == null) {
			log.error("No adds block for language " + language + " by name " + name);
		}
		return advertisementBlock;
	}

	public void setAdsResource(Resource resource) throws ParserConfigurationException, IOException, SAXException {
		advertisementBlocks.clear();

		if (resource != null && resource.exists() && resource.isReadable()) {
			final DocumentBuilder builder = BUILDER_FACTORY.newDocumentBuilder();
			final Document document = builder.parse(resource.getInputStream());

			final Element root = document.getDocumentElement();
			final String client = root.getAttribute("client");

			final NodeList blocks = root.getElementsByTagName("block");
			for (int i = 0; i < blocks.getLength(); i++) {
				final Element block = (Element) blocks.item(i);
				final String name = block.getAttribute("name");

				final NodeList items = block.getElementsByTagName("item");
				for (int j = 0; j < items.getLength(); j++) {
					final Element item = (Element) items.item(j);
					final String slot = item.getAttribute("slot");
					final Language language = Language.byCode(item.getAttribute("language"));
					final int width = Integer.valueOf(item.getAttribute("width"));
					final int height = Integer.valueOf(item.getAttribute("height"));

					advertisementBlocks.put(name + "_" + language, new AdvertisementBlock(client, slot, width, height, AdvertisementProvider.GOOGLE));
				}
			}
		}
	}
}
