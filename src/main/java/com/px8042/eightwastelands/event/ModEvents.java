package com.px8042.eightwastelands.event;
import com.px8042.eightwastelands.item.custom.NineHeavensPunishmentItem;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent.Pre;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.event.entity.living.LivingDropsEvent;
import com.px8042.eightwastelands.item.ModItems;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import top.theillusivec4.curios.api.CuriosApi;
import net.neoforged.neoforge.event.entity.item.ItemTossEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import com.px8042.eightwastelands.effect.ModMobEffects;
import net.minecraft.world.entity.Entity;
import com.px8042.eightwastelands.item.custom.SummerFanItem;
import com.px8042.eightwastelands.item.custom.SpringShearsItem;
import net.minecraft.world.entity.EntityType;






public class ModEvents {
    private static final double LUCK_DEPRIVATION_RANGE = 32.0D;//控制夺运范围
    private static final String HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT =
            "eightwastelands_has_received_nine_heavens_punishment";  //添加首次获得标记
    private static final String SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT =
            "eightwastelands_should_restore_nine_heavens_punishment";  //用于制作死亡时不掉落



    //上面是常量
    //下面是死亡时九重天罚不掉落事件
    @SubscribeEvent
    public void onLivingDeath(LivingDeathEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        if (hasNineHeavensPunishment(player)) {
            player.getPersistentData().putBoolean(
                    SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT,
                    true
            );
        }
    }
    //玩家重生事件
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        if (!data.getBoolean(SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT)) {
            return;
        }

        data.remove(SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT);

        if (hasNineHeavensPunishment(player)) {
            return;
        }

        ItemStack stack = new ItemStack(ModItems.NINE_HEAVENS_PUNISHMENT.get());

