package me.soapsuds.block_snorter.events;

import me.soapsuds.block_snorter.BlockSnorter;
import me.soapsuds.block_snorter.LConfig;
import me.soapsuds.block_snorter.file.Writer;
import me.soapsuds.block_snorter.helper.Helper;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
/**
 *
 * Authors: Spectre0987, 50ap5ud5
 */
@Mod.EventBusSubscriber(modid = BlockSnorter.MODID)
public class Events {

	@SubscribeEvent
	public static void logBlockBreak(BlockEvent.BreakEvent event) {
		if (LConfig.CONFIG.logBlockBreak.get()) {
			if (LConfig.CONFIG.filterBlockBreakTypes.get()) {
				if (Helper.doesBlockBrokenMatchFilter(event.getState().getBlock())) {
					Writer.writeToLog(event.getPlayer().getName().getFormattedText()
							+ " with UUID: " + event.getPlayer().getUniqueID()
							+ " in dimension " + DimensionType.getKey(event.getPlayer().dimension)
							+ " broke block " + event.getState()
							+ " at " + event.getPos()
							+ " on " + Helper.timeStampAtTimeZone());
					return;
				}
			}
			else {
				Writer.writeToLog(event.getPlayer().getName().getFormattedText()
						+ " with UUID: " + event.getPlayer().getUniqueID()
						+ " in dimension " + DimensionType.getKey(event.getPlayer().dimension)
						+ " broke block " + event.getState()
						+ " at " + event.getPos()
						+ " on " + Helper.timeStampAtTimeZone());
			}
		}
	}

	//Logs entity place block
	@SubscribeEvent
	public static void logBlockPlace(BlockEvent.EntityPlaceEvent event) {
		if (LConfig.CONFIG.logBlockPlace.get()) {
			if(event.getEntity() instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
				if (LConfig.CONFIG.filterBlockPlaceTypes.get()) {
					if (Helper.doesBlockPlacedMatchFilter(event.getState().getBlock())) {
						Writer.writeToLog(player.getName().getFormattedText()
								+ " with UUID: " + player.getUniqueID()
								+ " in dimension " + DimensionType.getKey(player.dimension)
								+ " placed block " + event.getState()
								+ " at " + event.getPos()
								+ " on " + Helper.timeStampAtTimeZone());
						return;
					}
				}
				else {
					Writer.writeToLog(player.getName().getFormattedText() + " with UUID: "
							+ player.getUniqueID() + " placed block " + event.getState()
							+ " in dimension " + DimensionType.getKey(player.dimension)
							+ " at " + event.getPos()
							+ " on " + Helper.timeStampAtTimeZone());
				}
			}
		}
	}

	//Logs entity item right click function
	@SubscribeEvent
	public static void logInteractions(PlayerInteractEvent.RightClickBlock event) {
		if (LConfig.CONFIG.logItemUseOnBlock.get()) {
			if(event.getEntity() instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
				BlockPos blockpos = event.getPos();
				if (LConfig.CONFIG.filterByBlockUseTypes.get()) {
					if (Helper.doesBlockInteractedMatchFilter(event.getWorld().getBlockState(blockpos).getBlock())) {
						Writer.writeToLog(player.getName().getFormattedText() + " with UUID: "
								+ player.getUniqueID() + " used " + event.getItemStack().getItem()
								+ " on " + event.getWorld().getBlockState(blockpos)
								+ " at " + event.getPos()
								+ " in  " + DimensionType.getKey(player.dimension)
								+ " on " + Helper.timeStampAtTimeZone());
						return;
					}
				}
				else if (LConfig.CONFIG.filterByItemUseTypes.get()) {
					if (Helper.doesItemUsedMatchFilter(event.getItemStack().getItem())) {
						Writer.writeToLog(player.getName().getFormattedText() + " with UUID: "
								+ player.getUniqueID() + " used " + event.getItemStack().getItem()
								+ " on " + event.getWorld().getBlockState(blockpos)
								+ " at " + event.getPos()
								+ " in  " + DimensionType.getKey(player.dimension)
								+ " on " + Helper.timeStampAtTimeZone());
						return;
					}
				}
				else {
					Writer.writeToLog(player.getName().getFormattedText() + " with UUID: "
							+ player.getUniqueID() + " used " + event.getItemStack().getItem()
							+ " on " + event.getWorld().getBlockState(blockpos)
							+ " at " + event.getPos()
							+ " in  " + DimensionType.getKey(player.dimension)
							+ " on " + Helper.timeStampAtTimeZone());
				}
			}
		}
	}

