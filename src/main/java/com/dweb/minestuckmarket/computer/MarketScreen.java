package com.dweb.minestuckmarket.computer;

import com.dweb.minestuckmarket.storage.ClientMarketData;
import com.dweb.minestuckmarket.storage.MarketContainer;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.gui.widget.ExtendedButton;

import java.util.ArrayList;
import java.util.List;

public class MarketScreen extends Screen {
    public static final String TITLE = "minestuck_market.digital_market";
    
    private static final ResourceLocation GUI_BACKGROUND = Minestuck.id("textures/gui/generic_extra_large.png");
    private static final int GUI_WIDTH = 224;
    private static final int GUI_HEIGHT = 176;
    
    private final ComputerBlockEntity computer;
    private final List<ExtendedButton> marketButtons = new ArrayList<>();
    
    private int xOffset;
    private int yOffset;
    
    public MarketScreen(ComputerBlockEntity computer) {
        super(Component.translatable(TITLE));
        this.computer = computer;
    }
    
    @Override
    protected void init() {
        super.init();
        
        yOffset = (this.height / 2) - (GUI_HEIGHT / 2);
        xOffset = (this.width / 2) - (GUI_WIDTH / 2);
        
        initContainerButtons();
    }
    
    private void initContainerButtons() {
        marketButtons.clear();
        int containerOffset = 0;
        
        for (MarketContainer container : ClientMarketData.marketContainers) {
            ExtendedButton iterateButton = new ExtendedButton(xOffset + 10, this.yOffset + 25 + (containerOffset * 5), 120, 16, container.getOwner().name(), button -> containerClick());
            marketButtons.add(iterateButton);
            addRenderableWidget(iterateButton);
            containerOffset++;
        }
    }
    
    private void containerClick() {
        
    }
    
    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(graphics);
        
        RenderSystem.setShaderColor(1, 1, 1, 1);
        
        graphics.blit(GUI_BACKGROUND, xOffset, yOffset, 0, 0, GUI_WIDTH, GUI_HEIGHT);
        
        super.render(graphics, mouseX, mouseY, partialTicks);
    }
    
    @Override
    public boolean isPauseScreen() {
        return false;
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
    
    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        //a hackey way to access the protected method exitProgram() when pressing ESC
        return computer.gui.keyPressed(keyCode, scanCode, modifiers);
    }
}
