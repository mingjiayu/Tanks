package Tanks.utils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MovingAverage {
    private final int size;

    public MovingAverage(int size) {
        this.size = size;
    }



    public float next(float value) {
        double sum = 0;
        for (int j = 0; j < size; j++) {
            sum += value;
        }
        return (float) (sum / size);
    }




}
