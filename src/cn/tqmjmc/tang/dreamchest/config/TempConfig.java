package cn.tqmjmc.tang.dreamchest.config;


import cn.tqmjmc.tang.dreamchest.DreamChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class TempConfig {

    public static File temp;
    public static ArrayList<Location> locations;
    public static HashMap<Location, Long> time;

    public static void load(){
        temp = new File(DreamChest.dataFolder, "temp/locations.yml");
        locations = new ArrayList<>();
        time = new HashMap<>();
        List<String> list = getLocations();
        Location loc;
        for (int i = 0; i < list.size(); i++) {
            String str = list.get(i);
            String[] arr = str.split(":");
            if (arr.length != 5) {
                continue;
            }
            loc = new Location(Bukkit.getWorld(arr[0]), Integer.parseInt(arr[1]), Integer.parseInt(arr[2]), Integer.parseInt(arr[3]));
            locations.add(loc);
            addTime(loc, Long.parseLong(arr[4]));
        }
    }

    public static void addTime(Location loc, long time){
        TempConfig.time.put(new Location(loc.getWorld(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()), time);
    }

    public static void addLocation(Location location, long second){
        YamlConfiguration config = getConfig();
        List<String> list = getLocations();

        // world:x:y:z:time
        list.add(location.getWorld().getName() + ":" + location.getBlockX() + ":" + location.getBlockY() + ":" + location.getBlockZ() + ":" + second);
        config.set("locations", list);
        TempConfig.locations.add(getBlockLocation(location));
        addTime(getBlockLocation(location), second);
        try {
            config.save(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getLocations(){
        YamlConfiguration config = getConfig();
        return config.getStringList("locations") == null ? config.getStringList("locations") : new ArrayList<>();

    }

    private static YamlConfiguration getConfig(){
        boolean b = temp.exists();
        if (!b) {
            try {
                temp.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return YamlConfiguration.loadConfiguration(temp);

    }

    public static boolean isRewardChest(Location loc){
        Location location = getBlockLocation(loc);
        return locations.contains(location);
    }

    public static void removeLocation(Location loc){
        if (isRewardChest(loc)) {
            locations.remove(getBlockLocation(loc));
            YamlConfiguration config = getConfig();
            List<String> list = getLocations();
            /*list.remove(loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ());*/
            for (int i = 0; i < list.size(); i++) {
                String str = list.get(i);
                if (str.startsWith(loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ())) {
                    list.remove(i);
                }
            }
            config.set("locations", list);
            time.remove(getBlockLocation(loc));
            try {
                config.save(temp);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private static Location getBlockLocation(Location loc){
        return new Location(loc.getWorld(),loc.getBlockX(),loc.getBlockY(), loc.getBlockZ());
    }

    public static void disable(){
        for (Location loc : time.keySet()) {
            long t = time.get(loc);
            YamlConfiguration config = getConfig();
            List<String> list = new ArrayList<>();
            list.add(loc.getWorld().getName() + ":" + loc.getBlockX() + ":" + loc.getBlockY() + ":" + loc.getBlockZ() + ":" + t);
        }
    }

}
