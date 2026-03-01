package jempasam.hexpigmentplus

import at.petrak.hexcasting.xplat.IXplatAbstractions
import net.minecraft.client.MinecraftClient
import net.minecraft.client.render.OverlayTexture
import net.minecraft.client.render.RenderLayer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.item.ItemRenderer
import net.minecraft.client.texture.SpriteAtlasTexture
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.client.world.ClientWorld
import net.minecraft.entity.LivingEntity
import net.minecraft.entity.player.PlayerEntity
import net.minecraft.item.ItemStack
import net.minecraft.util.DyeColor
import net.minecraft.util.math.ColorHelper
import net.minecraft.util.math.random.Random
import org.joml.Quaternionf

object HPPRenderHelper {

    fun renderModel(
        consumer: VertexConsumerProvider,
        stack: ItemStack,
        matrices: MatrixStack,
        entity: LivingEntity,
        light: Int,
    ){
        // Get model
        val mc= MinecraftClient.getInstance()
        val root_model=mc.itemRenderer.models.getModel(stack)
        val model=root_model.overrides.apply(root_model,stack,entity.world as? ClientWorld, entity, 0) ?: root_model
        val scale=model.transformation.head.scale
        val translation=model.transformation.head.translation
        val rotation=model.transformation.head.rotation

        // Colorizer
        val colorizer=
            if(entity is PlayerEntity) IXplatAbstractions.INSTANCE.getPigment(entity)
            else CFrozenPigment.DEFAULT.get()

        // Get colors
        val color=colorizer.colorProvider.getColor(entity.age.toFloat()*3, entity.pos.multiply(0.1))
        val color2=colorizer.colorProvider.getColor((entity.age.toFloat()+15)*3, entity.pos.multiply(0.1))
        val color3=colorizer.colorProvider.getColor((entity.age.toFloat()+30)*3, entity.pos.multiply(0.1))
        val color4=colorizer.colorProvider.getColor((entity.age.toFloat()+45)*3, entity.pos.multiply(0.1))
        val color5=colorizer.colorProvider.getColor((entity.age.toFloat()+60)*3, entity.pos.multiply(0.1))

        // Vertexs
        val vertexs = ItemRenderer.getArmorGlintConsumer(
            consumer, RenderLayer.getArmorCutoutNoCull(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE), false, stack.hasGlint()
        )

        //// MODEL TRANSFORMATION ////
        matrices.translate(translation.x.toDouble(), translation.y.toDouble(), translation.z.toDouble())
        matrices.translate(.5f, .5f, .5f)
        matrices.scale(scale.x, scale.y, scale.z)
        matrices.multiply(Quaternionf().rotateZYX(rotation.z*0.017453292F, rotation.y*0.017453292F, rotation.x*0.017453292F))
        matrices.translate(-.5f, -.5f, -.5f)

        // Draw
        val quads=model.getQuads(null, null, Random.create())
        for(quad in quads){
            val quad_color=when(quad.colorIndex){
                0 -> color
                1 -> color2
                2 -> color3
                3 -> color4
                4 -> color5
                else-> DyeColor.WHITE.signColor
            }
            vertexs.quad(
                matrices.peek(),
                quad,
                ColorHelper.Argb.getRed(quad_color)/255f,
                ColorHelper.Argb.getGreen(quad_color)/255f,
                ColorHelper.Argb.getBlue(quad_color)/255f,
                light,
                OverlayTexture.DEFAULT_UV
            )
        }
    }

}