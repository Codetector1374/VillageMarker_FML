package cn.codetector.minecraft.VillageMarker3.Village;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Codetector on 2015/11/14.
 *
 * @author Codetector
 */
public class CachedVillages {
    private static CachedVillages sharedInstance = new CachedVillages();
    private Map<Integer, List> worldVillageMap = new HashMap();

    public static CachedVillages getSharedInstance(){
        return sharedInstance;
    }

    public void clearVillageForDimension(int d){
        if (worldVillageMap.containsKey(d))
            worldVillageMap.get(d).clear();
    }

    public List getVillageListForDimension(int d){
        if (!worldVillageMap.containsKey(d)){
            worldVillageMap.put(d,new ArrayList());
        }
        return worldVillageMap.get(d);
    }
}
