package com.epam.esm.repository;

import com.epam.esm.model.GiftCertificate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface GiftCertificateRepository extends JpaRepository<GiftCertificate, Long> {

    Page<GiftCertificate> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCaseAndTagList_Name(
            String name, String description, String tagName, Pageable pageable);

    Page<GiftCertificate> findByNameContainingIgnoreCaseAndDescriptionContainingIgnoreCase(
            String name, String description, Pageable pageable);
}