package ma.projet.coworking.service;

import ma.projet.coworking.model.Room;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface IRoomService {
    Room addNewRoom(MultipartFile photo, String roomType, BigDecimal pricePerHour ) throws SQLException, IOException;

    List<String> getAllRoomTypes();

    List<Room> getAllRooms();

    byte[] getRoomPhotoByRoomId(Long Id) throws SQLException;

    void deleteRoom(Long Id);

    Room updateRoom(Long Id, String roomType, BigDecimal pricePerHour, byte[] photoBytes);

    Optional<Room> getRoomById(Long Id);

    List<Room> getAvailableRooms(LocalDate date, LocalTime heureDebut, LocalTime heureFin, String roomType);
}
