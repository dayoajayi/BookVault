package com.example.BookVault.catalog.persistence;

import com.example.BookVault.catalog.domain.BookId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookJPARepository extends JpaRepository<BookJPAEntity, BookId> {

    public Optional<BookJPAEntity> findByIsbn(String isbn);

    public boolean existsByIsbn(String isbn);
}
