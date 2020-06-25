package mod.linguardium.compatevents.events.ai.pathing.LandPathNodeMaker;


import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.block.BlockState;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.util.ActionResult;


/*
        Event intended to be run in LandPathNodeMaker$getCommonNodeType in order to
        determine the PathNodeType that the blockstate provides.

        return null to pass processing to other events or vanilla method
        return PathNodeType to end processing

        Intended compatibility addressed:
        Lithium optimizes the getCommonNodeType call with an overwrite causing incompatibilities.
        This interface will allow Lithium to get the proper PathNodeType using its getTaggedBlockType$lithium method

 */
public interface getCommonNodeTypeCallback
{
    Event<getCommonNodeTypeCallback> EVENT = EventFactory.createArrayBacked(getCommonNodeTypeCallback.class,
            (listeners) -> (state) -> {
                for (getCommonNodeTypeCallback listener : listeners) {
                    PathNodeType result = listener.processBlockState(state);

                    if(result != null) {
                        return result;
                    }
                }
                return null;
            });

    PathNodeType processBlockState(BlockState state);
}
