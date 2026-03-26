package com.pollos.autenticacion.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.pollos.autenticacion.dto.UsersResponseDTO;
import com.pollos.autenticacion.entity.Users;
import com.pollos.autenticacion.repository.UsersRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsersService {
    private final UsersRepository usersRepository;

    public List<UsersResponseDTO> getUsers() {
        List<UsersResponseDTO> response = new ArrayList<>();
        List<Users> usersFound = usersRepository.findAll();

        for (Users users : usersFound) {
            UsersResponseDTO user = new UsersResponseDTO();
            user.setId(users.getId());
            user.setEmail(users.getEmail());
            response.add(user);
        }

        return response;
    }
}
