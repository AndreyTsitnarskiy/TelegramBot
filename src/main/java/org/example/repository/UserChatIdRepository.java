package org.example.repository;

import org.example.model.UserChatID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserChatIdRepository extends JpaRepository<UserChatID, Integer> {
}
