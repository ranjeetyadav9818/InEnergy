package com.inenergis.controller.asset;

import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.device.DeviceLine;
import com.inenergis.entity.device.DeviceLink;
import com.inenergis.service.AssetDeviceService;
import com.inenergis.service.DeviceLineService;
import com.inenergis.util.MapUtils;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;
import org.primefaces.model.map.Polyline;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class CommodityMapController implements Serializable {

    protected MapModel mapModel;
    protected Marker marker;
    protected int zoom = 7;
    protected double mapCenterLat = 0.0;
    protected double mapCenterLong = 0.0;

    @Inject
    AssetDeviceService assetDeviceService;
    @Inject
    UIMessage uiMessage;
    @Inject
    DeviceLineService deviceLineService;

    protected void calculateMapCenter(List<Pair<Double, Double>> coordinates) {
        Pair<Double, Double> pair = MapUtils.calculateMapCenter(coordinates);
        mapCenterLat = pair.getLeft();
        mapCenterLong = pair.getRight();
    }

    protected void addDevice(AssetDevice assetDevice, List<Pair<Double, Double>> coordinates, List<AssetDevice> allDevices, String icon) {
        try {
            Marker marker = new Marker(new LatLng(assetDevice.getLatitude().doubleValue(),assetDevice.getLongitude().doubleValue()), assetDevice.getName(), assetDevice, icon);
            mapModel.addOverlay(marker);
            coordinates.add(new ImmutablePair<>(assetDevice.getLatitude().doubleValue(), assetDevice.getLongitude().doubleValue()));
            allDevices.add(assetDevice);
        } catch (Exception e){}
    }

    public void onOverlaySelect(OverlaySelectEvent event) {
        if(event.getOverlay() instanceof  Marker){
            marker = (Marker) event.getOverlay();
        } else {
            marker = null;
        }
        if(event.getOverlay() instanceof Polyline) {
            Object data = event.getOverlay().getData();
            if (data instanceof DeviceLink){
                DeviceLink link = ((DeviceLink) data);
                uiMessage.addMessage("Connection {0} ({1}), From ({2}) to ({3}). Attributes: {4}", link.getName(), link .getType().getName(), link.commaSeparatedSources(), link.commaSeparatedTargets(), link.commaSeparatedAttributes());
            } else if (data instanceof DeviceLine){
                Polyline overlay = (Polyline) event.getOverlay();
                uiMessage.addMessage("Pipeline {0}", ((DeviceLine) overlay.getData()).getName());
            }
        }
    }

    protected void printConnections(List<AssetDevice> allDevices) {
        for (AssetDevice device : allDevices) {
            if (device.getLongitude() == null || device.getLatitude() == null) {
                continue;
            }
            for (DeviceLink deviceLink : device.getSourceLinks()) {
                for (AssetDevice targetDevice : deviceLink.getTargets()) {
                    if (targetDevice.getLatitude() != null && targetDevice.getLongitude() != null) {
                        Polyline polyline = generatePolyline(deviceLink);

                        LatLng coord = new LatLng(device.getLatitude().doubleValue(), device.getLongitude().doubleValue());
                        polyline.getPaths().add(coord);
                        coord = new LatLng(targetDevice.getLatitude().doubleValue(), targetDevice.getLongitude().doubleValue());
                        polyline.getPaths().add(coord);
                        mapModel.addOverlay(polyline);
                    }
                }
            }
        }
    }

    protected Polyline generatePolyline(DeviceLink deviceLink) {
        Polyline polyline = new Polyline();
        polyline.setStrokeWeight(3);
        polyline.setData(deviceLink);
        switch (deviceLink.getType()) {
            case IN:
                polyline.setStrokeColor("#EE6666");
                polyline.setStrokeOpacity(0.6);
                break;
            case OUT:
                polyline.setStrokeColor("#6666EE");
                polyline.setStrokeOpacity(0.6);
                break;
            case BIDIRECTIONAL:
                polyline.setStrokeColor("#EE82EE");
                polyline.setStrokeOpacity(0.8);
                break;
        }
        return polyline;
    }

    protected Polyline generatePolyline(DeviceLine deviceLine) {
        Polyline polyline = new Polyline();
        polyline.setStrokeWeight(5);
        polyline.setData(deviceLine);
        polyline.setStrokeColor("#C0C0C0");
        polyline.setStrokeOpacity(0.95);
        return polyline;
    }

}