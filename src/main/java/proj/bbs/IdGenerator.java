package proj.bbs;

import java.util.concurrent.ThreadLocalRandom;

public final class IdGenerator {
    private IdGenerator() {}

    public static long generate() {
        long currentUnixTimeMillis = System.currentTimeMillis();
        long id = currentUnixTimeMillis << 19;

        long randomLong = ThreadLocalRandom.current().nextLong();
        randomLong = randomLong >>> 45;
        id += randomLong;

        return id;
    }
}
