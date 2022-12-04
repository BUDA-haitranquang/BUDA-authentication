package com.buda.api.login.staff;

import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import com.buda.api.login.JwtResponseDTO;
import com.buda.builder.JwtTokenBuilder;
import com.buda.entities.Staff;
import com.buda.entities.User;
import com.buda.repository.StaffRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.server.ResponseStatusException;

@Service
public class StaffLoginService implements UserDetailsService{
    private final StaffRepository staffRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public StaffLoginService(StaffRepository staffRepository)
    {
        this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.staffRepository = staffRepository;
    }
    public JwtResponseDTO correctLogin(String account, String rawPassword)
    {
        Optional<Staff> staff = this.staffRepository.findStaffByAccount(account);
        if ((staff.isPresent())
        &&(this.bCryptPasswordEncoder.matches(rawPassword, staff.get().getPassword())))
        {
            if (!(staff.get().getEnabled().equals(Boolean.TRUE))){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "This staff account has been disabled");
            }
            JwtTokenBuilder jwtTokenUtil = new JwtTokenBuilder();

            // return token 
            Map<String, Object> claims = new HashMap<String, Object>();
            claims.put("staffID", staff.get().getStaffID());
            claims.put("roles", staff.get().getRoles());
            String jwtaccessToken = jwtTokenUtil.generateStaffAccessToken(staff);
            String jwtrefreshToken = jwtTokenUtil.generateStaffRefreshToken(staff);
        
            return new JwtResponseDTO(jwtaccessToken, jwtrefreshToken);
        }
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "false");
    }
    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        Optional<Staff> staff = this.staffRepository.findStaffByAccount(account);

        if(!staff.isPresent()){
            throw new UsernameNotFoundException("Not found staff with uuid: " + account);
        }
        
        staff.get().getRoles().forEach(role -> 
            {
                authorities.add(new SimpleGrantedAuthority(role.getName()));
            }
        );
        return new org.springframework.security.core.userdetails.User(staff.get().getAccount(), 
                                                                      staff.get().getPassword(), authorities);
    }
}
