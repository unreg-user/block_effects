package wta.items;

import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.FoodComponents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import wta.AllInit;
import wta.items.classes.BlueExpItem;
import wta.items.classes.RedExpItem;

import java.util.List;

import static wta.Block_effects.MODID;

public class ItemsInit {
    public static Item redExpI;
    public static Item blueExpI;
    public static void init(){
        blueExpI=Registry.register(
                Registries.ITEM,
                Identifier.of(MODID, "blue_exp"),
                new BlueExpItem(
                        new Item.Settings()
                                .food(FoodComponents.APPLE)
                )
        );
        redExpI=Registry.register(
                Registries.ITEM,
                Identifier.of(MODID, "red_exp"),
                new RedExpItem(
                        new Item.Settings()
                                .food(FoodComponents.APPLE)
                )
        );

        AllInit.addToInMI(List.of(
                blueExpI, redExpI
        ));
    }
}
