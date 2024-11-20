package com.dweb.minestuckmarket.computer;

import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class MarketScreen extends Screen {
    public static final String TITLE = "minestuck_market.digital_market";
    
    private final ComputerBlockEntity computer;
    
    public MarketScreen(ComputerBlockEntity computer) {
        super(Component.translatable("minestuck_market.digital_market"));
        this.computer = computer;
    }
    
    @Override
    protected void init() {
        super.init();
    }
}
