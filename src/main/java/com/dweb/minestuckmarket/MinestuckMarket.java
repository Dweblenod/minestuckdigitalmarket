package com.dweb.minestuckmarket;

import com.dweb.minestuckmarket.computer.ProgramSetup;
import com.dweb.minestuckmarket.network.MMPackets;
import com.mraof.minestuck.item.MSCreativeTabs;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod(MinestuckMarket.MOD_ID)
public class MinestuckMarket {
    public static final String MOD_ID = "minestuck_market";
    public static final int PROGRAM_ID = 4;
    
    public static ResourceLocation id(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    public static final RegistryObject<Item> MARKET_DISK = ITEMS.register("market_disk", () -> new TempDiskItem(new Item.Properties().stacksTo(1)));
    
    public MinestuckMarket() {
        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        
        eventBus.addListener(this::commonSetup);
        
        ITEMS.register(eventBus);
        
        MinecraftForge.EVENT_BUS.register(this);
        
        eventBus.addListener(this::addCreative);
        
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }
    
    private void commonSetup(final FMLCommonSetupEvent event) {
        event.enqueueWork(ProgramSetup::registerMarketProgram);
        event.enqueueWork(MMPackets::registerPackets);
    }
    
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() == MSCreativeTabs.MAIN.getKey())
            event.accept(MARKET_DISK);
    }
}
