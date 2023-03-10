package com.inenergis.entity.locationRegistration;

import com.inenergis.entity.IdentifiableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "LOCATION_CHANGELOG")
public class LocationChangelog extends IdentifiableEntity {

    @Column(name = "CREATION_DATE")
    private Date creationDate;

    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;

    @Column(name = "USER")
    private String userId;

    @Column(name = "CHANGELOG_TYPE")
    @Enumerated(EnumType.STRING)
    private LocationChangelogType type;

    @Column(name = "PROCESSED")
    private boolean processed;

    @ManyToOne
    @JoinColumn(name = "LINKED_CHANGE_ID")
    private LocationChangelog linkedChange;

    @ManyToOne
    @JoinColumn(name = "RESOURCE_ID")
    private IsoResource isoResource;

    @ManyToOne
    @JoinColumn(name = "LOCATION_ID")
    private LocationSubmissionStatus location;

    // Another record points to this one
    @Transient
    private boolean dependant = false;

    @Transient
    private boolean modified = false;

    @Transient
    private boolean hidden = false;

    @Transient
    private boolean highlighted = false;

    @Transient
    private boolean recommended;

    public void toggleModified() {
        modified = !modified;
    }

    public boolean hasLinkedChange() {
        return linkedChange != null;
    }

    public enum LocationChangelogType {
        ADDED, REMOVED, MOVED, UNENROLLED, NONE, MOVING
    }

    public LocationChangelog() {
        type = LocationChangelogType.NONE;
        creationDate = new Date();
    }
}
