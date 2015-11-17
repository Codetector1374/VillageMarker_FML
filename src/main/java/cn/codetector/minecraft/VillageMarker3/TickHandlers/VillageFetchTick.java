package cn.codetector.minecraft.VillageMarker3.TickHandlers;

import cn.codetector.minecraft.VillageMarker3.LogHelper;
import cn.codetector.minecraft.VillageMarker3.Village.CachedVillages;
import cn.codetector.minecraft.VillageMarker3.Village.VMVillage;
import net.minecraft.client.Minecraft;
import net.minecraft.village.Village;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

/**
 * Created by Codetector on 2015/11/14.
 *
 * @author Codetector
 */
public class VillageFetchTick {
    public static void updateVillages(){
        if(Minecraft.getMinecraft() != null){
            //Single Player
            Integer[] DimensionIds = DimensionManager.getIDs();
            for (int d : DimensionIds) {
                WorldServer worldServer = DimensionManager.getWorld(d);
                CachedVillages sharedCV = CachedVillages.getSharedInstance();
                sharedCV.clearVillageForDimension(d);
                for (Object v : worldServer.getVillageCollection().getVillageList()) {
                    if (v != null && v instanceof Village) {
                        Village vlg = (Village) v;
                        VMVillage vmVillage = new VMVillage(vlg.getVillageDoorInfoList(), vlg.getCenter(), vlg.getVillageRadius(), vlg.getNumVillagers());
                        sharedCV.getVillageListForDimension(d).add(vmVillage);
                    } else {
                        LogHelper.logger.error("NULL VILLAGE");
                    }
                }
            }
        }
    }
}
