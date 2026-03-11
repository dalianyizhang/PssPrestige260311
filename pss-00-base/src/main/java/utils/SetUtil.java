package utils;

import java.util.HashSet;
import java.util.Set;

public class SetUtil {
    /**
     * Set 并：s1 ∪ s2
     */
    public static <T> Set<T> sumSet(Set<T> s1, Set<T> s2) {
        Set<T> res = new HashSet<>();
        if (s1 == null && s2 == null) {
            return res;
        }
        if (s1 != null) res.addAll(s1);
        if (s2 != null) res.addAll(s2);
        return res;
    }

    /**
     * Set 交: s1 ∩ s2
     */
    public static <T> Set<T> crossSet(Set<T> s1, Set<T> s2) {
        Set<T> res = new HashSet<>();
        if (s1 == null || s2 == null || s1.size() == 0 || s2.size() == 0) {
            return res;
        }
        res.addAll(s1);
        res.retainAll(s2);
        return res;
    }

    /**
     * Set 差: s1 - s2
     */
    public static <T> Set<T> minusSet(Set<T> s1, Set<T> s2) {
        Set<T> res = new HashSet<>();
        if (s1 != null) res.addAll(s1);
        if (s2 != null) res.removeAll(s2);
        return res;
    }
}
