package com.dweb.minestuckmarket.computer;

import com.dweb.minestuckmarket.MinestuckMarket;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.computer.ButtonListProgram;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;

public class MarketApp extends ButtonListProgram {
    public static final String THEME = "minestuck.program.market.theme";
    public static final String TITLE = "minestuck.program.market.title";
    
    public static final ResourceLocation ICON = MinestuckMarket.id("textures/gui/desktop_icon/market.png");
    
    @Override
    protected ArrayList<UnlocalizedString> getStringList(ComputerBlockEntity be) {
        var list = new ArrayList<UnlocalizedString>();
        
        list.add(new UnlocalizedString(TITLE));
        list.add(new UnlocalizedString(THEME));
        
        return list;
    }
    
    @Override
    protected void onButtonPressed(ComputerBlockEntity be, String buttonName, Object[] data) {
        if (be.getLevel() == null)
            return;
        
        switch (buttonName) {
            case THEME -> {
                be.gui.getMinecraft().setScreen(null);
                be.gui.getMinecraft().setScreen(new MarketScreen(be));
            }
        }
    }
    
    @Override
    public ResourceLocation getIcon() {
        return ICON;
    }
}
