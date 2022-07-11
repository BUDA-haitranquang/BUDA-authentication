package com.buda.api.plan.update;

import com.buda.entities.enumeration.PlanType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserPlanDTO {
    private Long userID;
    private PlanType planType;
}
