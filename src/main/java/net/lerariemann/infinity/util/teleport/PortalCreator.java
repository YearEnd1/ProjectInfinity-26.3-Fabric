package net.lerariemann.infinity.util.teleport;


import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.access.MinecraftServerAccess;
import net.lerariemann.infinity.block.custom.Boopable;
import net.lerariemann.infinity.compat.CreateCompat;
import net.lerariemann.infinity.compat.DimLibCompat;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.lerariemann.infinity.block.custom.InfinityPortalBlock;
import net.lerariemann.infinity.block.entity.InfinityPortalBlockEntity;
import net.lerariemann.infinity.dimensions.RandomDimension;
import net.lerariemann.infinity.registry.core.ModItems;
import net.lerariemann.infinity.registry.var.ModSounds;
import net.lerariemann.infinity.util.InfinityMethods;
import net.lerariemann.infinity.util.core.CommonIO;
import net.lerariemann.infinity.util.core.NbtUtils;
import net.lerariemann.infinity.util.loading.DimensionGrabber;
import net.lerariemann.infinity.options.PortalColorApplier;
import net.lerariemann.infinity.registry.var.ModCriteria;
import net.lerariemann.infinity.registry.var.ModPayloads;
import net.lerariemann.infinity.registry.var.ModStats;
import net.lerariemann.infinity.util.platform.InfinityPlatform;
import net.minecraft.BlockUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
//? if >1.21 {
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.component.WritableBookContent;
import net.minecraft.world.item.component.WrittenBookContent;
//?}
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.LevelResource;

import java.nio.file.Path;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

import static net.lerariemann.infinity.InfinityMod.MOD_ID;
import static net.lerariemann.infinity.compat.ComputerCraftCompat.checkPrintedPage;

public interface PortalCreator {
    /**
     * Check if the item that is colliding with the Portal can be used to
     * transform it into an Infinity Portal.
     */
    static void tryCreatePortalFromItem(ServerLevel world, BlockPos pos, ItemEntity entity) {
        if (entity.isRemoved()) return;
        ItemStack itemStack = entity.getItem();

        /* Check if the item provided is a transfinite key. */
        if (entity.getItem().getItem().equals(ModItems.TRANSFINITE_KEY.get())) {
            ResourceLocation key_dest = ModItems.TRANSFINITE_KEY.get().getDestinationParsed(itemStack, world);
            boolean bl = modifyOnInitialCollision(key_dest, world, pos);
            if (bl) entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            return;
        }

        /* Check if the item provided is a book of some kind. */
        //? if >1.21 {
        WritableBookContent writableComponent = itemStack.getComponents().get(DataComponents.WRITABLE_BOOK_CONTENT);
        WrittenBookContent writtenComponent = itemStack.getComponents().get(DataComponents.WRITTEN_BOOK_CONTENT);
        String printedComponent = null;
        if (InfinityPlatform.INSTANCE.isModLoaded("computercraft")) {
            try {
                printedComponent = checkPrintedPage(itemStack);
            }
            catch (Exception e) {
                InfinityMod.LOGGER.warn("An incompatible version of ComputerCraft is present. Please update it to the latest version");
            }
        }
        if (writableComponent != null || writtenComponent != null || printedComponent != null) {
            String content = parseComponents(writableComponent, writtenComponent, printedComponent);
            content = InfinityMethods.dimTextPreprocess(content);
            ResourceLocation id = InfinityMethods.dimTextToId(content);
            boolean bl = modifyOnInitialCollision(id, world, pos);
            if (bl) entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            recordIdTranslation(world.getServer(), id, content);
        }
        //?} else {
        /*else if (itemStack.getItem() == Items.WRITTEN_BOOK || itemStack.getItem() == Items.WRITABLE_BOOK) {
            var compound = itemStack.getTag();
            String content;
            if (compound != null) {
                content = parseComponents(compound, itemStack.getItem());
            }
            else content = "";
            var id = InfinityMethods.dimTextToId(content);
            boolean bl = modifyOnInitialCollision(id, world, pos);
            if (bl) entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
            recordIdTranslation(world.getServer(), id, content);
        }
        else if (InfinityPlatform.INSTANCE.isModLoaded("computercraft")) {
            try {
                String content;
                content = checkPrintedPage(itemStack);
                var id = InfinityMethods.dimTextToId(content);
                boolean bl = modifyOnInitialCollision(id, world, pos);
                if (bl) entity.remove(Entity.RemovalReason.CHANGED_DIMENSION);
                recordIdTranslation(world.getServer(), id, content);
            } catch (Exception e) {
                InfinityMod.LOGGER.warn("An incompatible version of ComputerCraft is present. Please update it to the latest version");
            }
        }
        *///?}
    }

