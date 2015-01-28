package com.integraresoftware.android.odemsaprotocols.objects;


import android.util.Log;

public class CalculateDopamineDrip {

    final static String TAG = "CalculateDopamineDrip";

    private double weightKg, concentration, mlPerHour, gttsPerMinute;
    private int dripset, dose;

    public CalculateDopamineDrip(int dose, double weightKg,
                                 double concentration, int dripset) {
        this.dose = dose;
        this.weightKg = weightKg;
        this.concentration = concentration;
        this.dripset = dripset;
    }

    public double calculateMlPerHour() {
        Log.d(TAG, "dose = " + dose + ", weightKg = " + weightKg + ", concentration = " + concentration);

        mlPerHour = dose * weightKg * 60;
        mlPerHour = mlPerHour / concentration;

        return mlPerHour;
    }

    public double calculateDripsPerMinute() {
        gttsPerMinute = (mlPerHour * dripset) / 60;

        return gttsPerMinute;
    }
}
