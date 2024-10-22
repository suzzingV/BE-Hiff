package hiff.hiff.behiff.domain.catalog.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.Field;
import hiff.hiff.behiff.domain.catalog.domain.entity.University;
import hiff.hiff.behiff.domain.catalog.exception.CatalogException;
import hiff.hiff.behiff.domain.catalog.infrastructure.FieldRepository;
import hiff.hiff.behiff.domain.catalog.infrastructure.UniversityRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CatalogSchoolService {

    private final UniversityRepository universityRepository;

    public List<University> getAllFields() {
        return universityRepository.findAll();
    }
}
