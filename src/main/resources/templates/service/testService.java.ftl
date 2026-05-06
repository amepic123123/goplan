package ${basePackage}.service;

<#if modelSpecific?? && modelSpecific>
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import ${basePackage}.dto.${modelName}Dto;
import ${basePackage}.mapper.${modelName}Mapper;
import ${basePackage}.model.${modelName};
import ${basePackage}.repository.${modelName}Repository;

@Service
public class ${serviceName}Service {

    private final ${modelName}Repository repository;
    private final ${modelName}Mapper mapper;

    public ${serviceName}Service(${modelName}Repository repository, ${modelName}Mapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    public ${modelName}Dto save(${modelName}Dto dto) {
        ${modelName} model = mapper.toModel(dto);
        ${modelName} saved = repository.save(model);
        return mapper.toDto(saved);
    }

    public ${modelName}Dto findById(Long id) {
        Optional<${modelName}> model = repository.findById(id);
        return model.map(mapper::toDto).orElse(null);
    }

    public List<${modelName}Dto> findAll() {
        return repository.findAll().stream()
            .map(mapper::toDto)
            .toList();
    }
}
<#else>
import org.springframework.stereotype.Service;

@Service
public class ${serviceName}Service {
}
</#if>
