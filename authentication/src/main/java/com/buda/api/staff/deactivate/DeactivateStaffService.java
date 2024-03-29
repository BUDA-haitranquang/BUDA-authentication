package com.buda.api.staff.deactivate;

import java.util.Optional;

import javax.transaction.Transactional;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buda.entities.Staff;
import com.buda.repository.StaffRepository;

@Service
public class DeactivateStaffService {
    private final StaffRepository staffRepository;
    @Autowired
    public DeactivateStaffService(StaffRepository staffRepository){
        this.staffRepository = staffRepository;
    }
    @Transactional
    public Staff deactivateStaffByStaffID(Long userID, Long staffID){
        Optional<Staff> staffOptional = this.staffRepository.findStaffByStaffID(staffID);
        if ((staffOptional.get().getUserID().equals(userID))){
            Staff staff = staffOptional.get();
            staff.setEnabled(Boolean.FALSE);
            staff.setPassword("undefined");
            this.staffRepository.save(staff);
            return staff;
        }
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Staff not found");
    }
}
