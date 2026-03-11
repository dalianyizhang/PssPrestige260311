package pojo;

import Do.CrewDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pojo.Enums.Collection;
import pojo.Enums.Rarity;
import pojo.Enums.Special;

import java.util.Objects;

/**
 * Crew 是较细致的数据结构，对于各数据类型进行了细分，用于进一步的数据分析
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Crew {
    private int id;
    private String name_en;
    private String name_zh;

    private Rarity rarity; //星级
    private Equipments equipments; //装备
    private Special special; //技能
    private Collection collection; //团队
    private CrewStats crewStats; //所有属性值

    public Crew(int id) {
        this.id = id;
    }

    public Crew(int id, String name) {
        this.id = id;
        this.name_en = name;
    }

    public Crew(int id, Rarity rarity){
        this.id = id;
        this.rarity = rarity;
    }

    public CrewDo toCrewDo(){
        CrewDo res = new CrewDo(id, rarity.name_en);
        res.setName_en(name_en);
        res.setName_zh(name_zh);
        res.setEquipments(equipments.toString());
        res.setSpecial(special.name_en);
        res.setCollection(collection.name_en);
        res.setCrewStats(crewStats);
        return res;
    }

    @Override
    public String toString() {
        return "Crew{" +
                "id=" + id +
                ",name=" + name_zh +
                ",rarity=" + rarity.name() +
                ",equipments=" + equipments.toString() +
                "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crew crew = (Crew) o;
        return id == crew.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
