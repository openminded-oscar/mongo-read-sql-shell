package co.oleh.mongoreadsqlshell;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ConsoleRunner implements CommandLineRunner {
    @Autowired
    private CommonMongoRepository commonMongoRepository;


    @Override
    public void run(String... args) throws Exception {
        commonMongoRepository.read();
    }
}
