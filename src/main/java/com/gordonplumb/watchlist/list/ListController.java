package com.gordonplumb.watchlist.list;

import com.gordonplumb.watchlist.list.models.*;
import com.gordonplumb.watchlist.user.User;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/list")
public class ListController {

    private final ListService service;

    public ListController(ListService service) {
        this.service = service;
    }

    @PostMapping()
    public ResponseEntity<ListDTO> createList(@RequestBody ListManagementRequest body) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Watchlist watchlist = service.createList(user, body.getName());
        if (watchlist != null) {
            ListDTO listDto = new ListDTO(watchlist.getId(), watchlist.getUser().getId(), watchlist.getName());
            return ResponseEntity.ok(listDto);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<Page<ListDTO>> getUsersLists(
        @PathVariable(value="id") String userId,
        @RequestParam(required = false) Integer pageNumber,
        @RequestParam(required = false) Integer pageSize
    ) {
        Page<ListDTO> lists = service.getUsersLists(
            userId,
            pageNumber != null ? pageNumber : 0,
            pageSize != null ? pageSize : 100
        ).map((entity) -> new ListDTO(
            entity.getId(),
            entity.getUser().getId(),
            entity.getName()
        ));
        return ResponseEntity.ok(lists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDTO> getList(@PathVariable(value="id") long listId) {
        Watchlist watchlist = service.getList(listId);
        return ResponseEntity.ok(new ListDTO(
            watchlist.getId(),
            watchlist.getUser().getId(),
            watchlist.getName()
        ));
    }

    @GetMapping("/{id}/items")
    public ResponseEntity<Page<ListItemDTO>> getListItems(
        @PathVariable(value="id") long listId,
        @RequestParam(required = false) Integer pageNumber,
        @RequestParam(required = false) Integer pageSize
    ) {
        Page<ListItemDTO> listItems = service.getListItems(
            listId,
            pageNumber != null ? pageNumber : 0,
            pageSize != null ? pageSize : 100
        ).map((entity) -> {
            String tags = entity.getTags();
            return new ListItemDTO(
                entity.getId(),
                entity.getTitle(),
                tags == null ? new String[0] : tags.split(","),
                entity.getRuntime(),
                entity.isWatched()
            );
        });

        return ResponseEntity.ok(listItems);
    }

    @PostMapping("/{id}/items")
    public ResponseEntity<ListItemDTO> addListItem(
        @PathVariable(value = "id") long listId,
        @RequestBody ListItemDTO listItemValues
    ) {
        ListItem listItem = this.service.addListItem(
            listId,
            listItemValues.getTmdbId(),
            listItemValues.getTitle(),
            listItemValues.getTags(),
            listItemValues.getRuntime(),
            listItemValues.isWatched()
        );

        return ResponseEntity.ok(new ListItemDTO(
            listItem.getId(),
            listItem.getTitle(),
            listItem.getTags().split(","),
            listItem.getRuntime(),
            listItem.isWatched()
        ));
    }

    @PutMapping("/{listId}/items/{itemId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void updateListItem(
        @PathVariable(value = "listId") long listId,
        @PathVariable(value = "itemId") long itemId,
        @RequestBody ListItemDTO updateValues
    ) {
        service.updateListItem(
            listId,
            itemId,
            updateValues.getTags(),
            updateValues.isWatched()
        );
    }

    @DeleteMapping("/{listId}/items/{itemId}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteListItem(
        @PathVariable(value = "listId") long listId,
        @PathVariable(value = "itemId") long itemId
    ) {
        service.deleteListItem(listId, itemId);
    }
}
