/*
 * This file was made by modmuss50. View the licence file to see what licence this is is on. You can always ask me if you would like to use part or all of this file in your project.
 */

package me.modmuss50.network.partSystem.block;


import me.modmuss50.mods.lib.vecmath.Vecs3d;
import me.modmuss50.network.partSystem.ModPart;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import org.lwjgl.opengl.GL11;

public class RenderModPart extends TileEntitySpecialRenderer {
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float delta) {
		TileEntityModPart te = (TileEntityModPart) tileEntity;

		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		{
			for (ModPart modPart : te.getParts()) {
				modPart.renderDynamic(new Vecs3d(0, 0, 0), delta);
			}
		}
		GL11.glPopMatrix();
	}
}
