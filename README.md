[![Build Status](https://travis-ci.org/supreme2302/TPBack.svg?branch=master)](https://travis-ci.org/supreme2302/TPBack)

https://lingvomake.ml/back/swagger-ui.html#/

<h3>Бэкенд сервера для ТП</h3>

Описание апишки:
<ul>
    <li> Admins - для работы с пользователями на Web (создателями приложения). ("/adminDTO")
    <ul>
        <li>GET /adminDTO/info - получение ирнформации о авторизованном пользователе(Эмейл)
        <li>POST /adminDTO/register - регистрация пользователя json : {email:"", password:""}
        <li>POST /adminDTO/auth - авторизация пользователя, json тот же что и в регистрации
        <li>POST /adminDTO/change -  изменение пароля пользователя , передаете строчку с новым паролем
    </ul>
    <li> Schools - для создания и администрирования приложений школ, каждой школе соответствует API.("/schoolDTO")
    <ul>
        <li>POST /schoolDTO/create - создание школы json: {name:""}
        <li>GET /schoolDTO/get - получение школы у авторизованного пользователя(ниче не надо, мы по его сессии узнаем) 
    </ul>
    P.S. Надо бы чтобы у школы могло бы быть несколько админов( при помощи промежуточной таблички, или сделав поле school_id и users не уникальной)+++ Надо сделать возможность редизайна (change)
    <li> Courses - для просмотра и редактирования созданых курсов.("courseDTO")
    <ul>
        <li>POST /courseDTO/create - создание курса json: {name: ""}(нам нужно только имя)
        <li>GET /courseDTO/ - вытащить названия и id всех курсов данной школы.
        <li>GET /courseDTO/{courseId} - получить курс по Id.
        <li>POST /courseDTO/change - изменение курса json: {name: ""}
        <li>POST /courseDTO/delete - удаление курса json: id(без ничего)
    </ul>   
    <li> Groups - для просмотра и редактирования групп ("/groupDTO/{school_id}/{course_id}")  
    <ul>
        <li>POST /groupDTO/create - создание группы у данного курса json: {name: "", courseid: "", start_date: "", curr_unit:" " - необязательно)} 
        <li>GET /groupDTO/{course_id} - вывести все группы для данного курса
        <li>GET /groupDTO/{group_id} - вывести группу по id
        <li>POST /groupDTO/delete - удаление группы у курса
        <li>POST /groupDTO/change - изменение информации о группе json: {name: "",courseid: "", start_date: "", curr_unit:" " - необязательно)}
    </ul>
    <li> Unit - для создания unitов ("/unitDTO/{course_id}"}
    <ul>
        <li>POST /unitDTO/create - создание unita у данного курса json:{name:"", course_id:"", start_date:"", position:""}
        <li>GET /unitDTO/{course_id} - вывод всех unitов данного курса                      
        <li>POST /unitDTO/change - изменение данных в юните
        <li>POST /unitDTO/delete - удаление unita у курса json как и у create
    </ul>
    <li> Students - для создания/изменения студентов ("/studentDTO/
    <ul>
        <li>GET /studentDTO - вывод всех студентов в школе
        <li>POST /studentDTO/create - создание студента json:{ email:"", name:"", surname: "", password: "", group_id: "", school_id: "", phone: ""}
        <li>GET /studentDTO/courseDTO/{course_id} - вывод студентов в курсе
        <li>GET /studentDTO/groupDTO/{group_id} - вывод студентов в группе
        <li>GET /studentDTO/{email} - вывод студента
        <li>POST /studentDTO/change - изменение данных студента json как и у create
        <li>POST /studentDTO/delete - удаление студента из школы  json{email:} 
        <li>POST /studentDTO/auth - авторизация студента json: {email:"", password:""}
    </ul>
    P.S. Поиск по фамилии студента надо бы сделать
    <li> Tasks - для создания/изменения заданий ("/taskDTO/")
    <ul>
        <li>GET /taskDTO/{unit_id} - вывод всех тасков в юните
        <li>POST taskDTO/create/ - создание нового таска json:{name:"", descripson:"",type:"",password:"", unit_id:"",task_data:""(это json внутри jsonа для создания таска, в формочке делать будем)}
        <li>GET /taskDTO/ - выводит все таски в школе
        <li>POST /taskDTO/change - изменение данных tasks json как и у create
        <li>POST /taskDTO/delete - удаление tasks из школы  json{id:""} 
    </ul>
</ul>
