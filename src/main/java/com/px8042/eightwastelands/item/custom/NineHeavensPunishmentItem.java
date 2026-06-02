package com.px8042.eightwastelands.item.custom;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import com.px8042.eightwastelands.eightwastelands;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import com.px8042.eightwastelands.effect.ModMobEffects;
import java.util.List;


public class NineHeavensPunishmentItem extends Item implements ICurioItem {

    //常量
    private static final int TRIBULATION_EFFECT_DURATION = 40;   //用于添加九重天劫的效果

    private static final String NEXT_LIGHTNING_TIME =
            "eightwastelands_next_lightning_time";



    private static final int MIN_LIGHTNING_DELAY = 35 * 20;
    private static final int MAX_LIGHTNING_DELAY = 45 * 20;
    private static final int HARDCORE_LIGHTNING_DELAY = 20 * 20;



    private static final int SLOWNESS_DURATION = 40;
    private static final int SLOWNESS_AMPLIFIER = 1;


    private static final int MINING_FATIGUE_DURATION = 40;
    private static final int MINING_FATIGUE_AMPLIFIER = 1;



    public static final String GOLDEN_APPLE_REGEN_BLOCK_TIME =
            "eightwastelands_golden_apple_regen_block_time";


    public static final ResourceLocation LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(
                    eightwastelands.MOD_ID,
                    "life_exhaustion_max_health"
            );

    private static final double LIFE_EXHAUSTION_MAX_HEALTH_MULTIPLIER = -0.5D;



    public static final ResourceLocation WIND_EVIL_ARMOR_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(
                    eightwastelands.MOD_ID,
                    "wind_evil_armor"
            );

    public static final ResourceLocation WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID =
            ResourceLocation.fromNamespaceAndPath(
                    eightwastelands.MOD_ID,
                    "wind_evil_armor_toughness"
            );

    private static final double WIND_EVIL_ATTRIBUTE_MULTIPLIER = -1.0D;

    private static final float LIGHTNING_DAMAGE_MAX_HEALTH_RATIO = 0.5F;   //天劫雷电伤害,固定打玩家最大生命的一半血
    //下面是反转常量,
    public static final String LUCK_DEPRIVATION_REVERSED =
            "eightwastelands_reversed_luck_deprivation";
    public static final String WIND_EVIL_REVERSED =
            "eightwastelands_reversed_wind_evil";
    public static final String LIFE_EXHAUSTION_REVERSED =
            "eightwastelands_reversed_life_exhaustion";
    public static final String HEART_DEMON_REVERSED =
            "eightwastelands_reversed_heart_demon";


    //下面是方法

    //下面做反转的方法
    //反转夺运
    public static boolean isLuckDeprivationReversed(Player player) {
        return player.getPersistentData().getBoolean(LUCK_DEPRIVATION_REVERSED);
    }
    //反转风煞
    public static boolean isWindEvilReversed(Player player) {
        return player.getPersistentData().getBoolean(WIND_EVIL_REVERSED);
    }
    //反转寿尽
    public static boolean isLifeExhaustionReversed(Player player) {
        return player.getPersistentData().getBoolean(LIFE_EXHAUSTION_REVERSED);
    }
    //心魔
    public static boolean isHeartDemonReversed(Player player) {
        return player.getPersistentData().getBoolean(HEART_DEMON_REVERSED);
    }


