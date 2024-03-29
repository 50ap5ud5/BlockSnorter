package me.soapsuds.block_snorter.helper;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import me.soapsuds.block_snorter.LConfig;
import me.soapsuds.block_snorter.constants.Constants.LogType;
import me.soapsuds.block_snorter.constants.Constants.WriteType;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;

/**
 * Created by 50ap5ud5
 * on 8 Jul 2020 @ 3:19:25 pm
 */
public class Helper {

   /**
    * Provides a timestamp with ability to format the date-time format and timezone ID
    * @param dateTimeFormat
    * @param timeZoneId
    * @return
    */
   public static String timeStampAtTimeZone(String dateTimeFormat, String timeZoneId) {
      Date date = new Date();
      Instant instant = date.toInstant(); //get timestamp at this instant
      long epochSeconds = instant.atZone(ZoneId.of(timeZoneId)).toEpochSecond(); //convert to epochSeconds (UTC)
      DateTimeFormatter format = DateTimeFormatter.ofPattern(dateTimeFormat); //Prepare Formatter
      ZonedDateTime zonedDateTime = LocalDateTime.ofEpochSecond(epochSeconds, 0,
                                OffsetDateTime.now(ZoneId.of(timeZoneId)).getOffset()).atZone(ZoneId.of(timeZoneId)); //Transform epochseconds to user defined timezone
       return zonedDateTime.format(format) + "-" + timeZoneId;
   }

   /**
    * Returns a timestamp based on config defined date format and timezone
    * @return
    */
   public static String timeStampAtTimeZone() {
        String dt_format = LConfig.CONFIG.dateTimeFormat.get();
        String timeZone = LConfig.CONFIG.timeZoneId.get();
        return Helper.timeStampAtTimeZone(dt_format, timeZone);
	}

   /**
    * Get Timezone list
    * @param tz
    * @return
    * @Note Referenced from: https://mkyong.com/java/java-display-list-of-timezone-with-gmt/
    */
   public static String displayTimeZone(TimeZone tz) {
      long hours = TimeUnit.MILLISECONDS.toHours(tz.getRawOffset());
      long minutes = TimeUnit.MILLISECONDS.toMinutes(tz.getRawOffset())
                                 - TimeUnit.HOURS.toMinutes(hours);
      // avoid -4:-30 issue
      minutes = Math.abs(minutes);

      String result = "";
      if (hours > 0) {
         result = String.format("(GMT+%d) %s", hours, tz.getID());
      } 
      if (hours == 0) {
    	  result = String.format("(GMT) %s", tz.getID());
      }
      else if (hours < 0){
         result = String.format("(GMT%d) %s", hours, tz.getID());
      }
      return result;
   }

   /**
    * Checks if the block being broken matches config file values
    * @param block
    * @return
    */
   public static boolean doesBlockBrokenMatchFilter(Block block) {
      return LConfig.CONFIG.blockBreakTypes.get().stream().anyMatch(name ->
         block.getRegistryName().toString().equals(name)
      );
   }

   /**
    * Checks if the block being placed matches config file values
    * @param block
    * @return
    */
   public static boolean doesBlockPlacedMatchFilter(Block block) {
      return LConfig.CONFIG.blockPlaceTypes.get().stream().anyMatch(name ->
         block.getRegistryName().toString().contentEquals(name)
      );
   }

   /**
    * Checks if the block of which an item is used on matches config file values
    * @param block
    * @return
    */
   public static boolean doesBlockInteractedMatchFilter(Block block) {
      return LConfig.CONFIG.blockInteractTypes.get().stream().anyMatch(name ->
         block.getRegistryName().toString().contentEquals(name)
      );
   }

   /**
    * Checks if item being right clicked matches that of config defined items
    * @param item
    * @return
    */
   public static boolean doesItemUsedMatchFilter(Item item) {
      return LConfig.CONFIG.itemUseTypes.get().stream().anyMatch(name ->
      item.getRegistryName().toString().contentEquals(name)
      );
   }

   /**
    * Checks if destination dimension matches that of config defined values
    * @param type
    * @return
    */
   public static boolean doesDimensionDestinationMatchFilter(RegistryKey<World> world) {
      return LConfig.CONFIG.dimChangeDestinations.get().stream().anyMatch(name ->
      world.location().toString().contentEquals(name));
   }
   
   public static boolean doesDimensionDestinationTypeMatchFilter(RegistryKey<DimensionType> world) {
      return LConfig.CONFIG.dimChangeDestinationTypes.get().stream().anyMatch(name ->
      world.location().toString().contentEquals(name));
   }

   /**
    * Checks if container type being opened/closed match config defined values
    * @param type
    * @return
    */
   public static boolean doesContainerTypeMatchFilter(ContainerType<?> type) {
      return LConfig.CONFIG.containerTypes.get().stream().anyMatch(name ->
      type.getRegistryName().toString().contentEquals(name));
   }

