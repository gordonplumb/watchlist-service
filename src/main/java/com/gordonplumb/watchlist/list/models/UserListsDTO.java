package com.gordonplumb.watchlist.list.models;

import org.springframework.data.domain.Page;

public class UserListsDTO {
    private String name;
    private Page<ListDTO> lists;

    public UserListsDTO(String name, Page<ListDTO> lists) {
        this.name = name;
        this.lists = lists;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Page<ListDTO> getLists() {
        return lists;
    }

    public void setLists(Page<ListDTO> lists) {
        this.lists = lists;
    }
}
