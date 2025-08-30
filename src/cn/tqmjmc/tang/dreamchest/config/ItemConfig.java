package cn.tqmjmc.tang.dreamchest.config;

import cn.tqmjmc.tang.dreamchest.DreamChest;
import cn.tqmjmc.tang.dreamchest.domain.DreamItem;
import cn.tqmjmc.tang.dreamchest.domain.RewardChest;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
item/xxx.yml
名称1:
 itemStack...
名称2:
 itemStack...
3

4

5

 */
public class ItemConfig {

    public static YamlConfiguration temp;
    public static File folder;

    public static void load(){
        folder = new File(DreamChest.dataFolder, "item");
        temp = YamlConfiguration.loadConfiguration(new File(DreamChest.dataFolder, "temp"));
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
                    ItemStack item = (ItemStack) config.get(key);
                    DreamConfig.items.put(key ,item);
                    setTemp(f.getName().substring(0, f.getName().length() - 4), item);
                }
            }
        }
    }

    public static void setTemp(String fileName, String name){
        List<String> list = temp.getStringList(fileName);
        if (list != null) {
            list.add(name);
            temp.set(fileName, list);
        } else {
            temp.set(fileName, Arrays.asList(name));

        }
        try {
            temp.save(new File(DreamChest.dataFolder, "temp"));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String addItem(String fileName, String name, ItemStack item){
        if (DreamConfig.items.containsKey(name)) {
            if (item == null) return DreamChest.process("&6手上物品为空！");
            File file = new File(folder, fileName + ".yml");
            YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
            config.set(name, item);
            setTemp(fileName, name);
            try {
                config.save(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
            DreamConfig.items.put(name, item);
            return DreamChest.process("&a添加成功！");
        }
        return DreamChest.process("&citem已被存储！ 如需更新请删除再设置");
    }

    public static String addItem(String name, ItemStack item){
        return addItem(name, name, item);
    }

    public static String removeItem(String name){
        String fileName = getTemp(name);
        if (fileName == null) {
            return ChatColor.COLOR_CHAR + "aitem不存在！";
        }
        removeTemp(fileName, name);
        File file = new File(folder, fileName + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(name, null);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        DreamConfig.items.remove(name);
        return null;
    }

    public static void setTemp(String fileName, ItemStack item){
        setTemp(fileName, item.getItemMeta().getDisplayName());
    }

    /**
     * 删除temp
     * @param fileName
     * @param itemName
     */
    public static void removeTemp(String fileName, String itemName){
        List<String> list = temp.getStringList(fileName);
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                String s = list.get(i);
                if (itemName != null && itemName.equalsIgnoreCase(s)) {
                    list.remove(s);
                }
                temp.set(fileName, list);
                try {
                    temp.save(new File(DreamChest.dataFolder, "temp"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 获取name所在的文件fileName
     * @param name
     * @return name所在的文件名
     */
    public static String getTemp(String name){
        Set<String> keys = temp.getKeys(false);
        for (String key : keys) {
            List<String> list = temp.getStringList(key);
            if (list != null && list.contains(name)) {
                return key;
            }
        }
        return null;
    }

    private static void updateItem(String fileName, ItemStack item){
        File file = new File(folder, fileName + ".yml");
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(getName(item), item);
        try {
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getName(ItemStack item){
        Set<String> set = DreamConfig.items.keySet();
        for (String name : set) {
            ItemStack stack = DreamConfig.items.get(name);
            if (stack.equals(item)) {
                return name;
            }
        }
        return null;
    }

    public static void save(){
        Map<String, ItemStack> spawns = DreamConfig.items;
        for (String s : spawns.keySet()) {
            ItemStack item = spawns.get(s);
            String fileName = getTemp(s);
            if (fileName != null) {
                updateItem(fileName, item);
            } else {
                addItem(s, item);
            }
        }
    }
}
