package ${basePackage}.dto;

<#if imports?? && imports?size gt 0>
<#list imports as imp>
import ${imp};
</#list>
</#if>
public class ${modelName}Dto {

    private Long id;
<#if properties??>
<#list properties as p>

    private ${p.type} ${p.name};
</#list>
</#if>

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
<#if properties??>
<#list properties as p>

    public ${p.type} get${p.name?cap_first}() {
        return ${p.name};
    }

    public void set${p.name?cap_first}(${p.type} ${p.name}) {
        this.${p.name} = ${p.name};
    }
</#list>
</#if>
}
