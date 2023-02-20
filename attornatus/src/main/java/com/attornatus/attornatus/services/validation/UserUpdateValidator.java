package com.attornatus.attornatus.services.validation;

import com.attornatus.attornatus.dto.UserUpdateDTO;
import com.attornatus.attornatus.entities.User;
import com.attornatus.attornatus.repositories.UserRepository;
import com.attornatus.attornatus.resources.exceptions.FieldMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserUpdateValidator implements ConstraintValidator<UserUpdateValid, UserUpdateDTO> {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UserRepository repository;

    @Override
    public void initialize(UserUpdateValid ann) {
    }

    @Override
    public boolean isValid(UserUpdateDTO dto, ConstraintValidatorContext context) {

        @SuppressWarnings("unchecked")
        var varsUri = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        long userId = Long.parseLong(varsUri.get("id"));

        List<FieldMessage> list = new ArrayList<>();

        User entity = repository.findByName(dto.getName());

        if (entity != null && userId != entity.getId()) {
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
