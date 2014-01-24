<#-- @ftlvariable name="result" type="java.lang.String" -->

<style type="text/css">
    #maintain div {
        margin-bottom: 10px;
    }
</style>

<div id="maintain">
<#if result??>
    <div style="border: 1px yellow solid; width: 100%; margin-bottom: 10px">
        Last action result: ${result}.
    </div>
</#if>
    <div>
        <a href="moves">Check Moves</a>
    </div>

    <div>
        <a href="membership">Change Membership</a>
        <a href="awards">Grant Award</a>
    </div>

    <div>
        <a href="dict/flush">Flush dict changes</a>
        <a href="dict/reload">Reload dictionaries from disk</a>
    </div>

    <div>
        <a href="regenerateRatings">Regenerate All Ratings</a>
        <a href="regenerateStatistic">Regenerate All Statistic</a>
    </div>

    <div>
        <a href="clean/messages">Clean Messages</a>
    </div>

    <div>
        <a href="gc">GC</a>
    </div>
</div>