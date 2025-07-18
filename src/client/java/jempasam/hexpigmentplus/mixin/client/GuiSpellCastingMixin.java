package jempasam.hexpigmentplus.mixin.client;

import at.petrak.hexcasting.api.casting.eval.ResolvedPatternType;
import at.petrak.hexcasting.client.gui.GuiSpellcasting;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import net.fabricmc.fabric.impl.client.indigo.renderer.helper.ColorHelper;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(GuiSpellcasting.class)
public class GuiSpellCastingMixin {
	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/ResolvedPatternType;getColor()I"))
	public int render_color(ResolvedPatternType type) {
		//if(!HexlinkClientConfig.INSTANCE.getColored_casting())return type.getColor();
		if(type==ResolvedPatternType.EVALUATED){
			var client=MinecraftClient.getInstance();
			var time=System.currentTimeMillis()%1000000/1000.0f;
			if(client!=null && client.player!=null){
				var colorizer=IXplatAbstractions.INSTANCE.getPigment(client.player);
				if(colorizer!=null)return colorizer.getColorProvider().getColor(time,client.player.getPos().add(0,time,time));
			}
		}
		return type.getColor();
	}


	@Redirect(method = "render", at = @At(value = "INVOKE", target = "Lat/petrak/hexcasting/api/casting/eval/ResolvedPatternType;getFadeColor()I"), remap = false)
	public int render_fadecolor(ResolvedPatternType type) {
		//if(!HexlinkClientConfig.INSTANCE.getColored_casting())return type.getFadeColor();
		if(type==ResolvedPatternType.EVALUATED){
			var client=MinecraftClient.getInstance();
			var time=(System.currentTimeMillis()%1_000_000+200_000)/1000.0f;
			if(client!=null && client.player!=null){
				var colorizer=IXplatAbstractions.INSTANCE.getPigment(client.player);
				if(colorizer!=null)return ColorHelper.multiplyRGB(colorizer.getColorProvider().getColor(time,client.player.getPos().add(0,time,time)),0.2f)+0xcccccc;

			}
		}
		return type.getColor();
	}
}