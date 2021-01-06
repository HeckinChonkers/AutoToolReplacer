package com.AutoToolReplace;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerDestroyItemEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
//import org.apache.logging.log4j.LogManager;
//import org.apache.logging.log4j.Logger;
import net.minecraft.item.*;
import java.util.List;
import java.util.stream.Collectors;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("autotoolreplace")
public class AutoToolReplace
{
    // Directly reference a log4j logger.
    //private static final Logger LOGGER = LogManager.getLogger();

    public AutoToolReplace() {
        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
    }
    
    @SubscribeEvent
    public void onItemBreak(PlayerDestroyItemEvent itemEvent) {
    	ItemStack borkedItem = itemEvent.getOriginal();
    	PlayerInventory pInv = itemEvent.getPlayer().inventory;
    	if(borkedItem.getToolTypes() != null && borkedItem.getToolTypes().size() > 0) {
    		List<ItemStack> sameTools = pInv.mainInventory
    				.stream()
    				.filter(x -> x.getToolTypes() != null 
    				&& x.getToolTypes().size() > 0
    				&& x.getToolTypes().toArray()[0] == borkedItem.getToolTypes().toArray()[0])
    				.collect(Collectors.toList());
    		
    				List<ItemStack> sameMatTool = null;
    				
    				if(sameTools != null && sameTools.size() > 0) {
    					sameMatTool = sameTools
    						.stream()
    						.filter(x -> x.getTranslationKey() == borkedItem.getTranslationKey())
    						.collect(Collectors.toList());
    				} else {
    					return;
    				}
    				if(sameMatTool != null && sameMatTool.size() > 0) {
    					int newToolSlot = pInv.getSlotFor(sameMatTool.get(0));
    					ItemStack newTool = pInv.getStackInSlot(newToolSlot);
    					itemEvent.getPlayer().setItemStackToSlot(net.minecraft.inventory.EquipmentSlotType.MAINHAND, newTool);
    					pInv.removeStackFromSlot(newToolSlot);
    				} else {
    					itemEvent.getPlayer().setItemStackToSlot(net.minecraft.inventory.EquipmentSlotType.MAINHAND, sameTools.get(0));
    				}
    			}
    	}
}
