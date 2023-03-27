package de.m_marvin.industria.core.physics.types;

import de.m_marvin.unimat.impl.Quaternion;
import de.m_marvin.univec.impl.Vec3d;

public class ShipPosition {
	
	public Quaternion orientation;
	public Vec3d position;
	
	public ShipPosition(Quaternion orientation, Vec3d position) {
		this.orientation = orientation;
		this.position = position;
	}
	
	public Quaternion getOrientation() {
		return orientation;
	}
	
	public void setOrientation(Quaternion orientation) {
		this.orientation = orientation;
	}
	
	public Vec3d getPosition() {
		return position;
	}
	
	public void setPosition(Vec3d position) {
		this.position = position;
	}
	
}
