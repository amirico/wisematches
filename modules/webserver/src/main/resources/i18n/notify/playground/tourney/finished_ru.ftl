<#-- @ftlvariable name="context.tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="context.division" type="wisematches.playground.tourney.regular.TourneyDivision" -->
<#-- @ftlvariable name="context.place" type="wisematches.playground.tourney.TourneyPlace" -->
<#import "../../utils.ftl" as util>

<p>
    Поздравляем, вы
    выйграли ${context.tourney.number?string}${gameMessageSource.getNumeralEnding(context.tourney.number, locale)}
    Турнир WiseMatches.
</p>

<p>
    После всех препятствий вы
    заняли ${gameMessageSource.getMessage("tourney.place." + context.place.place + ".label", locale)}
    в категории
${gameMessageSource.getMessage("tourney.section." + context.division.section.name()?lower_case + ".label", locale)}.
</p>

<p>
    Обращаем ваше внимание, что соответсвующая запись была добавлена в список ваших
    наград: <@util.link href="playground/profile/awards?p=${principal.id}"/>.
</p>

<p>
    Пожалуйста, не забудьте подписаться на участи в следующем турнире
    WiseMatches: <@util.link href="playground/tourney"/>.
</p>