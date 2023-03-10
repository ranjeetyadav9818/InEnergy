package com.inenergis.controller.asset;

import com.inenergis.entity.assetTopology.NetworkType;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.device.DeviceLine;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.MapUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.Polyline;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class PipelineMapController extends CommodityMapController {

    Logger log = LoggerFactory.getLogger(PipelineMapController.class);

    private Polyline polyline;
    private Long deviceToAdd;
    private DeviceLine selectedLine;
    private DeviceLine newDeviceLine;
    private List<DeviceLine> deviceLines;
    private CommodityType commodityType;

    @PostConstruct
    public void init() {
        commodityType = ParameterEncoderService.getCommodityTypeParameter();
        mapModel = new DefaultMapModel();
        deviceLines = deviceLineService.getAll();
        List<Pair<Double, Double>> coordinates = new ArrayList<>();
        List<AssetDevice> allDevices = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(deviceLines)) {
            for (DeviceLine deviceLine : deviceLines) {
                Polyline polyline = generatePolyline(deviceLine);

                LatLng coord1 = new LatLng(deviceLine.getStartLatitude().doubleValue(), deviceLine.getStartLongitude().doubleValue());
                polyline.getPaths().add(coord1);
                coordinates.add(new ImmutablePair<>(coord1.getLat(), coord1.getLng()));
                addIntermediateLines(deviceLine, coordinates, polyline);
                coord1 = new LatLng(deviceLine.getEndLatitude().doubleValue(), deviceLine.getEndLongitude().doubleValue());
                coordinates.add(new ImmutablePair<>(coord1.getLat(), coord1.getLng()));
                polyline.getPaths().add(coord1);
                mapModel.addOverlay(polyline);
            }
            log.info("zoom {} calculated based on {}", zoom, coordinates);
        }

        final Map<NetworkType, List<AssetDevice>> networkMap = assetDeviceService.getAll().stream()
                .distinct()
                .filter(device -> device.getAsset().getAssetProfile().getNetworkType().getCommodityType() == commodityType)
                .collect(Collectors.groupingBy(device -> device.getAsset().getAssetProfile().getNetworkType()));

        String[] locationIcons = {
                "http://maps.google.com/mapfiles/ms/micons/blue-dot.png",
                "http://maps.google.com/mapfiles/ms/micons/yellow-dot.png",
                "http://maps.google.com/mapfiles/ms/micons/red-dot.png"
        };

        int i = 0;
        for (Map.Entry<NetworkType, List<AssetDevice>> entry : networkMap.entrySet()) {
            for (AssetDevice assetDevice : entry.getValue()) {
                addDevice(assetDevice, coordinates, allDevices, locationIcons[i]);
            }
            i = (i + 1) % locationIcons.length;
        }

        calculateMapCenter(coordinates);
        zoom = MapUtils.calculateZoom(MapUtils.calculateMaxDistance(new ImmutablePair<>(mapCenterLat, mapCenterLong), coordinates)) - 1;
        printConnections(allDevices);
    }

    private void addIntermediateLines(DeviceLine deviceLine, List<Pair<Double, Double>> coordinates, Polyline polyline) {
        Pair<Double, Double> firstPoint = MapUtils.calculateFirstPointSquare(coordinates);
        List<AssetDevice> devices = deviceLine.getDevices().stream().sorted(Comparator.comparingDouble(e1 -> MapUtils.calculateDistance(firstPoint, new ImmutablePair<>(e1.getLatitude().doubleValue(), e1.getLongitude().doubleValue())))).collect(Collectors.toList());
        for (AssetDevice assetDevice : devices) {
            try {
                LatLng coordinate = new LatLng(assetDevice.getLatitude().doubleValue(), assetDevice.getLongitude().doubleValue());
                coordinates.add(new ImmutablePair<>(coordinate.getLat(), coordinate.getLng()));
                polyline.getPaths().add(coordinate);
            } catch (Exception e) {

            }
        }
    }

    public void deleteLine(DeviceLine line) {
        deviceLineService.delete(line);
        init();
    }

    public void deleteDeviceFromLine(AssetDevice device, DeviceLine line) {
        line.getDevices().remove(device);
        deviceLineService.save(line);
        init();
    }

    public void addDevice(DeviceLine line) {
        deviceToAdd = 0L;
        selectedLine = line;
    }

    public void newDevice(DeviceLine line) {
        AssetDevice device = assetDeviceService.getById(deviceToAdd);
        if (device != null) {
            line.getDevices().add(device);
            deviceLineService.save(line);
            cancelDeviceFromLine();
            init();
        } else {
            uiMessage.addMessage("Device with id {0} not found", deviceToAdd);
        }
    }

    public void cancelDeviceFromLine() {
        deviceToAdd = null;
        selectedLine = null;
    }

    public void addDeviceLine() {
        newDeviceLine = new DeviceLine();
    }

    public void createDeviceLine() {
        deviceLineService.save(newDeviceLine);
        cancelDeviceLine();
        init();
    }

    public void cancelDeviceLine() {
        newDeviceLine = null;
    }

}