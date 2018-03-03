<%--
  Created by IntelliJ IDEA.
  User: GM
  Date: 2018/2/26
  Time: 19:56
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>RestDemo</title>
    <link href="http://cdn.bootcss.com/bootstrap/3.1.1/css/bootstrap.min.css"
    rel="stylesheet">
</head>
<body>

<div class="container">
    <div class="page-header">
        <h1>RestTest</h1>
    </div>

    <div class="panel panel-default">
         <div class="panel-heading">RestTest List</div>
    <div class="panel-body">
        <div id="test"></div>
    </div>
    </div>
</div>

<script src="http://cdn.bootcss.com/jquery/2.1.1/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/3.1.1/js/bootstrap.min.js"></script>
<script src="http://cdn.bootcss.com/handlebars.js/1.3.0/handlebars.min.js"></script>

<script type="text/x-handlebars-template" id="test_table_template">
{{#if data}}
    <table class="table table-hover" id="test_table">
        <thead>
            <tr>
                <th>ID</th>
                <th>Name</th>

            </tr>

        </thead>

        <tbody>
            {{#data}}
        <tr data-id="{{id}}" data-name="{{name}}">
            <td>{{id}}</td>
            <td>{{name}}</td>
        </tr>
        {{/data}}
        </tbody>
    </table>
{{else}}
    <div class="alert alert-warning">Can not find any data!</div>
    {{/if}}

</script>
<script>
    $(function(){
        $.ajax({
            type:'get',
            url:'http://localhost:8080/WebTest3/ws/rest/tests',
            dataType:'jsonp',
            jsonp:'_jsonp',
            jsonpCallback:'callback',
            success:function(data){
                var template = $("#test_table_template").html();
                 var render = Handlebars.compile(template);
                 var html = render({
                    data:data,
                });
                $('#test').html(html);
            }
        });
    });


</script>


</body>
</html>
