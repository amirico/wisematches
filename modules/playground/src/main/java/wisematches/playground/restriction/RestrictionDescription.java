package wisematches.playground.restriction;

import wisematches.personality.Membership;

import java.util.Collections;
import java.util.Map;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public final class RestrictionDescription {
    private String name;
    private Map<Membership, Comparable> restrictions;

    public RestrictionDescription() {
    }

    public RestrictionDescription(String name, Membership membership, Comparable restriction) {
        this(name, Collections.singletonMap(membership, restriction));
    }

    public RestrictionDescription(String name, Map<Membership, Comparable> restrictions) {
        this.name = name;
        this.restrictions = restrictions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Comparable getRestriction(Membership membership) {
        return restrictions.get(membership);
    }

    public Map<Membership, Comparable> getRestrictions() {
        return restrictions;
    }

    public void setRestrictions(Map<Membership, Comparable> restrictions) {
        this.restrictions = restrictions;
    }
}
