<#-- @ftlvariable name="context" type="wisematches.playground.propose.ChallengeGameProposal" -->
<#import "../../utils.ftl" as notify>

<p>
    Your challenge #${context.id} "${context.gameSettings.title!""}" was rejected by player or by timeout.
</p>
<p>
    If you don't know the player well we don't recommend you send new challenge.
</p>