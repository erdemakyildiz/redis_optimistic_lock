package com.redis_optimistic_lock.repository;

import com.redis_optimistic_lock.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Erdem Akyıldız on 22.02.2019.
 */
@Repository
public interface UserRepository extends CrudRepository<User, Long> {

}
