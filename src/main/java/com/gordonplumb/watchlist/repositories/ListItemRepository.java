package com.gordonplumb.watchlist.repositories;

import com.gordonplumb.watchlist.models.ListItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListItemRepository extends JpaRepository<ListItem, Long> {

    Page<ListItem> findAllByWatchlistId(long listId, Pageable pageable);
}
