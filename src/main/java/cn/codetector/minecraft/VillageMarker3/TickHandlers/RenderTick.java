package cn.codetector.minecraft.VillageMarker3.TickHandlers;

import cn.codetector.minecraft.VillageMarker3.Village.CachedVillages;
import cn.codetector.minecraft.VillageMarker3.Village.VMVillage;
import cn.codetector.minecraft.VillageMarker3.VillageMarker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.village.Village;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Iterator;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;

/**
 * Created by Codetector on 2015/11/15.
 *
 * @author Codetector
 */
public class RenderTick {
    EntityPlayer p;
    double offsetX;
    double offsetY;
    double offsetZ;
    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent.Text event){
        if(Minecraft.getMinecraft().gameSettings.showDebugInfo) {
        //Debug only info
            event.right.add("");
            event.right.add("\u00A7eVillageMarker Version:" + VillageMarker.VERSION);
            Village nearestVillage = DimensionManager.getWorld(Minecraft.getMinecraft().thePlayer.dimension).getVillageCollection().getNearestVillage(Minecraft.getMinecraft().thePlayer.getPosition(),5000);
            event.right.add("\u00A7fNearest Village Villager Count: " + (nearestVillage == null ? "N/A" : nearestVillage.getNumVillagers()));
            event.right.add("Nearest Village Door Count: "+(nearestVillage == null ? "N/A" : nearestVillage.getNumVillageDoors()));
            event.right.add("Nearest Village Center: "+(nearestVillage == null ? "N/A" : nearestVillage.getCenter().getX()+","+nearestVillage.getCenter().getY()+","+nearestVillage.getCenter().getZ()+"(XYZ)"));
        }
    }

    @SubscribeEvent
    public void onRenderGameContent(RenderWorldLastEvent event){
        VillageFetchTick.updateVillages();
        if (this.p == null || this.p != Minecraft.getMinecraft().thePlayer){
            p = Minecraft.getMinecraft().thePlayer;
        }
        offsetX = p.posX;
        offsetY = p.posY;
        offsetZ = p.posZ;

        Tessellator t = Tessellator.getInstance();
        WorldRenderer wr = t.getWorldRenderer();
        int c = 0;
        List vmVillages = CachedVillages.getSharedInstance().getVillageListForDimension(Minecraft.getMinecraft().thePlayer.dimension);
        for (Iterator ir$ = vmVillages.iterator(); ir$.hasNext();){
            Object irv = ir$.next();
            if (irv instanceof VMVillage){
                VMVillage v = (VMVillage)irv;
                glEnable(GL_BLEND);
                glDisable(GL_TEXTURE_2D);

                wr.setTranslation(-offsetX,-offsetY,-offsetZ);

                glBlendFunc(1, 32769);
                setColor(c%6);
                c++;

                glEnable(GL_POINT_SMOOTH);
                glDepthMask(false);
                wr.setVertexFormat(DefaultVertexFormats.BLOCK);

                //Render Sphere
                renderDotedSphere(t.getWorldRenderer(),v.villageCenter,v.radius);

                //Door Lines
                for (Iterator i$ = v.doors.iterator(); i$.hasNext();){
                    Object Dobj = i$.next();
                    glLineWidth(4);
                    if (Dobj instanceof BlockPos){
                        wr.startDrawing(1);
                        wr.addVertex(((BlockPos) Dobj).getX(),((BlockPos) Dobj).getY(),((BlockPos) Dobj).getZ());
                        wr.addVertex(v.villageCenter.getX(),v.villageCenter.getY(),v.villageCenter.getZ());
                        Tessellator.getInstance().draw();
                    }
                }

                //Spawning Area

                renderSpawningBox(t.getWorldRenderer(),v.villageCenter,c);

                glDisable(3042);
                glEnable(3553);
                glDepthMask(true);
                wr.setTranslation(0,0,0);
            }
        }

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

    private static void renderDotedSphere(WorldRenderer wr, BlockPos vCen, int radius){
        float density = 0.2253521F;

        int intervals = 24 + (int)(density * 72.0F);

        double[] xs = new double[intervals * (intervals / 2 + 1)];
        double[] zs = new double[intervals * (intervals / 2 + 1)];
        double[] ys = new double[intervals * (intervals / 2 + 1)];
        for (double phi = 0.0D; phi < 6.283185307179586D; phi += 6.283185307179586D / intervals) {
            for (double theta = 0.0D; theta < 3.141592653589793D; theta += 3.141592653589793D / (intervals / 2))
            {
                double dx = radius * Math.sin(phi) * Math.cos(theta);
                double dz = radius * Math.sin(phi) * Math.sin(theta);
                double dy = radius * Math.cos(phi);

                int index = (int)(phi / (6.283185307179586D / intervals) + intervals * theta / (3.141592653589793D / (intervals / 2)));

                xs[index] = (vCen.getX() + dx);
                zs[index] = (vCen.getZ() + dz);
                ys[index] = (vCen.getY() + dy);
            }
        }
        wr.startDrawing(0);
        for (int t1 = 0; t1 < intervals * (intervals / 2 + 1); t1++) {
            wr.addVertex(xs[t1], ys[t1], zs[t1]);
        }
        Tessellator.getInstance().draw();
    }

    private static void renderSpawningBox(WorldRenderer wr, BlockPos vCen, int c)
    {
        glPolygonMode(1032, 6914);
        glDisable(2884);

        wr.startDrawing(7);

        glLineWidth(2.0F);

        setWallColor((c - 1) % 6);
        glBlendFunc(772, 1);

        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);

        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);

        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);

        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);

        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);

        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);

        Tessellator.getInstance().draw();

        setColor((c - 1) % 6);
        glBlendFunc(1, 32769);

        wr.startDrawing(7);
        glPolygonMode(1032, 6913);

        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);

        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);

        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);

        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);

        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() + 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);

        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() + 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() + 2.999D, vCen.getZ() - 7.999D);
        wr.addVertex(vCen.getX() - 7.999D, vCen.getY() - 2.999D, vCen.getZ() - 7.999D);
        
        Tessellator.getInstance().draw();
        
        glEnable(2852);
        glLineStipple(5, (short)34952);

        wr.startDrawing(1);
        for (int xi = -8; xi <= 8; xi++)
        {
            for (int yi = -3; yi <= 3; yi++) {
                if ((xi == -8) || (xi == 8) || (yi == -3) || (yi == 3))
                {
                    wr.addVertex(vCen.getX() + xi, vCen.getY() + yi, vCen.getZ() - 8);
                    wr.addVertex(vCen.getX() + xi, vCen.getY() + yi, vCen.getZ() + 8);

                    wr.addVertex(vCen.getX() - 8, vCen.getY() + yi, vCen.getZ() + xi);
                    wr.addVertex(vCen.getX() + 8, vCen.getY() + yi, vCen.getZ() + xi);
                }
            }
            for (int yi = -8; yi <= 8; yi++) {
                if ((xi == -8) || (xi == 8) || (yi == -8) || (yi == 8))
                {
                    wr.addVertex(vCen.getX() + xi, vCen.getY() + 3, vCen.getZ() + yi);
                    wr.addVertex(vCen.getX() + xi, vCen.getY() - 3, vCen.getZ() + yi);
                }
            }
        }
        Tessellator.getInstance().draw();

        wr.addVertex(0.0D, 0.0D, 0.0D);

        glDisable(2852);
        glPolygonMode(1032, 6914);
        glEnable(2884);
    }

    private static void setWallColor(int c)
    {
        if (c == 0) {
            glColor4f(0.12F, 0.0F, 0.0F, 0.0F);
        } else if (c == 4) {
            glColor4f(0.0F, 0.1F, 0.0F, 0.0F);
        } else if (c == 2) {
            glColor4f(0.0F, 0.0F, 0.125F, 0.0F);
        } else if (c == 5) {
            glColor4f(0.105F, 0.105F, 0.0F, 0.0F);
        } else if (c == 1) {
            glColor4f(0.105F, 0.0F, 0.105F, 0.0F);
        } else if (c == 3) {
            glColor4f(0.0F, 0.105F, 0.105F, 0.0F);
        } else {
            glColor4f(1.0F, 1.0F, 1.0F, 0.0F);
        }
    }
}
