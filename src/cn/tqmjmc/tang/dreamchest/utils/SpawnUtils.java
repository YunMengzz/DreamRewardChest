package cn.tqmjmc.tang.dreamchest.utils;

import cn.tqmjmc.tang.dreamchest.DreamChest;
import cn.tqmjmc.tang.dreamchest.config.TempConfig;
import cn.tqmjmc.tang.dreamchest.domain.DreamItem;
import cn.tqmjmc.tang.dreamchest.domain.RewardChest;
import cn.tqmjmc.tang.dreamchest.domain.SpawnArea;
import cn.tqmjmc.tang.dreamchest.domain.SpawnChest;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class SpawnUtils {
    /**
     * 务必使用异步调用！！！！！！
     * @param chest
     * @return 是否成功
     */
    public static boolean spawnChest(SpawnChest chest){
        Location location = null;
        int total = 0;
        while(location == null){
             location = getLocation(chest.getArea(), chest.getWorld());
             total++;
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (total > 30) {
                // 次数过多！
                return false;
            }
        }
        RewardChest c = chest.getChest();
        Location loc = location;
        Future<Chest> future = Bukkit.getScheduler().callSyncMethod(DreamChest.plugin, () -> {
            loc.getBlock().setType(Material.CHEST);
            return (Chest) loc.getBlock().getState();
        });
        Chest chestState;
        try {
            chestState = future.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return false;
        }
        if (chestState == null) return false;
        Inventory inventory = chestState.getBlockInventory();
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < inventory.getSize(); i++) {
            list.add(i);
        }
        Random random = new Random();
        for (Integer index : c.getItems().keySet()) {
            DreamItem dItem = c.getItems().get(index);
            double ran = dItem.getRandom();
            if (random.nextDouble() <= ran) {
                // add
                int i = random.nextInt(list.size());
                inventory.setItem(list.get(i), dItem.getItem());
                // use remove(Object o)  必须强转
                list.remove((Object) i);
            }
        }
        // update 更新到Block
        chestState.update();

        TempConfig.addLocation(location, chest.getExistsTime());

        return true;
    }


    public static Location getLocation(SpawnArea area, World world){
        return getLocation(area.getX1(), area.getX2(), area.getY1(), area.getY2(), area.getZ1(), area.getZ2(), world);
    }

    public static Location getLocation(int x1, int x2, int y1, int y2, int z1, int z2, World world){
        int x = randomNumber(x1, x2);
        int z = randomNumber(z1, z2);
        if (y1 < y2) {
            int var = y2;
            y2 = y1;
            y1 = var;
        }
        for (int i = y2; i >= y1; i++) {
            Location loc = new Location(world, x, i, z);
            // 线程安全
            Future<Block> future = Bukkit.getScheduler().callSyncMethod(DreamChest.plugin, loc::getBlock);
            try {
                if (future.get().getType() != Material.AIR) {
                    return new Location(world, x, i + 1, z);
                }
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int randomNumber(int a, int b){
        if (a > b) {
            int var = a - b;
            int i = new Random().nextInt(var);
            return b + i;
        } else if (a == b) {
            return a;
        }
        else {
            int var = b - a;
            int i = new Random().nextInt(var);
            return a + i;
        }
    }

}
