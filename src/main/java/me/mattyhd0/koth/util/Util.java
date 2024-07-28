package me.mattyhd0.koth.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Util {
    /**
     * Parses gradients, hex colors, and legacy color codes
     *
     * @param value The message
     * @return A color-replaced message
     */
    public static String color(String value) {
        if (StringUtil.isEmpty(value)) return value;

        value = HexUtils.parseRainbow(value);
        value = HexUtils.parseGradients(value);
        value = HexUtils.parseHex(value);
        value = HexUtils.parseLegacy(value);

        return value;
    }

    public static List<String> coloredList(Collection<String> list) {
        return list.stream().map(Util::color).distinct().collect(Collectors.toList());
    }

    public static boolean locationIsInZone(Location location, Location position1, Location position2){
        int locationX = (int) location.getX();
        int locationY = (int) location.getY();
        int locationZ = (int) location.getZ();

        double position1X = Math.max(position1.getX(), position2.getX());
        double position1Y = Math.max(position1.getY(), position2.getY());
        double position1Z = Math.max(position1.getZ(), position2.getZ());

        double position2X = Math.min(position1.getX(), position2.getX());
        double position2Y = Math.min(position1.getY(), position2.getY());
        double position2Z = Math.min(position1.getZ(), position2.getZ());

        /* System.out.println(
                MessageFormat.format("{0} {1} {2} > {3} {4} {5} | {6} {7} {8}", position1X, position1Y, position1Z, position2X, position2Y, position2Z, locationX, locationY, locationZ)
        ); */

        return (position1X >= locationX  && locationX >= position2X) &&
               (position1Y >= locationY && locationY >= position2Y) &&
               (position1Z >= locationZ && locationZ >= position2Z);
    }

    public static Location getCenterFrom(Location position1, Location position2){
        double position1X = Math.max(position1.getX(), position2.getX());
        double position1Y = Math.max(position1.getY(), position2.getY());
        double position1Z = Math.max(position1.getZ(), position2.getZ());

        double position2X = Math.min(position1.getX(), position2.getX());
        double position2Y = Math.min(position1.getY(), position2.getY());
        double position2Z = Math.min(position1.getZ(), position2.getZ());

        return new Location(position1.getWorld(), (position1X + position2X) / 2, (position1Y + position2Y) / 2, (position1Z + position2Z) / 2);
    }

    public static String getItemName(ItemStack itemStack){
        ItemMeta itemMeta = itemStack.getItemMeta();

        return itemMeta != null && itemMeta.getDisplayName() != null ?
            itemStack.getItemMeta().getDisplayName() :
            getNameFromId(itemStack.getType().toString());
    }

    public static String getNameFromId(String string){
        // Divide la id EJEMPLO: DIAMOND_SWORD en el array {"DIAMOND", "SWORD"}
        String[] strings = string.split("_");
        StringBuilder finalString = new StringBuilder();

        // Transforma el array {"DIAMOND", "SWORD"} en el String "Diamond Sword "
        for(String str: strings){
            str = str.toLowerCase();
            str = str.replaceFirst(Character.toString(str.charAt(0)), Character.toString(str.charAt(0)).toUpperCase());
            finalString.append(str).append(" ");
        }

        // Elimina el " " (espacio) al final del texto si este existe
        if (finalString.charAt(finalString.length() - 1) == ' ') finalString = new StringBuilder(finalString.substring(0, finalString.length() - 1));

        return finalString.toString();

    }

    public static Location getLocationFromConfig(YMLFile ymlFile, String key){
        FileConfiguration config = ymlFile.get();
        double x = config.getDouble(key + ".x");
        double y = config.getDouble(key + ".y");
        double z = config.getDouble(key + ".z");

        World world = Bukkit.getWorld(config.getString(key + ".world"));
        return world == null ? null : new Location(world, x, y, z);
    }

    public static void saveLocationToConfig(YMLFile ymlFile, String key, Location location){
        FileConfiguration config = ymlFile.get();

        config.set(key + ".x", location.getX());
        config.set(key + ".y", location.getY());
        config.set(key + ".z", location.getZ());
        config.set(key + ".world", location.getWorld().getName());

        ymlFile.save();
    }
}