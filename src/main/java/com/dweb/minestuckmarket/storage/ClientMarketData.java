package com.dweb.minestuckmarket.storage;

import java.util.ArrayList;
import java.util.List;

public final class ClientMarketData {
    public static List<MarketContainer> marketContainers = new ArrayList<>();
    
    public static void updateAvailableMarkets(List<MarketContainer> newMarketContainers)
    {
        marketContainers.clear();
        marketContainers.addAll(newMarketContainers);
    }
    
}
