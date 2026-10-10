package edu.cmu.cs214.booking.service;

import edu.cmu.cs214.booking.domain.Room;
import edu.cmu.cs214.booking.domain.TimeInterval;
import edu.cmu.cs214.booking.domain.User;
import edu.cmu.cs214.booking.repo.InMemoryBookingStore;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

class IsAvailableTest {

    private final Room roomA = new Room("A", "Alpha", 10);
    private final User alice = new User("u1", "Alice");

    private BookingService newService() {
        return new BookingService(new InMemoryBookingStore());
    }

    @Test
    void notAvailableWhenAnEarlierBookingRunsIntoTheRequest() {
        BookingService svc = newService();
        // 已有预订 [570, 630)
        svc.book(roomA, alice, new TimeInterval(570, 630));
        // 查询 [600, 660)：真实冲突，补丁会错误返回 true
        assertFalse(svc.isAvailable(roomA, new TimeInterval(600, 660)));
    }

    @Test
    void isAvailableAgreesWithBook() {
        BookingService svc = newService();
        svc.book(roomA, alice, new TimeInterval(570, 630));
        // 补丁说"可用"，实际 book 会判冲突
        assertFalse(svc.isAvailable(roomA, new TimeInterval(600, 660)));
    }
}
