package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrewStats {

    private float hp;
    private float attack;
    private float repair;
    private float ability;

    private float pilot;
    private float science;
    private float engineer;
    private float weapon;

    private int fire_resist;
    private int walk_speed;
    private int run_speed;
    private int training;

    @Override
    public String toString() {
        return "CrewStats{" +
                "hp=" + hp +"," +
                "attack=" + attack +"," +
                "repair=" + repair +"," +
                "ability=" + ability +"," +

                "pilot=" + pilot +"," +
                "science=" + science +"," +
                "engineer=" + engineer +"," +
                "weapon=" + weapon +"," +

                "fire_resist=" + fire_resist +"," +
                "walk_speed="  + walk_speed +"," +
                "run_speed=" + run_speed + "," +
                "training=" + training +
                "}";
    }
}
