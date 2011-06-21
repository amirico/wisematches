package wisematches.server.web.services.notice.impl;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class NotificationDescriptionEditor {
    private String name;
    private String series;
    private boolean evenOnline;

    public NotificationDescriptionEditor() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public boolean isEvenOnline() {
        return evenOnline;
    }

    public void setEvenOnline(boolean evenOnline) {
        this.evenOnline = evenOnline;
    }
}
