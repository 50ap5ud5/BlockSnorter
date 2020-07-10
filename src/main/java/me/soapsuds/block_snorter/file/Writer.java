package me.soapsuds.block_snorter.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.TimeZone;

import me.soapsuds.block_snorter.BlockSnorter;
import me.soapsuds.block_snorter.LConfig;
import me.soapsuds.block_snorter.helper.Helper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
/**
 * Author: Spectre0987 and 50ap5ud5
 */
@Mod.EventBusSubscriber(modid = BlockSnorter.MODID)
public class Writer {
	private static File file;
	private static File timeZonesFile;
	private static File subFolderDir;
	private static File timeZoneFolderDir;
	public static FileWriter writer;
	public static FileWriter timeZoneListWriter;
	private static String parent;

	@SubscribeEvent
	public static void onServerStart(FMLServerStartingEvent event) {
		try {
		     parent = event.getServer().getDataDirectory().getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		subFolderDir = Paths.get(parent + "//" + LConfig.CONFIG.subFolderDirectory.get()).toFile();
		timeZoneFolderDir = Paths.get(parent + "//config").toFile();
		try {
			if(!subFolderDir.exists()) {
				subFolderDir.mkdirs();
				System.out.println("Log Folder created at: " + subFolderDir);
			}
			if (!timeZoneFolderDir.exists()) {
				timeZoneFolderDir.mkdirs();
				System.out.println("Generated Minecraft Server Config folder at: " + subFolderDir);
			}
		}
		catch(SecurityException e) {
			e.printStackTrace();
		}

		file = Paths.get(subFolderDir.toString(), "//" + LConfig.CONFIG.fileName.get() + "-" + Helper.timeStampAtTimeZone() + ".txt").toFile();
		timeZonesFile = Paths.get(timeZoneFolderDir.toString(), "//" +  "block_snorter-timezones.txt").toFile();
		try {
			if(!file.exists()) {
				file.createNewFile();
				System.out.println("Created new Log File at: " + file);
			}
			if (!timeZonesFile.exists()) {
				timeZonesFile.createNewFile();
				System.out.println("Create Timezone List File: " + timeZonesFile);
				timeZoneListWriter = new FileWriter(timeZonesFile);

				String[] ids = TimeZone.getAvailableIDs();
				for (String id : ids) {
					writeTimeZones(Helper.displayTimeZone(TimeZone.getTimeZone(id)));
				}
				System.out.println("Finished generating Timezone List");
			}
			else {
				if (file.exists()){
					System.out.println("Log File Exists at: " + file);
				}
				if (timeZonesFile.exists()){
					System.out.println("Timezone List File Exists at: " + timeZonesFile);
				}
			}
			writer = new FileWriter(file);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void writeToLog(String str) {
		try {
			writer.write(str + "\n");
			writer.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

	public static void writeTimeZones(String str) {
		try {
			timeZoneListWriter.write(str + "\n");
			timeZoneListWriter.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}

}

