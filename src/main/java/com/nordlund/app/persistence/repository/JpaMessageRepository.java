package com.nordlund.app.persistence.repository;

import com.nordlund.app.persistence.model.JpaMessage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaMessageRepository extends CrudRepository<JpaMessage, Long> {
}
