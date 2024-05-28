package pmf.awp.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pmf.awp.project.exception.NoSuchElementException;
import pmf.awp.project.model.Role;
import pmf.awp.project.repository.RoleRepository;

@Service
public class RoleService {
    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name) throws RuntimeException {
        return roleRepository.findByName(name).orElseThrow(() -> new NoSuchElementException("Role not found."));
    }
}
