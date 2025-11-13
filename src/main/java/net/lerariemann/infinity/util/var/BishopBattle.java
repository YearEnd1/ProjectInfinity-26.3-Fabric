package net.lerariemann.infinity.util.var;

import net.lerariemann.infinity.entity.custom.AntEntity;
import net.lerariemann.infinity.entity.custom.BishopEntity;
import net.lerariemann.infinity.registry.core.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;
import java.util.Objects;

public class BishopBattle {
    public String teamName;
    public ServerLevel serverWorld;
    public Scoreboard scoreboard;
    public PlayerTeam team;

    public BishopBattle(ServerLevel world) {
        this(world, "bishop_battle_" + world.random.nextInt());
    }

    public BishopBattle(ServerLevel world, String teamName) {
        this.teamName = teamName;
        serverWorld = world;
        scoreboard = world.getScoreboard();
        team = getOrCreateTeam(teamName);
    }

    public PlayerTeam getOrCreateTeam(String name) {
        PlayerTeam t = scoreboard.getPlayerTeam(name);
        if (t == null) t = scoreboard.addPlayerTeam(name);
        return t;
    }

    public void addEntity(LivingEntity entity) {
        scoreboard.addPlayerToTeam(entity.getScoreboardName(), team);
    }

    public void start(BlockPos pos) {
        AntEntity ant = ModEntities.ANT.get().spawn(serverWorld, pos, MobSpawnType.MOB_SUMMONED);
        BishopEntity bishop = ModEntities.BISHOP.get().spawn(serverWorld, pos, MobSpawnType.MOB_SUMMONED);
        if (ant != null && bishop != null) {
            //FIXME - is there a good way to do scale on 1.20? pekhui?
            //? if >1.21
            /*Objects.requireNonNull(ant.getAttribute(Attributes.SCALE)).setBaseValue(3.0);*/
            Objects.requireNonNull(ant.getAttribute(Attributes.MAX_HEALTH)).setBaseValue(20);
            Objects.requireNonNull(ant.getAttribute(Attributes.MOVEMENT_SPEED)).setBaseValue(0.25f);
            ant.setHealth(20);
            bishop.startRiding(ant);
            bishop.setItemSlot(EquipmentSlot.CHEST, Items.IRON_CHESTPLATE.getDefaultInstance());
            bishop.setItemSlot(EquipmentSlot.LEGS, Items.IRON_LEGGINGS.getDefaultInstance());
            bishop.setItemSlot(EquipmentSlot.FEET, Items.IRON_BOOTS.getDefaultInstance());
            ant.addToBattle(this);
            bishop.addToBattle(this);
        }
    }

    public void stop() {
        scoreboard.removePlayerTeam(team);
    }
}
