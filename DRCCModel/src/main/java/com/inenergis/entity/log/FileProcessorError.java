package com.inenergis.entity.log;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Getter
@Setter
@Table(name = "FILE_PROCESSOR_ERRORS")
public class FileProcessorError extends IdentifiableEntity {


    @Column(name = "ROWDATA", length = 65535)
    byte[] rowData;

    @Column(name = "ERROR")
    String error;

    @Column(name = "EXCEPTION_DETAIL",length = 65535)
    byte[] exceptionDetail;

    @Column(name = "RESOLVED")
    Boolean resolved;

    @ManyToOne
    @JoinColumn(name = "FILE_PROCESSOR_ID")
    FileProcessorLog fileProcessorLog;

}
