package com.playtomic.tests.wallet.api;

import com.playtomic.tests.wallet.entity.Wallet;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WalletResourceProcessor implements ResourceProcessor<Resource<Wallet>> {

    @SneakyThrows
    @Override
    public Resource<Wallet> process(Resource<Wallet> resource) {
        WalletLinkProvider.addLinks(resource);
        return resource;
    }

}
