package Do;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrestigeDo {
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
        PrestigeDo prestigeDo = (PrestigeDo) o;
        return firstSonId == prestigeDo.firstSonId && secondSonId == prestigeDo.secondSonId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(fatherId, firstSonId, secondSonId);
    }

    /**
     * @return
     */
    public String[] getData4Str() {
        return new String[]{
                String.valueOf(firstSonId),
                String.valueOf(secondSonId),
                String.valueOf(fatherId)
        };
    }

    public static String[] getHeader4Str(){
        return new String[]{
                "材料一",
                "材料二",
                "目标随从"
        };
    }
}
