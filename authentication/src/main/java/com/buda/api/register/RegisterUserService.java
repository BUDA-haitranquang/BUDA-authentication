package com.buda.api.register;

import java.util.Optional;

import com.buda.entities.User;
import com.buda.entities.enumeration.PlanType;
import com.buda.repository.RoleRepository;
import com.buda.repository.UserRepository;

import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
@Service
public class RegisterUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Autowired
    private SendConfirmRegisterMailService confirmRegisterMailService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public RegisterUserService(UserRepository userRepository,
    RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
    }
    @Transactional
    public void registerNewUser(UserRegister userRegister) {
        String email = userRegister.getEmail();
        String phoneNumber = userRegister.getPhoneNumber();
        String username = userRegister.getUsername();

        Optional<User> mailUser = userRepository.findUserByEmail(email);
        if ((email!=null) && (mailUser.isPresent()))
        {
            //BAD REQUEST da ton tai email
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already exists email");
        }
        if (!EmailValidator.getInstance().isValid(email) || email.length() > 60)
        {
            //khong phai email
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email");
        }
        Optional<User> phoneUser = userRepository.findUserByPhoneNumber(phoneNumber);
        if ((phoneNumber!=null) && (phoneUser.isPresent()))
        {
            //BAD REQUEST da ton tai phone
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already exists phoneNumber");
        }
        if (!phoneNumber.matches("[0-9]+"))
        {
            //khong phai phone
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid phone");
        }
        Optional<User> userNameUser = userRepository.findUserByUserName(username);
        if ((username!=null) && (userNameUser.isPresent()))
        {
            //BAD REQUEST da ton tai username
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Already exists userName");
        }
        if (userRegister.getPassword().length() < 8)
        {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Weak password");
        }

        User newUser = new User(userRegister);
        newUser.setPassword(bCryptPasswordEncoder.encode(newUser.getPassword()));
        newUser.addRole(roleRepository.findRoleByName("USER").get());
        newUser.setPlanType(PlanType.BASIC);
        userRepository.save(newUser);
        confirmRegisterMailService.sendMailConfirmationTo(email);
    }
    
}
