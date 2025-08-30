package cn.tqmjmc.tang.dreamchest.domain;

import org.bukkit.configuration.serialization.ConfigurationSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RewardChest implements ConfigurationSerializable {

    /*
    名字为key
    String name;*/
    // Integer: 编号  DreamItem：物品封装类
    public Map<Integer, DreamItem> items;
    public String name;

    public RewardChest(String name) {
        items = new HashMap<>();
        this.name = name;
    }

    public RewardChest() {
        items = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public RewardChest(Map<Integer, DreamItem> items) {
        this.items = items == null ? new HashMap<>() : items;
    }


    public Map<Integer, DreamItem> getItems() {
        return items;
    }

    public void setItems(Map<Integer, DreamItem> items) {
        this.items = items;
    }

    public void addItem(DreamItem item){
        int index = items.size();
        items.put(index, item);
    }

    private void addItem(int index, DreamItem item){
        items.put(index, item);
    }

    public DreamItem removeItem(int index){
        DreamItem item = items.get(index);
        items.remove(index);
        return item;
    }

    public DreamItem getItem (int index){
        return items.getOrDefault(index, new DreamItem());
    }

    /*
    default奖励箱1:
      items:
       - 'item1'
       - 'item2'
       - 'item3'
       - 'item4'
     */
    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();
        /*map.put("name", name);*/
        int size = map.size();
        String str;
        DreamItem dItem;
        List<String> list = new ArrayList<>();
        // 索引:item名:几率(double)
        for (int i = 0; i < items.size(); i++) {
            dItem = items.get(i);
            if (dItem != null) {
                str = i + ":" + dItem.getName() + ":" + dItem.getRandom();
                list.add(str);
            }
        }
        map.put("items", list);

        return map;
    }

    public static RewardChest deserialize(Map<String, Object> map){
        RewardChest chest = new RewardChest();
        List<String> list = (List<String>) map.get("items");
        String str;
        int index;
        String items;
        double random;
        for (int i = 0; i < list.size(); i++) {
            str = list.get(i);
            String[] arr = str.split(":");
            index = Integer.parseInt(arr[0]);
            items = arr[1];
            random = Double.parseDouble(arr[2]);
            chest.addItem(index, new DreamItem(items, random));
        }
        return chest;
    }
}
