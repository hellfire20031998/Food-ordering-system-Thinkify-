package com.hellFire.FoodOrderingSystemThinkify.respositories;

import com.hellFire.FoodOrderingSystemThinkify.models.AppUser;

import java.util.List;

public interface IAppUserRepository {

    List<AppUser> findAll();
    AppUser findById(Long id);
    AppUser save(AppUser appUser);
}
