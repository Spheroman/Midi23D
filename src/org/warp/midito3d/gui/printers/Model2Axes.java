package org.warp.midito3d.gui.printers;

import org.warp.midito3d.printers.Printer2Axes;

public class Model2Axes implements PrinterModel {
    private MotorSetting[] motors;

    public Model2Axes() {
        motors = new MotorSetting[]{new MotorSetting(),new MotorSetting()};
    }

    public Model2Axes(MotorSetting x, MotorSetting y) {
        motors = new MotorSetting[]{x,y};
    }

    @Override
    public int getMotorsCount() {
        return 2;
    }

    @Override
    public MotorSetting getMotor(int number) {
        return motors[number];
    }

    @Override
    public String getName() {
        return "XY Axes";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public String getMotorName(int i) {
        switch (i) {
            case 0:
                return "Motor X";
            case 1:
                return "Motor Y";
            default:
                return "err";
        }
    }

    @Override
    public Printer2Axes createPrinterObject(PrinterModelArea printerModelArea) {
        return new Printer2Axes(motors[0].createMotorObject(), motors[1].createMotorObject(), printerModelArea.createAreaObject());
    }

}
