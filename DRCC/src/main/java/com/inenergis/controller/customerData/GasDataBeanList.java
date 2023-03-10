package com.inenergis.controller.customerData;

import java.util.ArrayList;
import java.util.List;

public class GasDataBeanList {
	
	private String name;
	private String icon;
	private List<GasDataBean> dataBeans = new ArrayList<GasDataBean>();
	
	public GasDataBeanList(){
	}
	
	public GasDataBeanList(String name, String icon){
		this.setName(name);
		this.setIcon(icon);
	}
	
	public GasDataBeanList(String name, String icon, List<GasDataBean> dataBeans){
		this.setName(name);
		this.setIcon(icon);
		this.setDataBeans(dataBeans);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<GasDataBean> getDataBeans() {
		return dataBeans;
	}

	public void setDataBeans(List<GasDataBean> dataBeans) {
		this.dataBeans = dataBeans;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	
}
