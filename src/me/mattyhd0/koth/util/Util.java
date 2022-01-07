package me.mattyhd0.koth.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    public static String color(String text){

        text = text.replaceAll("&#", "#");

        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(text);

        if(text.length() > 0){

            while (matcher.find()) {

                String color = text.substring(matcher.start(), matcher.end());

                try {
                    text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
                } catch (NoSuchMethodError e){
                    text = text.replace(color, "");
                }

                matcher = pattern.matcher(text);

            }

        }

        return ChatColor.translateAlternateColorCodes('&', text);

    }

    public static boolean locationIsInZone(Location location, Location position1, Location position2){

        int locationX = (int) location.getX();
        int locationY = (int) location.getY();
        int locationZ = (int) location.getZ();

        int position1X = (int) position1.getX();
        int position1Y = (int) position1.getY();
        int position1Z = (int) position1.getZ();

        int position2X = (int) position2.getX();
        int position2Y = (int) position2.getY();
        int position2Z = (int) position2.getZ();

        if(position2X > position1X){
            int temp = position1X;
            position1X = position2X;
            position2X = temp;
        }

        if(position2Y > position1Y){
            int temp = position1Y;
            position1Y = position2Y;
            position2Y = temp;
        }

        if(position2Z > position1Z){
            int temp = position1Z;
            position1Z = position2Z;
            position2Z = temp;
        }

        return (
                        (locationX <= position1X  && locationX >= position2X) &&
                        (locationY <= position1Y && locationY >= position2Y) &&
                        (locationZ <= position1Z && locationZ >= position2Z)
                );

    }

    public static Location getCenterFrom(Location position1, Location position2){

        int position1X = (int) position1.getX();
        int position1Y = (int) position1.getY();
        int position1Z = (int) position1.getZ();

        int position2X = (int) position2.getX();
        int position2Y = (int) position2.getY();
        int position2Z = (int) position2.getZ();

        if(position2X > position1X){
            int temp = position1X;
            position1X = position2X;
            position2X = temp;
        }

        if(position2Y > position1Y){
            int temp = position1Y;
            position1Y = position2Y;
            position2Y = temp;
        }

        if(position2Z > position1Z){
            int temp = position1Z;
            position1Z = position2Z;
            position2Z = temp;
        }

        return new Location(position1.getWorld(), (position1X+position2X)/2, (position1Y+position2Y)/2, (position1Z+position2Z)/2);

    }

    public static String getItemName(ItemStack itemStack){

        ItemMeta itemMeta = itemStack.getItemMeta();

        if(itemMeta != null && itemMeta.getDisplayName() != null){
            return itemStack.getItemMeta().getDisplayName();
        } else {
            return getNameFromId(itemStack.getType().toString());
        }
    }

    public static String getNameFromId(String string){

        //Divide la id EJEMPLO: DIAMOND_SWORD en el array {"DIAMOND", "SWORD"}
        String[] strings = string.split("_");
        String finalString = "";

        //Transforma el array {"DIAMOND", "SWORD"} en el String "Diamond Sword "
        for(String str: strings){
            str = str.toLowerCase();
            str = str.replaceFirst(Character.toString(str.charAt(0)), Character.toString(str.charAt(0)).toUpperCase());
            finalString = finalString+str+" ";
        }

        //Elimina el " " (espacio) al final del texto si este existe
        if(finalString.charAt(finalString.length()-1) == ' ') finalString = finalString.substring(0, finalString.length()-1);

        return finalString;

    }

    public static Location getLocationFromConfig(YMLFile ymlFile, String key){

        FileConfiguration config = ymlFile.get();
        double x, y, z;

        x = config.getDouble(key+".x");
        y = config.getDouble(key+".y");
        z = config.getDouble(key+".z");

        World world = Bukkit.getWorld(config.getString(key+".world"));

        if(world != null) return new Location(world, x, y, z);
        return null;

    }

    public static void saveLocationToConfig(YMLFile ymlFile, String key, Location location){

        FileConfiguration config = ymlFile.get();

        config.set(key+".x", location.getX());
        config.set(key+".y", location.getY());
        config.set(key+".z", location.getZ());
        config.set(key+".world", location.getWorld().getName());

        ymlFile.save();

    }

}
