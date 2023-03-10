package com.caiso.soa.service;

import com.caiso.soa._2006_06_13.standardattachmentinfor.CompressFlag;
import com.caiso.soa._2006_06_13.standardattachmentinfor.StandardAttachmentInfor;
import com.caiso.soa.batchvalidationstatus_v1.BatchValidationStatus;
import com.caiso.soa.batchvalidationstatus_v1.ObjectFactory;
import com.caiso.soa.custom.attachment.AttachmentHash;
import com.caiso.soa.custom.security.CAISOUsernameToken;
import com.caiso.soa.custom.security.CAISOWSHeader;
import com.caiso.soa.drregistrationdata_v1.DRRegistrationData;
import org.apache.cxf.attachment.AttachmentUtil;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.cxf.helpers.IOUtils;
import org.apache.cxf.jaxb.JAXBDataBinding;
import org.apache.cxf.message.Attachment;
import org.apache.cxf.message.Message;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;


public class CaisoNetworkHelper {



    public static DRRegistrationData getDRResponse(Client client) throws JAXBException, IOException, NoSuchAlgorithmException {
        Collection<Attachment> attachments = (Collection<Attachment>) client.getResponseContext().get(Message.ATTACHMENTS);
        for (Attachment attachment : attachments) {
            InputStream is = extracter(attachment.getDataHandler().getInputStream());
            JAXBContext jaxbContext = JAXBContext.newInstance(com.caiso.soa.drregistrationdata_v1.ObjectFactory.class);
            DRRegistrationData response = ((JAXBElement<DRRegistrationData>) jaxbContext.createUnmarshaller().unmarshal(is)).getValue();
            return response;
        }
        return null;
    }

    public static BatchValidationStatus getBatchStatusResponse(Client client) throws JAXBException, IOException, NoSuchAlgorithmException {
        Collection<Attachment> attachments = (Collection<Attachment>) client.getResponseContext().get(Message.ATTACHMENTS);
        for (Attachment attachment : attachments) {
            InputStream is = extracter(attachment.getDataHandler().getInputStream());
            JAXBContext jaxbContext = JAXBContext.newInstance(ObjectFactory.class);
            BatchValidationStatus response = ((JAXBElement<BatchValidationStatus>) jaxbContext.createUnmarshaller().unmarshal(is)).getValue();
            return response;
        }
        return null;
    }

    public static void addAttachment(Client client, String xml, String namespace) throws JAXBException, IOException, NoSuchAlgorithmException {
        Collection<Attachment> attachments = new ArrayList<>();
        Attachment attachmentImpl = AttachmentUtil.createMtomAttachment(true, null, namespace, xml.getBytes(), 0, xml.getBytes().length, 0);
        attachments.add(attachmentImpl);
        client.getRequestContext().put(Message.ATTACHMENTS,attachments);
    }
    public static void addAttachmentHash(Client client, String xml) throws JAXBException, IOException, NoSuchAlgorithmException {
        List<Header> headersList = ((List<Header>) client.getRequestContext().get(Header.HEADER_LIST));
        if (headersList == null) {
            headersList = new ArrayList<>();
        }
        AttachmentHash attachmentHash = new AttachmentHash();
        attachmentHash.setHashValue(calculateAttachment(xml));
        Header attachmentHashHeader = new Header(new QName("http://www.caiso.com/mrtu/soa/schemas/2005/09/attachmenthash","attachmentHash"),attachmentHash,new JAXBDataBinding(AttachmentHash.class));

        headersList.add(attachmentHashHeader);
        client.getRequestContext().put(Header.HEADER_LIST, headersList);
    }
    public static void addAttachmentInformation(Client client) throws JAXBException {
        List<Header> headersList = ((List<Header>) client.getRequestContext().get(Header.HEADER_LIST));
        if (headersList == null) {
            headersList = new ArrayList<>();
        }
        StandardAttachmentInfor attachmentInfor = new StandardAttachmentInfor();
        com.caiso.soa._2006_06_13.standardattachmentinfor.Attachment attachment = new com.caiso.soa._2006_06_13.standardattachmentinfor.Attachment();
        attachment.setId("1");
        attachment.setCompressFlag(CompressFlag.YES);
        attachment.setCompressMethod("gzip");
        attachmentInfor.getAttachment().add(attachment);
        Header attachmentHeader = new Header(new QName("http://www.caiso.com/soa/2006-06-13/StandardAttachmentInfor.xsd","standardAttachmentInfor"),attachmentInfor,new JAXBDataBinding(StandardAttachmentInfor.class));

        headersList.add(attachmentHeader);
        client.getRequestContext().put(Header.HEADER_LIST, headersList);
    }
    public static void addCaisoSecurityHeader(Client client, String username) throws JAXBException {

        List<Header> headersList = ((List<Header>) client.getRequestContext().get(Header.HEADER_LIST));
        if (headersList == null) {
            headersList = new ArrayList<>();
        }
        CAISOWSHeader caisowsHeader = new CAISOWSHeader();
        CAISOUsernameToken caisoUsernameToken = new CAISOUsernameToken();
        caisoUsernameToken.setNonce(""+new Date().getTime()+"000");
        caisoUsernameToken.setUsername(username);
        caisoUsernameToken.setCreated(new Date());
        caisowsHeader.setCaisoUsernameToken(caisoUsernameToken);
        Header testSoapHeader1 = new Header(new QName("http://www.caiso.com/soa/2006-09-30/CAISOWSHeader.xsd", "CAISOWSHeader"), caisowsHeader, new JAXBDataBinding(CAISOWSHeader.class));

        headersList.add(testSoapHeader1);

        // Add SOAP headers to the web service request
        client.getRequestContext().put(Header.HEADER_LIST, headersList);
    }

