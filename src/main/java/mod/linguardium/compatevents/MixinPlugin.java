package mod.linguardium.compatevents;

import com.google.common.collect.Sets;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.metadata.CustomValue;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.apache.logging.log4j.Level;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static mod.linguardium.compatevents.CompatEvents.log;

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

        if (LandPathNodeMaker && otherTargets.contains("net.minecraft.entity.ai.pathing.LandPathNodeMaker")) {
            //myTargets.remove("net.minecraft.entity.ai.pathing.LandPathNodeMaker");
            log(Level.WARN, "LandPathNodeMaker compatibility requested but another mod modifies LandPathNodeMaker.\nIf other mod does not support CompatEvents, please contact that mod's author for compatibility.\nSkipping Mixin.");
            LandPathNodeMaker=false;
        }

    }

    @Override
    public List<String> getMixins() {
        List<String> mixins = new ArrayList<>();
        if (LandPathNodeMaker)
            mixins.add("ai.pathing.LandPathNodeMakerMixin");
        return mixins;
    }

    @Override
    public void preApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {

    }

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        log(Level.INFO, "Mixin "+ mixinClassName + " applied to "+targetClassName);
    }
}
