package cn.codetector.minecraft.VillageMarker3.TickHandlers;

import cn.codetector.minecraft.VillageMarker3.LogHelper;
import cn.codetector.minecraft.VillageMarker3.Village.CachedVillages;
import cn.codetector.minecraft.VillageMarker3.Village.VMVillage;
import net.minecraft.client.Minecraft;
import net.minecraft.village.Village;
import net.minecraft.village.VillageDoorInfo;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.List;

/**
 * Created by Codetector on 2015/11/14.
 *
 * @author Codetector
 */
public class VillageFetchTick {
    @SubscribeEvent
    public void onWorldTick(TickEvent.WorldTickEvent event){
        Minecraft mc = Minecraft.getMinecraft();
        if(mc != null && mc.thePlayer != null){
            //Single Player
            WorldServer worldServer = DimensionManager.getWorld(mc.thePlayer.dimension);
            LogHelper.logger.info(worldServer.getVillageCollection());
        }
    }
}
