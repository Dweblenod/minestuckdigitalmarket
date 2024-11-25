package com.dweb.minestuckmarket.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.UUID;

public record Merchant(Component name, UUID uuid, Map<ItemStack, PaymentData> listings) {
    //TODO change Item to ItemStack, use ItemStack.of() and itemStack.save()
    public boolean playerMatch(Player player) {
        return player.getUUID().equals(uuid);
    }
    
    public void addListing(ItemStack item, PaymentData data) {
        listings.put(item, data);
    }
    
    public static Merchant fromTag(CompoundTag containerNbt) {
        Component name = Component.literal(containerNbt.getString("name"));
        UUID id = containerNbt.getUUID("id");
        //TODO listings is not getting read properly
        ListTag listingsTag = containerNbt.getList("listings", Tag.TAG_LIST);
        Map<ItemStack, PaymentData> listings = PaymentType.listingsFromTag(listingsTag);
        
        return new Merchant(name, id, listings);
    }
    
    public CompoundTag createTag() {
        CompoundTag tag = new CompoundTag();
        
        tag.putString("name", name.getString());
        tag.putUUID("id", uuid);
        tag.put("listings", PaymentType.createTagFromListings(listings));
        
        return tag;
    }
    
    
}
