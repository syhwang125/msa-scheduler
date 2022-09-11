package com.msa.quartz.bizapp.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.msa.quartz.bizapp.entity.BizAppStore;
import com.msa.quartz.bizapp.reposiotry.BizRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class BizService {

	@Autowired
	private final BizRepository bizRepo;
	
	
	public BizAppStore register(BizAppStore newBiz) {
		log.info(">>>>> AppName = [" + newBiz.getName() + "]" + " saved.");
		return this.bizRepo.save(newBiz);
	}
	
	public void modify(BizAppStore newBiz) {
//		Optional<BizAppStore> bizApp = this.bizRepo.findById(newBiz.getAppId());
//       this.bizRepo.modify(newBiz);
	}
	
	public void remove(Long appId) {
		this.bizRepo.deleteById(appId);
	}

//	public Optional<BizAppStore> find(Long appId) {		
//		return bizRepo.findById(appId);
//				                .map( this::convert ) 
//				                .orElseThrow( () -> new EntityNotFoundException( id ) );
//	}

	public List<BizAppStore> findAll() {
		return this.bizRepo.findAll();
	}

	public Optional<BizAppStore> find(Long appId) {
		return bizRepo.findById(appId);
	}

}
