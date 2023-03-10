package com.inenergis.controller.general;


import com.inenergis.entity.Document;
import com.inenergis.entity.IdentifiableEntity;
import org.picketlink.Identity;
import org.picketlink.idm.model.basic.User;
import org.primefaces.model.UploadedFile;

import javax.ejb.Stateless;
import java.util.Date;

@Stateless
public class DocumentHelper {
    public Document generateDocument(UploadedFile file, Identity identity, IdentifiableEntity entity) {
        Document document = new Document();
        document.setAuthor(((User) identity.getAccount()).getEmail());
        document.setCreationDate(new Date());
        document.setEntity(entity.getClass().getName());
        document.setEntityId(entity.getId().toString());
        document.setFileName(file.getFileName());
        document.setContentType(file.getContentType());
        return document;
    }
}