	@SubscribeEvent
	public static void logContainerOpen(PlayerContainerEvent.Open event) { //TODO: Get Container position rather than rely on raytrace look vector
		if (LConfig.CONFIG.logContainerUse.get()) {
			if(event.getEntity() instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
				if (LConfig.CONFIG.filterContainerTypes.get()) {
					if (Helper.doesContainerTypeMatchFilter(event.getContainer().getType())) {
						Writer.writeToLog(player.getName().getFormattedText() + " with UUID: " + player.getUniqueID()
						+ " opened container type " + event.getContainer().getType().getRegistryName()
						+ " in dimension " + DimensionType.getKey(player.dimension)
						+ " at " + Helper.getRTLookingAt(player,5).getHitVec()
						+ " on " + Helper.timeStampAtTimeZone());
						return;
					}
				}
				else {
					Writer.writeToLog(player.getName().getFormattedText() + " with UUID: " + player.getUniqueID()
					+ " opened container type " + event.getContainer().getType().getRegistryName()
					+ " in dimension " + DimensionType.getKey(player.dimension)
					+ " at " + Helper.getRTLookingAt(player,5).getHitVec()
					+ " on " + Helper.timeStampAtTimeZone());
				}
			}
		}
	}

	@SubscribeEvent
	public static void logContainerClose(PlayerContainerEvent.Close event) {//TODO: Get Container position rather than rely on raytrace look vector
		if (LConfig.CONFIG.logContainerUse.get()) {
			if(event.getEntity() instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
				if (LConfig.CONFIG.filterContainerTypes.get()) {
					if (Helper.doesContainerTypeMatchFilter(event.getContainer().getType())) {
						Writer.writeToLog(player.getName().getFormattedText() + " with UUID: " + player.getUniqueID()
						+ " closed container type " + event.getContainer().getType().getRegistryName()
						+ " in dimension " + DimensionType.getKey(player.dimension)
						+ " at " + Helper.getRTLookingAt(player,5).getHitVec()
						+ " on " + Helper.timeStampAtTimeZone());
						return;
					}
				}
				else {
					Writer.writeToLog(player.getName().getFormattedText() + " with UUID: " + player.getUniqueID()
					+ " closed container type " + event.getContainer().getType().getRegistryName()
					+ " in dimension " + DimensionType.getKey(player.dimension)
					+ " at " + Helper.getRTLookingAt(player,5).getHitVec()
					+ " on " + Helper.timeStampAtTimeZone());
				}
			}
		}
	}

	@SubscribeEvent
	public static void logPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
		if(event.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
			if (LConfig.CONFIG.filterDimDestinationTypes.get()) {
				if (Helper.doesDimensionDestinationMatchFilter(event.getTo())) {
					Writer.writeToLog(player.getName().getFormattedText() + " with UUID: " + player.getUniqueID()
					+ " changed dimension from " + event.getFrom()
					+ " to " + event.getTo()
					+ " on " + Helper.timeStampAtTimeZone());
					return;
				}
			}
			else {
				Writer.writeToLog(player.getName().getFormattedText() + " with UUID: " + player.getUniqueID()
				+ " changed dimension from " + event.getFrom()
				+ " to " + event.getTo()
				+ " on " + Helper.timeStampAtTimeZone());
			}
		}
	}
}