package cn.tqmjmc.tang.dreamchest.domain;

import cn.tqmjmc.tang.dreamchest.config.DreamConfig;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.HashMap;
import java.util.Map;

/**
 * spawn1:
 *  reTime: 60   # 单位为s  刷新时间
 *  existsTime: 1  # 单位s  存在时间
 *  world: 'world'
 *  area:
 *   # SpawnArea
 *   x1: -1
 *   x2: 1
 *   y1: 200
 *   y2: 256
 *   z1: -1
 *   z2: 1
 *   maxChest: 1  #最大箱子数
 *  chestName: "default奖励箱1"
 */

/**
 * 初始化先初始化chest下的 再初始化spawn下的 不然无法找到RewardChest
 */
public class SpawnChest implements ConfigurationSerializable {

    long reTime;
    long existsTime;
    World world;
    SpawnArea area;
    String chestName;
    String spawnName;

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("reTime", reTime + "");
        map.put("existsTime", existsTime + "");
        map.put("world", world.getName());
        map.put("area", area);
        map.put("chestName", chestName);
        return map;
    }

    public static SpawnChest deserialize(Map<String, Object> map){
        World world = Bukkit.getWorld((String) map.get("world"));
        if (world == null) return null;

        SpawnChest chest = new SpawnChest();

        chest.setWorld(world);
        chest.setReTime(Long.parseLong((String) map.get("reTime")));
        chest.setExistsTime(Long.parseLong((String) map.get("existsTime")));
        chest.setArea((SpawnArea) map.get("area"));
        chest.setChestName((String) map.get("chestName"));
        return chest;
    }


    public long getReTime() {
        return reTime;
    }

    public void setReTime(long reTime) {
        this.reTime = reTime;
    }

    public long getExistsTime() {
        return existsTime;
    }

    public void setExistsTime(long existsTime) {
        this.existsTime = existsTime;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public SpawnArea getArea() {
        return area;
    }

    public void setArea(SpawnArea areas) {
        this.area = areas;
    }

    public SpawnChest() {}

    public RewardChest getChest() {
        return DreamConfig.chests.get(chestName);
    }

    public String getChestName() {
        return chestName;
    }

    public void setChestName(String chestName) {
        this.chestName = chestName;
    }

    public SpawnChest(long reTime, long existsTime, World world, SpawnArea area, String chestName, String spawnName) {
        this.reTime = reTime;
        this.existsTime = existsTime;
        this.world = world;
        this.area = area;
        this.chestName = chestName;
        this.spawnName = spawnName;
    }

    public String getSpawnName() {
        return spawnName;
    }

    public void setSpawnName(String spawnName) {
        this.spawnName = spawnName;
    }
}
