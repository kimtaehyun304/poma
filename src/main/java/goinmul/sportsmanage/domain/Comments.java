package goinmul.sportsmanage.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comments {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "customer_support_id")
    private CustomerSupport customerSupport;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Comments parent;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private String content;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "parent")
    @BatchSize(size = 1000)
    private List<Comments> children = new ArrayList<>();

    public Comments(CustomerSupport customerSupport, Comments parent, User user, String content) {
        this.customerSupport = customerSupport;
        this.parent = parent;
        this.user = user;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public Comments(Long parentId) {
        this.id = parentId;

    }


    /*
    public static Comments createPost(CustomerSupport customerSupport, Comments parent, User user, String content, LocalDateTime createdAt) {
        Comments comments = new Comments();
        comments.getId() = 1L;
        return customerSupport;
    }*/

}
