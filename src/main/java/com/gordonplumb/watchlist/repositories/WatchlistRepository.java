package com.gordonplumb.watchlist.repositories;

import com.gordonplumb.watchlist.models.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
}
