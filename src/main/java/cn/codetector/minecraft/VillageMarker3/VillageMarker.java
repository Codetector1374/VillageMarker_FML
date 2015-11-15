package cn.codetector.minecraft.VillageMarker3;

import cn.codetector.minecraft.VillageMarker3.TickHandlers.VillageFetchTick;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

/**
 * Created by Codetector on 2015/11/14.
 *
 * @author Codetector
 */
@Mod(modid = VillageMarker.MODID, version = VillageMarker.VERSION)
public class VillageMarker {
    public static final String MODID = "VM18";
    public static final String VERSION = "3.0";

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event){
        FMLCommonHandler.instance().bus().register(new VillageFetchTick());
        FMLLog.getLogger().info("CVM","Load");
    }
}
