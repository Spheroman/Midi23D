package org.warp.midito3d.gui.printers;

import org.warp.midito3d.printers.Motor;

public class MotorSetting {
	public double ppi;

	public MotorSetting(double ppi) {
		this.ppi = ppi;
	}
	
	public MotorSetting() {
		this.ppi = 100;
	}

	public Motor createMotorObject() {
		return new Motor(ppi);
	}
}
