package com.cg.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cg.entity.Role;

public interface RoleRepository extends JpaRepository<Role,Integer>
{
    Optional<Role> findByName(String name);

}
