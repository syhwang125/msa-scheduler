package com.msa.quartz.bizapp.entity;

@Entity
@Table( name = "BizAppStore")
@Getter
@Setter
public class BizAppStore {

	private String appId;
	private String name;
	private String desc;
	private String status;
	
}
