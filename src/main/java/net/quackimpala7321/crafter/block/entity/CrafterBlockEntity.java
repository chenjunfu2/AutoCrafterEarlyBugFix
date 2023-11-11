//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package net.quackimpala7321.crafter.block.entity;

import com.google.common.annotations.VisibleForTesting;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.LootableContainerBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventories;
import net.minecraft.inventory.RecipeInputInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.recipe.RecipeMatcher;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.quackimpala7321.crafter.registry.ModBlockEntities;
import net.quackimpala7321.crafter.block.CrafterBlock;
import net.quackimpala7321.crafter.screen.CrafterScreenHandler;

import java.util.Iterator;
import java.util.List;

public class CrafterBlockEntity extends LootableContainerBlockEntity implements RecipeInputInventory, ExtendedScreenHandlerFactory {
    public static final int GRID_WIDTH = 3;
    public static final int GRID_HEIGHT = 3;
    public static final int GRID_SIZE = 9;
    public static final int SLOT_DISABLED = 1;
    public static final int SLOT_ENABLED = 0;
    public static final int TRIGGERED_PROPERTY = 9;
    public static final int PROPERTIES_COUNT = 10;
    private DefaultedList<ItemStack> inputStacks;
    private int craftingTicksRemaining;
    protected final PropertyDelegate propertyDelegate;

