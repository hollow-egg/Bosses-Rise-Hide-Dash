package dev.egg.bosses_rise_hide_dash.mixin;

import dev.egg.bosses_rise_hide_dash.BossesRiseHideDash;
import net.unusual.block_factorys_bosses.event.ClientEvents;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientEvents.class)
public class ClientEventsMixin {
    @Inject(method = "renderGUI", at = @At("HEAD"), cancellable = true)
    private static void SetDash(CallbackInfo ci) {
        if (!BossesRiseHideDash.ShouldShowDash()) {
            ci.cancel();
        }
    }
}
