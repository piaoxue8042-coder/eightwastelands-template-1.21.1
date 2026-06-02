package com.px8042.eightwastelands.item;

import com.px8042.eightwastelands.eightwastelands;
import com.px8042.eightwastelands.item.custom.NineHeavensPunishmentItem;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import com.px8042.eightwastelands.item.custom.WealthItem;
import com.px8042.eightwastelands.item.custom.SummerFanItem;
import com.px8042.eightwastelands.item.custom.SpringShearsItem;
import com.px8042.eightwastelands.item.custom.HeartDemonMaskItem;


public class ModItems {

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(eightwastelands.MOD_ID);


    //注册九重天罚
    public static final DeferredItem<Item> NINE_HEAVENS_PUNISHMENT =
            ITEMS.register("nine_heavens_punishment",
                    () -> new NineHeavensPunishmentItem(
                            new Item.Properties().stacksTo(1)
                    ));

    public static void register(IEventBus eventBus) {

        ITEMS.register(eventBus);
    }
    //注册财富
    public static final DeferredItem<Item> WEALTH =
            ITEMS.register("wealth",
                    () -> new WealthItem(
                            new Item.Properties().stacksTo(1)
                    ));
    //注册夏扇
    public static final DeferredItem<Item> SUMMER_FAN =
            ITEMS.register("summer_fan",
                    () -> new SummerFanItem(
                            new Item.Properties().stacksTo(1)
                    ));
    //注册春剪
    public static final DeferredItem<Item> SPRING_SHEARS =
            ITEMS.register("spring_shears",
                    () -> new SpringShearsItem(
                            new Item.Properties().stacksTo(1)
                    ));
    //心魔
    public static final DeferredItem<Item> HEART_DEMON_MASK =
            ITEMS.register("heart_demon_mask",
                    () -> new HeartDemonMaskItem(
                            new Item.Properties().stacksTo(1)
                    ));

    //此处添加新物品
}
