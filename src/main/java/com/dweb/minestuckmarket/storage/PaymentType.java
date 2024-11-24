package com.dweb.minestuckmarket.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public interface PaymentType {
    Object getData(CompoundTag tag);
    
    CompoundTag putData(Object data);
    
    boolean canPay(ServerPlayer player, CompoundTag tag);
    
    void pay(ServerPlayer player, CompoundTag tag);
    
    static Map<Item, PaymentData> listingsFromTag(ListTag tag) {
        Map<Item, PaymentData> listings = new HashMap<>();
        
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag iterateTag = tag.getCompound(i);
            Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(iterateTag.getString("item")));
            PaymentData data = PaymentData.fromTag(iterateTag.getCompound("payment_data"));
            
            if (item != null)
                listings.put(item, data);
        }
        
        return listings;
    }
    
    static ListTag createTagFromListings(Map<Item, PaymentData> listings) {
        ListTag listTag = new ListTag();
        
        for (Map.Entry<Item, PaymentData> entry : listings.entrySet()) {
            CompoundTag iterateTag = new CompoundTag();
            
            iterateTag.putString("item", ForgeRegistries.ITEMS.getKey(entry.getKey()).toString());
            iterateTag.put("payment_data", entry.getValue().createTag());
            
            listTag.add(iterateTag);
        }
        
        return listTag;
    }
}
