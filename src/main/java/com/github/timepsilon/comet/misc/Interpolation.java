package com.github.timepsilon.comet.misc;

import org.bukkit.util.EulerAngle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class Interpolation {

    public static List<EulerAngle> angleLerp(EulerAngle start, EulerAngle finish,int time) {
        List<Double> t = IntStream.range(0, time).boxed().collect(Collectors.toList()).stream().map(x -> (double) x/time).collect(Collectors.toList());
        List<EulerAngle> angles = new ArrayList<>();

        t.forEach(p -> {
            double n = easeInOut(p);
            angles.add(new EulerAngle(
                    Lerp(start.getX(), finish.getX(), n),
                    Lerp(start.getY(), finish.getY(), n),
                    Lerp(start.getZ(), finish.getZ(), n)));
        });

        return angles;
    }

    /**Linear Interpolation
     *
     * @param start The starting value
     * @param end The ending value
     * @param t The percent of the interpolation
     * @return The linearly interpolated value at t between start and end
     */
    private static double Lerp(double start, double end, double t) {
        return start + (end - start) * t;
    }

    private static double Flip(double x) {
        return 1 - x;
    }

    private static double easeOut(double x) {
        return Flip(easeIn(1-x));
    }

    private static double easeIn(double x) {
        return x*x;
    }

    private static double easeInOut(double x) {
        return Lerp(easeIn(x),easeOut(x),x);
    }
}
