package goinmul.sportsmanage.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import java.time.LocalDateTime;

@Entity
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
public class Ground {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ground_id")
    private Long id;
    private String name;
    private String location;

    @ColumnDefault("0")
    private Integer price;

    private LocalDateTime createdAt;

    public static Ground createGround(String name, String location, Integer price){
        Ground ground = new Ground();
        ground.setName(name);
        ground.setLocation(location);
        ground.setPrice(price);
        ground.setCreatedAt(LocalDateTime.now());
        return ground;
    }

    public static Ground createGround(Long id){
        Ground ground = new Ground();
        ground.setId(id);
        return ground;
    }

    public void changeGround(String name, String location, Integer price){
        this.setName(name);
        this.setLocation(location);
        this.setPrice(price);

    }
}
