package ma.projet.coworking.service;

import lombok.RequiredArgsConstructor;
import ma.projet.coworking.exception.InvalidBookingRequestException;
import ma.projet.coworking.exception.ResourceNotFoundException;
import ma.projet.coworking.model.BookedRoom;
import ma.projet.coworking.model.Room;
import ma.projet.coworking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
@RequiredArgsConstructor
public class BookingService implements IBookingService {
    private final BookingRepository bookingRepository;
    private final IRoomService roomService;


    @Override
    public List<BookedRoom> getAllBookings() {
        return bookingRepository.findAll();
    }


    @Override
    public List<BookedRoom> getBookingsByUserEmail(String email) {
        return bookingRepository.findByClientEmail(email);
    }

    @Override
    public void cancelBooking(Long id) {
        bookingRepository.deleteById(id);
    }

    @Override
    public List<BookedRoom> getAllBookingsByRoomId(Long id) {
        return bookingRepository.findByRoomId(id);
    }

    @Override
    public String saveBooking(Long id, BookedRoom bookingRequest) {
        if (bookingRequest.getHeureFin().isBefore(bookingRequest.getHeureDebut())){
            throw new InvalidBookingRequestException("Check-in date must come before check-out date");
        }
        Room room = roomService.getRoomById(id).get();
        List<BookedRoom> existingBookings = room.getBookings();
        boolean roomIsAvailable = roomIsAvailable(bookingRequest,existingBookings);
        if (roomIsAvailable){
            room.addBooking(bookingRequest);
            bookingRepository.save(bookingRequest);
        }else{
            throw  new InvalidBookingRequestException("Sorry, This room is not available for the selected dates;");
        }
        return bookingRequest.getBookingConfirmationCode();
    }

    @Override
    public BookedRoom findByBookingConfirmationCode(String confirmationCode) {
        return bookingRepository.findByBookingConfirmationCode(confirmationCode)
                .orElseThrow(() -> new ResourceNotFoundException("No booking found with booking code :"+confirmationCode));

    }


    private boolean roomIsAvailable(BookedRoom bookingRequest, List<BookedRoom> existingBookings) {
        return existingBookings.stream()
                .noneMatch(existingBooking ->
                        bookingRequest.getHeureFin().equals(existingBooking.getHeureDebut())
                                || bookingRequest.getHeureDebut().isBefore(existingBooking.getHeureFin())
                                || (bookingRequest.getHeureDebut().isAfter(existingBooking.getHeureDebut())
                                && bookingRequest.getHeureDebut().isBefore(existingBooking.getHeureFin()))
                                || (bookingRequest.getHeureDebut().isBefore(existingBooking.getHeureDebut())

                                && bookingRequest.getHeureFin().equals(existingBooking.getHeureFin()))
                                || (bookingRequest.getHeureDebut().isBefore(existingBooking.getHeureDebut())

                                && bookingRequest.getHeureFin().isAfter(existingBooking.getHeureFin()))

                                || (bookingRequest.getHeureDebut().equals(existingBooking.getHeureFin())
                                && bookingRequest.getHeureFin().equals(existingBooking.getHeureDebut()))

                                || (bookingRequest.getHeureDebut().equals(existingBooking.getHeureFin())
                                && bookingRequest.getHeureFin().equals(bookingRequest.getHeureDebut()))
                );
    }




}
