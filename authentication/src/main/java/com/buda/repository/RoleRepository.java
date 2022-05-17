package com.buda.repository;

import java.util.Optional;

import com.buda.entities.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
    Optional<Role> findRoleByRoleID(Long RoleID);
    Optional<Role> findRoleByName(String name); 
}
