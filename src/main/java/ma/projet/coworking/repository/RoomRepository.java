package ma.projet.coworking.repository;

import ma.projet.coworking.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("SELECT DISTINCT r.roomType FROM Room r")
    List<String> findDistinctRoomTypes();

    @Query(" SELECT r FROM Room r " +
            " WHERE r.roomType LIKE %:roomType% " +
            " AND r.id NOT IN (" +
            "  SELECT br.room.id FROM BookedRoom br " +
            "  WHERE br.date = :date " +  // Ajouter la condition de date
            "    AND ((br.heureDebut <= :heureFin) AND (br.heureFin >= :heureDebut))" +
            ")")
    List<Room> findAvailableRoomsByDatesAndType(LocalDate date, LocalTime heureDebut, LocalTime heureFin, String roomType);
}