        if (!tryEquipToCalamitySlot(player, stack)) {
            if (!player.getInventory().add(stack)) {
                player.drop(stack, false);
            }
        }
    }
    //检测是否已经有天罚
    private boolean hasNineHeavensPunishment(Player player) {

        if (player.getInventory().contains(new ItemStack(ModItems.NINE_HEAVENS_PUNISHMENT.get()))) {
            return true;
        }

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {
                    if (stacks.getStackInSlot(slot).is(ModItems.NINE_HEAVENS_PUNISHMENT.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    //修复死亡后消失
    @SubscribeEvent
    public void onPlayerClone(PlayerEvent.Clone event) {

        if (!event.isWasDeath()) {
            return;
        }

        Player oldPlayer = event.getOriginal();
        Player newPlayer = event.getEntity();

        CompoundTag oldData = oldPlayer.getPersistentData();
        CompoundTag newData = newPlayer.getPersistentData();

        if (oldData.getBoolean(SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT)) {
            newData.putBoolean(SHOULD_RESTORE_NINE_HEAVENS_PUNISHMENT, true);
        }

        if (oldData.getBoolean(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT)) {
            newData.putBoolean(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT, true);
        }
    }
    private void ensureNineHeavensPunishment(Player player) {

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        if (!data.getBoolean(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT)) {
            data.putBoolean(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT, true);
        }

        if (!hasNineHeavensPunishment(player)) {
            ItemStack stack = new ItemStack(ModItems.NINE_HEAVENS_PUNISHMENT.get());

            if (!tryEquipToCalamitySlot(player, stack)) {
                player.getInventory().add(stack);
            }
        }

        removeDuplicateNineHeavensPunishment(player);
    }
    private void removeDuplicateNineHeavensPunishment(Player player) {

        boolean hasCalamityOne = hasNineHeavensPunishmentInCalamity(player);

        if (!hasCalamityOne) {
            return;
        }

        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {

            ItemStack stack = player.getInventory().getItem(i);

            if (!stack.is(ModItems.NINE_HEAVENS_PUNISHMENT.get())) {
                continue;
            }

            player.getInventory().setItem(i, ItemStack.EMPTY);
        }
    }
    private boolean hasNineHeavensPunishmentInCalamity(Player player) {

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (stacks.getStackInSlot(slot).is(ModItems.NINE_HEAVENS_PUNISHMENT.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }



    //下面是开局给予功能
    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        CompoundTag data = player.getPersistentData();

        if (data.getBoolean(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT)) {
            return;
        }

        data.putBoolean(HAS_RECEIVED_NINE_HEAVENS_PUNISHMENT, true);

        giveAndEquipNineHeavensPunishment(player);
    }
    private void giveAndEquipNineHeavensPunishment(Player player) {

        ItemStack stack = new ItemStack(ModItems.NINE_HEAVENS_PUNISHMENT.get());

        if (tryEquipToCalamitySlot(player, stack)) {
            return;
        }

        if (!player.getInventory().add(stack)) {
            player.drop(stack, false);
        }
    }
    private boolean tryEquipToCalamitySlot(Player player, ItemStack stack) {

        final boolean[] equipped = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (!stacks.getStackInSlot(slot).isEmpty()) {
                        continue;
                    }

                    stacks.setStackInSlot(slot, stack.copy());
                    stack.setCount(0);
                    equipped[0] = true;
                    return;
                }
            });
        });

        return equipped[0];
    }
    //上面写的是开局给予功能

    //这个是防止玩家丢弃功能
    @SubscribeEvent
    public void onItemToss(ItemTossEvent event) {

        Player player = event.getPlayer();

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        ItemStack stack = event.getEntity().getItem();

        if (!stack.is(ModItems.NINE_HEAVENS_PUNISHMENT.get())) {
            return;
        }

        event.setCanceled(true);

        if (!player.getInventory().add(stack.copy())) {
            player.drop(stack.copy(), false);
        }

        event.getEntity().discard();
    }


    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (player.isCreative() || player.isSpectator()) {
            return;
        }

        if (player.getAbilities().flying) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.EARTH_DISASTER)) {
            return;
        }

        Vec3 movement = player.getDeltaMovement();

        player.setDeltaMovement(
                movement.x,
                -0.25D,
                movement.z
        );

        player.fallDistance = 0.0F;
        player.hasImpulse = true;
    }

    private void removeLifeExhaustionIfInactive(Player player) {

        if (!player.hasEffect(ModMobEffects.LIFE_EXHAUSTION)) {
            removeLifeExhaustionModifier(player);
        }
    }
    private void removeLifeExhaustionModifier(Player player) {

        AttributeInstance maxHealth = player.getAttribute(Attributes.MAX_HEALTH);

        if (maxHealth == null) {
            return;
        }

        if (maxHealth.getModifier(NineHeavensPunishmentItem.LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID) != null) {
            maxHealth.removeModifier(NineHeavensPunishmentItem.LIFE_EXHAUSTION_MAX_HEALTH_MODIFIER_ID);
        }
    }
    @SubscribeEvent
    public void onLivingDamage(Pre event) {

        applySummerFanShield(event);
        applySpringShearsFateCut(event);
        applyHeartDemon(event);
        applySpiritExhaustion(event);
    }
    //夏扇实现
    private void applySummerFanShield(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!SummerFanItem.isDamageShieldReady(player)) {
            return;
        }

        event.setNewDamage(0.0F);

        SummerFanItem.consumeDamageShield(player);

        player.displayClientMessage(
                Component.literal("夏扇护体：本次伤害已被化解。"),
                true
        );
    }
    //春剪实现
    private void applySpringShearsFateCut(Pre event) {

        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        if (player.level().isClientSide()) {
            return;
        }

        LivingEntity target = event.getEntity();

        if (!target.isAlive()) {
            return;
        }

        if (target instanceof Player) {
            return;
        }

        if (target.getHealth() <= 1.0F) {
            return;
        }

        if (event.getNewDamage() <= 0.0F) {
            return;
        }

        if (!hasSpringShearsEquipped(player)) {
            return;
        }

        if (!SpringShearsItem.isFateCutReady(player)) {
            return;
        }

        float immediateDamage = target.getMaxHealth()
                * SpringShearsItem.FATE_CUT_IMMEDIATE_MAX_HEALTH_RATIO;

        SpringShearsItem.cutHealthWithoutKilling(target, immediateDamage);

        target.addEffect(new MobEffectInstance(
                ModMobEffects.SPRING_SHEARS_FATE_CUT,
                SpringShearsItem.FATE_CUT_DURATION,
                0,
                false,
                false,
                true
        ));

        target.addEffect(new MobEffectInstance(
                MobEffects.WEAKNESS,
                SpringShearsItem.FATE_CUT_DURATION,
                SpringShearsItem.FATE_CUT_WEAKNESS_AMPLIFIER,
                false,
                false,
                true
        ));

        target.addEffect(new MobEffectInstance(
                MobEffects.WITHER,
                SpringShearsItem.FATE_CUT_DURATION,
                SpringShearsItem.FATE_CUT_WITHER_AMPLIFIER,
                false,
                false,
                true
        ));

        SpringShearsItem.startFateCutCooldown(player);

        player.displayClientMessage(
                Component.literal("春剪裁命：已剪去其半数天命。"),
                true
        );
    }





    //检测春剪是否佩戴
    private boolean hasSpringShearsEquipped(Player player) {

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (stacks.getStackInSlot(slot).is(ModItems.SPRING_SHEARS.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }
    //心魔板块
    @SubscribeEvent
    public void onWitherKilledByHeartDemonMask(LivingDeathEvent event) {

        if (event.getEntity().level().isClientSide()) {
            return;
        }

        if (event.getEntity().getType() != EntityType.WITHER) {
            return;
        }

        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        if (!hasHeartDemonMaskEquipped(player)) {
            return;
        }

        if (NineHeavensPunishmentItem.isHeartDemonReversed(player)) {
            return;
        }

        player.getPersistentData().putBoolean(
                NineHeavensPunishmentItem.HEART_DEMON_REVERSED,
                true
        );

        player.removeEffect(ModMobEffects.HEART_DEMON);

        player.displayClientMessage(
                Component.literal("心魔已反转：魔由心生，亦由心灭。"),
                true
        );
    }
    private boolean hasHeartDemonMaskEquipped(Player player) {

        final boolean[] found = {false};

        CuriosApi.getCuriosInventory(player).ifPresent(curiosInventory -> {
            curiosInventory.getStacksHandler("calamity").ifPresent(stacksHandler -> {

                var stacks = stacksHandler.getStacks();

                for (int slot = 0; slot < stacks.getSlots(); slot++) {

                    if (stacks.getStackInSlot(slot).is(ModItems.HEART_DEMON_MASK.get())) {
                        found[0] = true;
                        return;
                    }
                }
            });
        });

        return found[0];
    }


    //=============================================================================
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {

        Player player = event.getEntity();

        if (player.level().isClientSide()) {
            return;
        }

        removeLifeExhaustionIfInactive(player);
        removeWindEvilIfInactive(player);

        ensureNineHeavensPunishment(player);
    }
    private void applySpiritExhaustion(Pre event) {

        if (!(event.getSource().getEntity() instanceof Player player)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.SPIRIT_EXHAUSTION)) {
            return;
        }

        event.setNewDamage(event.getNewDamage() * 0.6F);    //玩家造成伤害降低至40%
    }
    private void applyHeartDemon(Pre event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (event.getSource().is(DamageTypes.FELL_OUT_OF_WORLD)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.HEART_DEMON)) {
            return;
        }

        player.addEffect(new MobEffectInstance(
                MobEffects.CONFUSION,
                10 * 20,
                0,
                false,
                false,
                true
        ));
    }
    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {

        Player player = event.getEntity();

        if (!(event.getTarget() instanceof AbstractVillager)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.KARMA)) {
            return;
        }

        event.setCanceled(true);
        event.setCancellationResult(InteractionResult.FAIL);
    }
    @SubscribeEvent
    public void onLivingHeal(LivingHealEvent event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.WITHERED_BLOOD)) {
            return;
        }

        if (shouldBlockHealing(player, event.getAmount())) {
            event.setCanceled(true);
        }
    }


    @SubscribeEvent
    public void onItemUseFinish(LivingEntityUseItemEvent.Finish event) {

        if (!(event.getEntity() instanceof Player player)) {
            return;
        }

        if (!player.hasEffect(ModMobEffects.WITHERED_BLOOD)) {
            return;
        }

        ItemStack stack = event.getItem();

        if (!stack.is(Items.GOLDEN_APPLE) && !stack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
            return;
        }

        player.getPersistentData().putLong(
                NineHeavensPunishmentItem.GOLDEN_APPLE_REGEN_BLOCK_TIME,
                player.level().getGameTime()
        );
    }

    private boolean shouldBlockHealing(Player player, float healAmount) {

        if (isGoldenAppleRegenBlocked(player)) {
            return true;
        }

        if (isNaturalFoodHealing(player, healAmount)) {
            return true;
        }

        return false;
    }
    private boolean isGoldenAppleRegenBlocked(Player player) {

        CompoundTag data = player.getPersistentData();

        if (!data.contains(NineHeavensPunishmentItem.GOLDEN_APPLE_REGEN_BLOCK_TIME)) {
            return false;
        }

        long lastEatTime = data.getLong(NineHeavensPunishmentItem.GOLDEN_APPLE_REGEN_BLOCK_TIME);
        long currentTime = player.level().getGameTime();

        // 30秒内阻止金苹果/附魔金苹果带来的生命恢复
        return currentTime - lastEatTime <= 30 * 20;
    }
    private boolean isNaturalFoodHealing(Player player, float healAmount) {

        if (healAmount > 1.0F) {
            return false;
        }

        return player.getFoodData().getFoodLevel() >= 18;
    }
    private void removeWindEvilIfInactive(Player player) {

        if (!player.hasEffect(ModMobEffects.WIND_EVIL)) {
            removeWindEvilModifiers(player);
        }
    }
    private void removeWindEvilModifiers(Player player) {

        AttributeInstance armor = player.getAttribute(Attributes.ARMOR);
        AttributeInstance armorToughness = player.getAttribute(Attributes.ARMOR_TOUGHNESS);

        if (armor != null && armor.getModifier(NineHeavensPunishmentItem.WIND_EVIL_ARMOR_MODIFIER_ID) != null) {
            armor.removeModifier(NineHeavensPunishmentItem.WIND_EVIL_ARMOR_MODIFIER_ID);
        }

        if (armorToughness != null && armorToughness.getModifier(NineHeavensPunishmentItem.WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID) != null) {
            armorToughness.removeModifier(NineHeavensPunishmentItem.WIND_EVIL_ARMOR_TOUGHNESS_MODIFIER_ID);
        }
    }
    // 夺运
    @SubscribeEvent
    public void onLivingDrops(LivingDropsEvent event) {

        Entity deadEntity = event.getEntity();

        if (deadEntity.level().isClientSide()) {
            return;
        }

        boolean hasLuckDeprivationPlayerNearby =
                !deadEntity.level().getEntitiesOfClass(
                        Player.class,
                        deadEntity.getBoundingBox().inflate(LUCK_DEPRIVATION_RANGE),
                        this::isLuckDeprivationActive
                ).isEmpty();

        if (!hasLuckDeprivationPlayerNearby) {
            return;
        }

        event.getDrops().clear();
    }

    private boolean isLuckDeprivationActive(Player player) {
        return player.hasEffect(ModMobEffects.LUCK_DEPRIVATION);
    }


}