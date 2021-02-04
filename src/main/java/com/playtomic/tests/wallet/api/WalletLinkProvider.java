package com.playtomic.tests.wallet.api;

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

        if (!resource.hasLink("wallet")) {
            resource.add(WalletLinkProvider.wallet(id));
        }

        resource.add(WalletLinkProvider.movements(id));
    }

    private Link movements(Long walletId) {
        return ControllerLinkBuilder
              .linkTo(ControllerLinkBuilder.methodOn(WalletController.class)
                                           .movements(walletId, null))
              .withRel("movements");
    }

    private Link wallet(Long walletId) {
        return ControllerLinkBuilder
              .linkTo(WalletController.class).slash(walletId).withRel("wallet");
    }
}