    private static String calculateAttachment(String encBytes) throws IOException, NoSuchAlgorithmException {
        MessageDigest msgDigest = MessageDigest.getInstance("SHA1");
        byte[] mdbytes = msgDigest.digest(encBytes.getBytes());

        //Below attachmentHash has to be passed to WS Security Header in Caiso Request
        String attachmentHash =  new String(Base64.getEncoder().encode(mdbytes));
        return attachmentHash;
    }

    private static InputStream extracter(InputStream is) throws IOException {
        String src = new BufferedReader(new InputStreamReader(is)).lines().collect(Collectors.joining("\n"));
        return new GZIPInputStream(new ByteArrayInputStream(Base64.getDecoder().decode(src.replace("\r","").replace("\n",""))));
    }

    public static InputStream getADSBatchDetails(String s) throws IOException {
        return extracter(new ByteArrayInputStream(s.getBytes()));
    }

    public static void main(String ... args) throws IOException {
        String s = "H4sIAAAAAAAAAI1U0W7iMBD8lSj3DHaAXjlkXFECXKXSixJ6r8jEe8FSYiPbCeXv6wSCKKXVSVEU\n" +
                "r2dnd8eekIe3Ivcq0EYoOfaDLvY9kKniQmZj/3U17wx9z1gmOcuVhLF/AOM/UBLGMWTCWM2sSwyZ\n" +
                "ZZ4jkmbsb63djRDa7/fdlAmjuqkqkFEMfU5ZV0H3zfAfPiVLMIZl8BsYB03JShTgEEB7OLjv4H6n\n" +
                "11/hu1GAR4Nf3UEw7OD7EcYEnXEkUaVOgboiCUGnBfl7nItWjuYnvsMBQW2IoKuSp2XEDrli3E0I\n" +
                "hZs6BrNT0sBl6+t5meeUFPFTSINBr1fTNgsiWQE0WiSP62Adu1Zwz+t4/SFBzcYVZaRVJZrSTXIY\n" +
                "LWYtD/oKGdZNiE1pgU9SKyphD5SwtO6KvkwIOn0SU24KYVsUzCSvVarVqhUdNooOVhiPmqeLMW4V\n" +
                "/SbzmjWxTNtL3vv/5/2Y6wa+NdhRdNDAFyDBia+cBM/udCZZpiFrDiNSQtpWwudJtI4Wk9k6mi56\n" +
                "rZgtGHgkFYcTNjlik8ez5p9w6HapOpqArpxDZtI2jTYMz5cHeAOUpFvgZe4iU6W0M9hxnibhsl/0\n" +
                "BRDdlMN505aGkorlJdDpn5f5U7ychQQdA074E+B4pV6tyF03Z7ndWFNV7Jhsp3j9fA2/y9kwA65T\n" +
                "WILdKk4D7Anpufde2K2XLN2VvEKQnVaZZgWNXZRtGmYvjAlq40SDaey7OuycmTR4Ifxz+ZygDzvX\n" +
                "JrnhUHTt6Rv/IPoOpD/VawIFAAA=";
        InputStream extracter = extracter(new ByteArrayInputStream(s.getBytes()));
        String s1 = IOUtils.toString(extracter);
        System.out.println(s1);
    }
}
