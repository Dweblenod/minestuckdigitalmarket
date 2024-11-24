package com.dweb.minestuckmarket.computer;

import com.dweb.minestuckmarket.MinestuckMarket;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.ComputerScreen;
import com.mraof.minestuck.computer.ComputerProgram;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class MarketApp extends ComputerProgram {
    public static final ResourceLocation ICON = MinestuckMarket.id("textures/gui/desktop_icon/market.png");
    
    @Override
    public void onInitGui(ComputerScreen gui) {
        gui.getMinecraft().setScreen(null);
        gui.getMinecraft().setScreen(new MarketScreen(gui.be));
    }
    
    @Override
    public void paintGui(GuiGraphics guiGraphics, ComputerScreen computerScreen, ComputerBlockEntity computerBlockEntity) {
    
    }
    
    @Override
    public ResourceLocation getIcon() {
        return ICON;
    }
}
