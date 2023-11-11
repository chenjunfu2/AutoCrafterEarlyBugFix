package net.quackimpala7321.crafter.networking.packet;

import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.listener.ServerPlayPacketListener;
import net.minecraft.network.packet.Packet;

public class SlotChangedC2SPacket implements Packet<ServerPlayPacketListener> {
    private final int slotId;

    public SlotChangedC2SPacket(int slotId) {
        this.slotId = slotId;
    }

    public SlotChangedC2SPacket(PacketByteBuf buf) {
        this.slotId = buf.readInt();
    }

    @Override
    public void write(PacketByteBuf buf) {

    }

    @Override
    public void apply(ServerPlayPacketListener listener) {

    }

    public int getSlotId() {
        return this.slotId;
    }
}
