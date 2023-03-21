{
    "jsonrpc": "2.0",
    "method": "trigger.create",
    "params": {
        "description": "${triggerName}",
        "expression": "${expression}",
        "priority" : ${ruleLevel},
        "manual_close":1,
        "tags": [
        <#if tagMap??>
            <#list tagMap? keys as key>
                {
                "tag": "${key}",
                "value": "${tagMap[key]}"
                }<#if key_has_next>,</#if>
            </#list>
        </#if>
        ]
    },
    "auth": "${userAuth}",
    "id": 1
}
