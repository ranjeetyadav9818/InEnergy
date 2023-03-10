package com.inenergis.entity.marketIntegration;

import com.inenergis.entity.IdentifiableEntity;
import com.inenergis.entity.locationRegistration.LocationSubmissionStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.collections.CollectionUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@ToString(of = "name")
@EqualsAndHashCode(of = "name", callSuper = false)
@Entity
@Table(name = "MI_ISO")
public class Iso extends IdentifiableEntity {
    @Column(name = "NAME")
    private String name;

    @Column(name = "ACTIVE")
    private boolean active;

    @Column(name = "BASE_LOCALE")
    private String baseLocale;

    @Column(name = "EXTENDED_LOCALE")
    private String extendedLocale;

    @OneToMany(mappedBy = "iso", fetch = FetchType.LAZY)
    private List<IsoProfile> profiles;

    @OneToMany(mappedBy = "iso", fetch = FetchType.LAZY)
    private List<LocationSubmissionStatus> statuses;

    public IsoProfile getActiveProfile() {
        if (CollectionUtils.isNotEmpty(getProfiles())) {
            for (IsoProfile profile : getProfiles()) {
                Date now = new Date();
                if (now.after(profile.getEffectiveStartDate()) && (profile.getEffectiveEndDate() == null || now.before(profile.getEffectiveEndDate()))) {
                    return profile;
                }
            }
        }
        return null;
    }
}
