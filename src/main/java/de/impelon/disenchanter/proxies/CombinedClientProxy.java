package de.impelon.disenchanter.proxies;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import de.impelon.disenchanter.blocks.BlockDisenchantmentTable;
import de.impelon.disenchanter.blocks.TileEntityDisenchantmentTable;
import de.impelon.disenchanter.gui.TileEntityDisenchantmentTableRenderer;
import de.impelon.disenchanter.DisenchanterMain;

public class CombinedClientProxy extends CommonProxy {
	
	@Override
	public void preInit(FMLPreInitializationEvent ev) {
		super.preInit(ev);
	}
	
	@Override
	public void registerBlocks(RegistryEvent.Register<Block> ev) {
		super.registerBlocks(ev);
		
		ModelLoader.setCustomStateMapper(disenchantmentTable, new DefaultStateMapper() {
			@Override
			protected ModelResourceLocation getModelResourceLocation(IBlockState state) {
				StringBuilder variant = new StringBuilder();
				variant.append(BlockDisenchantmentTable.AUTOMATIC.getName() + "=" + state.getValue(BlockDisenchantmentTable.AUTOMATIC).toString());
				variant.append(',');
				variant.append(BlockDisenchantmentTable.BULKDISENCHANTING.getName() + "=" + state.getValue(BlockDisenchantmentTable.BULKDISENCHANTING).toString());
				variant.append(',');
				variant.append(BlockDisenchantmentTable.VOIDING.getName() + "=" + state.getValue(BlockDisenchantmentTable.VOIDING).toString());
				return new ModelResourceLocation(disenchantmentTable.getUnlocalizedName().substring(5), variant.toString());
		    }
		});
	}
	
	@Override
	public void registerItems(RegistryEvent.Register<Item> ev) {
		super.registerItems(ev);
		
		for (byte meta = 0; meta < 8; meta++) {
			IBlockState state = disenchantmentTable.getStateFromMeta(meta);
			StringBuilder variant = new StringBuilder();
			variant.append(BlockDisenchantmentTable.AUTOMATIC.getName() + "=" + state.getValue(BlockDisenchantmentTable.AUTOMATIC).toString());
			variant.append(',');
			variant.append(BlockDisenchantmentTable.BULKDISENCHANTING.getName() + "=" + state.getValue(BlockDisenchantmentTable.BULKDISENCHANTING).toString());
			variant.append(',');
			variant.append(BlockDisenchantmentTable.VOIDING.getName() + "=" + state.getValue(BlockDisenchantmentTable.VOIDING).toString());
			ModelLoader.setCustomModelResourceLocation(itemDisenchantmentTable, meta, 
					new ModelResourceLocation(disenchantmentTable.getUnlocalizedName().substring(5), variant.toString()));
		}
	}
	
	@Override
	public void load(FMLInitializationEvent ev) {
		super.load(ev);
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityDisenchantmentTable.class, new TileEntityDisenchantmentTableRenderer());
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent ev) {
		super.postInit(ev);
		
		if (DisenchanterMain.config.get("general", "CheckVersion", true).getBoolean())
			new Thread(DisenchanterMain.versionChecker, "Version Check").start();
	}
}
