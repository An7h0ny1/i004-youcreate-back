package com.igrowker.miniproject.User.Repository;

import com.igrowker.miniproject.User.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoleRepository extends JpaRepository<Role, Long> {
   List<Role> findRoleEntitiesByEnumRoleIn(List<String> roles);
}
