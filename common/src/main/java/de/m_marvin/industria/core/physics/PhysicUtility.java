package de.m_marvin.industria.core.physics;

import java.lang.Math;
import java.util.*;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import de.m_marvin.industria.core.physics.types.ShipHitResult;
import de.m_marvin.industria.core.util.GameUtility;
import de.m_marvin.industria.core.util.MathUtility;
import de.m_marvin.unimat.impl.Quaternion;
import de.m_marvin.univec.impl.Vec3i;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.*;
import org.joml.primitives.AABBic;
import org.valkyrienskies.core.api.ships.LoadedShip;
import org.valkyrienskies.core.api.ships.QueryableShipData;
import org.valkyrienskies.core.api.ships.ServerShip;
import org.valkyrienskies.core.api.ships.Ship;
import org.valkyrienskies.core.api.ships.properties.ShipTransform;
import org.valkyrienskies.core.apigame.world.chunks.BlockType;
import org.valkyrienskies.core.impl.game.ships.ShipData;
import org.valkyrienskies.mod.common.BlockStateInfo;
import org.valkyrienskies.mod.common.VSGameUtilsKt;

import de.m_marvin.industria.core.physics.types.ShipPosition;
import de.m_marvin.univec.impl.Vec3d;
import kotlin.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;


import org.joml.Matrix4dc;
import org.joml.Vector3d;

import static org.valkyrienskies.mod.common.VSGameUtilsKt.*;

public class PhysicUtility {

	private static Map<String, Long> shipNames = new HashMap<>();

	/* Naming and finding of ships */


	public static void setShipName(long contraptionId, String name) {
		shipNames.put(name, contraptionId);
	}

	public static Long getShipIdByName(Level level, String name) {
		if (shipNames.containsKey(name)) {
			return shipNames.get(name);
		} else {
			return 0L;
		}
	}

	public static Ship getShipByName(Level level, String name) {
		if (shipNames.containsKey(name)) {
			return VSGameUtilsKt.getShipObjectWorld(level).getLoadedShips().getById(shipNames.get(name));
		} else {
			return null;
		}
	}

	public static Iterable<Ship> getShipIntersecting(Level level, BlockPos position)  {
		return VSGameUtilsKt.getShipsIntersecting(level, new AABB(position, position));
	}

	public static Ship getShipOfBlock(Level level, BlockPos shipBlockPos) {
		return VSGameUtilsKt.getShipObjectManagingPos(level, shipBlockPos);
	}

	public static Ship getShipById(Level level, long id) {
		for (Ship contraption : VSGameUtilsKt.getShipObjectWorld(level).getLoadedShips()) {
			if (contraption.getId() == id) {
				return contraption;
			}
		}
		return null;
	}

	/* Translating of positions and moving of ships */
	
	public static Vec3d toShipPos(Ship ship, Vec3d pos) {
		Matrix4dc worldToShip = ship.getWorldToShip();
		if (worldToShip != null) {
			Vector3d transformPosition = worldToShip.transformPosition(pos.writeTo(new Vector3d()));
			return Vec3d.fromVec(transformPosition);
		}
		return new Vec3d(0, 0, 0);
	}
	
	public static BlockPos toShipBlockPos(Ship ship, Vec3d pos) {
		Vec3d position = toShipPos(ship, pos);
		return new BlockPos(position.x, position.y, position.z);
	}
	
	public static BlockPos toShipBlockPos(Ship ship, BlockPos pos) {
		return toShipBlockPos(ship, Vec3d.fromVec(pos));
	}

	public static Vec3d toWorldPos(Ship ship, Vec3d pos) {
		Matrix4dc shipToWorld = ship.getShipToWorld();
		if (shipToWorld != null) {
			Vector3d transformedPosition = shipToWorld.transformPosition(pos.writeTo(new Vector3d()));
			return Vec3d.fromVec(transformedPosition);
		}
		return new Vec3d(0, 0, 0);
	}
	
