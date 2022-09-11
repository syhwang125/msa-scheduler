package com.msa.quartz.bizapp.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.msa.quartz.bizapp.entity.BizAppStore;
import com.msa.quartz.bizapp.service.BizService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BizController {

	@Autowired
	BizService bizService;
	
	@PostMapping("/bizapps")
	public BizAppStore register(@RequestBody BizAppStore newBiz) {
		return bizService.register(newBiz);
	}
	
	@GetMapping("/bizapps/{id}")
	public Optional<BizAppStore> find(@PathVariable Long appId) {
		return bizService.find(appId);
	}
	
	@GetMapping("/bizapps")
	public List<BizAppStore> findAll() {
		return this.bizService.findAll();
	}
	
	@PutMapping("/bizapps") 
	public void modify (@RequestBody  BizAppStore newBiz) {
		bizService.modify(newBiz);
	}
	
	
}
