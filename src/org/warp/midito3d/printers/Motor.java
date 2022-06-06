package org.warp.midito3d.printers;

public class Motor {
	private final double stepsPerMillimeter;
	
	public Motor(double stepsPerMillimeter) {
		this.stepsPerMillimeter = stepsPerMillimeter;
	}
	
	public double getStepsPerMillimeter() {
		return stepsPerMillimeter;
	}
}
