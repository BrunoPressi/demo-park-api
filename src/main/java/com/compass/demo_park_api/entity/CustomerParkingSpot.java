package com.compass.demo_park_api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "customer_parking_spot")
public class CustomerParkingSpot implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "number_receipt", unique = true, nullable = false, length = 15)
    private String receipt;

    @Column(name = "license_plate_number", nullable = false, length = 8)
    private String licensePlateNumber;

    @Column(name = "car_brand", nullable = false, length = 45)
    private String carBrand;

    @Column(name = "car_model", nullable = false, length = 45)
    private String carModel;

    @Column(name = "car_color", nullable = false, length = 45)
    private String carColor;

    @Column(name = "entry_date", nullable = false)
    private LocalDateTime entryDate;

    @Column(name = "exit_date", nullable = true)
    private LocalDateTime exitDate;

    @Column(name = "value", nullable = true, columnDefinition = "decimal(7,2)")
    private BigDecimal value;

    @Column(name = "discount", nullable = true, columnDefinition = "decimal(7,2)")
    private BigDecimal discount;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "parkiking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @Column(name = "date_creation")
    @CreatedDate
    private LocalDateTime dateCreation;

    @Column(name = "date_modify")
    @LastModifiedDate
    private LocalDateTime dateModify;

    @Column(name = "create_by")
    @CreatedBy
    private String createBy;

    @Column(name = "modify_by")
    @LastModifiedBy
    private String modifyBy;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CustomerParkingSpot that = (CustomerParkingSpot) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "CustomerParkingSpot{" +
                "id=" + id +
                '}';
    }
}
