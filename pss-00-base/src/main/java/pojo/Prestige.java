package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Prestige {
    private int fatherId;
    private int firstSonId;
    private int secondSonId;

    @Override
    public String toString() {
        return "Prestige{'"
                + fatherId + "' = '"
                + firstSonId + "' + '"
                + secondSonId + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prestige crewPrestige = (Prestige) o;
        return crewPrestige.firstSonId == firstSonId && crewPrestige.secondSonId == secondSonId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fatherId, firstSonId, secondSonId);
    }
}
