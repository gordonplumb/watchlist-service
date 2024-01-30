package com.gordonplumb.watchlist.list.models;

import com.gordonplumb.watchlist.list.models.ListItem;
import com.gordonplumb.watchlist.user.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class Watchlist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @ManyToOne
    @JoinColumn(name="user_id", nullable = false)
    private User user;
    private String name;
    @OneToMany(mappedBy = "watchlist")
    private List<ListItem> listItems;

    public Watchlist() {}

    public Watchlist(User user, String name) {
        this.user = user;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ListItem> getListItems() {
        return this.listItems;
    }
}
