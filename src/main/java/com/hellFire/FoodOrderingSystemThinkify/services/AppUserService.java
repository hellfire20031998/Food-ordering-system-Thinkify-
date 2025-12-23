package com.hellFire.FoodOrderingSystemThinkify.services;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.AddAppUserRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.AppUserDto;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.UserNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.models.AppUser;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    @Autowired
    private IAppUserRepository appUserRepository;

    public AppUserDto createAppUser(AddAppUserRequest request) {
        AppUser appUser = new AppUser();
        appUser.setName(request.getName());
        return toDto(appUserRepository.save(appUser));
    }

    public List<AppUserDto> getAllAppUsers() {
        return toDtoList(appUserRepository.findAll());
    }

    public AppUser getAppUserById(Long id) throws UserNotFoundException {
        AppUser appUser = appUserRepository.findById(id);
        if (appUser == null) {
            throw new UserNotFoundException("User not found with id: " + id);
        }else {
            return appUser;
        }
    }

    public AppUserDto toDto(AppUser appUser) {
        AppUserDto appUserDto = new AppUserDto();
        appUserDto.setId(appUser.getId());
        appUserDto.setName(appUser.getName());
        return appUserDto;
    }

    public List<AppUserDto> toDtoList(List<AppUser> appUsers) {
        return appUsers.stream().map(this::toDto).collect(Collectors.toList());
    }
}
