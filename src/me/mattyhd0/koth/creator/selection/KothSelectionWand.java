package me.mattyhd0.koth.creator.selection;

import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KothSelectionWand {

    private static ItemStack wand;

    public static void setupWand(){

        Material material = Material.valueOf(Config.getConfig().getString("selection-wand.type"));
        String name = Util.color(Config.getConfig().getString("selection-wand.name"));
        List<String> lore = new ArrayList<>();
        for(String line: Config.getConfig().getStringList("selection-wand.lore")){
            lore.add(Util.color(line));
        }


        ItemStack itemStack = new ItemStack(material);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);

        itemStack.setItemMeta(itemMeta);
        wand = itemStack;

    }

    public static ItemStack getWand(){

        return wand;

    }

}
