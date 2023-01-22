import com.taskmanager.server.KVServer;

import java.io.IOException;


public class Main {

    public static void main(String[] args) {
        try {
           KVServer server = new KVServer();
           server.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}