package mod.linguardium.compatevents.mixins.ai.pathing;

import mod.linguardium.compatevents.events.ai.pathing.LandPathNodeMaker.getCommonNodeTypeCallback;
import net.minecraft.entity.ai.pathing.LandPathNodeMaker;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LandPathNodeMaker.class)
public abstract class LandPathNodeMakerMixin {
    @Inject(
            method = "getCommonNodeType",
            at = @At("HEAD"),
            cancellable = true
    )
    private static void getCommonNodeTypeCallbackHandler(BlockView blockView, BlockPos blockPos, CallbackInfoReturnable<PathNodeType> info){
        PathNodeType result = getCommonNodeTypeCallback.EVENT.invoker().processBlockState(blockView.getBlockState(blockPos));
        if (result != null) {
            info.setReturnValue(result);
        }
    }
}