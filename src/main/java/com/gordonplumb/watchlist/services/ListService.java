package com.gordonplumb.watchlist.services;

import com.gordonplumb.watchlist.models.ListItem;
import com.gordonplumb.watchlist.models.User;
import com.gordonplumb.watchlist.models.Watchlist;
import com.gordonplumb.watchlist.models.exceptions.BadRequestException;
import com.gordonplumb.watchlist.models.exceptions.ForbiddenException;
import com.gordonplumb.watchlist.models.exceptions.ResourceNotFoundException;
import com.gordonplumb.watchlist.repositories.ListItemRepository;
import com.gordonplumb.watchlist.repositories.WatchlistRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ListService {

    private final WatchlistRepository listRepository;
    private final ListItemRepository listItemRepository;

    public ListService(
        WatchlistRepository listRepository, ListItemRepository listItemRepository
    ) {
        this.listRepository = listRepository;
        this.listItemRepository = listItemRepository;
    }

    public Watchlist getList(long listId) {
        return listRepository
            .findById(listId)
            .orElseThrow(() -> new ResourceNotFoundException("List not found"));
    }

    public Page<Watchlist> getUsersLists(long userId, int pageNumber, int pageSize) {
        return listRepository.findAllByUserId(
            userId,
            PageRequest.of(pageNumber, pageSize)
        );
    }

    public Watchlist createList(User user, String name) {
        if (listRepository.findAllByUserId(user.getId(), PageRequest.of(0, 1)).isEmpty()) {
            // remove if i decide to support multiple lists per user
            return null;
        }
        String listName = name == null ? "" : name.strip();
        if (listName.isEmpty()) {
            listName = user.getName() + "'s list";
        }
        Watchlist watchlist = new Watchlist(user, listName);
        this.listRepository.save(watchlist);
        return watchlist;
    }

    public Page<ListItem> getListItems(long listId, int pageNumber, int pageSize) {
        Pageable pageable = PageRequest.of(pageNumber, pageSize);

        return listItemRepository.findAllByWatchlistId(listId, pageable);
    }

    public ListItem addListItem(long listId, int tmdbId, String title, String[] tags, int runtime, boolean watched) {
        Watchlist list = this.getList(listId);
        this.verifyUserIsAuthorized(list);
        String tagsCsv = this.convertTagArrayToString(tags);
        ListItem listItem = new ListItem(list, tmdbId, title, tagsCsv, runtime, watched);
        try {
            this.listItemRepository.save(listItem);
        } catch (DataIntegrityViolationException ex) {
            throw new BadRequestException("Data integrity exception");
        }

        return listItem;
    }

    public void updateListItem(
        long listId,
        long itemId,
        String[] updatedTags,
        Boolean updatedWatchedStatus
    ) {
        Watchlist list = this.getList(listId);
        this.verifyUserIsAuthorized(list);

        ListItem listItem = list.getListItems()
            .stream()
            .filter(item -> item.getId() == itemId)
            .findFirst()
            .orElse(null);

        if (listItem == null) {
            throw new BadRequestException("The item does not belong to this list");
        }

        boolean shouldUpdate = false;
        if (updatedTags != null) {
            String tagsCsv = this.convertTagArrayToString(updatedTags);
            if (!Objects.equals(listItem.getTags(), tagsCsv)) {
                listItem.setTags(tagsCsv);
                shouldUpdate = true;
            }
        }

        if (updatedWatchedStatus != null && listItem.isWatched() != updatedWatchedStatus) {
            listItem.setWatched(updatedWatchedStatus);
            shouldUpdate = true;
        }

        if (shouldUpdate) {
            this.listItemRepository.save(listItem);
        }
    }

    public void deleteListItem(long listId, long itemId) {
        Watchlist list = this.getList(listId);
        this.verifyUserIsAuthorized(list);

        if (list.getListItems().stream().noneMatch(item -> item.getId() == itemId)) {
            throw new BadRequestException("The item does not belong to this list");
        }

        this.listItemRepository.deleteById(itemId);
    }

    private void verifyUserIsAuthorized(Watchlist list) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (list.getUser().getId() != user.getId()) {
            throw new ForbiddenException("User not authorized");
        }
    }

    private String convertTagArrayToString(String[] tags) {
        String tagsCsv = Arrays
            .stream(tags)
            .map(String::strip)
            .filter(tag -> !tag.isEmpty())
            .collect(Collectors.joining(","));

        return tagsCsv.isEmpty() ? null : tagsCsv;
    }
}
