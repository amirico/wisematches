package wisematches.server.web.services.restriction.impl;

import wisematches.personality.Membership;
import wisematches.personality.player.Player;
import wisematches.server.web.services.restriction.RestrictionDescription;
import wisematches.server.web.services.restriction.RestrictionException;
import wisematches.server.web.services.restriction.RestrictionManager;

import java.util.*;

/**
 * @author Sergey Klimenko (smklimenko@gmail.com)
 */
public class RestrictionManagerImpl implements RestrictionManager {
    private final Map<Key, Comparable> restrictions = new HashMap<Key, Comparable>();
    private final Collection<RestrictionDescription> descriptions = new ArrayList<RestrictionDescription>();

    public RestrictionManagerImpl() {
    }

    @Override
    public Collection<RestrictionDescription> getRestrictionDescriptions() {
        return Collections.unmodifiableCollection(descriptions);
    }

    @Override
    public Comparable getRestriction(Player player, String name) {
        return restrictions.get(new Key(name, player));
    }

    @Override
    public boolean hasRestriction(Player player, String name) {
        return restrictions.containsKey(new Key(name, player));
    }

    @Override
    @SuppressWarnings("unchecked")
    public void checkRestriction(Player player, String name, Comparable value) throws RestrictionException {
        final Key key = new Key(name, player);
        if (!restrictions.containsKey(key)) {
            return;
        }
        final Comparable comparable = restrictions.get(key);
        if (comparable.compareTo(value) <= 0) {
            throw new RestrictionException(name, comparable, value);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean isRestricted(Player player, String name, Comparable value) {
        final Key key = new Key(name, player);
        if (!restrictions.containsKey(key)) {
            return false;
        }
        final Comparable comparable = restrictions.get(key);
        return comparable.compareTo(value) <= 0;
    }

    public void setRestrictions(Collection<RestrictionDescription> restrictions) {
        this.restrictions.clear();
        this.descriptions.clear();

        if (restrictions != null) {
            this.descriptions.addAll(restrictions);

            for (RestrictionDescription restriction : restrictions) {
                for (Map.Entry<Membership, Comparable> entry : restriction.getRestrictions().entrySet()) {
                    this.restrictions.put(new Key(restriction.getName(), entry.getKey()), entry.getValue());
                }
            }
        }
    }

    private static final class Key {
        private final String name;
        private final Membership membership;

        private Key(String name, Player player) {
            this(name, player.getMembership());
        }

        private Key(String name, Membership membership) {
            this.name = name;
            this.membership = membership;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;
            return membership == key.membership && name.equals(key.name);
        }

        @Override
        public int hashCode() {
            int result = name.hashCode();
            result = 31 * result + membership.hashCode();
            return result;
        }
    }
}
