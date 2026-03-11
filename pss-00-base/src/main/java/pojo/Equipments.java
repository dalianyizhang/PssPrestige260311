package pojo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Equipments {
    private boolean accessory;
    private boolean body;
    private boolean head;
    private boolean leg;
    private boolean pet;
    private boolean weapon;

    public Equipments() {
        accessory = false;
        body = false;
        head = false;
        leg = false;
        pet = false;
        weapon = false;
    }

    /**
     * 将掩码解析为装备位信息
     * @param mask pss官网使用二进制掩码存储装备位信息，但该数字为十进制
     *             因此需先转为二进制，再逐位读取信息
     * @return
     */
    public static Equipments parseMask(int mask){
        String binaryString = String.format("%6s", Integer.toBinaryString(mask)).replace(' ', '0');
        Equipments res = new Equipments();
        if(binaryString.charAt(0)=='1'){
            res.setPet(true);
        }
        if(binaryString.charAt(1)=='1'){
            res.setAccessory(true);
        }
        if(binaryString.charAt(2)=='1'){
            res.setWeapon(true);
        }
        if(binaryString.charAt(3)=='1'){
            res.setLeg(true);
        }
        if(binaryString.charAt(4)=='1'){
            res.setBody(true);
        }
        if(binaryString.charAt(5)=='1'){
            res.setHead(true);
        }
        return res;
    }

    public static Equipments valueOf(String equipments) {
        Equipments res = new Equipments();
        if (!equipments.isBlank()) {
            String[] strings = equipments.split("\\+");
            for (String equipment : strings) {
                switch (equipment.toUpperCase()) {
                    case "ACCESSORY":
                        res.setAccessory(true);
                        break;
                    case "BODY":
                        res.setBody(true);
                        break;
                    case "HEAD":
                        res.setHead(true);
                        break;
                    case "LEG":
                        res.setLeg(true);
                        break;
                    case "PET":
                        res.setPet(true);
                        break;
                    case "WEAPON":
                        res.setWeapon(true);
                        break;
                    default:
                        break;
                }
            }
        }
        return res;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (isAccessory()) {
            sb.append("肩+");
        }
        if (isHead()) {
            sb.append("头+");
        }
        if (isBody()) {
            sb.append("胸+");
        }
        if (isWeapon()) {
            sb.append("手+");
        }
        if (isLeg()) {
            sb.append("腿+");
        }
        if (isPet()) {
            sb.append("宠+");
        }
        if (sb.length() > 0) {
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }
}
