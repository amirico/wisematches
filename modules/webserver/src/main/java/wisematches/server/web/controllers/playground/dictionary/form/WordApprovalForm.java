package wisematches.server.web.controllers.playground.dictionary.form;

import java.util.Arrays;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class WordApprovalForm {
    private String type;
    private long[] ids;

    public WordApprovalForm() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long[] getIds() {
        return ids;
    }

    public void setIds(long[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return "WordApprovalForm{" +
                "type='" + type + '\'' +
                ", ids=" + Arrays.toString(ids) +
                '}';
    }
}
