package com.gordonplumb.watchlist.list;

import com.gordonplumb.watchlist.list.models.*;
import com.gordonplumb.watchlist.user.User;
import com.gordonplumb.watchlist.user.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/list")
public class ListController {

    private final ListService listService;
    private final UserService userService;

    public ListController(
        ListService listService,
        UserService userService
    ) {
        this.listService = listService;
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<ListDTO> createList(@RequestBody ListManagementRequest body) {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Watchlist watchlist = listService.createList(user, body.getName());
        if (watchlist != null) {
            ListDTO listDto = new ListDTO(watchlist.getId(), watchlist.getUser().getId(), watchlist.getName());
            return ResponseEntity.ok(listDto);
        }

        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserListsDTO> getUsersLists(
        @PathVariable(value="id") long userId,
        @RequestParam(required = false) Integer pageNumber,
        @RequestParam(required = false) Integer pageSize
    ) {
        User user = userService.getUser(userId);

        // TODO: remove the paging?
        // Probably not going to let users make an unbounded number of lists
        Page<ListDTO> lists = listService.getUsersLists(
            userId,
            0,
            10
        ).map((entity) -> new ListDTO(
            entity.getId(),
            entity.getUser().getId(),
            entity.getName()
        ));
        return ResponseEntity.ok(new UserListsDTO(
            user.getName(),
            lists
        ));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ListDTO> getList(@PathVariable(value="id") long listId) {
        Watchlist watchlist = listService.getList(listId);
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
        Page<ListItemDTO> listItems = listService.getListItems(
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
        ListItem listItem = this.listService.addListItem(
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
        listService.updateListItem(
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
        listService.deleteListItem(listId, itemId);
    }
}
