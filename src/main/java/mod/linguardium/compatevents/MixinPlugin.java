package mod.linguardium.compatevents;

import com.google.common.collect.Sets;
import mod.linguardium.compatevents.mixins.ai.pathing.LandPathNodeMakerMixin;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.List;
import java.util.Set;

import static mod.linguardium.compatevents.CompatEvents.log;
import static mod.linguardium.compatevents.events.ai.pathing.LandPathNodeMaker.getCommonNodeTypeCallback.EVENT;

public class MixinPlugin implements IMixinConfigPlugin {
    boolean LandPathNodeMaker = false;

    @Override
    public void onLoad(String mixinPackage) {


    }

    @Override
    public String getRefMapperConfig() {
        return null;
    }

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        return true;
    }
    private void processCompatEventsValues(ModMetadata meta) {

        for (CustomValue v : meta.getCustomValue("CompatEvents").getAsArray()) {
            if (v.getAsString().equals("LandPathNodeMaker")) {
                log(Level.INFO,meta.getName()+"("+meta.getId()+") requested LandPathNodeMaker Compatibility");
                LandPathNodeMaker = true;
            }
        }
    }
    @Override
    public void acceptTargets(Set<String> myTargets, Set<String> otherTargets) {
        for (ModContainer mod : FabricLoader.getInstance().getAllMods()) {
            if (mod.getMetadata().containsCustomValue("CompatEvents")) {
                processCompatEventsValues(mod.getMetadata());
            }
        }
        if (!LandPathNodeMaker) {
            myTargets.remove("net.minecraft.entity.ai.pathing.LandPathNodeMaker");
        }
        Set<String> toRemove= Sets.newHashSet();
        for (String target : myTargets) {
            if (otherTargets.contains(target)) {
                log(Level.WARN, "Mixin being applied to same target: "+target+"\nIf other mod does not support CompatEvents, please contact that mod's author for compatibility.\nSkipping Mixin.");
                toRemove.add(target);
            }
        }
        myTargets.removeIf(toRemove::contains);
    }

    @Override
    public List<String> getMixins() {
        return null;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        log(Level.INFO, "Mixin "+ mixinClassName + " applied to "+targetClassName);
    }
}
