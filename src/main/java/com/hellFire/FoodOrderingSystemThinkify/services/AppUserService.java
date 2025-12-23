package com.hellFire.FoodOrderingSystemThinkify.services;

import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.AddAppUserRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.requests.AddWalletRequest;
import com.hellFire.FoodOrderingSystemThinkify.dtos.responses.AppUserDto;
import com.hellFire.FoodOrderingSystemThinkify.exceptions.UserNotFoundException;
import com.hellFire.FoodOrderingSystemThinkify.models.AppUser;
import com.hellFire.FoodOrderingSystemThinkify.models.Wallet;
import com.hellFire.FoodOrderingSystemThinkify.models.enums.Currency;
import com.hellFire.FoodOrderingSystemThinkify.models.pojo.ItemPrice;
import com.hellFire.FoodOrderingSystemThinkify.respositories.IAppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AppUserService {

    @Autowired
    private IAppUserRepository appUserRepository;

    @Autowired
    private WalletService walletService;

    public AppUserDto createAppUser(AddAppUserRequest request) {
        AppUser appUser = new AppUser();
        appUser.setName(request.getName());
        Wallet wallet = new Wallet();
        wallet.setItemPrice(new ItemPrice(0.0, Currency.INR));
        appUser.setWallet(walletService.saveWallet(wallet));
        return toDto(appUserRepository.save(appUser));
    }

    public AppUserDto addWallet(Long userId, AddWalletRequest request) throws UserNotFoundException {
        AppUser appUser = getAppUserById(userId);
        Double price = appUser.getWallet().getItemPrice().getPrice() + request.getItemPrice().getPrice();
        Currency currency = appUser.getWallet().getItemPrice().getCurrency();
        if (Objects.isNull(currency)) {
            currency = Currency.INR;
        }
        Wallet wallet = appUser.getWallet();
        wallet.setItemPrice(new ItemPrice(price, currency));
        Wallet newWallet = walletService.saveWallet(wallet);
        appUser.setWallet(newWallet);
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
        appUserDto.setWallet(walletService.toDto(appUser.getWallet()));
        return appUserDto;
    }

    public List<AppUserDto> toDtoList(List<AppUser> appUsers) {
        return appUsers.stream().map(this::toDto).collect(Collectors.toList());
    }
}
