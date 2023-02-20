package com.attornatus.attornatus.services;

import com.attornatus.attornatus.dto.AddressDTO;
import com.attornatus.attornatus.entities.Address;
import com.attornatus.attornatus.entities.User;
import com.attornatus.attornatus.repositories.AddressRepository;
import com.attornatus.attornatus.repositories.UserRepository;
import com.attornatus.attornatus.services.exceptions.AddressDoesNotBelongUser;
import com.attornatus.attornatus.services.exceptions.ResourceNotFoundException;
import com.attornatus.attornatus.services.exceptions.TwoMainAddressException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;

@Service
public class AddressService {

    @Autowired
    private AddressRepository repository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public AddressDTO insertAddressOfPerson(Long id, AddressDTO dto) {
        try {
            authService.validateSelfOfAdmin(id);

            User user = userRepository.getReferenceById(id);

            if (checkMainAddress(user, dto)) {
                throw new TwoMainAddressException("User with ID " + id + " already has registered main address");
            }

            Address entity = new Address();
            copyDtoToEntity(entity, dto, user);
            entity = repository.save(entity);
            return new AddressDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("User with ID " + id + " not found.");
        }
    }

    @Transactional
    public AddressDTO informMainAddress(Long addressId, AddressDTO dto) {
        authService.validateSelfOfAdmin(dto.getUserId());

        User user = userRepository.getReferenceById(dto.getUserId());

        if (checkMainAddress(user, dto)) {
            throw new TwoMainAddressException("User with ID " + dto.getUserId() + " already has registered main address");
        }

        Address entity = repository.getReferenceById(addressId);

        if (!checkAddressUser(dto.getUserId(), entity.getUser().getId())) {
            throw new AddressDoesNotBelongUser("Address with ID " + addressId + " does not belong to user " + user.getUsername());
        }

        copyDtoToEntity(entity, dto, user);
        entity = repository.save(entity);
        return new AddressDTO(entity);
    }

    private void copyDtoToEntity(Address entity, AddressDTO dto, User user) {
        entity.setPublicPlace(dto.getPublicPlace());
        entity.setCep(dto.getCep());
        entity.setNumber(dto.getNumber());
        entity.setCity(dto.getCity());
        entity.setMainAddress(dto.getMainAddress());
        entity.setUser(user);
    }

    private boolean checkMainAddress(User user, AddressDTO dto) {
        return user.countTotalAddress() == 1 && dto.getMainAddress().equals(true) ? true : false;
    }

    private boolean checkAddressUser(Long userId, Long addressUserId) {
        return userId == addressUserId;
    }
}