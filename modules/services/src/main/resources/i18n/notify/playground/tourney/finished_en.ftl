<#-- @ftlvariable name="context.tourney" type="wisematches.playground.tourney.regular.Tourney" -->
<#-- @ftlvariable name="context.division" type="wisematches.playground.tourney.regular.TourneyDivision" -->
<#-- @ftlvariable name="context.place" type="wisematches.playground.tourney.TourneyPlace" -->
<#import "../../utils.ftl" as util>

<p>
    Congratulations, you have
    won ${context.tourney.number?string}${gameMessageSource.getNumeralEnding(context.tourney.number, locale)}
    WiseMatches Tourney.
</p>

<p>
    After all obstacles you
    have ${gameMessageSource.getMessage("tourney.place." + context.place.place + ".label", locale)} in
${gameMessageSource.getMessage("tourney.section." + context.division.section.name()?lower_case + ".label", locale)}
    section.
</p>

<p>
    Please note that appropriate record was added to list of all your
    awards: <@util.link href="playground/profile/awards?p=${principal.id}"/>
</p>

<p>
    Please don't forget to subscribe to next WiseMatches tourney: <@util.link href="playground/tourney"/>.
</p>