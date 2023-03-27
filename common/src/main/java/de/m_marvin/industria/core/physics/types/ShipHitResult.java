package de.m_marvin.industria.core.physics.types;

import org.valkyrienskies.core.api.ships.Ship;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ShipHitResult extends HitResult {

	private final Ship ship;
	private final BlockPos shipBlock;
	private final boolean miss;
		
	protected ShipHitResult(boolean miss, Vec3 location, BlockPos shipBlock, Ship ship) {
		super(location);
		this.miss = miss;
		this.shipBlock = shipBlock;
		this.ship = ship;
	}
	
	@Override
	public Type getType() {
		return this.miss ? Type.MISS : Type.BLOCK;
	}
	
	public BlockPos getShipBlock() {
		return shipBlock;
	}
	
	public Ship getShip() {
		return ship;
	}
	
	public static ShipHitResult miss(Vec3 location) {
		return new ShipHitResult(true, location, null, null);
	}
	
	public static ShipHitResult hit(Vec3 location, BlockPos shipBlock, Ship ship) {
		return new ShipHitResult(false, location, shipBlock, ship);
	}
	
}
