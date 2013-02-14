<#-- @ftlvariable name="context.award" type="wisematches.server.services.award.Award" -->
<#-- @ftlvariable name="context.player" type="wisematches.core.Personality" -->
<#-- @ftlvariable name="context.descriptor" type="wisematches.server.services.award.AwardDescriptor" -->
<#import "../../utils.ftl" as notify>

<p>
    Поздравляем!
</p>

<p>
    За свои успехи вы получили новую награду
    <strong>"${messageSource.getMessage("awards." + context.award.code+ ".label", locale)}"</strong>
<#if !context.descriptor.type.ribbon>
    достоинством
    <strong>${messageSource.getMessage("awards." + context.award.weight.name()?lower_case + ".label", locale)}</strong></#if>
    .
</p>

<p>
    Список всех выших наград вы можете посмотреть
    в <@notify.link "playground/profile/awards?p=${context.player.id?string}">своем
    профайле</@notify.link>.
</p>