package com.dweb.minestuckmarket.network;

import com.dweb.minestuckmarket.MinestuckMarket;
import com.mraof.minestuck.network.MSPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.function.Function;

public class MMPackets {
    //uses some of Minestucks existing packet infrastructure
    
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.ChannelBuilder.named(MinestuckMarket.id("main"))
            .serverAcceptedVersions(s -> true).clientAcceptedVersions(s -> true).networkProtocolVersion(() -> PROTOCOL_VERSION).simpleChannel();
    
    private static int nextIndex;
    
    public static void registerPackets() {
        nextIndex = 0;
        registerClientHeaded(ClientMarketAccessPacket.class, ClientMarketAccessPacket::decode);
    }
    
    private static <P extends MSPacket> void registerClientHeaded(Class<P> packetClass, Function<FriendlyByteBuf, P> decoder) {
        INSTANCE.messageBuilder(packetClass, nextIndex, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(MSPacket::encode).decoder(decoder).consumerMainThread(MSPacket::consume).add();
        nextIndex++;
    }
    
    
    public static <P> void sendToPlayer(P packet, ServerPlayer player) {
        INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), packet);
    }
}
