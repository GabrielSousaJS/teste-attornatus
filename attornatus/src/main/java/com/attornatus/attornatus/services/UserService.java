package com.attornatus.attornatus.services;

import com.attornatus.attornatus.dto.*;
import com.attornatus.attornatus.entities.Address;
import com.attornatus.attornatus.entities.Role;
import com.attornatus.attornatus.entities.User;
import com.attornatus.attornatus.repositories.RoleRepository;
import com.attornatus.attornatus.repositories.UserRepository;
import com.attornatus.attornatus.services.exceptions.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private static Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository repository;

    @Autowired
    private RoleRepository roleRepository;

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional(readOnly = true)
    public Page<UserMinDTO> findAllPersonPaged(Pageable pageable) {
        Page<User> page = repository.findAll(pageable);
        return page.map(UserMinDTO::new);
    }

    @Transactional(readOnly = true)
    public UserMinDTO findByIdPerson(Long id) {
        authService.validateSelfOfAdmin(id);
        Optional<User> obj = repository.findById(id);
        User user = obj.orElseThrow(() -> new ResourceNotFoundException("User ID " + id + " not found"));
        return new UserMinDTO(user);
    }

    @Transactional(readOnly = true)
    public List<AddressMinDTO> findAddressOfPerson(Long id) {
        authService.validateSelfOfAdmin(id);

        List<Address> addressList = repository.findAddressOfPerson(id);

        if (addressList.isEmpty()) {
            throw new ResourceNotFoundException("User with ID " + id + " has no address");
        }

        return addressList.stream().map(AddressMinDTO::new).collect(Collectors.toList());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public UserMinDTO insertPerson(UserInsertDTO dto) {
        User entity = new User();
        copyDtoToEntity(entity, dto);
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity = repository.save(entity);
        return new UserMinDTO(entity);
    }

    @Transactional
    public UserMinDTO updatePerson(Long id, UserUpdateDTO dto) {
        try {
            authService.validateSelfOfAdmin(id);
            User entity = repository.getReferenceById(id);
            copyDtoToEntity(entity, dto);
            entity = repository.save(entity);
            return new UserMinDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("User id " + id + " not found to update data.");
        }
    }

    private void copyDtoToEntity(User entity, UserDTO dto) {
        entity.setName(dto.getName());
        entity.setBirthDate(dto.getBirthDate());

        entity.getRoles().clear();
        for (RoleDTO roleDto : dto.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDto.getId());
            entity.getRoles().add(role);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = repository.findByName(username);

        if (user == null) {
            logger.error("Name not found " + username);
            throw new UsernameNotFoundException("Name not found");
        } else {
            logger.info("Name found " + username);
            return user;
        }
    }
}
