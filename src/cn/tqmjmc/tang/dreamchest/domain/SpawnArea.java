package cn.tqmjmc.tang.dreamchest.domain;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * x1: -1
 * x2: 1
 * y1: 200
 * y2: 256
 * z1: -1
 * z2: 1
 * maxChest: 1  #最大箱子数
 */
public class SpawnArea implements ConfigurationSerializable {

    int x1;
    int x2;
    int y1;
    int y2;
    int z1;
    int z2;
    int maxChest;

    public SpawnArea(int x1, int x2, int y1, int y2, int z1, int z2, int maxChest) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.z1 = z1;
        this.z2 = z2;
        this.maxChest = maxChest;
    }

    public SpawnArea() {
    }

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("x1", x1);
        map.put("x2", x2);
        map.put("y1", y1);
        map.put("y2", y2);
        map.put("z1", z1);
        map.put("z2", z2);
        map.put("maxChest", maxChest);
        return map;
    }

    public static SpawnArea deserialize(Map<String, Object> map){
        SpawnArea area = new SpawnArea();
        area.setX1(Integer.parseInt((String) map.get("x1")));
        area.setX2(Integer.parseInt((String) map.get("x2")));
        area.setY1(Integer.parseInt((String) map.get("y1")));
        area.setY2(Integer.parseInt((String) map.get("y2")));
        area.setZ1(Integer.parseInt((String) map.get("z1")));
        area.setZ2(Integer.parseInt((String) map.get("z2")));
        area.setMaxChest(Integer.parseInt((String) map.get("maxChest")));
        return area;
    }

    public int getX1() {
        return x1;
    }

    public void setX1(int x1) {
        this.x1 = x1;
    }

    public int getX2() {
        return x2;
    }

    public void setX2(int x2) {
        this.x2 = x2;
    }

    public int getY1() {
        return y1;
    }

    public void setY1(int y1) {
        this.y1 = y1;
    }

    public int getY2() {
        return y2;
    }

    public void setY2(int y2) {
        this.y2 = y2;
    }

    public int getZ1() {
        return z1;
    }

    public void setZ1(int z1) {
        this.z1 = z1;
    }

    public int getZ2() {
        return z2;
    }

    public void setZ2(int z2) {
        this.z2 = z2;
    }

    public int getMaxChest() {
        return maxChest;
    }

    public void setMaxChest(int maxChest) {
        this.maxChest = maxChest;
    }
}
