package com.inenergis.billingEngine.sa;

import com.inenergis.entity.program.RatePlanProfile;
import org.springframework.data.repository.Repository;

public interface RatePlanProfileDao extends Repository<RatePlanProfile, String> {

    RatePlanProfile save(RatePlanProfile ratePlanProfile);
}
