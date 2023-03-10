package com.itron.mdm.service;

import com.itron.mdm.custom.security.ItronSecurityHeader;
import com.itron.mdm.custom.security.ItronUsernameToken;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.headers.Header;
import org.apache.cxf.jaxb.JAXBDataBinding;

import javax.xml.bind.JAXBException;
import javax.xml.namespace.QName;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ItronNetworkHelper {

    public static void addItronSecurityHeader(Client client, Properties properties) throws JAXBException {
        List<Header> headersList = ((List<Header>) client.getRequestContext().get(Header.HEADER_LIST));
        if (headersList == null) {
            headersList = new ArrayList<>();
        }

        ItronUsernameToken itronUsernameToken = new ItronUsernameToken();
        itronUsernameToken.setUsername(properties.getProperty("itron.api.username"));
        itronUsernameToken.setPassword(properties.getProperty("itron.api.password"));

        ItronSecurityHeader itronSecurityHeader = new ItronSecurityHeader();
        itronSecurityHeader.setItronUsernameToken(itronUsernameToken);

        Header header = new Header(new QName("http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd", "Security", "wsse"), itronSecurityHeader,
                new JAXBDataBinding(ItronSecurityHeader.class));

        headersList.add(header);

        client.getRequestContext().put(Header.HEADER_LIST, headersList);
    }
}