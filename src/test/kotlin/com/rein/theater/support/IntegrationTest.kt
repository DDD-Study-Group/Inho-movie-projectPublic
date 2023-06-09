package com.rein.theater.support

import com.rein.theater.TheaterApplication
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [ TheaterApplication::class ])
@AutoConfigureMockMvc
@TestPropertySource(locations = [ "classpath:application-local.yml" ])
abstract class IntegrationTest {
    @Autowired
    protected lateinit var mvc: MockMvc
}
