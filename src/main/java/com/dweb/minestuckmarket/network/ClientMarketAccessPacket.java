package com.dweb.minestuckmarket.network;

import com.dweb.minestuckmarket.storage.ClientMarketData;
import com.dweb.minestuckmarket.storage.MarketContainer;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;

import java.util.List;

public record ClientMarketAccessPacket(List<MarketContainer> marketContainers) implements MSPacket.PlayToClient {
    @Override
    public void encode(FriendlyByteBuf buffer) {
        CompoundTag nbt = new CompoundTag();
        nbt.put(MarketContainer.NBT_LIST_NAME, MarketContainer.createNbtFromList(marketContainers));
        buffer.writeNbt(nbt);
    }
    
    public static ClientMarketAccessPacket decode(FriendlyByteBuf buffer) {
        CompoundTag nbt = buffer.readNbt();
        List<MarketContainer> marketContainers = MarketContainer.createListFromNbt(nbt);
        
        return new ClientMarketAccessPacket(marketContainers);
    }
    
    @Override
    public void execute() {
        ClientMarketData.updateAvailableMarkets(marketContainers);
    }
}