   /**
    * Gets the Raytrace result of the entity's look vector
    * @Note Referenced from https://gitlab.com/Spectre0987/TardisMod-1-14/-/blob/master/src/main/java/net/tardis/mod/helper/PlayerHelper.java
    * @param entity
    * @param distance
    * @return
    */
   public static RayTraceResult getRTLookingAt(Entity entity, double distance) {
      Vector3d lookVec = entity.getLookAngle();
      for (int i = 0; i < distance * 2; i++) {
          float scale = i / 2F;
          Vector3d pos = entity.position().add(0, entity.getEyeHeight(), 0).add(lookVec.scale(scale));
          BlockPos bPos = new BlockPos(pos);
          if (entity.level.getBlockState(bPos).getCollisionShape(entity.level, new BlockPos(pos)) != VoxelShapes.empty()
               && !entity.level.getBlockState(bPos).isAir(entity.level, bPos)) {
              return new BlockRayTraceResult(pos, Direction.getNearest(pos.x, pos.y, pos.z), bPos, false);
          } else {
        	  Vector3d min = pos.add(0.25F, 0.25F, 0.25F);
        	  Vector3d max = pos.add(-0.25F, -0.25F, -0.25F);
              for (Entity e : entity.level.getEntities(entity, new AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z))) {
                  return new EntityRayTraceResult(e);
              }
          }
      }
      return null;
   }
   /**
    * Helper method to construct a log entry based on multiple parameter.
    * @param entity
    * @param logType
    * @param auditObjectName
    * @param currentDimension
    * @param posX
    * @param posY
    * @param posZ
    * @param timeStamp
    * @param type
    * @return
    */
   public static String constructEntry(String timeStamp, Entity entity, LogType logType, String auditObjectName, RegistryKey<World> currentDimension, String posX, String posY, String posZ, WriteType type) {
	   String logEntry = "";
	   String delimiter = "";
	   String entityName = entity.getDisplayName().getString();
	   String dimName = currentDimension.location().toString();
	   String[] entries = new String[]{timeStamp, entityName, entity.getUUID().toString(), logType.name(), auditObjectName, dimName, posX, posY, posZ};
	   switch(type) {
           case TXT:
              delimiter = " ";
              logEntry = String.join(delimiter, entries);
              break;
           case CSV:
              delimiter = ",";
              logEntry = String.join(delimiter, entries);
              break;
           default:
              delimiter = " ";
              logEntry = String.join(delimiter, entries);
              break;
       }
       return logEntry;
   }
   /**
    * Helper method to construct a log entry based on multiple parameter. Overloads construct Entry to allow for auditing of 2 objects
    * @param entity
    * @param logType
    * @param auditObjectName
    * @param auditObject2
    * @param currentDimension
    * @param posX
    * @param posY
    * @param posZ
    * @param timeStamp
    * @param type
    * @return
    */
   public static String constructEntry2(String timeStamp, Entity entity, LogType logType, String auditObject, String auditObject2, RegistryKey<World> currentDimension, String posX, String posY, String posZ, WriteType type) {
	   String logEntry = "";
	   String delimiter = "";
	   String entityName = entity.getDisplayName().getString();
	   String dimName = currentDimension == null ? "N_A" : currentDimension.location().toString();
	   String[] entries = new String[]{timeStamp, entityName, entity.getUUID().toString(), logType.name(), auditObject, auditObject2.isEmpty() ? "N_A" : auditObject2, dimName, posX, posY, posZ};
	   switch(type) {
           case TXT:
              delimiter = " ";
              logEntry = String.join(delimiter, entries);
              break;
           case CSV:
              delimiter = ",";
              logEntry = String.join(delimiter, entries);
              break;
           default:
              delimiter = " ";
              logEntry = String.join(delimiter, entries);
              break;
       }
       return logEntry;
   }

   public static String genHeaders(WriteType type) {
	   String logEntry = "";
	   String delimiter = "";
	   String[] entries = new String[]{"Timestamp","EntityName", "EntityUUID", "ActionType", "USING/FROM", "ON/TO", "ObjectCurrentDimension", "posX", "posY", "posZ"};
	   switch(type) {
       case TXT:
          delimiter = " ";
          logEntry = String.join(delimiter, entries);
          break;
       case CSV:
           delimiter = ",";
           logEntry = String.join(delimiter, entries);
           break;
       default:
           delimiter = " ";
           logEntry = String.join(delimiter, entries);
           break;
	   }
	   return logEntry;
   }

   public static String escapeSpecialCharacters(String data) {
       String escapedData = data.replaceAll("\\R", " ");
       if (data.contains(",") || data.contains("\"") || data.contains("'")) {
           data = data.replace("\"", "\"\"");
           escapedData = "\"" + data + "\"";
       }
       return escapedData;
   }

}
