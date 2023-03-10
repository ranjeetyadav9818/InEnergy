package com.inenergis.controller.asset;

import com.inenergis.controller.general.AssetHelper;
import com.inenergis.entity.asset.Asset;
import com.inenergis.entity.device.AssetDevice;
import com.inenergis.entity.genericEnum.CommodityType;
import com.inenergis.service.AssetService;
import com.inenergis.service.DeviceLinkService;
import com.inenergis.service.DeviceService;
import com.inenergis.service.ParameterEncoderService;
import com.inenergis.util.UIMessage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Named
@ViewScoped
@Getter
@Setter
public class CatalogAndInventoriesImportExportController implements Serializable {

    public static final String THERE_WAS_AN_EXCEPTION_TRYING_TO_PROCESS_THE_FILE_0_PLEASE_CONTACT_YOUR_ADMINISTRATOR_ABOUT_THE_EXPECTED_FORMAT_OF_THE_FILE = "There was an exception trying to process the file {0}, please contact your administrator about the expected format of the file";
    public static final String RECORDS_PROCESSED_FROM_FILE_1 = "{0} records processed from file {1}";
    @Inject
    private AssetHelper assetHelper;

    @Inject
    private AssetService assetService;

    @Inject
    private DeviceService deviceService;

    @Inject
    private DeviceLinkService deviceLinkService;

    @Inject
    private UIMessage uiMessage;

    private List<Asset> assets = new ArrayList<>();

    private Logger log = LoggerFactory.getLogger(CatalogAndInventoriesImportExportController.class);

    private CommodityType commodityType;

    @PostConstruct
    public void init() {
        commodityType = ParameterEncoderService.getCommodityTypeParameter();
    }

    @Transactional
    public void submit(FileUploadEvent event) throws IOException {
        String type = (String) event.getComponent().getAttributes().get("type");
        List<Asset> assets = new ArrayList<>();
        List<AssetDevice> devices = new ArrayList<>();
        UploadedFile file = event.getFile();
        int numLinksProcessed = 0;
        if (file == null) {
            uiMessage.addMessage("file is mandatory", FacesMessage.SEVERITY_ERROR);
        } else {
            String debugLine = StringUtils.EMPTY;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputstream()));
                String firstLine = br.readLine(); //Skip first line
                if (firstLine == null) {
                    throw new IOException("empty file");
                }
                for (String line : br.lines().collect(Collectors.toList())) {
                    debugLine = line;
                    if (StringUtils.isEmpty(line)) {
                        throw new IOException("empty file");
                    }
                    switch (type) {
                        case "assets":
                            assets.add(assetHelper.createAsset(line));
                            break;
                        case "devices":
                            devices.add(assetHelper.createDevice(line));
                            break;
                        case "links":
                            assetHelper.createDeviceLink(line);
                            numLinksProcessed++;
                            break;
                    }
                }
                assetService.saveOrReplace(assets);
                deviceService.saveOrReplace(devices);
                uiMessage.addMessage(RECORDS_PROCESSED_FROM_FILE_1, Math.max(assets.size(), Math.max(devices.size(), numLinksProcessed)), file.getFileName());
            } catch (Exception e) {
                uiMessage.addMessage(THERE_WAS_AN_EXCEPTION_TRYING_TO_PROCESS_THE_FILE_0_PLEASE_CONTACT_YOUR_ADMINISTRATOR_ABOUT_THE_EXPECTED_FORMAT_OF_THE_FILE,
                        FacesMessage.SEVERITY_ERROR, file.getFileName());
                log.error(THERE_WAS_AN_EXCEPTION_TRYING_TO_PROCESS_THE_FILE_0_PLEASE_CONTACT_YOUR_ADMINISTRATOR_ABOUT_THE_EXPECTED_FORMAT_OF_THE_FILE + " " + file.getFileName(), e);
                log.error(debugLine);
                throw e; //To rollback the transaction. Most of all for device links. To delete the previously saved.
            }
        }
    }

    public StreamedContent download(String type) {
        final ByteArrayInputStream stream;
        try {
            switch (type) {
                case "assets":
                    stream = assetHelper.downloadAssets(commodityType);
                    break;
                case "inventories":
                    stream = assetHelper.downloadDevices(commodityType);
                    break;
                case "connections":
                    stream = assetHelper.downloadLinks(commodityType);
                    break;
                default:
                    throw new IllegalArgumentException("Type unknown");
            }
            return new DefaultStreamedContent(stream, "text/plain", type + ".csv");
        } catch (Exception e) {
            log.error(type, e);
            uiMessage.addMessage(MessageFormat.format("Content {0} could not be downloaded.",type), FacesMessage.SEVERITY_ERROR);
            return null;
        }
    }
}