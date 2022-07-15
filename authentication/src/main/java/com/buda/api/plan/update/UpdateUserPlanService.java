package com.buda.api.plan.update;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.buda.entities.User;
import com.buda.repository.UserRepository;

@Service
public class UpdateUserPlanService {
    private final UserRepository userRepository;
    @Autowired
    public UpdateUserPlanService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @Transactional
    public void updateBatchUserPlan(List<UpdateUserPlanDTO> updateUserPlanDTOs) {
        List<User> updatedUsers = new ArrayList<User>();
        for (UpdateUserPlanDTO updateUserPlanDTO: updateUserPlanDTOs) {
            User user = this.userRepository.findUserByUserID(updateUserPlanDTO.getUserID()).get();
            if (user != null) {
                user.setPlanType(updateUserPlanDTO.getPlanType());
                updatedUsers.add(user);
            }
        }
        this.userRepository.saveAll(updatedUsers);
    }
    @Transactional
    public User updateUserPlan(UpdateUserPlanDTO updateUserPlanDTO) {
        User user = this.userRepository.findUserByUserID(updateUserPlanDTO.getUserID()).get();
        if (user != null) {
            user.setPlanType(updateUserPlanDTO.getPlanType());
            this.userRepository.save(user);
            return user;
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User not found");
    }
}
