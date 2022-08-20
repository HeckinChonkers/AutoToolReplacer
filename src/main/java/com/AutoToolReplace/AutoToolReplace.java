package com.AutoToolReplace;

import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("autotoolreplace")
public class AutoToolReplace {

	public AutoToolReplace() {
		// Register ourselves for server and other game events we are interested in
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onItemBreak(PlayerDestroyItemEvent itemEvent) {
		Item borkedItem = itemEvent.getOriginal().getItem();
		Inventory pInv = itemEvent.getEntity().getInventory();
		List<ItemStack> sameTools = pInv.items.stream().filter(x -> x.getItem().getClass() == borkedItem.getClass())
				.collect(Collectors.toList());
		if (sameTools != null && sameTools.size() > 0) {
			List<ItemStack> sameMatTool = sameTools.stream()
					.filter(x -> x.getDescriptionId() == borkedItem.getDescriptionId()).collect(Collectors.toList());
			ItemStack newTool = null;

			if (sameMatTool != null && sameMatTool.size() > 0) {
				newTool = sameMatTool.get(0);
			} else {
				newTool = sameTools.get(0);
			}

			if (newTool != null) {
				//Doing it this way to have inventory update properly
				pInv.add(pInv.selected, newTool);
				pInv.removeItem(newTool);
			}
		}
	}
}
