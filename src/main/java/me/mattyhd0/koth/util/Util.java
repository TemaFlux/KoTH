package me.mattyhd0.koth.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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

    public static List<String> coloredList(Collection<String> list){
        List<String> coloredList = new ArrayList<>();

        for(String line: list){
            coloredList.add(color(line));
        }

        return coloredList;

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

        /*System.out.println(
                MessageFormat.format("{0} {1} {2} > {3} {4} {5} | {6} {7} {8}", position1X, position1Y, position1Z, position2X, position2Y, position2Z, locationX, locationY, locationZ)
        );*/

        return (
                        (position1X >= locationX  && locationX >= position2X) &&
                        (position1Y >= locationY && locationY >= position2Y) &&
                        (position1Z >= locationZ && locationZ >= position2Z)
                );

    }

    public static Location getCenterFrom(Location position1, Location position2){

        double position1X = Math.max(position1.getX(), position2.getX());
        double position1Y = Math.max(position1.getY(), position2.getY());
        double position1Z = Math.max(position1.getZ(), position2.getZ());

        double position2X = Math.min(position1.getX(), position2.getX());
        double position2Y = Math.min(position1.getY(), position2.getY());
        double position2Z = Math.min(position1.getZ(), position2.getZ());

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
