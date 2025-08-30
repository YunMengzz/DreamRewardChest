package cn.tqmjmc.tang.dreamchest.domain;

import cn.tqmjmc.tang.dreamchest.config.DreamConfig;
import org.bukkit.inventory.ItemStack;

public class DreamItem {

    private ItemStack item;
    private double random;


    public DreamItem(String name, double random) {
        this.item = DreamConfig.items.get(name);
        this.random = random;
    }

    public DreamItem(ItemStack item, double random) {
        this.item = item;
        this.random = random;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public String getName(){
        return item.getItemMeta().getDisplayName();
    }

    public double getRandom() {
        return random;
    }

    public void setRandom(double random) {
        this.random = random;
    }

    public DreamItem() {
    }
}
