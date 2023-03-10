package com.inenergis.controller.customerData;

import java.util.ArrayList;
import java.util.List;

public class DataBeanList {
	
	private String name;
	private String icon;
	private List<DataBean> dataBeans = new ArrayList<DataBean>();
	
	public DataBeanList(){
	}
	
	public DataBeanList(String name, String icon){
		this.setName(name);
		this.setIcon(icon);
	}
	
	public DataBeanList(String name, String icon, List<DataBean> dataBeans){
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

	public List<DataBean> getDataBeans() {
		return dataBeans;
	}

	public void setDataBeans(List<DataBean> dataBeans) {
		this.dataBeans = dataBeans;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	
}
