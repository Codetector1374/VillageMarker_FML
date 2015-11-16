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
    public static void updateVillages(){
        Minecraft mc = Minecraft.getMinecraft();
        if(mc != null && mc.thePlayer != null){
            //Single Player
            WorldServer worldServer = DimensionManager.getWorld(mc.thePlayer.dimension);
            CachedVillages.vmVillages.clear();
            for (Object v : worldServer.getVillageCollection().getVillageList()){
                if (v != null && v instanceof Village) {
                    Village vlg = (Village) v;
                    VMVillage vmVillage = new VMVillage(vlg.getVillageDoorInfoList(), vlg.getCenter(), vlg.getVillageRadius(), vlg.getNumVillagers());
                    CachedVillages.vmVillages.add(vmVillage);
                }else{
                    LogHelper.logger.error("NULL VILLAGE");
                }
            }
        }
    }
}
