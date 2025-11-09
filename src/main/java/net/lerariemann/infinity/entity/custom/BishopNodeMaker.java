package net.lerariemann.infinity.entity.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.pathfinder.Node;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import org.jetbrains.annotations.Nullable;

public class BishopNodeMaker extends WalkNodeEvaluator {
    @Override
    public int getNeighbors(Node[] successors, Node node) {
        int i = 0;
        int j = 0;
        PathType pathNodeType = this.getCachedPathType(node.x, node.y + 1, node.z);
        PathType pathNodeType2 = this.getCachedPathType(node.x, node.y, node.z);
        if (this.mob.getPathfindingMalus(pathNodeType) >= 0.0F && pathNodeType2 != PathType.STICKY_HONEY) {
            j = Mth.floor(Math.max(1.0F, this.mob.maxUpStep()));
        }

        double d = this.getFloorLevel(new BlockPos(node.x, node.y, node.z));

        Node[] succ = new Node[Direction.Plane.HORIZONTAL.length()];
        for (Direction direction : Direction.Plane.HORIZONTAL) {
            Node pathNode = this.findAcceptedNode(node.x + direction.getStepX(), node.y, node.z + direction.getStepZ(), j, d, direction, pathNodeType2);
            succ[direction.get2DDataValue()] = pathNode;
        }
        for (Direction directionx : Direction.Plane.HORIZONTAL) {
            Direction direction2 = directionx.getClockWise();
            if (this.isDiagonalValid(node,
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
                if (this.isDiagonalValid(pathNode2)) {
                    successors[i++] = pathNode2;
                }
            }
        }

        return i;
    }

    @Override
    protected boolean isDiagonalValid(Node xNode, @Nullable Node zNode, @Nullable Node xDiagNode) {
        if (xDiagNode == null || zNode == null || (xDiagNode.y > xNode.y && zNode.y > xNode.y)) {
            return false;
        } else if (zNode.type != PathType.WALKABLE_DOOR && xDiagNode.type != PathType.WALKABLE_DOOR) {
            boolean bl = xDiagNode.type == PathType.FENCE && zNode.type == PathType.FENCE && (double)this.mob.getBbWidth() < 0.5;
            return (xDiagNode.y < xNode.y || xDiagNode.costMalus >= 0.0F || bl) && (zNode.y < xNode.y || zNode.costMalus >= 0.0F || bl);
        } else {
            return false;
        }
    }
}
