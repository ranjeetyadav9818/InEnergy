package com.inenergis.controller.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Named;

import com.inenergis.entity.PdpSrParticipant.SuccessfulNotificationType;


@Named
public class SuccessfulNotificationTypeHelper {

	public List<SuccessfulNotificationType> getList() {
		return Arrays.asList(SuccessfulNotificationType.values());
	}
	
	public List<SuccessfulNotificationType> getManualNotificationList() {
		List<SuccessfulNotificationType> pst = new ArrayList<SuccessfulNotificationType>();
		pst.add(SuccessfulNotificationType.ATTEMPTED);
		pst.add(SuccessfulNotificationType.DELIVERED);
		return pst;
	}
	
}
