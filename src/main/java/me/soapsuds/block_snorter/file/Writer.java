package me.soapsuds.block_snorter.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.TimeZone;

import me.soapsuds.block_snorter.BlockSnorter;
import me.soapsuds.block_snorter.LConfig;
import me.soapsuds.block_snorter.constants.Constants.WriteType;
import me.soapsuds.block_snorter.helper.Helper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
/**
 * Author: Spectre0987 and 50ap5ud5
 */
@Mod.EventBusSubscriber(modid = BlockSnorter.MODID)
public class Writer {
   public static File LOG_FOLDER_DIR = new File(LConfig.CONFIG.logFolderDirectory.get() + "//");
   public static File LOG_FILE;
   public static File TIMEZONES_FOLDER_DIR;
   public static File TIMEZONE_LIST;
   public static FileWriter LOG_WRITER;
   public static FileWriter timeZoneListWriter;
   public static WriteType type;
   public static String parent;

   @SubscribeEvent
   public static void onServerStart(FMLServerStartingEvent event) {
	  genLogFile();
      try {
		parent = event.getServer().getDataDirectory().getCanonicalPath();
	  } catch (IOException e1) {
		e1.printStackTrace();
	  }
      TIMEZONES_FOLDER_DIR = Paths.get(parent + "//config").toFile();
      try {
         if (!TIMEZONES_FOLDER_DIR.exists()) {
            TIMEZONES_FOLDER_DIR.mkdirs();
            System.out.println("Generated Minecraft Server Config folder at: " + TIMEZONES_FOLDER_DIR);
         }
      }
      catch(SecurityException e) {
         e.printStackTrace();
      }
      TIMEZONE_LIST = Paths.get(TIMEZONES_FOLDER_DIR.toString(), "//" +  "block_snorter-timezones.txt").toFile();
      try {
         if (!TIMEZONE_LIST.exists()) {
            TIMEZONE_LIST.createNewFile();
            System.out.println("Create Timezone List File: " + TIMEZONE_LIST);
            timeZoneListWriter = new FileWriter(TIMEZONE_LIST);

            String[] ids = TimeZone.getAvailableIDs();
            for (String id : ids) {
               writeTimeZones(Helper.displayTimeZone(TimeZone.getTimeZone(id)));
            }
            System.out.println("Finished generating Timezone List");
         }
         else if (TIMEZONE_LIST.exists()){
            System.out.println("Timezone List File Exists at: " + TIMEZONE_LIST);
         }
      }
      catch(IOException e) {
         e.printStackTrace();
      }
   }

   public static void writeToLog(String str) {
      try {
         LOG_WRITER.write(str + "\n");
         LOG_WRITER.flush();
      }
      catch(IOException e){
         e.printStackTrace();
      }
   }

   private static void writeTimeZones(String str) {
      try {
         timeZoneListWriter.write(str + "\n");
         timeZoneListWriter.flush();
      }
      catch(IOException e){
         e.printStackTrace();
      }
   }
   
   private static void genLogFile() {
	  type = LConfig.CONFIG.fileExtensionType.get().contains(WriteType.CSV.name()) ? WriteType.CSV : WriteType.TXT;
      try {
         if(!LOG_FOLDER_DIR.exists()) {
            LOG_FOLDER_DIR.mkdirs();
            System.out.println("Log Folder created at: " + LOG_FOLDER_DIR);
         }
      }
      catch(SecurityException e) {
         e.printStackTrace();
      }
      LOG_FILE = new File(LOG_FOLDER_DIR + "//" + LConfig.CONFIG.fileName.get() + "-" + Helper.timeStampAtTimeZone() + "." + type.name().toLowerCase());
      try {
          if(!LOG_FILE.exists()) {
              LOG_FILE.createNewFile();
              System.out.println("Created new Log File at: " + LOG_FILE);
          }
          else if (LOG_FILE.exists()){
              System.out.println("Log File Exists at: " + LOG_FILE);
          }
          LOG_WRITER = new FileWriter(LOG_FILE);
          writeToLog(Helper.genHeaders(type));
       }
      catch(IOException e) {
          e.printStackTrace();
          System.out.println("ERROR: Log File of: " + LOG_FILE + " could not be created!");
      }
   }

}

