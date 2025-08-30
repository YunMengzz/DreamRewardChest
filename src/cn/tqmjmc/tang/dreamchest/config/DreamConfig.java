package cn.tqmjmc.tang.dreamchest.config;

import cn.tqmjmc.tang.dreamchest.DreamChest;
import cn.tqmjmc.tang.dreamchest.domain.DreamItem;
import cn.tqmjmc.tang.dreamchest.domain.RewardChest;
import cn.tqmjmc.tang.dreamchest.domain.SpawnChest;
import cn.tqmjmc.tang.dreamchest.domain.SpawnArea;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

/**
@author StartDream-kylin


 */
public class DreamConfig {

    public static Map<String, ItemStack> items;
    public static Map<String, RewardChest> chests;
    public static Map<String, SpawnChest> spawns;

    public static void load(){
        DreamChest.config = DreamChest.plugin.getConfig();
        DreamChest.lang = YamlConfiguration.loadConfiguration(new InputStreamReader(DreamChest.plugin.getResource("lang.yml")));
        File folder = DreamChest.dataFolder;
        File file = new File(folder, "item");
        if (!file.exists()) {
            file.mkdirs();
        }
        File item = new File(file, "default.yml");
        if (!item.exists()) {
            try {
                item.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileConfiguration config = YamlConfiguration.loadConfiguration(item);
            ItemStack stack = new ItemStack(Material.STONE, 2);
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(DreamChest.process("&5测试物品"));
            meta.setLore(Arrays.asList(DreamChest.process("&6这是一个测试物品"), "111"));
            stack.setItemMeta(meta);
            config.set("测试物品1", stack);
            config.set("测试物品2", stack);
            try {
                config.save(item);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        file = new File(folder, "temp"); // 缓存Directory
        if (!file.exists()) {
            file.mkdirs();
        }
        File items = new File(file, "items.yml");
        if (!items.exists()) {
            try {
                items.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(items);
            config.set("default", Arrays.asList("测试物品1", "测试物品2"));
            try {
                config.save(items);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        File spawnChests = new File(folder, "locations.yml");
        if (!spawnChests.exists()) {
            try {
                spawnChests.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(spawnChests);
            config.set("locations", new ArrayList<String>());
            try {
                config.save(spawnChests);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        file = new File(folder, "spawn");
        if (!file.exists()) {
            file.mkdirs();
        }
        File spawn = new File(file, "default.yml");
        if (!spawn.exists()) {
            try {
                spawn.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            /*
            spawn1:
             reTime: 60   # 单位为min
             existsTime: 1
             world: 'world'
             area:
              1:
               # SpawnArea
               x1: -1
               x2: 1
               y1: 200
               y2: 256
               z1: -1
               z2: 1
               maxChest: 1  #最大箱子数
             chestName: "default奖励箱1"
             */
            YamlConfiguration config = YamlConfiguration.loadConfiguration(spawn);
            SpawnChest spawnChest = new SpawnChest(60, 1, Bukkit.getWorld("world"), new SpawnArea(-1, 1, 200, 256, -1, 1, 1), "default奖励箱1","spawn1");
            config.set("spawn1", spawnChest);
            try {
                config.save(spawn);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



        file = new File(folder, "chest");
        if (!file.exists()) {
            file.mkdirs();
        }
        File chest = new File(file, "default.yml");
        if (!chest.exists()) {
            try {
                chest.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            YamlConfiguration config = YamlConfiguration.loadConfiguration(chest);
            /*
            default:
              name: name
              items:
               - 'item1'
               - 'item2'
               - 'item3'
               - 'item4'
             */
            RewardChest c = new RewardChest("default奖励箱1"); // "default奖励箱1"
            c.addItem(new DreamItem("测试物品1", 1));
            c.addItem(new DreamItem("测试物品2", 0.5));
            config.set("default奖励箱1", c);
            try {
                config.save(chest);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ItemConfig.load();
        ChestConfig.load();
        SpawnConfig.load();
        TempConfig.load();
    }


    public static void createFile(File file){
        if (file != null) {
            if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public static void save(){
        ItemConfig.save();
        ChestConfig.save();
        SpawnConfig.save();
    }
}
