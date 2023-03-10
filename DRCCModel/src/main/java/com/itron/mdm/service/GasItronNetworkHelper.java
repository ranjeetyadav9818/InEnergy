package com.itron.mdm.service;

import com.itron.mdm.custom.security.GasItronSecurityHeader;
import com.itron.mdm.custom.security.GasItronUsernameToken;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GasItronNetworkHelper {

    public static void addItronSecurityHeader(Client client, Properties properties) throws JAXBException {
        List<Header> headersList = ((List<Header>) client.getRequestContext().get(Header.HEADER_LIST));
        if (headersList == null) {
            headersList = new ArrayList<>();
        }

        GasItronUsernameToken gasItronUsernameToken = new GasItronUsernameToken();
        gasItronUsernameToken.setUsername(properties.getProperty("itron.api.username"));
        gasItronUsernameToken.setPassword(properties.getProperty("itron.api.password"));

        GasItronSecurityHeader gasItronSecurityHeader = new GasItronSecurityHeader();
        gasItronSecurityHeader.setGasItronUsernameToken(gasItronUsernameToken);

        Header header = new Header(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse"), gasItronSecurityHeader,
                new JAXBDataBinding(GasItronSecurityHeader.class));

        headersList.add(header);

        client.getRequestContext().put(Header.HEADER_LIST, headersList);
    }
}