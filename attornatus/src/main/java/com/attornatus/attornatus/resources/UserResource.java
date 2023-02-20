package com.attornatus.attornatus.resources;

import com.attornatus.attornatus.dto.AddressMinDTO;
import com.attornatus.attornatus.dto.UserInsertDTO;
import com.attornatus.attornatus.dto.UserMinDTO;
import com.attornatus.attornatus.dto.UserUpdateDTO;
import com.attornatus.attornatus.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users")
public class UserResource {

    @Autowired
    private UserService service;


    @GetMapping
    public ResponseEntity<Page<UserMinDTO>> findAllPersonPaged(Pageable pageable) {
        Page<UserMinDTO> pageDTO = service.findAllPersonPaged(pageable);
        return ResponseEntity.ok().body(pageDTO);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UserMinDTO> findByIdPerson(@PathVariable Long id) {
        UserMinDTO userDTO = service.findByIdPerson(id);
        return ResponseEntity.ok().body(userDTO);
    }

    @GetMapping(value = "/{id}/addresses")
    public ResponseEntity<List<AddressMinDTO>> findAddressOfPerson(@PathVariable Long id) {
        List<AddressMinDTO> addressList = service.findAddressOfPerson(id);
        return ResponseEntity.ok().body(addressList);
    }

    @PostMapping
    public ResponseEntity<UserMinDTO> insertPerson(@Valid @RequestBody UserInsertDTO dto) {
        UserMinDTO minDTO = service.insertPerson(dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(minDTO);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UserMinDTO> updatePerson(@PathVariable Long id, @Valid @RequestBody UserUpdateDTO dto) {
        UserMinDTO newDto = service.updatePerson(id, dto);
        return ResponseEntity.ok().body(newDto);
    }
}
