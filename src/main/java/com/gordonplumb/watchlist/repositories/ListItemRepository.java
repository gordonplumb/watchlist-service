package com.gordonplumb.watchlist.repositories;

import com.gordonplumb.watchlist.models.ListItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListItemRepository extends JpaRepository<ListItem, Long> {
}
