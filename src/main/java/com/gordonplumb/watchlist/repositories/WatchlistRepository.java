package com.gordonplumb.watchlist.repositories;

import com.gordonplumb.watchlist.models.Watchlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    Page<Watchlist> findAllByUserId(long id, Pageable pageable);
}
