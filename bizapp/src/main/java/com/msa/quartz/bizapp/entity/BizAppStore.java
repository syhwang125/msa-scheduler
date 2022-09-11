package com.msa.quartz.bizapp.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table( name = "BizAppStore")
@Getter
@NoArgsConstructor
public class BizAppStore  {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="APPID", nullable=false)
	long appId;
	
//	private String appId;	
	
	@Column(name = "NAME", length=100, nullable = false)
	private String name;
	
	@Column(name = "DESCRIPTION", length=50, nullable = true)
	private String desc;
	
	@Column(name = "STATUS", length=100, nullable = true)
	private String status;   // start, stop, pause
	
	
	/*
	public BizAppStore retrieve(String appId2) {
		// TODO Auto-generated method stub
		return null;
	}
	public List<BizAppStore> retrieveAll() {
		// TODO Auto-generated method stub
		return null;
	}
	public Object update(BizAppStore newBiz) {
		// TODO Auto-generated method stub
		return null;
	}
	public String create(BizAppStore newBiz) {
		// TODO Auto-generated method stub
		return null;
	}
	public void delete(String appId2) {
		// TODO Auto-generated method stub
		
	}
	*/
}