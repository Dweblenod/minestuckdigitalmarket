package com.dweb.minestuckmarket.storage;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public class PaymentData {
    private final PaymentType paymentType;
    private CompoundTag data;
    
    PaymentData(PaymentType paymentType, CompoundTag data) {
        this.paymentType = paymentType;
        this.data = data;
    }
    
    public PaymentType getPaymentType() {
        return paymentType;
    }
    
    public CompoundTag getData() {
        return data;
    }
    
    public void setData(CompoundTag newData) {
        data = newData;
    }
    
    public static PaymentData fromTag(CompoundTag tag) {
        String paymentRegistry = tag.getString("payment_type");
        ResourceLocation paymentTypeLocation = ResourceLocation.tryParse(paymentRegistry);
        PaymentType paymentType = PaymentTypes.REGISTRY.get().getValue(paymentTypeLocation);
        
        CompoundTag data = tag.getCompound("data");
        
        return new PaymentData(paymentType, data);
    }
    
    public CompoundTag createTag() {
        CompoundTag tag = new CompoundTag();
        
        String paymentRegistry = PaymentTypes.REGISTRY.get().getKey(paymentType).toString();
        tag.putString("payment_type", paymentRegistry);
        
        tag.put("data", data);
        
        return tag;
    }
}
