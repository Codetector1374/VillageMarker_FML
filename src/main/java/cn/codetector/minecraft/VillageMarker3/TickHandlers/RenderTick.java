package cn.codetector.minecraft.VillageMarker3.TickHandlers;

import cn.codetector.minecraft.VillageMarker3.VillageMarker;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

/**
 * Created by Codetector on 2015/11/15.
 *
 * @author Codetector
 */
public class RenderTick {
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event){
        if(Minecraft.getMinecraft().gameSettings.showDebugInfo) {
        //Debug only info
            event.right.add("");
            event.right.add("\u00A7eVillageMarker Version:" + VillageMarker.VERSION);
        }

    }
}
