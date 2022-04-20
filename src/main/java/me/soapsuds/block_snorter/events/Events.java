package me.soapsuds.block_snorter.events;

import java.util.Optional;

import me.soapsuds.block_snorter.BlockSnorter;
import me.soapsuds.block_snorter.LConfig;
import me.soapsuds.block_snorter.constants.Constants.LogType;
import me.soapsuds.block_snorter.file.Writer;
import me.soapsuds.block_snorter.helper.Helper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.DimensionType;
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
			BlockPos blockpos = event.getPos();
			Block block = event.getState().getBlock();
			String x = Integer.toString(blockpos.getX());
			String y = Integer.toString(blockpos.getY());
			String z = Integer.toString(blockpos.getZ());
			if (LConfig.CONFIG.filterBlockBreakTypes.get()) {
				if (Helper.doesBlockBrokenMatchFilter(event.getState().getBlock())) {
					Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getPlayer(), LogType.BLOCK_BREAK, block.getRegistryName().toString(), "", event.getPlayer().level.dimension(), x, y, z, Writer.type));
				}
			}
			else {
				Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getPlayer(), LogType.BLOCK_BREAK, block.getRegistryName().toString(), "", event.getPlayer().level.dimension(), x, y, z, Writer.type));
			}
		}
	}

	//Logs entity place block
	@SubscribeEvent
	public static void logBlockPlace(BlockEvent.EntityPlaceEvent event) {
		if (LConfig.CONFIG.logBlockPlace.get()) {
			if(event.getEntity() instanceof ServerPlayerEntity) {
				ServerPlayerEntity player = (ServerPlayerEntity)event.getEntity();
				BlockPos blockpos = event.getPos();
				Block block = event.getWorld().getBlockState(blockpos).getBlock();
				String x = Integer.toString(blockpos.getX());
				String y = Integer.toString(blockpos.getY());
				String z = Integer.toString(blockpos.getZ());
				if (LConfig.CONFIG.filterBlockPlaceTypes.get()) {
					if (Helper.doesBlockPlacedMatchFilter(event.getState().getBlock())) {
						Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), player, LogType.BLOCK_PLACE, block.getRegistryName().toString(), "", event.getEntity().level.dimension(), x, y, z, Writer.type));
					}
				}
				else {
					Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), player, LogType.BLOCK_PLACE, block.getRegistryName().toString(), "", event.getEntity().level.dimension(), x, y, z, Writer.type));
				}
			}
		}
	}

	//Logs entity item right click function
	@SubscribeEvent
	public static void logItemRightClick(PlayerInteractEvent.RightClickBlock event) {
		if (LConfig.CONFIG.logItemUseOnBlock.get()) {
			BlockPos blockpos = event.getPos();
			Block block = event.getWorld().getBlockState(blockpos).getBlock();
			String blockName = block.getRegistryName().toString();
			String itemName = event.getItemStack().getItem().getRegistryName().toString();
			String x = Integer.toString(blockpos.getX());
			String y = Integer.toString(blockpos.getY());
			String z = Integer.toString(blockpos.getZ());
			if (LConfig.CONFIG.filterByBlockUseTypes.get()) {
				if (Helper.doesBlockInteractedMatchFilter(event.getWorld().getBlockState(blockpos).getBlock())) {
					Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getEntity(), LogType.ITEM_RIGHT_CLICK, itemName, blockName, event.getEntity().level.dimension(), x, y, z, Writer.type));
				}
			}
			else if (LConfig.CONFIG.filterByItemUseTypes.get()) {
				if (Helper.doesItemUsedMatchFilter(event.getItemStack().getItem())) {
					Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getEntity(), LogType.ITEM_RIGHT_CLICK, itemName, blockName, event.getEntity().level.dimension(), x, y, z, Writer.type));
				}
			}
			else {
				Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getEntity(), LogType.ITEM_RIGHT_CLICK, itemName, blockName, event.getEntity().level.dimension(), x, y, z, Writer.type));
			}
		}
	}

	@SubscribeEvent
	public static void logContainerOpen(PlayerContainerEvent.Open event) {
		if (LConfig.CONFIG.logContainerUse.get()) {
			if (!event.getEntity().level.isClientSide() && event.getContainer() != null) {
				RayTraceResult rt = Helper.getRTLookingAt(event.getEntity(),10D);
			    Entity ent = event.getEntity();
			    String x = "";
			    String y = "";
			    String z = "";
			    String tileOrEntityType = "";
			    String directionOrName = "";
				if (rt != null) {
					Vector3d vec = rt.getLocation();
					x = Double.toString(vec.x()); //Get hit vec values, use these as a fall back
					y = Double.toString(vec.y());
					z = Double.toString(vec.z());
					if (rt instanceof BlockRayTraceResult) {
						BlockRayTraceResult brt = (BlockRayTraceResult)rt;
						x = Integer.toString(brt.getBlockPos().getX()); //Get exact block pos, for pretty printing
						y = Integer.toString(brt.getBlockPos().getY());
						z = Integer.toString(brt.getBlockPos().getZ());
						directionOrName = event.getEntity().getDirection().getOpposite().toString();
						TileEntity tile = event.getEntity().level.getBlockEntity(brt.getBlockPos());
						if (tile != null)
						     tileOrEntityType = tile.getType().getRegistryName().toString(); //Get the actual tile entity name, because container types cannot be read easily
						else if (tile == null && event.getContainer().getType() != null)//Handle when the block isn't a TE for some reason?
						     tileOrEntityType = event.getContainer().getType().getRegistryName().toString(); //Fallback value if we can't get TE
						else tileOrEntityType = event.getContainer().getClass().toString(); //Get container for cases when the type is null
					}
					if (rt instanceof EntityRayTraceResult) {//Handle Entities that have a container
						EntityRayTraceResult ert = (EntityRayTraceResult)rt;
						x = Integer.toString(ert.getEntity().blockPosition().getX()); //Get exact entity pos, for pretty printing
						y = Integer.toString(ert.getEntity().blockPosition().getY());
						z = Integer.toString(ert.getEntity().blockPosition().getZ());
						if (ert.getEntity().getType() != null && event.getContainer().getType() != null) {
						    tileOrEntityType = ert.getEntity().getType().getRegistryName().toString();
						    directionOrName = ert.getEntity().getDisplayName().getString(); //Get entity name to handle cases where players have named their entity containers
						}
						else {
						    tileOrEntityType = event.getEntity().getEncodeId(); //Handle cases where entity type is null
						}
					}
					if (LConfig.CONFIG.filterContainerTypes.get()) {
					    if (Helper.doesContainerTypeMatchFilter(event.getContainer().getType())) {
						    Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getEntity(), LogType.CONTAINER_OPEN, tileOrEntityType, directionOrName, event.getEntity().level.dimension(), x, y, z, Writer.type));
					    }
				    }
				    else {
				        Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getEntity(), LogType.CONTAINER_OPEN, tileOrEntityType, directionOrName, event.getEntity().level.dimension(), x, y, z, Writer.type));
				    }
				}
			    else {
			        x = Integer.toString(ent.blockPosition().getX());
			        y = Integer.toString(ent.blockPosition().getY());
			        z = Integer.toString(ent.blockPosition().getZ());
			    	directionOrName = ent.getDirection().getOpposite().getName();
			        Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getEntity(), LogType.CONTAINER_OPEN, tileOrEntityType, directionOrName, event.getEntity().level.dimension(), x, y, z, Writer.type));
			    }
		    }
		}
	}

	@SubscribeEvent
	public static void logContainerClose(PlayerContainerEvent.Close event) {
		if (LConfig.CONFIG.logContainerUse.get() && LConfig.CONFIG.logContainerClose.get()) {
			if (!event.getEntity().level.isClientSide() && event.getContainer() != null) {
				Entity ent = event.getEntity();
			    String x = "";
			    String y = "";
			    String z = "";
			    String tileOrEntityType = "";
			    String directionOrName = "";
				RayTraceResult rt = Helper.getRTLookingAt(event.getEntity(),10D);
				if (rt != null) {
					Vector3d vec = rt.getLocation();
					x = Double.toString(vec.x()); //Get hit vec values, use these as a fall back
				    y = Double.toString(vec.y());
					z = Double.toString(vec.z());
					if (rt instanceof BlockRayTraceResult) {
						BlockRayTraceResult brt = (BlockRayTraceResult)rt;
						x = Integer.toString(brt.getBlockPos().getX()); //Get exact block pos, for pretty printing
						y = Integer.toString(brt.getBlockPos().getY());
						z = Integer.toString(brt.getBlockPos().getZ());
						directionOrName = event.getEntity().getDirection().getOpposite().toString();
						TileEntity tile = event.getEntity().level.getBlockEntity(brt.getBlockPos());
						if (tile != null)
						     tileOrEntityType = tile.getType().getRegistryName().toString(); //Get the actual tile entity name, because container types cannot be read easily
						else if (tile == null && event.getContainer().getType() != null)//Handle when the block isn't a TE for some reason?
						     tileOrEntityType = event.getContainer().getType().getRegistryName().toString(); //Fallback value if we can't get TE
						else tileOrEntityType = event.getContainer().getClass().toString(); //Get container for cases when the type is null
					}
					if (rt instanceof EntityRayTraceResult) {//Handle Entities that have a container
						EntityRayTraceResult ert = (EntityRayTraceResult)rt;
						x = Integer.toString(ert.getEntity().blockPosition().getX()); //Get exact entity pos, for pretty printing
						y = Integer.toString(ert.getEntity().blockPosition().getY());
						z = Integer.toString(ert.getEntity().blockPosition().getZ());
						if (ert.getEntity().getType() != null && event.getContainer().getType() != null) {
						    tileOrEntityType = ert.getEntity().getType().getRegistryName().toString();
						    directionOrName = ert.getEntity().getDisplayName().getString(); //Get entity name to handle cases where players have named their entity containers
						}
						else {
						    tileOrEntityType = event.getEntity().getEncodeId(); //Handle cases where entity type is null
						}
					}
				    if (LConfig.CONFIG.filterContainerTypes.get()) {
					    if (Helper.doesContainerTypeMatchFilter(event.getContainer().getType())) {
						    Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getEntity(), LogType.CONTAINER_CLOSE, tileOrEntityType, directionOrName, event.getEntity().level.dimension(), x, y, z, Writer.type));
					    }
				    }
				    else {
				        Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getEntity(), LogType.CONTAINER_CLOSE, tileOrEntityType, directionOrName, event.getEntity().level.dimension(), x, y, z, Writer.type));
				    }
				}
			    else {
			    	x = Integer.toString(ent.blockPosition().getX());
			    	y = Integer.toString(ent.blockPosition().getY());
			    	z = Integer.toString(ent.blockPosition().getZ());
			    	directionOrName = ent.getDirection().getOpposite().getName();
			    	Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), event.getEntity(), LogType.CONTAINER_CLOSE, tileOrEntityType, directionOrName, ent.level.dimension(), x, y, z, Writer.type));
			    }
		    }
		}
	}

	@SubscribeEvent
	public static void logPlayerDimensionChange(PlayerEvent.PlayerChangedDimensionEvent event) {
		if(event.getEntity() instanceof ServerPlayerEntity) {
			ServerPlayerEntity player = (ServerPlayerEntity)event.getPlayer();
			BlockPos pos = player.blockPosition(); //log player position at place of dim change
			String x = Integer.toString(pos.getX());
			String y = Integer.toString(pos.getY());
			String z = Integer.toString(pos.getZ());
			if (LConfig.CONFIG.logDimChange.get()) {
				if (LConfig.CONFIG.filterDimDestinations.get()) {
					if (Helper.doesDimensionDestinationMatchFilter(event.getTo())) {
						Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), player, LogType.DIM_CHANGE, event.getFrom().location().toString(), event.getTo().location().toString(), null, x, y, z, Writer.type));
					}
				}
				if (LConfig.CONFIG.filterDimDestinationTypes.get()) {
					DimensionType typeToWorld = player.getServer().getLevel(event.getTo()).dimensionType();
					Optional<RegistryKey<DimensionType>> typeKey = player.getServer().registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getResourceKey(typeToWorld);
					if (typeKey.isPresent()) {
						if (Helper.doesDimensionDestinationTypeMatchFilter(typeKey.get())) {
							Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), player, LogType.DIM_CHANGE, event.getFrom().location().toString(), event.getTo().location().toString(), null, x, y, z, Writer.type));
						}
					}
				}
				else {
					Writer.writeToLog(Helper.constructEntry2(Helper.timeStampAtTimeZone(), player, LogType.DIM_CHANGE, event.getFrom().location().toString(), event.getTo().location().toString(), null, x, y, z, Writer.type)); 
				}
			}
		}
	}
}
