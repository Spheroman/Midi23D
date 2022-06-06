package org.warp.midito3d.printers;

import org.warp.midito3d.PrinterArea;

import java.io.IOException;
import java.util.Locale;

import org.warp.midito3d.PrinterArea;

public class Printer2Axes implements Printer{
    public static final int modelID = 0x04;

    private final Motor[] motors;
    private final PrinterArea printerArea;
    double[] motorsPosition = new double[2];
    double[] motorsDirection = new double[] {1d, 1d};

    public Printer2Axes(Motor x, Motor y, PrinterArea printerArea) {
        motors = new Motor[]{x,y};
        this.printerArea = printerArea;
    }

    @Override
    public int getMotorsCount() {
        return 2;
    }

    @Override
    public void initialize(GCodeOutput po) throws IOException {
        po.writeLine("M82");
        po.writeLine("G21");
        po.writeLine("G90");
        po.writeLine("G28 X Y");
        po.writeLine("G92 X0 Y0 Z0 (set origin to current position)\n" +
                "G0 X0 Y0 Z0 F2000.0 (Pointless move to origin to reset feed rate to a sane value)");
    }

    @Override
    public void wait(GCodeOutput po, double time) throws IOException {
        po.writeLine(String.format(Locale.US, "G04 P%.4f", time));
    }

    @Override
    public void move(GCodeOutput po, double time, double... speed) throws IOException {
//		double speedPart = Math.sqrt(Math.pow(speed[0], 2d)+Math.pow(speed[1], 2d)+Math.pow(speed[2], 2d));
        time/=60d;
        double distance = Math.sqrt(Math.pow(speed[0] * time, 2d)+Math.pow(speed[1] * time, 2d));
        double speedPart = distance/time*10000;

//		double distance0 = speed[0] * time;
//		double feed0 = speed[0];
//		speedPart = ((distance/distance0)*feed0);

        for (int i = 0; i < 2; i++) {
            double motorDelta = ((speed[i] * time) * motorsDirection[i]);
            motorsPosition[i] += motorDelta;
            if (isBiggerThanMax(i, motorsPosition[i])) {
                motorsDirection[i] = -1d;
            }
            if (isSmallerThanMin(i, motorsPosition[i])) {
                motorsDirection[i] = 1d;
            }
        }

        po.writeLine(String.format(Locale.US, "G01 F%.10f", speedPart));
        po.writeLine(String.format(Locale.US, "G01 F%.10f X%.10f Y%.10f", speedPart, motorsPosition[0], motorsPosition[1]));
    }

    @Override
    public void goTo(GCodeOutput po, double speed, double... position) throws IOException {
        double speedPart = Math.sqrt(Math.pow(speed*motors[0].getStepsPerMillimeter(), 2d)+Math.pow(speed*motors[1].getStepsPerMillimeter(), 2d));
        motorsPosition = position;
        po.writeLine(String.format(Locale.US, "G00 F%.10f X%.10f Y%.10f", speedPart, position[0], position[1]));
    }

    @Override
    public void stop(GCodeOutput po) throws IOException {
    }

    @Override
    public Motor getMotor(int number) {
        return motors[number];
    }

    @Override
    public boolean isBiggerThanMax(int motor, double val) {
        if (motor == 0) return val > printerArea.maxX;
        else if (motor == 1) return val > printerArea.maxY;
        else return false;
    }

    @Override
    public boolean isSmallerThanMin(int motor, double val) {
        if (motor == 0) return val < printerArea.minX;
        else if (motor == 1) return val < printerArea.minY;
        else return false;
    }
}
