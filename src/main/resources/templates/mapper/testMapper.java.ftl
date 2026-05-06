package ${basePackage}.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ${basePackage}.dto.${modelName}Dto;
import ${basePackage}.model.${modelName};

@Mapper(componentModel = "spring")
public interface ${modelName}Mapper {
    ${modelName}Mapper INSTANCE = Mappers.getMapper(${modelName}Mapper.class);

    ${modelName}Dto toDto(${modelName} model);
    ${modelName} toModel(${modelName}Dto dto);
}
