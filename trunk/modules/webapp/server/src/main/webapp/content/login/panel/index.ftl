<#include "/core.ftl">

<div id="info-main">
    <div id="info-main-header">
    <@spring.message code="info.main.label"/>
    </div>
    <div id="info-main-text">
    <@spring.message code="info.main.text"/>
    </div>

    <div id="info-games">
        <div class="info-game">
            <div class="info-game-image">
                <img src="<@spring.message code="info.game.scibble.image"/>"/>
            </div>
            <div class="info-game-name">
            <@spring.message code="info.game.scibble.name"/>
            </div>
            <div class="info-game-content">
            <@spring.message code="info.game.scibble.description"/>
            </div>
            <div class="info-game-rules">
                <a href="/info/rules.html?game=scribble">
                <@spring.message code="info.game.common.rules.label"/>
                </a>
            </div>
            <div class="space-line"></div>
        </div>
    </div>
</div>
