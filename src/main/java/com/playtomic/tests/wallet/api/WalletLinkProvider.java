package com.playtomic.tests.wallet.api;

import java.math.BigDecimal;

import com.playtomic.tests.wallet.entity.Wallet;
import lombok.experimental.UtilityClass;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

@UtilityClass
public class WalletLinkProvider {

    public void addLinks(Resource<Wallet> resource) {
        Wallet wallet = resource.getContent();
        Long id = wallet.getId();
        BigDecimal balance = wallet.getBalance();

        if (BigDecimal.ZERO.compareTo(balance) < 0) {
            resource.add(WalletLinkProvider.charges(id));
        }
        if (!resource.hasLink("wallet")) {
            resource.add(WalletLinkProvider.wallet(id));
        }

        resource.add(WalletLinkProvider.recharges(id));
    }

    private Link charges(Long walletId) {
        return ControllerLinkBuilder
              .linkTo(ControllerLinkBuilder.methodOn(WalletController.class)
                                           .charge(walletId, null))
              .withRel("charges");
    }

    private Link recharges(Long walletId) {
        return ControllerLinkBuilder
              .linkTo(ControllerLinkBuilder.methodOn(WalletController.class)
                                           .recharge(walletId, null))
              .withRel("recharges");
    }

    private Link wallet(Long walletId) {
        return ControllerLinkBuilder
              .linkTo(WalletController.class).slash(walletId).withRel("wallet");
    }
}
