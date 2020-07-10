package me.soapsuds.block_snorter;


import java.util.List;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;
/**
 *
 * Created by 50ap5ud5
 * on 8 Jul 2020 @ 10:00:15 pm
 */
public class LConfig {

	public static final LConfig CONFIG;
    public static final ForgeConfigSpec CONFIG_SPEC;

    static {
        final Pair<LConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(LConfig::new);
        CONFIG = specPair.getLeft();
        CONFIG_SPEC = specPair.getRight();
    }
    public ConfigValue<String> subFolderDirectory;
    public ConfigValue<String> fileName;
    public ConfigValue<String> dateTimeFormat;
    public ConfigValue<String> timeZoneId;

    public ForgeConfigSpec.BooleanValue logBlockBreak;
    public ForgeConfigSpec.BooleanValue filterBlockBreakTypes;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> blockBreakTypes;

    public ForgeConfigSpec.BooleanValue logBlockPlace;
    public ForgeConfigSpec.BooleanValue filterBlockPlaceTypes;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> blockPlaceTypes;

    public ForgeConfigSpec.BooleanValue logItemUseOnBlock;
    public ForgeConfigSpec.BooleanValue filterByItemUseTypes;
    public ForgeConfigSpec.BooleanValue filterByBlockUseTypes;

    public ForgeConfigSpec.ConfigValue<List<? extends String>> itemUseTypes;
    public ForgeConfigSpec.BooleanValue filterBlockInteractTypes;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> blockInteractTypes;

    public ForgeConfigSpec.BooleanValue logBlockDimChange;
    public ForgeConfigSpec.BooleanValue filterDimDestinationTypes;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> dimChangeDestinationTypes;

    public ForgeConfigSpec.BooleanValue logContainerUse;
    public ForgeConfigSpec.BooleanValue filterContainerTypes;
    public ForgeConfigSpec.ConfigValue<List<? extends String>> containerTypes;

    public LConfig(ForgeConfigSpec.Builder builder) {

      builder.push("Log File Configurations");
      subFolderDirectory = builder.translation("config.block_snorter.folder_path").comment("Defines subfolder path for log files to be created in", "Generates this subfolder under Minecraft Server install folder").define("subFolderDirectory", "auditing/block_logs", String.class::isInstance);
      fileName = builder.translation("config.block_snorter.file_name").comment("Defines file name for log files.", "A creation date timestamp will be appended to this name").define("fileName", "log", String.class::isInstance);
      dateTimeFormat = builder.translation("config.block_snorter.date_time_format").comment("Defines Date-Time Format for Timestamps used in log file name and entries").define("dateTimeFormat", "dd-MM-yyyy_HH-mm-ss", String.class::isInstance);
      timeZoneId = builder.translation("config.block_snorter.time_zone_id").comment("Defines Timezone Offset for log files.","List of Valid Ids are in the block_snorter-timezones.txt file located in your config folder").define("timeZoneId", "GMT", String.class::isInstance);
      builder.pop();

      builder.push("Block Audit"); //Block Log Start

      builder.push("Block Break");
      logBlockBreak = builder.translation("config.block_snorter.block.break").comment("Toggles Whether to log block break").define("logBlockBreak", true);
      filterBlockBreakTypes = builder.translation("config.block_snorter.block.break.filter").comment("Toggles filtering of block break logging by block type").define("filterBlockBreakTypes", false);
      blockBreakTypes = builder.translation("config")
           .comment("List of blocks that the logger should listen for when broken")
           .defineList("blockBreakTypes", Lists.newArrayList("minecraft:grass"), String.class::isInstance);
      builder.pop();

      builder.push("Block Place");
      logBlockPlace = builder.translation("config.block_snorter.block.place").comment("Toggles Whether to log block placement").define("logBlockPlace", true);
      filterBlockPlaceTypes = builder.translation("config.block_snorter.block.place.filter").comment("Toggles filtering of block place logs by block type").define("filterBlockPlaceTypes", false);
      blockPlaceTypes = builder.translation("config.block_snorter.blockPlaceTypes")
           .comment("List of blocks that the logger should listen for when placed")
           .defineList("blockPlaceTypes", Lists.newArrayList("minecraft:tnt", "minecraft:command_block","minecraft:bedrock","minecraft:chain_command_block", "minecraft:repeating_command_block"), String.class::isInstance);
      builder.pop();

      builder.push("Item Use on Block");
      logItemUseOnBlock = builder.translation("config.block_snorter.block.item_use").comment("Toggles whether to log item used on blocks").define("logItemUseOnBlock", true);
      filterByItemUseTypes = builder.translation("config.block_snorter.item_use.filter_item").comment("Toggles filter of item use logs by item used").define("filterByItemUseTypes", false);
      filterByBlockUseTypes = builder.translation("config.block_snorter.item_use.filter_block").comment("Toggles whether to filter by type of block the item is used on").define("filterByBlockUseTypes", false);
      blockInteractTypes = builder.translation("config.block_snorter.blockInteractTypes")
           .comment("List of blocks that the logger should listen for when something interacted with this block")
           .defineList("blockInteractTypes", Lists.newArrayList("minecraft:end_portal_frame","minecraft:end_portal","minecraft:bedrock","minecraft:obsidian","minecraft:command_block","minecraft:chain_command_block", "minecraft:repeating_command_block"), String.class::isInstance);
      itemUseTypes = builder.translation("config.block_snorter.itemUseTypes")
           .comment("List of items that the logger should listen for when the item is used to interact with a block")
           .defineList("itemUseTypes", Lists.newArrayList("minecraft:water_bucket","minecraft:lava_bucket"), String.class::isInstance);
      builder.pop();

      builder.pop(); //Block Log End

      builder.push("Dimension Audit");
      logBlockDimChange = builder.translation("config.block_snorter.dim_change").comment("Toggles whether to log player dimension change events").define("logBlockDimChange", true);
      filterDimDestinationTypes = builder.translation("config.block_snorter.dim.filter").comment("Toggles filtering of dimension change logs by destination dimension type").define("filterDimDestinationTypes", false);
      dimChangeDestinationTypes = builder.translation("config.block_snorter.dimChangeDestinationTypes")
           .comment("List of dimensions to listen for when entity travels to this dimension")
           .defineList("dimChangeDestinationTypes", Lists.newArrayList("minecraft:overworld","minecraft:the_nether","tardis"), String.class::isInstance);
        builder.pop();

      builder.push("Container Audit");
      logContainerUse = builder.translation("config.block_snorter.container.use").comment("Toggle whether to log container opening").define("logContainerOpen", true);
      filterContainerTypes = builder.translation("config.block_snorter.container.filter").comment("Toggles filtering of container use logging by container type").define("filterContainerTypes", false);
      containerTypes = builder.translation("config.block_snorter.containerTypes")
           .comment("List of container types that the audit should track when they are opened","These are NOT the namespace id of blocks")
           .defineList("containerTypes", Lists.newArrayList("minecraft:generic_9x1",
                "minecraft:generic_9x2",
                "minecraft:generic_9x3",
                "minecraft:generic_9x4",
                "minecraft:generic_9x5",
                "minecraft:generic_9x6",
                "minecraft:anvil",
                "minecraft:beacon",
                "minecraft:blast_furnace",
                "minecraft:furnace",
                "minecraft:hopper",
                "minecraft:brewing_stand",
                "minecraft:shulker_box",
                "minecraft:smoker"), String.class::isInstance);
      builder.pop();
    }
}