    /**
     * Extracts the string used to generate the dimension ID from component content.
     */
    //? if >1.21 {
    static String parseComponents(WritableBookContent writableComponent, WrittenBookContent writtenComponent, String printedComponent) {
        String content = "";
        try {
            if (writableComponent != null) {
                content = writableComponent.pages().getFirst().raw();
            }
            if (writtenComponent != null) {
                content = writtenComponent.pages().getFirst().raw().getString();
            }
        }
        catch (NoSuchElementException e) {
            content = "";
        }

        if (printedComponent != null) {
            content = printedComponent;
        }
        return content;
    }
    //?} else {
    /*static String parseComponents(CompoundTag compound, Item item) {
        ListTag pages = compound.getList("pages", Tag.TAG_STRING);
        if (pages.isEmpty()) {
            return "";
        }
        else if (item == Items.WRITTEN_BOOK) {
            String pagesString = pages.get(0).getAsString();
            return pagesString.substring(pagesString.indexOf(':')+2, pagesString.length()-2);
        }
        else if (item == Items.WRITABLE_BOOK) {
            return pages.get(0).getAsString();
        }
        else return "";
    }
    *///?}

    /**
     * Sets the portal color and destination and calls to open the portal immediately if the portal key is blank.
     * Statistics for opening the portal are attributed to the nearest player.
     */
    static boolean modifyOnInitialCollision(ResourceLocation dimName, ServerLevel world, BlockPos pos) {
        MinecraftServer server = world.getServer();
        Player nearestPlayer = world.getNearestPlayer(pos.getX(), pos.getY(), pos.getZ(), 5, false);

        if (((MinecraftServerAccess)server).infinity$needsInvocation()) {
            onInvocationNeedDetected(nearestPlayer);
            return false;
        }

        /* Set color and destination. Open status = the world that is being accessed exists already. */
        boolean dimensionExistsAlready = server.levelKeys().contains(ResourceKey.create(Registries.DIMENSION, dimName));
        modifyPortalRecursive(world, pos, dimName, dimensionExistsAlready);

        if (dimensionExistsAlready) {
            if (nearestPlayer != null) nearestPlayer.awardStat(ModStats.PORTALS_OPENED_STAT, 1);
            runAfterEffects(world, pos, false, true);
        }

        /* If the portal key is blank, open the portal immediately. */
        else if (InfinityMod.provider.isPortalKeyBlank()) {
            openWithStatIncrease(nearestPlayer, server, world, pos);
        }
        else {
            runAfterEffects(world, pos, false, false);
        }
        return true;
    }

    /* Calls to open the portal and attributes the relevant statistics to a player provided. */
    static void openWithStatIncrease(Player player, MinecraftServer s, ServerLevel world, BlockPos pos) {
        if (((MinecraftServerAccess)s).infinity$needsInvocation()) {
            onInvocationNeedDetected(player);
            return;
        }
        boolean isDimensionNew = open(s, world, pos);
        if (player != null) {
            if (isDimensionNew) {
                player.awardStat(ModStats.DIMS_OPENED_STAT, 1);
                ModCriteria.DIMS_OPENED.get().trigger((ServerPlayer)player);
            }
            player.awardStat(ModStats.PORTALS_OPENED_STAT, 1);
        }
    }

