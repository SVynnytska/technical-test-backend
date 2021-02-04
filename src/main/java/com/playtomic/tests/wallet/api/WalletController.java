package com.playtomic.tests.wallet.api;

import javax.validation.Valid;

import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.service.WalletService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/wallets")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @PostMapping("/{id}/movements")
    public ResponseEntity<Resource<Wallet>> movements(@PathVariable @NonNull Long id,
          @RequestBody @Valid MovementRequest request) {
        return walletService.move(id, request.getAmount(), request.getType())
                            .map(this::toResource)
                            .map(ResponseEntity::ok)
                            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    private Resource<Wallet> toResource(Wallet wallet) {
        Resource<Wallet> resource = new Resource<>(wallet);
        WalletLinkProvider.addLinks(resource);
        return resource;
    }
}
