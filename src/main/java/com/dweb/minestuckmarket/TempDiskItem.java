package com.dweb.minestuckmarket;

import com.dweb.minestuckmarket.storage.MarketContainer;
import com.dweb.minestuckmarket.storage.MarketData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import java.util.Optional;

import static com.dweb.minestuckmarket.computer.ProgramSetup.CONTAINER_TITLE;

public class TempDiskItem extends Item {
    public TempDiskItem(Properties pProperties) {
        super(pProperties);
    }
    
    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {
        //TODO inappropriate to open menu at this stage
        //TODO inappropriate to determine menu via computer.owner
        if(pLevel instanceof ServerLevel level) {
            Optional<MarketContainer> proposedContainer = MarketData.get(level).getPlayersContainer(player);
            
            if(proposedContainer.isEmpty())
                return super.use(pLevel, player, pUsedHand);
            
            player.openMenu(new SimpleMenuProvider((containerId, playerInventory, menuPlayer) ->
                    ChestMenu.threeRows(containerId, playerInventory, proposedContainer.get()), CONTAINER_TITLE)
            );
        }
        
        return super.use(pLevel, player, pUsedHand);
    }
}
