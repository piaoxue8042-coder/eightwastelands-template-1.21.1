package com.px8042.eightwastelands.damage;

import com.px8042.eightwastelands.eightwastelands;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {

    public static final ResourceKey<DamageType> HEAVENLY_TRIBULATION_LIGHTNING =
            ResourceKey.create(
                    Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(
                            eightwastelands.MOD_ID,
                            "heavenly_tribulation_lightning"
                    )
            );

    public static final ResourceKey<DamageType> HEAVENLY_THUNDER_SEAL_LIGHTNING =
            ResourceKey.create(
                    Registries.DAMAGE_TYPE,
                    ResourceLocation.fromNamespaceAndPath(
                            eightwastelands.MOD_ID,
                            "heavenly_thunder_seal_lightning"
                    )
            );
}