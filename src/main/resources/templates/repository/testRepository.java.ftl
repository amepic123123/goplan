package ${basePackage}.repository;

<#if modelSpecific?? && modelSpecific>
import org.springframework.data.jpa.repository.JpaRepository;
import ${basePackage}.model.${modelName};

public interface ${repositoryName}Repository extends JpaRepository<${modelName}, Long> {}
<#else>
public interface ${repositoryName}Repository {}
</#if>
