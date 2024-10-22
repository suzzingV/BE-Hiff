package hiff.hiff.behiff.domain.catalog.application.service;

import hiff.hiff.behiff.domain.catalog.domain.entity.Field;
import hiff.hiff.behiff.domain.catalog.exception.CatalogException;
import hiff.hiff.behiff.domain.catalog.infrastructure.FieldRepository;
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
public class CatalogFieldService {

    private final FieldRepository fieldRepository;

    public Field findById(Long fieldId) {
        return fieldRepository.findById(fieldId)
            .orElseThrow(() -> new CatalogException(ErrorCode.FIELD_NOT_FOUND));
    }

    public List<Field> getAllFields() {
        return fieldRepository.findAll();
    }
}
