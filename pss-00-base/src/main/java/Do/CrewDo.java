package Do;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pojo.Crew;
import pojo.CrewStats;
import pojo.Enums.Collection;
import pojo.Enums.Rarity;
import pojo.Enums.Special;
import pojo.Equipments;

/**
 * CrewDo是比较粗糙/原始/raw的数据结构，仅用 int，float，String获取数据类型
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CrewDo {
    private int id;
    private String name_en;
    private String name_zh;

    private String rarity; //星级
    private String equipments; //装备位
    private String special; //技能
    private String collection; //团队

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

    public CrewDo(int id) {
        this.id = id;
    }

    public CrewDo(int id, String rarity) {
        this.id = id;
        this.rarity = rarity;
    }

    public CrewDo(int id, String rarity, String name_en) {
        this.id = id;
        this.rarity = rarity;
        this.name_en = name_en;
    }

    public void setCrewStats(CrewStats crewStats) {
        hp = crewStats.getHp();
        attack = crewStats.getAttack();
        repair = crewStats.getRepair();
        ability = crewStats.getAbility();
        pilot = crewStats.getPilot();
        science = crewStats.getScience();
        engineer = crewStats.getEngineer();
        weapon = crewStats.getWeapon();
        fire_resist = crewStats.getFire_resist();
        walk_speed = crewStats.getWalk_speed();
        run_speed = crewStats.getRun_speed();
        training = crewStats.getTraining();
    }

    /**
     * 将 CrewDo 转换为 Crew
     */
    public Crew toCrew() {
        Crew res = new Crew(id, name_en);
        res.setName_zh(name_zh);
        res.setRarity(Rarity.valueOf(rarity));
        res.setEquipments(Equipments.valueOf(equipments));
        res.setSpecial(Special.valueOf(special));
        res.setCollection(Collection.valueOf(collection));
        res.setCrewStats(new CrewStats(hp, attack, repair, ability, pilot, science, engineer, weapon,
                fire_resist, walk_speed, run_speed, training));
        return res;
    }

    /**
     * 以String形式 获取 CrewDo的全数据，次序重要
     *
     * @return
     */
    public String[] getData4Str() {
        return new String[]{
                String.valueOf(id), String.valueOf(name_zh),
                rarity, equipments, special, collection,
                String.valueOf(hp), String.valueOf(attack), String.valueOf(repair), String.valueOf(ability),
                String.valueOf(pilot), String.valueOf(science), String.valueOf(engineer), String.valueOf(weapon),
                String.valueOf(fire_resist), String.valueOf(walk_speed), String.valueOf(run_speed), String.valueOf(training)
        };
    }

    public static String[] getHeader4Str() {
        return new String[]{
                "序号", "姓名",
                "稀有度", "装备位", "技能", "团队",
                "血量","攻击","维修","能力",
                "导航","科技","引擎","武器",
                "火抗","走路","跑步","训练度"
        };
    }

    @Override
    public String toString() {
        return "CrewDo{" +
                "id=" + id +
                ",name_en=" + name_en +
                ",name_zh=" + name_zh +
                ",rarity=" + rarity +
                ",equipments=" + equipments +
                ",hp=" + hp +
                ",training=" + training +
                "}";
    }
}
