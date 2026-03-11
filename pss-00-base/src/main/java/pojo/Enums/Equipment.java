package pojo.Enums;

/**
 * 船员装备位
 */
public enum Equipment {
    NONE("无", "none", 0),
    ACCESSORY("饰品", "accessory", 1),
    BODY("身体", "body", 2),
    HEAD("头部", "head", 3),
    LEG("腿部", "leg", 4),
    PET("宠物", "pet", 5),
    WEAPON("手部", "weapon", 6);

    public final String name_zh;
    public final String name_en;
    public final int index;

    Equipment(String _name_zh, String _name_en, int _index) {
        this.name_zh = _name_zh;
        this.name_en = _name_en;
        this.index = _index;
    }

    // 覆盖方法
    @Override
    public String toString() {
        return this.name_zh;
    }

    public static Equipment valueOf(int value) {
        for (Equipment e : Equipment.values()) {
            if (e.index == value) {
                return e;
            }
        }
        return Equipment.NONE;
    }
}
