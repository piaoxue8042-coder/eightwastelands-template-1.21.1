package com.px8042.eightwastelands.item.custom;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;
import net.minecraft.ChatFormatting;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.ChatFormatting;


import java.util.List;



public class SummerFanItem extends Item implements ICurioItem {

    public static final String SUMMER_FAN_DAMAGE_SHIELD_READY =
            "eightwastelands_summer_fan_damage_shield_ready";

    public static final String SUMMER_FAN_NEXT_SHIELD_TIME =
            "eightwastelands_summer_fan_next_shield_time";

    private static final int SHIELD_COOLDOWN = 10 * 20;

    public SummerFanItem(Properties properties) {
        super(properties);
    }

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


    @Override
    public void curioTick(SlotContext slotContext, ItemStack stack) {

        if (!slotContext.identifier().equals("calamity")) {
            return;
        }

        if (!(slotContext.entity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        reverseWindEvil(player);
        refreshDamageShield(player);
    }

    private void reverseWindEvil(Player player) {

        if (!NineHeavensPunishmentItem.isWindEvilReversed(player)) {
            player.getPersistentData().putBoolean(
                    NineHeavensPunishmentItem.WIND_EVIL_REVERSED,
                    true
            );

            player.displayClientMessage(
                    Component.literal("风煞已反转：罡风化作护体清风。"),
                    true
            );
        }
    }

    private void refreshDamageShield(Player player) {

        long currentTime = player.level().getGameTime();

        long nextShieldTime = player.getPersistentData().getLong(
                SUMMER_FAN_NEXT_SHIELD_TIME
        );

        if (currentTime < nextShieldTime) {
            return;
        }

        if (player.getPersistentData().getBoolean(SUMMER_FAN_DAMAGE_SHIELD_READY)) {
            return;
        }

        player.getPersistentData().putBoolean(
                SUMMER_FAN_DAMAGE_SHIELD_READY,
                true
        );

        player.displayClientMessage(
                Component.literal("夏扇护体：已可抵御下一次伤害。"),
                true
        );
    }

    public static void consumeDamageShield(Player player) {

        player.getPersistentData().putBoolean(
                SUMMER_FAN_DAMAGE_SHIELD_READY,
                false
        );

        player.getPersistentData().putLong(
                SUMMER_FAN_NEXT_SHIELD_TIME,
                player.level().getGameTime() + SHIELD_COOLDOWN
        );
    }

    public static boolean isDamageShieldReady(Player player) {
        return player.getPersistentData().getBoolean(SUMMER_FAN_DAMAGE_SHIELD_READY);
    }
    @Override
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.summer_fan.description"
        ).withStyle(style -> style.withColor(0x77DD77).withItalic(true)));

        tooltipComponents.add(Component.empty());

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.summer_fan.effect_1"
        ).withStyle(style -> style.withColor(0xB0C4DE)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.summer_fan.effect_2"
        ).withStyle(style -> style.withColor(0xAA77FF)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.summer_fan.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}
