package com.dweb.minestuckmarket.computer;

import com.dweb.minestuckmarket.MinestuckMarket;
import com.dweb.minestuckmarket.storage.MarketData;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ComputerProgram;
import com.mraof.minestuck.computer.ProgramData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

public class ProgramSetup {
    public static final Component CONTAINER_TITLE = Component.translatable("container.enderchest");
    
    public static void registerMarketProgram() {
        ProgramData.registerProgram(MinestuckMarket.PROGRAM_ID, new ItemStack(MinestuckMarket.MARKET_DISK.get()), MARKET_HANDLER);
    }
    
    @Mod.EventBusSubscriber(modid = MinestuckMarket.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ComputerProgram.registerProgramClass(MinestuckMarket.PROGRAM_ID, MarketApp.class);
        }
    }
    
    private static final ProgramData.ProgramHandler MARKET_HANDLER = new ProgramData.ProgramHandler() {
        @Override
        public void onDiskInserted(ComputerBlockEntity computer) {
            if (computer.getLevel() instanceof ServerLevel level) {
                Player player = computer.owner.getPlayer(level.getServer());
                
                if (player != null)
                    MarketData.get(level).addPlayerContainer(player);
            }
        }
        
        @Override
        public void onLoad(ComputerBlockEntity computer) {
        
        }
        
        @Override
        public void onClosed(ComputerBlockEntity computer) {
        
        }
    };
}
