package cn.codetector.minecraft.VillageMarker3.Village;

import net.minecraft.util.BlockPos;
import net.minecraft.village.VillageDoorInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Codetector on 2015/11/15.
 *
 * @author Codetector
 */
public class VMVillage {
    public List doors = new ArrayList();
    public BlockPos villageCenter;
    public int radius;
    public int villagerCount;

    public VMVillage(List drs, BlockPos center, int radius, int villagerCount) {
        for (Object vds : drs) {
            doors.add(((VillageDoorInfo) vds).getDoorBlockPos());
        }
        this.villageCenter = center;
        this.radius = radius;
        this.villagerCount = villagerCount;
    }
}