    public CrafterBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.CRAFTER, pos, state);
        this.inputStacks = DefaultedList.ofSize(GRID_SIZE, ItemStack.EMPTY);
        this.craftingTicksRemaining = 0;
        this.propertyDelegate = new PropertyDelegate() {
            private final int[] disabledSlots = new int[GRID_SIZE];
            private int triggered = 0;

            public int get(int index) {
                return index == TRIGGERED_PROPERTY ? this.triggered : this.disabledSlots[index];
            }

            public void set(int index, int value) {
                if (index == TRIGGERED_PROPERTY) {
                    this.triggered = value;
                } else {
                    this.disabledSlots[index] = value;
                }

            }

            public int size() {
                return PROPERTIES_COUNT;
            }
        };
    }

    public Text getContainerName() {
        return Text.translatable("container.crafter");
    }

    public ScreenHandler createScreenHandler(int syncId, PlayerInventory playerInventory) {
        return new CrafterScreenHandler(syncId, playerInventory, this, this.propertyDelegate);
    }

    public void setSlotEnabled(int slot, boolean enabled) {
        if (this.canToggleSlot(slot)) {
            this.propertyDelegate.set(slot, enabled ? SLOT_ENABLED : SLOT_DISABLED);
            this.markDirty();
        }
    }

    public boolean isSlotDisabled(int slot) {
        if (slot >= 0 && slot < 9) {
            return this.propertyDelegate.get(slot) == SLOT_DISABLED;
        } else {
            return false;
        }
    }

    public boolean isValid(int slot, ItemStack stack) {
        if (this.propertyDelegate.get(slot) == SLOT_DISABLED) {
            return false;
        } else {
            ItemStack itemStack = this.inputStacks.get(slot);
            int i = itemStack.getCount();
            if (i >= itemStack.getMaxCount()) {
                return false;
            } else if (itemStack.isEmpty()) {
                return true;
            } else {
                return !this.betterSlotExists(i, itemStack, slot);
            }
        }
    }

    private boolean betterSlotExists(int count, ItemStack stack, int slot) {
        for(int i = slot + 1; i < 9; ++i) {
            if (!this.isSlotDisabled(i)) {
                ItemStack itemStack = this.getStack(i);
                if (itemStack.isEmpty() || itemStack.getCount() < count && ItemStack.canCombine(itemStack, stack)) {
                    return true;
                }
            }
        }

        return false;
    }

    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.craftingTicksRemaining = nbt.getInt("crafting_ticks_remaining");
        this.inputStacks = DefaultedList.ofSize(this.size(), ItemStack.EMPTY);
        if (!this.deserializeLootTable(nbt)) {
            Inventories.readNbt(nbt, this.inputStacks);
        }

        int[] is = nbt.getIntArray("disabled_slots");

        for(int i = 0; i < 9; ++i) {
            this.propertyDelegate.set(i, 0);
        }

        int[] var7 = is;
        int var4 = is.length;

        for(int var5 = 0; var5 < var4; ++var5) {
            int j = var7[var5];
            if (this.canToggleSlot(j)) {
                this.propertyDelegate.set(j, 1);
            }
        }

        this.propertyDelegate.set(TRIGGERED_PROPERTY, nbt.getInt("triggered"));
    }

    protected void writeNbt(NbtCompound nbt) {
        super.writeNbt(nbt);
        nbt.putInt("crafting_ticks_remaining", this.craftingTicksRemaining);
        if (!this.serializeLootTable(nbt)) {
            Inventories.writeNbt(nbt, this.inputStacks);
        }

        this.putDisabledSlots(nbt);
        this.putTriggered(nbt);
    }

    public int size() {
        return GRID_SIZE;
    }

    public boolean isEmpty() {
        Iterator<ItemStack> var1 = this.inputStacks.iterator();

        ItemStack itemStack;
        do {
            if (!var1.hasNext()) {
                return true;
            }

            itemStack = var1.next();
        } while(itemStack.isEmpty());

        return false;
    }

    public ItemStack getStack(int slot) {
        return this.inputStacks.get(slot);
    }

    public void setStack(int slot, ItemStack stack) {
        if (this.isSlotDisabled(slot)) {
            this.setSlotEnabled(slot, true);
        }

        super.setStack(slot, stack);
    }

    public boolean canPlayerUse(PlayerEntity player) {
        if (this.world != null && this.world.getBlockEntity(this.pos) == this) {
            return !(player.squaredDistanceTo((double)this.pos.getX() + 0.5, (double)this.pos.getY() + 0.5, (double)this.pos.getZ() + 0.5) > 64.0);
        } else {
            return false;
        }
    }

    @Override
    public DefaultedList<ItemStack> getInvStackList() {
        return this.inputStacks;
    }

    protected void setInvStackList(DefaultedList<ItemStack> list) {
        this.inputStacks = list;
    }

    public int getWidth() {
        return GRID_WIDTH;
    }

    public int getHeight() {
        return GRID_HEIGHT;
    }

    @Override
    public List<ItemStack> getInputStacks() {
        return this.inputStacks;
    }

    public void provideRecipeInputs(RecipeMatcher finder) {
        for(ItemStack itemStack : this.inputStacks) {
            finder.addUnenchantedInput(itemStack);
        }
    }

    private void putDisabledSlots(NbtCompound nbt) {
        IntList intList = new IntArrayList();

        for(int i = 0; i < GRID_SIZE; ++i) {
            if (this.isSlotDisabled(i)) {
                intList.add(i);
            }
        }

        nbt.putIntArray("disabled_slots", intList);
    }

    private void putTriggered(NbtCompound nbt) {
        nbt.putInt("triggered", this.propertyDelegate.get(TRIGGERED_PROPERTY));
    }

    public void setTriggered(boolean triggered) {
        this.propertyDelegate.set(TRIGGERED_PROPERTY, triggered ? 1 : 0);
    }

    @VisibleForTesting
    public boolean isTriggered() {
        return this.propertyDelegate.get(TRIGGERED_PROPERTY) == 1;
    }

    public static void tickCrafting(World world, BlockPos pos, BlockState state, CrafterBlockEntity blockEntity) {
        int i = blockEntity.craftingTicksRemaining - 1;
        if (i >= 0) {
            blockEntity.craftingTicksRemaining = i;
            if (i == 0) {
                world.setBlockState(pos, state.with(CrafterBlock.CRAFTING, false), 3);
            }

        }
    }

    public void setCraftingTicksRemaining(int craftingTicksRemaining) {
        this.craftingTicksRemaining = craftingTicksRemaining;
    }

    public int getComparatorOutput() {
        int i = 0;

        for(int j = 0; j < this.size(); ++j) {
            ItemStack itemStack = this.getStack(j);
            if (!itemStack.isEmpty() || this.isSlotDisabled(j)) {
                ++i;
            }
        }

        return i;
    }

    private boolean canToggleSlot(int slot) {
        return slot > -1 && slot < 9 && this.inputStacks.get(slot).isEmpty();
    }

    @Override
    public void writeScreenOpeningData(ServerPlayerEntity player, PacketByteBuf buf) {
        buf.writeBlockPos(this.pos);
    }
}
