package com.example.BookVault.persistence;

import com.example.BookVault.persistence.BookJPAEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJPARepository extends JpaRepository<BookJPAEntity, Integer> {
}
