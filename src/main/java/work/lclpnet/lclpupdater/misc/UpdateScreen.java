package work.lclpnet.lclpupdater.misc;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
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

		IFormattableTextComponent updateComponent = new TranslationTextComponent("lclpupdater.update")
				.mergeStyle(TextFormatting.GREEN, TextFormatting.BOLD);
		addButton(new Button(this.width / 2 - 100, (int) (this.height * 0.45), 200, 20, updateComponent, obj -> {
			Helper.startLCLPLauncher(success -> {
				if(success) this.minecraft.shutdown();
				else startFailed = true;
			});
		}));

		IFormattableTextComponent cancelComponent = new TranslationTextComponent("lclpupdater.cancel")
				.mergeStyle(TextFormatting.RED);
		addButton(new Button(this.width / 2 - 100, (int) (this.height * 0.55), 200, 20, cancelComponent, obj -> this.minecraft.shutdown()));
	}

	@Override
	public void render(MatrixStack mStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(mStack);

		drawMultiLineCenteredString(mStack, font, new TranslationTextComponent("lclpupdater.title"), 2F, this.width / 2, (int) (this.height * 0.05), 0xfaaf37);
		drawMultiLineCenteredString(mStack, font, new TranslationTextComponent("lclpupdater.desc", info.getVersion()), 1F, this.width / 2, (int) (this.height * 0.2), 0xffffff);

		if(startFailed) drawMultiLineCenteredString(mStack, font, new TranslationTextComponent("lclpupdater.startfailed"), 1F, this.width / 2, (int) (this.height * 0.75), 0xff0000);

		super.render(mStack, mouseX, mouseY, partialTicks);
	}

	protected void drawMultiLineCenteredString(MatrixStack mStack, FontRenderer fr, ITextComponent str, float scale, int x, int y, int color) {
		float neg = 1F / scale;
		x *= neg;
		y *= neg;

		for (IReorderingProcessor s : fr.trimStringToWidth(str, this.width)) {
			mStack.push();
			mStack.scale(scale, scale, scale);
			fr.func_238407_a_(mStack, s, (float) (x - fr.func_243245_a(s) / 2.0), y, color);
			mStack.pop();
			y += fr.FONT_HEIGHT * scale;
		}
	}

	@Override
	public boolean shouldCloseOnEsc() {
		return false;
	}

}
