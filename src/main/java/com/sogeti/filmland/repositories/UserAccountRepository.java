package com.sogeti.filmland.repositories;

import com.sogeti.filmland.models.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    UserAccount findOneByUserNameIgnoreCase(String userName);
}
