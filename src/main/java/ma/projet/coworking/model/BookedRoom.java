package ma.projet.coworking.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BookedRoom {



    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull(message = "La date est obligatoire")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime heureDebut;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime heureFin;
    @AssertTrue(message = "End time of the meeting must be after it's start time")
    private boolean isEndTimeAfterStartTime() {
        return heureFin.isAfter(heureDebut);
    }

    @AssertTrue(message = "Meeting can start only after 6am and before 7pm")
    private boolean isStartTimeValid() {
        return heureDebut.isAfter(LocalTime.of(5, 59)) && heureDebut.isBefore(LocalTime.of(19, 1));
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @Column(name = "client_fullName")
    private String clientFullName;

    @Column(name = "client_email")
    private String clientEmail;

    private int capacity;

    @Column(name = "confirmation_Code")
    private String bookingConfirmationCode;

    public void setBookingConfirmationCode(String bookingConfirmationCode) {
        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
