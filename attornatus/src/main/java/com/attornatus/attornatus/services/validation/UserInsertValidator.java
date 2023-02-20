package com.attornatus.attornatus.services.validation;

import com.attornatus.attornatus.dto.UserInsertDTO;
import com.attornatus.attornatus.entities.User;
import com.attornatus.attornatus.repositories.UserRepository;
import com.attornatus.attornatus.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO> {

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserInsertValid ann) {
    }

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {

        List<FieldMessage> list = new ArrayList<>();

        User entity = repository.findByName(dto.getName());

        if (entity != null) {
            list.add(new FieldMessage("name", "Person already registered!"));
        }

        for (FieldMessage e : list) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldError())
                    .addConstraintViolation();
        }
        return list.isEmpty();
    }
}
