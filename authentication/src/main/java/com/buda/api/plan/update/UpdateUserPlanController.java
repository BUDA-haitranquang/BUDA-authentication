package com.buda.api.plan.update;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("*")
@RequestMapping("api/plan/update")
public class UpdateUserPlanController {
    private final UpdateUserPlanService updateUserPlanService;
    @Autowired
    public UpdateUserPlanController(UpdateUserPlanService updateUserPlanService) {
        this.updateUserPlanService = updateUserPlanService;
    }
    @PostMapping("/batch")
    public ResponseEntity<?> updateBatchUserPlan(@RequestBody List<UpdateUserPlanDTO> updateUserPlanDTOs) {
        this.updateUserPlanService.updateBatchUserPlan(updateUserPlanDTOs);
        return ResponseEntity.ok().body("OK");
    }
    @PostMapping
    public ResponseEntity<?> updateUserPlan(@RequestBody UpdateUserPlanDTO updateUserPlanDTO) {
        this.updateUserPlanService.updateUserPlan(updateUserPlanDTO);
        return ResponseEntity.ok().body("OK");
    }
}
