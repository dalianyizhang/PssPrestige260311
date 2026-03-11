package pojo.Enums;

import java.util.Objects;

/**
 * 船员稀有度/星级
 */
public enum Rarity {
    COMMON("普通","common",1),
    ELITE("精英","elite",2),
    UNIQUE("独特","unique",3),
    EPIC("史诗","epic",4),
    HERO("英雄","hero",5),
    SPECIAL("特殊","special",6),
    LEGENDARY("传奇","legendary",7);

    public final String name_zh;
    public final String name_en;
    public final int index;

    Rarity(String _name_zh, String _name_en, int _index) {
        this.name_zh = _name_zh;
        this.name_en = _name_en;
        this.index = _index;
    }

    // 覆盖方法
    @Override
    public String toString() {
        return this.name_zh;
    }

    // public static Rarity valueOf(int value){
    //     for (Rarity r : Rarity.values()) {
    //         if (r.index == value) {
    //             return r;
    //         }
    //     }
    //     return null;
    // }

    /**
     * 传入星级中文名，返回对应星级数值
     */
    public static int str2Int(String str){
        for (Rarity r : Rarity.values()) {
            if (Objects.equals(r.name_zh, str)) {
                return r.index;
            }
        }
        return 0;
    }

    /**
     * 高一级
     */
    public static Rarity upRarity(Rarity rarity){
        switch (rarity){
            case COMMON:
                return Rarity.ELITE;
            case ELITE:
                return Rarity.UNIQUE;
            case UNIQUE:
                return Rarity.EPIC;
            case EPIC:
                return Rarity.HERO;
            case HERO:
                return Rarity.LEGENDARY;
            default:
                return null;
        }
    }

    /**
     * 低一级
     */
    public static Rarity downRarity(Rarity rarity){
        switch (rarity){
            case LEGENDARY:
                return Rarity.HERO;
            case HERO:
                return Rarity.EPIC;
            case EPIC:
                return Rarity.UNIQUE;
            case UNIQUE:
                return Rarity.ELITE;
            case ELITE:
                return Rarity.COMMON;
            default:
                return null;
        }
    }
}
