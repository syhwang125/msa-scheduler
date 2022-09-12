package com.msa.biz.controller;

import java.util.List;
import java.util.Optional;


import com.msa.biz.entity.BizAppStore;
import com.msa.biz.service.BizService;


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
