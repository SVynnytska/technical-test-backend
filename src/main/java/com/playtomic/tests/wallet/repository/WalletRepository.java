package com.playtomic.tests.wallet.repository;

import java.util.Optional;
import javax.persistence.LockModeType;

import com.playtomic.tests.wallet.entity.Wallet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RestResource;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    @RestResource(exported = false)
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT w FROM Wallet w WHERE w.id =:id")
    Optional<Wallet> getWithWriteLock(@Param("id") Long id);

}
