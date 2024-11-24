package com.dweb.minestuckmarket.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketContainer extends SimpleContainer {
    public static final int CONTAINER_SIZE = 27;
    public static final String NBT_LIST_NAME = "market_containers";
    
    private Merchant owner;
    
    public MarketContainer(Player owner) {
        super(CONTAINER_SIZE);
        this.owner = new Merchant(owner.getName(), owner.getUUID(), new HashMap<>());
    }
    
    public MarketContainer(Merchant owner) {
        super(CONTAINER_SIZE);
        this.owner = owner;
    }
    
    public MarketContainer(ListTag containerNbt) {
        super(CONTAINER_SIZE);
        fromTag(containerNbt);
    }
    
    public Merchant getOwner() {
        return owner;
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
    
    public static ListTag createNbtFromList(List<MarketContainer> marketContainers) {
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
                this.owner = Merchant.fromTag(containerNbt.getCompound(k));
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
        ListTag listTag = new ListTag();
        
        listTag.add(owner.createTag());
        
        for (int i = 0; i < this.getContainerSize(); ++i) {
            ItemStack itemstack = this.getItem(i);
            if (!itemstack.isEmpty()) {
                CompoundTag compoundtag = new CompoundTag();
                compoundtag.putByte("Slot", (byte) i);
                itemstack.save(compoundtag);
                listTag.add(compoundtag);
            }
        }
        
        return listTag;
    }
}
