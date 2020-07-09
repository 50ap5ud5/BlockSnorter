package me.soapsuds.block_snorter.file;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import me.soapsuds.block_snorter.BlockSnorter;
import me.soapsuds.block_snorter.LConfig;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
/**
 * Author: Spectre0987 and 50ap5ud5
 */
@Mod.EventBusSubscriber(modid = BlockSnorter.MODID)
public class Writer {
	private static DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy");
	private static File file;
	private static File folderDir;
	public static FileWriter writer;
	private static String parent;

	@SubscribeEvent
	public static void onServerStart(FMLServerStartingEvent event) {
		LocalDate dateObj = LocalDate.now();
		try {
		     parent = event.getServer().getDataDirectory().getCanonicalPath();
		} catch (IOException e) {
			e.printStackTrace();
		}

		folderDir = Paths.get(parent + "//" + LConfig.CONFIG.folderDirectory.get()).toFile();

		try {
			if(!folderDir.exists()) {
				folderDir.mkdirs();
				System.out.println("Log Folder created at: " + folderDir);
			}
		}
		catch(SecurityException e) {
			e.printStackTrace();
		}

		file = Paths.get(folderDir.toString(), "//" + LConfig.CONFIG.fileName.get() + "-" + dateObj.format(format)  + ".txt").toFile();

		try {
			if(!file.exists()) {
				file.createNewFile();
				System.out.println("Created new Log File: " + file);
			}
			else {
				System.out.println("Log File Exists at: " + file);
			}
			writer = new FileWriter(file);
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public static void write(String str) {
		try {
			writer.write(str + "\n");
			writer.flush();
		}
		catch(IOException e){
			e.printStackTrace();
		}
	}
}

