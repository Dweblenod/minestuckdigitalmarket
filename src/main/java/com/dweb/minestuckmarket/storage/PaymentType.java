package com.dweb.minestuckmarket.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.HashMap;
import java.util.Map;

public interface PaymentType {
    Object getData(CompoundTag tag);
    
    CompoundTag putData(Object data);
    
    boolean canPay(ServerPlayer player, CompoundTag tag);
    
    void pay(ServerPlayer player, CompoundTag tag);
    
    static Map<ItemStack, PaymentData> listingsFromTag(ListTag tag) {
        Map<ItemStack, PaymentData> listings = new HashMap<>();
        
        for (int i = 0; i < tag.size(); i++) {
            CompoundTag iterateTag = tag.getCompound(i);
            ItemStack itemStack = ItemStack.of(iterateTag.getCompound("item_stack"));
            PaymentData data = PaymentData.fromTag(iterateTag.getCompound("payment_data"));
            
            if (!itemStack.isEmpty())
                listings.put(itemStack, data);
        }
        
        return listings;
    }
    
    static ListTag createTagFromListings(Map<ItemStack, PaymentData> listings) {
        ListTag listTag = new ListTag();
        
        for (Map.Entry<ItemStack, PaymentData> entry : listings.entrySet()) {
            CompoundTag iterateTag = new CompoundTag();
            
            iterateTag.put("item_stack", entry.getKey().save(new CompoundTag()));
            iterateTag.put("payment_data", entry.getValue().createTag());
            
            listTag.add(iterateTag);
        }
        
        return listTag;
    }
}
