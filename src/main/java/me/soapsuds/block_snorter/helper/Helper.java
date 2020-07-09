package me.soapsuds.block_snorter.helper;

import me.soapsuds.block_snorter.LConfig;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.EntityRayTraceResult;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.dimension.DimensionType;

/**
 * Created by 50ap5ud5
 * on 8 Jul 2020 @ 3:19:25 pm
 */
public class Helper {

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
	public static boolean doesDimensionDestinationMatchFilter(DimensionType type) {
		return LConfig.CONFIG.dimChangeDestinationTypes.get().stream().anyMatch(name ->
		type.getRegistryName().toString().contentEquals(name));
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
	       Vec3d lookVec = entity.getLookVec();
	       for (int i = 0; i < distance * 2; i++) {
	           float scale = i / 2F;
	           Vec3d pos = entity.getPositionVector().add(0, entity.getEyeHeight(), 0).add(lookVec.scale(scale));
	           if (entity.world.getBlockState(new BlockPos(pos)).getCollisionShape(entity.world, new BlockPos(pos)) != VoxelShapes.empty()
                   && !entity.world.isAirBlock(new BlockPos(pos))) {
	               return new BlockRayTraceResult(pos, Direction.getFacingFromVector(pos.x, pos.y, pos.z), new BlockPos(pos), false);
	           } else {
	               Vec3d min = pos.add(0.25F, 0.25F, 0.25F);
	               Vec3d max = pos.add(-0.25F, -0.25F, -0.25F);
	               for (Entity e : entity.world.getEntitiesWithinAABBExcludingEntity(entity, new AxisAlignedBB(min.x, min.y, min.z, max.x, max.y, max.z))) {
	                   return new EntityRayTraceResult(e);
	               }
	           }
	       }
	       return null;
	   }

}
