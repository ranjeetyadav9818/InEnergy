package com.inenergis.util;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Antonio on 21/11/2017.
 */
public final class MapUtils {
    private MapUtils(){}

    public static Double calculateMaxDistance(Pair<Double, Double> mapCenter, List<Pair<Double, Double>> points) {
        Double result = 0.0;
        for (Pair<Double, Double> point : points) {
            double sqrt = Math.sqrt(Math.pow(mapCenter.getKey() - point.getKey(), 2) + Math.pow(mapCenter.getValue() - point.getValue(), 2));
            result = Math.max(result, sqrt);
        }
        return result;
    }
    public static Double calculateDistance(Pair<Double, Double> point1, Pair<Double, Double> point2) {
        return Math.sqrt(Math.pow(point1.getKey() - point2.getKey(), 2) + Math.pow(point1.getValue() - point2.getValue(), 2));
    }

    public static int calculateZoom(Double distance) {
        //LOG(250/B1,2)+1
        if (distance == 0.0) {
            return 16;
        }
        return (int) Math.round(Math.log(250 / distance) + 2.5);
    }

    public static Pair<Double, Double> calculateMapCenter(List<Pair<Double,Double>> coordinates) {
        Double mapCenterLat = 0d;
        Double mapCenterLong = 0d;
        if(CollectionUtils.isNotEmpty(coordinates)){
            for (Pair<Double,Double> coordinate : coordinates) {
                mapCenterLat += coordinate.getKey();
                mapCenterLong += coordinate.getValue();
            }
            mapCenterLat = mapCenterLat/coordinates.size();
            mapCenterLong = mapCenterLong/coordinates.size();
        }
        return new ImmutablePair<>(mapCenterLat,mapCenterLong);
    }

    public static Pair<Double, Double> calculateCenter(Pair<Double,Double> coordinate1, Pair<Double,Double> coordinate2) {
        return calculateMapCenter(Arrays.asList(coordinate1, coordinate2));
    }

    public static Pair<Double, Double> calculateFirstPointSquare(List<Pair<Double,Double>> coordinates) {
        Double mapCenterLat = 2000d;
        Double mapCenterLong = 2000d;
        for (Pair<Double,Double> coordinate : coordinates) {
            mapCenterLat = Math.min(coordinate.getLeft(),mapCenterLat);
            mapCenterLong = Math.min(coordinate.getRight(),mapCenterLong);
        }
        return new ImmutablePair<>(mapCenterLat,mapCenterLong);
    }
}
