package com.cafezinhu.cobblemonthings

import net.fabricmc.api.ClientModInitializer

class CobblemonThingsClient: ClientModInitializer{
    override fun onInitializeClient() {
        PokemonUseWaystone.InitializePokemonInteractionWatcher()
    }
}