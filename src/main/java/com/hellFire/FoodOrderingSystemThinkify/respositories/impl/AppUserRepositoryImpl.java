package com.hellFire.FoodOrderingSystemThinkify.respositories.impl;

import com.hellFire.FoodOrderingSystemThinkify.models.AppUser;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IAppUserRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.hellFire.FoodOrderingSystemThinkify.utils.Utils.getNextId;

@Repository
public class AppUserRepositoryImpl implements IAppUserRepository {

    private HashMap<Long, AppUser> users = new HashMap<>();

    @Override
    public List<AppUser> findAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public AppUser findById(Long id) {
        return users.getOrDefault(id, null);
    }

    @Override
    public AppUser save(AppUser appUser) {
        if(appUser.getId() == null) {
            appUser.setId(getNextId());
        }
        users.put(appUser.getId(), appUser);
        return appUser;
    }
}
