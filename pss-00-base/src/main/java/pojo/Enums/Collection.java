package pojo.Enums;

/**
 * 船员团队
 */
public enum Collection {

    NONE("无", "none", 0),
    ALIEN_TECH("外星科技","alien_tech", 10),
    ANIMATRONICS("动画","animatronics",  28),
    ARDENT("圣堂", "ardent", 15),
    ATHLETES("运动员","athletes",  32),
    CATS("猫", "cats", 3),
    CONSTELLATION("星座", "constellation", 9),
    COSMIC_CRUSADERS("宇宙十字军","cosmic_crusaders",  30),
    CRITTERS("动物","critters",  5),
    DRAKIAN("龙族", "drakian", 31),
    EGG_HUNTERS("彩蛋猎人", "egg_hunters", 27),
    FEDERATION("联邦", "federation", 7),
    GALACTIC_MARINERS("银河美少女","galactic_mariners",  29),
    GRAY("格雷", "gray", 13),
    JOSEON_TRADERS("朝商","joseon_traders",  26),
    LOST_LOVERS("失落的恋人", "lost_lovers", 16),
    OFFICE_WORKERS("上班族", "office_workers", 4),
    PIRATES("探险者", "pirates", 6),
    QTARIAN("卡塔利恩", "qtarian", 8),
    SANGO("三国", "sango", 2),
    SAVY_SODA("精明苏打", "savy_soda", 1),
    SEAFOOD("海鲜","seafood",  21),
    SPOOKY("幽灵", "spooky", 23),
    SYMPHONY("交响乐团", "symphony", 24),
    TASK_FORCE_XMAS("圣诞特遣队", "task_force_xmas", 22),
    THE_VOID("虚空","the_void",  25),
    VISIRI("虫族", "visiri", 14),
    SECTION_9("第九节","Section_9",34),
    CYANIDE_HAPPINESS("氰化物","cyanide_happiness",35),
    THE_GUNSHOW("枪展","the_Gunshow",36),
    COWBOY_BEBOP("星际牛仔","cowboy_bebop",37),

    NEW("未知","new",999);


    public final String name_zh;
    public final String name_en;
    public final int index;

    private Collection(String _name_zh, String _name_en, int _index) {
        this.name_zh = _name_zh;
        this.name_en = _name_en;
        this.index = _index;
    }

    // 覆盖方法
    @Override
    public String toString() {
        return this.name_zh;
    }

    public static Collection valueOf(int value) {
        for (Collection t : Collection.values()) {
            if (t.index == value) {
                return t;
            }
        }
        return NEW;
    }

}
