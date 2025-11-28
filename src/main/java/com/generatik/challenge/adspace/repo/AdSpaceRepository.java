package com.generatik.challenge.adspace.repo;

import com.generatik.challenge.adspace.model.AdSpaceEntity;
import com.generatik.challenge.adspace.model.AdSpaceStatus;
import com.generatik.challenge.adspace.model.AdSpaceType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdSpaceRepository extends JpaRepository<AdSpaceEntity, Long> {
    List<AdSpaceEntity> findByStatus(AdSpaceStatus status);
    List<AdSpaceEntity> findByStatusAndType(AdSpaceStatus status, AdSpaceType type);
    List<AdSpaceEntity> findByStatusAndCity(AdSpaceStatus status, String city);
    List<AdSpaceEntity> findByStatusAndTypeAndCity(AdSpaceStatus status, AdSpaceType type, String city);
}

