package work.lclpnet.lclpupdater.misc;

import com.mojang.blaze3d.platform.GlStateManager;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.StringTextComponent;
import work.lclpnet.lclpupdater.util.Helper;
import work.lclpnet.lclpupdater.util.Installation;

public class UpdateScreen extends Screen{

	protected Installation info;
	private boolean startFailed = false;
	
	public UpdateScreen(Installation install) {
		super(new StringTextComponent("Update"));
		this.info = install;
	}
	
	@Override
	protected void init() {
		super.init();
		
		addButton(new Button(this.width / 2 - 100, (int) (this.height * 0.45), 200, 20, "§l§a" + I18n.format("lclpupdater.update"), obj -> {
			Helper.startLCLPLauncher(success -> {
				if(success) this.minecraft.shutdown();
				else startFailed = true;
			});
		}));
		addButton(new Button(this.width / 2 - 100, (int) (this.height * 0.55), 200, 20, "§c" + I18n.format("lclpupdater.cancel"), obj -> this.minecraft.shutdown()));
	}
	
	@Override
	public void render(int p_render_1_, int p_render_2_, float p_render_3_) {
		this.renderBackground();
		
		drawMultiLineCenteredString(font, I18n.format("lclpupdater.title"), 2D, this.width / 2, (int) (this.height * 0.05), 0xfaaf37);
		drawMultiLineCenteredString(font, I18n.format("lclpupdater.desc", info.getVersion()), 1D, this.width / 2, (int) (this.height * 0.2), 0xffffff);
		
		if(startFailed) drawMultiLineCenteredString(font, I18n.format("lclpupdater.startfailed"), 1D, this.width / 2, (int) (this.height * 0.75), 0xff0000);
		
		super.render(p_render_1_, p_render_2_, p_render_3_);
	}

	@SuppressWarnings("deprecation")
	private void drawMultiLineCenteredString(FontRenderer fr, String str, double scale, int x, int y, int color) {
		double neg = 1D / scale;
		x *= neg;
		y *= neg;
		
        for (String s : fr.listFormattedStringToWidth(str, this.width)) {
        	GlStateManager.scaled(scale, scale, scale);
            fr.drawStringWithShadow(s, (float) (x - fr.getStringWidth(s) / 2.0), y, color);
            GlStateManager.scaled(neg, neg, neg);
            y += fr.FONT_HEIGHT * scale;
        }
    }
    
    @Override
    public boolean shouldCloseOnEsc() {
    	return false;
    }
    
}