	public static Vec3d toWorldPos(Ship contaption, BlockPos pos) {
		return toWorldPos(contaption, Vec3d.fromVec(pos).addI(0.5, 0.5, 0.5));
	}

	public static BlockPos toWorldBlockPos(Ship ship, BlockPos pos) {
		Vec3d position = toWorldPos(ship, pos);
		return new BlockPos(position.x, position.y, position.z);
	}

	public static ShipPosition getPosition(ServerShip ship, boolean massCenter) {
		if (massCenter) {
			Vec3d position = Vec3d.fromVec(ship.getTransform().getPositionInWorld());
			Quaterniondc jomlQuat = ship.getTransform().getShipToWorldRotation();
			Quaternion orientation = new Quaternion((float) jomlQuat.x(), (float) jomlQuat.y(), (float) jomlQuat.z(), (float) jomlQuat.w());
			return new ShipPosition(orientation, position);
		} else {
			AABBic shipBounds = ship.getShipAABB();
			Vec3d shipCoordCenter = MathUtility.getMiddle(new BlockPos(shipBounds.minX(), shipBounds.minY(), shipBounds.minZ()), new BlockPos(shipBounds.maxX(), shipBounds.maxY(), shipBounds.maxZ()));
			Vec3d shipCoordMassCenter = Vec3d.fromVec(ship.getInertiaData().getCenterOfMassInShip());
			Vec3d centerOfMassOffset = shipCoordMassCenter.sub(shipCoordCenter).add(1.0, 1.0, 1.0);
			Vec3d position = Vec3d.fromVec(ship.getTransform().getPositionInWorld()).sub(centerOfMassOffset);
			Quaterniondc jomlQuat = ship.getTransform().getShipToWorldRotation();
			Quaternion orientation = new Quaternion((float) jomlQuat.x(), (float) jomlQuat.y(), (float) jomlQuat.z(), (float) jomlQuat.w());
			return new ShipPosition(orientation, position);
		}

	}

	public static void setPosition(ServerShip ship, ShipPosition position, boolean massCenter) {
		if (massCenter) {
			ShipTransform transform = ship.getTransform();
			((Vector3d) transform.getPositionInWorld()).set(position.getPosition().writeTo(new Vector3d()));
			((Quaterniond) transform.getShipToWorldRotation()).set(position.getOrientation().i(), position.getOrientation().j(), position.getOrientation().k(), position.getOrientation().r());
			((ShipData) ship).setTransform(transform);	// FIXME does not work with LoadedShip
		} else {
			AABBic shipBounds = ship.getShipAABB();
			Vec3d shipCoordCenter = MathUtility.getMiddle(new BlockPos(shipBounds.minX(), shipBounds.minY(), shipBounds.minZ()), new BlockPos(shipBounds.maxX(), shipBounds.maxY(), shipBounds.maxZ()));
			Vec3d shipCoordMassCenter = Vec3d.fromVec(ship.getInertiaData().getCenterOfMassInShip());
			Vec3d centerOfMassOffset = shipCoordMassCenter.sub(shipCoordCenter).add(1.0, 1.0, 1.0);
			ShipTransform transform = ship.getTransform();
			((Vector3d) transform.getPositionInWorld()).set(position.getPosition().add(centerOfMassOffset).writeTo(new Vector3d()));
			((Quaterniond) transform.getShipToWorldRotation()).set(position.getOrientation().i(), position.getOrientation().j(), position.getOrientation().k(), position.getOrientation().r());
			((ShipData) ship).setTransform(transform);	// FIXME does not work with LoadedShip
		}
	}


	public static ServerShip createShipAt(Vec3d position, float scale, Level level) {
		assert level instanceof ServerLevel : "Can't manage ships on client side!";
		Ship parentShip = VSGameUtilsKt.getShipManagingPos(level, position.writeTo(new Vector3d()));
		if (parentShip != null) {
			position = PhysicUtility.toWorldPos(parentShip, position);
		}
		String dimensionId = getDimensionId(level);
		Ship newShip = VSGameUtilsKt.getShipObjectWorld((ServerLevel) level).createNewShipAtBlock(position.writeTo(new Vector3i()), false, scale, dimensionId);

		// Stone for safety reasons
		BlockPos pos2 = PhysicUtility.toShipBlockPos(newShip, position);
		level.setBlock(pos2, Blocks.STONE.defaultBlockState(), 3);

		return (ServerShip) newShip;
	}

