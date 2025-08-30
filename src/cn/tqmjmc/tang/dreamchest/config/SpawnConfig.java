package cn.tqmjmc.tang.dreamchest.config;

import cn.tqmjmc.tang.dreamchest.DreamChest;
import cn.tqmjmc.tang.dreamchest.domain.SpawnChest;
import cn.tqmjmc.tang.dreamchest.utils.ScheUtils;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class SpawnConfig {

    public static File folder;

    public static void load(){
        folder = new File(DreamChest.dataFolder, "spawn");
        loadFile(folder);

    }

    public static void loadFile(File file){
        File[] files = file.listFiles();
        if (files == null) {
            return;
        }
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                loadFile(f);
            } else if (f.getName().endsWith(".yml")){
                YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
                Set<String> keys = config.getKeys(false);
                for (String key : keys) {
                    SpawnChest chest = (SpawnChest) config.get(key);
                    chest.setSpawnName(key);
                    ScheUtils.runSpawnChest(chest);
                    DreamConfig.spawns.put(key ,chest);
                }
            }
        }
    }

    public static String getFileName(String spawnName){
        if (DreamConfig.spawns.containsKey(spawnName)) {
            return getFileName(folder, spawnName);
        }
        return null;
    }

    private static String getFileName(File file, String spawnName) {
        File[] files = file.listFiles();
        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                String str = getFileName(f, spawnName);
                if (str != null) {
                    return str;
                }
            } else if (f.getName().endsWith(".yml")){
                YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
                Set<String> keys = config.getKeys(false);
                if (keys.contains(spawnName)) return f.getName().substring(0, f.getName().length() - 4);
            }
        }
        return null;
    }

    public static String addSpawn(String fileName, String spawnName, SpawnChest spawn){
        if (DreamConfig.spawns.containsKey(spawnName)) {
            return DreamChest.process("&cspawn已被存储！ 如需更新请删除再设置");
        }
        File file = new File(folder, fileName + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(spawnName, spawn);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DreamConfig.spawns.put(spawnName, spawn);
        ScheUtils.runSpawnChest(spawn);
        return DreamChest.process("&a添加成功！");
    }

    public static String addSpawn(String spawnName, SpawnChest spawn){
        return addSpawn(spawnName, spawnName, spawn);
    }

    public static String removeSpawn(String spawnName){
        String fileName = getFileName(spawnName);
        if (fileName == null) {
            return "未找到spawn！";
        }
        File file = new File(folder, fileName + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(spawnName, null);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DreamConfig.spawns.remove(spawnName);
        return "删除成功！";
    }

    private static void updateSpawn(String fileName, SpawnChest spawn){
        File file = new File(folder, fileName + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(spawn.getSpawnName(), spawn);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(){
        Map<String, SpawnChest> spawns = DreamConfig.spawns;
        for (String s : spawns.keySet()) {
            SpawnChest spawn = spawns.get(s);
            String fileName = getFileName(s);
            if (fileName != null) {
                updateSpawn(fileName, spawn);
            } else {
                addSpawn(s, spawn);
            }
        }
    }
}
