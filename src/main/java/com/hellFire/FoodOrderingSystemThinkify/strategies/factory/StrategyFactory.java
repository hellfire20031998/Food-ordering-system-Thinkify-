package com.hellFire.FoodOrderingSystemThinkify.strategies.factory;

import com.hellFire.FoodOrderingSystemThinkify.models.enums.RestaurantSelection;
import com.hellFire.FoodOrderingSystemThinkify.strategies.IRestaurantSelectionStrategy;
import com.hellFire.FoodOrderingSystemThinkify.strategies.strategyImpl.HighestRatingStrategy;
import com.hellFire.FoodOrderingSystemThinkify.strategies.strategyImpl.LowestCostStrategy;
import org.springframework.stereotype.Component;

@Component
public class StrategyFactory {

    public IRestaurantSelectionStrategy getStrategy(RestaurantSelection strategy) {
        if(RestaurantSelection.HIGH_RATING.equals(strategy)) {
            return new HighestRatingStrategy();
        }else {
            return new LowestCostStrategy();
        }
    }
}
