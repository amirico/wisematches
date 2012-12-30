package wisematches.playground.dictionary;

import java.io.Serializable;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class WordDefinition implements Serializable {
    private String text;
    private String attributes;

    public WordDefinition(String text, String attributes) {
        this.text = text;
        this.attributes = attributes;
    }

    public String getText() {
        return text;
    }

    public String getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("WordDefinition");
        sb.append("{text='").append(text).append('\'');
        sb.append(", attributes='").append(attributes).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
