package me.mattyhd0.koth.creator.selection.item;

import me.mattyhd0.koth.util.Config;
import me.mattyhd0.koth.util.Util;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class KothSelectionWand extends ItemStack{

    public KothSelectionWand(){

        super(Material.STONE);

        Material material = Material.valueOf(Config.getConfig().getString("selection-wand.type"));
        String name = Util.color(Config.getConfig().getString("selection-wand.name"));
        List<String> lore = new ArrayList<>();
        for(String line: Config.getConfig().getStringList("selection-wand.lore")){
            lore.add(Util.color(line));
        }

        setType(material);

        ItemMeta itemMeta = getItemMeta();

        itemMeta.setDisplayName(name);
        itemMeta.setLore(lore);

        setItemMeta(itemMeta);

    }

}