    public NineHeavensPunishmentItem(Properties properties) {
        super(properties);
    }
    //穿戴和取下
    @Override
    public boolean canEquip(SlotContext slotContext, ItemStack stack) {
        return slotContext.identifier().equals("calamity");
    }
    @Override
    public boolean canUnequip(SlotContext slotContext, ItemStack stack) {

        if (!(slotContext.entity() instanceof Player player)) {
            return false;
        }

        return player.isCreative() || player.isSpectator();
    }
    //添加九种负面效果的显示
    private void applyTribulationMobEffects(Player player) {

        if (player.level().isClientSide()) {
            return;
        }

        player.addEffect(new MobEffectInstance(
                ModMobEffects.HEAVENLY_TRIBULATION,
                TRIBULATION_EFFECT_DURATION,
                0,
                false,
                false,
                true
        ));

        player.addEffect(new MobEffectInstance(
                ModMobEffects.EARTH_DISASTER,
                TRIBULATION_EFFECT_DURATION,
                0,
                false,
                false,
                true
        ));

        player.addEffect(new MobEffectInstance(
                ModMobEffects.KARMA,
                TRIBULATION_EFFECT_DURATION,
                0,
                false,
                false,
                true
        ));

        if (!isHeartDemonReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.HEART_DEMON,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.HEART_DEMON);
        }

        player.addEffect(new MobEffectInstance(
                ModMobEffects.SPIRIT_EXHAUSTION,
                TRIBULATION_EFFECT_DURATION,
                0,
                false,
                false,
                true
        ));

        player.addEffect(new MobEffectInstance(
                ModMobEffects.WITHERED_BLOOD,
                TRIBULATION_EFFECT_DURATION,
                0,
                false,
                false,
                true
        ));

