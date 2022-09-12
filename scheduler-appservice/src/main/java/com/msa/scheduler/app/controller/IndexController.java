package com.msa.scheduler.app.controller;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.msa.scheduler.app.entity.SchedulerJobInfo;

import lombok.AllArgsConstructor;

@Controller
@AllArgsConstructor
public class IndexController {

//	private final RestTemplate restTemplate; 	

	// 나중에 마이크로서비스(scheduler-quartz) 생성후에는 서비스 호출하는 형태로 변경할 것 
	@GetMapping("/index")
	public String index(Model model) {
		
		
		List<SchedulerJobInfo> jobList = null;
		SchedulerJobInfo sjInfo = new SchedulerJobInfo();
		sjInfo.setJobId(new Long("1"));
		sjInfo.setJobName("syJob");
		sjInfo.setJobStatus("Start");
		sjInfo.setJobGroup("syJobGroup");
		sjInfo.setCronExpression("0 0/1 * 1/1 * ? *");
		jobList.add(sjInfo);
		
		model.addAttribute("jobs", jobList);
		return "index";
	}
	
/*	
    @RequestMapping("/list")
	public ModelAndView list() {
		ModelAndView modelAndView = new ModelAndView("list");
		List<Map<String, Object>> jobList = null;
//        List<Map<String, Object>> jobList = restTemplate.getForObject("http://localhost:18080/quartz/", List.class);
        modelAndView.addObject("list", jobList);
        return modelAndView;	
	}
	
	@RequestMapping("/status")
	public String status() {
		return "status";
	}
*/
	
}