	public static Ship convertToShip(AABB areaBounds, boolean removeOriginal, float scale, Level level) {
		assert level instanceof ServerLevel : "Can't manage contraptions on client side!";

		BlockPos structureCornerMin = null;
		BlockPos structureCornerMax = null;

		int areaMinBlockX = (int) Math.floor(areaBounds.minX);
		int areaMinBlockY = (int) Math.floor(areaBounds.minY);
		int areaMinBlockZ = (int) Math.floor(areaBounds.minZ);
		int areaMaxBlockX = (int) Math.floor(areaBounds.maxX);
		int areaMaxBlockY = (int) Math.floor(areaBounds.maxY);
		int areaMaxBlockZ = (int) Math.floor(areaBounds.maxZ);
		boolean hasSolids = false;

		for (int x = areaMinBlockX; x <= areaMaxBlockX; x++) {
			for (int z = areaMinBlockZ; z <= areaMaxBlockZ; z++) {
				for (int y = areaMinBlockY; y <= areaMaxBlockY; y++) {

					BlockPos itPos = new BlockPos(x, y, z);
					BlockState itState = level.getBlockState(itPos);

					if (PhysicUtility.isValidShipBlock(itState)) {

						if (structureCornerMin == null) {
							structureCornerMin = itPos;
						} else {
							structureCornerMin = MathUtility.getMinCorner(itPos, structureCornerMin);
						}

						if (structureCornerMax == null) {
							structureCornerMax = itPos;
						} else {
							structureCornerMax = MathUtility.getMaxCorner(itPos, structureCornerMax);
						}

					}

					if (PhysicUtility.isSolidShipBlock(itState)) hasSolids = true;

				}
			}
		}

		if (!hasSolids) return null;

		if (structureCornerMax == null) structureCornerMax = structureCornerMin = new BlockPos(areaBounds.getCenter().x(), areaBounds.getCenter().y(), areaBounds.getCenter().z());

		Vec3d contraptionPos = MathUtility.getMiddle(structureCornerMin, structureCornerMax);
		ServerShip contraption = createShipAt(contraptionPos, scale, level);

		Vec3d contraptionOrigin = PhysicUtility.toShipPos(contraption, contraptionPos);

		for (int x = areaMinBlockX; x <= areaMaxBlockX; x++) {
			for (int z = areaMinBlockZ; z <= areaMaxBlockZ; z++) {
				for (int y = areaMinBlockY; y <= areaMaxBlockY; y++) {
					BlockPos itPos = new BlockPos(x, y, z);
					Vec3d relativePosition = Vec3d.fromVec(itPos).sub(contraptionPos);
					Vec3d shipPos = contraptionOrigin.add(relativePosition);

					GameUtility.copyBlock(level, itPos, new BlockPos(shipPos.x, shipPos.y, shipPos.z));

				}
			}
		}

		if (removeOriginal) {
			for (int x = structureCornerMin.getX(); x <= structureCornerMax.getX(); x++) {
				for (int z = structureCornerMin.getZ(); z <= structureCornerMax.getZ(); z++) {
					for (int y = structureCornerMin.getY(); y <= structureCornerMax.getY(); y++) {
						GameUtility.removeBlock(level, new BlockPos(x, y, z));
					}
				}
			}
		}

		for (int x = structureCornerMin.getX(); x <= structureCornerMax.getX(); x++) {
			for (int z = structureCornerMin.getZ(); z <= structureCornerMax.getZ(); z++) {
				for (int y = structureCornerMin.getY(); y <= structureCornerMax.getY(); y++) {
					BlockPos itPos = new BlockPos(x, y, z);
					Vec3d relativePosition = Vec3d.fromVec(itPos).sub(contraptionPos);
					Vec3d shipPos = contraptionOrigin.add(relativePosition);

					GameUtility.triggerUpdate(level, itPos);
					GameUtility.triggerUpdate(level, new BlockPos(shipPos.x, shipPos.y, shipPos.z));
				}
			}
		}

		setPosition((ServerShip) contraption, new ShipPosition(new Quaternion(new Vec3i(0, 1, 1), 0), contraptionPos), false);

		return contraption;

	}

