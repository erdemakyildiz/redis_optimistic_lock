package com.redis_optimistic_lock;

import com.redis_optimistic_lock.entity.User;
import com.redis_optimistic_lock.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RedisOptimisticLockApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(RedisOptimisticLockApplication.class, args);
    }

    @Autowired
    private UserService userService;

    @Override
    public void run(String... args) throws Exception {
        User user = new User();
        user.setId(2);
        user.setName("Erdem");
        user.setSurname("Akyıldız");
        user.setAge(25);

        userService.save(user);
        final User dbUser = userService.getUser(user.getId());

        /**
         * 10 kere aynı nesene üzerinden yaşı bir arttıracak şekilde işlem yapıyoruz.
         * Eğer optimistic lock kullanmasaydık son işlemden sonra kullanıcının yaşı 26 olarak
         * gözükecekdi.
         *
         * Optimistic lock kullandığımız için son işlemden sonra kullanıcının yaşı 35 oluyor.
         */
        System.out.println("Kullanıcının Yaşı : " + user.getAge());

        updateUserAge(1, dbUser);
        updateUserAge(1, dbUser);
        updateUserAge(1, dbUser);
        updateUserAge(1, dbUser);
        updateUserAge(1, dbUser);
        updateUserAge(1, dbUser);
        updateUserAge(1, dbUser);
        updateUserAge(1, dbUser);
        updateUserAge(1, dbUser);
        updateUserAge(1, dbUser);

        System.out.println("Kullanıcının Yaşı Son Hali : " + userService.getUser(user.getId()).getAge());

    }

    private void updateUserAge(int value, User user) throws CloneNotSupportedException {
        User cloneUser = (User) user.clone();
        cloneUser.setAge(user.getAge() + value);

        /**
         * İlk çektiğimiz verinin değişmemesi için nesneyi klonluyoruz
         */
        boolean saveStatus = userService.save(cloneUser);
        if (saveStatus) {
            System.out.println("Kullanıcı verisi güncellendi.");
        } else{
            /**
             * Elimizdeki veri bayatladığı için güncelleme işlemini yapmadı.
             * Tekrar güncel veriyi çekip onun üzerinde değşikliklerimizi yapıyoruz.
             */
            user = userService.getUser(user.getId());
            updateUserAge(value, user);
        }
    }
}
