package cn.codetector.minecraft.VillageMarker3.TickHandlers;

import cn.codetector.minecraft.VillageMarker3.Village.CachedVillages;
import cn.codetector.minecraft.VillageMarker3.Village.VMVillage;
import cn.codetector.minecraft.VillageMarker3.VillageMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;

import static org.lwjgl.opengl.GL11.*;

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
            Village nearestVillage = DimensionManager.getWorld(Minecraft.getMinecraft().thePlayer.dimension).getVillageCollection().getNearestVillage(Minecraft.getMinecraft().thePlayer.getPosition(),5000);
            event.right.add("\u00A7fNearest Village Villager Count: " + (nearestVillage == null ? "N/A" : nearestVillage.getNumVillagers()));
        }
    }

    @SubscribeEvent
    public void onRenderGameContent(RenderWorldLastEvent event){
        if(Minecraft.getMinecraft().theWorld.getTotalWorldTime()%20 == 0)
        VillageFetchTick.updateVillages();

        double offsetX = Minecraft.getMinecraft().thePlayer.posX;
        double offsetY = Minecraft.getMinecraft().thePlayer.posY;
        double offsetZ = Minecraft.getMinecraft().thePlayer.posZ;

        GlStateManager.pushMatrix();

        GlStateManager.translate(-offsetX,-offsetY,-offsetZ);

        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        int c = 0;

        for (Iterator ir$ = CachedVillages.vmVillages.iterator(); ir$.hasNext();){
            Object irv = ir$.next();
            if (irv instanceof VMVillage){
                VMVillage v = (VMVillage)irv;
                glEnable(GL_BLEND);
                glDisable(GL_TEXTURE_2D);

                glBlendFunc(1, 32769);
                setColor(c%6);
                c++;

                glEnable(GL_POINT_SMOOTH);

                glDepthMask(false);

                wr.setVertexFormat(DefaultVertexFormats.BLOCK);

//                wr.setTranslation(-offsetX,-offsetY,-offsetZ);


                float density = 0.2253521F;

                int intervals = 24 + (int)(density * 72.0F);

                double[] xs = new double[intervals * (intervals / 2 + 1)];
                double[] zs = new double[intervals * (intervals / 2 + 1)];
                double[] ys = new double[intervals * (intervals / 2 + 1)];
                for (double phi = 0.0D; phi < 6.283185307179586D; phi += 6.283185307179586D / intervals) {
                    for (double theta = 0.0D; theta < 3.141592653589793D; theta += 3.141592653589793D / (intervals / 2))
                    {
                        double dx = v.radius * Math.sin(phi) * Math.cos(theta);
                        double dz = v.radius * Math.sin(phi) * Math.sin(theta);
                        double dy = v.radius * Math.cos(phi);

                        int index = (int)(phi / (6.283185307179586D / intervals) + intervals * theta / (3.141592653589793D / (intervals / 2)));

                        xs[index] = (v.villageCenter.getX() + dx);
                        zs[index] = (v.villageCenter.getZ() + dz);
                        ys[index] = (v.villageCenter.getY() + dy);
                    }
                }
                wr.startDrawing(0);
                for (int t1 = 0; t1 < intervals * (intervals / 2 + 1); t1++) {
                    wr.addVertex(xs[t1], ys[t1], zs[t1]);
                }
                t.draw();

                //Door Lines
                for (Iterator i$ = v.doors.iterator(); i$.hasNext();){
                    Object Dobj = i$.next();
                    glLineWidth(2);
                    if (Dobj instanceof BlockPos){
                        wr.startDrawing(1);
                        wr.addVertex(((BlockPos) Dobj).getX(),((BlockPos) Dobj).getY(),((BlockPos) Dobj).getZ());
                        wr.addVertex(v.villageCenter.getX(),v.villageCenter.getY(),v.villageCenter.getZ());
                        Tessellator.getInstance().draw();
                    }
                }

                glDisable(3042);
                glEnable(3553);
                glDepthMask(true);
                wr.setTranslation(0,0,0);
            }
        }
        GlStateManager.popMatrix();

    }

    private static void setColor(int c)
    {
        if (c == 0) {
            glColor4f(1.0F, 0.0F, 0.0F, 1.0F);
        } else if (c == 4) {
            glColor4f(0.0F, 1.0F, 0.0F, 1.0F);
        } else if (c == 2) {
            glColor4f(0.0F, 0.0F, 1.0F, 1.0F);
        } else if (c == 5) {
            glColor4f(1.0F, 1.0F, 0.0F, 1.0F);
        } else if (c == 1) {
            glColor4f(1.0F, 0.0F, 1.0F, 1.0F);
        } else if (c == 3) {
            glColor4f(0.0F, 1.0F, 1.0F, 1.0F);
        } else {
            glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}
