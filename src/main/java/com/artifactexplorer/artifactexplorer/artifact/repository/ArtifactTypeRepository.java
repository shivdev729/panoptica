package com.artifactexplorer.artifact.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.artifactexplorer.artifact.entity.ArtifactType;
import java.util.List;

// artifact/repository/ArtifactTypeRepository.java
@Repository
public interface ArtifactTypeRepository extends JpaRepository<ArtifactType, String> {
    List<ArtifactType> findByParentTypeIsNull();           // top-level categories
    List<ArtifactType> findByParentType_TypeId(String parentTypeId);
}
