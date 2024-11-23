package com.dweb.minestuckmarket.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MarketContainer extends SimpleContainer {
    public static final int CONTAINER_SIZE = 27;
    public static final String NBT_LIST_NAME = "market_containers";
    
    private UUID ownerId;
    
    public MarketContainer(UUID ownerId) {
        super(CONTAINER_SIZE);
        this.ownerId = ownerId;
    }
    
    public MarketContainer(ListTag containerNbt) {
        super(CONTAINER_SIZE);
        fromTag(containerNbt);
    }
    
    public UUID getOwnerId() {
        return ownerId;
    }
    
    public static List<MarketContainer> createListFromNbt(CompoundTag nbt) {
        List<MarketContainer> marketContainers = new ArrayList<>();
        
        ListTag marketContainerList = nbt.getList(NBT_LIST_NAME, Tag.TAG_LIST);
        for (int i = 0; i < marketContainerList.size(); i++) {
            ListTag tag = marketContainerList.getList(i);
            marketContainers.add(new MarketContainer(tag));
        }
        
        return marketContainers;
    }
    
    public static ListTag createNbtFromList(List<MarketContainer> marketContainers)
    {
        ListTag marketContainerList = new ListTag();
        
        marketContainerList.addAll(marketContainers.stream().map(MarketContainer::createTag).toList());
        
        return marketContainerList;
    }
    
    //fromTag and createTag currently copies from PlayerEnderChestContainer
    @Override
    public void fromTag(ListTag containerNbt) {
        for (int i = 0; i < this.getContainerSize(); ++i) {
            this.setItem(i, ItemStack.EMPTY);
        }
        
        for (int k = 0; k < containerNbt.size(); ++k) {
            if (k == 0) {
                this.ownerId = containerNbt.getCompound(k).getUUID("owner_id");
                continue;
            }
            
            CompoundTag compoundtag = containerNbt.getCompound(k);
            int j = compoundtag.getByte("Slot") & 255;
            if (j >= 0 && j < this.getContainerSize()) {
                this.setItem(j, ItemStack.of(compoundtag));
            }
        }
    }
    
    @Override
    public ListTag createTag() {
        ListTag listtag = new ListTag();
        
        CompoundTag ownerTag = new CompoundTag();
        ownerTag.putUUID("owner_id", ownerId);
        listtag.add(ownerTag);
        
        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte) i);
                itemstack.save(compoundtag);
                listtag.add(compoundtag);
            }
        }
        
        return listtag;
    }
}
