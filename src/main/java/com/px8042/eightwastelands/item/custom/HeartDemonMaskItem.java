package com.px8042.eightwastelands.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.List;

public class HeartDemonMaskItem extends Item implements ICurioItem {

    public HeartDemonMaskItem(Properties properties) {
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
    public void appendHoverText(
            ItemStack stack,
            TooltipContext context,
            List<Component> tooltipComponents,
            TooltipFlag tooltipFlag
    ) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(Component.literal("✦ 心魔照影 ✦")
                .withStyle(style -> style.withColor(0xC71585).withBold(true)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.heart_demon_mask.description"
        ).withStyle(style -> style.withColor(0xC71585).withItalic(true)));

        tooltipComponents.add(Component.empty());

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.heart_demon_mask.effect_1"
        ).withStyle(style -> style.withColor(0xFF5555)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.heart_demon_mask.effect_2"
        ).withStyle(style -> style.withColor(0xAA77FF)));

        tooltipComponents.add(Component.translatable(
                "tooltip.eightwastelands.heart_demon_mask.curse"
        ).withStyle(ChatFormatting.GRAY));
    }
}