    static void onInvocationNeedDetected(Player player) {
        if (player != null) player.displayClientMessage(Component.translatable("error.infinity.invocation_needed"), false);
    }

    /**
     * Opens the portal by trying to make it usable, including a call to generate a dimension if needed.
     */
    static boolean open(MinecraftServer s, ServerLevel world, BlockPos pos) {
        BlockEntity blockEntity = world.getBlockEntity(pos);
        boolean bl = false;
        if (blockEntity instanceof InfinityPortalBlockEntity npbe) {
            /* Call dimension creation. */
            ResourceLocation i = npbe.getDimension();
            if (i.getNamespace().equals(MOD_ID)) {
                bl = tryAddInfinityDimension(s, i);
            }

            /* Set the portal's open status making it usable. */
            modifyPortalRecursive(world, pos, be -> {
                be.setOpen(true);
                be.setChanged();
            });
            runAfterEffects(world, pos, bl, true);
        }
        return bl;
    }

    /**
     * Updates this and all neighbouring portal blocks with a new dimension and open status.
     */
    static void modifyPortalRecursive(ServerLevel world, BlockPos pos, ResourceLocation id, boolean open) {
        modifyPortalRecursive(world, pos, forInitialSetupping(world, pos, id, open));
    }

    /**
     * Recursively creates a queue of neighbouring portal blocks and for each of them executes an action.
     */
    static void modifyPortalRecursive(ServerLevel world, BlockPos pos, Consumer<InfinityPortalBlockEntity> consumer) {
        modifyPortalRecursive(world, pos, new PortalModifier(consumer));
    }
    static void modifyPortalRecursive(ServerLevel world, BlockPos pos, BiConsumer<Level, BlockPos> modifier) {
        Direction.Axis axis = world.getBlockState(pos).getValue(NetherPortalBlock.AXIS);
        BlockUtil.FoundRectangle rect = BlockUtil.getLargestRectangleAround(pos, axis, 21,
                Direction.Axis.Y, 21, posx -> world.getBlockState(posx).getBlock() instanceof NetherPortalBlock);
        for (int i = 0; i < rect.axis1Size; i++) for (int j = 0; j < rect.axis2Size; j++) {
            BlockPos blockPos = rect.minCorner.above(j).relative(axis, i);
            modifier.accept(world, blockPos);
        }
    }

    static Consumer<BlockPos> infPortalSetupper(ServerLevel world, BlockPos pos) {
        BlockState originalState = world.getBlockState(pos);
        BlockState state = ModBlocks.PORTAL.get().defaultBlockState()
                .setValue(NetherPortalBlock.AXIS, originalState.getValue(NetherPortalBlock.AXIS))
                .setValue(InfinityPortalBlock.BOOP, !Boopable.getBoop(world.getBlockState(pos)));
        return p -> world.setBlock(p, state, 3, 1);
    }

    static PortalModifierUnion forInitialSetupping(ServerLevel world, BlockPos pos, ResourceLocation id, boolean open) {
        PortalColorApplier applier = PortalColorApplier.of(id, world.getServer());
        PortalModifierUnion union = new PortalModifierUnion()
                .addSetupper(infPortalSetupper(world, pos))
                .addModifier(nbpe -> nbpe.setDimension(id))
                .addModifier(npbe -> npbe.setColor(applier.apply(npbe.getBlockPos())))
                .addModifier(npbe -> npbe.setOpen(open))
                .addModifier(BlockEntity::setChanged);
        //? if <1.21.2 {
        /*if (InfinityPlatform.INSTANCE.isModLoaded("create")) {
            union.addModifier(CreateCompat::tryModifyRails);
        }
        *///?}
        return union;
    }

