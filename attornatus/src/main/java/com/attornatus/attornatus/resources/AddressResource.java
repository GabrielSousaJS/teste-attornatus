package com.attornatus.attornatus.resources;

import com.attornatus.attornatus.dto.AddressDTO;
import com.attornatus.attornatus.services.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

@RestController
@RequestMapping(value = "/addresses")
public class AddressResource {

    @Autowired
    private AddressService service;

    @PostMapping(value = "/{id}")
    public ResponseEntity<AddressDTO> insertAddressOfPersonWithAdminProfileOrCurrentUser(@PathVariable Long id, @Valid @RequestBody AddressDTO dto) {
        dto = service.insertAddressOfPerson(id, dto);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();

        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping(value = "/{id}/mainaddress")
    public ResponseEntity<AddressDTO> informMainAddress(@PathVariable Long id, @Valid @RequestBody AddressDTO dto) {
        AddressDTO addressDTO = service.informMainAddress(id, dto);
        return ResponseEntity.ok().body(addressDTO);
    }
}
