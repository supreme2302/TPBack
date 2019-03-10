[![Build Status](https://travis-ci.org/supreme2302/TPBack.svg?branch=master)](https://travis-ci.org/supreme2302/TPBack)

<h3>Бэкенд сервера для ТП</h3>

Описание апишки:
<ul>
    <li> Admins - для работы с пользователями на Web (создателями приложения). ("/users")
    <ul>
        <li>GET /admins/info - получение ирнформации о авторизованном пользователе(Эмейл)
        <li>POST /admins/register - регистрация пользователя json : {email:"", pasword:""}
        <li>POST /admins/auth - авторизация пользователя, json тот же что и в регистрации
        <li>POST /admins/change -  изменение пароля пользователя , передаете строчку с новым паролем
    </ul>
    <li> Schools - для создания и администрирования приложений школ, каждой школе соответствует API.("/schools")
    <ul>
        <li>POST /schools/create - создание школы json: {name:""}
        <li>GET /schools/get - получение школы у авторизованного пользователя(ниче не надо, мы по его сессии узнаем) 
    </ul>
    P.S. Надо бы чтобы у школы могло бы быть несколько админов( при помощи промежуточной таблички, или сделав поле school_id и users не уникальной)
    <li> Courses - для просмотра и редактирования созданых курсов.("courses")
    <ul>
        <li>POST /courses/create - создание курса json: {name: ""}(нам нужно только имя)
        <li>GET /courses/ - вытащить названия и id всех курсов данной школы.
        <li>POST /courses/delete - удаление курса json: {name: ""}
    </ul>   
    <li> Groups - для просмотра и редактирования групп ("/groups/{school_id}/{course_id}")  
    <ul>
        <li>POST /groups/newgroup - создание группы у данного курса json: {name: "", courseid: "", start_date: "", curr_unit:" " - необязательно)} 
        <li>GET /groups/{course_id} - вывести все группы для данного курса
        <li>POST /groups/delete - удаление группы у курса
        <li>POST /groups/change - изменение информации о группе json: {name: "",courseid: "", start_date: "", curr_unit:" " - необязательно)}
    </ul>
    <li> Unit - для создания unitов ("/units/{course_id}"}
    <ul>
        <li>POST /units/create - создание unita у данного курса json:{name:"", course_id:"", start_date:"", position:""}
        <li>GET /units/{course_id} - вывод всех unitов данного курса                      
        <li>POST /units/change - изменение данных в юните
        <li>POST /units/delete - удаление unita у курса json как и у create
    </ul>
    <li> Students - для создания/изменения студентов ("/students/
    <ul>
        <li>GET /students - вывод всех студентов в школе
        <li>POST /students/create - создание студента json:{ email:"", name:"", surname: "", password: "", group_id: "", school_id: "", phone: ""}
        <li>GET /students/course/{course_id} - вывод студентов в курсе
        <li>GET /students/group/{group_id} - вывод студентов в группе
        <li>POST /students/change - изменение данных студента json как и у create
        <li>POST /students/delete - удаление студента из школы  json{email:} 
        <li>POST /students/login - авторизация студента json: {email:"", password:""}
    </ul>
    P.S. Поиск по фамилии студента надо бы сделать
    <li> Tasks - для создания/изменения заданий ("/tasks/")
    <ul>
        <li>GET /tasks/{unit_id} - вывод всех тасков в юните
        <li>POST /create/ - создание нового таска json:{
    </ul>
</ul>