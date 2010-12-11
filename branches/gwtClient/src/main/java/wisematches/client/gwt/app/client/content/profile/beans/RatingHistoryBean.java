package wisematches.client.gwt.app.client.content.profile.beans;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author <a href="mailto:smklimenko@gmail.com">Sergey Klimenko</a>
 */
public class RatingHistoryBean implements Serializable {
    private int startMounth;
    private int graphSteps;
    private int[] rating;

    public RatingHistoryBean() {
    }

    public int[] getRating() {
        return rating;
    }

    public void setRating(int[] rating) {
        this.rating = rating;
    }

    public int getStartMounth() {
        return startMounth;
    }

    public void setStartMounth(int startMounth) {
        this.startMounth = startMounth;
    }

    @Override
    public String toString() {
        return "RatingHistoryBean{" +
                "startMounth=" + startMounth +
                ", graphSteps=" + graphSteps +
                ", rating=" + (rating == null ? null : Arrays.asList(rating)) +
                '}';
    }
}
