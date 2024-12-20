package ma.projet.coworking.service;


import lombok.RequiredArgsConstructor;
import ma.projet.coworking.exception.InternalServerException;
import ma.projet.coworking.exception.ResourceNotFoundException;
import ma.projet.coworking.model.Room;
import ma.projet.coworking.repository.RoomRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;



@Service
@RequiredArgsConstructor
public class RoomService implements IRoomService {
    private final RoomRepository roomRepository;
    @Override
    public Room addNewRoom(MultipartFile file, String roomType, BigDecimal pricePerHour) throws SQLException, IOException {
        Room room = new Room();
        room.setRoomType(roomType);
        room.setPricePerHour(pricePerHour);
        if (!file.isEmpty()){
            byte[] photoBytes = file.getBytes();
            Blob photoBlob = new SerialBlob(photoBytes);
            room.setPhoto(photoBlob);
        }
        return roomRepository.save(room);
    }

    @Override
    public List<String> getAllRoomTypes() {
        return roomRepository.findDistinctRoomTypes();
    }

    @Override
    public List<Room> getAllRooms() {
        return roomRepository.findAll();
    }

    @Override
    public byte[] getRoomPhotoByRoomId(Long id) throws SQLException {
        Optional<Room> theRoom = roomRepository.findById(id);
        if(theRoom.isEmpty()){
            throw new ResourceNotFoundException("Sorry, Room not found!");
        }
        Blob photoBlob = theRoom.get().getPhoto();
        if(photoBlob != null){
            return photoBlob.getBytes(1, (int) photoBlob.length());
        }
        return null;
    }

    @Override
    public void deleteRoom(Long id) {
        Optional<Room> theRoom = roomRepository.findById(id);
        if(theRoom.isPresent()){
            roomRepository.deleteById(id);
        }
    }

    @Override
    public Room updateRoom(Long id, String roomType, BigDecimal pricePerHour, byte[] photoBytes) {
        Room room = roomRepository.findById(id).get();
        if (roomType != null) room.setRoomType(roomType);
        if (pricePerHour != null) room.setPricePerHour(pricePerHour);
        if (photoBytes != null && photoBytes.length > 0) {
            try {
                room.setPhoto(new SerialBlob(photoBytes));
            } catch (SQLException ex) {
                throw new InternalServerException("Fail updating room");
            }
        }
        return roomRepository.save(room);
    }

    @Override
    public Optional<Room> getRoomById(Long id) {
        return Optional.of(roomRepository.findById(id).get());
    }

    @Override
    public List<Room> getAvailableRooms(LocalDate date, LocalTime heureDebut, LocalTime heureFin, String roomType)
    {
        return roomRepository.findAvailableRoomsByDatesAndType(date, heureDebut, heureFin, roomType);
    }
}
