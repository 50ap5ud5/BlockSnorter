package me.soapsuds.block_snorter;


import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
/**
 * Author: Spectre0987 and 50ap5ud5
 */

@Mod(BlockSnorter.MODID)
public class BlockSnorter
{
    public static final String MODID = "block_snorter";

    public BlockSnorter() {
         ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, LConfig.CONFIG_SPEC);
    }
}
