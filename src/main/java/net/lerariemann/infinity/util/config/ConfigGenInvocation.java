package net.lerariemann.infinity.util.config;

import net.lerariemann.infinity.InfinityMod;
import net.lerariemann.infinity.access.MinecraftServerAccess;
import net.lerariemann.infinity.registry.core.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigGenInvocation {
    protected Map<String, BlockState> map;
    public static int[] offsets = new int[]{-1, 0, 1};
    public static int[] offsets_y = new int[]{1, 2, 3};
    public ServerLevel world;
    public BlockPos pos;

    public static void invokeOn(ServerPlayer player) {
        //? if <1.21.2 {
        /*ServerLevel w = player.serverLevel();
        int y = w.getMaxBuildHeight() - 10;
        *///?} else {
        ServerLevel w = player.level();
        int y= w.getMaxY();
        //?}
        BlockPos pos = new BlockPos(player.getBlockX(), y, player.getBlockZ());
        (new ConfigGenInvocation(w, pos)).run();
    }

    public ConfigGenInvocation(ServerLevel w, BlockPos p) {
        world = w;
        pos = p;
        map = new HashMap<>();
    }

    public void run() {
        InfinityMod.LOGGER.info("Invoking the name of the Cosmic Altar...");
        //setting up space
        map.put("0,0,0", world.getBlockState(pos));
        world.setBlockAndUpdate(pos, ModBlocks.COSMIC_ALTAR.get().defaultBlockState());
        for (int i : offsets) for (int j : offsets_y) for (int k : offsets) {
            map.put(i + "," + j + "," + k, world.getBlockState(pos.offset(i, j, k)));
            world.setBlockAndUpdate(pos.offset(i, j, k), Blocks.AIR.defaultBlockState());
        }
        //invocation
        ConfigGenerator.generateAll(world.getServer());
        Set<String> fluidBlockNames = ConfigGenerator.generateFluids();
        ConfigGenerator.generateBlocks(world, pos.above(2), pos.above(), fluidBlockNames);
        //putting everything back
        for (int i : offsets) for (int j : offsets_y) for (int k : offsets) {
            world.setBlockAndUpdate(pos.offset(i, j, k), fromMap(i, j, k));
        }
        world.setBlockAndUpdate(pos, fromMap(0, 0, 0));

        ((MinecraftServerAccess)world.getServer()).infinity$onInvocation();
    }

    BlockState fromMap(int i, int j, int k) {
        BlockState s = map.get(i + "," + j + "," + k);
        if (s==null) s = Blocks.AIR.defaultBlockState();
        return s;
    }
}
