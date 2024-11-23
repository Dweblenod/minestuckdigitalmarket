package com.dweb.minestuckmarket.storage;

import com.dweb.minestuckmarket.MinestuckMarket;
import com.dweb.minestuckmarket.network.ClientMarketAccessPacket;
import com.dweb.minestuckmarket.network.MMPackets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.storage.DimensionDataStorage;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Mod.EventBusSubscriber(modid = MinestuckMarket.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class MarketData extends SavedData {
    private static final Logger LOGGER = LogManager.getLogger();
    
    private static final String DATA_NAME = MinestuckMarket.MOD_ID + "_extra";
    
    private final List<MarketContainer> marketContainers = new ArrayList<>();
    
    private MarketData() {
    }
    
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
    
    }
    
    @SubscribeEvent
    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        
        if (player.level().isClientSide || player instanceof FakePlayer)
            return;
        
        if (player.level().getGameTime() % 60 != 0)
            return;
        
        if (event.player instanceof ServerPlayer serverPlayer) {
            MarketData data = MarketData.get(serverPlayer.server);
            updateVisibleMarkets(data.marketContainers, serverPlayer);
        }
    }
    
    public static MarketData load(CompoundTag nbt) {
        MarketData data = new MarketData();
        
        data.marketContainers.clear();
        data.marketContainers.addAll(MarketContainer.createListFromNbt(nbt));
        
        return data;
    }
    
    @Override
    public CompoundTag save(CompoundTag compound) {
        compound.put(MarketContainer.NBT_LIST_NAME, MarketContainer.createNbtFromList(marketContainers));
        
        return compound;
    }
    
    public List<MarketContainer> getMarketContainers() {
        return marketContainers;
    }
    
    public Optional<MarketContainer> getPlayersContainer(Player player) {
        return marketContainers.stream().filter(container -> player.getUUID().equals(container.getOwnerId())).findAny();
    }
    
    public void addPlayerContainer(Player player) {
        UUID playerId = player.getUUID();
        if (marketContainers.stream().noneMatch(container -> container.getOwnerId().equals(playerId)))
            marketContainers.add(new MarketContainer(playerId));
        setDirty();
    }
    
    public static void updateVisibleMarkets(List<MarketContainer> marketContainers, ServerPlayer player) {
        //TODO use config to set visibility
        ClientMarketAccessPacket packet = new ClientMarketAccessPacket(marketContainers);
        MMPackets.sendToPlayer(packet, player);
    }
    
    public static MarketData get(Level level) {
        MinecraftServer server = level.getServer();
        if (server == null)
            throw new IllegalArgumentException("Can't get extra data instance on client side! (Got null server from level)");
        return get(server);
    }
    
    public static MarketData get(MinecraftServer mcServer) {
        ServerLevel level = mcServer.getLevel(Level.OVERWORLD);
        
        DimensionDataStorage storage = level.getDataStorage();
        
        return storage.computeIfAbsent(MarketData::load, MarketData::new, DATA_NAME);
    }
    
    @Override
    public boolean isDirty() {
        return true;
    }
}