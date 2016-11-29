<#ftl output_format="HTML">

<#macro body_class></#macro>
<#macro body_content></#macro>

<#macro footer></#macro>

<#macro all_page_content>
<!DOCTYPE html>
    <html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=utf-8" />
        <link href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-BVYiiSIFeK1dGmJRAkycuHAHRg32OmUcww7on3RYdg4Va+PmSTsz/K68vbdEjh4u" crossorigin="anonymous">
        <link href="${urlHelper.path("/static/css/app.css")}" rel="stylesheet">
        <script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
        <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js" integrity="sha384-Tc5IQib027qvyjSMfHjOMaLkfuWVxZxUPnCJA7l2mCWNIpG9mGCD8wGNIcPD7Txa" crossorigin="anonymous"></script>
        <script src="${urlHelper.path("/static/js/app.js")}"></script>
    </head>
    <body class="<@body_class/>">
        <@body_content/>
        <@footer/>
    </body>
    </html>
</#macro>
