{
    "jsonrpc": "2.0",
    "method": "usermacro.create",
    "params": {
        "hostid": "${hostid}",
        "macro": "${macro}",
        "value": "${value}"
        <#if description??>
            ,"description":"${description}"
        </#if>
    },
    "id": 1
}
