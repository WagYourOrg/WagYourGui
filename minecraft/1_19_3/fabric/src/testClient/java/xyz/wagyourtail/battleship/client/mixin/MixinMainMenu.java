package xyz.wagyourtail.battleship.client.mixin;

import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.network.chat.Component;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import xyz.wagyourtail.battleship.client.screen.MainMenuScreen;
import xyz.wagyourtail.wagyourgui.api.element.Renderable;

@Mixin(TitleScreen.class)
public abstract class MixinMainMenu extends Screen {

    protected MixinMainMenu(Component title) {
        super(title);
    }

    @Inject(method = "init", at = @At("RETURN"))
    private void init(CallbackInfo info) {
        // add button to main menu top left
        this.addRenderableWidget(Button.builder(Component.literal("battleship"), (btn) -> {
            Renderable.RENDERER.openScreen(new MainMenuScreen());
        }).bounds(5, 5, 100, 20).build());
    }
}
