package com.px8042.eightwastelands.item;

import com.px8042.eightwastelands.eightwastelands;
import com.px8042.eightwastelands.item.custom.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;import com.px8042.eightwastelands.block.ModBlocks;
import net.minecraft.world.item.BlockItem;

import static com.px8042.eightwastelands.block.ModBlocks.BLOCKS;


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
    //归元铃,反转绝灵
    public static final DeferredItem<Item> GUIYUAN_BELL =
            ITEMS.register("guiyuan_bell",
                    () -> new GuiyuanBellItem(
                            new Item.Properties().stacksTo(1)
                    ));
    //血颅骨,反转因果
    public static final DeferredItem<Item> BLOOD_SKULL =
            ITEMS.register("blood_skull",
                    () -> new BloodSkullItem(
                            new Item.Properties().stacksTo(1)
                    ));
    //生桩,反转地灾
    public static final DeferredItem<Item> SHENG_ZHUANG =
            ITEMS.register("sheng_zhuang",
                    () -> new ShengZhuangItem(
                            new Item.Properties().stacksTo(1)
                    ));
    //雷电伤害
    public static final DeferredItem<Item> HEAVENLY_THUNDER_SEAL =
            ITEMS.register("heavenly_thunder_seal",
                    () -> new HeavenlyThunderSealItem(
                            new Item.Properties().stacksTo(1)
                    ));
    // 遗忘，鬼器
    public static final DeferredItem<Item> FORGETFULNESS =
            ITEMS.register("forgetfulness",
                    () -> new ForgetfulnessItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .durability(800)
                    ));
    // 劫灰戒，仙器
    public static final DeferredItem<Item> JIEHUI_RING =
            ITEMS.register("jiehui_ring",
                    () -> new JiehuiRingItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .durability(500)
                    ));
    // 楼砧
    public static final DeferredItem<Item> LOU_ZHEN =
            ITEMS.register("lou_zhen",
                    () -> new BlockItem(
                            ModBlocks.LOU_ZHEN.get(),
                            new Item.Properties()
                    ));
    // 风行履，仙器
    public static final DeferredItem<Item> FENGXING_BOOTS =
            ITEMS.register("fengxing_boots",
                    () -> new FengxingBootsItem(
                            new Item.Properties()
                                    .stacksTo(1)
                                    .durability(600)
                    ));

    //此处添加新物品
}
