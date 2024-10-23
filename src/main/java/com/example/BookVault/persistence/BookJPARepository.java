package com.example.BookVault.persistence;

import com.example.BookVault.domain.BookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookJPARepository extends JpaRepository<BookJPAEntity, BookId> {
}
