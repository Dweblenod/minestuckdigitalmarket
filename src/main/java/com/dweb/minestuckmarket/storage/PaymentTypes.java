package com.dweb.minestuckmarket.storage;


import com.dweb.minestuckmarket.MinestuckMarket;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.player.PlayerSavedData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public final class PaymentTypes {
    public static final DeferredRegister<PaymentType> REGISTER = DeferredRegister.create(MinestuckMarket.id("payment_type"), MinestuckMarket.MOD_ID);
    public static final Supplier<IForgeRegistry<PaymentType>> REGISTRY = REGISTER.makeRegistry(() -> new RegistryBuilder<PaymentType>().disableSaving().disableSync());
    
    public static final RegistryObject<PaymentType> BOONDOLLAR = REGISTER.register(Boondollar.NAME, Boondollar::new);
    
    public static class Boondollar implements PaymentType {
        public static final String NAME = "boondollar";
        
        @Override
        public Object getData(CompoundTag tag) {
            return tag.getInt("amount");
        }
        
        @Override
        public CompoundTag putData(Object data) {
            CompoundTag tag = new CompoundTag();
            tag.putInt("amount", (int) data);
            return tag;
        }
        
        @Override
        public boolean canPay(ServerPlayer player, CompoundTag tag) {
            if (player == null)
                return false;
            
            PlayerData data = PlayerSavedData.getData(player);
            if (data != null)
                return data.getBoondollars() >= ((int) getData(tag));
            
            return false;
        }
        
        @Override
        public void pay(ServerPlayer player, CompoundTag tag) {
            PlayerData data = PlayerSavedData.getData(player);
            if (data != null)
                data.takeBoondollars(((int) getData(tag)));
        }
    }
}
