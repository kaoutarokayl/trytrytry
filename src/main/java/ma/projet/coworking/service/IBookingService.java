package ma.projet.coworking.service;


import ma.projet.coworking.model.BookedRoom;

import java.util.List;



public interface IBookingService {
    void cancelBooking(Long id);

    List<BookedRoom> getAllBookingsByRoomId(Long id);

    String saveBooking(Long id, BookedRoom bookingRequest);

    BookedRoom findByBookingConfirmationCode(String confirmationCode);

    List<BookedRoom> getAllBookings();

    List<BookedRoom> getBookingsByUserEmail(String email);
}
