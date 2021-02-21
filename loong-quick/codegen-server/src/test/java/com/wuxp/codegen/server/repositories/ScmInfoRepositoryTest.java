package com.wuxp.codegen.server.repositories;

import com.wuxp.codegen.server.entities.ScmInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;


@DataJpaTest
class ScmInfoRepositoryTest {

    @Autowired
    private ScmInfoRepository scmInfoRepository;

    @Test
    void testFinAll() {
        List<ScmInfo> list = scmInfoRepository.findAll();
        Assertions.assertTrue(list.isEmpty());
    }
}
