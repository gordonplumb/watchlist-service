package com.gordonplumb.watchlist.list;

import com.gordonplumb.watchlist.list.models.Watchlist;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {
    Page<Watchlist> findAllByUserId(long id, Pageable pageable);
}
