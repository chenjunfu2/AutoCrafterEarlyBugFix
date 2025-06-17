package net.quackimpala7321.crafter.networking;

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.quackimpala7321.crafter.AutocrafterEarly;
import net.quackimpala7321.crafter.block.entity.CrafterBlockEntity;

public class ModMessages {
    public static final Identifier SLOT_CHANGED = new Identifier(AutocrafterEarly.MOD_ID, "slot_changed");
    
    public static void registerMessages() {
        ServerPlayNetworking.registerGlobalReceiver(SLOT_CHANGED, ((server, player, handler, buf, responseSender) -> {
            int slotId = buf.readInt();
            BlockPos pos = buf.readBlockPos();
            boolean newState = buf.readBoolean();

            server.execute(() -> {
                BlockEntity blockEntity = player.getWorld().getBlockEntity(pos);
                if (!(blockEntity instanceof CrafterBlockEntity crafterBlockEntity)) return;

                crafterBlockEntity.setSlotEnabled(slotId, newState);
            });
        }));
    }
}
