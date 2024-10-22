package hiff.hiff.behiff.domain.catalog.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.Field;
import hiff.hiff.behiff.domain.catalog.domain.entity.Grad;
import hiff.hiff.behiff.domain.catalog.domain.entity.University;
import hiff.hiff.behiff.domain.catalog.exception.CatalogException;
import hiff.hiff.behiff.domain.catalog.infrastructure.FieldRepository;
import hiff.hiff.behiff.domain.catalog.infrastructure.GradRepository;
import hiff.hiff.behiff.domain.catalog.infrastructure.UniversityRepository;
import hiff.hiff.behiff.global.response.properties.ErrorCode;
import java.util.List;
import java.util.Optional;
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
    private final GradRepository gradRepository;

    public List<University> getAllFields() {
        return universityRepository.findAll();
    }

    public List<Grad> getAllGrads() {
        return gradRepository.findAll();
    }
}
