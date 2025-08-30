package cn.tqmjmc.tang.dreamchest.config;

import cn.tqmjmc.tang.dreamchest.DreamChest;
import cn.tqmjmc.tang.dreamchest.domain.RewardChest;
import cn.tqmjmc.tang.dreamchest.domain.SpawnChest;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

/**
chest/xxx.yml

 # 2:   ...

 */
public class ChestConfig {

    public static File folder;

    public static void load(){
        folder = new File(DreamChest.dataFolder, "chest");;
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
                    RewardChest chest = (RewardChest) config.get(key);
                    chest.setName(f.getName().substring(0, f.getName().length() - 4));
                    DreamConfig.chests.put(key ,chest);
//                    setTemp(f.getName().substring(0, f.getName().length() - 4), item);
                }
            }
        }
    }

    public static String getFileName(String chestName){
        if (DreamConfig.chests.containsKey(chestName)) {
            return getFileName(folder, chestName);
        } else return null;
    }

    private static String getFileName(File file, String name){
        File[] files = file.listFiles();
        if (files == null) {
            return null;
        }
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                String str = getFileName(f, name);
                if (str != null) {
                    return str;
                }
            } else if (f.getName().endsWith(".yml")){
                YamlConfiguration config = YamlConfiguration.loadConfiguration(f);
                Set<String> keys = config.getKeys(false);
                    if (keys.contains(name)) return f.getName().substring(0, f.getName().length() - 4);
            }
        }
        return null;
    }

    public static String addChest(String fileName, String name, RewardChest chest){
        if (DreamConfig.chests.containsKey(fileName)) {
            return DreamChest.process("&cchest已被存储！ 如需更新请删除再设置");
        }
        File file = new File(folder, fileName + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(name, chest);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DreamConfig.chests.put(name, chest);
        return DreamChest.process("&a添加成功！");
    }

    public static String addChest(String name, RewardChest chest){
        return addChest("default", name, chest);
    }

    public static String removeChest(String chestName){
        String fileName = getFileName(chestName);
        if (fileName == null) {
            return "未找到chest！";
        }
        File file = new File(folder, fileName + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(chestName, null);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DreamConfig.chests.remove(chestName);
        return "删除成功！";
    }

    private static void updateChest(String fileName, RewardChest chest){
        File file = new File(folder, fileName + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(chest.getName(), chest);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void save(){
        Map<String, RewardChest> spawns = DreamConfig.chests;
        for (String s : spawns.keySet()) {
            RewardChest chest = spawns.get(s);
            String fileName = getFileName(s);
            if (fileName != null) {
                updateChest(fileName, chest);
            } else {
                addChest(s, chest);
            }
        }
    }
}
