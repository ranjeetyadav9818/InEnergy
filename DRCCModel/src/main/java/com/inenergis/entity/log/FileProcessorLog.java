package com.inenergis.entity.log;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "FILE_PROCESSOR_LOG")
public class FileProcessorLog extends IdentifiableEntity {

    @Column(name = "PROCESS_DATE")
    Date processDate;

    @Column(name = "FILENAME")
    String filename;

    @Column(name = "TIME_COMPLETED")
    Date timeCompleted;


    @Column(name = "FILETYPE")
    String fileType = "CDW";

//    @Column(name = "ERRORS")
//    Boolean hasErrors;

    @OneToMany(mappedBy = "fileProcessorLog", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    List<FileProcessorError> fileProcessorErrors;

    @Transient
    public int fileProcessorPendingErrors(){
        int count=0;
        if (fileProcessorErrors != null) {
            for (FileProcessorError fileProcessorError : fileProcessorErrors) {
                if (!fileProcessorError.getResolved()){
                    count++;
                }
            }
        }
        return count;
    }
}
