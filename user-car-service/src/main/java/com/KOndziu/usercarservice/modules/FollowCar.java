package com.KOndziu.usercarservice.modules;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "follow_cars")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class FollowCar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "car_id")
    private Integer carId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "mark")
    private String mark;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "color")
    private String color;

    @Column(name = "image")
    @Lob
    private byte[] image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="id")
    @Getter(AccessLevel.PRIVATE)
    private User user;



    public FollowCar(Integer userId, String mark, String carType, String color, byte[] image) {
        this.userId = userId;
        this.mark = mark;
        this.carType = carType;
        this.color = color;
        this.image = image;
    }
}