        if (!isLuckDeprivationReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.LUCK_DEPRIVATION,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.LUCK_DEPRIVATION);
        }

        if (!isWindEvilReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.WIND_EVIL,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.WIND_EVIL);
        }

        if (!isLifeExhaustionReversed(player)) {
            player.addEffect(new MobEffectInstance(
                    ModMobEffects.LIFE_EXHAUSTION,
                    TRIBULATION_EFFECT_DURATION,
                    0,
                    false,
                    false,
                    true
            ));
        } else {
            player.removeEffect(ModMobEffects.LIFE_EXHAUSTION);
        }
    }


    //下面是实时检测诅咒触发

    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {

        if (!slotContext.identifier().equals("calamity")) {
            return;
        }

        if (!(slotContext.entity() instanceof Player player)) {
            return;
        }
        applyTribulationMobEffects(player);

        //客户端



        //服务端

        if (!(player.level() instanceof ServerLevel level)) {
            return;
        }

        if (player.hasEffect(ModMobEffects.HEAVENLY_TRIBULATION)) {
            tickHeavenlyTribulation(player, level);
        }
        if (player.hasEffect(ModMobEffects.EARTH_DISASTER)) {
            tickEarthDisaster(player);
        }

        if (player.hasEffect(ModMobEffects.SPIRIT_EXHAUSTION)) {
            tickSpiritExhaustion(player);
        }

        if (player.hasEffect(ModMobEffects.LIFE_EXHAUSTION)) {
            tickLifeExhaustion(player);
        }

        if (player.hasEffect(ModMobEffects.WIND_EVIL)) {
            tickWindEvil(player);
        }
    }
    //下面给九重天罚做一个描述,就是鼠标移上去能看的
    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        tooltipComponents.add(Component.literal("✦ 九劫齐降 ✦")
                .withStyle(style -> style.withColor(0x550000).withBold(true)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.description"
        ).withStyle(style -> style.withColor(0x7A0000).withItalic(true)));

        tooltipComponents.add(Component.empty());

        tooltipComponents.add(Component.literal("滴答……血色的纹路在物品上蔓延。")
                .withStyle(style -> style.withColor(0x550000).withItalic(true)));

        tooltipComponents.add(Component.empty());

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.heavenly_tribulation"
        ).withStyle(style -> style.withColor(0x33CCFF)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.earth_disaster"
        ).withStyle(style -> style.withColor(0x8B7355)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.karma"
        ).withStyle(style -> style.withColor(0x8A2BE2)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.heart_demon"
        ).withStyle(style -> style.withColor(0xC71585)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.spirit_exhaustion"
        ).withStyle(style -> style.withColor(0x3F7F5F)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.withered_blood"
        ).withStyle(style -> style.withColor(0xAA0000)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.luck_deprivation"
        ).withStyle(style -> style.withColor(0xB8860B)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.wind_evil"
        ).withStyle(style -> style.withColor(0xB0C4DE)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.nine_heavens_punishment.life_exhaustion"
        ).withStyle(style -> style.withColor(0xC0C0C0)));
    }









    private void summonLightningOnPlayer(Player player, ServerLevel level) {

        LightningBolt lightning = EntityType.LIGHTNING_BOLT.create(level);

        if (lightning == null) {
            return;
        }

        lightning.moveTo(
                player.getX(),
                player.getY(),
                player.getZ()
        );

        // 只显示闪电，不让原版闪电自己造成伤害
        lightning.setVisualOnly(true);

        level.addFreshEntity(lightning);

        damagePlayerByHalfMaxHealth(player, level);
    }
    private void damagePlayerByHalfMaxHealth(Player player, ServerLevel level) {

        float damage = player.getMaxHealth() * LIGHTNING_DAMAGE_MAX_HEALTH_RATIO;

        player.hurt(
                level.damageSources().lightningBolt(),
                damage
        );
    }

    private int getNextLightningDelay(ServerLevel level) {

        if (isHardcore(level)) {
            return HARDCORE_LIGHTNING_DELAY;
        }

        return MIN_LIGHTNING_DELAY
                + level.random.nextInt(MAX_LIGHTNING_DELAY - MIN_LIGHTNING_DELAY + 1);
    }

    private boolean isHardcore(ServerLevel level) {
        return level.getServer().isHardcore();
    }

    //tick方法
    private void tickEarthDisaster(Player player) {

        player.addEffect(new MobEffectInstance(
                MobEffects.MOVEMENT_SLOWDOWN,
                SLOWNESS_DURATION,
                SLOWNESS_AMPLIFIER,
                false,
                false,
                true
        ));
    }
    private void tickSpiritExhaustion(Player player) {

        player.addEffect(new MobEffectInstance(
                MobEffects.DIG_SLOWDOWN,
                MINING_FATIGUE_DURATION,
                MINING_FATIGUE_AMPLIFIER,
                false,
                false,
                true
        ));
    }
    private void tickLifeExhaustion(Player player) {

        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth == null) {
            return;
        }

        if (maxHealth.getModifier(LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID) == null) {
            maxHealth.addTransientModifier(new AttributeModifier(
                    LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID,
                    LIFE_EXHAUSTION_MAX_HEALTH_MULTIPLIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }

        float currentMaxHealth = player.getMaxHealth();

        if (player.getHealth() > currentMaxHealth) {
            player.setHealth(currentMaxHealth);
        }
    }
    private void tickHeavenlyTribulation(Player player, ServerLevel level) {

        CompoundTag data = player.getPersistentData();
        long gameTime = level.getGameTime();

        if (!data.contains(NEXT_LIGHTNING_TIME)) {
            data.putLong(NEXT_LIGHTNING_TIME, gameTime + getNextLightningDelay(level));
            return;
        }

        long nextLightningTime = data.getLong(NEXT_LIGHTNING_TIME);

        if (gameTime < nextLightningTime) {
            return;
        }

        if (level.canSeeSky(player.blockPosition())) {
            summonLightningOnPlayer(player, level);
        }

        data.putLong(NEXT_LIGHTNING_TIME, gameTime + getNextLightningDelay(level));
    }
    private void tickWindEvil(Player player) {

        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        AttributeInstance armorToughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS);

        if (armor != null && armor.getModifier(WIND_EVIL_ARMOR_MODIFIER_ID) == null) {
            armor.addTransientModifier(new AttributeModifier(
                    WIND_EVIL_ARMOR_MODIFIER_ID,
                    WIND_EVIL_ATTRIBUTE_MULTIPLIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }

        if (armorToughness != null && armorToughness.getModifier(WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID) == null) {
            armorToughness.addTransientModifier(new AttributeModifier(
                    WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID,
                    WIND_EVIL_ATTRIBUTE_MULTIPLIER,
                    AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL
            ));
        }
    }
}