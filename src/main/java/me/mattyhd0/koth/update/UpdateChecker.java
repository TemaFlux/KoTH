package me.mattyhd0.koth.update;

import com.google.gson.Gson;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class UpdateChecker {

    private final String version;
    private final int spigotResourceId;
    private SpigotResource spigotResource;
    private String latestVersion;

    public UpdateChecker(@NotNull Plugin plugin, int spigotResourceId){

        try {

            URL urlObject = new URL("https://api.spigotmc.org/simple/0.2/index.php?action=getResource&id="+spigotResourceId);
            URLConnection urlConnection = urlObject.openConnection();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String response = "";
            String line;

            while ((line = bufferedReader.readLine()) != null) {

                response = response + line;

            }

            bufferedReader.close();
            SpigotResource spigotResourceTmp = new Gson().fromJson(response, SpigotResource.class);
            spigotResourceTmp.spigotResourceId = spigotResourceId;
            spigotResource = spigotResourceTmp;
        } catch (IOException e){
            spigotResource = null;
        }

        this.spigotResourceId = spigotResourceId;
        version = plugin.getDescription().getVersion();

    }

    public boolean isRunningLatestVersion() {
        return version.equals(spigotResource.getCurrentVersion());
    }

    public String getVersion() {
        return version;
    }
    
    public String getLatestVersion() {
        return latestVersion;
    }

    public SpigotResource getSpigotResource() {
        return spigotResource;
    }

    public boolean requestIsValid() {
        return spigotResource != null;
    }

    public static class SpigotResource{

        private int spigotResourceId;
        private String current_version;
        private String title;

        public String getCurrentVersion() {
            return current_version;
        }

        public String getDownloadUrl(){

            String url = ("https://www.spigotmc.org/resources/"+title+"."+spigotResourceId+"/").replaceAll(" ", "-");
            String validChars = "abcdefghijklmnlopqrstuvwxyz"+"abcdefghijklmnlopqrstuvwxyz".toUpperCase()+"0123456789:/.-_";
            String validUrl = "";

            for(int i = 0; i < url.length(); i++){

                String c = Character.toString(url.charAt(i));

                if(validChars.contains(c)){
                    validUrl = validUrl+c;
                }

            }

            return validUrl;

        }
    }

}
