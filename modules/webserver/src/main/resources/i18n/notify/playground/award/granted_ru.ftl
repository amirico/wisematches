<#-- @ftlvariable name="context.award" type="wisematches.playground.award.Award" -->
<#-- @ftlvariable name="context.player" type="wisematches.personality.Personality" -->
<#-- @ftlvariable name="context.descriptor" type="wisematches.playground.award.AwardDescriptor" -->
<#import "/spring.ftl" as spring/>
<#import "../../utils.ftl" as notify>

<p>
    Поздравляем!
</p>

<p>
    За свои успехи вы получили новую награду
    <strong>"${gameMessageSource.getMessage("awards." + context.award.code+ ".label", locale)}"</strong>
<#if !context.descriptor.type.ribbon>
    достоинством
    <strong>${gameMessageSource.getMessage("awards." + context.award.weight.name()?lower_case + ".label", locale)}</strong></#if>
    .
</p>

<p>
    Список всех выших наград вы можете посмотреть
    в <@notify.link "playground/profile/awards?p=${context.player.id?string}">своем
    профайле</@notify.link>.
</p>