package com.msa.scheduler.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.msa.scheduler.app.entity.SchedulerJobInfo;
import com.msa.scheduler.app.service.SchedulerJobService;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class IndexController {

//	private final RestTemplate restTemplate; 	

	@Autowired
	private SchedulerJobService scheduleJobService;
	
	// 나중에 마이크로서비스(scheduler-quartz) 생성후에는 서비스 호출하는 형태로 변경할 것 
	// scheduler job register
	
    @Autowired
    private SchedulerJobService service;
    
	@GetMapping("/index")
	public String index(Model model) {
<<<<<<< HEAD
		
		List<SchedulerJobInfo> jobList = new ArrayList<>();
		/*  		
		SchedulerJobInfo sjInfo = new SchedulerJobInfo();
		sjInfo.setJobId(new Long("1"));
		sjInfo.setJobName("syJob");
		sjInfo.setJobStatus("Start");
		sjInfo.setJobGroup("syJobGroup");
		sjInfo.setCron_Expression("0 0/1 * 1/1 * ? *");
		jobList.add(sjInfo);
		*/

		jobList = service.getAllJobList();
=======
		List<SchedulerJobInfo> jobList = scheduleJobService.getAllJobList();

		//		List<SchedulerJobInfo> jobList = null;
//		SchedulerJobInfo sjInfo = new SchedulerJobInfo();
//		sjInfo.setJobId(new Long("1"));
//		sjInfo.setJobName("syJob");
//		sjInfo.setJobStatus("Start");
//		sjInfo.setJobGroup("syJobGroup");
//		sjInfo.setCronExpression("0 0/1 * 1/1 * ? *");
//		sjInfo.setDescription("bizApplicationDesc");
//		jobList.add(sjInfo);
		
>>>>>>> 815c33c2ce07774a19e3522e335963ea990d1f42
		model.addAttribute("jobs", jobList);
		return "index";
	}
	

    @RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("list");
//		List<Map<String, Object>> jobList = null;
//		restTemplate = new RestTemplate();
//        List<Map<String, Object>> jobList = restTemplate.getForObject("http://localhost:8081/quartz/", List.class);
//        modelAndView.addObject("list", jobList);
        return modelAndView;	
	}
	
	@RequestMapping("/status")
	public String status() {
		return "status";
	}

	
}
