package pojo.Enums;

import java.util.Objects;

/**
 * 船员技能
 */
public enum Special {
    NONE("无", "none", "",0),
    ARSON("纵火", "arson","SetFire",1),
    BLOOD_LUST("嗜血", "blood_lust","Bloodlust",2),
    FIRE_WALK("火步", "fire_walk","FireWalk", 3),
    FIRST_AID("急救", "first_aid","HealSelfHp",4),
    FREEZE("冰冻", "freeze","Freeze", 5),
    GAS("毒气", "gas","DamageToSameRoomCharacters", 6),
    HEALING_RAIN("治疗", "healing_rain","HealSameRoomCharacters",7),
    PHASE_SHIFT("相闪", "phase_shift","Invulnerability",8),
    RUSH("加速", "rush","AddReload",9),
    STASIS_SHIELD("电盾", "stasis_shield","ProtectRoom",10),
    SYSTEM_HACK("黑客", "system_hack","DeductReload",11),
    ULTRA_DISMANTLE("强拆", "ultra_dismantle","DamageToRoom",12),
    URGENT_REPAIR("急修", "urgent_repair","HealRoomHp", 13),
    CRITICAL_STRIKE("刺杀", "critical_strike","DamageToCurrentEnemy",14);

    public final String name_zh;
    public final String name_en;
    public final String name_pss;
    public final int index;

    Special(String _name_zh, String _name_en, String _name_pss, int _index) {
        this.name_zh = _name_zh;
        this.name_en = _name_en;
        this.name_pss = _name_pss;
        this.index = _index;
    }

    // 覆写方法
    @Override
    public String toString() {
        return this.name_zh;
    }

    public static Special tranPssName(String name_pss){
        for (Special s : Special.values()) {
            if (Objects.equals(s.name_pss, name_pss)) {
                return s;
            }
        }
        return NONE;
    }

    public static Special valueOf(int value){
        for (Special s : Special.values()) {
            if (s.index == value) {
                return s;
            }
        }
        return null;
    }
}
