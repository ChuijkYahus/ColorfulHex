package jempasam.colorfulhex.armor

import jempasam.hexpigmentplus.HPPRenderHelper
import net.fabricmc.fabric.api.client.rendering.v1.ArmorRenderer
import net.minecraft.client.render.VertexConsumerProvider
import net.minecraft.client.render.entity.model.BipedEntityModel
import net.minecraft.client.util.math.MatrixStack
import net.minecraft.entity.EquipmentSlot
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import org.joml.Quaternionf
import kotlin.math.min


class MagicianHatRenderer: ArmorRenderer {
    override fun render(
        matrices: MatrixStack, buffer: VertexConsumerProvider,
        stack: ItemStack, entity: LivingEntity, slot: EquipmentSlot, light: Int,
        contextModel: BipedEntityModel<LivingEntity>
    ) {
        if(slot!=EquipmentSlot.HEAD)return

        val pi = Math.PI.toFloat()

        //// MOVEMENT ANIMATION ////
        //Height depends on fall time / Is pushed by wind
        var height = min(1f, entity.fallDistance/5f) * 0.2f
        matrices.translate(0f, -height, 0f)

        //// HEAD TRANSFORMATION ////
        matrices.push()
        matrices.multiply(Quaternionf().rotateZYX(contextModel.head.roll, contextModel.head.yaw, contextModel.head.pitch))
        matrices.translate(-.5f, -.75f, -.5f)
        matrices.translate(.5f,.5f,.5f)
        matrices.scale(0.65f, 0.65f, 0.65f)
        matrices.multiply(Quaternionf().rotateZYX(pi,0f,0f))
        matrices.translate(-.5f,-.5f,-.5f)

        HPPRenderHelper.renderModel(buffer, stack, matrices, entity, light)

        matrices.pop()
    }
}