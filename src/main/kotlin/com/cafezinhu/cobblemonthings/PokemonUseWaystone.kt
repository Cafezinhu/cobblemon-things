package com.cafezinhu.cobblemonthings


import net.minecraft.client.Minecraft
import net.minecraft.resources.ResourceLocation
import org.joml.Vector3f
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.client.gui.interact.wheel.InteractWheelOption
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.Entity
import net.minecraft.world.level.entity.EntityTypeTest

class PokemonUseWaystone {
    companion object {
        @JvmStatic
        public final fun InitializePokemonInteractionWatcher() {

            val resource_location = ResourceLocation("minecraft:textures/item/ender_pearl.png")
            val color = Vector3f(1F, 1F, 1F)
            CobblemonEvents.POKEMON_INTERACTION_GUI_CREATION.subscribe { event ->
                val pokemon_id = event.pokemonID
                var option = InteractWheelOption(resource_location, "Teleport to Home", { color }, {
                    Minecraft.getInstance().setScreen(null)
                    val uuid = Minecraft.getInstance().player?.uuid
                    if(uuid != null) {
                        println("uuid valido")
                        val player = Minecraft.getInstance().singleplayerServer?.playerList?.getPlayer(uuid)
                        val respawn = player?.respawnPosition?.center
                        val dimension_name = player?.respawnDimension?.location()?.path
                        val levels = Minecraft.getInstance().singleplayerServer?.allLevels
                        var level: ServerLevel? = null
                        if(dimension_name != null && levels != null){
                            for(l in levels){
                                if(l.dimension().location().path == dimension_name){
                                    level = l
                                }
                            }
                        }
                        println(player != null)
                        println(respawn != null)
                        if(player != null && respawn != null && level != null){
                            player.teleportTo(level, respawn.x, respawn.y, respawn.z, 0F, 0F)
                        }
                    }
                })
                var levels = Minecraft.getInstance().player?.server?.allLevels
                if(levels == null){
                    levels = Minecraft.getInstance().singleplayerServer?.allLevels
                }
                if(levels != null){
                    for (level in levels){
                        var entity = level.getEntity(event.pokemonID)
                        if(entity != null && entity is PokemonEntity){
                            for(move in entity.pokemon.moveSet.getMoves()){
                                if(move.name.lowercase() == "teleport"){
                                    event.addFillingOption(option)
                                    return@subscribe
                                }
                            }
                        }
                    }
                }


            }
        }
    }
}