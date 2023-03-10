package com.inenergis.microbot.camel.dao;

import com.inenergis.entity.Account;
import org.springframework.data.repository.Repository;
import org.springframework.stereotype.Component;

@Component
public interface AccountDao extends Repository<Account, Long> {

    Account getByAccountId(String accountId);
}