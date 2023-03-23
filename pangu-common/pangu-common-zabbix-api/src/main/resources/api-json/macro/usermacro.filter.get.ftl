{
    "jsonrpc": "2.0",
    "method": "usermacro.get",
    "params": {
        "output": [
            "macro",
            "value",
            "description"
        ],
        "hostids": "${hostid}",
        <#if macro??>
        "filter": {
            "macro": "${macro}"
        }
        </#if>
    },
    "id": 1
}