    /**
     * Calls to create the dimension based on its ID. Returns true if the dimension being opened is indeed brand new.
     */
    static boolean tryAddInfinityDimension(MinecraftServer server, ResourceLocation id) {
        /* checks if the dimension requested is valid and does not already exist */
        if (!id.getNamespace().equals(MOD_ID)) return false;
        ResourceKey<Level> key = ResourceKey.create(Registries.DIMENSION, id);
        if (((MinecraftServerAccess)(server)).infinity$hasToAdd(key)) return false;
        ServerLevel w = server.getLevel(key);
        if (w!=null) return false;

        /* creates the dimension datapack */
        RandomDimension d = new RandomDimension(id, server);

        // create the dimension
        if (InfinityPlatform.INSTANCE.isModLoaded("dimlib")) {
            DimLibCompat.add(server, id, DimensionGrabber.readDimensionFromDisk(d));
        } else {
            ((MinecraftServerAccess)(server)).infinity$addWorld(key, DimensionGrabber.readDimensionFromDisk(d));
        }
        server.getPlayerList().getPlayers().forEach(
                a -> sendNewWorld(a, id, d)); //and send everyone its data for clientside updating
        return true;
    }

    /**
     * Create and send S2C packets necessary for the client to process a freshly added dimension.
     */
    static void sendNewWorld(ServerPlayer player, ResourceLocation id, RandomDimension d) {
        d.random_biomes.forEach(b -> new ModPayloads.BiomeAddS2CPayload(InfinityMethods.getId(b.name), b.data).send(player));
        new ModPayloads.WorldAddS2CPayload(id, d.type != null ? d.type.data : new CompoundTag()).send(player);
    }

    /**
     * Jingle signaling if the portal is usable or not.
     */
    static void runAfterEffects(ServerLevel world, BlockPos pos, boolean dimensionIsNew, boolean portalWorks) {
        if (!portalWorks) playSound(world, pos, ModSounds.portalFail);
        else {
            if (dimensionIsNew) playSound(world, pos, ModSounds.portalOpen);
            playSound(world, pos, SoundEvents.BEACON_ACTIVATE);
        }
    }
    static void playSound(ServerLevel world, BlockPos pos, SoundEvent soundEvent) {
        world.playSound(null, pos, soundEvent, SoundSource.BLOCKS, 1f, 1f);
    }

    record PortalModifier(Consumer<InfinityPortalBlockEntity> modifier) implements BiConsumer<Level, BlockPos> {
        @Override
        public void accept(Level world, BlockPos pos) {
            if (world.getBlockEntity(pos) instanceof InfinityPortalBlockEntity npbe) modifier.accept(npbe);
        }
    }

    record PortalModifierUnion(List<Consumer<BlockPos>> setuppers, List<Consumer<InfinityPortalBlockEntity>> modifiers)
            implements BiConsumer<Level, BlockPos> {
        public PortalModifierUnion() {
            this(new ArrayList<>(), new ArrayList<>());
        }
        PortalModifierUnion addSetupper(Consumer<BlockPos> setupper) {
            setuppers.add(setupper);
            return this;
        }
        public PortalModifierUnion addModifier(Consumer<InfinityPortalBlockEntity> modifier) {
            modifiers.add(modifier);
            return this;
        }

        @Override
        public void accept(Level world, BlockPos pos) {
            setuppers.forEach(setupper -> setupper.accept(pos));
            BlockEntity blockEntity = world.getBlockEntity(pos);
            if (blockEntity instanceof InfinityPortalBlockEntity npbe)
                modifiers.forEach(modifier -> modifier.accept(npbe));
        }
    }

    static void recordIdTranslation(MinecraftServer server, ResourceLocation id, String value) {
        Path dir = server.getWorldPath(LevelResource.DATAPACK_DIR).resolve("infinity");
        String filename = "translation_tables.json";
        CompoundTag comp = CommonIO.read(dir.resolve(filename));
        String key = id.getPath();
        if (comp.contains(key)) {
            ListTag l;
            if (NbtUtils.compoundHasString(comp, key)) {
                l = new ListTag();
                l.add(comp.get(key));
            }
            else l = NbtUtils.getList(comp, key, Tag.TAG_STRING);
            StringTag nbts = StringTag.valueOf(value);
            if (l.contains(nbts)) return; //no need to record a value twice
            comp.remove(key);
            comp.put(key, l);
        }
        else comp.putString(key, value);
        CommonIO.write(comp, dir, filename);
    }
}
