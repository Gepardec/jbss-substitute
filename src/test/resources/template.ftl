USER=${SOME_USER!"unknown"}
PASSWORD=${SOME_PASSWORD!"unknown"}
<#list SERVER?keys as server>
HOST_${server}=${SERVER[server]}
</#list>