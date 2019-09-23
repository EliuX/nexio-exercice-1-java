package com.nexio.exercices.persistence;

import com.nexio.exercices.configuration.JpaConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@DataJpaTest
@Import(JpaConfig.class)
public class JPASpecificationsTest {


    @Test
    @WithMockUser(username = "user@nexio.com")
    public void shouldReturnActiveUser() {
        assertEquals(
                "Expected to return username of active user",
                "user@nexio.com",
                JPASpecifications.currentUsername()
        );
    }
}