	public static ServerShip assembleToShip(List<BlockPos> blocks, boolean removeOriginal, float scale, Level level) {
		assert level instanceof ServerLevel : "Can't manage contraptions on client side!";

		if (blocks.isEmpty()) {
			return null;
		}

		BlockPos structureCornerMin = blocks.get(0);
		BlockPos structureCornerMax = blocks.get(0);
		boolean hasSolids = false;

		for (BlockPos itPos : blocks) {
			structureCornerMin = MathUtility.getMinCorner(structureCornerMin, itPos);
			structureCornerMax = MathUtility.getMaxCorner(structureCornerMax, itPos);

			if (PhysicUtility.isSolidShipBlock(level.getBlockState(itPos))) hasSolids = true;
		}

		if (!hasSolids) return null;

		Vec3d contraptionPos = MathUtility.getMiddle(structureCornerMin, structureCornerMax);
		ServerShip contraption = createShipAt(contraptionPos, scale, level);

		Vec3d contraptionOrigin = PhysicUtility.toShipPos(contraption, contraptionPos);
		BlockPos centerBlockPos = new BlockPos(contraptionPos.x, contraptionPos.y, contraptionPos.z);

		for (BlockPos itPos : blocks) {
			Vec3d relativePosition = Vec3d.fromVec(itPos).sub(contraptionPos);
			Vec3d shipPos = contraptionOrigin.add(relativePosition);

			GameUtility.copyBlock(level, itPos, new BlockPos(shipPos.x, shipPos.y, shipPos.z));

		}

		if (!blocks.contains(centerBlockPos)) {
			BlockPos centerShipPos = PhysicUtility.toShipBlockPos(contraption, centerBlockPos);
			level.setBlock(centerShipPos, Blocks.AIR.defaultBlockState(), 3);
		}

		if (removeOriginal) {
			for (BlockPos itPos : blocks) {
				GameUtility.removeBlock(level, itPos);
			}
		}

		for (BlockPos itPos : blocks) {
			Vec3d relativePosition = Vec3d.fromVec(itPos).sub(contraptionPos);
			Vec3d shipPos = contraptionOrigin.add(relativePosition);

			GameUtility.triggerUpdate(level, itPos);
			GameUtility.triggerUpdate(level, new BlockPos(shipPos.x, shipPos.y, shipPos.z));
		}

		setPosition((ServerShip) contraption, new ShipPosition(new Quaternion(new Vec3i(0, 1, 1), 0), contraptionPos), false);

		return contraption;


	}

