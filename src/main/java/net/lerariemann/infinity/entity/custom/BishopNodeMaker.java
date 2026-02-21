package net.lerariemann.infinity.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.pathfinder.Node;
//? if >1.21 {
import net.minecraft.world.level.pathfinder.PathType;
//?} else {
/*import net.minecraft.world.level.pathfinder.BlockPathTypes;
*///?}
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jspecify.annotations.Nullable;

public class BishopNodeMaker extends WalkNodeEvaluator {
    @Override
    public int getNeighbors(Node[] successors, Node node) {
        int i = 0;
        int j = 0;
        //? if >1.21 {
        PathType pathNodeType = this.getCachedPathType(node.x, node.y + 1, node.z);
        PathType pathNodeType2 = this.getCachedPathType(node.x, node.y, node.z);
        //?} else {
        /*BlockPathTypes pathNodeType = this.getCachedBlockType(this.mob, node.x, node.y + 1, node.z);
        BlockPathTypes pathNodeType2 = this.getCachedBlockType(this.mob, node.x, node.y, node.z);
        *///?}
        if (this.mob.getPathfindingMalus(pathNodeType) >= 0.0F && pathNodeType2 !=
                //? if >1.21 {
                PathType
                //?} else {
                /*BlockPathTypes
                *///?}
                .STICKY_HONEY) {
            j = Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
        }

        double d = this.getFloorLevel(new BlockPos(node.x, node.y, node.z));

        Node[] succ = new Node[Direction.Plane.HORIZONTAL
                //? if >1.21 {
                .length()
                //?} else {
                /*.stream().toList().size()
                *///?}
                ];
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Node pathNode = this.findAcceptedNode(node.x + direction.getStepX(), node.y, node.z + direction.getStepZ(), j, d, direction, pathNodeType2);
            succ[direction.get2DDataValue()] = pathNode;
        }
        for (Direction directionx : Direction.Plane.HORIZONTAL) {
            Direction direction2 = directionx.getClockWise();
            if (this.isDiagonalValid(node,
                    //? if <1.21 {
                    /*node,
                    *///?}
                    succ[directionx.get2DDataValue()],
                    succ[direction2.get2DDataValue()])) {
                Node pathNode2 = this.findAcceptedNode(
                        node.x + directionx.getStepX() + direction2.getStepX(),
                        node.y,
                        node.z + directionx.getStepZ() + direction2.getStepZ(),
                        j,
                        d,
                        directionx,
                        pathNodeType2
                );
                if (this.isDiagonalValid(pathNode2
                        //? if <1.21 {
                        /*, null, null, null
                        *///?}
                )) {
                    successors[i++] = pathNode2;
                }
            }
        }

        return i;
    }

    //? if >1.21 {
    static final PathType door = PathType.WALKABLE_DOOR;
    static final PathType fence = PathType.FENCE;
     //?} else {
    /*static final BlockPathTypes door = BlockPathTypes.WALKABLE_DOOR;
    static final BlockPathTypes fence = BlockPathTypes.FENCE;
    *///?}


    //? if >1.21 {
    @Override
    protected boolean isDiagonalValid(Node xNode, @Nullable Node xDiagNode, @Nullable Node zNode) {
        if (xDiagNode == null || zNode == null || (xDiagNode.y > xNode.y && zNode.y > xNode.y)) {
            return false;
        } else if (zNode.type != door && xDiagNode.type != door) {
            boolean bl = xDiagNode.type == fence && zNode.type == fence && (double)this.mob.getBbWidth() < 0.5;
            return (xDiagNode.y < xNode.y || xDiagNode.costMalus >= 0.0F || bl) && (zNode.y < xNode.y || zNode.costMalus >= 0.0F || bl);
        } else {
            return false;
        }
    }
    //?} else {
    /*@Override
    protected boolean isDiagonalValid(Node xNode, @Nullable Node zNode, @Nullable Node xDiagNode, @Nullable Node zDiagNode) {
        if (xDiagNode == null || zNode == null || (xDiagNode.y > xNode.y && zNode.y > xNode.y)) {
            return false;
        } else if (zNode.type != door && xDiagNode.type != door) {
            boolean bl = xDiagNode.type == fence && zNode.type == fence && (double)this.mob.getBbWidth() < 0.5;
            return (xDiagNode.y < xNode.y || xDiagNode.costMalus >= 0.0F || bl) && (zNode.y < xNode.y || zNode.costMalus >= 0.0F || bl);
        } else {
            return false;
        }
    }
    *///?}
}
