package com.epam.esm.dao.impl;

import com.epam.esm.config.AppConfig;
import com.epam.esm.exceptions.DataNotFoundException;
import com.epam.esm.exceptions.ExceptionCodes;
import com.epam.esm.model.GiftCertificate;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.epam.esm.constants.GiftCertificatesTestConstants.*;
import static com.epam.esm.exceptions.ExceptionCodes.NOT_FOUND_GIFT_CERTIFICATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = AppConfig.class)
@Sql(scripts = "classpath:schema.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "classpath:test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
public class GiftCertificateDaoImplTest {

    @Autowired
    private GiftCertificateDaoImpl giftCertificateDao;

    @Test
    public void getTags_correctTagList_whenGetGiftCertificates() {
        assertEquals(GIFT_CERTIFICATE_LIST, giftCertificateDao.getGiftCertificates(),
                "When getting certificates, list should be equal to database values");
    }

    @Test
    public void getGiftCertificate_correctGiftCertificate_whenGetGiftCertificate() {
        assertEquals(GIFT_CERTIFICATE_2, giftCertificateDao.getGiftCertificate(GIFT_CERTIFICATE_2.getId()),
                "When getting certificate, it should be equal to database value");
    }

    @Test
    public void getGiftCertificate_exception_whenTryToGetAbsentCertificate() {
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> giftCertificateDao.getGiftCertificate(ABSENT_ID),
                "Tag should be not found and  DataNotFoundException should be thrown");
        assertEquals(NOT_FOUND_GIFT_CERTIFICATE.getErrorCode(), exception.getMessage(), "Exception message should be " + NOT_FOUND_GIFT_CERTIFICATE.getErrorCode());
    }

    @Test
    public void updateGiftCertificate_updatedGiftCertificate_whenGiftCertificateWasUpdated() {
        giftCertificateDao.updateGiftCertificate(UPDATED_CERTIFICATE);
        assertEquals(UPDATED_CERTIFICATE, giftCertificateDao.getGiftCertificate(UPDATED_CERTIFICATE.getId()),
                "Tag should be updated");
    }

    @Test
    public void saveGiftCertificate_savedGiftCertificate_whenGiftCertificateWasSaved() {
        GiftCertificate savingCertificate = NEW_GIFT_CERTIFICATE;
        giftCertificateDao.saveGiftCertificate(savingCertificate);
        savingCertificate.setId(NEW_ID);
        assertEquals(savingCertificate, giftCertificateDao.getGiftCertificate(NEW_ID), "Gift certificate should be saved with correct ID");
    }

    @Test
    public void deleteGiftCertificate_Exception_whenTryToGetDeletedGiftCertificate() {
        giftCertificateDao.deleteGiftCertificate(GIFT_CERTIFICATE_1.getId());
        DataNotFoundException exception = assertThrows(DataNotFoundException.class, () -> giftCertificateDao.getGiftCertificate(GIFT_CERTIFICATE_1.getId()),
                "Tag should be not found and  DataNotFoundException should be thrown");
        assertEquals(NOT_FOUND_GIFT_CERTIFICATE.getErrorCode(), exception.getMessage(), "Exception message should be " + NOT_FOUND_GIFT_CERTIFICATE.getErrorCode());
    }

    @Test
    public void getGiftCertificates_orderedList_whenGetListOrderByName() {
        List<GiftCertificate> giftCertificateListOrderedByName = GIFT_CERTIFICATE_LIST
                .stream()
                .sorted(Comparator.comparing(GiftCertificate::getName))
                .collect(Collectors.toList());
        assertEquals(giftCertificateListOrderedByName, giftCertificateDao.getGiftCertificates(true, false, false),
                "Actual list should be ordered by name");
    }

    @Test
    public void getGiftCertificates_orderedList_whenGetListOrderByCreatedDate() {
        List<GiftCertificate> giftCertificateListOrderedByName = GIFT_CERTIFICATE_LIST
                .stream()
                .sorted(Comparator.comparing(GiftCertificate::getCreateDate))
                .collect(Collectors.toList());
        assertEquals(giftCertificateListOrderedByName, giftCertificateDao.getGiftCertificates(false, true, false),
                "Actual list should be ordered by created date");
    }

    @Test
    public void getGiftCertificates_orderedList_whenGetListOrderByCreatedDateAndName() {
        List<GiftCertificate> giftCertificateListOrderedByName = GIFT_CERTIFICATE_LIST
                .stream()
                .sorted(Comparator.comparing(GiftCertificate::getName)
                        .thenComparing(GiftCertificate::getCreateDate))
                .collect(Collectors.toList());
        assertEquals(giftCertificateListOrderedByName, giftCertificateDao.getGiftCertificates(true, true, false),
                "Actual list should be ordered by name and created date");
    }

    @Test
    public void getGiftCertificates_orderedList_whenGetListOrderByNameDesc() {
        List<GiftCertificate> giftCertificateListOrderedByName = GIFT_CERTIFICATE_LIST
                .stream()
                .sorted(Comparator.comparing(GiftCertificate::getName).reversed())
                .collect(Collectors.toList());
        assertEquals(giftCertificateListOrderedByName, giftCertificateDao.getGiftCertificates(true, false, true),
                "Actual list should be ordered by name desc");
    }

    @Test
    public void getGiftCertificatesByTagName_certificateList_whenGetListByTagName() {
        assertEquals(GIFT_CERTIFICATE_LIST_WITH_TAG_NAME, giftCertificateDao.getGiftCertificatesByTagName(TAG_NAME,false, false, false),
                "Actual list should be filtered by tag name");
    }

    @Test
    public void getGiftCertificatesByName_certificateList_whenGetListByName() {
        List<GiftCertificate> giftCertificateListOrderedByName = GIFT_CERTIFICATE_LIST
                .stream()
                .filter(g -> g.getName().contains(NAME))
                .collect(Collectors.toList());
        assertEquals(giftCertificateListOrderedByName, giftCertificateDao.getGiftCertificatesByName(NAME,false, false, false),
                "Actual list should be filtered by name");
    }

    @Test
    public void getGiftCertificatesByDescription_certificateList_whenGetListByDescription() {
        List<GiftCertificate> giftCertificateListOrderedByName = GIFT_CERTIFICATE_LIST
                .stream()
                .filter(g -> g.getDescription().contains(DESCRIPTION))
                .collect(Collectors.toList());
        assertEquals(giftCertificateListOrderedByName, giftCertificateDao.getGiftCertificatesByDescription(DESCRIPTION,false, false, false),
                "Actual list should be filtered by description");
    }
}
