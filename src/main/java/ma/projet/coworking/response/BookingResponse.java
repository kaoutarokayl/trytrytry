package ma.projet.coworking.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookingResponse {

    private Long id;

    private LocalDate date;

    private LocalTime heureDebut;

    private LocalTime heureFin;

    private int capacity;



    private String clientFullName;

    private String clientEmail;



    private String bookingConfirmationCode;

    private RoomResponse room;

    public BookingResponse(Long id, LocalDate date, LocalTime heureDebut, LocalTime heureFin,
                           String bookingConfirmationCode) {
        this.id = id;

        this.date= date;

        this.heureDebut= heureDebut;
        this.heureFin= heureFin;

        this.bookingConfirmationCode = bookingConfirmationCode;
    }
}