	public static void removeShip(ServerLevel level, Ship ship) {
		AABBic bounds = ship.getShipAABB();
		if (bounds != null) {
			for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
				for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
					for (int z = bounds.minZ(); z <= bounds.maxZ(); z++) {
						GameUtility.removeBlock(level, new BlockPos(x, y, z));
					}
				}
			}
		}
		shipNames.remove(ship.getId());
	}

	/* Listing and creation ships in the world */

	public static QueryableShipData<LoadedShip> getLoadedShips(Level level) {
		return VSGameUtilsKt.getShipObjectWorld(level).getLoadedShips();
	}

	public static QueryableShipData<Ship> getAllShips(Level level) {
		return VSGameUtilsKt.getShipObjectWorld(level).getAllShips();
	}

	public static ServerShip assembleToShip(ServerLevel level, List<BlockPos> blocks, boolean removeOriginal, float scale) {
		assert level instanceof ServerLevel : "Can't manage contraptions on client side!";

		if (blocks.isEmpty()) {
			return null;
		}

		BlockPos structureCornerMin = blocks.get(0);
		BlockPos structureCornerMax = blocks.get(0);
		boolean hasSolids = false;

		for (BlockPos itPos : blocks) {
			structureCornerMin = MathUtility.getMinCorner(structureCornerMin, itPos);
			structureCornerMax = MathUtility.getMaxCorner(structureCornerMax, itPos);

			if (PhysicUtility.isSolidShipBlock(level.getBlockState(itPos))) hasSolids = true;
		}

		if (!hasSolids) return null;

		Vec3d contraptionPos = MathUtility.getMiddle(structureCornerMin, structureCornerMax);
		ServerShip contraption = createShipAt(contraptionPos, scale, level);

		Vec3d contraptionOrigin = PhysicUtility.toShipPos(contraption, contraptionPos);
		BlockPos centerBlockPos = new BlockPos(contraptionPos.x, contraptionPos.y, contraptionPos.z);

		for (BlockPos itPos : blocks) {
			Vec3d relativePosition = Vec3d.fromVec(itPos).sub(contraptionPos);
			Vec3d shipPos = contraptionOrigin.add(relativePosition);

			GameUtility.copyBlock(level, itPos, new BlockPos(shipPos.x, shipPos.y, shipPos.z));

		}

		if (!blocks.contains(centerBlockPos)) {
			BlockPos centerShipPos = PhysicUtility.toShipBlockPos(contraption, centerBlockPos);
			level.setBlock(centerShipPos, Blocks.AIR.defaultBlockState(), 3);
		}

		if (removeOriginal) {
			for (BlockPos itPos : blocks) {
				GameUtility.removeBlock(level, itPos);
			}
		}

		for (BlockPos itPos : blocks) {
			Vec3d relativePosition = Vec3d.fromVec(itPos).sub(contraptionPos);
			Vec3d shipPos = contraptionOrigin.add(relativePosition);

			GameUtility.triggerUpdate(level, itPos);
			GameUtility.triggerUpdate(level, new BlockPos(shipPos.x, shipPos.y, shipPos.z));
		}

		setPosition((ServerShip) contraption, new ShipPosition(new Quaternion(new Vec3i(0, 1, 1), 0), contraptionPos), false);

		return contraption;
	}

	/* Raycasting for ships */

	public static ShipHitResult clipForShip(Level level, Vec3d from, Vec3d direction, double range) {
		return clipForShip(level, from, from.add(direction.mul(range)));
	}

	public static ShipHitResult clipForShip(Level level, Vec3d from, Vec3d to) {
		ClipContext clipContext = new ClipContext(from.writeTo(new Vec3(0, 0, 0)), to.writeTo(new Vec3(0, 0, 0)), ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, null);
		HitResult clipResult = level.clip(clipContext);

		if (clipResult.getType() == HitResult.Type.BLOCK) {
			BlockPos hitBlockPos = ((BlockHitResult) clipResult).getBlockPos();
			Ship ship = VSGameUtilsKt.getShipObjectManagingPos(level, hitBlockPos);
			if (ship != null) {
				Vec3 hitPosition = clipResult.getLocation();
				return ShipHitResult.hit(hitPosition, hitBlockPos, ship);
			}

		}
		return ShipHitResult.miss(clipResult.getLocation());
	}

	/* Util stuff */

	public static void triggerBlockChange(Level level, BlockPos pos, BlockState prevState, BlockState newState) {
		BlockStateInfo.INSTANCE.onSetBlock(level, pos, prevState, newState);
	}
	
	public static boolean isSolidShipBlock(BlockState state) {
		Pair<Double, BlockType> blockData = BlockStateInfo.INSTANCE.get(state);
		return blockData.getSecond() == VSGameUtilsKt.getVsCore().getBlockTypes().getSolid() && blockData.getFirst() > 0;
	}
	
	public static boolean isValidShipBlock(BlockState state) {
		return !state.isAir();
	}
	
}
