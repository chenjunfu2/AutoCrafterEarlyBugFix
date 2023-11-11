//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.quackimpala7321.crafter.gui.screen.ingame;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.screen.slot.Slot;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.quackimpala7321.crafter.AutocrafterEarly;
import net.quackimpala7321.crafter.networking.ModMessages;
import net.quackimpala7321.crafter.screen.CrafterScreenHandler;
import net.quackimpala7321.crafter.screen.slot.CrafterInputSlot;

@Environment(EnvType.CLIENT)
public class CrafterScreen extends HandledScreen<CrafterScreenHandler> {
    private static final Identifier DISABLED_SLOT_TEXTURE = new Identifier(AutocrafterEarly.MOD_ID, "container/crafter/disabled_slot");
    private static final Identifier POWERED_REDSTONE_TEXTURE = new Identifier(AutocrafterEarly.MOD_ID, "container/crafter/powered_redstone");
    private static final Identifier UNPOWERED_REDSTONE_TEXTURE = new Identifier(AutocrafterEarly.MOD_ID, "container/crafter/unpowered_redstone");
    private static final Identifier TEXTURE = new Identifier(AutocrafterEarly.MOD_ID, "textures/gui/container/crafter.png");
    private static final Text TOGGLEABLE_SLOT_TEXT = Text.translatable("crafter.gui.toggleable_slot");
    private final PlayerEntity player;

    public CrafterScreen(CrafterScreenHandler handler, PlayerInventory playerInventory, Text title) {
        super(handler, playerInventory, title);
        this.player = playerInventory.player;
    }

    protected void init() {
        super.init();
        this.titleX = (this.backgroundWidth - this.textRenderer.getWidth(this.title)) / 2;
    }

    protected void onSlotChangedState(int slotId, int handlerId, boolean newState) {
        if (this.client == null || this.client.world == null) return;

        ClientPlayNetworking.send(
                ModMessages.SLOT_CHANGED,
                PacketByteBufs.create()
                        .writeInt(slotId)
                        .writeBlockPos(this.handler.getPos())
                        .writeBoolean(newState));
    }

    protected void onMouseClick(Slot slot, int slotId, int button, SlotActionType actionType) {
        if (this.player.isSpectator()) {
            super.onMouseClick(slot, slotId, button, actionType);
        } else {
            if (slotId > -1 && slotId < 9 && slot instanceof CrafterInputSlot) {
                if (slot.hasStack()) {
                    super.onMouseClick(slot, slotId, button, actionType);
                    return;
                }

                boolean bl = this.handler.isSlotDisabled(slotId);
                if (bl || this.handler.getCursorStack().isEmpty()) {
                    this.handler.setSlotEnabled(slotId, bl);
                    this.onSlotChangedState(slotId, this.handler.syncId, bl);
                    if (bl) {
                        this.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.4F, 1.0F);
                    } else {
                        this.player.playSound(SoundEvents.UI_BUTTON_CLICK.value(), 0.4F, 0.75F);
                    }
                }
            }

            super.onMouseClick(slot, slotId, button, actionType);
        }
    }

    public void drawDisabledSlot(DrawContext context, CrafterInputSlot slot) {
        context.drawGuiTexture(DISABLED_SLOT_TEXTURE, slot.x - 1, slot.y - 1, 18, 18);
    }

    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        super.render(context, mouseX, mouseY, delta);
        this.drawArrowTexture(context);
        this.drawMouseoverTooltip(context, mouseX, mouseY);
        if (this.focusedSlot instanceof CrafterInputSlot && !this.handler.isSlotDisabled(this.focusedSlot.id) && this.handler.getCursorStack().isEmpty() && !this.focusedSlot.hasStack()) {
            context.drawTooltip(this.textRenderer, TOGGLEABLE_SLOT_TEXT, mouseX, mouseY);
        }

    }

    private void drawArrowTexture(DrawContext context) {
        int i = this.width / 2 + 9;
        int j = this.height / 2 - 48;
        context.drawGuiTexture(this.handler.isTriggered() ? POWERED_REDSTONE_TEXTURE : UNPOWERED_REDSTONE_TEXTURE, i, j, 16, 16);
    }

    protected void drawBackground(DrawContext context, float delta, int mouseX, int mouseY) {
        int i = (this.width - this.backgroundWidth) / 2;
        int j = (this.height - this.backgroundHeight) / 2;
        context.drawTexture(TEXTURE, i, j, 0, 0, this.backgroundWidth, this.backgroundHeight);
    }
}
