package wisematches.client.gwt.login.client.content.info;

import com.google.gwt.user.client.ui.Widget;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
class AboutWidget extends Widget {
/*
    private final int itemNumber;
    private final Record record;

    public AboutWidget(Record record) {
        this(record, 0);
    }

    public AboutWidget(Record record, int itemNumber) {
        this.record = record;
        this.itemNumber = itemNumber;
        initWidget();
    }

    private void initWidget() {
        final Element div = DOM.createDiv();
        setElement(div);

        final String imageURL = record.getAsString("image");
        if (imageURL != null) {
            Element image = DOM.createDiv();
            image.setClassName("image");
            image.setInnerHTML("<img src=\"" + imageURL + "\"/>");

            div.appendChild(image);
        }

        final Element header = DOM.createDiv();
        header.setClassName("header");
        if (itemNumber != 0) {
            header.setInnerHTML(itemNumber + ". " + record.getAsString("header"));
        } else {
            header.setInnerHTML(record.getAsString("header"));
        }
        div.appendChild(header);

        final Element content = DOM.createDiv();
        content.setClassName("content");
        content.setInnerHTML(record.getAsString("content"));
        div.appendChild(content);
    }
*/
}
